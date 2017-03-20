package net.info420.fabien.androidtravailpratique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PrefsActivity extends AppCompatActivity {
  private final static String TAG = PrefsActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_prefs);
  }
}
