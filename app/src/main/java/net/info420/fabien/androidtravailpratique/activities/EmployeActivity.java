package net.info420.fabien.androidtravailpratique.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Task;

public class EmployeActivity extends Activity {
  private final static String TAG = EmployeActivity.class.getName();

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
    employeeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      employeeUri = extras.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE);
      Log.d(TAG, employeeUri.getPath());

      fillData(employeeUri);
    }
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_employee);

    ColorHelper.setStatusBarColor(this);

    tvEmployeeName = (TextView) findViewById(R.id.tv_employee_name);
    tvEmployeeJob = (TextView) findViewById(R.id.tv_task_description);
    tvEmployeeMail = (TextView) findViewById(R.id.tv_task_date);
    tvEmployeePhone = (TextView) findViewById(R.id.tv_employee_phone);
    btnEmployeeSendSms = (Button) findViewById(R.id.btn_employee_send_sms);
    btnEmployeeCall = (Button) findViewById(R.id.btn_employee_call);

    btnEmployeeSendSms.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendSMS();
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
    String[] projection = {Employe.KEY_ID,
      Employe.KEY_nom,
      Employe.KEY_poste,
      Employe.KEY_email,
      Employe.KEY_telephone};

    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      employeeId = cursor.getInt(cursor.getColumnIndexOrThrow(Employe.KEY_ID));
      employeePhone = cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_telephone));

      // On mets les données dans l'UI
      tvEmployeeName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_nom)));
      tvEmployeeJob.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_poste)));
      tvEmployeeMail.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_email)));
      tvEmployeePhone.setText(employeePhone);

      // Fermeture du curseur
      cursor.close();
    }
  }

  // On rafraîchit quand on revient dans l'Activity (ex. : en revenant d'ModifierEmployeActivity)
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
        Intent i = new Intent(this, ModifierEmployeActivity.class);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, employeeUri);
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
        getContentResolver().update(TodoContentProvider.CONTENT_URI_TASK,
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

  private void sendSMS() {
    // On vérifie qu'il y a bien un numéro de téléphone
    // On vérifie aussi qu'on a bien la permission d'envoyer un SMS
    if ((employeePhone != null) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
      // TODO : Faire une classe séparée qui renvoie un dialogue
      // Source : http://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog#29048271
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
      alertDialog.setTitle(getString(R.string.info_sending_a_sms));
      alertDialog.setMessage(getString(R.string.info_enter_your_message));

      final EditText input = new EditText(this);
      input.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
      alertDialog.setView(input);

      alertDialog.setPositiveButton(getString(R.string.action_send),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            String message = input.getText().toString();
            if (!message.isEmpty()) {
              // Source : http://stackoverflow.com/questions/10752394/smsmanager-sendtextmessage-is-not-working
              SmsManager.getDefault().sendTextMessage(employeePhone,
                                                      null,
                                                      message,
                                                      PendingIntent.getBroadcast(getBaseContext(), 0, new Intent("SMS_SENT"), 0),
                                                      null);
            }
          }
        });

      alertDialog.setNegativeButton(getString(R.string.action_cancel),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

      alertDialog.show();
    }
  }

  private void callEmployee() {
    // On vérifie qu'il y a bien un numéro de téléphone
    // On vérifie aussi qu'on a bien la permission d'appeler le contact
    if ((employeePhone != null) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
      Intent callIntent = new Intent(Intent.ACTION_CALL);
      callIntent.setData(Uri.parse("tel:" + employeePhone));
      startActivity(callIntent);
    }
  }
}
