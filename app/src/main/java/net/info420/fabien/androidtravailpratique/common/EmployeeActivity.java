package net.info420.fabien.androidtravailpratique.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;

public class EmployeeActivity extends AppCompatActivity {
  private final static String TAG = EmployeeActivity.class.getName();

  private TextView  tvEmployeeName;
  private TextView  tvEmployeeJob;
  private TextView  tvEmployeeMail;
  private TextView  tvEmployeePhone;
  private Button    btnEmployeeSendSms;
  private Button    btnEmployeeCall;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee);

    initUI();
  }

  private void initUI() {
    tvEmployeeName      = (TextView)  findViewById(R.id.tv_employee_name);
    tvEmployeeJob       = (TextView)  findViewById(R.id.tv_employee_job);
    tvEmployeeMail      = (TextView)  findViewById(R.id.tv_employee_mail);
    tvEmployeePhone     = (TextView)  findViewById(R.id.tv_employee_phone);
    btnEmployeeSendSms  = (Button)    findViewById(R.id.btn_employee_send_sms);
    btnEmployeeCall     = (Button)    findViewById(R.id.btn_employee_call);

    btnEmployeeSendSms.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Envoie un SMS à l'employé
      }
    });

    btnEmployeeCall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Appeler l'employé
      }
    });
  }
}
