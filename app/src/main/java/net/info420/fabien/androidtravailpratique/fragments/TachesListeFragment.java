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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.activities.AjouterTacheActivity;
import net.info420.fabien.androidtravailpratique.activities.TacheActivity;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;
import net.info420.fabien.androidtravailpratique.adapters.TacheAdapter;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.info420.fabien.androidtravailpratique.models.Tache.KEY_urgence;

/**
 * Created by fabien on 17-04-11.
 */

public class TachesListeFragment extends ListFragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = TachesListeFragment.class.getName();

  private TacheAdapter taskAdapter;

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  private Spinner              spTaskFiltersFilters;
  private Spinner              spTaskFiltersDates;
  private Spinner              spTaskFiltersEmployees;
  private Spinner              spTaskFiltersUrgencies;
  private Spinner              spTaskFiltersCompletion;
  private FloatingActionButton fabAddTask;

  private Map<Integer, Integer> spTaskAssignedEmployeeMap;

  // private TacheFragment taskFragment;

  // public TachesListeFragment() {
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

    spTaskFiltersFilters    = (Spinner)               view.findViewById(R.id.sp_task_filters_filters);
    spTaskFiltersDates      = (Spinner)               view.findViewById(R.id.sp_task_filters_dates);
    spTaskFiltersEmployees  = (Spinner)               view.findViewById(R.id.sp_task_filters_employees);
    spTaskFiltersUrgencies  = (Spinner)               view.findViewById(R.id.sp_task_filters_urgencies);
    spTaskFiltersCompletion = (Spinner)               view.findViewById(R.id.sp_task_filters_completion);
    fabAddTask              = (FloatingActionButton)  view.findViewById(R.id.fab_add_task);

    spTaskFiltersFilters.setOnItemSelectedListener(this);
    spTaskFiltersDates.setOnItemSelectedListener(this);
    spTaskFiltersEmployees.setOnItemSelectedListener(this);
    spTaskFiltersUrgencies.setOnItemSelectedListener(this);
    spTaskFiltersCompletion.setOnItemSelectedListener(this);

    fabAddTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Nouvelle tâche
        startActivity(new Intent(getContext(), AjouterTacheActivity.class));
      }
    });
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    setFillData();

    registerForContextMenu(getListView());

    // Je mets la seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNames = new ArrayList<>();
    employeeNames.add(getString(R.string.task_filter_all_employee));

    // C'est l'heure d'aller chercher les noms des employés
    // Ceci sert à associé correctement un nom d'employé et son id
    spTaskAssignedEmployeeMap = new HashMap<>();

    // TODO : Je fais ceci souvent, je devrais le mettre dans une fonction
    Cursor employeeCursor = getActivity().getContentResolver().query(TodoContentProvider.CONTENT_URI_EMPLOYE, new String[] { Employe.KEY_ID, Employe.KEY_nom}, null, null, null);

    if (employeeCursor != null) {
      Integer position = 1;

      while (employeeCursor.moveToNext()) {
        employeeNames.add(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));
        spTaskAssignedEmployeeMap.put(position,
                                      employeeCursor.getInt(employeeCursor.getColumnIndexOrThrow(Employe.KEY_ID)));

        position++;
      }

      // Fermeture du curseur
      employeeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, employeeNames);
    spTaskFiltersEmployees.setAdapter(adapterTaskFiltersEmployees);
  }

  // Ceci filtre les données avec ce qui a été choisis dans les spinners
  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int intId, long longId) {
    setCurrentFilter(spTaskFiltersFilters.getSelectedItemId());

    // Valeurs par défaut
    String selection                = null;
    ArrayList<String> selectionArgs = new ArrayList<>();
    String sortOrder                = Tache.KEY_date + " ASC";

    // Quel spinner a été cliqué?
    switch (adapterView.getId()) {
      case R.id.sp_task_filters_filters:
        switch((int) spTaskFiltersFilters.getSelectedItemId()) {
          case 2:
            sortOrder = Tache.KEY_employe_assigne_ID + " ASC";

            break;
          case 3:
            sortOrder = KEY_urgence + " ASC";

            break;
          case 4:
            sortOrder = Tache.KEY_fait + " ASC";

            break;
        }
      case R.id.sp_task_filters_dates:
        // On ajoute un filtre de date

        switch((int) spTaskFiltersDates.getSelectedItemId()) {
          case 1:
            // Aujourd'hui
            selection = Tache.KEY_date + " =?";
            selectionArgs.add(Long.toString(new LocalDate().toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).getMillis() / 10000));

            break;
          case 2:
            // Cette semaine
            selection = Tache.KEY_date + " BETWEEN ? AND ?";
            selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
            selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

            break;
          case 3:
            // Ce mois
            selection = Tache.KEY_date + " BETWEEN ? AND ?";
            selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMinimumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
            selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMaximumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

            break;
        }

        break;
      case R.id.sp_task_filters_employees:
        // On ajoute un filtre d'employés

        sortOrder = Tache.KEY_employe_assigne_ID + " ASC";

        switch((int) spTaskFiltersEmployees.getSelectedItemId()) {
          case 0:
            // Tous les employés
            break;
          default:
            // Dans ce cas, il a choisit un employé
            selection = Tache.KEY_employe_assigne_ID + "=?";
            selectionArgs.add(Long.toString(spTaskAssignedEmployeeMap.get((int) spTaskFiltersEmployees.getSelectedItemId())));

            break;
        }

        break;
      case R.id.sp_task_filters_urgencies:
        // On ajoute un filtre d'urgence

        sortOrder = KEY_urgence + " DESC";

        switch((int) spTaskFiltersUrgencies.getSelectedItemId()) {
          case 0:
            // Tous les niveaux d'urgence
            break;
          default:
            // Dans ce cas, il a choisit un niveau d'urgence
            selection = KEY_urgence + "=?";
            selectionArgs.add(Long.toString(spTaskFiltersUrgencies.getSelectedItemId() - 1)); // Bas, moyen, haut

            break;
        }

        break;
      case R.id.sp_task_filters_completion:
        // On ajoute un filtre de complétion

        sortOrder = Tache.KEY_fait + " ASC";

        switch((int) spTaskFiltersCompletion.getSelectedItemId()) {
          case 0:
            // Tous les niveaux de complétion
            break;
          default:
            // Dans ce cas, il a choisit un niveau de complétion
            selection = Tache.KEY_fait + "=?";
            selectionArgs.add(Long.toString(spTaskFiltersCompletion.getSelectedItemId() - 1)); // Bas, moyen, haut

            break;
        }

        break;
    }

    Log.d(TAG, String.format("Filtré par : %s en ordre de %s", selection, sortOrder));

    if (!selectionArgs.isEmpty()) {
      for (int i = 0; i < selectionArgs.size(); i++) {
        Log.d(TAG, String.format("    %s", selectionArgs.get(i)));
      }
    }

    // Conversion d'un ArrayList<String> en String[]
    // Source : http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
    setFillData(selection, (!selectionArgs.isEmpty()) ? selectionArgs.toArray(new String[selectionArgs.size()]) : null, sortOrder);
  }

  // Changer le filtre qui est actuellement utilisé
  private void setCurrentFilter(long filterId) {
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
    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(getContext(), TacheActivity.class);
    Uri taskUri = Uri.parse(TodoContentProvider.CONTENT_URI_TACHE + "/" + id);
    i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE, taskUri);

    startActivity(i);

    // taskFragment  = new TacheFragment();

    // taskFragment = TacheFragment.newInstance(R.string.title_task);

    // TODO : REDESIGN : Passer des données à un Fragment
    // Source : http://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment#15392591
    // Bundle bundle = new Bundle();
    // bundle.putParcelable( TodoContentProvider.CONTENT_ITEM_TYPE_TACHE,
    //                       Uri.parse(TodoContentProvider.CONTENT_URI_TACHE + "/" + id));
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
  private void setFillData() {
    fillData(null, null, Tache.KEY_date + " ASC");
  }

  // Remplir l'adapteur avec les bonnes données FILTRÉES
  private void setFillData(String selection, String[] selectionArgs, String sortOrder) {
    fillData(selection, selectionArgs, sortOrder);
  }

  private void fillData(String selection, String[] selectionArgs, String sortOrder) {
    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID};

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_task_name, R.id.tv_employe_email, R.id.tv_task_employee };

    // Enlever le filtrage
    String[] projection = { Tache.KEY_ID, Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID, Tache.KEY_fait, KEY_urgence};
    Cursor taskCursor = getActivity().getContentResolver().query(TodoContentProvider.CONTENT_URI_TACHE, projection, selection, selectionArgs, sortOrder);

    getLoaderManager().initLoader(0, null, this);
    taskAdapter = new TacheAdapter(getContext(), R.layout.task_row, taskCursor, from, to, 0, (TodoApplication) getActivity().getApplication());

    setListAdapter(taskAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Tache.KEY_ID, Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID, Tache.KEY_fait, Tache.KEY_urgence};

    CursorLoader cursorLoader = new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI_TACHE, projection, null, null, null);

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
