package net.info420.fabien.androidtravailpratique.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

/**
 * {@link android.app.Fragment} d'ajout et de modification de tâches
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    ?
 *
 * @see net.info420.fabien.androidtravailpratique.models.Tache
 * @see net.info420.fabien.androidtravailpratique.adapters.TacheAdapter
 * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
 *
 * {@link <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html">Source SQLite</a>}
 */
public class MettreAJourTacheFragment extends Fragment {
  private final static String TAG = MettreAJourTacheFragment.class.getName();

  /**
   * Exécuté à la création du {@link View}
   *
   * Instancie la {@link View}
   * Instancie l'interface
   *
   * @param inflater            @See {@link LayoutInflater}
   * @param container           La {@link View} qui contient le fragment
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return                    La {@link View} instanciée
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_mettre_a_jour_tache, container, false);
  }
}
