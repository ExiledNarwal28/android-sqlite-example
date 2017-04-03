package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;

public class NewEmployeeActivity extends AppCompatActivity {
  private final static String TAG = NewEmployeeActivity.class.getName();

  private EditText etEmployeeName;
  private EditText etEmployeeJob;
  private EditText etEmployeeMail;
  private EditText etEmployeePhone;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_employee);

    initUI();
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_new_employee);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    // TODO : Ajouter les options Annuler, Préférences

    etEmployeeName  = (EditText) findViewById(R.id.et_employee_name);
    etEmployeeJob   = (EditText) findViewById(R.id.et_employee_job);
    etEmployeeMail  = (EditText) findViewById(R.id.et_employee_mail);
    etEmployeePhone = (EditText) findViewById(R.id.et_employee_phone);
  }
}