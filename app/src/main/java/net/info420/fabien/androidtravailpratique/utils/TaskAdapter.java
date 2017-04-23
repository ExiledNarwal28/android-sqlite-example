package net.info420.fabien.androidtravailpratique.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.common.TaskerApplication;

import java.util.List;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class TaskAdapter extends SimpleCursorAdapter implements Filterable {
  private final String TAG = TaskAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TaskerApplication application;

  private List<String>originalData = null;
  private List<String>filteredData = null;

  private final class ViewHolder {
    public TextView tvTaskName;
    public TextView tvTaskDate;
    public TextView tvTaskUrgencyLevel;
    public CheckBox cbTaskCompleted;
  }

  public TaskAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, TaskerApplication application) {
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
    TaskAdapter.ViewHolder viewHolder;

    viewHolder                    = new TaskAdapter.ViewHolder();
    viewHolder.tvTaskName         = (TextView) view.findViewById(R.id.tv_task_name);
    viewHolder.tvTaskDate         = (TextView) view.findViewById(R.id.tv_task_date);
    viewHolder.tvTaskUrgencyLevel = (TextView) view.findViewById(R.id.tv_task_urgency_level);
    viewHolder.cbTaskCompleted    = (CheckBox) view.findViewById(R.id.cb_task_completed);

    // Initialisation du UI
    viewHolder.tvTaskName.setText(cursor.getString(cursor.getColumnIndex(Task.KEY_name)));
    viewHolder.tvTaskDate.setText(TaskerApplication.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_date))));
    viewHolder.cbTaskCompleted.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_completed))) == 1); // Conversion en boolean

    // viewHolder.tvTaskUrgencyLevel.setText(application.getUrgencyLevel(urgencyLevel));
    viewHolder.tvTaskUrgencyLevel.setBackgroundColor(application.getUrgencyLevelColor(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_urgency_level))));

    view.setTag(viewHolder);

    // TODO : Vérifier si c'est utile
    viewHolder.cbTaskCompleted.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO : Complete / uncomplete
      }
    });
  }

  // TODO : Rendre le ListView filtrable
  @Override
  public Filter getFilter() {

    Filter filter = new Filter() {
      @SuppressWarnings("unchecked")
      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        /*
        filteredData = (List<String>) results.values;
        notifyDataSetChanged();
        */
      }

      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        /*
        FilterResults results = new FilterResults();
        ArrayList<String> FilteredArrayNames = new ArrayList<String>();

        // perform your search here using the searchConstraint String.

        constraint = constraint.toString().toLowerCase();
        for (int i = 0; i < mDatabaseOfNames.size(); i++) {
          String dataNames = mDatabaseOfNames.get(i);
          if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
            FilteredArrayNames.add(dataNames);
          }
        }

        results.count = FilteredArrayNames.size();
        results.values = FilteredArrayNames;
        Log.e("VALUES", results.values.toString());

        return results;
        */
        return null;
      }
    };

    return filter;
  }
}