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
import android.widget.ListView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.activities.AjouterEmployeActivity;
import net.info420.fabien.androidtravailpratique.activities.EmployeActivity;
import net.info420.fabien.androidtravailpratique.adapters.EmployeAdapter;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;

/**
 * {@link android.app.Fragment} de la liste des employés
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    ?
 *
 * @see Employe
 * @see EmployeAdapter
 * @see TodoContentProvider
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html"
 *      target="_blank">
 *      Source : SQLite</a>
 */
public class EmployesListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = EmployesListeFragment.class.getName();

  private FloatingActionButton fabAjouterEmploye;

  private EmployeAdapter employeAdapter;

  /**
   * Exécuté à la création du {@link View}
   *
   * <ul>
   *  <li>Instancie la {@link View}</li>
   *  <li>Instancie l'interface</li>
   * </ul>
   *
   * @param   inflater            @See {@link LayoutInflater}
   * @param   container           La {@link View} qui contient le fragment
   * @param   savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return  La {@link View} instanciée
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_employes_liste, container, false);

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
   * @param savedInstanceState {@link Bundle} pouvant contenir des données
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    remplirData();

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
    String[] projection = {Employe.KEY_ID, Employe.KEY_nom, Employe.KEY_poste};

    return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI_EMPLOYE, projection, null, null, null);
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *  <li>Instancie le {@link FloatingActionButton}</li>
   *  <li>Ajoute les Listeners</li>
   * </ul>
   */
  private void initUI (View view) {
    fabAjouterEmploye = (FloatingActionButton)  view.findViewById(R.id.fab_ajouter_employe);

    fabAjouterEmploye.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Nouvelle tâche
        startActivity(new Intent(getContext(), AjouterEmployeActivity.class));
      }
    });
  }

  /**
   * Rempli la {@link ListView} des données
   *
   * <ul>
   *  <li>Construit un tableau de String, c'est le SELECT du {@link Cursor}</li>
   *  <li>Se sert du {@link CursorLoader} afin de placer les données dans un {@link EmployeAdapter}</li>
   *  <li>Ajoute l'{@link EmployeAdapter} à la {@link ListView}</li>
   * </ul>
   *
   * @see EmployeAdapter
   */
  private void remplirData() {
    // Affiche les champs de la base de données (nom)
    String[] from = new String[]{Employe.KEY_nom, Employe.KEY_poste};

    // Où on affiche les champs
    int[] to = new int[]{R.id.tv_employe_nom, R.id.tv_employe_poste};

    getLoaderManager().initLoader(0, null, this);
    employeAdapter = new EmployeAdapter(getContext(), R.layout.employe_row, null, from, to, 0);

    setListAdapter(employeAdapter);
  }

  /**
   * Modifie le {@link CursorLoader} pour les données téléchargées
   *
   * @param loader  Le {@link CursorLoader} déjà en place
   * @param data    Le {@link CursorLoader} contenant les nouvelles données
   */
  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    employeAdapter.swapCursor(data);
  }

  /**
   * Change le {@link CursorLoader} lorsque l'application est déconnéctée, puisque les données ne
   * sont plus valides
   *
   * @param loader  Le {@link CursorLoader} déjà en place
   */
  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    employeAdapter.swapCursor(null);
  }


  /**
   * Ouvre les détails d'un employé lorsqu'appuyé
   *
   * <ul>
   *  <li>Fait un nouveau {@link Intent} pour l'{@link EmployeActivity}</li>
   *  <li>Ajoute l'{@link Uri} dans le {@link Bundle}</li>
   *  <li>Démarre {@link EmployeActivity}</li>
   * </ul>
   *
   * @param listView  La {@link ListView}
   * @param view      La {@link View} cliquée
   * @param position  La position de l'item cliqué dans la liste
   * @param id        L'id de l'item
   */
  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);

    Intent i = new Intent(getContext(), EmployeActivity.class);
    i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + id));

    startActivity(i);
  }
}