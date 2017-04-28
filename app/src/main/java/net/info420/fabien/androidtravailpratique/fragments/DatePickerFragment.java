package net.info420.fabien.androidtravailpratique.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import net.info420.fabien.androidtravailpratique.interfaces.OnTacheDateChangeListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * {@link android.app.Fragment} de {@link DatePicker}
 * Source : https://developer.android.com/guide/topics/ui/controls/pickers.html
 *
 * Il permet d'afficher un {@link DatePicker} dans une {@link android.app.Fragment}
 * Semblable à un {@link android.widget.PopupWindow}
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-04-23
 *
 * @see DialogFragment
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
  private static final String TAG = DatePickerFragment.class.getName();

  /**
   * Exécuté à la création du {@link DialogFragment}
   *
   * Vérifie si un argment de date a été envoyé.
   *  Si oui, c'est cette date qui est affichée
   *  Sinon,  c'est la date actuelle
   *
   * @param savedInstanceState  {@link Bundle} pouvant contenir des données
   * @return                    Un nouveau {@link DatePickerDialog} avec la bonne date
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);

    DateTime dateTime;

    if (getArguments() != null) {
      // On se sert de la date de la tâche
      dateTime = new DateTime().withMillis(getArguments().getInt("date") * 10000L);
    } else {
      // On utilise la date actuelle comme date par défaut
      dateTime = new DateTime(DateTimeZone.UTC);
    }

    // On retourne une nouvelle instance avec la date choisie
    // Oui, les mois commencent à zéro. Oui, c'est con.
    return new DatePickerDialog(getActivity(), this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
  }

  /**
   * Retourne une nouvelle instance de {@link DatePickerFragment}
   *
   * @return Nouvelle instance de {@link DatePickerFragment}
   */
  public static DatePickerFragment newInstance() {
    return new DatePickerFragment();
  }

  /**
   * Retourne une nouvelle instance de {@link DatePickerFragment}
   *
   * @param  date Date à afficher dans le {@link DatePickerFragment}, en millisecondes
   * @return      Nouvelle instance de {@link DatePickerFragment}, avec date en argument de {@link Bundle}
   *
   * @see Bundle
   */
  public static DatePickerFragment newInstance(int date) {
    DatePickerFragment fragment = new DatePickerFragment();

    Bundle args = new Bundle();
    args.putInt("date", date);
    fragment.setArguments(args);

    return fragment;
  }

  /**
   * Modifie la date sur l'{@link android.app.Activity}
   *
   * @param view  {@link android.view.View} qui a changé la date
   * @param annee                           int de l'année de la date
   * @param mois                            int du mois de la date
   * @param jour                            int de la journée de la date
   *
   * @see android.app.DatePickerDialog.OnDateSetListener
   * @see OnTacheDateChangeListener
   */
  @Override
  public void onDateSet(DatePicker view, int annee, int mois, int jour) {
    ((OnTacheDateChangeListener) getActivity()).setTacheDate((int) (new DateTime(annee, mois + 1, jour, 0, 0).getMillis() / 10000));
  }
}
