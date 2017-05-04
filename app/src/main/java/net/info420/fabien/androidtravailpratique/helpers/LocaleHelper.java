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
 * @see <a href="http://gunhansancar.com/change-language-programmatically-in-android/"
 *      target="_blank">
 *      Source : Changer la {@link Locale} de l'application</a>
 * @see <a href="http://stackoverflow.com/questions/43292013/how-can-i-change-language-of-whole-application-by-only-single-click/43292068"
 *      target="_blank">
 *      Source : Changer la {@link Locale} de l'application, aussi</a>}
 */
public class LocaleHelper {
  private static final String TAG = LocaleHelper.class.getName();

  /**
   * Méthode statique pour initialiser la {@link Locale} avec les {@link android.content.SharedPreferences}
   *
   * <ul>
   *  <li>Appele setLocale</li>
   *  <li>Si aucune {@link Locale} n'a été sélectionnée dans les {@link android.content.SharedPreferences},
   *  on utilise la {@link Locale} par défaut</li>
   * </ul>
   *
   * @param context {@link Context} pour appeler la méthode setLocale
   */
  public static void initialize(Context context) {
    setLocale(context, PreferenceManager.getDefaultSharedPreferences(context).getString(TodoApplication.PREFS_LANGUE, Locale.getDefault().getLanguage()));
  }

  /**
   * Méthode statique publique pour modifier la {@link Locale} avec une langue spécifiée
   *
   * @param   context  {@link Context} pour appeler la méthode setLocale
   * @param   langue    String représentant la locale
   * @return  boolean représentant la réussite de l'opération
   */
  public static boolean setLocale(Context context, String langue) {
    return updateResources(context, langue);
  }

  /**
   * Méthode statique privée pour modifier la {@link Locale} avec une langue spécifiée
   *
   * <p>La ligne {@code resources.updateConfiguration(configuration, resources.getDisplayMetrics());}
   * est incorrecte. En fait, il faudrait utiliser {@code context.createConfigurationContext(configuration))}.
   * Par contre, cela ne fonctionne pas. La configuration n'est pas mise à jour automatiquement,
   * seulement au redémarrage de l'{@link android.app.Application}. Puisque cela n'est pas l'effet
   * désiré, je me sert simplement de ce qui n'est plus supporté.</p>
   *
   * @param   context  {@link Context} pour appeler la méthode setLocale
   * @param   langue    String représentant la locale
   * @return  boolean représentant la réussite de l'opération
   */
  private static boolean updateResources(Context context, String langue) {
    Locale locale = new Locale(langue);
    Locale.setDefault(locale);

    Resources resources = context.getResources();

    Configuration configuration = resources.getConfiguration();
    configuration.setLocale(locale);

    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    // context.createConfigurationContext(configuration);

    return true;
  }
}