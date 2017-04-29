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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.activities.AjouterTacheActivity;
import net.info420.fabien.androidtravailpratique.activities.TacheActivity;
import net.info420.fabien.androidtravailpratique.adapters.TacheAdapter;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

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
 * {@link android.app.Fragment} de la liste des tâches
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-04-11
 *
 * @see Tache
 * @see TacheAdapter
 * @see TodoContentProvider
 *
 * {@link <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html">Source SQLite</a>}
 */
public class TachesListeFragment extends ListFragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = TachesListeFragment.class.getName();

  private Spinner               spTacheFiltreFiltres;
  private Spinner               spTacheFiltreDates;
  private Spinner               spTacheFiltreEmploye;
  private Spinner               spTacheFiltreUrgence;
  private Spinner               spTacheFiltreFait;
  private FloatingActionButton  fabAjouterTache;

  private Map<Integer, Integer> spTacheEmployeAssigneMap;

  private TacheAdapter tacheAdapter;

  // private TacheFragment taskFragment;

  // public TachesListeFragment() {
  //   getChildFragmentManager().beginTransaction().add(R.id.fragment_container_task, taskFragment).commit();
  //   getChildFragmentManager().executePendingTransactions();
  // }

  /**
   * Exécuté à la création du {@link View}
   *
   * Instancie la {@link View}
   * Instancie l'interface
   * Ajoute les données d'employés dans le Spinner à cet effet
   *
   * @param inflater            @See {@link LayoutInflater}
   * @param container           La {@link View} qui contient le fragment
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return                    La {@link View} instanciée
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_task_list, container, false);

    initUI(view);
    setupEmployeAssigneUI();

    return view;
  }

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * Place le {@link ListView} correctement
   * Va chercher les données nécéssaires
   * Enregistre le {@link ListView}
   *
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    setRempliData();

    registerForContextMenu(getListView());
  }

  /**
   * Exécuté à la création du {@link Loader}
   *
   * Construit et retourne un curseur avec la projection nécéssaire pour remplir le {@link ListView}
   *
   * @param id    Id du {@link Loader}
   * @param args  Arguments
   * @return      Un nouveau {@link CursorLoader} avec la projection
   */
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Tache.KEY_ID, Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID, Tache.KEY_fait, Tache.KEY_urgence};

    return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI_TACHE, projection, null, null, null);
  }

  /**
   * Initialisation de l'interface
   *
   * Instancie les {@link View}
   * Ajoute les Listeners
   */
  private void initUI (View view) {
    // TODO : REDESIGN : Remove spinners

    spTacheFiltreFiltres  = (Spinner)               view.findViewById(R.id.sp_tache_filtre_filtres);
    spTacheFiltreDates    = (Spinner)               view.findViewById(R.id.sp_tache_filtre_dates);
    spTacheFiltreEmploye  = (Spinner)               view.findViewById(R.id.sp_tache_filtre_employe);
    spTacheFiltreUrgence  = (Spinner)               view.findViewById(R.id.sp_tache_filtre_urgence);
    spTacheFiltreFait     = (Spinner)               view.findViewById(R.id.sp_tache_filtre_fait);
    fabAjouterTache       = (FloatingActionButton)  view.findViewById(R.id.fab_ajouter_tache);

    spTacheFiltreFiltres.setOnItemSelectedListener(this);
    spTacheFiltreDates.setOnItemSelectedListener(this);
    spTacheFiltreEmploye.setOnItemSelectedListener(this);
    spTacheFiltreUrgence.setOnItemSelectedListener(this);
    spTacheFiltreFait.setOnItemSelectedListener(this);

    fabAjouterTache.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Nouvelle tâche
        startActivity(new Intent(getContext(), AjouterTacheActivity.class));
      }
    });
  }

  /**
   * Ajoute la liste des employés au Spinner approprié
   *
   * Met l'option de base (Tous les employés)
   * Ajoute les employés dans une liste de noms
   * Construit un {@link HashMap} pour faire le lien entre l'Id d'un employé et sa position dans le {@link Spinner}
   * Ajoute l'{@link android.widget.Adapter} au {@link Spinner}
   *
   * @see Employe
   * @see TodoContentProvider
   * @see HashMap
   *
   * {@link <a href="http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720">Ajout manuel d'item dans un Spinner</a>}
   */
  private void setupEmployeAssigneUI() {
    // Seule option actuelle dans le filtre des employés
    ArrayList<String> employeeNoms = new ArrayList<>();
    employeeNoms.add(getString(R.string.tache_filtre_tous_les_employes));

    // Ceci sert à associé correctement un nom d'employé et son id
    spTacheEmployeAssigneMap = new HashMap<>();

    // TODO : Je fais ceci souvent, je devrais le mettre dans une fonction
    Cursor employeCursor = getActivity().getContentResolver().query(TodoContentProvider.CONTENT_URI_EMPLOYE, new String[] { Employe.KEY_ID, Employe.KEY_nom}, null, null, null);

    if (employeCursor != null) {
      Integer position = 1; // 1, car 'Tous les employés' est en 0

      while (employeCursor.moveToNext()) {
        employeeNoms.add(employeCursor.getString(employeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));
        spTacheEmployeAssigneMap.put( position,
                                      employeCursor.getInt(employeCursor.getColumnIndexOrThrow(Employe.KEY_ID)));

        position++;
      }

      // Fermeture du curseur
      employeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    ArrayAdapter<String> adapterTacheFiltreEmployes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, employeeNoms);
    spTacheFiltreEmploye.setAdapter(adapterTacheFiltreEmployes);
  }

  /**
   * Change le filtre actuellement utilisé (visilibilité)
   *
   * @param filtreId Id du filtre à afficher
   */
  private void setFiltre(long filtreId) {
    spTacheFiltreDates.setVisibility(View.INVISIBLE);
    spTacheFiltreEmploye.setVisibility(View.INVISIBLE);
    spTacheFiltreUrgence.setVisibility(View.INVISIBLE);
    spTacheFiltreFait.setVisibility(View.INVISIBLE);

    switch ((int) filtreId) {
      case 1:
        spTacheFiltreDates.setVisibility(View.VISIBLE);
        break;
      case 2:
        spTacheFiltreEmploye.setVisibility(View.VISIBLE);
        break;
      case 3:
        spTacheFiltreUrgence.setVisibility(View.VISIBLE);
        break;
      case 4:
        spTacheFiltreFait.setVisibility(View.VISIBLE);
        break;
    }
  }

  /**
   * Appele rempliData avec un ordre par date
   */
  private void setRempliData() {
    rempliData(null, null, Tache.KEY_date + " ASC");
  }

  /**
   * Appele rempliData avec un ordre par date et des données filtrées
   */
  private void setRempliData(String selection, String[] selectionArgs, String sortOrder) {
    rempliData(selection, selectionArgs, sortOrder);
  }

  /**
   * Rempli la {@link ListView} des données
   *
   * Construit un tableau de String, c'est le SELECT du {@link Cursor}
   * Construit un curseur avec les données reçue (selection + selectionArgs filtrés ou null)
   * Se sert du {@link CursorLoader} afin de placer les données dans un {@link TacheAdapter}
   * Ajoute l'{@link TacheAdapter} à la {@link ListView}
   *
   * @see TacheAdapter
   */
  private void rempliData(String selection, String[] selectionArgs, String sortOrder) {
    // Affiche les champs de la base de données (name)
    String[] from = new String[] { Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID};

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_tache_nom, R.id.tv_tache_date, R.id.tv_tache_employe };

    // Pour le filtrage, on se fait un curseur spécial
    String[] projection = { Tache.KEY_ID, Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID, Tache.KEY_fait, KEY_urgence };
    Cursor tacheCursor  = getActivity().getContentResolver().query(TodoContentProvider.CONTENT_URI_TACHE, projection, selection, selectionArgs, sortOrder);

    getLoaderManager().initLoader(0, null, this);
    tacheAdapter = new TacheAdapter(getContext(), R.layout.task_row, tacheCursor, from, to, 0, (TodoApplication) getActivity().getApplication());

    setListAdapter(tacheAdapter);
  }

  /**
   * Filtre les données avec la sélection des {@link Spinner}
   *
   * Construit la sélection de base (non-filtré, en ordre de date)
   * Construit la sélection en fonction du Spinner (voir le switch-case)
   * Change les données du {@link ListView} avec les données filtrées
   *
   * @param adapterView La {@link View} {@link android.widget.Adapter} (en l'occurence, un item dans le {@link Spinner}
   * @param view        Le {@link Spinner}
   * @param position    Position de l'item sélectionné
   * @param Id          Id de l'item sélectionné en
   *
   * @see android.widget.AdapterView.OnItemSelectedListener
   *
   * {@link <a href="http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/">Conversion d'{@link ArrayList} à {@link java.util.Arrays}</a>}
   */
  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int position, long Id) {
    setFiltre(spTacheFiltreFiltres.getSelectedItemId());

    // Valeurs par défaut
    String            selection     = null;
    ArrayList<String> selectionArgs = new ArrayList<>();
    String            sortOrder     = Tache.KEY_date + " ASC";

    // Quel spinner a été cliqué?
    switch (adapterView.getId()) {
      case R.id.sp_tache_filtre_filtres:
        // Si c'est le premier (filtres), on ne fait que changer l'ordre

        switch((int) spTacheFiltreFiltres.getSelectedItemId()) {
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
      case R.id.sp_tache_filtre_dates:
        // On ajoute un filtre de date

        switch((int) spTacheFiltreDates.getSelectedItemId()) {
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
      case R.id.sp_tache_filtre_employe:
        // On ajoute un filtre d'employés

        sortOrder = Tache.KEY_employe_assigne_ID + " ASC";

        switch((int) spTacheFiltreEmploye.getSelectedItemId()) {
          case 0:
            // Tous les employés
            break;
          default:
            // Dans ce cas, il a choisit un employé
            selection = Tache.KEY_employe_assigne_ID + "=?";
            selectionArgs.add(Long.toString(spTacheEmployeAssigneMap.get((int) spTacheFiltreEmploye.getSelectedItemId())));

            break;
        }

        break;
      case R.id.sp_tache_filtre_urgence:
        // On ajoute un filtre d'urgence

        sortOrder = KEY_urgence + " DESC";

        switch((int) spTacheFiltreUrgence.getSelectedItemId()) {
          case 0:
            // Tous les niveaux d'urgence
            break;
          default:
            // Dans ce cas, il a choisit un niveau d'urgence
            selection = KEY_urgence + "=?";
            selectionArgs.add(Long.toString(spTacheFiltreUrgence.getSelectedItemId() - 1)); // Bas, moyen, haut

            break;
        }

        break;
      case R.id.sp_tache_filtre_fait:
        // On ajoute un filtre de complétion

        sortOrder = Tache.KEY_fait + " ASC";

        switch((int) spTacheFiltreFait.getSelectedItemId()) {
          case 0:
            // Tous les niveaux de complétion
            break;
          default:
            // Dans ce cas, il a choisit un niveau de complétion
            selection = Tache.KEY_fait + "=?";
            selectionArgs.add(Long.toString(spTacheFiltreFait.getSelectedItemId() - 1)); // Pas fait, fait

            break;
        }

        break;
    }

    // Conversion d'un ArrayList<String> en String[]
    // Source : http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
    setRempliData(selection, (!selectionArgs.isEmpty()) ? selectionArgs.toArray(new String[selectionArgs.size()]) : null, sortOrder);
  }

  /**
   * Ne fait rien si rien n'arrive
   *
   * @param adapterView La {@link View} {@link android.widget.Adapter} (en l'occurence, un item dans le {@link Spinner}
   */
  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    // Ne fait rien!
  }

  /**
   * Modifie le {@link CursorLoader} pour les données téléchargées
   *
   * @param loader  Le {@link CursorLoader} déjà en place
   * @param data    Le {@link CursorLoader} contenant les nouvelles données
   */
  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    tacheAdapter.swapCursor(data);
  }

  /**
   * Change le {@link CursorLoader} lorsque l'application est déconnéctée, puisque les données ne
   * sont plus valides
   *
   * @param loader  Le {@link CursorLoader} déjà en place
   */
  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    tacheAdapter.swapCursor(null);
  }

  /**
   * Ouvre les détails d'une tâche lorsqu'appuyé
   *
   * Fait un nouveau {@link Intent} pour l'{@link TacheActivity}
   * Ajoute l'{@link Uri} dans le {@link Bundle}
   * Démarre {@link TacheActivity}
   *
   * @param listView  La {@link ListView}
   * @param view      La {@link View} cliquée
   * @param position  La position de l'item cliqué dans la liste
   * @param id        L'id de l'item
   */
  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);

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
}
