package net.info420.fabien.androidtravailpratique.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;

public class EmployeeActivity extends Activity {
  private final static String TAG = EmployeeActivity.class.getName();

  private TextView tvEmployeeName;
  private TextView tvEmployeeJob;
  private TextView tvEmployeeMail;
  private TextView tvEmployeePhone;
  private Button btnEmployeeSendSms;
  private Button btnEmployeeCall;

  private Uri employeeUri;

  private int employeeId;

  private String employeePhone;

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

    tvEmployeeName = (TextView) findViewById(R.id.tv_employee_name);
    tvEmployeeJob = (TextView) findViewById(R.id.tv_task_description);
    tvEmployeeMail = (TextView) findViewById(R.id.tv_task_date);
    tvEmployeePhone = (TextView) findViewById(R.id.tv_employee_phone);
    btnEmployeeSendSms = (Button) findViewById(R.id.btn_employee_send_sms);
    btnEmployeeCall = (Button) findViewById(R.id.btn_employee_call);

    btnEmployeeSendSms.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Envoie un SMS à l'employé
      }
    });

    btnEmployeeCall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callEmployee();
      }
    });
  }

  private void fillData(Uri uri) {
    String[] projection = {Employee.KEY_ID,
      Employee.KEY_name,
      Employee.KEY_job,
      Employee.KEY_email,
      Employee.KEY_phone};

    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      employeeId = cursor.getInt(cursor.getColumnIndexOrThrow(Employee.KEY_ID));
      employeePhone = cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_phone));

      // On mets les données dans l'UI
      tvEmployeeName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_name)));
      tvEmployeeJob.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_job)));
      tvEmployeeMail.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_email)));
      tvEmployeePhone.setText(employeePhone);

      // Fermeture du curseur
      cursor.close();
    }
  }

  // On rafraîchit quand on revient dans l'Activity (ex. : en revenant d'EditEmployeeActivity)
  @Override
  public void onResume() {
    super.onResume();
    fillData(employeeUri);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_item, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_edit:
        Intent i = new Intent(this, EditEmployeeActivity.class);
        i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE, employeeUri);
        startActivity(i);
        break;
      case R.id.menu_delete:
        getContentResolver().delete(employeeUri, null, null);

        // Il faut aussi enlever cet employé de toutes les tâches
        // Source : http://stackoverflow.com/questions/6234171/how-do-i-update-an-android-sqlite-database-column-value-to-null-using-contentval
        ContentValues values = new ContentValues();
        values.putNull(Task.KEY_assigned_employee_ID);

        // On n'a besoin que des tâches qui ont cet employé
        // Source : https://developer.android.com/guide/topics/providers/content-provider-basics.html
        String selection = Task.KEY_assigned_employee_ID + " = ?";
        String[] selectionArgs = {Integer.toString(employeeId)};

        // Modification des tâches
        getContentResolver().update(TaskerContentProvider.CONTENT_URI_TASK,
          values,
          selection,
          selectionArgs);

        finish();
        break;
      default:
        break;
    }
    return true;
  }

  private void callEmployee() {
    // On vérifie qu'il y a bien un numéro de téléphone
    // On vérifie aussi qu'on a bien la permission d'appeler le contact
    Log.d(TAG, "ok" + employeePhone);
    if ((employeePhone != null) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
      Log.d(TAG, "super ok");
      Intent callIntent = new Intent(Intent.ACTION_CALL);
      callIntent.setData(Uri.parse("tel:" + employeePhone));
      startActivity(callIntent);
    }
  }
}
