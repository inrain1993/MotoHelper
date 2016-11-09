package ru.motohelper.motohelper;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.motohelper.motohelper.Fragments.FragmentSettings;


public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap mMap;

    Map<String, MyMarker> markersCollection = new HashMap<String, MyMarker>();
    EditText markerShortDescription;
    EditText markerLongDescription;
    EditText markerUserPhone;

    FragmentSettings fragmentSettings;

    TextView navUserLogin;
    TextView navUserFamNam;

    EditText shortDesc;
    EditText longDesc;
    EditText phone;

    RadioButton rbuttonCorrupt;
    RadioButton rbuttonLookF;
    RadioButton rbuttonAccident;

    Button btnDial;
    Button btnSMS;
    Button btnDeleteMarker;

    RadioButton markerTypeCorrupt;
    RadioButton markerTypeLookFriends;
    RadioButton markerTypeAccident;

    Button buttonCreateMarker;
    Button buttonCancelCreateMarker;

    Dialog modalAddMarker;
    Dialog modalOnView;

    LatLng positionToAddMarker;

    SettingsHolder appSettings;

    MyMarker selectedMarker;

    User currentUser;

    ServerUtilityGetMarkers getMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initializeMap();
        appSettings = new SettingsHolder(this);
        currentUser = appSettings.getCurrentUser();
        prepareToolbar();
        fragmentSettings = new FragmentSettings();



    }

    private void initializeMap() {
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }




    @Override
    // Set up Map behavour
    public void onMapReady(GoogleMap map) {
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                onCreateModalToAddMarker();
                positionToAddMarker = latLng;
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onCreateModalToViewMarker(marker);
                return false;
            }
        });


        // эта строка всегда последняя!
        mMap = map;


        getMarkers = new ServerUtilityGetMarkers(this);
        getMarkers.setDoDialog(true);
        ArrayList<MyMarker> markers;
        markers = new ArrayList<>();
        try {
            markers = getMarkers.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i=0;i<markers.size();i++){
            markers.get(i).addMarker(mMap);
            markersCollection.put(markers.get(i).getMarker().getId(),markers.get(i));
        }

    }

    public void onCreateModalToAddMarker() {

        modalAddMarker = new Dialog(MapActivity.this);
        modalAddMarker.setTitle(R.string.addMarker);
        modalAddMarker.setContentView(R.layout.modal_add_marker_r);

        markerShortDescription = (EditText) modalAddMarker.findViewById(R.id.shortDesc);
        markerLongDescription = (EditText) modalAddMarker.findViewById(R.id.longDesc);
        markerUserPhone = (EditText) modalAddMarker.findViewById(R.id.phone);

        buttonCreateMarker = (Button) modalAddMarker.findViewById(R.id.buttonAddMarker);
        buttonCancelCreateMarker = (Button) modalAddMarker.findViewById(R.id.buttonAddMarkerCancel);

        markerTypeCorrupt = (RadioButton) modalAddMarker.findViewById(R.id.corr);
        markerTypeLookFriends = (RadioButton) modalAddMarker.findViewById(R.id.lookf);
        markerTypeAccident = (RadioButton) modalAddMarker.findViewById(R.id.accident);

        markerTypeCorrupt.setOnClickListener(MapActivity.this);
        markerTypeLookFriends.setOnClickListener(MapActivity.this);
        markerTypeAccident.setOnClickListener(MapActivity.this);

        buttonCreateMarker.setOnClickListener(MapActivity.this);
        buttonCancelCreateMarker.setOnClickListener(MapActivity.this);


        modalAddMarker.show();
    }

    // создает модальное окно и наполняет его данными
    public void onCreateModalToViewMarker(Marker marker)
    {
        modalOnView = new Dialog(MapActivity.this);
        modalOnView.setTitle(markersCollection.get(marker.getId()).getUserLogin());
        modalOnView.setContentView(R.layout.modal_view_marker_r);

        shortDesc = (EditText) modalOnView.findViewById(R.id.shortDescView);
        longDesc = (EditText) modalOnView.findViewById(R.id.longDescView);
        phone = (EditText) modalOnView.findViewById(R.id.phoneView);

        rbuttonCorrupt = (RadioButton) modalOnView.findViewById(R.id.corrView);
        rbuttonLookF = (RadioButton) modalOnView.findViewById(R.id.lookfView);
        rbuttonAccident = (RadioButton) modalOnView.findViewById(R.id.accidentView);

        btnDial = (Button) modalOnView.findViewById(R.id.buttonDial);
        btnSMS = (Button) modalOnView.findViewById(R.id.buttonSMS);
        btnDeleteMarker = (Button) modalOnView.findViewById(R.id.buttonDeleteMarker);

        String markerUserLogin = markersCollection.get(marker.getId()).getUserLogin();
        String currentUserLogin = currentUser.getLogin();

        selectedMarker = markersCollection.get(marker.getId());
        if (markerUserLogin.equals(currentUserLogin)) {
            btnDeleteMarker.setVisibility(View.VISIBLE);
        } else {
            btnDeleteMarker.setVisibility(View.INVISIBLE);
        }

        btnDial.setOnClickListener(this);
        btnSMS.setOnClickListener(this);
        btnDeleteMarker.setOnClickListener(this);

        shortDesc.setEnabled(false);
        longDesc.setEnabled(false);
        phone.setEnabled(false);
        rbuttonLookF.setEnabled(false);
        rbuttonCorrupt.setEnabled(false);
        rbuttonAccident.setEnabled(false);

        // наполнение данными
        try {

            shortDesc.setText(markersCollection.get(marker.getId()).getShortDescription());
            longDesc.setText(markersCollection.get(marker.getId()).getDescription());
            phone.setText(markersCollection.get(marker.getId()).getPhone());
            switch (markersCollection.get(marker.getId()).getType()) {
                case 1:
                    rbuttonAccident.setChecked(true);
                    break;
                case 2:
                    rbuttonCorrupt.setChecked(true);
                    break;
                case 3:
                    rbuttonLookF.setChecked(true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        modalOnView.show();
    }




    /**
     * Implementations of Navigation Drawer
     */

    void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.accept, R.string.btnCancel);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.getHeaderView(0);


        navUserFamNam = (TextView) navHeaderView.findViewById(R.id.navUserFamNam);
        navUserFamNam.setText(currentUser.getFirstName() + " " + currentUser.getSecondName());

        navUserLogin = (TextView) navHeaderView.findViewById(R.id.navUserLogin);
        navUserLogin.setText(currentUser.getLogin());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_map:
                break;
            case R.id.nav_settings:
                fragmentTransaction.replace(R.id.content_main,fragmentSettings);
                break;
            case R.id.nav_filtering:
                break;
            case R.id.nav_vk:
                break;
            case R.id.nav_youtube:
                break;
        }
        fragmentTransaction.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     *
     * Обработчики нажатия на кнопки
     */


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddMarkerCancel:
                modalAddMarker.dismiss();
                break;
            case R.id.buttonAddMarker:
                //тип и инициализация
                int type = 1;
                if (markerTypeAccident.isChecked()) type = 1;
                if (markerTypeCorrupt.isChecked()) type = 2;
                if (markerTypeLookFriends.isChecked()) type = 3;

                MyMarker m = new MyMarker(positionToAddMarker, markerShortDescription.getText().toString(), markerLongDescription.getText().toString(),markerUserPhone.getText().toString(), type, true, currentUser.getLogin());
                m.addMarker(mMap);
                ServerUtilityAddMarker addMarker = new ServerUtilityAddMarker(this, m, currentUser);
                addMarker.execute();
                modalAddMarker.dismiss();
                markersCollection.put(m.getMarker().getId(), m);
                break;
            case R.id.buttonDial:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone.getText().toString()));
                startActivity(dialIntent);
                break;
            case R.id.buttonSMS:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts(
                        "sms", phone.getText().toString(), null));
                startActivity(smsIntent);
                break;

            case R.id.buttonDeleteMarker:
                modalOnView.cancel();
                break;
        }

    }

}
