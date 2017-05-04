package net.info420.fabien.androidtravailpratique.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.helpers.EmployeHelper;
import net.info420.fabien.androidtravailpratique.models.Tache;

/**
 * {@link android.widget.SimpleCursorAdapter} de tâches d'{@link net.info420.fabien.androidtravailpratique.fragments.TachesListeFragment}
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-03-27
 *
 * @see Tache
 * @see net.info420.fabien.androidtravailpratique.fragments.TachesListeFragment
 * @see TodoContentProvider
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidListView/article.html"
 *      target="_blank">
 *      Source : Les ListViews et Android</a>
 */
public class TacheAdapter extends SimpleCursorAdapter {
  private final String TAG = TacheAdapter.class.getName();

  private final LayoutInflater inflater;

  // Le contenu du XML de l'Adapter
  private final class ViewHolder {
    TextView tvTacheNom;
    TextView tvTacheDate;
    TextView tvTacheEmploye;
    TextView tvTacheUrgence;
    CheckBox cbTacheFait;
  }

  /**
   * Constructeur d'{@link TacheAdapter}
   *
   * <ul>
   *  <li>Envoie les paramètres à {@link SimpleCursorAdapter}</li>
   *  <li>Instancie le {@link LayoutInflater}</li>
   * </ul>
   *
   * @param context {@link Context} où afficher l'{@link TacheAdapter}
   * @param layout  Id du {@link android.text.Layout} à utiliser
   * @param cursor  {@link Cursor} de la sélection
   * @param from    Données à mettre dans des champs
   * @param to      Id des champs à remplir
   * @param flags   Flags de {@link SimpleCursorAdapter}
   *
   * @see SimpleCursorAdapter
   */
  public TacheAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
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
    return inflater.inflate(R.layout.tache_row, viewGroup, false);
  }

  /**
   * Exécuter lors de l'association de champs de base de données à un {@link View}
   *
   * <ul>
   *  <li>Instancie les {@link View}</li>
   *  <li>Ajoute les informations aux champs</li>
   *  <li>Ajoute le nom de l'employé (doit faire une requête séparée)</li>
   *  <li>Ajoute les Listeners</li>
   *  <li>Mets les bons tags au viewHolder</li>
   * </ul>
   *
   * @param view      {@link View} qui est bindée
   * @param context   {@link Context} où afficher l'{@link EmployeAdapter}
   * @param cursor    {@link Cursor} de la sélection
   */
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    final TacheAdapter.ViewHolder viewHolder;

    viewHolder                = new TacheAdapter.ViewHolder();
    viewHolder.tvTacheNom     = (TextView) view.findViewById(R.id.tv_tache_nom);
    viewHolder.tvTacheDate    = (TextView) view.findViewById(R.id.tv_tache_date);
    viewHolder.tvTacheEmploye = (TextView) view.findViewById(R.id.tv_tache_employe);
    viewHolder.tvTacheUrgence = (TextView) view.findViewById(R.id.tv_tache_urgence);
    viewHolder.cbTacheFait    = (CheckBox) view.findViewById(R.id.cb_tache_fait);

    viewHolder.tvTacheNom.setText(cursor.getString(cursor.getColumnIndex(Tache.KEY_nom)));
    viewHolder.tvTacheDate.setText(DateHelper.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_date))));
    viewHolder.cbTacheFait.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_fait))) == 1); // Conversion en boolean

    viewHolder.tvTacheUrgence.setBackgroundColor(ColorHelper.getUrgencyLevelColor(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_urgence)), context));

    // Vérifie s'il y a un employé associé
    if (!cursor.isNull(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID))) {
      viewHolder.tvTacheEmploye.setText(EmployeHelper.getEmployeNom(context, cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID))));
    } else {
      viewHolder.tvTacheEmploye.setText(context.getString(R.string.tache_aucun_employe));
    }

    view.setTag(viewHolder);
  }
}