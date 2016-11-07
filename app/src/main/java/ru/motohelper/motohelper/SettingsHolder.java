package ru.motohelper.motohelper;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.String;


public class SettingsHolder {
    Context context;
    SharedPreferences sharedPreferences;
    private String ipAddress;
    private final String ipAddressSettingName = "IP_ADDRESS";

    private String userLogin;
    private final String userLoginSettingName = "USER_LOGIN";

    private String userFirstName;
    private final String userFirstNameSettingName = "USER_FIRST_NAME";

    private String userSecondName;
    private final String userSecondNameSettingName = "USER_SECOND_NAME";

    private String password;
    private final String passwordSettingName = "PASSWORD";

    private String userPhone;
    private final String userPhoneSettingName = "USER_PHONE";



    private User currentUser;

    public SettingsHolder(Context cntx) {
        this.context = cntx;
        sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);

    }

    public void setIpAddress(String ipAddress) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(ipAddressSettingName, ipAddress);
        ed.commit();

    }

    public String getIpAddress() {
        String st = sharedPreferences.getString(ipAddressSettingName, "");
        return st;

    }
    public void setUserLogin(String userLogin){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(ipAddressSettingName, userLogin);
        ed.commit();
    }

    public void setUserNamePassword(String userName, String password) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(userLoginSettingName, userName);
        ed.putString(passwordSettingName, password);

        ed.commit();
    }

    public void setCurrentUser(User user) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(userLoginSettingName, user.getLogin());
        ed.putString(passwordSettingName, user.getPassword());

        ed.putString(userFirstNameSettingName, user.getFirstName());
        ed.putString(userSecondNameSettingName, user.getSecondName());

        ed.putString(userPhoneSettingName, user.getPhone());
        ed.commit();
    }

    public User getCurrentUser() {
        return new User(sharedPreferences.getString(userLoginSettingName,""), sharedPreferences.getString(passwordSettingName,""),
                sharedPreferences.getString(userFirstNameSettingName,""), sharedPreferences.getString(userSecondNameSettingName,""),
                sharedPreferences.getString(userPhoneSettingName,""));
    }

    public String getUserLogin() {
        return sharedPreferences.getString(userLoginSettingName, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(passwordSettingName, "");
    }


}
