package net.info420.fabien.androidtravailpratique;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity {
  private final static String TAG = TaskActivity.class.getName();

  private TextView tvTaskName;
  private CheckBox cbTaskComplete;
  private TextView tvTaskUrgencyLevel;
  private TextView tvTaskDescription;
  private TextView tvTaskDate;
  private Button btnTaskAssignedEmployee;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);

    initUI();
  }

  private void initUI() {
    tvTaskName              = (TextView)  findViewById(R.id.tv_task_name);
    cbTaskComplete          = (CheckBox)  findViewById(R.id.cb_task_completed);
    tvTaskUrgencyLevel      = (TextView)  findViewById(R.id.tv_task_urgency_level);
    tvTaskDescription       = (TextView)  findViewById(R.id.tv_employee_job);
    tvTaskDate              = (TextView)  findViewById(R.id.tv_employee_mail);
    btnTaskAssignedEmployee = (Button)    findViewById(R.id.btn_task_assigned_employee);

    btnTaskAssignedEmployee.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Amener à la page de l'employé
      }
    });
  }
}
