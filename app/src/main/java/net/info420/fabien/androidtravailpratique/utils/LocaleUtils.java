package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import net.info420.fabien.androidtravailpratique.common.TaskerApplication;

import java.util.Locale;

/**
 * Created by fabien on 17-04-26.
 */

public class LocaleUtils {
  public static void initialize(Context context) {
    setLocale(context, PreferenceManager.getDefaultSharedPreferences(context).getString(TaskerApplication.PREFS_LANGUAGE, "fr"));
  }

  public static void initialize(Context context, String defaultLanguage) {
    setLocale(context, defaultLanguage);
  }

  public static boolean setLocale(Context context, String language) {
    return updateResources(context, language);
  }

  private static boolean updateResources(Context context, String language) {
    Locale locale = new Locale(language);
    Locale.setDefault(locale);

    Resources resources = context.getResources();

    Configuration configuration = resources.getConfiguration();
    configuration.locale = locale;

    resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    return true;
  }
}