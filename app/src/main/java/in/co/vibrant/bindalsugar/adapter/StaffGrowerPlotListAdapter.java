package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class StaffGrowerPlotListAdapter extends RecyclerView.Adapter<StaffGrowerPlotListAdapter.MyHolder> {

    private Context context;
    List <GrowerModel> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public StaffGrowerPlotListAdapter(Context context, List <GrowerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grower_plot_list,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {
            if(arrayList.get(position).getLat1()==0 && arrayList.get(position).getLat2()==0)
            {
                holder.edit.setVisibility(View.GONE);
            }
            holder.edit.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_map_24));
            holder.txt1.setText(arrayList.get(position).getPlotSerial());
            holder.txt2.setText(arrayList.get(position).getGrowerSerial());
            holder.txt3.setText(arrayList.get(position).getPlotVillageCode());
            holder.txt4.setText(arrayList.get(position).getArea());
            holder.txt5.setText(arrayList.get(position).getShareArea());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent=new Intent(context, StaffGrowerPlotDetails.class);
                    intent.putExtra("vill",arrayList.get(position).getVillageCode());
                    intent.putExtra("grower",arrayList.get(position).getGrowerCode());
                    context.startActivity(intent);*/

                    ShowMap(arrayList.get(position));
                }
            });


            holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / 4;
            } else {
                devicewidth = device_width / 6;
            }
            int lastWidth = device_width - (devicewidth * 6);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }

            holder.txt1.getLayoutParams().width = devicewidth;
            holder.txt2.getLayoutParams().width = devicewidth;
            holder.txt3.getLayoutParams().width = devicewidth;
            holder.txt4.getLayoutParams().width = devicewidth;
            holder.txt5.getLayoutParams().width = lastWidth;
            holder.rl_text.getLayoutParams().height = 100;
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


    public void ShowMap(final GrowerModel growerActivityDetailsModel)
    {
        try{
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_plot_map, null);
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
                    try{
                        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        LatLng latlng = new LatLng(growerActivityDetailsModel.getLat1(), growerActivityDetailsModel.getLng1());
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(growerActivityDetailsModel.getLat1(), growerActivityDetailsModel.getLng1()), 16.0f));
//                Marker melbourne = googleMap.addMarker(
//                        new MarkerOptions()
//                                .position(latlng)
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        PolygonOptions polygonOptions=new PolygonOptions();
                        if(growerActivityDetailsModel.getLat1()>0)
                            polygonOptions.add(new LatLng(growerActivityDetailsModel.getLat1(), growerActivityDetailsModel.getLng1()));
                        if(growerActivityDetailsModel.getLat2()>0)
                            polygonOptions.add(new LatLng(growerActivityDetailsModel.getLat2(), growerActivityDetailsModel.getLng2()));
                        if(growerActivityDetailsModel.getLat3()>0)
                            polygonOptions.add(new LatLng(growerActivityDetailsModel.getLat3(), growerActivityDetailsModel.getLng3()));
                        if(growerActivityDetailsModel.getLat4()>0)
                            polygonOptions.add(new LatLng(growerActivityDetailsModel.getLat4(), growerActivityDetailsModel.getLng4()));

                        polygonOptions.strokeColor(Color.parseColor("#000000"));
                        polygonOptions.strokeWidth(2);
                        polygonOptions.fillColor(Color.parseColor("#6AFBED6A"));

                        googleMap.addPolygon(polygonOptions);
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"Error: "+e.toString());
                    }

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
                        navigateforclient(growerActivityDetailsModel.getLat1(), growerActivityDetailsModel.getLng1());
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
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
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
        RelativeLayout rl_text;
        ImageView edit;
        TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            txt5=itemView.findViewById(R.id.txt5);
            txt6=itemView.findViewById(R.id.txt6);
            txt7=itemView.findViewById(R.id.txt7);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text=itemView.findViewById(R.id.rl_text);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

