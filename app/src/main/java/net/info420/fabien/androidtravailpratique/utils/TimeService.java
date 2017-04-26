package net.info420.fabien.androidtravailpratique.utils;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.Preference;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fabien on 17-04-24.
 */

// Source : http://www.vogella.com/tutorials/AndroidServices/article.html
public class TimeService extends Service implements Preference.OnPreferenceChangeListener {
  public static final String TAG = TimeService.class.getName();

  public static final String NOTIFICATION = TimeReceiver.class.getCanonicalName();
  public static final String TASKS_COUNT  = "tasksCount";
  public static final String TO_DO_THIS_X = "toDoThisX";

  private String            selection     = null;
  private ArrayList<String> selectionArgs = new ArrayList<>();
  private String            toDoThisX     = null;

  // TODO : Trouver un moyen pour faire starté le service a toutes les X secondes
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, NOTIFICATION);

    getInfoFromPrefs();

    Timer timer         = new Timer();
    TimerTask timeTask  = new TimeTask();

    timer.scheduleAtFixedRate(timeTask, 0, 3000); // Ceci s'exécute, puis attend X secondes avant de continuer le programme

    return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  // TODO : Restarter le service lorsque les préférences changes -> Dans MainActivity, plutôt?
  @Override
  public boolean onPreferenceChange(Preference preference, Object o) {
    return false;
  }

  // Les opérations du service executés à chaque fréquence
  private void executeService() {
  }

  private void getInfoFromPrefs() {
    // TODO : Aller chercher les infos du toasts avec les préférences

    // Infos à aller chercher : Laps de temps (aujourd'hui, semaine, mois)
    //                          Niveau d'urgence minimum

    // On vide les arguments de sélection
    selectionArgs.removeAll(selectionArgs);

    // Laps de temps
    // switch (getPrefs("timespan") {
    switch (2) {
      case 0:
        // Aujourd'hui

        selection = "(" + Task.KEY_date + " =?) AND (" + Task.KEY_urgency_level + " >=?) AND (" + Task.KEY_completed + " =?)";

        selectionArgs.add(Long.toString(new LocalDate().toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).getMillis() / 10000));

        // toDoThisX = getPrefs("timespan") en texte
        toDoThisX = getString(R.string.info_to_do_this_day);
        break;
      case 1:
        // Semaine

        selection = "(" + Task.KEY_date + " BETWEEN ? AND ?) AND (" + Task.KEY_urgency_level + " >=?) AND (" + Task.KEY_completed + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        toDoThisX = getString(R.string.info_to_do_this_week);
        break;
      case 2:
        // Mois

        selection = "(" + Task.KEY_date + " BETWEEN ? AND ?) AND (" + Task.KEY_urgency_level + " >=?) AND (" + Task.KEY_completed + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMinimumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMaximumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        toDoThisX = getString(R.string.info_to_do_this_month);
        break;
    }

    // selectionArgs.add(getPrefs("urgencyLevel");
    selectionArgs.add(Integer.toString(0)); // Ajout du niveau d'urgence minimum pour recevcoir les Toasts
    selectionArgs.add(Integer.toString(0)); // Ajout du niveau de complétion (tâches non-complétées)
  }

  // Retourne le nombre de tâches en fonction du niveau d'urgence minimum et de la période de temps
  public int getTasksCount() {
    Cursor cursor = getContentResolver().query( TaskerContentProvider.CONTENT_URI_TASK,
                                                new String[] { Task.KEY_ID },
                                                selection,
                                                selectionArgs.toArray(new String[selectionArgs.size()]),
                                                null);

    return (cursor != null) ? cursor.getCount() : 0;
  }

  class TimeTask extends TimerTask {
    public void run() {
      Intent timeIntent = new Intent(NOTIFICATION);
      timeIntent.putExtra(TASKS_COUNT, getTasksCount()); // Nombre de tâche
      timeIntent.putExtra(TO_DO_THIS_X, toDoThisX);      // Texte en lien avec la période de temps
      sendBroadcast(timeIntent);
    }
  }
}