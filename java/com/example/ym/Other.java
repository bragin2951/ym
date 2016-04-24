package com.example.ym;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//остальные классы
public class Other {

    //проверка соединения
    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    public static String getCountAlbums(int alb) {
        if ((alb % 10 > 4) || (alb % 100 == 11) || (alb % 100 == 12) || (alb % 100 == 13) || (alb % 100 == 14))
            return "альбомов";
        else if (alb % 10 == 1)
            return "альбом";
        else
            return "альбома";
    }

    public static String getCountSongs(int song) {
        if ((song % 10 > 4) || (song % 100 == 11) || (song % 100 == 12) || (song % 100 == 13) || (song % 100 == 14))
            return "песен";
        else if (song % 10 == 1)
            return "песня";
        else
            return "песни";
    }
}
