package net.info420.fabien.androidtravailpratique.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class NewTaskActivity extends FragmentActivity {
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

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }

  // Source : https://developer.android.com/guide/topics/ui/controls/pickers.html
  public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      Log.d(TAG, "onCreateDialog");

      // On utilise la date actuelle comme date par défaut
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // On retourne une nouvelle instance
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      ((NewTaskActivity) getActivity()).taskDate = (int) (new DateTime(year, month + 1, day, 0, 0).getMillis() / 10000);
      ((NewTaskActivity) getActivity()).refreshDate();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshDate();
  }

  public void refreshDate() {
    if (taskDate != 0) {
      Log.d(TAG, String.format("New date : %s", taskDate));

      btnTaskDate.setText(((TaskerApplication) getApplication()).getFullDate((int) taskDate));
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
      case R.id.menu_prefs:
        startActivity(new Intent(this, PrefsActivity.class));
        break;
      default:
        break;
    }
    return true;
  }
}