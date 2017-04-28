package net.info420.fabien.androidtravailpratique.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.helpers.StringHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html#activities

public class TacheActivity extends Activity {
  private final static String TAG = TacheActivity.class.getName();

  private TextView tvTaskName;
  private CheckBox cbTaskComplete;
  private TextView tvTaskUrgencyLevel;
  private TextView tvTaskDescription;
  private TextView tvTaskDate;
  private Button btnTaskAssignedEmployee;

  private Uri taskUri;

  private int assignedEmployeeId; // Valeur par défaut

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);

    initUI();

    // On va chercher les informations
    taskUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      taskUri = extras.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE);
      Log.d(TAG, taskUri.getPath());

      fillData(taskUri);
    }
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_task);

    ColorHelper.setStatusBarColor(this);


    tvTaskName              = (TextView)  findViewById(R.id.tv_task_name);
    cbTaskComplete          = (CheckBox)  findViewById(R.id.cb_tache_fait);
    tvTaskUrgencyLevel      = (TextView)  findViewById(R.id.tv_task_urgency_level);
    tvTaskDescription       = (TextView)  findViewById(R.id.tv_task_description);
    tvTaskDate              = (TextView)  findViewById(R.id.tv_task_date);
    btnTaskAssignedEmployee = (Button)    findViewById(R.id.btn_task_assigned_employee);

    btnTaskAssignedEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), EmployeActivity.class);
        Uri employeeUri = Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + assignedEmployeeId);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, employeeUri);

        startActivity(i);
      }
    });
  }

  private void fillData(Uri taskUri) {
    String[] projection = { Tache.KEY_employe_assigne_ID,
                            Tache.KEY_nom,
                            Tache.KEY_description,
                            Tache.KEY_fait,
                            Tache.KEY_date,
                            Tache.KEY_urgence};

    Cursor cursor = getContentResolver().query(taskUri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // On mets les données dans l'UI
      tvTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow(Tache.KEY_nom)));
      cbTaskComplete.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_fait))) == 1); // Conversion en boolean
      tvTaskDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(Tache.KEY_description)));

      // Conversion en date
      tvTaskDate.setText(DateHelper.getLongueDate(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_date))));

      // Conversion en niveau d'urgence textuel
      tvTaskUrgencyLevel.setText(StringHelper.getUrgence(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_urgence)), this));

      // Et maintenant? Il faut afficher le nom de l'employé dans le bouton d'employé assigné.

      Log.d(TAG, Integer.toString(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID)));

      // Pour rediriger avec le bouton
      assignedEmployeeId = cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID));

      // Vérification de la colonne
      if (assignedEmployeeId != 0) {
        String[] employeeProjection = { Employe.KEY_nom};

        Uri employeeUri = Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID)));

        Cursor employeeCursor = getContentResolver().query(employeeUri, employeeProjection, null, null, null);

        if (employeeCursor != null) {
          employeeCursor.moveToFirst();

          btnTaskAssignedEmployee.setText(employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));

          // Fermeture du curseur
          employeeCursor.close();
        }
      }

      // Fermeture du curseur
      cursor.close();
    }

    // On cache le bouton si aucun employé n'est assigné.
    if (assignedEmployeeId == 0) {
      btnTaskAssignedEmployee.setVisibility(View.GONE);
    }
  }

  // On rafraîchit quand on revient dans l'Activity (ex. : en revenant d'ModifierTacheActivity)
  @Override
  public void onResume() {
    super.onResume();
    fillData(taskUri);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    Log.d(TAG, "We got it");
    getMenuInflater().inflate(R.menu.menu_item, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_edit:
        Intent i = new Intent(this, ModifierTacheActivity.class);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE, taskUri);
        startActivity(i);
        break;
      case R.id.menu_delete:
        getContentResolver().delete(taskUri, null, null);
        finish();
        break;
      default:
        break;
    }
    return true;
  }
}