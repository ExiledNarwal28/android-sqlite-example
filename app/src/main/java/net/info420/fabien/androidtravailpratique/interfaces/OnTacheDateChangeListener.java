package net.info420.fabien.androidtravailpratique.interfaces;

/**
 * Created by fabien on 17-04-23.
 */

public interface OnTacheDateChangeListener {
  public long taskDate = 0;

  public void setTacheDate(int tacheDate);

  public void onTaskDateChange();
}
