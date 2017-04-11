package net.info420.fabien.androidtravailpratique.common;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;
import net.info420.fabien.androidtravailpratique.utils.TaskAdapter;

import java.util.ArrayList;

/**
 * Created by fabien on 17-04-11.
 */

public class TaskListFragment extends ListFragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = TaskListFragment.class.getName();

  private TaskAdapter taskAdapter;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  private Spinner   spTaskFiltersDates;
  private Spinner   spTaskFiltersEmployees;
  private Spinner   spTaskFiltersUrgencies;
  private Spinner   spTaskFiltersCompletion;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_task_list, container, false);

    initUI(view);

    return view;
  }

  private void initUI (View view) {
    // TODO : REDESIGN : Remove spinners

    spTaskFiltersDates      = (Spinner) view.findViewById(R.id.sp_task_filters_dates);
    spTaskFiltersEmployees  = (Spinner) view.findViewById(R.id.sp_task_filters_employees);
    spTaskFiltersUrgencies  = (Spinner) view.findViewById(R.id.sp_task_filters_urgencies);
    spTaskFiltersCompletion = (Spinner) view.findViewById(R.id.sp_task_filters_completion);

    spTaskFiltersDates.setOnItemSelectedListener(this);
    spTaskFiltersEmployees.setOnItemSelectedListener(this);
    spTaskFiltersUrgencies.setOnItemSelectedListener(this);
    spTaskFiltersCompletion.setOnItemSelectedListener(this);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());

    // Je mets la seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNames = new ArrayList<>();
    employeeNames.add(getString(R.string.task_filter_all_employee));

    // C'est l'heure d'aller chercher les noms des employés
    String[] employeeProjection = { Employee.KEY_name };
    Cursor employeeCursor = getActivity().getContentResolver().query(TaskerContentProvider.CONTENT_URI_EMPLOYEE, employeeProjection, null, null, null);

    if (employeeCursor != null) {
      while (employeeCursor.moveToNext()) {
        employeeNames.add(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employee.KEY_name)));
      }

      // Fermeture du curseur
      employeeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, employeeNames);
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

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(getContext(), TaskActivity.class);
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
    taskAdapter = new TaskAdapter(getContext(), R.layout.task_row, null, from, to, 0, (TaskerApplication) getActivity().getApplication());

    setListAdapter(taskAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Task.KEY_ID, Task.KEY_name, Task.KEY_date, Task.KEY_completed, Task.KEY_urgency_level };
    CursorLoader cursorLoader = new CursorLoader(getContext(), TaskerContentProvider.CONTENT_URI_TASK, projection, null, null, null);

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
