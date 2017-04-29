package net.info420.fabien.androidtravailpratique.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

/**
 * {@link SQLiteOpenHelper} afin de construire la base de données et de la gèrer
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-23
 *
 * @see Employe
 * @see Tache
 * @see TodoContentProvider
 * @see SQLiteOpenHelper
 *
 * {@link <a href="http://instinctcoder.com/android-studio-sqlite-database-example/">Base de données SQLite et Android</a>}
 */
public class DBHelper  extends SQLiteOpenHelper {
  // Version de la base de donnée, qui doit être augmenté à chaque changement dans les tables
  private static final int DB_VERSION = 5;

  // Nom de la base de données
  private static final String DB_NOM = "royf_android.db";

  /**
   * Constructeur du {@link SQLiteOpenHelper}
   *
   * @param context  {@link Context} appelant le {@link DBHelper}
   */
  public DBHelper(Context context ) {
    super(context, DB_NOM, null, DB_VERSION);
  }

  /**
   * Crée la base de données
   *
   * Crée les tables d'employés et de taĉhes
   *
   * @param db  Base de données à créer
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    // Tout le nécéssaire pour construire les différentes tables

    String CREATE_TABLE_EMPLOYE = "CREATE TABLE " + Employe.TABLE                 + "("
                                                  + Employe.KEY_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                                                  + Employe.KEY_nom               + " TEXT, "
                                                  + Employe.KEY_poste             + " TEXT, "
                                                  + Employe.KEY_email             + " TEXT, "
                                                  + Employe.KEY_telephone         + " TEXT )";

    String CREATE_TABLE_TACHE   = "CREATE TABLE " + Tache.TABLE                   + "("
                                                  + Tache.KEY_ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                                                  + Tache.KEY_employe_assigne_ID  + " INTEGER, "
                                                  + Tache.KEY_nom                 + " TEXT, "
                                                  + Tache.KEY_description         + " TEXT, "
                                                  + Tache.KEY_fait                + " INTEGER, " // Dans SQLite, les booleans sont des integers 0 ou 1
                                                  + Tache.KEY_date                + " INTEGER, "
                                                  + Tache.KEY_urgence             + " INTEGER )";

    db.execSQL(CREATE_TABLE_EMPLOYE);
    db.execSQL(CREATE_TABLE_TACHE);
  }

  /**
   * Recrée la base de données si on augmente la version
   *
   * @param db                Base de données à mettre à jour
   * @param vieilleVersion    Numéro de la vieille version
   * @param nouvelleVersion   Numéro de la nouvelle version
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int vieilleVersion, int nouvelleVersion) {
    recreateDB(db);
  }

  /**
   * Recrée la base de données
   *
   * Drop les tables
   * Crée la base de données
   *
   * @param db Base de données à recréer
   */
  public void recreateDB(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + Employe.TABLE);
    db.execSQL("DROP TABLE IF EXISTS " + Tache.TABLE);

    onCreate(db);
  }
}