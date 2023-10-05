package in.co.vibrant.bindalsugar.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotOverLappingModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.LatLngUtil;

public class PlotOverLappingAdapter extends RecyclerView.Adapter<PlotOverLappingAdapter.MyHolder> {

    List<PlotOverLappingModel> arrayList;
    AlertDialog Alertdialog;
    private Context context;
    private ProgressDialog dialog;


    public PlotOverLappingAdapter(Context context, List<PlotOverLappingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plot_overlapping_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            /*if(position==0)
            {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait ...");
                dialog.show();
            }*/
            holder.txt1.setText(arrayList.get(position).getSrNo());
            holder.txt2.setText(arrayList.get(position).getPlotType());
            holder.txt3.setText(arrayList.get(position).getPlotVillCode());
            holder.txt4.setText(arrayList.get(position).getPlotVillageName());
            holder.txt5.setText(arrayList.get(position).getPlotSerial());
            holder.txt6.setText(arrayList.get(position).getVillCode());
            holder.txt7.setText(arrayList.get(position).getVillageName());
            holder.txt8.setText(arrayList.get(position).getGrwCode());
            holder.txt9.setText(arrayList.get(position).getName());
            holder.txt10.setText(arrayList.get(position).getFatherName());
            holder.txt11.setText(arrayList.get(position).getGrwSrNo());
            holder.txt12.setText(arrayList.get(position).getShareArea());
            holder.txt13.setText(arrayList.get(position).getSharePer());
            holder.txt14.setText(arrayList.get(position).getPlotArea());
            holder.txt15.setText(arrayList.get(position).getOverLapPer());
            holder.txt16.setText(arrayList.get(position).getSurveyDate());
            holder.txt17.setText(arrayList.get(position).getSurveyorCode());
            holder.txt18.setText(arrayList.get(position).getSurveyorName());
            if (position % 2 == 0) {
                holder.map_view.setVisibility(View.GONE);
            } else {
                holder.map_view.setVisibility(View.VISIBLE);
            }
            holder.map_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetDataList().execute(
                            arrayList.get(position).getFactory(),
                            arrayList.get(position).getPlotVillCode(),
                            arrayList.get(position).getPlotSerial(),
                            arrayList.get(position + 1).getPlotSerial());
                }
            });


            holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt6.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt7.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt8.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt9.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt10.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt11.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt12.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt13.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt14.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt15.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt16.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt17.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt18.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
            /*if(arrayList.size()==(position+1))
            {
                dialog.dismiss();
            }*/
        } catch (Exception e) {
            dialog.dismiss();
        }

    }


    public void ShowMap(final PlotOverLappingModel plotOverLapping) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView map_view;
        RelativeLayout rlLayout, rl_text_1;
        TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12, txt13, txt14, txt15, txt16, txt17, txt18;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            map_view = itemView.findViewById(R.id.map_view);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
            txt4 = itemView.findViewById(R.id.txt4);
            txt5 = itemView.findViewById(R.id.txt5);
            txt6 = itemView.findViewById(R.id.txt6);
            txt7 = itemView.findViewById(R.id.txt7);
            txt8 = itemView.findViewById(R.id.txt8);
            txt9 = itemView.findViewById(R.id.txt9);
            txt10 = itemView.findViewById(R.id.txt10);
            txt11 = itemView.findViewById(R.id.txt11);
            txt12 = itemView.findViewById(R.id.txt12);
            txt13 = itemView.findViewById(R.id.txt13);
            txt14 = itemView.findViewById(R.id.txt14);
            txt15 = itemView.findViewById(R.id.txt15);
            txt16 = itemView.findViewById(R.id.txt16);
            txt17 = itemView.findViewById(R.id.txt17);
            txt18 = itemView.findViewById(R.id.txt18);
            rl_text_1 = itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

    private class GetDataList extends AsyncTask<String, Void, Void> {
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
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetPlotOverLapMapData);
                request1.addProperty("factory", params[0]);
                request1.addProperty("PlotVill", params[1]);
                request1.addProperty("Plot1", params[2]);
                request1.addProperty("Plot2", params[3]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetPlotOverLapMapData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetPlotOverLapMapDataResult").toString();
                }

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                //dashboardData=new ArrayList <>();
                JSONArray jsonArray = new JSONArray(message);
                final JSONObject basePlot = jsonArray.getJSONObject(0);
                final JSONObject overlapPlot = jsonArray.getJSONObject(1);
                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_overlap_on_map, null);
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
                            //LatLng latlng = new LatLng(growerActivityDetailsModel.getEastLat(), growerActivityDetailsModel.getEastLng());
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            ArrayList<LatLng> basePlotArrayList = new ArrayList<LatLng>();
                            basePlotArrayList.add(new LatLng(basePlot.getDouble("GH_NE_LAT"), basePlot.getDouble("GH_NE_LNG")));
                            basePlotArrayList.add(new LatLng(basePlot.getDouble("GH_SE_LAT"), basePlot.getDouble("GH_SE_LNG")));
                            basePlotArrayList.add(new LatLng(basePlot.getDouble("GH_SW_LAT"), basePlot.getDouble("GH_SW_LNG")));
                            basePlotArrayList.add(new LatLng(basePlot.getDouble("GH_NW_LAT"), basePlot.getDouble("GH_NW_LNG")));
                            LatLng latlng = new LatLngUtil().getPolygonCenterPoint(basePlotArrayList);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));

                            String color = "#6AFBED6A";//Yellow
                            if (basePlot.getString("COLORCODE").equalsIgnoreCase("0")) {
                                color = "#6AFBED6A";//Yellow
                            } else {
                                color = basePlot.getString("COLORCODE").toUpperCase();
                            }

                            Polygon basePlotPolygon = googleMap.addPolygon(new PolygonOptions()
                                    .add(basePlotArrayList.get(0), basePlotArrayList.get(1), basePlotArrayList.get(2), basePlotArrayList.get(3))
                                    .strokeColor(Color.RED)
                                    .strokeWidth(2)
                                    .fillColor(Color.parseColor(color))
                            );
                            Marker basePlotMarker = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(latlng)
                                            .title("Base Plot")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            ArrayList<LatLng> overlapPlotArrayList = new ArrayList<LatLng>();
                            overlapPlotArrayList.add(new LatLng(overlapPlot.getDouble("GH_NE_LAT"), overlapPlot.getDouble("GH_NE_LNG")));
                            overlapPlotArrayList.add(new LatLng(overlapPlot.getDouble("GH_SE_LAT"), overlapPlot.getDouble("GH_SE_LNG")));
                            overlapPlotArrayList.add(new LatLng(overlapPlot.getDouble("GH_SW_LAT"), overlapPlot.getDouble("GH_SW_LNG")));
                            overlapPlotArrayList.add(new LatLng(overlapPlot.getDouble("GH_NW_LAT"), overlapPlot.getDouble("GH_NW_LNG")));
                            LatLng overlapPlotLatlng = new LatLngUtil().getPolygonCenterPoint(overlapPlotArrayList);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    overlapPlotLatlng, 16.0f));
                            Polygon overlapPlotPolygon = googleMap.addPolygon(new PolygonOptions()
                                    .add(overlapPlotArrayList.get(0), overlapPlotArrayList.get(1), overlapPlotArrayList.get(2), overlapPlotArrayList.get(3))
                                    .strokeColor(Color.RED)
                                    .strokeWidth(2)
                                    .fillColor(Color.parseColor(color))
                            );
                            Marker overlapPlotMarker = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(overlapPlotLatlng)
                                            .title("Overlap Plot")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    LinearLayout info = new LinearLayout(context);
                                    try {
                                        info.setOrientation(LinearLayout.VERTICAL);
                                        TextView title = new TextView(context);
                                        title.setTextColor(Color.BLACK);
                                        title.setGravity(Gravity.LEFT);
                                        //title.setTypeface(null, Typeface.BOLD);
                                        title.setTextSize(10);
                                        if (marker.getTitle().equalsIgnoreCase("Base Plot")) {
                                            title.setText(
                                                    "Plot Village : " + basePlot.getString("GH_PLVILL") + " / " + basePlot.getString("VILLNAME") + "\n" +
                                                            "Grower Village : " + basePlot.getString("G_VILL") + " / " + basePlot.getString("GVILLNAME") + "\n" +
                                                            "Grower Code: " + basePlot.getString("GH_GROW") + "\n" +
                                                            "Grower : " + basePlot.getString("G_HNAME") + "/" + basePlot.getString("G_HFATHR") + "\n" +
                                                            "Variety : " + basePlot.getString("PRAKAR") + " (" + basePlot.getString("VR_NAME") + ")" + "\n" +
                                                            "Type : " + basePlot.getString("PRAJATI") + "\n" +
                                                            "Area : " + basePlot.getString("GH_AREA") + "\n" +
                                                            "Land Type : " + basePlot.getString("LT_NAME") + "\n" +
                                                            "Plot Serial Number : " + basePlot.getString("GH_SRNO")
                                            );
                                        } else {
                                            title.setText(
                                                    "Plot Village : " + overlapPlot.getString("GH_PLVILL") + " / " + overlapPlot.getString("VILLNAME") + "\n" +
                                                            "Grower Village : " + overlapPlot.getString("G_VILL") + " / " + overlapPlot.getString("GVILLNAME") + "\n" +
                                                            "Grower Code: " + overlapPlot.getString("GH_GROW") + "\n" +
                                                            "Grower : " + overlapPlot.getString("G_HNAME") + "/" + overlapPlot.getString("G_HFATHR") + "\n" +
                                                            "Variety : " + overlapPlot.getString("PRAKAR") + " (" + overlapPlot.getString("VR_NAME") + ")" + "\n" +
                                                            "Type : " + overlapPlot.getString("PRAJATI") + "\n" +
                                                            "Area : " + overlapPlot.getString("GH_AREA") + "\n" +
                                                            "Land Type : " + overlapPlot.getString("LT_NAME") + "\n" +
                                                            "Plot Serial Number : " + overlapPlot.getString("GH_SRNO")
                                            );
                                        }

                                        TextView snippet = new TextView(context);
                                        snippet.setTextColor(Color.GRAY);
                                        snippet.setText(marker.getSnippet());
                                        info.addView(title);
                                        //info.addView(snippet);
                                    } catch (Exception e) {
                                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                                    }
                                    return info;
                                }
                            });

                        } catch (Exception e) {

                        }
                    }
                });

                Button closeMap = mView.findViewById(R.id.closeMap);
                closeMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alertdialog.dismiss();
                    }
                });

                Alertdialog.show();
                Alertdialog.setCancelable(false);
                Alertdialog.setCanceledOnTouchOutside(true);
            } catch (JSONException e) {
                //AlertPopUp(message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                //("Error:"+e.toString());
            }
        }
    }

}
