package net.info420.fabien.androidtravailpratique.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.fragments.DatePickerFragment;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.interfaces.OnTacheDateChangeListener;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class AjouterTacheActivity extends FragmentActivity implements OnTacheDateChangeListener {
  private final static String TAG = AjouterTacheActivity.class.getName();

  private ArrayAdapter<String> adapterTaskAssignedEmployees;

  private EditText  etTaskName;
  private EditText  etTaskDescription;
  private CheckBox  cbTaskCompleted;
  private Spinner   spTaskUrgencyLevel;
  private Button    btnTaskDate;
  private Button    btnValidate;
  private Spinner   spTaskAssignedEmployee;

  private Map<Integer, Integer> spTaskAssignedEmployeeMap;

  public long taskDate = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_task);

    initUI();
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_new_task);

    ColorHelper.setStatusBarColor(this);

    etTaskName              = (EditText)  findViewById(R.id.et_tache_nom);
    etTaskDescription       = (EditText)  findViewById(R.id.et_tache_description);
    cbTaskCompleted         = (CheckBox)  findViewById(R.id.cb_tache_fait);
    spTaskUrgencyLevel      = (Spinner)   findViewById(R.id.sp_tache_urgence);
    btnTaskDate             = (Button)    findViewById(R.id.btn_tache_date);
    btnValidate             = (Button)    findViewById(R.id.btn_valider);
    spTaskAssignedEmployee  = (Spinner)   findViewById(R.id.sp_tache_employe_assigne);

    // Je mets la seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNames = new ArrayList<>();
    employeeNames.add(getString(R.string.tache_aucun_employe)); // Ceci aura le id 0.

    // C'est l'heure d'aller chercher les noms des employés
    // Ceci sert à associé correctement un nom d'employé et son id
    spTaskAssignedEmployeeMap = new HashMap<Integer, Integer>();

    String[] employeeProjection = { Employe.KEY_nom, Employe.KEY_ID };
    Cursor employeeCursor = getContentResolver().query(TodoContentProvider.CONTENT_URI_EMPLOYE, employeeProjection, null, null, null);

    if (employeeCursor != null) {
      // Position dans le spinner
      Integer position = 1;

      while (employeeCursor.moveToNext()) {
        employeeNames.add(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));
        spTaskAssignedEmployeeMap.put(position,
                                      employeeCursor.getInt(employeeCursor.getColumnIndexOrThrow(Employe.KEY_ID)));

        position++;
      }

      // Fermeture du curseur
      employeeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskAssignedEmployees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, employeeNames);
    spTaskAssignedEmployee.setAdapter(adapterTaskAssignedEmployees);

    btnTaskDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePickerDialog(view);
      }
    });

    btnValidate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createTask();
      }
    });
  }

  public void setTacheDate(int tacheDate) { this.taskDate = tacheDate; onTacheDateChange(); }

  public void showDatePickerDialog(View v) {
    // Afin de mettre la date comme date par défaut dans le calendrier
    if (taskDate != 0) {
      DatePickerFragment.newInstance((int) taskDate).show(getFragmentManager(), "datePicker");
    } else {
      DatePickerFragment.newInstance().show(getFragmentManager(), "datePicker");
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    onTacheDateChange();
  }

  public void onTacheDateChange() {
    if (taskDate != 0) {
      Log.d(TAG, String.format("New date : %s", taskDate));

      btnTaskDate.setText(DateHelper.getLongueDate((int) taskDate));
    }
  }

  public void createTask() {
    String name         = etTaskName.getText().toString();
    String description  = etTaskDescription.getText().toString();
    int completed       = cbTaskCompleted.isChecked() ? 1 : 0;
    int date            = (int) taskDate;
    int urgencyLevel    = (int) spTaskUrgencyLevel.getSelectedItemId(); // TODO : Vérifier si ça marche vraiment

    // Pour l'employé assigné, je vérifie d'abord si quelque chose a été choisi dans le Spinner. Dans ce cas, j'ajoute le bon Id d'employé. Sinon, null.
    int assinedEmployee = ((spTaskAssignedEmployee.getSelectedItem() != null) && (spTaskAssignedEmployee.getSelectedItemId() != 0)) ? spTaskAssignedEmployeeMap.get((int) spTaskAssignedEmployee.getSelectedItemId()) : 0;

    // Toutes les informations obligatoires doivent êtes présentes
    if (name.length() == 0 || taskDate == 0) {
      Toast.makeText(getApplicationContext(), getString(R.string.attention_champs_vides), Toast.LENGTH_LONG).show();
      return;
    }

    ContentValues values = new ContentValues();
    values.put(Tache.KEY_nom,         name);
    values.put(Tache.KEY_description, description);
    values.put(Tache.KEY_fait,        completed);
    values.put(Tache.KEY_date,        date);
    values.put(Tache.KEY_urgence,     urgencyLevel);

    if (assinedEmployee != 0) {
      values.put(Tache.KEY_employe_assigne_ID, assinedEmployee);
    } else {
      values.putNull(Tache.KEY_employe_assigne_ID);
    }

    // Nouvelle tâche
    getContentResolver().insert(TodoContentProvider.CONTENT_URI_TACHE, values);

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
    }
    return true;
  }
}