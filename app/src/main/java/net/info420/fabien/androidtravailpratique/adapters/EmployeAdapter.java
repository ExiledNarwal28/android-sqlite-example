package net.info420.fabien.androidtravailpratique.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.helpers.EmployeHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;

/**
 * {@link android.widget.SimpleCursorAdapter} d'employés d'{@link net.info420.fabien.androidtravailpratique.fragments.EmployesListeFragment}
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-03-27
 *
 * @see Employe
 * @see net.info420.fabien.androidtravailpratique.fragments.EmployesListeFragment
 * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidListView/article.html"
 *      target="_blank">
 *      Source : Les ListViews et Android</a>
 */
public class EmployeAdapter extends SimpleCursorAdapter {
  private final String TAG = EmployeAdapter.class.getName();

  private final LayoutInflater inflater;

  // Le contenu du XML de l'Adapter
  private final class ViewHolder {
    TextView tvEmployeNom;
    TextView tvEmployePoste;
    TextView tvEmployeNbTaches;
  }

  /**
   * Constructeur d'{@link EmployeAdapter}
   *
   * <ul>
   *  <li>Envoie les paramètres à {@link SimpleCursorAdapter}</li>
   *  <li>Instancie le {@link LayoutInflater}</li>
   * </ul>
   *
   * @param context {@link Context} où afficher l'{@link EmployeAdapter}
   * @param layout  Id du {@link android.text.Layout} à utiliser
   * @param cursor  {@link Cursor} de la sélection
   * @param from    Données à mettre dans des champs
   * @param to      Id des champs à remplir
   * @param flags   Flags de {@link SimpleCursorAdapter}
   *
   * @see SimpleCursorAdapter
   */
  public EmployeAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
    super(context, layout, cursor, from, to, flags);

    this.inflater = LayoutInflater.from(context);
  }

  /**
   * Exécuter lors de la création d'un nouveau {@link View}
   *
   * <ul>
   *  <li>Retourne le {@link LayoutInflater} avec le bon {@link android.text.Layout}</li>
   * </ul>
   *
   * @param context   {@link Context} où afficher l'{@link EmployeAdapter}
   * @param cursor    {@link Cursor} de la sélection
   * @param viewGroup Groupes de {@link View} contenant la {@link View}
   * @return          La {@link View} retournée par le {@link LayoutInflater}
   */
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return inflater.inflate(R.layout.employe_row, viewGroup, false);
  }

  /**
   * Exécuter lors de l'association de champs de base de données à un {@link View}
   *
   * <ul>
   *  <li>Instancie les {@link View}</li>
   *  <li>Ajoute les informations aux champs</li>
   *  <li>Mets les bons tags au viewHolder</li>
   * </ul>
   *
   * @param view      {@link View} qui est bindée
   * @param context   {@link Context} où afficher l'{@link EmployeAdapter}
   * @param cursor    {@link Cursor} de la sélection
   */
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    EmployeAdapter.ViewHolder viewHolder;

    viewHolder                    = new EmployeAdapter.ViewHolder();
    viewHolder.tvEmployeNom       = (TextView) view.findViewById(R.id.tv_employe_nom);
    viewHolder.tvEmployePoste     = (TextView) view.findViewById(R.id.tv_employe_poste);
    viewHolder.tvEmployeNbTaches  = (TextView) view.findViewById(R.id.tv_employe_nb_taches);

    viewHolder.tvEmployeNom.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_nom)));
    viewHolder.tvEmployePoste.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_poste)));

    // Ajout du nombre de tâches
    Integer nbTaches = EmployeHelper.getEmployeNbTache(context, cursor.getInt(cursor.getColumnIndexOrThrow(Employe.KEY_ID)));
    String  nbTachesTexte;

    if (nbTaches == 0) {
      nbTachesTexte = context.getString(R.string.info_aucune_tache_assignee);
    } else if (nbTaches == 1) {
      nbTachesTexte = Integer.toString(nbTaches) + " " + context.getString(R.string.tache_assignee);
    } else {
      nbTachesTexte = Integer.toString(nbTaches) + " " + context.getString(R.string.taches_assignees);
    }

    viewHolder.tvEmployeNbTaches.setText(nbTachesTexte);

    view.setTag(viewHolder);
  }
}