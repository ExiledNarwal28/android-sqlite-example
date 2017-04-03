package net.info420.fabien.androidtravailpratique.common;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;
import net.info420.fabien.androidtravailpratique.utils.TaskAdapter;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = MainActivity.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;
  // private Cursor cursor;
  private TaskAdapter taskAdapter;

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  private Uri taskUri;
  private Uri employeeUri;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  // private ListView lvTaskList;
  private Spinner   spTaskFiltersDates;
  private Spinner   spTaskFiltersEmployees;
  private Spinner   spTaskFiltersUrgencies;
  private Spinner   spTaskFiltersCompletion;
  private TextView  tvNoTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO : Enlever dès que la base de données fonctionne

    // DÉBUT CRÉATION DE TÄCHES

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

    // FIN CRÉATION D'EMPLOYÉS

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());

    initUI();
  }

  protected void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_main);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    // TODO : Ajouter le bouton vers EmployeeListActivity
    // TODO : Ajouter les options Ajouter une tâche, Préférences

    // lvTaskList = (ListView) findViewById(R.id.list);
    spTaskFiltersDates      = (Spinner) findViewById(R.id.sp_task_filters_dates);
    spTaskFiltersEmployees  = (Spinner) findViewById(R.id.sp_task_filters_employees);
    spTaskFiltersUrgencies  = (Spinner) findViewById(R.id.sp_task_filters_urgencies);
    spTaskFiltersCompletion = (Spinner) findViewById(R.id.sp_task_filters_completion);

    spTaskFiltersDates.setOnItemSelectedListener(this);
    spTaskFiltersEmployees.setOnItemSelectedListener(this);
    spTaskFiltersUrgencies.setOnItemSelectedListener(this);
    spTaskFiltersCompletion.setOnItemSelectedListener(this);

    // TODO : Afficher si il n'y a aucune tâche
    // Par défaut, caché.
    tvNoTask = (TextView) findViewById(R.id.tv_no_task);
    tvNoTask.setVisibility(View.GONE);

    // Je mets la seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNames = new ArrayList<>();
    employeeNames.add(getString(R.string.task_filter_all_employee));

    // C'est l'heure d'aller chercher les noms des employés
    String[] employeeProjection = { Employee.KEY_name };
    Cursor employeeCursor = getContentResolver().query(TaskerContentProvider.CONTENT_URI_EMPLOYEE, employeeProjection, null, null, null);

    if (employeeCursor != null) {
      while (employeeCursor.moveToNext()) {
        employeeNames.add(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employee.KEY_name)));
      }

      // Fermeture du curseur
      employeeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, employeeNames);
    spTaskFiltersEmployees.setAdapter(adapterTaskFiltersEmployees);
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    switch (view.getId()) {
      case R.id.sp_task_filters_dates:
        // On ajoute un filtre de date
        break;
      case R.id.sp_task_filters_employees:
        // On ajoute un filtre d'employés
        break;
      case R.id.sp_task_filters_urgencies:
        taskAdapter.getFilter().filter(Long.toString(l));
        break;
      case R.id.sp_task_filters_completion:
        // On ajoute un filtre de complétion
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    // Ne fait rien!
  }

  /*

  // Crée un menu d'option avec du XML
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.listmenu, menu);
    return true;
  }

  // Réaction à une sélection dans le menu
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.insert:
        createTodo();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case DELETE_ID:
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + info.id);
        getContentResolver().delete(uri, null, null);
        fillData();
        return true;
    }
    return super.onContextItemSelected(item);
  }

  private void createTodo() {
    Intent i = new Intent(this, NewTaskActivity.class);
    startActivity(i);
  }

  */

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, TaskActivity.class);
    Uri taskUri = Uri.parse(TaskerContentProvider.CONTENT_URI_TASK + "/" + id);
    i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK, taskUri);

    startActivity(i);
  }

  private void fillData() {
    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Task.KEY_name, Task.KEY_date };

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_task_name, R.id.tv_task_date };

    getLoaderManager().initLoader(0, null, this);
    taskAdapter = new TaskAdapter(this, R.layout.task_row, null, from, to, 0, (TaskerApplication) getApplication());

    setListAdapter(taskAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Task.KEY_ID, Task.KEY_name, Task.KEY_date, Task.KEY_completed, Task.KEY_urgency_level };
    CursorLoader cursorLoader = new CursorLoader(this, TaskerContentProvider.CONTENT_URI_TASK, projection, null, null, null);

    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    taskAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    taskAdapter.swapCursor(null);
  }
}
