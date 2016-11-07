package ru.motohelper.motohelper;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

public class MyMarker {

    private Marker marker;
    private MarkerOptions markerOptions;
    private LatLng position;

    private String shortDescription;
    private String description;
    private String phone;
    private String userLogin;

    private boolean visibility;
    /**
     * 1-ДТП
     * 2-Поломка
     * 3-Ищу попутчиков
     */
    private int type;

    public MyMarker() {

    }

    public MyMarker(LatLng position, String shortDescription, String description, String phone, int type, boolean visibility, String userLogin) {
        this.position = position;
        this.shortDescription = shortDescription;
        this.description = description;
        this.phone = phone;
        this.type = type;
        this.visibility = visibility;
        this.userLogin = userLogin;
        setMarkerImage();
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setType(int type) {
        this.type = type;
        setMarkerImage();
    }

    public void setVisibility(boolean isVisible) {
        this.visibility = isVisible;
    }

    public void setPosition(LatLng latLng) {
        this.position = latLng;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getUserLogin(){
        return this.userLogin;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public LatLng getPosition() {
        return this.position;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public int getType() {
        return type;
    }

    public void addMarker(GoogleMap map) {
        markerOptions.visible(visibility);
        marker = map.addMarker(markerOptions);
    }

    void setMarkerImage() {
        switch (type) {
            //ДТП
            case 1:
                markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.accident)).position(position);
                break;
            case 2:
                markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mrkr3)).position(position);
                break;
            case 3:
                markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.lookfriends)).position(position);
                break;
        }
    }
}
