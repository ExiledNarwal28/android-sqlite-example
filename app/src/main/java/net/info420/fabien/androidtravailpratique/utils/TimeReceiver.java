package net.info420.fabien.androidtravailpratique.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-24.
 */

// Source : http://www.vogella.com/tutorials/AndroidServices/article.html
public class TimeReceiver extends BroadcastReceiver {
  private final static String TAG = TimeReceiver.class.getName();

  @Override
  public void onReceive(Context context, Intent intent) {
    if(intent.getExtras() != null) {
      // TODO : Changer ca

      if (intent.getExtras().getInt(TimeService.TASKS_COUNT) == 0) {
        Toast.makeText( context,
                        String.format("%s %s %s.",
                                      context.getString(R.string.info_you_have_no),
                                      context.getString(R.string.task).toLowerCase(),
                                      intent.getExtras().getString(TimeService.TO_DO_THIS_X)),
                        Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText( context,
                        String.format("%s %s %s %s.",
                                      context.getString(R.string.info_you_have),
                                      intent.getExtras().getInt(TimeService.TASKS_COUNT),
                                      ((intent.getExtras().getInt(TimeService.TASKS_COUNT) > 1) ? context.getString(R.string.tasks) : context.getString(R.string.task)).toLowerCase(),
                                      intent.getExtras().getString(TimeService.TO_DO_THIS_X)),
                        Toast.LENGTH_SHORT).show();
      }
    }
  }
}