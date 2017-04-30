package net.info420.fabien.androidtravailpratique.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import net.info420.fabien.androidtravailpratique.R;

/**
 * {@link android.app.Fragment} des préférences
 *
 * <p>Pour afficher la valeur des préférences, il suffit de mettre un '%s' dans le string du
 * résumé de la préférence (summary). String.format s'occupe de mettre la valeur de la préférence.</p>
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
 *
 * @see <a href="http://stackoverflow.com/questions/531427/how-do-i-display-the-current-value-of-an-android-preference-in-the-preference-su"
 *      target="_blank">
 *      Source : Afficher les préférences dans la liste des préférences</a>
 */
public class PrefsFragment extends PreferenceFragment {
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
  }
}