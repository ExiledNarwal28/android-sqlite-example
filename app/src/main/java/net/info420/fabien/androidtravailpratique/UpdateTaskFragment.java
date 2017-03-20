package net.info420.fabien.androidtravailpratique;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpdateTaskFragment extends Fragment {
  private final static String TAG = UpdateTaskFragment.class.getName();

  public UpdateTaskFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_update_task, container, false);
  }
}
