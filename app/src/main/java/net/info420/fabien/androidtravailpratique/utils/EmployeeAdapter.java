package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.common.TaskerApplication;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class EmployeeAdapter extends SimpleCursorAdapter {
  private final String TAG = EmployeeAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TaskerApplication application;

  private final class ViewHolder {
    public TextView tvEmployeeName;
    public TextView tvEmployeeJob;
  }

  public EmployeeAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, TaskerApplication application) {
    super(context, layout, cursor, from, to, flags);

    this.inflater = LayoutInflater.from(context);
    this.application = application;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    Log.d(TAG, "newView()");

    return inflater.inflate(R.layout.task_row, viewGroup, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    Log.d(TAG, "bindView()");

    EmployeeAdapter.ViewHolder viewHolder;

    viewHolder                = new EmployeeAdapter.ViewHolder();
    viewHolder.tvEmployeeName = (TextView) view.findViewById(R.id.tv_employee_name);
    viewHolder.tvEmployeeJob  = (TextView) view.findViewById(R.id.tv_employee_job);

    // Initialisation du UI
    viewHolder.tvEmployeeName.setText(cursor.getString(cursor.getColumnIndex(Employee.KEY_name)));
    viewHolder.tvEmployeeJob.setText(cursor.getString(cursor.getColumnIndex(Employee.KEY_job)));

    view.setTag(viewHolder);
  }
}