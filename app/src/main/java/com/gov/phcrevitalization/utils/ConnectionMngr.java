package com.gov.phcrevitalization.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gov.phcrevitalization.R;


public class ConnectionMngr {

	
	public static boolean hasConnection(final Context _cntx){
		ConnectivityManager cm = (ConnectivityManager) _cntx.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected())
			return true;
		else{
			AlertDialog.Builder dialogNetworkState = new AlertDialog.Builder(
					_cntx);
        	
        	dialogNetworkState.setTitle(_cntx.getResources().getString(R.string.app_name))
        				.setMessage("Please connect your device to the internet and Try Again")
        				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
								((Activity)_cntx).finish();

                            }
                        });
        	
        	AlertDialog dialog = dialogNetworkState.create();
        	dialog.show();
        	return false;
		}
	}

	public static boolean isConnectedToInternet(Context _cntx){
		ConnectivityManager cm = (ConnectivityManager) _cntx.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected())
			return true;
		else{
			return false;
		}
	}

}
