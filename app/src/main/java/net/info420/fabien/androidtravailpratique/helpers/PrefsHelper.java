package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Classe contenant des méthodes pour facilier la recherche de préférences
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-05-10
 *
 * @see <a href="http://stackoverflow.com/questions/21820031/getting-value-from-edittext-preference-in-preference-screen"
 *      target="_blank">
 *      Source : Aller chercher les données des préférences</a>
 */
public class PrefsHelper {
  private static final String TAG = PrefsHelper.class.getName();

  // Les noms des préférences
  private static final String PREFS_TOASTS             = "toasts";
  private static final String PREFS_TOASTS_FREQUENCE   = "toasts_frequence";
  private static final String PREFS_TOASTS_LAPS_TEMPS  = "toasts_laps_temps";
  private static final String PREFS_TOASTS_URGENCE     = "toasts_urgence";
  private static final String PREFS_TOASTS_AFFICHAGE   = "toasts_affichage";
  private static final String PREFS_LANGUE             = "langue";

  private static final boolean  PREFS_TOASTS_DEFAULT             = false;
  private static final String   PREFS_TOASTS_FREQUENCE_DEFAUT    = "600";
  private static final String   PREFS_TOASTS_LAPS_TEMPS_DEFAUT   = "1";
  private static final String   PREFS_TOASTS_URGENCE_DEFAUT      = "0";
  private static final String   PREFS_TOASTS_AFFICHAGE_DEFAUT    = "1";
  private static final String   PREFS_LANGUE_DEFAUT              = "fr";

  public static final String PREFS_LANGUE_EN = "en";

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du booléan
   * de l'activation des {@link android.widget.Toast}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  boolean de la {@link android.content.SharedPreferences} de l'activation des
   *          {@link android.widget.Toast}
   */
  public static boolean getToasts(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREFS_TOASTS, PREFS_TOASTS_DEFAULT);
  }

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du
   * {@link Integer} de la fréquence entre les {@link android.widget.Toast}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  Integer de la fréquence entre les {@link android.widget.Toast}
   */
  public static Integer getToastsFrequence(Context context) {
    return Integer.parseInt(
      PreferenceManager.getDefaultSharedPreferences(context).getString( PREFS_TOASTS_FREQUENCE,
                                                                        PREFS_TOASTS_FREQUENCE_DEFAUT));
  }

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du
   * {@link Integer} du laps de temps pour le calcul des {@link android.widget.Toast}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  Integer du laps de temps pour le calcul des {@link android.widget.Toast}
   */
  public static Integer getToastsLapsTemps(Context context) {
    return Integer.parseInt(
      PreferenceManager.getDefaultSharedPreferences(context).getString( PREFS_TOASTS_LAPS_TEMPS ,
                                                                        PREFS_TOASTS_LAPS_TEMPS_DEFAUT));
  }

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du
   * {@link Integer} du niveau d'urgence pour le calcul des {@link android.widget.Toast}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  Integer du niveau d'urgence pour le calcul des {@link android.widget.Toast}
   */
  public static Integer getToastsUrgence(Context context) {
    return Integer.parseInt(
      PreferenceManager.getDefaultSharedPreferences(context).getString( PREFS_TOASTS_URGENCE ,
                                                                        PREFS_TOASTS_URGENCE_DEFAUT));
  }

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du
   * {@link Integer} du temps d'affichage des {@link android.widget.Toast}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  Integer du temps d'affichage des {@link android.widget.Toast}
   */
  public static Integer getToastsAffichage(Context context) {
    return Integer.parseInt(
      PreferenceManager.getDefaultSharedPreferences(context).getString( PREFS_TOASTS_AFFICHAGE ,
                                                                        PREFS_TOASTS_AFFICHAGE_DEFAUT));
  }

  /**
   * Méthode statique pour aller chercher la {@link android.content.SharedPreferences} du
   * {@link String} de la sélectionnée pour l'{@link android.app.Application}
   *
   * @param   context {@link Context} pour appeler les {@link android.content.SharedPreferences}
   * @return  String de la langue sélectionnée pour l'{@link android.app.Application}
   */
  public static String getLangue(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFS_LANGUE ,
                                                                            PREFS_LANGUE_DEFAUT);
  }

  /**
   * Méthode statique pour vérifier si la clé d'un {@link android.content.SharedPreferences}
   * concerne les {@link android.widget.Toast}
   *
   * @param   key Clé de la {@link android.content.SharedPreferences}
   * @return  boolean vrai si la clé concerne les {@link android.widget.Toast}
   */
  public static boolean isToasts(String key) {
    return key.equals(PREFS_TOASTS)            ||
           key.equals(PREFS_TOASTS_FREQUENCE)  ||
           key.equals(PREFS_TOASTS_LAPS_TEMPS) ||
           key.equals(PREFS_TOASTS_URGENCE)    ||
           key.equals(PREFS_TOASTS_AFFICHAGE);
  }

  /**
   * Méthode statique pour vérifier si la clé d'un {@link android.content.SharedPreferences}
   * concerne langue de l'{@link android.app.Application}
   *
   * @param   key Clé de la {@link android.content.SharedPreferences}
   * @return  boolean vrai si la clé concerne langue de l'{@link android.app.Application}
   */
  public static boolean isLangue(String key) {
    return key.equals(PREFS_LANGUE);
  }
}
