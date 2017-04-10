package net.info420.fabien.androidtravailpratique.common;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html#activities

public class TaskActivity extends Activity {
  private final static String TAG = TaskActivity.class.getName();

  private TextView tvTaskName;
  private CheckBox cbTaskComplete;
  private TextView tvTaskUrgencyLevel;
  private TextView tvTaskDescription;
  private TextView tvTaskDate;
  private Button btnTaskAssignedEmployee;

  private Uri taskUri;

  private int assignedEmployeeId; // Valeur par défaut

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);

    initUI();

    // On va chercher les informations
    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      taskUri = extras.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);
      Log.d(TAG, taskUri.getPath());

      fillData(taskUri);
    }
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_task);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);


    tvTaskName              = (TextView)  findViewById(R.id.tv_task_name);
    cbTaskComplete          = (CheckBox)  findViewById(R.id.cb_task_completed);
    tvTaskUrgencyLevel      = (TextView)  findViewById(R.id.tv_task_urgency_level);
    tvTaskDescription       = (TextView)  findViewById(R.id.tv_task_description);
    tvTaskDate              = (TextView)  findViewById(R.id.tv_task_date);
    btnTaskAssignedEmployee = (Button)    findViewById(R.id.btn_task_assigned_employee);

    btnTaskAssignedEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), EmployeeActivity.class);
        Uri employeeUri = Uri.parse(TaskerContentProvider.CONTENT_URI_EMPLOYEE + "/" + assignedEmployeeId);
        i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE, employeeUri);

        startActivity(i);
      }
    });
  }

  private void fillData(Uri taskUri) {
    String[] projection = { Task.KEY_assigned_employee_ID,
                            Task.KEY_name,
                            Task.KEY_description,
                            Task.KEY_completed,
                            Task.KEY_date,
                            Task.KEY_urgency_level };

    Cursor cursor = getContentResolver().query(taskUri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // On mets les données dans l'UI
      tvTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Task.KEY_name)));
      cbTaskComplete.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_completed))) == 1); // Conversion en boolean
      tvTaskDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(Task.KEY_description)));

      // Conversion en date
      tvTaskDate.setText(((TaskerApplication) getApplication()).getFullDate(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_date))));

      // Conversion en niveau d'urgence textuel
      tvTaskUrgencyLevel.setText(((TaskerApplication) getApplication()).getUrgencyLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_urgency_level))));

      // Et maintenant? Il faut afficher le nom de l'employé dans le bouton d'employé assigné.

      Log.d(TAG, Integer.toString(cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID)));

      // Pour rediriger avec le bouton
      assignedEmployeeId = cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID));

      // Vérification de la colonne
      if (assignedEmployeeId != 0) {
        String[] employeeProjection = { Employee.KEY_name };

        Uri employeeUri = Uri.parse(TaskerContentProvider.CONTENT_URI_EMPLOYEE + "/" + cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID)));

        Cursor employeeCursor = getContentResolver().query(employeeUri, employeeProjection, null, null, null);

        if (employeeCursor != null) {
          employeeCursor.moveToFirst();

          btnTaskAssignedEmployee.setText(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employee.KEY_name)));

          // Fermeture du curseur
          employeeCursor.close();
        }
      }

      // Fermeture du curseur
      cursor.close();
    }

    // On cache le bouton si aucun employé n'est assigné.
    if (assignedEmployeeId == 0) {
      btnTaskAssignedEmployee.setVisibility(View.GONE);
    }
  }

  // On rafraîchit quand on revient dans l'Activity (ex. : en revenant d'EditTaskActivity)
  @Override
  public void onResume() {
    super.onResume();
    fillData(taskUri);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    Log.d(TAG, "We got it");
    getMenuInflater().inflate(R.menu.menu_item, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_edit:
        Intent i = new Intent(this, EditTaskActivity.class);
        i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK, taskUri);
        startActivity(i);
        break;
      case R.id.menu_delete:
        // TODO : Supprimer la tâche
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
