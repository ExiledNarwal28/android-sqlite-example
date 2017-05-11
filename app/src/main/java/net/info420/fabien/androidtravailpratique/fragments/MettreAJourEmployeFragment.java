package net.info420.fabien.androidtravailpratique.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

/**
 * {@link android.app.Fragment} d'ajout et de modification d'employé
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    ?
 *
 * @see net.info420.fabien.androidtravailpratique.models.Employe
 * @see net.info420.fabien.androidtravailpratique.adapters.EmployeAdapter
 * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html"
 *      target="_blank">
 *      Source : SQLite</a>
 */
public class MettreAJourEmployeFragment extends Fragment {
  private final static String TAG = MettreAJourEmployeFragment.class.getName();

  /**
   * Exécuté à la création du {@link View}
   *
   * <ul>
   *  <li>Instancie la {@link View}</li>
   *  <li>Instancie l'interface</li>
   * </ul>
   *
   * @param   inflater            {@link LayoutInflater}
   * @param   container           La {@link View} qui contient le fragment
   * @param   savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return  La {@link View} instanciée
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_mettre_a_jour_employe, container, false);
  }
}