package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class RaNonZoneWithVarietyAdapter extends RecyclerView.Adapter<RaNonZoneWithVarietyAdapter.MyHolder> {

    private Context context;
    JSONArray arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;
    double gtotalToDate=0.00,gTotalOnDate=0.00;

    public RaNonZoneWithVarietyAdapter(Context context, JSONArray arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_ra_variety_with_zone,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //,,,,,,,,,,,;
        try {
            if(arrayList.length()==(position+1))
            {
                holder.tvCount.setText("" + (position+1));
                holder.tv_name.setText("TOTAL");

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                int device_width = displaymetrics.widthPixels-50;
                int totalColumn = 4;
                int displayColumn = 4;
                device_width = device_width - totalColumn;
                int devicewidth = 0;
                if (device_width < 1100) {
                    devicewidth = device_width / displayColumn;
                } else {
                    devicewidth = device_width / displayColumn;
                }
                int lastWidth = device_width - (devicewidth * displayColumn);
                if (lastWidth <= devicewidth) {
                    lastWidth = devicewidth;
                }

                TableLayout dialogueTable = new TableLayout(context);
                dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dialogueTable.setShrinkAllColumns(false);
                dialogueTable.setStretchAllColumns(false);

                TableRow row = new TableRow(context);
                row.setId(position + 1);
                row.setBackgroundColor(Color.parseColor("#FFFFFF"));

                TableRow row1 = new TableRow(context);
                row1.setId(position + 2);
                row1.setBackgroundColor(Color.parseColor("#FFFFFF"));

                TextView tv3 = new TextView(context);
                tv3.setText("TOTAL TODATE TARGET");
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setWidth(devicewidth*2);
                tv3.setGravity(Gravity.LEFT);

                TextView tv4 = new TextView(context);
                tv4.setTextColor(Color.parseColor("#000000"));
                tv4.setText(new DecimalFormat("0.000").format(gtotalToDate));
                tv4.setWidth(devicewidth);
                tv4.setGravity(Gravity.RIGHT);

                row1.addView(tv3);
                row1.addView(tv4);

                dialogueTable.addView(row1);

                TableRow row2 = new TableRow(context);
                row2.setId(position + 2);
                row2.setBackgroundColor(Color.parseColor("#FFFFFF"));

                TextView tv5 = new TextView(context);
                tv5.setText("TOTAL ONDATE TARGET");
                tv5.setTextColor(Color.parseColor("#000000"));
                tv5.setWidth(devicewidth*2);
                tv5.setGravity(Gravity.LEFT);

                TextView tv6 = new TextView(context);
                tv6.setTextColor(Color.parseColor("#000000"));
                tv6.setText(new DecimalFormat("0.000").format(gTotalOnDate));
                tv6.setWidth(devicewidth);
                tv6.setGravity(Gravity.RIGHT);

                row2.addView(tv5);
                row2.addView(tv6);

                dialogueTable.addView(row2);



                holder.layout_booking.addView(dialogueTable);


            }
            else
            {
                JSONObject jsonObject=arrayList.getJSONObject(position);
                holder.tvCount.setText("" + (position+1));
                holder.tv_name.setText(jsonObject.getString("Zone").toUpperCase());

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                int device_width = displaymetrics.widthPixels-130;
                int totalColumn = 3;
                int displayColumn = 3;
                device_width = device_width - totalColumn;
                int devicewidth = 0;
                if (device_width < 1100) {
                    devicewidth = device_width / displayColumn;
                } else {
                    devicewidth = device_width / displayColumn;
                }
                int lastWidth = device_width - (devicewidth * displayColumn);
                if (lastWidth <= devicewidth) {
                    lastWidth = devicewidth;
                }


                TableLayout dialogueTable = new TableLayout(context);
                dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dialogueTable.setShrinkAllColumns(false);
                dialogueTable.setStretchAllColumns(false);
                TableRow tableHeading = new TableRow(context);

                tableHeading.setBackgroundColor(Color.parseColor("#000000"));

                TextView th1 = new TextView(context);
                th1.setTextColor(Color.parseColor("#FFFFFF"));
                th1.setText("Variety");
                th1.setGravity(Gravity.LEFT);
                th1.setWidth(devicewidth);
                tableHeading.addView(th1);

                TextView th3 = new TextView(context);
                th3.setTextColor(Color.parseColor("#FFFFFF"));
                th3.setText("TODATE");
                th3.setGravity(Gravity.RIGHT);
                th3.setWidth(devicewidth);
                tableHeading.addView(th3);

                TextView th4 = new TextView(context);
                th4.setTextColor(Color.parseColor("#FFFFFF"));
                th4.setText("ONDATE");
                th4.setGravity(Gravity.RIGHT);
                th4.setWidth(lastWidth);
                tableHeading.addView(th4);

                dialogueTable.addView(tableHeading);
                JSONArray jsonArray = jsonObject.getJSONArray("target");
                double totalOnDateArea=0.00,totalToDateArea=0.00;
                for (int j = 0; j < jsonArray.length(); j++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(j);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        totalOnDateArea = totalOnDateArea+object.getDouble("OndateArea");
                        totalToDateArea = totalToDateArea+object.getDouble("TodateArea");
                        gTotalOnDate = gTotalOnDate+object.getDouble("OndateArea");
                        gtotalToDate = gtotalToDate+object.getDouble("TodateArea");


                        TableRow row = new TableRow(context);
                        row.setId(position + j);

                        TextView tv1 = new TextView(context);
                        tv1.setText(object.getString("VRNAME"));
                        tv1.setWidth(devicewidth);
                        tv1.setGravity(Gravity.LEFT);

                        TextView tv3 = new TextView(context);
                        tv3.setText(new DecimalFormat("0.000").format(object.getDouble("TodateArea")));
                        tv3.setWidth(devicewidth);
                        tv3.setGravity(Gravity.RIGHT);

                        TextView tv4 = new TextView(context);
                        tv4.setText(new DecimalFormat("0.000").format(object.getDouble("OndateArea")));
                        tv4.setWidth(lastWidth);
                        tv4.setGravity(Gravity.RIGHT);

                        if (j % 2 == 0) {
                            row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv3.setTextColor(Color.parseColor("#000000"));
                            tv4.setTextColor(Color.parseColor("#000000"));
                        } else {
                            row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv3.setTextColor(Color.parseColor("#000000"));
                            tv4.setTextColor(Color.parseColor("#000000"));
                        }
                        row.addView(tv1);
                        row.addView(tv3);
                        row.addView(tv4);

                        dialogueTable.addView(row);
                    } catch (Exception e) {

                    }
                }
                TableRow row = new TableRow(context);
                row.setId(position + 1);
                row.setBackgroundColor(Color.parseColor("#000000"));

                TextView tv1 = new TextView(context);
                tv1.setText("TOTAL");
                tv1.setTextColor(Color.parseColor("#FFFFFF"));
                tv1.setWidth(devicewidth);
                tv1.setGravity(Gravity.LEFT);

                TextView tv3 = new TextView(context);
                tv3.setTextColor(Color.parseColor("#FFFFFF"));
                tv3.setText(new DecimalFormat("0.000").format(totalToDateArea));
                tv3.setWidth(devicewidth);
                tv3.setGravity(Gravity.RIGHT);

                TextView tv4 = new TextView(context);
                tv4.setTextColor(Color.parseColor("#FFFFFF"));
                tv4.setText(new DecimalFormat("0.000").format(totalOnDateArea));
                tv4.setWidth(lastWidth);
                tv4.setGravity(Gravity.RIGHT);

                row.addView(tv1);
                row.addView(tv3);
                row.addView(tv4);

                dialogueTable.addView(row);
                holder.layout_booking.removeAllViews();
                holder.layout_booking.addView(dialogueTable);


            }

        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.length();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout_booking;
        TextView tvCount,tv_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            layout_booking=itemView.findViewById(R.id.layout_booking);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
