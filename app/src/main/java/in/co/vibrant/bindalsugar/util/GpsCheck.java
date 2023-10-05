package in.co.vibrant.bindalsugar.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import in.co.vibrant.bindalsugar.R;


public class GpsCheck {

    Context context;

    public GpsCheck(Context context) {
        this.context = context;
    }

    public boolean isGpsEnable() {
        //LocationManager locationManager1 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //boolean gpsenable = locationManager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //return gpsenable;\
        boolean gpsenable=false;
        try {
            LocationManager lm = (LocationManager)
                    context.getSystemService(Context. LOCATION_SERVICE ) ;
            boolean gps_enabled = false;
            boolean network_enabled = false;
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(context)
                        .setMessage( context.getString(R.string.ERR_GPS_ENABLE) )
                        .setPositiveButton( context.getString(R.string.BTN_ENABLE_GPS) , new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                        context.startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                    }
                                })
                        .setNegativeButton( context.getString(R.string.BTN_LATER), new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                        ((Activity) context).finish();
                                    }
                                })
                        .show() ;
            }
            else
            {
                return true;
            }
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        try {

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return gpsenable;
    }



}
