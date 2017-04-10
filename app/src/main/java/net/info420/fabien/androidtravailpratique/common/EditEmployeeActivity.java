package net.info420.fabien.androidtravailpratique.common;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class EditEmployeeActivity extends FragmentActivity {
  private final static String TAG = EditEmployeeActivity.class.getName();

  private EditText  etEmployeeName;
  private EditText  etEmployeeJob;
  private EditText  etEmployeeMail;
  private EditText  etEmployeePhone;
  private Button    btnValidate;
  
  private Uri employeeUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_employee);

    initUI();

    Bundle extras = getIntent().getExtras();

    // Depuis l'instance sauvegarder
    employeeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE);

    // Ou passée depuis une autre activité
    if (extras != null) {
      employeeUri = extras.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE);

      fillData(employeeUri);
    }
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_edit_employee);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    etEmployeeName  = (EditText) findViewById(R.id.et_employee_name);
    etEmployeeJob   = (EditText) findViewById(R.id.et_employee_job);
    etEmployeeMail  = (EditText) findViewById(R.id.et_employee_mail);
    etEmployeePhone = (EditText) findViewById(R.id.et_employee_phone);
    btnValidate     = (Button)   findViewById(R.id.btn_validate);

    btnValidate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        updateEmployee();
      }
    });
  }

  public void updateEmployee() {
    String name   = etEmployeeName.getText().toString();
    String job    = etEmployeeJob.getText().toString();
    String mail   = etEmployeeMail.getText().toString();
    String phone  = etEmployeePhone.getText().toString();

    // Toutes les informations obligatoires doivent êtes présentes
    if (name.length() == 0) {
      Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_required_fields), Toast.LENGTH_LONG).show();
      return;
    }

    // Validation du numéro de téléphone
    // Source : http://stackoverflow.com/questions/6358380/phone-number-validation-android#6359128
    if (phone.length() != 0 && !PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
      Toast.makeText(getApplicationContext(), getString(R.string.warning_wrong_phone_format), Toast.LENGTH_LONG).show();
      return;
    }

    // Validation de l'adresse e-mail
    // Source : http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    if (mail.length() != 0 && !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
      Toast.makeText(getApplicationContext(), getString(R.string.warning_wrong_mail_format), Toast.LENGTH_LONG).show();
      return;
    }

    ContentValues values = new ContentValues();
    values.put(Employee.KEY_name,   name);
    values.put(Employee.KEY_job,    job);
    values.put(Employee.KEY_phone,  phone);
    values.put(Employee.KEY_email,  mail);

    // Modification employé
    getContentResolver().update(employeeUri, values, null, null);

    finish();
  }

  private void fillData(Uri employeeUri) {
    String[] projection = { Employee.KEY_name,
                            Employee.KEY_job,
                            Employee.KEY_email,
                            Employee.KEY_phone };

    Cursor cursor = getContentResolver().query(employeeUri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // On mets les données dans l'UI
      etEmployeeName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_name)));
      etEmployeeJob.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_job)));
      etEmployeeMail.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_email)));
      etEmployeePhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employee.KEY_phone)));

      // Fermeture du curseur
      cursor.close();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_update_item, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_cancel:
        finish();
        break;
      case R.id.menu_prefs:
        startActivity(new Intent(this, PrefsActivity.class));
        break;
      default:
        break;
    }
    return true;
  }
}