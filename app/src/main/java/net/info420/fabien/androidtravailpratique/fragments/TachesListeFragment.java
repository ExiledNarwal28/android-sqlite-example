package net.info420.fabien.androidtravailpratique.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.activities.AjouterTacheActivity;
import net.info420.fabien.androidtravailpratique.activities.TacheActivity;
import net.info420.fabien.androidtravailpratique.adapters.TacheAdapter;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.helpers.EmployeHelper;
import net.info420.fabien.androidtravailpratique.models.Tache;

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
 * @see <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html"
 *      target="_blank">
 *      Source : SQLite</a>
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

  /**
   * Exécuté à la création du {@link View}
   *
   * <ul>
   *  <li>Instancie la {@link View}</li>
   *  <li>Instancie l'interface</li>
   *  <li>Ajoute les données d'employés dans le Spinner à cet effet</li>
   * </ul>
   *
   * @param inflater            {@link LayoutInflater}
   * @param container           La {@link View} qui contient le fragment
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return                    La {@link View} instanciée
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_taches_liste, container, false);

    initUI(view);

    return view;
  }

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *  <li>Place le {@link ListView} correctement</li>
   *  <li>Va chercher les données nécéssaires</li>
   *  <li>Enregistre le {@link ListView}</li>
   * </ul>
   *
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2);
    setRempliData();

    registerForContextMenu(getListView());
  }

  /**
   * Exécuté à la création du {@link Loader}
   *
   * <ul>
   *  <li>Construit et retourne un curseur avec la projection nécéssaire pour remplir le
   *  {@link ListView}</li>
   * </ul>
   *
   * @param   id    Id du {@link Loader}
   * @param   args  Arguments
   * @return  Un nouveau {@link CursorLoader} avec la projection
   */
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { Tache.KEY_ID, Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID, Tache.KEY_fait, Tache.KEY_urgence};

    return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI_TACHE, projection, null, null, null);
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *  <li>Instancie les {@link View}</li>
   *  <li>Ajoute les Listeners</li>
   *  <li>Ajoute la liste des employés au Spinner approprié</li>
   * </ul>
   *
   * @see EmployeHelper
   * @see EmployeHelper#fillEmployesSpinner(Context, Spinner, Map, boolean, boolean)
   */
  private void initUI(View view) {
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

    spTacheEmployeAssigneMap = new HashMap<>();
    EmployeHelper.fillEmployesSpinner(getActivity(), spTacheFiltreEmploye, spTacheEmployeAssigneMap, true, false);
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
   * Appelle rempliData avec un ordre par date
   */
  private void setRempliData() {
    rempliData(null, null, Tache.KEY_date + " ASC");
  }

  /**
   * Appelle rempliData avec un ordre par date et des données filtrées
   */
  private void setRempliData(String selection, String[] selectionArgs, String sortOrder) {
    rempliData(selection, selectionArgs, sortOrder);
  }

  /**
   * Rempli la {@link ListView} des données
   *
   * <ul>
   *  <li>Construit un tableau de String, c'est le SELECT du {@link Cursor}</li>
   *  <li>Construit un curseur avec les données reçue (selection + selectionArgs filtrés ou null)</li>
   *  <li>Se sert du {@link CursorLoader} afin de placer les données dans un {@link TacheAdapter}</li>
   *  <li>Ajoute l'{@link TacheAdapter} à la {@link ListView}</li>
   * </ul>
   *
   * @see TacheAdapter
   */
  private void rempliData(String selection, String[] selectionArgs, String sortOrder) {
    // Affiche les champs de la base de données (nom)
    String[] from = new String[] { Tache.KEY_nom, Tache.KEY_date, Tache.KEY_employe_assigne_ID};

    // Où on affiche les champs
    int[] to = new int[] { R.id.tv_tache_nom, R.id.tv_tache_date, R.id.tv_tache_employe };

    // Pour le filtrage, on se fait un curseur spécial
    String[] projection = { Tache.KEY_ID,
                            Tache.KEY_nom,
                            Tache.KEY_date,
                            Tache.KEY_employe_assigne_ID,
                            Tache.KEY_fait,
                            Tache.KEY_urgence };

    Cursor tacheCursor  = getActivity().getContentResolver().query( TodoContentProvider.CONTENT_URI_TACHE,
                                                                    projection,
                                                                    selection,
                                                                    selectionArgs,
                                                                    sortOrder);

    getLoaderManager().initLoader(0, null, this);
    tacheAdapter = new TacheAdapter(getContext(), R.layout.tache_row, tacheCursor, from, to, 0);

    setListAdapter(tacheAdapter);
  }

  /**
   * Filtre les données avec la sélection des {@link Spinner}
   *
   * <ul>
   *  <li>Construit la sélection de base (non-filtré, en ordre de date)</li>
   *  <li>Construit la sélection en fonction du Spinner (voir le switch-case)</li>
   *  <li>Change les données du {@link ListView} avec les données filtrées</li>
   * </ul>
   *
   * @param adapterView La {@link View} {@link android.widget.Adapter} (en l'occurence, un item dans le {@link Spinner}
   * @param view        Le {@link Spinner}
   * @param position    Position de l'item sélectionné
   * @param Id          Id de l'item sélectionné en
   *
   * @see android.widget.AdapterView.OnItemSelectedListener
   * @see DateHelper#getAujourdhuiMillis()
   * @see DateHelper#getLundiMillis()
   * @see DateHelper#getDimancheMillis()
   * @see DateHelper#getPremierJourDuMoisMillis()
   * @see DateHelper#getDernierJourDuMoisMillis()
   *
   * @see <a href="http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/"
   *      target="_blank">
   *      Source : Conversion d'{@link ArrayList} à {@link java.util.Arrays}</a>
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
            selectionArgs.add(DateHelper.getAujourdhuiMillis());

            break;
          case 2:
            // Cette semaine
            selection = Tache.KEY_date + " BETWEEN ? AND ?";
            selectionArgs.add(DateHelper.getLundiMillis());
            selectionArgs.add(DateHelper.getDimancheMillis());

            break;
          case 3:
            // Ce mois
            selection = Tache.KEY_date + " BETWEEN ? AND ?";
            selectionArgs.add(DateHelper.getPremierJourDuMoisMillis());
            selectionArgs.add(DateHelper.getDernierJourDuMoisMillis());

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
   * <ul>
   *  <li>Fait un nouveau {@link Intent} pour l'{@link TacheActivity}</li>
   *  <li>Ajoute l'{@link Uri} dans le {@link Bundle}</li>
   *  <li>Démarre {@link TacheActivity}</li>
   * </ul>
   *
   * @param listView  La {@link ListView}
   * @param view      La {@link View} cliquée
   * @param position  La position de l'item cliqué dans la liste
   * @param id        L'id de l'item
   *
   * @see <a href="http://stackoverflow.com/questions/15352486/cant-click-on-items-in-listview-with-custom-adapter"
   *      target="_blank">
   *      Source : S'assurer que onListItemClick fonctionne</a>
   */
  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);

    Intent i = new Intent(getContext(), TacheActivity.class);
    Uri taskUri = Uri.parse(TodoContentProvider.CONTENT_URI_TACHE + "/" + id);
    i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE, taskUri);

    startActivity(i);
  }
}
