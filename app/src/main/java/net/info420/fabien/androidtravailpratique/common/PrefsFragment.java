package net.info420.fabien.androidtravailpratique.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-11.
 */

public class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.pref_items);
  }

  // TODO : Ceci permetterait d'écrire la valeur actuelle de la préférence dans la liste des préférences
  //        Source : http://stackoverflow.com/questions/531427/how-do-i-display-the-current-value-of-an-android-preference-in-the-preference-su
  //        À vérifier rendu là
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);

    if (pref instanceof ListPreference) {
      ListPreference listPref = (ListPreference) pref;
      pref.setSummary(listPref.getEntry());
    }
  }
}