package net.info420.fabien.androidtravailpratique.common;

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
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.DatePickerFragment;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.OnTaskDateChangeListener;
import net.info420.fabien.androidtravailpratique.utils.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class NewTaskActivity extends FragmentActivity implements OnTaskDateChangeListener {
  private final static String TAG = NewTaskActivity.class.getName();

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

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    etTaskName              = (EditText)  findViewById(R.id.et_task_name);
    etTaskDescription       = (EditText)  findViewById(R.id.et_task_description);
    cbTaskCompleted         = (CheckBox)  findViewById(R.id.cb_task_completed);
    spTaskUrgencyLevel      = (Spinner)   findViewById(R.id.sp_task_urgency_level);
    btnTaskDate             = (Button)    findViewById(R.id.btn_task_date);
    btnValidate             = (Button)    findViewById(R.id.btn_validate);
    spTaskAssignedEmployee  = (Spinner)   findViewById(R.id.sp_task_assigned_employee);

    // Je mets la seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNames = new ArrayList<>();
    employeeNames.add(getString(R.string.task_no_employee)); // Ceci aura le id 0.

    // C'est l'heure d'aller chercher les noms des employés
    // Ceci sert à associé correctement un nom d'employé et son id
    spTaskAssignedEmployeeMap = new HashMap<Integer, Integer>();

    String[] employeeProjection = { Employee.KEY_name, Employee.KEY_ID };
    Cursor employeeCursor = getContentResolver().query(TaskerContentProvider.CONTENT_URI_EMPLOYEE, employeeProjection, null, null, null);

    if (employeeCursor != null) {
      // Position dans le spinner
      Integer position = 1;

      while (employeeCursor.moveToNext()) {
        employeeNames.add(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employee.KEY_name)));
        spTaskAssignedEmployeeMap.put(position,
                                      employeeCursor.getInt(employeeCursor.getColumnIndexOrThrow(Employee.KEY_ID)));

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

  @Override
  public void setTaskDate(int taskDate) { this.taskDate = taskDate; onTaskDateChange(); }

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
    onTaskDateChange();
  }

  public void onTaskDateChange() {
    if (taskDate != 0) {
      Log.d(TAG, String.format("New date : %s", taskDate));

      btnTaskDate.setText(TaskerApplication.getFullDate((int) taskDate));
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
      alert();
      return;
    }

    ContentValues values = new ContentValues();
    values.put(Task.KEY_assigned_employee_ID, assinedEmployee);
    values.put(Task.KEY_name,                 name);
    values.put(Task.KEY_description,          description);
    values.put(Task.KEY_completed,            completed);
    values.put(Task.KEY_date,                 date);
    values.put(Task.KEY_urgency_level,        urgencyLevel);

    // Nouvelle tâche
    getContentResolver().insert(TaskerContentProvider.CONTENT_URI_TASK, values);

    finish();
  }

  private void alert() {
    Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_required_fields), Toast.LENGTH_LONG).show();
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
      default:
        break;
    }
    return true;
  }
}