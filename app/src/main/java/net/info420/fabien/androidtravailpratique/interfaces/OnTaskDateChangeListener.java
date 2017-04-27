package net.info420.fabien.androidtravailpratique.interfaces;

/**
 * Created by fabien on 17-04-23.
 */

public interface OnTaskDateChangeListener {
  public long taskDate = 0;

  public void setTaskDate(int taskDate);

  public void onTaskDateChange();
}
