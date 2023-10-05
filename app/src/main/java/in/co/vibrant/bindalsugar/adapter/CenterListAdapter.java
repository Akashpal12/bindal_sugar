package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.co.vibrant.bindalsugar.R;


public class CenterListAdapter extends RecyclerView.Adapter<CenterListAdapter.MyHolder> {

  /*  private Context context;
    List<CenterModal> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public CenterListAdapter(Context context, List<CenterModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }*/

    private Context context;

    int datasize = 0;
    public  CenterListAdapter(Context context,int datasize) {
        this.context = context;
        this.datasize = datasize;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_center,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
       /* holder.code.setText(arrayList.get(position).getCode());
        holder.name.setText(arrayList.get(position).getName());
        holder.distance.setText(arrayList.get(position).getDistance());

        holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.code.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.distance.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMap(arrayList.get(position).getLat(),arrayList.get(position).getLng(),arrayList.get(position).getRadious());
            }
        });
        if(position==0)
        {
            holder.iconrl.setVisibility(View.GONE);
        }
        holder.truckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CentreTruckListActivity.class);
                intent.putExtra("center_code",arrayList.get(position).getCode());
                intent.putExtra("center_name",arrayList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    public void ShowMap(final String strLat, final String strLng, final String StrRadious)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_center_location_on_map, null);
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
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    double lat = Double.parseDouble(strLat);
                    double lng = Double.parseDouble(strLng);
                    double radious = Double.parseDouble(StrRadious);
                    LatLng latlng = new LatLng(lat, lng);
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(lat, lng), 16.0f));
                    Marker melbourne = googleMap.addMarker(
                            new MarkerOptions()
                                    .position(latlng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    Circle circle = googleMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(radious)
                            .strokeColor(Color.parseColor("#90FFEB3B"))
                            .fillColor(Color.parseColor("#90FFEB3B"))
                            );
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
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

        Button navigateMap=mView.findViewById(R.id.navigateMap);
        navigateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    navigateforclient(Double.parseDouble(strLat),Double.parseDouble(strLng));
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

        }*/

    }

    @Override
    public int getItemCount() {
        return datasize;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView mapBtn,truckBtn;
        RelativeLayout rl_text,iconrl;
        TextView code,name,distance;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            truckBtn=itemView.findViewById(R.id.truckBtn);
            mapBtn=itemView.findViewById(R.id.mapBtn);
            code=itemView.findViewById(R.id.code);
            name=itemView.findViewById(R.id.name);
            distance=itemView.findViewById(R.id.distance);
            rl_text=itemView.findViewById(R.id.rl_text);
            iconrl=itemView.findViewById(R.id.iconrl);
        }
//dialogue_single_truck_location_on_map
        //public ImageView getImage(){ return this.imageView;}
    }

}
