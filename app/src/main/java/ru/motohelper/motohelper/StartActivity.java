package ru.motohelper.motohelper;


import android.Manifest;

import android.app.Dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    Button btnRegister;
    Button setIpOk;
    Button setIpCancel;

    EditText email;
    EditText password;
    EditText ipAdress;

    CheckBox remember;

    Dialog modal;

    String ipString = "";
    final int MY_PERMISSIONS_REQUEST_CODE = 1;

    SettingsHolder appSettings;

    ServerUtilityUserLogin userLogin;
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        if (checkPermissions()) {
        } else {
            setPermissions();
        }


        appSettings = new SettingsHolder(this);
        appSettings.setIpAddress("http://motohelperapp.ru/dbmhelp/");

        btnLogin = (Button) findViewById(R.id.btLogin);
        btnRegister = (Button) findViewById(R.id.btnGoRegistrate);

        email = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        // загружаем пароль и логин, если есть
        if (!appSettings.getUserLogin().equals("")) {
            email.setText(appSettings.getUserLogin());
            password.setText(appSettings.getPassword());
        }
        remember = (CheckBox) findViewById(R.id.remember);

        //Setting listeners
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                createDialogForIP();

                try {

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            openFileInput("data.txt")));

                    // читаем содержимое
                    while ((ipString = br.readLine()) != null) {
                        ipAdress.setText(ipString);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.action_VK:
                Uri adress = Uri.parse("https://vk.com/motohelper_official");
                Intent openLink = new Intent(Intent.ACTION_VIEW, adress);
                startActivity(openLink);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    void createDialogForIP() {
        modal = new Dialog(this);
        modal.setTitle("Установите IP адрес сервера");
        modal.setContentView(R.layout.set_ip);
        ipAdress = (EditText) modal.findViewById(R.id.ip);
        if (!appSettings.getIpAddress().equals("")) {
            ipAdress.setText(appSettings.getIpAddress());
        }
        setIpOk = (Button) modal.findViewById(R.id.setIp);
        setIpCancel = (Button) modal.findViewById(R.id.setipcancel);
        setIpOk.setOnClickListener(this);
        setIpCancel.setOnClickListener(this);
        modal.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setIp:
                appSettings.setIpAddress(ipAdress.getText().toString());
                modal.cancel();
                break;
            case R.id.setipcancel:
                modal.cancel();
            case R.id.btLogin:
                if (remember.isChecked()) {
                    appSettings.setUserNamePassword(email.getText().toString(), password.getText().toString());
                }
                userLogin = new ServerUtilityUserLogin(StartActivity.this, email.getText().toString(), password.getText().toString());

                try {
                    currentUser =userLogin.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                appSettings.setCurrentUser(currentUser);
                    Toast.makeText(this, currentUser.getFirstName(), Toast.LENGTH_SHORT).show();


                //по условию

                if (currentUser.getLogin().equals("invalid")) {
                    Toast.makeText(StartActivity.this, getResources().getText(R.string.UserInvalid), Toast.LENGTH_SHORT).show();
                    break;
                }
                if (currentUser.getLogin().equals("noConnection")) {
                    Toast.makeText(StartActivity.this, getResources().getText(R.string.NoConnection), Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent mapIntent = new Intent(this, MapActivity.class);
                startActivity(mapIntent);


                break;
            case R.id.btnGoRegistrate:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }

    }

    private boolean checkPermissions() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    private void setPermissions() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        this.requestPermissions(permissions, MY_PERMISSIONS_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {

        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

        }
    }
}
