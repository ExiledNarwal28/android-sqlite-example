package net.info420.fabien.androidtravailpratique.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;

public class EditEmployeeActivity extends Activity {
  private final static String TAG = EditEmployeeActivity.class.getName();

  private EditText etEmployeeName;
  private EditText etEmployeeJob;
  private EditText etEmployeeMail;
  private EditText etEmployeePhone;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_employee);

    initUI();
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_edit_employee);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    // TODO : Ajouter les options Modifier, Supprimer, Préférences

    etEmployeeName  = (EditText) findViewById(R.id.et_employee_name);
    etEmployeeJob   = (EditText) findViewById(R.id.et_employee_job);
    etEmployeeMail  = (EditText) findViewById(R.id.et_employee_mail);
    etEmployeePhone = (EditText) findViewById(R.id.et_employee_phone);
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
        // TODO : Annuler
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