package net.info420.fabien.androidtravailpratique.interfaces;

/**
 * Interface servant aux {@link android.app.Activity} qui ont un
 * {@link net.info420.fabien.androidtravailpratique.fragments.DatePickerFragment}
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-23
 *
 * @see net.info420.fabien.androidtravailpratique.models.Tache
 * @see net.info420.fabien.androidtravailpratique.activities.AjouterTacheActivity
 * @see net.info420.fabien.androidtravailpratique.activities.ModifierTacheActivity
 *
 */
public interface OnTacheDateChangeListener {
  void setTacheDate(int tacheDate);

  void onTacheDateChange();
}
