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


public class TruckListAdapter extends RecyclerView.Adapter<TruckListAdapter.MyHolder> {

    private Context context;

    int datasize = 0;
    public  TruckListAdapter(Context context,int datasize) {
        this.context = context;
        this.datasize = datasize;
  /*  private Context context;
    List<TruckDetails> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public TruckListAdapter(Context context, List<TruckDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }*/
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_truck,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
      /*  holder.name.setText(arrayList.get(position).getTruckName());
        holder.driver_name.setText(arrayList.get(position).getDriverName());
        holder.driver_number.setText(arrayList.get(position).getDriverMobile());

        holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  new GetLiveLocation().execute(arrayList.get(position).getTruckCode(),arrayList.get(position).getFactoryCode());
                //ShowMap();
            }
        });
        if(position==0)
        {
            holder.iconrl.setVisibility(View.GONE);
        }
        else
        {
            if(arrayList.get(position).getStatus().equalsIgnoreCase("ACTIVE"))
            {
                holder.iconrl.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.iconrl.setVisibility(View.GONE);
            }
        }//CentreTruckListActivity
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
        Alertdialog.setCanceledOnTouchOutside(true);*/

    }

 /*   private class GetLiveLocation extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetTruckCurrentLocation);
                request1.addProperty("tr_number", params[0]);
                request1.addProperty("f_code", params[1]);
                request1.addProperty("imeino", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetTruckCurrentLocation, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetTruckCurrentLocationResult").toString();
                }
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    final JSONObject object=jsonArray.getJSONObject(0);
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_truck_location_on_map, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    GoogleMap googleMap;
                    Button navigateMap=mView.findViewById(R.id.navigateMap);
                    TextView address=mView.findViewById(R.id.address);
                    TextView last_time=mView.findViewById(R.id.last_time);
                    TextView speed=mView.findViewById(R.id.speed);
                    TextView battery=mView.findViewById(R.id.battery);
                    address.setText("Location: "+object.getString("ADDRESS"));
                    last_time.setText("Last updated at: "+object.getString("CORDINATEDATE"));
                    speed.setText("Speed: "+object.getString("SPEED")+"Km/Hr");
                    battery.setText("Battery: "+object.getString("BATTERYPERCENT")+"%");
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
                                double lat = Double.parseDouble(object.getString("LAT"));
                                double lng = Double.parseDouble(object.getString("LNG"));
                                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                LatLng latlng = new LatLng(lat, lng);
                                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat, lng), 18.0f));
                                Marker melbourne = googleMap.addMarker(
                                        new MarkerOptions()
                                                .position(latlng)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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

                    navigateMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                navigateforclient(Double.parseDouble(object.getString("LAT")), Double.parseDouble(object.getString("LAT")));
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
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
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

    }*/


    @Override
    public int getItemCount() {
        return datasize;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView mapBtn;
        RelativeLayout rl_text,iconrl;
        TextView name,driver_name,driver_number;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mapBtn=itemView.findViewById(R.id.mapBtn);
            name=itemView.findViewById(R.id.name);
            driver_name=itemView.findViewById(R.id.driver_name);
            driver_number=itemView.findViewById(R.id.driver_number);
            rl_text=itemView.findViewById(R.id.rl_text);
            iconrl=itemView.findViewById(R.id.iconrl);
        }
//dialogue_single_truck_location_on_driver_name,map
        //public ImageView getImage(){ return this.imageView;}
    }

}
