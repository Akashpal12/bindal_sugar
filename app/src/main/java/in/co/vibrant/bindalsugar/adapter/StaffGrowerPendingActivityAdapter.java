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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.StaffActivityForm;


public class StaffGrowerPendingActivityAdapter extends RecyclerView.Adapter<StaffGrowerPendingActivityAdapter.MyHolder> {

    private Context context;
    List<GrowerActivityDetailsModel> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public StaffGrowerPendingActivityAdapter(Context context, List<GrowerActivityDetailsModel> arrayList) {
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
        //,,,,,,,,,,,;
        final int pos=position+1;
        holder.tvCount.setText(""+pos);
        holder.tv_name.setText(arrayList.get(position).getGrowerCode()+"/"+arrayList.get(position).getGrowerName());
        holder.grower_father.setText("Father Name  :"+arrayList.get(position).getGrowerFather());
        holder.plot_number.setText("Plot Number : "+arrayList.get(position).getPlotNumber());
        holder.area.setText("Area : "+arrayList.get(position).getArea());
        holder.mix_crop.setText("Mix Crop : "+arrayList.get(position).getMixCrop());
        holder.plot_village.setText("Plot Village : "+arrayList.get(position).getPlotVillage()+"/"+arrayList.get(position).getPlotVillageName());
        holder.variety.setText("Variety : "+arrayList.get(position).getVarietyName());
        holder.east.setText("East : "+arrayList.get(position).getEastDistance());
        holder.west.setText("West : "+arrayList.get(position).getWestDistance());
        holder.north.setText("North : "+arrayList.get(position).getNorthDistance());
        holder.south.setText("South : "+arrayList.get(position).getSouthDistance());
        holder.ivMapGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMap(arrayList.get(position));
            }
        });
        /*holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson=new Gson();
                Intent intent=new Intent(context, StaffActivityForm.class);
                intent.putExtra("staffData",gson.toJson(arrayList.get(position)));
                context.startActivity(intent);
            }
        });*/



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
        JSONArray jsonArray=arrayList.get(position).getGetActivityData();
        for (int j = 0; j < jsonArray.length() ; j++) {
            try {
                JSONObject object = jsonArray.getJSONObject(j);
                TableRow row = new TableRow(context);
                row.setId(position + j);

                TextView tv1 = new TextView(context);
                tv1.setText(object.getString("ACT_NAME"));
                tv1.setWidth(120);
                tv1.setGravity(Gravity.LEFT);

                TextView tv2 = new TextView(context);
                tv2.setText(object.getString("Days"));
                tv2.setWidth(120);
                tv2.setGravity(Gravity.LEFT);

                TextView tv3 = new TextView(context);
                tv3.setText(object.getString("status"));
                tv3.setWidth(120);
                tv3.setGravity(Gravity.LEFT);

                TextView tv4 = new TextView(context);
                tv4.setText(object.getString("Done_By"));
                tv4.setWidth(120);
                tv4.setGravity(Gravity.LEFT);

                TextView tv5 = new TextView(context);
                tv5.setText(object.getString("DoneDate"));
                tv5.setWidth(120);
                tv5.setGravity(Gravity.LEFT);

                TextView tv6 = new TextView(context);
                tv6.setText(object.getString("Late_Early"));
                tv6.setWidth(120);
                tv6.setGravity(Gravity.LEFT);

                TextView tv7 = new TextView(context);
                tv7.setText(object.getString("Supervisor_Visited"));
                tv7.setWidth(120);
                tv7.setGravity(Gravity.LEFT);

                ImageView tv8 = new ImageView(context);
                tv8.setImageResource(R.drawable.ic_baseline_edit_24);
                final String BKUP_SNO=object.getString("BKUP_SNO");
                final String ACT_CD=object.getString("ACT_CD");
                tv8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson=new Gson();
                        Intent intent=new Intent(context, StaffActivityForm.class);
                        intent.putExtra("staffData",gson.toJson(arrayList.get(position)));
                        intent.putExtra("BKUP_SNO",BKUP_SNO);
                        intent.putExtra("ACT_CD",ACT_CD);
                        context.startActivity(intent);
                    }
                });

                if (j % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                    tv1.setTextColor(Color.parseColor("#000000"));
                    tv2.setTextColor(Color.parseColor("#000000"));
                    tv3.setTextColor(Color.parseColor("#000000"));
                    tv4.setTextColor(Color.parseColor("#000000"));
                    tv5.setTextColor(Color.parseColor("#000000"));
                    tv6.setTextColor(Color.parseColor("#000000"));
                    tv7.setTextColor(Color.parseColor("#000000"));
                } else {
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
                row.addView(tv8);

                dialogueTable.addView(row);
            }
            catch (Exception e)
            {

            }
        }

        holder.layout_booking.addView(dialogueTable);
        //
    }

    public void ShowMap(final GrowerActivityDetailsModel growerActivityDetailsModel)
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
                LatLng latlng = new LatLng(growerActivityDetailsModel.getEastLat(), growerActivityDetailsModel.getEastLng());
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(growerActivityDetailsModel.getEastLat(), growerActivityDetailsModel.getEastLng()), 16.0f));
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
                    navigateforclient(growerActivityDetailsModel.getEastLat(), growerActivityDetailsModel.getEastLng());
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
        LinearLayout layout_booking;
        ImageView ivMapGps,ivAction,ivCall;
        TextView tvCount,tv_name,grower_father,plot_number,area,mix_crop,plot_village,variety,east,west,north,south;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            layout_booking=itemView.findViewById(R.id.layout_booking);
            ivMapGps=itemView.findViewById(R.id.ivMapGps);
            ivAction=itemView.findViewById(R.id.ivAction);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
            grower_father=itemView.findViewById(R.id.grower_father);
            plot_number=itemView.findViewById(R.id.plot_number);
            area=itemView.findViewById(R.id.area);
            mix_crop=itemView.findViewById(R.id.mix_crop);
            plot_village=itemView.findViewById(R.id.plot_village);
            variety=itemView.findViewById(R.id.variety);
            east=itemView.findViewById(R.id.east);
            west=itemView.findViewById(R.id.west);
            north=itemView.findViewById(R.id.north);
            south=itemView.findViewById(R.id.south);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
