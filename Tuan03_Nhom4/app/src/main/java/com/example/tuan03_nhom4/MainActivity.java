package com.example.tuan03_nhom4;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Exit(View view) {
        finish();
    }

    public void SignUp(View view) {
        TextView userName = findViewById(R.id.txtUserName);
        TextView password = findViewById(R.id.txtPass);
        TextView retype = findViewById(R.id.txtRepass);
        TextView birthday = findViewById(R.id.txtBirthdate);

        RadioGroup gender = findViewById(R.id.rdbGender);
        RadioButton rdbGenderChecked = findViewById(gender.getCheckedRadioButtonId());
        CheckBox tennis = findViewById(R.id.chkTennis);
        CheckBox futbal = findViewById(R.id.chkFutbal);
        CheckBox other = findViewById(R.id.chkOthers);


        String txtUserName = userName.getText().toString();
        String txtPassword = password.getText().toString();
        String txtRetype = retype.getText().toString();
        String txtBirthday = birthday.getText().toString();
        String txtGender = rdbGenderChecked.getText().toString();
        String txthobbies = tennis.isChecked() ? tennis.getText().toString() : "";
        txthobbies += (futbal.isChecked() ?", " + futbal.getText().toString() : "");
        txthobbies = txthobbies + (other.isChecked() ?", " + other.getText().toString() : "");

        if (txtPassword.length() > 0 && txtRetype.length() > 0) {
            //compare password with retype
            if (txtPassword.equals(txtRetype)) {
                //check format date dd/MM/yyyy
                if (isFormatDate(txtBirthday)) {
                    String sToast = new StringBuilder().append("Username: ").append(txtUserName).append("\n")
                            .append("Password: ").append(txtPassword).append("\n")
                            .append("Retype: ").append(txtRetype).append("\n")
                            .append("Birthday: ").append(txtBirthday).append("\n")
                            .append("Gender: ").append(txtGender).append("\n")
                            .append("Hobbies: ").append(txthobbies)
                            .toString();
                    Toast.makeText(this, sToast, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Sai định dạng ngày sinh. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Nhập lại mật khẩu không khớp. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isFormatDate(String sDate) {
        return sDate.matches("\\b(0?[1-9]|[12]\\d|3[01])[\\/\\-.](0?[1-9]|[12]\\d|3[01])[\\/\\-.](\\d{2}|\\d{4})\\b");
    }

    public void Reset(View view) {
        TextView userName = findViewById(R.id.txtUserName);
        TextView password = findViewById(R.id.txtPass);
        TextView retype = findViewById(R.id.txtRepass);
        TextView birthday = findViewById(R.id.txtBirthdate);
        RadioButton male=findViewById(R.id.rdbMale);
        CheckBox chbTennis= findViewById(R.id.chkTennis);
        CheckBox chbFutbal= findViewById(R.id.chkFutbal);
        CheckBox chbOther= findViewById(R.id.chkOthers);

        chbTennis.setChecked(false);
        chbFutbal.setChecked(false);
        chbOther.setChecked(false);
        male.setChecked(true);

        userName.setText("");
        password.setText("");
        retype.setText("");
        birthday.setText("");
    }

    public void selectDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView tetBirthday = findViewById(R.id.txtBirthdate);
                String sBirthday = dayOfMonth + "/" + (month+1) + "/" + year;
                tetBirthday.setText(sBirthday);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}