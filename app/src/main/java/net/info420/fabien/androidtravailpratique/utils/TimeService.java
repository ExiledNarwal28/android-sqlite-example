package net.info420.fabien.androidtravailpratique.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by fabien on 17-04-24.
 */

// Source : http://www.vogella.com/tutorials/AndroidServices/article.html
public class TimeService extends Service {

  // TODO : Trouver un moyen pour faire starté le service a toutes les X secondes
  // TODO : Vérifier quand la date est proche
  // TODO : Marcher avec des prefs
  // TODO : Caller le Broadcast Receiver
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}