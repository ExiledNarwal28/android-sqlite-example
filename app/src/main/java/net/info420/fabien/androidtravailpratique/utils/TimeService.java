package net.info420.fabien.androidtravailpratique.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-24.
 */

// Source : http://www.vogella.com/tutorials/AndroidServices/article.html
public class TimeService extends Service {
  public static final String TAG = TimeService.class.getName();

  public static final String NOTIFICATION = TimeReceiver.class.getCanonicalName();
  public static final String TASKS_COUNT  = "tasksCount";
  public static final String TO_DO_THIS_X = "toDoThisX";

  // TODO : Trouver un moyen pour faire starté le service a toutes les X secondes
  // TODO : Vérifier quand la date est proche
  // TODO : Marcher avec des prefs
  // TODO : Caller le Broadcast Receiver
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, NOTIFICATION);

    Intent timeIntent = new Intent(NOTIFICATION);
    timeIntent.putExtra(TASKS_COUNT, getTasksCount());
    timeIntent.putExtra(TO_DO_THIS_X, getString(R.string.info_to_do_this_week));
    sendBroadcast(timeIntent);

    return Service.START_NOT_STICKY; // Ceci permet de ne pas redémarrer le service s'il est terminé
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public int getTasksCount() {

    return 0;
  }

}