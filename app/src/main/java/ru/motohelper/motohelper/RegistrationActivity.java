package ru.motohelper.motohelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrationActivity extends Activity implements View.OnClickListener {

    EditText userName;
    EditText userSecondName;
    EditText userLogin;
    EditText userPassword;
    EditText userPasswordConfirm;
    EditText userPhone;

    Button btnRegistrate;
    Button btnCancelRegistrate;

    User userToRegistrate;

    ServerUtilityUserRegistration registrator;

    final String REGISTER_USER = "registerUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        userName = (EditText) findViewById(R.id.editText_userName);
        userSecondName = (EditText) findViewById(R.id.editText_userSecondName);
        userLogin = (EditText) findViewById(R.id.editText_userLogin);
        userPassword = (EditText) findViewById(R.id.editText_userPassword);
        userPasswordConfirm = (EditText) findViewById(R.id.editText_userPasswordConfirm);
        userPhone = (EditText) findViewById(R.id.editText_userPhone);

        btnRegistrate = (Button) findViewById(R.id.btn_RegistrateUser);
        btnCancelRegistrate = (Button) findViewById(R.id.btn_CancelRegistrate);

        btnRegistrate.setOnClickListener(this);
        btnCancelRegistrate.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_RegistrateUser:
                if(userPassword.getText().toString().equals(userPasswordConfirm.getText().toString()) || userName.getText().toString() == "" || userSecondName.getText().toString() == "" || userLogin.getText().toString()=="" || userPhone.getText().toString() == "") {
                    userToRegistrate = new User(userLogin.getText().toString(), userPassword.getText().toString(), userName.getText().toString(), userSecondName.getText().toString(), userPhone.getText().toString());
                    registrator = new ServerUtilityUserRegistration(this, REGISTER_USER, userToRegistrate);
                    registrator.execute();
                    super.onBackPressed();
                }else{
                    Toast.makeText(this,getResources().getText(R.string.PasswordsIncorrect), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_CancelRegistrate:
                super.onBackPressed();
                break;
        }

    }
}
