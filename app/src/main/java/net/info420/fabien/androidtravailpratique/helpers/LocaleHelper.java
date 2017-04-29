package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import net.info420.fabien.androidtravailpratique.application.TodoApplication;

import java.util.Locale;

/**
 * Classe contenant des méthodes pour facilier la recherche de Strings
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-26
 *
 * @see Configuration
 * @see Locale
 *
 * {@link <a href="http://gunhansancar.com/change-language-programmatically-in-android/">Changer la {@link Locale} de l'application</a>}
 * {@link <a href="http://stackoverflow.com/questions/43292013/how-can-i-change-language-of-whole-application-by-only-single-click/43292068">Changer la {@link Locale} de l'application, aussi</a>}
 */
public class LocaleHelper {
  private static final String TAG = LocaleHelper.class.getName();

  /**
   * Méthode statique pour initialiser la {@link Locale} avec les {@link android.content.SharedPreferences}
   *
   * Appele setLocale
   * Si aucune {@link Locale} n'a été sélectionnée dans les {@link android.content.SharedPreferences},
   * on utilise la {@link Locale} par défaut
   *
   * @param context {@link Context} pour appeler la méthode setLocale
   */
  public static void initialize(Context context) {
    setLocale(context, PreferenceManager.getDefaultSharedPreferences(context).getString(TodoApplication.PREFS_LANGUE, Locale.getDefault().getLanguage()));
  }

  /**
   * Méthode statique publique pour modifier la {@link Locale} avec une langue spécifiée
   *
   * @param context  {@link Context} pour appeler la méthode setLocale
   * @param langue    String représentant la locale
   * @return          boolean représentant la réussite de l'opération
   */
  public static boolean setLocale(Context context, String langue) {
    return updateResources(context, langue);
  }

  /**
   * Méthode statique privée pour modifier la {@link Locale} avec une langue spécifiée
   *
   * @param context  {@link Context} pour appeler la méthode setLocale
   * @param langue    String représentant la locale
   * @return          boolean représentant la réussite de l'opération
   */
  private static boolean updateResources(Context context, String langue) {
    Locale locale = new Locale(langue);
    Locale.setDefault(locale);

    Resources resources = context.getResources();

    Configuration configuration = resources.getConfiguration();
    configuration.setLocale(locale);

    // TODO : Ceci n'est plus supporté. Pourtant, createConfigurationContent ne semble pas fonctionner!
    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    // context.getApplicationContext().createConfigurationContext(configuration);

    return true;
  }
}