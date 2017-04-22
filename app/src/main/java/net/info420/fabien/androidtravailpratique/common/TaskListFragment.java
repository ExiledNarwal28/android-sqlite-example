package net.info420.fabien.androidtravailpratique.common;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

import static net.info420.fabien.androidtravailpratique.utils.Task.KEY_date;

/**
 * Created by fabien on 17-04-11.
 */

public class TaskListFragment extends ListFragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = TaskListFragment.class.getName();

  private TaskAdapter taskAdapter;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  private Spinner   spTaskFiltersFilters;
  private Spinner   spTaskFiltersDates;
  private Spinner   spTaskFiltersEmployees;
  private Spinner   spTaskFiltersUrgencies;
  private Spinner   spTaskFiltersCompletion;

  // private TaskFragment taskFragment;

  // public TaskListFragment() {
  //   getChildFragmentManager().beginTransaction().add(R.id.fragment_container_task, taskFragment).commit();
  //   getChildFragmentManager().executePendingTransactions();
  // }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_task_list, container, false);

    initUI(view);

    return view;
  }

  private void initUI (View view) {
    // TODO : REDESIGN : Remove spinners

    spTaskFiltersFilters    = (Spinner) view.findViewById(R.id.sp_task_filters_filters);
    spTaskFiltersDates      = (Spinner) view.findViewById(R.id.sp_task_filters_dates);
    spTaskFiltersEmployees  = (Spinner) view.findViewById(R.id.sp_task_filters_employees);
    spTaskFiltersUrgencies  = (Spinner) view.findViewById(R.id.sp_task_filters_urgencies);
    spTaskFiltersCompletion = (Spinner) view.findViewById(R.id.sp_task_filters_completion);

    spTaskFiltersFilters.setOnItemSelectedListener(this);
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
    Log.d(TAG, String.format("onItemSelected adapterView.getId() : %s, selected item : %s, %s", adapterView.getId(),
                                                                                                ((Spinner)getActivity().findViewById(adapterView.getId())).getSelectedItemId(),
                                                                                                ((Spinner)getActivity().findViewById(adapterView.getId())).getSelectedItem()));

    setCurrentFilter(spTaskFiltersFilters.getSelectedItemId());

    if (((Spinner)getActivity().findViewById(adapterView.getId())).getSelectedItemId() == 0) {
      fillData();
    } else {
      switch (adapterView.getId()) {
        case R.id.sp_task_filters_dates:
          // On ajoute un filtre de date

          switch((int) spTaskFiltersDates.getSelectedItemId()) {
            case 1:
              // Aujourd'hui
              break;
            case 2:
              // Cette semaine
              String dateSelection        = Task.KEY_date + " BETWEEN ? AND ?";
              String dateSelectionArgs[]  = { Long.toString(new LocalDate().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).toDateTime(DateTimeZone.UTC).getMillis() / 1000),
                                              Long.toString(new LocalDate().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).toDateTime(DateTimeZone.UTC).getMillis() / 1000)};
              Log.d(TAG, String.format("%s - %s", dateSelectionArgs[0], dateSelectionArgs[1]));

              fillData(dateSelection, dateSelectionArgs);
              break;
            case 3:
              // Ce mois
              break;
          }
          break;
        case R.id.sp_task_filters_employees:
          // On ajoute un filtre d'employés
          fillData();
          break;
        case R.id.sp_task_filters_urgencies:
          // On ajoute un filtre d'urgence

          // Si c'est "Tous les niveaux d'urgences
          if (spTaskFiltersUrgencies.getSelectedItemId() == 0) {
            fillData();
          } else { // Si l'utilisateur a sélectionné un niveau d'urgence
            String [] urgencyLevelSelectionArgs = { Long.toString(spTaskFiltersUrgencies.getSelectedItemId() - 1) };
            fillData(Task.KEY_urgency_level + "=?", urgencyLevelSelectionArgs);
          }

          break;
        case R.id.sp_task_filters_completion:
          // On ajoute un filtre de complétion
          break;
      }
    }
  }

  private void setCurrentFilter(long filterId) {
    Log.d(TAG, String.format("setCurrentFilter(%s)", filterId));

    spTaskFiltersDates.setVisibility(View.INVISIBLE);
    spTaskFiltersEmployees.setVisibility(View.INVISIBLE);
    spTaskFiltersUrgencies.setVisibility(View.INVISIBLE);
    spTaskFiltersCompletion.setVisibility(View.INVISIBLE);

    switch ((int) filterId) {
      case 1:
        spTaskFiltersDates.setVisibility(View.VISIBLE);
        break;
      case 2:
        spTaskFiltersEmployees.setVisibility(View.VISIBLE);
        break;
      case 3:
        spTaskFiltersUrgencies.setVisibility(View.VISIBLE);
        break;
      case 4:
        spTaskFiltersCompletion.setVisibility(View.VISIBLE);
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

    // taskFragment  = new TaskFragment();

    // taskFragment = TaskFragment.newInstance(R.string.title_task);

    // TODO : REDESIGN : Passer des données à un Fragment
    // Source : http://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment#15392591
    // Bundle bundle = new Bundle();
    // bundle.putParcelable( TaskerContentProvider.CONTENT_ITEM_TYPE_TASK,
    //                       Uri.parse(TaskerContentProvider.CONTENT_URI_TASK + "/" + id));
    // taskFragment.setArguments(bundle);

    // Fragment en PopupWindow
    // Source : http://stackoverflow.com/questions/11754309/android-popupwindow-from-a-fragment#11754352
    // LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // View popupView = layoutInflater.inflate(R.layout.fragment_task, null);

    // Affichage du popup au centre de l'écran
    // Source : http://stackoverflow.com/questions/6063667/show-a-popupwindow-centralized#7440187
    // new PopupWindow(popupView,
    //                 ViewGroup.LayoutParams.WRAP_CONTENT,
    //                 ViewGroup.LayoutParams.WRAP_CONTENT).showAtLocation(getView(), Gravity.CENTER, 0, 0);

    // getFragmentManager().beginTransaction().add(R.id.fragment_container, taskFragment).commit();
    // getChildFragmentManager().beginTransaction().add(R.id.fragment_container_task, taskFragment).commit();
    // getChildFragmentManager().executePendingTransactions();

    // TODO : REDESIGN : On pourrait aussi faire ça comme ça, mais ça n'affiche pas la bonne tâche! Ça affiche ce qu'il y a déjà dans le FragmentManager(), donc les listes des tâches et d'employés ou les préférences.
    //        Dans ce ça là, ce sera la liste des tâches, parce que c'est ce qu'il y a dans le FragmentManager (fragment_task_list.xml).
    // taskFragment.show(getFragmentManager(), "dialog");
  }

  // Remplir l'adapteur avec les bonnes données
  private void fillData() {
    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Task.KEY_name, KEY_date };

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_task_name, R.id.tv_task_date };

    // Enlever le filtrage
    String[] taskProjection = { Task.KEY_ID, Task.KEY_name, Task.KEY_date, Task.KEY_completed, Task.KEY_urgency_level };
    Cursor taskCursor = getActivity().getContentResolver().query(TaskerContentProvider.CONTENT_URI_TASK, taskProjection, null, null, null);

    getLoaderManager().initLoader(0, null, this);
    taskAdapter = new TaskAdapter(getContext(), R.layout.task_row, taskCursor, from, to, 0, (TaskerApplication) getActivity().getApplication());

    setListAdapter(taskAdapter);
  }

  // Remplir l'adapteur avec les bonnes données FILTRÉES
  private void fillData(String selection, String[] selectionArgs) {
    Log.d(TAG, String.format("fillData(%s, %s)", selection, selectionArgs[0]));

    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Task.KEY_name, KEY_date };

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_task_name, R.id.tv_task_date };

    // Filtrage
    String[] taskProjection = { Task.KEY_ID, Task.KEY_name, Task.KEY_date, Task.KEY_completed, Task.KEY_urgency_level };
    Cursor taskCursor = getActivity().getContentResolver().query(TaskerContentProvider.CONTENT_URI_TASK, taskProjection, selection, selectionArgs, null);

    getLoaderManager().initLoader(0, null, this);
    taskAdapter = new TaskAdapter(getContext(), R.layout.task_row, taskCursor, from, to, 0, (TaskerApplication) getActivity().getApplication());

    setListAdapter(taskAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Task.KEY_ID, Task.KEY_name, KEY_date, Task.KEY_completed, Task.KEY_urgency_level };

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
