package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ListGrowerFinderAdapter extends RecyclerView.Adapter<ListGrowerFinderAdapter.MyHolder> {

    private Context context;
    List <GrowerFinderModel> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public ListGrowerFinderAdapter(Context context, List <GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grower_finder,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName());
        holder.tv_father_name.setText(arrayList.get(position).getFather());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName()+" - "+arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getVillageName()+" - "+arrayList.get(position).getVillageCode());
        holder.tv_variety.setText("Variety: "+arrayList.get(position).getVariety());
        holder.tv_cane_type.setText("Cane Type: "+arrayList.get(position).getPrakar());
        holder.tv_cane_area.setText("Area: "+arrayList.get(position).getCaneArea());
        holder.tv_plot_serial_number.setText("Plot Sr No : "+arrayList.get(position).getPlotNo());
        holder.tv_variety_group.setText("Cane Type Group: "+arrayList.get(position).getVarietyGroupCode());
        holder.ivEdit.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_gps_fixed_white_24));
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMap(arrayList.get(position));
            }
        });
        //holder.ivEdit.setVisibility(View.GONE);
        /*holder.tv_id.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_mobile.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_factory.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));

        holder.tv_imei.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));*/
        //holder.tv_status.setText(arrayList.get(position).getTodate());
        /*if(arrayList.get(position).getStatus().equalsIgnoreCase("Stop")){
            holder.tv_status.setTextColor(Color.parseColor("#FFE91E63"));
        }
        else if(arrayList.get(position).getStatus().equalsIgnoreCase("Running"))
        {
            holder.tv_status.setTextColor(Color.parseColor("#FF4CAF50"));
        }*/

        /*if(arrayList.get(position).getName().equalsIgnoreCase("Total") ||
                arrayList.get(position).getName().equalsIgnoreCase("Avg") ||
                arrayList.get(position).getName().equalsIgnoreCase("Projected"))
        {
            holder.tv_name.setTypeface(null, Typeface.BOLD);
        }*/

    }

    public void ShowMap(final GrowerFinderModel growerActivityDetailsModel)
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
                LatLng latlng = new LatLng(growerActivityDetailsModel.getNorthEastLat(), growerActivityDetailsModel.getNorthEastLng());
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(growerActivityDetailsModel.getNorthEastLat(), growerActivityDetailsModel.getNorthEastLng()), 16.0f));

                PolygonOptions polygonOptions=new PolygonOptions();
                //ne,se,sw,nw
                if(growerActivityDetailsModel.getNorthEastLat()>0)
                    polygonOptions.add(new LatLng(growerActivityDetailsModel.getNorthEastLat(), growerActivityDetailsModel.getNorthEastLng()));
                if(growerActivityDetailsModel.getSouthEastLat()>0)
                    polygonOptions.add(new LatLng(growerActivityDetailsModel.getSouthEastLat(), growerActivityDetailsModel.getSouthEastLng()));
                if(growerActivityDetailsModel.getSouthWestLat()>0)
                    polygonOptions.add(new LatLng(growerActivityDetailsModel.getSouthWestLat(), growerActivityDetailsModel.getSouthWestLng()));
                if(growerActivityDetailsModel.getNorthWestLat()>0)
                    polygonOptions.add(new LatLng(growerActivityDetailsModel.getNorthWestLat(), growerActivityDetailsModel.getNorthWestLng()));

                polygonOptions.strokeColor(Color.parseColor("#000000"));
                polygonOptions.strokeWidth(2);
                polygonOptions.fillColor(Color.parseColor("#6AFBED6A"));

                googleMap.addPolygon(polygonOptions);
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
                    navigateforclient(growerActivityDetailsModel.getNorthEastLat(), growerActivityDetailsModel.getNorthEastLng());
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
        RelativeLayout rlLayout,rl_text_1;
        ImageView ivEdit;
        TextView tvCount,tv_grower_name,tv_father_name,tv_plot_village,tv_grower_village,tv_variety,tv_cane_type,
                tv_cane_area,tv_variety_group,tv_plot_serial_number;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_father_name=itemView.findViewById(R.id.tv_father_name);
            tv_plot_village=itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village=itemView.findViewById(R.id.tv_grower_village);
            tv_variety=itemView.findViewById(R.id.tv_variety);
            tv_cane_type=itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area=itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group=itemView.findViewById(R.id.tv_variety_group);
            tv_plot_serial_number=itemView.findViewById(R.id.tv_plot_serial_number);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
