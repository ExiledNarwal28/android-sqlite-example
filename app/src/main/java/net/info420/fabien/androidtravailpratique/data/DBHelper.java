package net.info420.fabien.androidtravailpratique.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

/**
 * Created by fabien on 17-03-23.
 */

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

public class DBHelper  extends SQLiteOpenHelper {
  // Version de la base de donnée, qui doit être augmenté à chaque changement dans les tables
  private static final int DATABASE_VERSION = 4;

  // Nom de la base de données
  private static final String DATABASE_NAME = "royf_android.db";

  public DBHelper(Context context ) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Tout le nécéssaire pour construire les différentes tables

    String CREATE_TABLE_EMPLOYEE      = "CREATE TABLE " + Employe.TABLE + "("
      + Employe.KEY_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
      + Employe.KEY_nom + " TEXT, "
      + Employe.KEY_poste + " TEXT, "
      + Employe.KEY_email            + " TEXT, "
      + Employe.KEY_telephone + " TEXT )";

    String CREATE_TABLE_TASK          = "CREATE TABLE " + Tache.TABLE + "("
      + Tache.KEY_ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
      + Tache.KEY_employe_assigne_ID + " INTEGER, "
      + Tache.KEY_nom + " TEXT, "
      + Tache.KEY_description          + " TEXT, "
      + Tache.KEY_fait + " INTEGER, " // Dans SQLite, les booleans sont des integers 0 ou 1
      + Tache.KEY_date                 + " INTEGER, "
      + Tache.KEY_urgence + " INTEGER )";

    db.execSQL(CREATE_TABLE_EMPLOYEE);
    db.execSQL(CREATE_TABLE_TASK);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    recreateDB(db);
  }

  public void recreateDB(SQLiteDatabase db) {
    // Drop de toutes les tables
    db.execSQL("DROP TABLE IF EXISTS " + Employe.TABLE);
    db.execSQL("DROP TABLE IF EXISTS " + Tache.TABLE);

    // Recréer les tables
    onCreate(db);
  }
}