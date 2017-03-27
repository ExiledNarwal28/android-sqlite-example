package net.info420.fabien.androidtravailpratique.common;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import net.info420.fabien.androidtravailpratique.utils.DBHelper;

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
}
