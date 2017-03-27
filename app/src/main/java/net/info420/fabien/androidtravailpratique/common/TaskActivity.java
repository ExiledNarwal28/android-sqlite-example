package net.info420.fabien.androidtravailpratique.common;

import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Task;

import java.util.Date;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html#activities

public class TaskActivity extends AppCompatActivity {
  private final static String TAG = TaskActivity.class.getName();

  private TextView tvTaskName;
  private CheckBox cbTaskComplete;
  private TextView tvTaskUrgencyLevel;
  private TextView tvTaskDescription;
  private TextView tvTaskDate;
  private Button btnTaskAssignedEmployee;

  private Uri taskUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);

    initUI();

    // On va chercher les informations
    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      taskUri = extras.getParcelable(TaskerContentProvider.CONTENT_ITEM_TYPE_TASK);

      fillData(taskUri);
    }
  }

  private void initUI() {
    tvTaskName              = (TextView)  findViewById(R.id.tv_task_name);
    cbTaskComplete          = (CheckBox)  findViewById(R.id.cb_task_completed);
    tvTaskUrgencyLevel      = (TextView)  findViewById(R.id.tv_task_urgency_level);
    tvTaskDescription       = (TextView)  findViewById(R.id.tv_employee_job);
    tvTaskDate              = (TextView)  findViewById(R.id.tv_employee_mail);
    btnTaskAssignedEmployee = (Button)    findViewById(R.id.btn_task_assigned_employee);

    btnTaskAssignedEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Amener à la page de l'employé
      }
    });
  }

  private void fillData(Uri uri) {
    String[] projection = { Task.KEY_assigned_employee_ID,
                            Task.KEY_name,
                            Task.KEY_description,
                            Task.KEY_completed,
                            Task.KEY_date,
                            Task.KEY_urgency_level };

    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // Pour un Spinner
      // TODO : Enlever ceci si ça ne sert à rien
      /*
        String category = cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));
        // Pour un spinner
        for (int i = 0; i < mCategory.getCount(); i++) {

          String s = (String) mCategory.getItemAtPosition(i);
          if (s.equalsIgnoreCase(category)) {
            mCategory.setSelection(i);
          }
        }
      */

      // On mets les données dans l'UI
      tvTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Task.KEY_name)));
      cbTaskComplete.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_completed))) == 1); // Conversion en boolean
      tvTaskDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(Task.KEY_description)));
      btnTaskAssignedEmployee.setText(Integer.toString(cursor.getInt((cursor.getColumnIndexOrThrow(Task.KEY_assigned_employee_ID))))); // TODO : Convertir en employé

      // Source : http://stackoverflow.com/questions/13005116/android-convert-unix-time-to-gmt-time#13005144
      // Conversion en date
      // TODO : Vérifier l'année
      Date d = new Date(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_date)));
      SimpleDateFormat f = new SimpleDateFormat("EEEE d MMMM YYYY"); // Dimanche 1 janvier 1970
      f.setTimeZone(TimeZone.getTimeZone("GMT"));
      tvTaskDate.setText(f.format(d));

      // Conversion en niveau d'urgence textuel
      tvTaskUrgencyLevel.setText(((TaskerApplication) getApplication()).getUrgencyLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Task.KEY_urgency_level))));

      // Fermeture du curseur
      cursor.close();
    }
  }
}
