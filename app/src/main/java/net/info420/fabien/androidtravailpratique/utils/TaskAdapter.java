package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class TaskAdapter extends SimpleCursorAdapter {
  private LayoutInflater inflater;

  public TaskAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
    super(context, layout, c, from, to, flags);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return inflater.inflate(R.layout.task_row, viewGroup, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    /*
    if(cursor.getPosition()%2==1) {
      view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
    }
    else {
      view.setBackgroundColor(context.getResources().getColor(R.color.background_even));
    }
    */

    TextView tvTaskName = (TextView) view.findViewById(R.id.tv_task_name);
    TextView tvTaskDate = (TextView) view.findViewById(R.id.tv_task_date);

    tvTaskName.setText(cursor.getString(cursor.getColumnIndex(Task.KEY_name)));
    tvTaskDate.setText(cursor.getString(cursor.getColumnIndex(Task.KEY_date)));
  }
}
