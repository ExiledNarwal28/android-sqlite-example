package net.info420.fabien.androidtravailpratique.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by fabien on 17-04-24.
 */

// Source : http://www.vogella.com/tutorials/AndroidServices/article.html
public class TimeReceiver extends BroadcastReceiver {
  private final static String TAG = TimeReceiver.class.getName();

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "WHAT");

    if(intent.getAction().equals("time")){
      // TODO : Changer ca
      String state = intent.getExtras().getString("extra");
    }
  }
}