package net.info420.fabien.androidtravailpratique.activities;

import android.content.ContentValues;
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
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class AjouterEmployeActivity extends FragmentActivity {
  private final static String TAG = AjouterEmployeActivity.class.getName();

  private EditText  etEmployeeName;
  private EditText  etEmployeeJob;
  private EditText  etEmployeeMail;
  private EditText  etEmployeePhone;
  private Button    btnValidate;

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

    ColorHelper.setStatusBarColor(this);

    etEmployeeName  = (EditText) findViewById(R.id.et_employe_nom);
    etEmployeeJob   = (EditText) findViewById(R.id.et_employe_poste);
    etEmployeeMail  = (EditText) findViewById(R.id.et_employe_email);
    etEmployeePhone = (EditText) findViewById(R.id.et_employe_telephone);
    btnValidate     = (Button)   findViewById(R.id.btn_valider);

    btnValidate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createEmployee();
      }
    });
  }

  public void createEmployee() {
    String name   = etEmployeeName.getText().toString();
    String job    = etEmployeeJob.getText().toString();
    String mail   = etEmployeeMail.getText().toString();
    String phone  = etEmployeePhone.getText().toString();

    // Toutes les informations obligatoires doivent êtes présentes
    if (name.length() == 0) {
      Toast.makeText(getApplicationContext(), getString(R.string.attention_champs_vides), Toast.LENGTH_LONG).show();
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
    values.put(Employe.KEY_nom,   name);
    values.put(Employe.KEY_poste,    job);
    values.put(Employe.KEY_telephone,  phone);
    values.put(Employe.KEY_email,  mail);

    // Nouvelle tâche
    getContentResolver().insert(TodoContentProvider.CONTENT_URI_EMPLOYE, values);

    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_modifier_item, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_annuler:
        finish();
        break;
      default:
        break;
    }
    return true;
  }
}