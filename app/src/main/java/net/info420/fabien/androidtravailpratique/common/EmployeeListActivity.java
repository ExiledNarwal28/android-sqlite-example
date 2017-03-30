package net.info420.fabien.androidtravailpratique.common;

import android.app.ListActivity;
import android.app.LoaderManager;
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

  // private ListView lvEmployeeList;
  private TextView tvNoEmployee;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

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

  private void fillData() {
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