package net.info420.fabien.androidtravailpratique;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EmployeeListActivity extends AppCompatActivity {
  private final static String TAG = EmployeeListActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);
  }
}
