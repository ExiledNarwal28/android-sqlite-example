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
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class TacheAdapter extends SimpleCursorAdapter {
  private final String TAG = TacheAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TodoApplication application;

  private final class ViewHolder {
    TextView tvTaskName;
    TextView tvTaskDate;
    TextView tvTaskEmployee;
    TextView tvTaskUrgencyLevel;
    CheckBox cbTaskCompleted;
  }

  // TODO : Passe un contexte plutôt qu'une Application
  public TacheAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, TodoApplication application) {
    super(context, layout, cursor, from, to, flags);

    this.inflater = LayoutInflater.from(context);
    this.application = application;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return inflater.inflate(R.layout.task_row, viewGroup, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    TacheAdapter.ViewHolder viewHolder;

    viewHolder                    = new TacheAdapter.ViewHolder();
    viewHolder.tvTaskName         = (TextView) view.findViewById(R.id.tv_task_name);
    viewHolder.tvTaskDate         = (TextView) view.findViewById(R.id.tv_employe_email);
    viewHolder.tvTaskEmployee     = (TextView) view.findViewById(R.id.tv_task_employee);
    viewHolder.tvTaskUrgencyLevel = (TextView) view.findViewById(R.id.tv_task_urgency_level);
    viewHolder.cbTaskCompleted    = (CheckBox) view.findViewById(R.id.cb_tache_fait);

    // Initialisation du UI
    viewHolder.tvTaskName.setText(cursor.getString(cursor.getColumnIndex(Tache.KEY_nom)));
    viewHolder.tvTaskDate.setText(DateHelper.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_date))));
    viewHolder.cbTaskCompleted.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_fait))) == 1); // Conversion en boolean

    // viewHolder.tvTaskUrgencyLevel.setText(application.getUrgence(urgencyLevel));
    viewHolder.tvTaskUrgencyLevel.setBackgroundColor(ColorHelper.getUrgencyLevelColor(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_urgence)), application));

    if (!cursor.isNull(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID))) {
      // On doit aller chercher le nom de l'employé

      Cursor employeeCursor = context.getContentResolver().query( TodoContentProvider.CONTENT_URI_EMPLOYE,
                                                                  new String[] { Employe.KEY_ID, Employe.KEY_nom},
                                                                  Employe.KEY_ID + " =?",
                                                                  new String[] { Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID))) },
                                                                  null);

      if (employeeCursor != null) {
        employeeCursor.moveToFirst();

        // viewHolder.tvTaskEmployee.setText(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));

        // Fermeture du curseur
        employeeCursor.close();
      }
    } else {
      viewHolder.tvTaskEmployee.setText(context.getString(R.string.tache_aucun_employe));
    }

    view.setTag(viewHolder);

    // TODO : Vérifier si c'est utile
    viewHolder.cbTaskCompleted.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO : Complete / uncomplete
      }
    });
  }
}