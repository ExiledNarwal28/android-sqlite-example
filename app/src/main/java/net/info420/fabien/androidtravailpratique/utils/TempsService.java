package net.info420.fabien.androidtravailpratique.utils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Tache;

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
public class TempsService extends Service {
  public static final String TAG = TempsService.class.getName();

  public static final String NOTIFICATION  = TempsReceiver.class.getCanonicalName();
  public static final String TASKS_COUNT   = "tasksCount";
  public static final String TIMESPAN      = "timespan";
  public static final String URGENCY_LEVEL = "urgencyLevel";

  private boolean           toastsEnabled    = true;
  private String            selection        = null;
  private ArrayList<String> selectionArgs    = new ArrayList<>();
  private int               frequency        = 0;
  private String            timespanText     = null;
  private String            urgencyLevelText = null;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, NOTIFICATION);

    getInfoFromPrefs();

    // Si la fréquence est "jamais", on ne veut pas starter de Timer
    if (frequency == 0) {
      return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
    }

    if (toastsEnabled) {
      Timer timer         = new Timer();
      TimerTask timeTask  = new TimeTask();

      timer.scheduleAtFixedRate(timeTask, 0, frequency * 1000); // Ceci s'exécute, puis attend X secondes avant de continuer le programme
    }

    return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  // Aller chercher les informations depuis les préférences
  // Source : http://stackoverflow.com/questions/21820031/getting-value-from-edittext-preference-in-preference-screen
  private void getInfoFromPrefs() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    // Infos à aller chercher : Booléen pour l'activation des toasts
    //                          Laps de temps (aujourd'hui, semaine, mois)
    //                          Niveau d'urgence minimum
    //                          Fréquence des notifications

    // On va chercher le booléen d'activation des toasts
    toastsEnabled = prefs.getBoolean(TodoApplication.PREFS_TOASTS, true);

    // On va chercher la fréquence
    frequency = Integer.parseInt(prefs.getString(TodoApplication.PREFS_TOASTS_FREQUENCE, "600"));

    // On vide les arguments de sélection
    selectionArgs.removeAll(selectionArgs);

    // Laps de temps
    // switch (getPrefs("timespanText") {
    switch (Integer.parseInt(prefs.getString(TodoApplication.PREFS_TOASTS_LAPS_TEMPS, "1"))) {
      case 0:
        // Aujourd'hui

        selection = "(" + Tache.KEY_date + " =?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDate().toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).getMillis() / 10000));

        // timespanText = getPrefs("timespanText") en texte
        timespanText = getString(R.string.info_to_do_this_day);
        break;
      case 1:
        // Semaine

        selection = "(" + Tache.KEY_date + " BETWEEN ? AND ?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        timespanText = getString(R.string.info_to_do_this_week);
        break;
      case 2:
        // Mois

        selection = "(" + Tache.KEY_date + " BETWEEN ? AND ?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMinimumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMaximumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        timespanText = getString(R.string.info_to_do_this_month);
        break;
    }

    // On va ensuite chercher le texte qui décrit le niveau d'urgence
    switch (Integer.parseInt(prefs.getString(TodoApplication.PREFS_TOASTS_URGENCE, Integer.toString(0)))) {
      case 0:
        urgencyLevelText = getString(R.string.info_urgency_low_and_plus);
        break;
      case 1:
        urgencyLevelText = getString(R.string.info_urgency_medium_and_plus);
        break;
      case 2:
        urgencyLevelText = getString(R.string.info_urgency_high);
        break;
    }

    // selectionArgs.add(getPrefs("urgencyLevel");
    selectionArgs.add(prefs.getString(TodoApplication.PREFS_TOASTS_URGENCE, Integer.toString(0))); // Ajout du niveau d'urgence minimum pour recevcoir les Toasts
    selectionArgs.add(Integer.toString(0)); // Ajout du niveau de complétion (tâches non-complétées)

    // Pour afficher un petit descriptif du genre : (bas et +)
  }

  // Retourne le nombre de tâches en fonction du niveau d'urgence minimum et de la période de temps
  public int getTasksCount() {
    Cursor cursor = getContentResolver().query( TodoContentProvider.CONTENT_URI_TACHE,
                                                new String[] { Tache.KEY_ID },
                                                selection,
                                                selectionArgs.toArray(new String[selectionArgs.size()]),
                                                null);

    return (cursor != null) ? cursor.getCount() : 0;
  }

  // Ce qui sera exécuté chaque X seconde
  class TimeTask extends TimerTask {
    public void run() {
      Intent timeIntent = new Intent(NOTIFICATION);

      timeIntent.putExtra(TASKS_COUNT,    getTasksCount()); // Nombre de tâche
      timeIntent.putExtra(TIMESPAN,       timespanText);      // Texte en lien avec la période de temps
      timeIntent.putExtra(URGENCY_LEVEL,  urgencyLevelText);      // Texte en lien avec le niveau d'urgence

      sendBroadcast(timeIntent);
    }
  }
}