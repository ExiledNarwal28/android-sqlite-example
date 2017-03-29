package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.common.TaskerApplication;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class TaskAdapter extends SimpleCursorAdapter {
  private final String TAG = TaskAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TaskerApplication application;

  private final class ViewHolder {
    public TextView tvTaskName;
    public TextView tvTaskDate;
    public CheckBox cbTaskCompleted;
  }

  public TaskAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, TaskerApplication application) {
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

    TaskAdapter.ViewHolder viewHolder;

    viewHolder                  = new TaskAdapter.ViewHolder();
    viewHolder.tvTaskName       = (TextView) view.findViewById(R.id.tv_task_name);
    viewHolder.tvTaskDate       = (TextView) view.findViewById(R.id.tv_task_date);
    viewHolder.cbTaskCompleted  = (CheckBox) view.findViewById(R.id.cb_task_completed);

    // Initialisation du UI
    viewHolder.tvTaskName.setText(cursor.getString(cursor.getColumnIndex(Task.KEY_name)));
    viewHolder.tvTaskDate.setText(application.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_date))));

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