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
import android.widget.ListView;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.EmployeeAdapter;

public class EmployeeListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = EmployeeListActivity.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;

  // private Cursor cursor;
  private EmployeeAdapter employeeAdapter;

  // TODO : Enlever ceci si ça ne sert plus quand la base de données sera fonctionnelle
  private Uri employeeUri;

  // private ListView lvEmployeeList;
  private TextView tvNoEmployee;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);

    // TODO : Enlever dès que la base de données fonctionne

    // DÉBUT CRÉATION DE TÄCHES

    employeeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE);

    String[] names = {"Fabien Roy", "William Leblanc", "Jean-Sébastien Giroux"};
    String[] jobs = {"Programmeur-analyste", "PDG de BlazeIt inc.", "Icône de l'Internet"};
    String[] emails = {"fabien@cognitio.ca", "william@blazeit.org", "giroux@twitch.com"};
    String[] phones = {"418-409-6568", "420-420-4242", "123-456-7890"};

    for (int i = 0; i < names.length; i++) {
      ContentValues values = new ContentValues();

      values.put(Employee.KEY_name, names[i]);
      values.put(Employee.KEY_job, jobs[i]);
      values.put(Employee.KEY_email, emails[i]);
      values.put(Employee.KEY_phone, phones[i]);

      Log.d(TAG, String.format("Insertion de tâche dans la base de données avec %s:%s %s:%s %s:%s %s:%s",
        Employee.KEY_name, names[i],
        Employee.KEY_job, jobs[i],
        Employee.KEY_email, emails[i],
        Employee.KEY_phone, phones[i]));

      employeeUri = getContentResolver().insert(TaskerContentProvider.CONTENT_URI_EMPLOYEE, values);
    }

    // FIN CRÉATION DE TÄCHES

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData(employeeUri);

    registerForContextMenu(getListView());

    initUI();
  }

  private void initUI() {
    // lvEmployeeList = (ListView) findViewById(R.id.lv_employee_list);

    // TODO : Afficher si il n'y a aucun employé
    // Par défaut, caché.
    tvNoEmployee = (TextView) findViewById(R.id.tv_no_employee);
    tvNoEmployee.setVisibility(View.GONE);
  }

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, EmployeeActivity.class);
    Uri employeeUri = Uri.parse(TaskerContentProvider.CONTENT_URI_EMPLOYEE + "/" + id);
    i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE, employeeUri);

    startActivity(i);
  }

  private void fillData(Uri uri) {
    // Affiche les champs de la base de données (name)
    String[] from = new String[]{Employee.KEY_name, Employee.KEY_job};

    // Où on affiche les champs
    int[] to = new int[]{R.id.tv_employee_name, R.id.tv_employee_job};

    getLoaderManager().initLoader(0, null, this);
    employeeAdapter = new EmployeeAdapter(this, R.layout.employee_row, null, from, to, 0, (TaskerApplication) getApplication());

    setListAdapter(employeeAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {Employee.KEY_ID, Employee.KEY_name, Employee.KEY_job};
    CursorLoader cursorLoader = new CursorLoader(this, TaskerContentProvider.CONTENT_URI_EMPLOYEE, projection, null, null, null);

    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    employeeAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    employeeAdapter.swapCursor(null);
  }
}