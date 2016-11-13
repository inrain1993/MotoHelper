package ru.motohelper.motohelper;


import android.content.Context;
import android.content.SharedPreferences;


public class SettingsHolder {
    private Context context;
    private SharedPreferences sharedPreferences;
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

    private int allowAutoRefresh;
    private final String getAllowAutoRefreshSettingName = "REFRESH_AUTO";

    private boolean showZoomButton;
    private final String getShowZoomButtonSettingName = "SHOW_ZOOM_BUTTON";

    private boolean showMyLocationButton;
    private final String getShowMyLocationButtonSettingName = "SHOW_MYLOCBUTTON";

    private boolean showCompassButton;
    private final String getShowCompassButtonSettingName ="SHOW_COMPASS";

    private boolean showOnlyMyMarkers;
    private final String getShowOnlyMyMarkers = "SHOW_MY_MARKERS";

    private boolean showOnlyAccidents;
    private final String getShowOnlyAccidents = "SHOW_ACCIDENTS";

    private boolean showOnlyCorrupts;
    private final String getShowOnlyCorrupts = "SHOW_CORRUPTS";

    private boolean showOnlyLookFriends;
    private final String getShowOnlyLookFriends = "SHOW_LOOKFRIENDS";

    private String refreshTimeOut;
    private final String getRefreshTimeOutSettingName = "REFRESH_TIMEOUT";



    private User currentUser;

    public SettingsHolder(Context cntx) {
        this.context = cntx;
        sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);

    }

    public void setShowOnlyMyMarkers(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowOnlyMyMarkers,b);
        ed.commit();

    }
    public void setShowOnlyAccidents(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowOnlyAccidents,b);
        ed.commit();

    }
    public void setShowOnlyCorrupts(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowOnlyCorrupts,b);
        ed.commit();

    }
    public void setShowOnlyLookFriends(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowOnlyLookFriends,b);
        ed.commit();

    }

    public void setRefreshTimeOut(int timeout){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(getRefreshTimeOutSettingName,Integer.toString(timeout));
        ed.commit();
    }

    public String  getRefreshTimeout(){
        String ii;
        try {
            ii = sharedPreferences.getString(getRefreshTimeOutSettingName,"");
            return ii;
        }catch (Exception e){
            this.setRefreshTimeOut(0);
        }

     return "20";

    }
    public boolean getShowOnlyMyMarkers(){
        return sharedPreferences.getBoolean(getShowOnlyMyMarkers, false);
    }
    public boolean getShowOnlyAccidents(){
        return sharedPreferences.getBoolean(getShowOnlyAccidents, false);
    }
    public boolean getShowOnlyCorrupts(){
        return sharedPreferences.getBoolean(getShowOnlyCorrupts, false);
    }
    public boolean getShowOnlyLookFriends(){
        return sharedPreferences.getBoolean(getShowOnlyLookFriends, false);
    }

    public boolean getGetShowCompassButtonSetting() {
        return sharedPreferences.getBoolean(getShowCompassButtonSettingName, false);
    }

    public boolean getGetShowMyLocationButtonSetting() {
        return sharedPreferences.getBoolean(getShowMyLocationButtonSettingName, false);
    }

    public boolean getGetShowZoomButtonSetting() {
        return sharedPreferences.getBoolean(getShowZoomButtonSettingName, false);
    }

    public boolean getGetAllowAutoRefreshSetting() {
         return sharedPreferences.getBoolean(getAllowAutoRefreshSettingName, false);
    }

    public void setAllowAutoRefresh(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getAllowAutoRefreshSettingName,b);
        ed.commit();
    }

    public void setShowZoomButton(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowZoomButtonSettingName,b);
        ed.apply();
    }

    public void setShowMyLocationButton(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowMyLocationButtonSettingName,b);
        ed.commit();
    }

    public void setShowCompassButton(boolean b){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(getShowCompassButtonSettingName,b);
        ed.commit();
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
