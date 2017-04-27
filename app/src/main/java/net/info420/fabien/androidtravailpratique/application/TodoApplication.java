package net.info420.fabien.androidtravailpratique.application;

import android.app.Application;

import net.info420.fabien.androidtravailpratique.data.DBHelper;

// TODO : Ajouter l'organisation des fichiers dans la doc http://blog.smartlogic.io/2013-07-09-organizing-your-android-development-code-structure/
// TODO : Exporter les méthodes vers un helper

/**
 * L'application de base
 * Cette classe est le point d'entrée dans l'application Android.
 * Elle contient des variables utilisées partout dans l'application.
 *
 * @author Fabien Roy
 * @version 1.0
 * @since 17-03-26
 */
public class TodoApplication extends Application {
  private static final String TAG = TodoApplication.class.getName();

  // Les noms des préférences
  public static final String PREFS_TOASTS             = "toasts";
  public static final String PREFS_TOASTS_FREQUENCE   = "toasts_frequence";
  public static final String PREFS_TOASTS_LAPS_TEMPS  = "toasts_laps_temps";
  public static final String PREFS_TOASTS_URGENCE     = "toasts_urgence";
  public static final String PREFS_LANGUE             = "langue";

  // TODO : Prod : Enlever ceci
  // Variables de développement
  public DBHelper dbHelper;
  public boolean  recreationDb         = false;
  public boolean  creationTestTaches   = false;
  public boolean  creationTestEmployes = false;

  /**
   * S'exécute lors de la création de la classe
   */
  @Override
  public void onCreate() {
    super.onCreate();

    // TODO : Enlever dès que la base de données fonctionne

    // Recréation de la base de données
    dbHelper = new DBHelper(this);

    // Pour le développement, recréation de la base de données
    if (recreationDb) {
      dbHelper.recreateDB(dbHelper.getWritableDatabase());
    }
  }
}
