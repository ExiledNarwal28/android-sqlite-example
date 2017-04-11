package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-11.
 */

public class PrefsFragment extends PreferenceFragmentCompat {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_prefs, container, false);
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    addPreferencesFromResource(R.xml.pref_items);
  }
}