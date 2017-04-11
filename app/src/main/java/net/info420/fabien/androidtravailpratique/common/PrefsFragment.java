package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-11.
 */

public class PrefsFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.pref_items);
  }

}