package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import net.info420.fabien.androidtravailpratique.R;

public class PrefsActivity extends AppCompatActivity {
  private final static String TAG = PrefsActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prefs);
    getFragmentManager().beginTransaction().add(R.id.fragment_container_prefs, new MyPreferencesFragment()).commit();
  }

  public static class MyPreferencesFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_items);
    }
  }
}
