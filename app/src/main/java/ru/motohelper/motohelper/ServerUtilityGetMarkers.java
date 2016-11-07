package ru.motohelper.motohelper;

import android.content.Context;
import android.os.AsyncTask;
import java.util.ArrayList;

public class ServerUtilityGetMarkers extends AsyncTask <Void, Void, ArrayList<MyMarker>> {

    private Context c;
    private ArrayList<MyMarker> markersFromServer;

    private boolean doDialog;

    public ServerUtilityGetMarkers(Context c){
        this.c = c;
    }

    public void setDoDialog(boolean d){
        this.doDialog=d;
    }

    @Override
    protected ArrayList<MyMarker> doInBackground(Void... voids) {
        return null;
    }
}
