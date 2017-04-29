package net.info420.fabien.androidtravailpratique.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
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
 * {@link <a href="http://www.vogella.com/tutorials/AndroidListView/article.html">Les ListViews et Android</a>}
 */
public class EmployeAdapter extends SimpleCursorAdapter {
  private final String TAG = EmployeAdapter.class.getName();

  private final LayoutInflater inflater;

  // Le contenu du XML de l'Adapter
  private final class ViewHolder {
    TextView tvEmployeNom;
    TextView tvEmployePoste;
  }

  /**
   * Constructeur d'{@link EmployeAdapter}
   *
   * Envoie les paramètres à {@link SimpleCursorAdapter}
   * Instancie le {@link LayoutInflater}
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
   * Retourne le {@link LayoutInflater} avec le bon {@link android.text.Layout}
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
   * Instancie les {@link View}
   * Ajoute les informations aux champs
   * Mets les bons tags au viewHolder
   *
   * @param view      {@link View} qui est bindée
   * @param context   {@link Context} où afficher l'{@link EmployeAdapter}
   * @param cursor    {@link Cursor} de la sélection
   */
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    EmployeAdapter.ViewHolder viewHolder;

    viewHolder                = new EmployeAdapter.ViewHolder();
    viewHolder.tvEmployeNom   = (TextView) view.findViewById(R.id.tv_employe_nom);
    viewHolder.tvEmployePoste = (TextView) view.findViewById(R.id.tv_employe_poste);

    viewHolder.tvEmployeNom.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_nom)));
    viewHolder.tvEmployePoste.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_poste)));

    view.setTag(viewHolder);
  }
}