package ru.motohelper.motohelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerUtilityGetMarkers extends AsyncTask<Void, Void, ArrayList<MyMarker>> {

    private Context c;
    private ArrayList<MyMarker> markersFromServer;

    private boolean doDialog;
    private SettingsHolder appSettings;

    private ProgressDialog processing;

    public ServerUtilityGetMarkers(Context c) {
        this.c = c;
        appSettings = new SettingsHolder(c);
    }

    public void setDoDialog(boolean d) {
        this.doDialog = d;
    }

    @Override
    protected void onPreExecute() {
        if (doDialog) {
            processing = new ProgressDialog(c);
            processing.setMessage("Регистрация пользователя в системе");
            processing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            processing.setIndeterminate(true);
            processing.setCancelable(false);
            processing.show();
        }

    }

    @Override
    protected void onPostExecute(ArrayList<MyMarker> result) {
        if (doDialog) {
            processing.dismiss();
        }

    }

    @Override
    protected ArrayList<MyMarker> doInBackground(Void... voids) {
        HttpParams params1 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params1, 1000 * 15);
        HttpConnectionParams.setSoTimeout(params1, 1000 * 15);

        HttpClient client = new DefaultHttpClient(params1);
        HttpPost post = new HttpPost(appSettings.getIpAddress() + "select_all_markers.php");

        ArrayList<MyMarker> serverMarkers = new ArrayList<MyMarker>();

        try {

            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            String[] messages;
            messages = result.split("@");
            for (int i = 0; i < messages.length; i++) {
                JSONObject jObject = new JSONObject(messages[i]);
                String jId = jObject.getString("id");
                String jlat = jObject.getString("lat");
                String jlon = jObject.getString("lng");
                String jtype = jObject.getString("type");
                String jtitle = jObject.getString("title");
                String jdescr = jObject.getString("descr");
                String julogin = jObject.getString("userLogin");
                String jfam = jObject.getString("fam");
                String juserFname = jObject.getString("name");
                String juserPhone = jObject.getString("phone");
                LatLng position = new LatLng(Double.valueOf(jlat), Double.valueOf(jlon));

                MyMarker m = new MyMarker(position, jtitle, jdescr, juserPhone, Integer.parseInt(jtype), true, julogin);
                m.setUserName(juserFname);
                m.setUserSecondName(jfam);
                serverMarkers.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverMarkers;
    }
}
