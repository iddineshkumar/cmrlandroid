package com.cm.cmrl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by Iddinesh.
 */
public class ConnectionDetector {
	
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	public static String getConnectivityStatusString(Context applicationContext)
	{
			int conn = ConnectionDetector.getConnectivityStatus(applicationContext);
			String status = null;
			if (conn == ConnectionDetector.TYPE_WIFI) {
				status = "Wifi enabled";
			} else if (conn == ConnectionDetector.TYPE_MOBILE) {
				status = "Mobile data enabled";
			} else if (conn == ConnectionDetector.TYPE_NOT_CONNECTED) {
				status = "Not connected to Internet";
			}
			return status;
	}

	private static int getConnectivityStatus(Context applicationContext) {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) applicationContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;
			
			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
			
		}
		return TYPE_NOT_CONNECTED;
	}
	
		
}
