package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.TruckInCentreModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class TruckInYardAdapter extends RecyclerView.Adapter<TruckInYardAdapter.MyHolder> {

    private Context context;
    List<TruckInCentreModal> arrayList;
    AlertDialog Alertdialog;


    public TruckInYardAdapter(Context context, List<TruckInCentreModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_truck_in_yard,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.centre_code.setText(arrayList.get(position).getCentreCode());
        holder.centre_name.setText(arrayList.get(position).getCentreName());
        holder.truck_number.setText(arrayList.get(position).getTruckName());
        holder.driver_name.setText(arrayList.get(position).getDriverName());
        holder.driver_number.setText(arrayList.get(position).getDriverNumber());
        holder.arrival.setText(arrayList.get(position).getDeparterTime());
        holder.wait_time.setText(arrayList.get(position).getWaitTime());
        holder.yard_time.setText(arrayList.get(position).getTranTime());

        //holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.centre_code.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.centre_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.truck_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.arrival.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.wait_time.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.yard_time.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        if (arrayList.get(position).getDriverNumber().length() == 10) {
            holder.ivCall.setVisibility(View.VISIBLE);
        }
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (arrayList.get(position).getDriverNumber().length() == 10) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + arrayList.get(position).getDriverNumber()));
                        context.startActivity(intent);
                    }
                }
                catch (SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }

            }
        });
        holder.ivgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMap();
            }
        });
        //CentreTruckListActivity
    }

    public void ShowMap()
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_truck_location_on_map, null);
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

        Alertdialog.show();
        Alertdialog.setCancelable(false);
        Alertdialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ivCall,ivgps;
        TextView centre_code,centre_name,truck_number,driver_name,driver_number,arrival,wait_time,yard_time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            centre_code=itemView.findViewById(R.id.centre_code);
            centre_name=itemView.findViewById(R.id.centre_name);
            driver_name=itemView.findViewById(R.id.driver_name);
            driver_number=itemView.findViewById(R.id.driver_number);
            truck_number=itemView.findViewById(R.id.truck_number);
            arrival=itemView.findViewById(R.id.arrival);
            wait_time=itemView.findViewById(R.id.wait_time);
            yard_time=itemView.findViewById(R.id.yard_time);
            ivCall=itemView.findViewById(R.id.ivCall);
            ivgps=itemView.findViewById(R.id.ivgps);
        }
//dialogue_single_truck_location_on_driver_name,map
        //public ImageView getImage(){ return this.imageView;}
    }

}
