package net.info420.fabien.androidtravailpratique.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

public class UpdateEmployeeFragment extends Fragment {
  private final static String TAG = UpdateEmployeeFragment.class.getName();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_update_employee, container, false);
  }
}