package net.info420.fabien.androidtravailpratique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
  private final static String TAG = MainActivity.class.getName();

  private ArrayAdapter<String> adapterTaskFiltersEmployees;

  private Spinner spTaskFiltersDates;
  private Spinner spTaskFiltersEmployees;
  private Spinner spTaskFiltersUrgencies;
  private Spinner spTaskFiltersCompletion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initUI();
  }

  protected void initUI() {
    spTaskFiltersDates = (Spinner) findViewById(R.id.sp_task_filters_dates);
    spTaskFiltersEmployees = (Spinner) findViewById(R.id.sp_task_filters_employees);
    spTaskFiltersUrgencies = (Spinner) findViewById(R.id.sp_task_filters_urgencies);
    spTaskFiltersCompletion = (Spinner) findViewById(R.id.sp_task_filters_completion);

    spTaskFiltersDates.setOnItemSelectedListener(this);
    spTaskFiltersEmployees.setOnItemSelectedListener(this);
    spTaskFiltersUrgencies.setOnItemSelectedListener(this);
    spTaskFiltersCompletion.setOnItemSelectedListener(this);

    // Je mets la seule option actuelle dans le filtre des employés
    // TODO : Ajouter les employés dynamiquement
    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { getString(R.string.task_filter_all_employee) });
    spTaskFiltersEmployees.setAdapter(adapterTaskFiltersEmployees);
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    switch (view.getId()) {
      case R.id.sp_task_filters_dates:
        // On ajoute un filtre de date
        break;
      case R.id.sp_task_filters_employees:
        // On ajoute un filtre d'employés
        break;
      case R.id.sp_task_filters_urgencies:
        // On ajoute un filtre de niveau d'urgence
        break;
      case R.id.sp_task_filters_completion:
        // On ajoute un filtre de complétion
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    // Ne fait rien!
  }
}
