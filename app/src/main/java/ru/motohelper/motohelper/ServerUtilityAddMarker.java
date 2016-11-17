package ru.motohelper.motohelper;

import android.content.Context;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by Michael on 06.11.2016.
 */

public class ServerUtilityAddMarker extends AsyncTask<Void, Void, String> {

    ProgressDialog processing;
    String newId;
    Context ctx;
    MyMarker m;
    SettingsHolder settingsHolder;
    User currentUser;


    public ServerUtilityAddMarker(Context c, MyMarker m, User currentUser) {
        this.ctx = c;
        this.m = m;
        this.currentUser = currentUser;
        settingsHolder = new SettingsHolder(this.ctx);
    }


    @Override
    protected void onPreExecute() {
        processing = new ProgressDialog(ctx);
        processing.setMessage(ctx.getResources().getString(R.string.processing));
        processing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processing.setIndeterminate(true);
        processing.setCancelable(false);
        processing.show();

    }

    @Override
    protected String doInBackground(Void... voids) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<NameValuePair>();

        HttpParams params1 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params1, 1000 * 15
        );
        HttpConnectionParams.setSoTimeout(params1, 1000 * 15);

        HttpClient client = new DefaultHttpClient(params1);
        HttpPost post = new HttpPost(settingsHolder.getIpAddress()
                + "insert_marker.php");

        HttpPost markerIdGet = new HttpPost(settingsHolder.getIpAddress()
                + "get_marker_id.php");

        try {
            HttpResponse response = client.execute(markerIdGet);
            HttpEntity entity = response.getEntity();
            newId = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataToSend.add(new BasicNameValuePair("id", newId));
        dataToSend.add(new BasicNameValuePair("lat", Double
                .toString(m.getPosition().latitude)));
        dataToSend.add(new BasicNameValuePair("lng", Double
                .toString(m.getPosition().longitude)));
        dataToSend.add(new BasicNameValuePair("type", Integer
                .toString(m.getType())));
        dataToSend.add(new BasicNameValuePair("title", m.getShortDescription()));
        dataToSend.add(new BasicNameValuePair("descr", m.getDescription()));
        dataToSend.add(new BasicNameValuePair("userLogin", m.getUserLogin()));

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend,HTTP.UTF_8));
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newId;
    }

    @Override
    protected void onPostExecute(String result) {
        processing.dismiss();
        super.onPostExecute(result);
    }
}
