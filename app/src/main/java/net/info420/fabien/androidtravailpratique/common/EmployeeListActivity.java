package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import net.info420.fabien.androidtravailpratique.R;

public class EmployeeListActivity extends AppCompatActivity {
  private final static String TAG = EmployeeListActivity.class.getName();

  private ListView lvEmployeeList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);

    initUI();
  }

  private void initUI() {
    lvEmployeeList = (ListView) findViewById(R.id.lv_employee_list);
  }
}
