package net.info420.fabien.androidtravailpratique.application;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.DBHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by fabien on 17-03-26.
 */

// TODO : Ajouter l'organisation des fichiers dans la doc http://blog.smartlogic.io/2013-07-09-organizing-your-android-development-code-structure/

public class TaskerApplication extends Application {
  private static final String TAG = TaskerApplication.class.getName();

  public static final String PREFS_TOASTS               = "toasts";
  public static final String PREFS_TOASTS_FREQUENCY     = "toasts_frequency";
  public static final String PREFS_TOASTS_TIMESPAN      = "toasts_timespan";
  public static final String PREFS_TOASTS_URGENCY_LEVEL = "toasts_urgency_level";
  public static final String PREFS_LANGUAGE             = "language";

  // TODO : Enlever ceci si ça sert à rien.
  public SQLiteDatabase database;
  public DBHelper dbHelper;

  public boolean recreateDB         = false;
  public boolean writeTestTasks     = false;
  public boolean writeTestEmployees = false;

  @Override
  public void onCreate() {
    super.onCreate();

    // TODO : Enlever dès que la base de données fonctionne

    // Recréation de la bd
    dbHelper = new DBHelper(this);

    if (recreateDB) {
      dbHelper.recreateDB(dbHelper.getWritableDatabase());
    }
  }

  // TODO : Vérifier si c'est utile
  // TODO : Comment rendre ça statique?
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
  // TODO : Comment rendre ça statique?
  public int getUrgencyLevelColor(int urgencyLevel) {
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

  public static String getDate(int unixDate) {
    return DateTimeFormat.forPattern("EEEE d MMM").print(new DateTime().withMillis(unixDate * 10000L));
  }

  public static String getFullDate(int unixDate) {
    return DateTimeFormat.forPattern("EEEE d MMMM yyyy").print(new DateTime().withMillis(unixDate * 10000L));
  }

  public void setStatusBarColor(Activity activity) {
    // Source : http://stackoverflow.com/questions/22192291/how-to-change-the-status-bar-color-in-android
    // Changer la couleur de la status bar

    Window window = activity.getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
  }
}
