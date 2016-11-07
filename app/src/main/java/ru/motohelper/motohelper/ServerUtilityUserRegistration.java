package ru.motohelper.motohelper;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.*;
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

public class ServerUtilityUserRegistration extends AsyncTask<Void, Void, Integer> {
    Context ctx;
    User user;
    SettingsHolder appSettings;
    final String REGISTER_USER = "registerUser";
    final String VALIDATE_USER = "validateUser";
    ProgressDialog processing;

    public ServerUtilityUserRegistration(Context context) {
        ctx = context;
        appSettings = new SettingsHolder(ctx);
    }

    public ServerUtilityUserRegistration(Context context, String operation, User u) {
        ctx = context;
        user = u;
        appSettings = new SettingsHolder(ctx);
    }


    @Override
    protected void onPreExecute() {
        processing = new ProgressDialog(ctx);
        processing.setMessage("Регистрация пользователя в системе");
        processing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processing.setIndeterminate(true);
        processing.setCancelable(false);
        processing.show();
    }


    @Override
    protected Integer doInBackground(Void... voids) {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * 15);
        HttpConnectionParams.setSoTimeout(httpParams, 1000 * 15);
        HttpClient client = new DefaultHttpClient(httpParams);
        int rsp = 0;


        ArrayList<NameValuePair> dataToSend = new ArrayList<NameValuePair>();
        dataToSend.add(new BasicNameValuePair("name", user.getFirstName()));
        dataToSend.add(new BasicNameValuePair("fam", user.getSecondName()));
        dataToSend.add(new BasicNameValuePair("email", user.getLogin()));
        dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
        dataToSend.add(new BasicNameValuePair("phone", user.getPhone()));

        HttpPost post = new HttpPost(appSettings.getIpAddress() + "reg_user.php");

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend,HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            rsp = Integer.parseInt(responseString);

        } catch (Exception e) {
            e.printStackTrace();
            rsp = -1;
        }

        return rsp;
    }


    @Override
    protected void onPostExecute(Integer result) {
        processing.dismiss();

        switch (result) {
            case 1:
                Toast.makeText(ctx,
                        "Пользователь с таким адресом уже есть!",
                        Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(ctx, "Вы зарегистрированы!",
                        Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(
                        ctx,
                        "Внутренняя ошибка сервера. Пожалуйста попробуйте позже!",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }


}
