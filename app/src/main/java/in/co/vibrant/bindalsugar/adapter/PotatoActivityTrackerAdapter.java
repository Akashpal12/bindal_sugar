package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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


public class PotatoActivityTrackerAdapter extends RecyclerView.Adapter<PotatoActivityTrackerAdapter.MyHolder> {

    private Context context;
    List<PotatoTodayActivityModal> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public PotatoActivityTrackerAdapter(Context context, List<PotatoTodayActivityModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_potato_tracker,null);
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

        TableLayout dialogueTable = new TableLayout(context);
        dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogueTable.setShrinkAllColumns(false);
        dialogueTable.setStretchAllColumns(false);
        TableRow tableHeading = new TableRow(context);

        tableHeading.setBackgroundColor(Color.parseColor("#000000"));

        TextView th1 = new TextView(context);
        th1.setTextColor(Color.parseColor("#FFFFFF"));
        th1.setText("Activity Name");
        th1.setGravity(Gravity.LEFT);
        th1.setWidth(280);
        tableHeading.addView(th1);

        TextView th2 = new TextView(context);
        th2.setTextColor(Color.parseColor("#FFFFFF"));
        th2.setText("Due Date");
        th2.setGravity(Gravity.LEFT);
        th2.setWidth(240);
        tableHeading.addView(th2);

        TextView th3 = new TextView(context);
        th3.setTextColor(Color.parseColor("#FFFFFF"));
        th3.setText("Status");
        th3.setGravity(Gravity.LEFT);
        th3.setWidth(180);
        tableHeading.addView(th3);

        TextView th4 = new TextView(context);
        th4.setTextColor(Color.parseColor("#FFFFFF"));
        th4.setText("Done By");
        th4.setGravity(Gravity.LEFT);
        th4.setWidth(180);
        tableHeading.addView(th4);

        TextView th5 = new TextView(context);
        th5.setTextColor(Color.parseColor("#FFFFFF"));
        th5.setText("Done Date");
        th5.setGravity(Gravity.LEFT);
        th5.setWidth(240);
        tableHeading.addView(th5);

        TextView th6 = new TextView(context);
        th6.setTextColor(Color.parseColor("#FFFFFF"));
        th6.setText("Late / Early");
        th6.setGravity(Gravity.LEFT);
        th6.setWidth(280);
        tableHeading.addView(th6);

        TextView th7 = new TextView(context);
        th7.setTextColor(Color.parseColor("#FFFFFF"));
        th7.setText("Supervisor Visited");
        th7.setGravity(Gravity.LEFT);
        th7.setWidth(280);
        tableHeading.addView(th7);

        TextView th8 = new TextView(context);
        th8.setTextColor(Color.parseColor("#FFFFFF"));
        th8.setText("Image");
        th8.setGravity(Gravity.LEFT);
        th8.setWidth(180);
        tableHeading.addView(th8);

        dialogueTable.addView(tableHeading);

        for (int j = 0; j < 10; j++) {
            TableRow row = new TableRow(context);
            row.setId(position+j);

            TextView tv1 = new TextView(context);
            tv1.setText("Irrigation");
            tv1.setWidth(120);
            tv1.setGravity(Gravity.LEFT);


            TextView tv2 = new TextView(context);
            tv2.setText("06/09/2020");
            tv2.setWidth(120);
            tv2.setGravity(Gravity.LEFT);

            TextView tv3 = new TextView(context);
            tv3.setText("Done");
            tv3.setWidth(120);
            tv3.setGravity(Gravity.LEFT);

            TextView tv4 = new TextView(context);
            tv4.setText("Self");
            tv4.setWidth(120);
            tv4.setGravity(Gravity.LEFT);

            TextView tv5 = new TextView(context);
            tv5.setText("03/08/2020");
            tv5.setWidth(120);
            tv5.setGravity(Gravity.LEFT);

            TextView tv6 = new TextView(context);
            tv6.setText("1 Day Late");
            tv6.setWidth(120);
            tv6.setGravity(Gravity.LEFT);

            TextView tv7 = new TextView(context);
            tv7.setText("Yes");
            tv7.setWidth(120);
            tv7.setGravity(Gravity.LEFT);

            ImageView tv8 = new ImageView(context);
            tv8.setImageResource(R.drawable.ic_baseline_ac_unit_24);

            if(j%2==0)
            {
                row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                tv1.setTextColor(Color.parseColor("#000000"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv5.setTextColor(Color.parseColor("#000000"));
                tv6.setTextColor(Color.parseColor("#000000"));
                tv7.setTextColor(Color.parseColor("#000000"));
            }
            else
            {
                row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv1.setTextColor(Color.parseColor("#000000"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv5.setTextColor(Color.parseColor("#000000"));
                tv6.setTextColor(Color.parseColor("#000000"));
                tv7.setTextColor(Color.parseColor("#000000"));
            }
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            row.addView(tv4);
            row.addView(tv5);
            row.addView(tv6);
            row.addView(tv7);

            dialogueTable.addView(row);
        }

        holder.layout_booking.addView(dialogueTable);
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
        ImageView ivMapGps;
        LinearLayout layout_booking;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            layout_booking=itemView.findViewById(R.id.layout_booking);
            ivMapGps=itemView.findViewById(R.id.ivMapGps);

        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
