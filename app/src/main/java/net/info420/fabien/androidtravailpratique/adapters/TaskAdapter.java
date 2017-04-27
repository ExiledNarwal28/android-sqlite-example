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
import net.info420.fabien.androidtravailpratique.application.TaskerApplication;
import net.info420.fabien.androidtravailpratique.data.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employee;
import net.info420.fabien.androidtravailpratique.models.Task;

/**
 * Created by fabien on 17-03-27.
 */

// Source : http://www.vogella.com/tutorials/AndroidListView/article.html

public class TaskAdapter extends SimpleCursorAdapter {
  private final String TAG = TaskAdapter.class.getName();

  private final LayoutInflater inflater;
  private final TaskerApplication application;

  private final class ViewHolder {
    TextView tvTaskName;
    TextView tvTaskDate;
    TextView tvTaskEmployee;
    TextView tvTaskUrgencyLevel;
    CheckBox cbTaskCompleted;
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
    viewHolder.tvTaskEmployee     = (TextView) view.findViewById(R.id.tv_task_employee);
    viewHolder.tvTaskUrgencyLevel = (TextView) view.findViewById(R.id.tv_task_urgency_level);
    viewHolder.cbTaskCompleted    = (CheckBox) view.findViewById(R.id.cb_task_completed);

    // Initialisation du UI
    viewHolder.tvTaskName.setText(cursor.getString(cursor.getColumnIndex(Task.KEY_name)));
    viewHolder.tvTaskDate.setText(TaskerApplication.getDate(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_date))));
    viewHolder.cbTaskCompleted.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_completed))) == 1); // Conversion en boolean

    // viewHolder.tvTaskUrgencyLevel.setText(application.getUrgencyLevel(urgencyLevel));
    viewHolder.tvTaskUrgencyLevel.setBackgroundColor(application.getUrgencyLevelColor(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_urgency_level))));

    if (!cursor.isNull(cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID))) {
      // On doit aller chercher le nom de l'employé

      Cursor employeeCursor = context.getContentResolver().query( TaskerContentProvider.CONTENT_URI_EMPLOYEE,
                                                                  new String[] { Employee.KEY_ID, Employee.KEY_name },
                                                                  Employee.KEY_ID + " =?",
                                                                  new String[] { Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID))) },
                                                                  null);

      if (employeeCursor != null) {
        employeeCursor.moveToFirst();

        // viewHolder.tvTaskEmployee.setText(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employee.KEY_name)));

        // Fermeture du curseur
        employeeCursor.close();
      }
    } else {
      viewHolder.tvTaskEmployee.setText(context.getString(R.string.task_no_employee));
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