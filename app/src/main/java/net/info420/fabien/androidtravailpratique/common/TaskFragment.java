package net.info420.fabien.androidtravailpratique.common;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Created by fabien on 17-04-11.
 */

public class TaskFragment extends DialogFragment {

  public TaskFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_task_list, container, false);

    initUI(view);

    return view;
  }

  private void initUI(View view) {

  }
}
