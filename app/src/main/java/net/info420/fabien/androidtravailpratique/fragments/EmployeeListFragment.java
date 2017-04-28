package net.info420.fabien.androidtravailpratique.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.activities.AjouterEmployeActivity;
import net.info420.fabien.androidtravailpratique.adapters.EmployeAdapter;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.activities.EmployeActivity;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;

public class EmployeeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = EmployeeListFragment.class.getName();

  private FloatingActionButton fabAddEmployee;

  // private Cursor cursor;
  private EmployeAdapter employeAdapter;

  // private ListView lvEmployeeList;
  private TextView tvNoEmployee;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_employee_list, container, false);

    initUI(view);

    return view;
  }

  private void initUI (View view) {
    fabAddEmployee = (FloatingActionButton)  view.findViewById(R.id.fab_add_employee);

    fabAddEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Nouvelle tâche
        startActivity(new Intent(getContext(), AjouterEmployeActivity.class));
      }
    });
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());
  }

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(getContext(), EmployeActivity.class);
    Uri employeeUri = Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + id);
    i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, employeeUri);

    startActivity(i);
  }

  private void fillData() {
    // Affiche les champs de la base de données (name)
    String[] from = new String[]{Employe.KEY_nom, Employe.KEY_poste};

    // Où on affiche les champs
    int[] to = new int[]{R.id.tv_employee_name, R.id.tv_task_description};

    getLoaderManager().initLoader(0, null, this);
    employeAdapter = new EmployeAdapter(getContext(), R.layout.employee_row, null, from, to, 0, (TodoApplication) getActivity().getApplication());

    setListAdapter(employeAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {Employe.KEY_ID, Employe.KEY_nom, Employe.KEY_poste};
    CursorLoader cursorLoader = new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI_EMPLOYE, projection, null, null, null);

    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    employeAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    employeAdapter.swapCursor(null);
  }
}