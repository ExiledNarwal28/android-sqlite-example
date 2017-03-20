package net.info420.fabien.androidtravailpratique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {
  private final static String TAG = TaskActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);
  }
}
