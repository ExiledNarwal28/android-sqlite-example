package net.info420.fabien.androidtravailpratique.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

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

    int year;
    int month;
    int day;

    // Vérification de sûreté.
    if (savedInstanceState != null) {
      // On se sert de la date de la tâche
      DateTime dateTime = new DateTime().withMillis(savedInstanceState.getLong("taskDate") * 10000L);

      year  = dateTime.getYear();
      month = dateTime.getMonthOfYear() - 1; // Oui, les mois commencent à zéro. Oui, c'est con.
      day   = dateTime.getDayOfYear();

      Log.d(TAG, String.format("Date déjà là : %s %s %s", year, month, day));
    } else {
      // On utilise la date actuelle comme date par défaut
      final Calendar c  = Calendar.getInstance();

      year        = c.get(Calendar.YEAR);
      month       = c.get(Calendar.MONTH);
      day         = c.get(Calendar.DAY_OF_MONTH);

      Log.d(TAG, String.format("Date pas déjà là : %s %s %s", year, month, day));
    }

    // On retourne une nouvelle instance
    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    ((OnTaskDateChangeListener) getActivity()).setTaskDate((int) (new DateTime(year, month + 1, day, 0, 0).getMillis() / 10000));
    ((OnTaskDateChangeListener) getActivity()).onTaskDateChange();
  }
}
