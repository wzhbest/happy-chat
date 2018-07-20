package com.barran.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.barran.lib.utils.log.Logs;

/**
 * network
 *
 * Created by tanwei on 2017/10/21.
 */

public class NetworkUtil {
    
    public enum ConnectionType {
        UNKNOWN, INVALID, ETHERNET, WIFI, G_UNKNOWN, G2, G3, G4
    }
    
    public static boolean isNetworkAvailable(Context context) {
        return getConnectedNetworkInfo(context) != null;
    }
    
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = getConnectedNetworkInfo(context);
        if (networkInfo != null) {
            return networkInfo.getType();
        }
        
        return -1;
    }
    
    public static NetworkInfo getConnectedNetworkInfo(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Logs.w("network", "failed to get connectivity manager");
            }
            else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return anInfo;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            Logs.w("network", e.toString());
        }
        return null;
    }
    
    public static boolean isMobileConnected(Context context) {
        return (ConnectivityManager.TYPE_MOBILE == getNetworkType(context));
    }
    
    public static boolean isWifiConnected(Context context) {
        return (ConnectivityManager.TYPE_WIFI == getNetworkType(context));
    }
    
    public static String getConnectionTypeString(Context context) {
        ConnectionType type = getConnectionType(context);
        switch (type) {
            case UNKNOWN:
            default:
                return "unknown";
            case ETHERNET:
                return "ethernet";
            case WIFI:
                return "wifi";
            case G_UNKNOWN:
                return "g_unknown";
            case G2:
                return "2g";
            case G3:
                return "3g";
            case G4:
                return "4g";
        }
    }
    
    public static ConnectionType getConnectionType(Context context) {
        
        if (!isNetworkAvailable(context))
            return ConnectionType.INVALID;
        
        if (isWifiConnected(context)) {
            return ConnectionType.WIFI;
        }
        else {
            NetworkInfo networkInfo = getConnectedNetworkInfo(context);
            if (networkInfo == null) {
                return ConnectionType.UNKNOWN;
            }
            int nType = networkInfo.getType();
            
            switch (nType) {
                case ConnectivityManager.TYPE_WIFI:
                    return ConnectionType.WIFI;
                case ConnectivityManager.TYPE_ETHERNET:
                    return ConnectionType.ETHERNET;
                case ConnectivityManager.TYPE_MOBILE:
                    int subType = networkInfo.getSubtype();
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return ConnectionType.G2;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return ConnectionType.G3;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return ConnectionType.G4;
                        default:
                            // 三种3G制式
                            String subTypeName = networkInfo.getSubtypeName();
                            if (subTypeName.equalsIgnoreCase("TD-SCDMA")
                                    || subTypeName.equalsIgnoreCase("WCDMA")
                                    || subTypeName.equalsIgnoreCase("CDMA2000")) {
                                return ConnectionType.G3;
                            }
                            else {
                                return ConnectionType.UNKNOWN;
                            }
                    }
                default:
                    return ConnectionType.UNKNOWN;
            }
        }
    }
}
