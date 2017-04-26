package net.info420.fabien.androidtravailpratique.common;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.LocaleUtils;
import net.info420.fabien.androidtravailpratique.utils.Task;
import net.info420.fabien.androidtravailpratique.utils.TimeReceiver;
import net.info420.fabien.androidtravailpratique.utils.TimeService;

import org.joda.time.DateTime;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
  private final static String TAG = MainActivity.class.getName();

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  private Uri taskUri;
  private Uri employeeUri;

  private TimeReceiver timeReceiver = new TimeReceiver();

  private Menu menu;

  private Intent timeServiceIntent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // TODO : Enlever dès que la base de données fonctionne

    if (((TaskerApplication) getApplication()).writeTestTasks) {
      taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

      String[]  taskNames           = { "Test0",                                    "Test1",                                                      "Test2",                                                      "Test3" };
      String[]  taskDescriptions    = { "Description0",                             "Description1",                                               "Description2",                                               "Description3" };
      Boolean[] taskCompleteds      = { false,                                      false,                                                        true,                                                         false };
      int[]     taskDates           = { (int) (new DateTime().getMillis() / 10000), (int) (new DateTime(2017, 4, 20, 0, 0).getMillis() / 10000),  (int) (new DateTime(2017, 4, 22, 0, 0).getMillis() / 10000),  (int) (new DateTime(2017, 4, 28, 0, 0).getMillis() / 10000) };
      int[]     taskUrgencyLevels   = { 0,                                          2,                                                            1,                                                            0 };

      for (int i = 0; i < taskNames.length; i++) {
        ContentValues values = new ContentValues();

        // La tâche #4 n'a pas d'employé assigné (pour des tests)
        if (i <= 2) {
          values.put(Task.KEY_assigned_employee_ID, i + 1); // Employés auto-généré (en bas)
        } else {
          values.putNull(Task.KEY_assigned_employee_ID);
        }

        values.put(Task.KEY_name,                 taskNames[i]);
        values.put(Task.KEY_description,          taskDescriptions[i]);
        values.put(Task.KEY_completed,            taskCompleteds[i]);
        values.put(Task.KEY_date,                 taskDates[i]);
        values.put(Task.KEY_urgency_level,        taskUrgencyLevels[i]);

        /*
        Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s %s:%s",
          Task.KEY_name,          taskNames[i],
          Task.KEY_description,   taskDescriptions[i],
          Task.KEY_completed,     taskCompleteds[i],
          Task.KEY_date,          taskDates[i],
          Task.KEY_urgency_level, taskUrgencyLevels[i]));
        */

        taskUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_TASK, values);
      }
    }

    if (((TaskerApplication) getApplication()).writeTestEmployees) {
      taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

      String[] employeeNames  = {"Fabien Roy",            "William Leblanc",      "Jean-Sébastien Giroux"};
      String[] employeeJobs   = {"Programmeur-analyste",  "PDG de BlazeIt inc.",  "Icône de l'Internet"};
      String[] employeeEmails = {"fabien@cognitio.ca",    "william@blazeit.org",  "giroux@twitch.com"};
      String[] employeePhones = {"418-409-6568",          "420-420-4242",         "123-456-7890"};

      for (int i = 0; i < employeeNames.length; i++) {
        ContentValues values = new ContentValues();

        values.put(Employee.KEY_name, employeeNames[i]);
        values.put(Employee.KEY_job, employeeJobs[i]);
        values.put(Employee.KEY_email, employeeEmails[i]);
        values.put(Employee.KEY_phone, employeePhones[i]);

        /*
        Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s",
          Employee.KEY_name, employeeNames[i],
          Employee.KEY_job, employeeJobs[i],
          Employee.KEY_email, employeeEmails[i],
          Employee.KEY_phone, employeePhones[i]));
        */

        employeeUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_EMPLOYEE, values);
      }
    }

    timeServiceIntent = new Intent(this, TimeService.class);
    startService(timeServiceIntent);

    initUI();
    LocaleUtils.updateConfig(this);
  }

  protected void initUI() {
    setContentView(R.layout.activity_main);

    TaskListFragment taskListFragment = new TaskListFragment();
    taskListFragment.setArguments(getIntent().getExtras());

    // Ceci permet de mettre le fragment d'une liste (ou n'importe quel autre fragment) dans un conteneur à cet effet.
    getFragmentManager().beginTransaction().add(R.id.fragment_container, taskListFragment).commit();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_task_list);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    registerReceiver(timeReceiver, new IntentFilter(TimeService.NOTIFICATION));
  }

  @Override
  protected void onPause() {
    super.onPause();
    PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    unregisterReceiver(timeReceiver);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_task_list, menu);

    this.menu = menu;

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO : REDESIGN : Ne pas changer le fragment si c'est le fragment actuel

    menu.clear();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();

    switch (item.getItemId()) {
      case R.id.icon_task_list:
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        toolbar.setTitle(R.string.title_task_list);
        transaction.replace(R.id.fragment_container, new TaskListFragment());
        transaction.commit();
        break;
      case R.id.icon_employee_list:
        getMenuInflater().inflate(R.menu.menu_employee_list, menu);
        toolbar.setTitle(R.string.title_employee_list);
        transaction.replace(R.id.fragment_container, new EmployeeListFragment());
        transaction.commit();
        break;
      case R.id.icon_prefs:
        getMenuInflater().inflate(R.menu.menu_prefs, menu);
        toolbar.setTitle(R.string.title_prefs);
        transaction.replace(R.id.fragment_container, new PrefsFragment());
        transaction.commit();
        break;
      default:
        break;
    }

    return true;
  }

  // Lorsqu'une préférence de toast est modifiée, on recommence le TimeService
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    // Si la préférence concerne les toasts...
    if (key.equals(TaskerApplication.PREFS_TOASTS)           ||
        key.equals(TaskerApplication.PREFS_TOASTS_FREQUENCY) ||
        key.equals(TaskerApplication.PREFS_TOASTS_TIMESPAN)  ||
        key.equals(TaskerApplication.PREFS_TOASTS_URGENCY_LEVEL)) {

      // On recommence le service!
      stopService(timeServiceIntent);
      startService(timeServiceIntent);
    }

    // Si la préférence concerne la langue...
    if (key.equals(TaskerApplication.PREFS_LANGUAGE)) {
      ((TaskerApplication) getApplication()).setupLocale();
      // recreate(); // On restart l'activité, afin de modifier la langue
    }
  }
}
