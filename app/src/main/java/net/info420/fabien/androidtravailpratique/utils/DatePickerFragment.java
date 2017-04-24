package net.info420.fabien.androidtravailpratique.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by fabien on 17-04-23.
 */

// Source : https://developer.android.com/guide/topics/ui/controls/pickers.html
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
  public static DatePickerFragment newInstance() {
    return new DatePickerFragment();
  }

  public static DatePickerFragment newInstance(int date) {
    DatePickerFragment f = new DatePickerFragment();

    Bundle args = new Bundle();
    args.putInt("date", date);
    f.setArguments(args);

    return f;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);

    DateTime dateTime;
    int year;
    int month;
    int day;

    // Vérification de sûreté.
    if (getArguments() != null) {
      // On se sert de la date de la tâche
      dateTime = new DateTime().withMillis(getArguments().getInt("date") * 10000L);
    } else {
      // On utilise la date actuelle comme date par défaut
      dateTime = new DateTime(DateTimeZone.UTC);
    }

    // On retourne une nouvelle instance
    return new DatePickerDialog(getActivity(), this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth()); // Oui, les mois commencent à zéro. Oui, c'est con.
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    ((OnTaskDateChangeListener) getActivity()).setTaskDate((int) (new DateTime(year, month + 1, day, 0, 0).getMillis() / 10000));
  }
}
