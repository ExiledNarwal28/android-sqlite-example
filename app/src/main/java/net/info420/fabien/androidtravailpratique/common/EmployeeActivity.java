package net.info420.fabien.androidtravailpratique.common;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;

public class EmployeeActivity extends AppCompatActivity {
  private final static String TAG = EmployeeActivity.class.getName();

  private TextView  tvEmployeeName;
  private TextView  tvEmployeeJob;
  private TextView  tvEmployeeMail;
  private TextView  tvEmployeePhone;
  private Button    btnEmployeeSendSms;
  private Button    btnEmployeeCall;

  private Uri employeeUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee);

    initUI();

    // On va chercher les informations
    employeeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      employeeUri = extras.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE);
      Log.d(TAG, employeeUri.getPath());

      fillData(employeeUri);
    }
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_employee);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    // TODO : Ajouter les options Modifier, Supprimer, Préférences

    tvEmployeeName      = (TextView)  findViewById(R.id.tv_employee_name);
    tvEmployeeJob       = (TextView)  findViewById(R.id.tv_task_description);
    tvEmployeeMail      = (TextView)  findViewById(R.id.tv_task_date);
    tvEmployeePhone     = (TextView)  findViewById(R.id.tv_employee_phone);
    btnEmployeeSendSms  = (Button)    findViewById(R.id.btn_employee_send_sms);
    btnEmployeeCall     = (Button)    findViewById(R.id.btn_employee_call);

    btnEmployeeSendSms.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Envoie un SMS à l'employé
      }
    });

    btnEmployeeCall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Appeler l'employé
      }
    });
  }

  private void fillData(Uri uri) {
    String[] projection = { Employee.KEY_name,
                            Employee.KEY_job,
                            Employee.KEY_email,
                            Employee.KEY_phone };

    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // On mets les données dans l'UI
      tvEmployeeName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_name)));
      tvEmployeeJob.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_job)));
      tvEmployeeMail.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_email)));
      tvEmployeePhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_phone)));

      // Fermeture du curseur
      cursor.close();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_item, menu);
    return true;
  }
}
