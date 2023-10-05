package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PurchyReportModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class PurchyAllDeatilsShowAdapter extends RecyclerView.Adapter<PurchyAllDeatilsShowAdapter.MyHolder> {

    private Context context;
    List <PurchyReportModal> arrayList;
    double Lat, Lng;
    String address = "";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;


    public PurchyAllDeatilsShowAdapter(Context context, List <PurchyReportModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.purchyshowalldetails_desing,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            findLocation();
            holder.sno.setText(String.valueOf(position+1));
            //holder.Purchyno.setText(arrayList.get(position).getPLOTVILLAGECODE());
            holder.plot_vill.setText(arrayList.get(position).getPLOTVILLAGENAME());
            holder.plot_no.setText(arrayList.get(position).getPLOTVILLAGECODE());
            holder.variety_group.setText(arrayList.get(position).getVARIETYCODE());
            holder.variety_name.setText(arrayList.get(position).getPLOTVILLAGENAME());
            holder.crop_type.setText(arrayList.get(position).getCANETYPE());
            holder.area.setText(arrayList.get(position).getAREA());
            holder.supcode.setText(arrayList.get(position).getSUPCODE());
            holder.sup_name.setText(arrayList.get(position).getSUPNAME());

            holder.GH_NE_LAT.setText("GH_NE_LAT  : "+arrayList.get(position).getGH_NE_LAT());
            holder.GH_NE_LNG.setText("GH_NE_LNG  : "+arrayList.get(position).getGH_NE_LNG());

            holder.GH_NW_LAT.setText("GH_NW_LAT : "+arrayList.get(position).getGH_NW_LAT());
            holder.GH_NW_LNG.setText("GH_NW_LNG : "+arrayList.get(position).getGH_NW_LNG());

            holder.GH_SE_LAT.setText("GH_SE_LAT  : "+arrayList.get(position).getGH_SE_LAT());
            holder.GH_SE_LNG.setText("GH_SE_LNG  : "+arrayList.get(position).getGH_SW_LNG());

            holder.GH_SW_LAT.setText("GH_SW_LAT : "+arrayList.get(position).getGH_SW_LAT());
            holder.GH_SW_LNG.setText("GH_SW_LNG : "+arrayList.get(position).getGH_SW_LNG());





        } catch (Exception e) {
            e.printStackTrace();
        }







        //-----------------click the list--------------------------------------------------------

        holder.open_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat=arrayList.get(position).getGH_NW_LAT();
                String lng=arrayList.get(position).getGH_NE_LNG();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+Lat+","+Lng+"&daddr="+lat+","+lng));
                context.startActivity(intent);

            }
        });


    }





    //-----------------fined the current location---------------------------------------
        protected void findLocation() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            SettingsClient settingsClient = LocationServices.getSettingsClient(context);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //checkUpdate();
                }
            });
            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            new AlertDialogManager().AlertPopUpFinish(context, "Security Error: please enable gps service");
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity) context,
                                    1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location = locationResult.getLastLocation();
                            if (location.isFromMockProvider()) {
                                new AlertDialogManager().showToast((Activity) context, "Security Error : you can not use this application because we detected fake GPS ?");
                            } else {
                                try {
                                    List<Address> addresses = new ArrayList<>();
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    Geocoder geocoder;
                                    geocoder = new Geocoder(context, Locale.getDefault());
                                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                                    address = addresses.get(0).getAddressLine(0);

                                } catch (SecurityException se) {
                                    address = "Error : Security error";
                                    new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
                                } catch (Exception se) {
                                    new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
        }

    }
    //----------------------------------------------------------------------------------



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TextView plot_vill,plot_no,variety_group,sno;
        TextView variety_name,crop_type,area,supcode,sup_name;
        TextView GH_NE_LAT,GH_NE_LNG,GH_NW_LAT,GH_NW_LNG;
        TextView GH_SE_LAT,GH_SE_LNG,GH_SW_LAT,GH_SW_LNG,Purchyno;
        ImageView open_location;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            sno=itemView.findViewById(R.id.sno);
            Purchyno=itemView.findViewById(R.id.Purchyno);
            plot_vill=itemView.findViewById(R.id.plot_vill);
            plot_no=itemView.findViewById(R.id.plot_no);
            variety_group=itemView.findViewById(R.id.variety_group);
            variety_name=itemView.findViewById(R.id.variety_name);
            crop_type=itemView.findViewById(R.id.crop_type);
            area=itemView.findViewById(R.id.area);
            supcode=itemView.findViewById(R.id.supcode);
            sup_name=itemView.findViewById(R.id.sup_name);
            open_location=itemView.findViewById(R.id.open_location);

            GH_NE_LAT=itemView.findViewById(R.id.GH_NE_LAT);
            GH_NE_LNG=itemView.findViewById(R.id.GH_NE_LNG);

            GH_NW_LAT=itemView.findViewById(R.id.GH_NW_LAT);
            GH_NW_LNG=itemView.findViewById(R.id.GH_NW_LNG);


            GH_SE_LAT=itemView.findViewById(R.id.GH_SE_LAT);
            GH_SE_LNG=itemView.findViewById(R.id.GH_SE_LNG);

            GH_SW_LAT=itemView.findViewById(R.id.GH_SW_LAT);
            GH_SW_LNG=itemView.findViewById(R.id.GH_SW_LNG);

        }


    }

}
