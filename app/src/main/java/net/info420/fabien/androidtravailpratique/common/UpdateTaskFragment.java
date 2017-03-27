package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.info420.fabien.androidtravailpratique.R;

public class UpdateTaskFragment extends Fragment {
  private final static String TAG = UpdateTaskFragment.class.getName();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_update_task, container, false);
  }

  /*
   * private void saveState() {
   *   String category = (String) mCategory.getSelectedItem();
   *   String summary = mTitleText.getText().toString();
   *   String description = mBodyText.getText().toString();
   *
   *   // only save if either summary or description
   *   // is available
   *
   *   if (description.length() == 0 && summary.length() == 0) {
   *     return;
   *   }
   *
   *   ContentValues values = new ContentValues();
   *    values.put(TodoTable.COLUMN_CATEGORY, category);
   *    values.put(TodoTable.COLUMN_SUMMARY, summary);
   *    values.put(TodoTable.COLUMN_DESCRIPTION, description);
   *
   *    if (todoUri == null) {
   *      // New todo
   *      todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
   *    } else {
   *      // Update todo
   *      getContentResolver().update(todoUri, values, null, null);
   *    }
   *  }
   */
}
