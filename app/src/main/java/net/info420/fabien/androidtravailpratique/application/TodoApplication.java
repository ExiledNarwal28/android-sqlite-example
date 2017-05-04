package net.info420.fabien.androidtravailpratique.application;

import android.app.Application;

/**
 * L'application de base
 *
 * <p>Cette classe est le point d'entrée dans l'application Android.
 * Elle contient des variables utilisées partout dans l'application.
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-26
 */
public class TodoApplication extends Application {
  private static final String TAG = TodoApplication.class.getName();

  // Les noms des préférences
  public static final String PREFS_TOASTS             = "toasts";
  public static final String PREFS_TOASTS_FREQUENCE   = "toasts_frequence";
  public static final String PREFS_TOASTS_LAPS_TEMPS  = "toasts_laps_temps";
  public static final String PREFS_TOASTS_URGENCE     = "toasts_urgence";
  public static final String PREFS_TOASTS_AFFICHAGE   = "toasts_affichage";
  public static final String PREFS_LANGUE             = "langue";

  public static final String PREFS_TOASTS_FREQUENCE_DEFAUT  = "600";
  public static final String PREFS_TOATS_LAPS_TEMPS_DEFAUT  = "1";
  public static final String PREFS_TOATS_URGENCE_DEFAUT     = "0";
  public static final String PREFS_TOASTS_AFFICHAGE_DEFAUT  = "1";
  public static final String PREFS_LANGUE_DEFAUT            = "fr";

  /**
   * S'exécute lors de la création de la classe
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }
}
