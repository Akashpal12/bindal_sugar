package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.TruckInCentreModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class TruckInTransitAdapter extends RecyclerView.Adapter<TruckInTransitAdapter.MyHolder> {

    private Context context;
    List<TruckInCentreModal> arrayList;
    AlertDialog Alertdialog;


    public TruckInTransitAdapter(Context context, List<TruckInCentreModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_truck_in_transit,null);
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
        holder.wait_time.setText(arrayList.get(position).getTranTime());
        holder.cane_weight.setText(arrayList.get(position).getCaneWeight());

        //holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.centre_code.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.centre_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.truck_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.arrival.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.wait_time.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.cane_weight.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
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
                //ShowMap();
               // new GetLiveLocation().execute(arrayList.get(position).getTruckName(),arrayList.get(position).getFactoryCode());
            }
        });
        //CentreTruckListActivity
    }

    /*public void ShowMap()
    {
        android.app.AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
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

    }*/

    /*private class GetLiveLocation extends AsyncTask<String, Void, Void> {
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
    }*/

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ivCall,ivgps;
        TextView centre_code,centre_name,truck_number,driver_name,driver_number,arrival,wait_time,cane_weight;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            centre_code=itemView.findViewById(R.id.centre_code);
            centre_name=itemView.findViewById(R.id.centre_name);
            driver_name=itemView.findViewById(R.id.driver_name);
            driver_number=itemView.findViewById(R.id.driver_number);
            truck_number=itemView.findViewById(R.id.truck_number);
            arrival=itemView.findViewById(R.id.arrival);
            wait_time=itemView.findViewById(R.id.wait_time);
            cane_weight=itemView.findViewById(R.id.cane_weight);
            ivCall=itemView.findViewById(R.id.ivCall);
            ivgps=itemView.findViewById(R.id.ivgps);
        }
//dialogue_single_truck_location_on_driver_name,map
        //public ImageView getImage(){ return this.imageView;}
    }

}
