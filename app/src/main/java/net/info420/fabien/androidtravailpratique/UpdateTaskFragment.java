package net.info420.fabien.androidtravailpratique;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;

public class UpdateTaskFragment extends Fragment {
  private final static String TAG = UpdateTaskFragment.class.getName();

  private Spinner spTaskEmployees;

  public UpdateTaskFragment() {
    initUI();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_update_task, container, false);
  }

  private void initUI() {
    // TODO : initUI

    spTaskEmployees = (Spinner) findView

    // Je mets la seule option actuelle dans le filtre des employés
    // TODO : Ajouter les employés dynamiquement
    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    adapterTaskFiltersEmployees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { getString(R.string.task_filter_all_employee) });
    spTaskFiltersEmployees.setAdapter(adapterTaskFiltersEmployees);
  }

  public void showDatePickerDialog(View v) {
    // DialogFragment newFragment = new DatePickerFragment();
    // newFragment.show(getSupportFragmentManager(), "datePicker");
    // TODO : Demander à l'activité de montrer le fragment du DatePicker
  }

  // Source : https://developer.android.com/guide/topics/ui/controls/pickers.html
  public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // On utilise la date actuelle comme date par défaut
      // TODO : Seulement s'il n'y a pas d'autres données
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // On retourne une nouvelle instance
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

    }
  }
}
