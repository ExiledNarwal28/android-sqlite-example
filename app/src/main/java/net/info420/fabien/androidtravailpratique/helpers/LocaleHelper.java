package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import net.info420.fabien.androidtravailpratique.application.TodoApplication;

import java.util.Locale;

/**
 * Created by fabien on 17-04-26.
 */

public class LocaleHelper {
  public static void initialize(Context context) {
    setLocale(context, PreferenceManager.getDefaultSharedPreferences(context).getString(TodoApplication.PREFS_LANGUE, Locale.getDefault().getLanguage()));
  }

  public static boolean setLocale(Context context, String language) {
    return updateResources(context, language);
  }

  private static boolean updateResources(Context context, String language) {
    Locale locale = new Locale(language);
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