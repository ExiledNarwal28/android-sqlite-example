package net.info420.fabien.androidtravailpratique.common;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.utils.DBHelper;

import java.util.Date;

/**
 * Created by fabien on 17-03-26.
 */

public class TaskerApplication extends Application {
  private static final String TAG = TaskerApplication.class.getName();

  // TODO : Enlever ceci si ça sert à rien.
  public SQLiteDatabase database;
  public DBHelper dbHelper;

  @Override
  public void onCreate() {
    super.onCreate();

    // TODO : Enlever dès que la base de données fonctionne

    // Recréation de la bd
    dbHelper = new DBHelper(this);
    dbHelper.recreateDB(dbHelper.getWritableDatabase());
  }

  // TODO : Vérifier si c'est utile
  public String getUrgencyLevel(int urgencyLevel) {
    Log.d(TAG, "text : " + Integer.toString(urgencyLevel));
    switch (urgencyLevel) {
      case 0:
        return getString(R.string.task_urgency_level_low);
      case 1:
        return getString(R.string.task_urgency_level_medium);
      case 2:
        return getString(R.string.task_urgency_level_high);
      default:
        return getString(R.string.error);
    }
  }

  // TODO : Vérifier si c'est utile
  public int getUrgencyLevelColor(int urgencyLevel) {
    Log.d(TAG, "color : " + Integer.toString(urgencyLevel));
    switch (urgencyLevel) {
      case 0:
        return getColor(R.color.colorUrgencyLevelLow);
      case 1:
        return getColor(R.color.colorUrgencyLevelMedium);
      case 2:
        return getColor(R.color.colorUrgencyLevelHigh);
      default:
        return getColor(R.color.colorPrimaryText);
    }
  }

  // TODO : Vérifier si c'est utile
  // TODO : Vérifier l'année
  // Source : http://stackoverflow.com/questions/13005116/android-convert-unix-time-to-gmt-time#13005144
  public String getDate(int unixDate) {
    Date d = new Date(unixDate);
    SimpleDateFormat f = new SimpleDateFormat("d MMMM YY"); // Dimanche 1 janvier 1970
    f.setTimeZone(TimeZone.getTimeZone("GMT"));
    return f.format(d);
  }

  // TODO : Vérifier si c'est utile
  // TODO : Vérifier l'année
  // Source : http://stackoverflow.com/questions/13005116/android-convert-unix-time-to-gmt-time#13005144
  public String getFullDate(int unixDate) {
    Date d = new Date(unixDate);
    SimpleDateFormat f = new SimpleDateFormat("EEEE d MMMM YYYY"); // Dimanche 1 janvier 1970
    f.setTimeZone(TimeZone.getTimeZone("GMT"));
    return f.format(d);
  }
}
