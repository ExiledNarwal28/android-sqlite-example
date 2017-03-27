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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Task;

public class MainActivity extends ListActivity implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = MainActivity.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;
  // private Cursor cursor;
  private SimpleCursorAdapter adapter;

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  private Uri taskUri;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  // private ListView lvTaskList;
  private Spinner spTaskFiltersDates;
  private Spinner spTaskFiltersEmployees;
  private Spinner spTaskFiltersUrgencies;
  private Spinner spTaskFiltersCompletion;
  private TextView tvNoTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    // TODO : Enlever dès que la base de données fonctionne

    // DÉBUT CRÉATION DE TÄCHES

    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

    String[]  names           = { "Test0",        "Test1",        "Test2" };
    String[]  descriptions    = { "Description0", "Description1", "Description2" };
    Boolean[] completeds      = { false,          false,          true };
    int[]     dates           = { 1522108800,     1490745600,     1490659200 };
    int[]     urgency_levels  = { 0,              2,              1 };

    for (int i = 0; i < names.length; i++) {
      ContentValues values = new ContentValues();

      values.put(Task.KEY_assigned_employee_ID, 1); // Pas encore d'employés
      values.put(Task.KEY_name,                 names[i]);
      values.put(Task.KEY_description,          descriptions[i]);
      values.put(Task.KEY_completed,            completeds[i]);
      values.put(Task.KEY_date,                 dates[i]);
      values.put(Task.KEY_urgency_level,        urgency_levels[i]);

      Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s %s:%s",
        Task.KEY_name,          names[i],
        Task.KEY_description,   descriptions[i],
        Task.KEY_completed,     completeds[i],
        Task.KEY_date,          dates[i],
        Task.KEY_urgency_level, urgency_levels[i]));

      taskUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_TASK, values);
    }

    // FIN CRÉATION DE TÄCHES

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());

    initUI();
  }

  protected void initUI() {
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
    // TODO : Ajouter les employés dynamiquement
    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { getString(R.string.task_filter_all_employee) });
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
        // On ajoute un filtre de niveau d'urgence
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
    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, TaskActivity.class);
    Uri todoUri = Uri.parse(TaskerContentProvider.CONTENT_URI_TASK + "/" + id);
    i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK, todoUri);

    startActivity(i);
  }

  private void fillData() {
    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Task.KEY_name };
    // Où on affiche les champs
    int[] to = new int[] { R.id.task_name };

    getLoaderManager().initLoader(0, null, this);
    adapter = new SimpleCursorAdapter(this, R.layout.task_row, null, from, to, 0);

    setListAdapter(adapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Task.KEY_ID, Task.KEY_name };
    CursorLoader cursorLoader = new CursorLoader(this, TaskerContentProvider.CONTENT_URI_TASK, projection, null, null, null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    adapter.swapCursor(null);
  }
}
