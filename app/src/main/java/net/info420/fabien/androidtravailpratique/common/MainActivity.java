package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;

public class MainActivity extends FragmentActivity {
  private final static String TAG = MainActivity.class.getName();

  // TODO : Est-ce nécéssaire?
  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  // private Uri taskUri;
  // private Uri employeeUri;

  TaskListFragment taskListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    taskListFragment = new TaskListFragment();

    // TODO : Enlever dès que la base de données fonctionne

    // DÉBUT CRÉATION DE TÄCHES
    /*
    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

    String[]  taskNames           = { "Test0",        "Test1",        "Test2",        "Test3" };
    String[]  taskDescriptions    = { "Description0", "Description1", "Description2", "Description3" };
    Boolean[] taskCompleteds      = { false,          false,          true,           false };
    int[]     taskDates           = { 1522108800,     1490745600,     1490659200,     1500659200 };
    int[]     taskUrgencyLevels   = { 0,              2,              1,              0 };

    for (int i = 0; i < taskNames.length; i++) {
      ContentValues values = new ContentValues();

      // La tâche #4 n'a pas d'employé assigné (pour des tests)
      if (i <= 2) {
        values.put(Task.KEY_assigned_employee_ID, i + 1); // Employés auto-généré (en bas)
      }
      values.put(Task.KEY_name,                 taskNames[i]);
      values.put(Task.KEY_description,          taskDescriptions[i]);
      values.put(Task.KEY_completed,            taskCompleteds[i]);
      values.put(Task.KEY_date,                 taskDates[i]);
      values.put(Task.KEY_urgency_level,        taskUrgencyLevels[i]);

      Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s %s:%s",
        Task.KEY_name,          taskNames[i],
        Task.KEY_description,   taskDescriptions[i],
        Task.KEY_completed,     taskCompleteds[i],
        Task.KEY_date,          taskDates[i],
        Task.KEY_urgency_level, taskUrgencyLevels[i]));

      taskUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_TASK, values);
    }

    // FIN CRÉATION DE TÄCHES

    // DÉBUT CRÉATION D'EMPLOYÉS

    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

    String[] employeeNames  = {"Fabien Roy",            "William Leblanc",      "Jean-Sébastien Giroux"};
    String[] employeeJobs   = {"Programmeur-analyste",  "PDG de BlazeIt inc.",  "Icône de l'Internet"};
    String[] employeeEmails = {"fabien@cognitio.ca",    "william@blazeit.org",  "giroux@twitch.com"};
    String[] employeePhones = {"418-409-6568",          "420-420-4242",         "123-456-7890"};

    for (int i = 0; i < employeeNames.length; i++) {
      ContentValues values = new ContentValues();

      values.put(Employee.KEY_name,   employeeNames[i]);
      values.put(Employee.KEY_job,    employeeJobs[i]);
      values.put(Employee.KEY_email,  employeeEmails[i]);
      values.put(Employee.KEY_phone,  employeePhones[i]);

      Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s",
        Employee.KEY_name,  employeeNames[i],
        Employee.KEY_job,   employeeJobs[i],
        Employee.KEY_email, employeeEmails[i],
        Employee.KEY_phone, employeePhones[i]));

      employeeUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_EMPLOYEE, values);
    }
    */
    // FIN CRÉATION D'EMPLOYÉS

    initUI();
  }

  protected void initUI() {
    // TODO : Mettre le fragment des tâches
    setContentView(R.layout.activity_main);

    taskListFragment.setArguments(getIntent().getExtras());

    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, taskListFragment).commit();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_main);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO : REDESIGN : Changer la toolbar en fonction du fragment
    // TODO : REDESIGN : Changer de fragment avec la toolbar

    switch (item.getItemId()) {
      case R.id.menu_task_list:
        // startActivity(new Intent(this, EmployeeListFragment.class));
        break;
      case R.id.menu_employee_list:
        // startActivity(new Intent(this, EmployeeListFragment.class));
        break;
      default:
        break;
    }
    return true;
  }
}
