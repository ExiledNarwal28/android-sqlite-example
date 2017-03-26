package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fabien on 17-03-23.
 */

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

public class DBHelper  extends SQLiteOpenHelper {
  // Version de la base de donnée, qui doit être augmenté à chaque changement dans les tables
  private static final int DATABASE_VERSION = 1;

  // Nom de la base de données
  private static final String DATABASE_NAME = "royf_android.db";

  public DBHelper(Context context ) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Tout le nécéssaire pour construire les différentes tables

    String CREATE_TABLE_EMPLOYEE      = "CREATE TABLE " + Employee.TABLE + "("
      + Employee.KEY_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
      + Employee.KEY_name             + " TEXT, "
      + Employee.KEY_job              + " TEXT, "
      + Employee.KEY_email            + " TEXT, "
      + Employee.KEY_phone            + " TEXT )";

    String CREATE_TABLE_TASK          = "CREATE TABLE " + Task.TABLE + "("
      + Task.KEY_ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
      + Task.KEY_assigned_employee_ID + " INTEGER "
      + Task.KEY_name                 + " TEXT, "
      + Task.KEY_description          + " TEXT, "
      + Task.KEY_completed            + " INTEGER, " // Dans SQLite, les booleans sont des integers 0 ou 1
      + Task.KEY_date                 + " INTEGER, "
      + Task.KEY_urgency_level        + " INTEGER )";

    db.execSQL(CREATE_TABLE_EMPLOYEE);
    db.execSQL(CREATE_TABLE_TASK);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    recreateDB(db);
  }

  public void recreateDB(SQLiteDatabase db) {
    // Drop de toutes les tables
    db.execSQL("DROP TABLE IF EXISTS " + Employee.TABLE);
    db.execSQL("DROP TABLE IF EXISTS " + Task.TABLE);

    // Recréer les tables
    onCreate(db);
  }
}