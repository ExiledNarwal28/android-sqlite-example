package net.info420.fabien.androidtravailpratique.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;

/**
 * {@link android.app.Fragment} des préférences
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-04-11
 *
 * @see Preference
 * @see PreferenceFragment
 * @see SharedPreferences
 * @see OnSharedPreferenceChangeListener
 * @see net.info420.fabien.androidtravailpratique.application.TodoApplication où il y a les noms
 * des préférences
 */
public class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
  private static final String TAG = PrefsFragment.class.getName();

  /**
   * Exécuté à la création du {@link PrefsFragment}
   *
   * <ul>
   *  <li>Ajoute les préférences depuis R.xml.pref_items</li>
   * </ul>
   *
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.pref_items);

    PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
  }

  // TODO : Afficher la valeur de préférence lorsque modifier
  /**
   * Modifie la valeur affichée de la préférence lorsque modifiée
   *
   * @param sharedPreferences {@link SharedPreferences} modifiée
   * @param key               Nom de la {@link SharedPreferences}
   *
   * @see <a href="http://stackoverflow.com/questions/531427/how-do-i-display-the-current-value-of-an-android-preference-in-the-preference-su"
   *      target="_blank">
   *      Source : Afficher les préférences dans la liste des préférences</a>
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);

    if (pref instanceof ListPreference) {
      ListPreference listPref = (ListPreference) pref;
      CharSequence lol = pref.getSummary();

      Log.d(TAG, String.format("%s %s %s", lol, String.format(lol.toString(), listPref.getEntry().toString()), String.format(lol.toString(), listPref.getEntry().toString().toLowerCase())));
      // pref.setSummary(listPref.getEntry().toString().toLowerCase());

      pref.setSummary(String.format(pref.getSummary().toString(), (listPref.getEntry().toString().toLowerCase())));
    }
  }
}