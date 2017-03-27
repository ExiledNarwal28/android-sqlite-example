package net.info420.fabien.androidtravailpratique.common;

import android.app.ListActivity;
import android.content.ContentValues;
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

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Task;

public class MainActivity extends ListActivity implements AdapterView.OnItemSelectedListener {
  private final static String TAG = MainActivity.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;
  // private Cursor cursor;
  private SimpleCursorAdapter adapter;

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  private Uri taskUri;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  private ListView lvTaskList;
  private Spinner spTaskFiltersDates;
  private Spinner spTaskFiltersEmployees;
  private Spinner spTaskFiltersUrgencies;
  private Spinner spTaskFiltersCompletion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    // TODO : Enlever dès que la base de données fonctionne

    // DÉBUT CRÉATION DE TÄCHES

    // check from the saved Instance
    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

    String[]  names           = { "Test0",        "Test1",        "Test2" };
    String[]  descriptions    = { "Description0", "Description1", "Description2" };
    Boolean[] completeds      = { false,          false,          true };
    int[]     dates           = { 1522108800,     1490745600,     1490659200 };
    int[]     urgency_levels  = { 0,              2,              1 };

    for (int i = 0; i < names.length - 1; i++) {
      ContentValues values = new ContentValues();

      values.put(Task.KEY_assigned_employee_ID, 1); // Pas encore d'employés
      values.put(Task.KEY_name,                 names[i]);
      values.put(Task.KEY_description,          descriptions[i]);
      values.put(Task.KEY_completed,            completeds[i]);
      values.put(Task.KEY_date,                 dates[i]);
      values.put(Task.KEY_urgency_level,        urgency_levels[i]);

      Log.d(TAG, String.format("Insertion de la tâche #%s dans la base de données avec %s:%s %s:%s %s:%s %s:%s %s:%s",
        1,
        Task.KEY_name,          names[i],
        Task.KEY_description,   descriptions[i],
        Task.KEY_completed,     completeds[i],
        Task.KEY_date,          dates[i],
        Task.KEY_urgency_level, urgency_levels[i]));

      taskUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_TASK, values);
    }

    // FIN CRÉATION DE TÄCHES

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    // fillData();

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
}
