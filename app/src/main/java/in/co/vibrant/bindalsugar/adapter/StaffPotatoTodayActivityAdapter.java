package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PotatoTodayActivityModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.StaffActivityForm;


public class StaffPotatoTodayActivityAdapter extends RecyclerView.Adapter<StaffPotatoTodayActivityAdapter.MyHolder> {

    private Context context;
    List<PotatoTodayActivityModal> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public StaffPotatoTodayActivityAdapter(Context context, List<PotatoTodayActivityModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_staff_potato_today_activity,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.ivMapGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMap();
            }
        });
        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, StaffActivityForm.class);
                context.startActivity(intent);
            }
        });
        //
    }

    public void ShowMap()
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_potatotracker_on_map, null);
        dialogbilder.setView(mView);
        Alertdialog = dialogbilder.create();
        GoogleMap googleMap;
        //holder.ll.removeAllViewsInLayout();
        MapView mMapView = (MapView) mView.findViewById(R.id.mapView);
        MapsInitializer.initialize(context);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(Alertdialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                LatLng latlng = new LatLng(26.897269, 80.991764);
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(26.897269, 80.991764), 16.0f));
                Marker melbourne = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latlng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        });

        Button closeMap=mView.findViewById(R.id.closeMap);
        closeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alertdialog.dismiss();
            }
        });

        Button navigateMap=mView.findViewById(R.id.navigation);
        navigateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    navigateforclient(26.897269,80.991764);
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
            }
        });

        Alertdialog.show();
        Alertdialog.setCancelable(false);
        Alertdialog.setCanceledOnTouchOutside(true);

    }

    public void navigateforclient(final double lat, final double lng)
    {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + location.getLatitude() + "," + location.getLongitude() + "&destination=" + lat + "," + lng + "&travelmode=driving&dir_action=navigate"));
                                context.startActivity(intent);
                            }
                        }
                    });
        }
        catch(SecurityException e)
        {

        }
        catch(Exception e)
        {

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ivMapGps,ivAction;
        TextView registration_no,r_date,r_name,r_village;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivMapGps=itemView.findViewById(R.id.ivMapGps);
            ivAction=itemView.findViewById(R.id.ivAction);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
