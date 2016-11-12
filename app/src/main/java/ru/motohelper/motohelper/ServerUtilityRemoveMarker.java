package ru.motohelper.motohelper;

/**
 * Created by Michael on 11.11.2016.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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

import java.util.ArrayList;


public class ServerUtilityRemoveMarker extends AsyncTask<Void, Void, Void> {

    private Context ctx;
    private MyMarker m;
    private ProgressDialog processing;
    private String serverAddress;

    public ServerUtilityRemoveMarker(Context c, MyMarker m, String serverAddress) {
        this.ctx = c;
        this.m = m;
        this.serverAddress = serverAddress;
    }

    @Override
    protected void onPreExecute() {
        processing = new ProgressDialog(ctx);
        processing.setMessage(ctx.getString(R.string.processing));
        processing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processing.setIndeterminate(true);
        processing.setCancelable(false);
        processing.show();
        processing.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<NameValuePair>();

        HttpParams params1 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params1,
                15 * 10000);
        HttpConnectionParams.setSoTimeout(params1, 15 * 10000);

        HttpClient client = new DefaultHttpClient(params1);
        dataToSend.add(new BasicNameValuePair("id",m.getServerID()));
        HttpPost post = new HttpPost(serverAddress
                + "delete_marker_by_id.php");

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend,
                    HTTP.UTF_8));
            client.execute(post);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        processing.dismiss();
        Toast.makeText(ctx, "Маркер удаляется из базы данных",
                Toast.LENGTH_SHORT).show();
        super.onPostExecute(result);
    }
}
