package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.RemedialPlotsModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.PocPlotsAllPlotsView;
import in.co.vibrant.bindalsugar.view.supervisor.RemedialPlotFinderMapView;

public class RemedialPlotsAdapter extends RecyclerView.Adapter<RemedialPlotsAdapter.MyHolder> {

    private Context context;
    List<RemedialPlotsModel> arrayList;
    FusedLocationProviderClient fusedLocationClient;
    AlertDialog Alertdialog;


    public RemedialPlotsAdapter(Context context, List<RemedialPlotsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.poc_plots_reportlist, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
           if (position == 0) {
                try {

                    holder.poc_plot_code_name.setText(" Desease Code/Name");
                    holder.plot_village_code_name.setText(" Plot Village Code/Name");
                    holder.plotsr_no.setText(" Plot Sr No");
                    holder.grower_code_name_father.setText(" Grower Code/Name/Father");
                    holder.plot_find_txt.setText(" Find Plot");
                    holder.navigation_txt.setText(" Navigation");
                    holder.view_all_txt.setText(" View All Plots");



                    holder.navigation_img.setVisibility(View.GONE);
                    holder.plot_find_img.setVisibility(View.GONE);
                    holder.plot_find_txt.setVisibility(View.VISIBLE);
                    holder.navigation_txt.setVisibility(View.VISIBLE);
                    holder.view_all_plots_img.setVisibility(View.GONE);
                    holder.view_all_txt.setVisibility(View.VISIBLE);

                    holder.poc_plot_code_name.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.plot_village_code_name.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.plotsr_no.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.grower_code_name_father.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.plot_find_txt.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.navigation_txt.setTypeface(Typeface.DEFAULT_BOLD);


                } catch (Exception e) {

                }


            }


            else {
                try {
                    final int pos = position - 1;
                    holder.poc_plot_code_name.setText(" "+arrayList.get(pos).getDISEASECODE()+"/"+arrayList.get(pos).getDISEASE());
                    holder.plot_village_code_name.setText(" "+arrayList.get(pos).getPLVLCODE()+"/"+arrayList.get(pos).getPLVLNAME());
                    holder.plotsr_no.setText(" "+arrayList.get(pos).getPLNO());
                    holder.grower_code_name_father.setText(" "+arrayList.get(pos).getG_CODE()+"/"+arrayList.get(pos).getG_NAME()+"/"+arrayList.get(pos).getG_FATHER());
                    holder.navigation_img.setVisibility(View.VISIBLE);
                    holder.plot_find_img.setVisibility(View.VISIBLE);
                    holder.view_all_plots_img.setVisibility(View.VISIBLE);
                    holder.plot_find_txt.setVisibility(View.GONE);
                    holder.navigation_txt.setVisibility(View.GONE);
                    holder.view_all_txt.setVisibility(View.GONE);





                    holder.navigation_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ShowMap(arrayList.get(pos));                            }
                            catch (Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }

                        }
                    });

                    holder.view_all_plots_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent a = new Intent(context, PocPlotsAllPlotsView.class);
                            a.putExtra("V_CODE",arrayList.get(pos).getPLVLCODE());
                            context.startActivity(a);


                        }
                    });
                    holder.plot_find_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RemedialPlotFinderMapView.class);
                            intent.putExtra("lat",arrayList.get(pos).getGH_NE_LAT());
                            intent.putExtra("lng",arrayList.get(pos).getGH_NE_LNG());
                           intent.putExtra("GET_DISEASE",arrayList.get(pos).getDISEASECODE());
                            context.startActivity(intent);
                           // ((Activity)context).finish();
                        }
                    });




                }catch (Exception e){

                }

            }
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }


    }
    public void ShowMap(final RemedialPlotsModel model)
    {
        try{
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_poc_plot_map, null);
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
                       // ne,se,sw,nw
                        LatLng latlng = new LatLng(model.getGH_NE_LAT(), model.getGH_NE_LNG());
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(model.getGH_NE_LAT(), model.getGH_NE_LNG()), 16.0f));
//                Marker melbourne = googleMap.addMarker(
//                        new MarkerOptions()
//                                .position(latlng)
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        PolygonOptions polygonOptions=new PolygonOptions();
                        if(model.getGH_NE_LAT()>0)
                            polygonOptions.add(new LatLng(model.getGH_NE_LAT(), model.getGH_NE_LNG()));
                        if(model.getGH_SE_LAT()>0)
                            polygonOptions.add(new LatLng(model.getGH_SE_LAT(), model.getGH_SE_LNG()));
                        if(model.getGH_SW_LAT()>0)
                            polygonOptions.add(new LatLng(model.getGH_SW_LAT(), model.getGH_SW_LNG()));
                        if(model.getGH_NW_LAT()>0)
                            polygonOptions.add(new LatLng(model.getGH_NW_LAT(), model.getGH_NW_LNG()));

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
                        navigateforclient(model.getGH_NE_LAT(), model.getGH_NE_LNG());
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
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView heading, poc_plot_code_name, plot_village_code_name, plotsr_no, grower_code_name_father, plot_find_txt, navigation_txt,view_all_txt;
        ImageView navigation_img,plot_find_img,view_all_plots_img;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            poc_plot_code_name = itemView.findViewById(R.id.poc_plot_code_name);
            plot_village_code_name = itemView.findViewById(R.id.plot_village_code_name);
            plotsr_no = itemView.findViewById(R.id.plotsr_no);
            grower_code_name_father = itemView.findViewById(R.id.grower_code_name_father);
            plot_find_txt = itemView.findViewById(R.id.plot_find_txt);
            navigation_txt = itemView.findViewById(R.id.navigation_txt);
            navigation_img = itemView.findViewById(R.id.navigation_img);
            plot_find_img = itemView.findViewById(R.id.plot_find_img);
            heading = itemView.findViewById(R.id.heading);
            view_all_txt = itemView.findViewById(R.id.view_all_txt);
            view_all_plots_img = itemView.findViewById(R.id.view_all_plots_img);


        }


    }


}
