package net.info420.fabien.androidtravailpratique.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.models.Employe;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class EmployeAdapter extends SimpleCursorAdapter {
  private final String TAG = EmployeAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TodoApplication application;

  private final class ViewHolder {
    public TextView tvEmployeeName;
    public TextView tvEmployeeJob;
  }

  // TODO : Vérifier si on a besoin de l'application
  public EmployeAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, TodoApplication application) {
    super(context, layout, cursor, from, to, flags);

    this.inflater = LayoutInflater.from(context);
    this.application = application;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return inflater.inflate(R.layout.employee_row, viewGroup, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    EmployeAdapter.ViewHolder viewHolder;

    viewHolder                = new EmployeAdapter.ViewHolder();
    viewHolder.tvEmployeeName = (TextView) view.findViewById(R.id.tv_employe_nom);
    viewHolder.tvEmployeeJob  = (TextView) view.findViewById(R.id.tv_employe_poste);

    // Initialisation du UI
    viewHolder.tvEmployeeName.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_nom)));
    viewHolder.tvEmployeeJob.setText(cursor.getString(cursor.getColumnIndex(Employe.KEY_poste)));

    view.setTag(viewHolder);
  }
}