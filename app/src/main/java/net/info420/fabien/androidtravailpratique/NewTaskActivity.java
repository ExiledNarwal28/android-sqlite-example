package net.info420.fabien.androidtravailpratique;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {
  private final static String TAG = NewTaskActivity.class.getName();

  private EditText  etTaskName;
  private EditText  etTaskDescription;
  private CheckBox  cbTaskCompleted;
  private Spinner   spTaskUrgencyLevel;
  private Button    btnTaskDate;
  private Spinner   spTaskAssignedEmployee;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_task);

    initUI();
  }

  private void initUI() {
    etTaskName              = (EditText)  findViewById(R.id.et_task_name);
    etTaskDescription       = (EditText)  findViewById(R.id.et_task_description);
    cbTaskCompleted         = (CheckBox)  findViewById(R.id.cb_task_completed);
    spTaskUrgencyLevel      = (Spinner)   findViewById(R.id.sp_task_urgency_level);
    btnTaskDate             = (Button)    findViewById(R.id.btn_task_date);
    spTaskAssignedEmployee  = (Spinner)   findViewById(R.id.sp_task_assigned_employee);

    btnTaskDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePickerDialog(view);
      }
    });
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }

  // Source : https://developer.android.com/guide/topics/ui/controls/pickers.html
  public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // On utilise la date actuelle comme date par défaut
      // TODO : Seulement s'il n'y a pas d'autres données
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // On retourne une nouvelle instance
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

    }
  }
}