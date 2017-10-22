package com.productividadcc.utilerias;

/**
 * Created by crediclub-icortes on 1/08/17.
 */

import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

public class Utilerias {

    public static boolean isWifiOnline(Context mContext){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getActiveNetworkInfo();

        if (mWifi != null){
            if(mWifi.getType() == ConnectivityManager.TYPE_WIFI && mWifi.isConnected())
                return true;
        }

        return false;
    }
}
