package ru.motohelper.motohelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ru.motohelper.motohelper.MyMarker;
import ru.motohelper.motohelper.R;
import ru.motohelper.motohelper.User;

/**
 * Created by Michael on 16.11.2016.
 */

public class NotifManager {
    private Set<MyMarker> currentState;
    private Set<MyMarker> newState;
    private Location currentLocation;
    private int radius;
    private User currentUser;
    private Context context;

    public NotifManager(Context c) {
        context = c;
    }

    public void setRadius(int r) {
        radius = r;
    }

    public void setCurrentLocation(Location c) {
        currentLocation = c;
    }

    public void setCurrentUser(User u){
        currentUser = u;
    }

    public void notify(ArrayList<MyMarker> currentMarkers, ArrayList<MyMarker> serverMarkers){
        ArrayList<MyMarker> mArray = getMarkersToNotify(currentMarkers,serverMarkers);

        for (int i = 0; i < mArray.size(); i++) {
            Location l = new Location("location");
            l.setAltitude(mArray.get(i).getPosition().latitude);
            l.setLongitude(mArray.get(i).getPosition().longitude);
            try {
                double dist = calculateDestination(
                        mArray.get(i).getPosition().latitude,
                        mArray.get(i).getPosition().longitude,
                        currentLocation.getLatitude(), currentLocation.getLongitude());
                if (dist <= radius * 1000 && !mArray.get(i).getUserLogin().equals(currentUser.getLogin())) {
                    sendNotification("Пользователь "
                            + mArray.get(i).getUserName()
                            + " добавил маркер недалеко от вас.", mArray
                            .get(i).getBitmap());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


    }

    private void sendNotification(String notificationText, int icon) {
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res,icon))
                // .setTicker(res.getString(R.string.warning)) // текст в строке
                // состояния
                .setTicker(res.getString(R.string.MarkerWasAddednearby))
                .setWhen(System.currentTimeMillis()).setAutoCancel(true)
                // Заголовок уведомления
                .setContentTitle(res.getString(R.string.MarkerWasAdded))
                // .setContentText(res.getString(R.string.notifytext))
                .setContentText(notificationText); // Текст уведомления

        @SuppressWarnings("deprecation")
        Notification notification = builder.getNotification(); // до API 16
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }


    private ArrayList<MyMarker> getMarkersToNotify(ArrayList<MyMarker> currentMarkers, ArrayList<MyMarker> serverMarkers) {
        this.currentState = new HashSet<MyMarker>(currentMarkers);
        this.newState = new HashSet<MyMarker>(serverMarkers);

        Iterator it = currentState.iterator();

        while (it.hasNext()) {
            newState.remove(it.next());
        }

        return new ArrayList<MyMarker>(newState);
    }


    // calculate distance in meters
    double calculateDestination(double A1, double B1, double A2, double B2) {

        double pi = Math.PI;
        double rad = 6372795;

        // get rads
        double lat1 = A1 * pi / 180;
        double lat2 = A2 * pi / 180;

        double lon1 = B1 * pi / 180;
        double lon2 = B2 * pi / 180;

        // cos sin
        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lon1);
        double sl2 = Math.sin(lon2);

        // delta
        double delta = lon2 - lon1;

        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);
        double p1 = Math.pow(cl2 * sdelta, 2);
        double p2 = Math.pow((cl1 * sl2) - (sl1 * cl2 * cdelta), 2);
        double p3 = Math.pow(p1 + p2, 0.5);
        double p4 = sl1 * sl2;
        double p5 = cl1 * cl2 * cdelta;
        double p6 = p4 + p5;

        double p7 = p3 / p6;
        double anglerad = Math.atan(p7);
        double dist = anglerad * rad;

        return dist;
    }

}
