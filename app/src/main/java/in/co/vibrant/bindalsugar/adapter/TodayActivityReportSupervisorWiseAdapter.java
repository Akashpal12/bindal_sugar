package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ReportMultiple;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class TodayActivityReportSupervisorWiseAdapter extends RecyclerView.Adapter<TodayActivityReportSupervisorWiseAdapter.MyHolder> {

    private Context context;
    List<ReportMultiple> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;
    //double gtotalTarget=0.00,gtotalToDate=0.00,gTotalOnDate=0.00;

    public TodayActivityReportSupervisorWiseAdapter(Context context, List<ReportMultiple> arrayList) {
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
        try {
            Typeface typeface1 = ResourcesCompat.getFont(context, R.font.roboto_black);

            holder.tvCount.setText("" + (position+1));
            holder.tv_name.setText(arrayList.get(position).getName().toUpperCase());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels-150;
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

            int color = Color.argb(255, new Random().nextInt(150), new Random().nextInt(150), new Random().nextInt(150));
            tableHeading.setBackgroundColor(color);

            TableRow tableHeading1 = new TableRow(context);

            //tableHeading1.setBackgroundColor(Color.parseColor("#000000"));
            tableHeading1.setBackgroundColor(color);

            TextView th11 = new TextView(context);
            th11.setTextColor(Color.parseColor("#FFFFFF"));
            th11.setText("Supervisor");
            th11.setGravity(Gravity.LEFT);
            th11.setWidth(devicewidth);
            tableHeading1.addView(th11);

            TextView th12 = new TextView(context);
            th12.setTextColor(Color.parseColor("#FFFFFF"));
            th12.setText("On Date Plot");
            th12.setGravity(Gravity.CENTER);
            th12.setWidth(devicewidth/2);
            tableHeading1.addView(th12);

            TextView th13 = new TextView(context);
            th13.setTextColor(Color.parseColor("#FFFFFF"));
            th13.setText("To Date Plot");
            th13.setGravity(Gravity.CENTER);
            th13.setWidth(devicewidth/2);
            tableHeading1.addView(th13);

            TextView th14 = new TextView(context);
            th14.setTextColor(Color.parseColor("#FFFFFF"));
            th14.setText("On Date Area");
            th14.setGravity(Gravity.CENTER);
            th14.setWidth(devicewidth/2);
            tableHeading1.addView(th14);

            TextView th15 = new TextView(context);
            th15.setTextColor(Color.parseColor("#FFFFFF"));
            th15.setText("To Date Area");
            th15.setGravity(Gravity.CENTER);
            th15.setWidth(devicewidth/2);
            tableHeading1.addView(th15);

            dialogueTable.addView(tableHeading1);


            th11.setTypeface(typeface1);
            th12.setTypeface(typeface1);
            th13.setTypeface(typeface1);
            th14.setTypeface(typeface1);
            th15.setTypeface(typeface1);


            JSONArray jsonArray = arrayList.get(position).getJsonArray();
            double totalOnDatePlot=0.00,totalToDatePlot=0.00,totalOnDateArea=0.00,totalToDateArea=0.00;
            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(j);
                    DecimalFormat decimalFormat = new DecimalFormat("0.000");
                    totalOnDatePlot = totalOnDatePlot+object.getDouble("ONDATEPLOT");
                    totalToDatePlot = totalToDatePlot+object.getDouble("TODATEPLOT");
                    totalOnDateArea = totalOnDateArea+object.getDouble("ONDATEAREA");
                    totalToDateArea = totalToDateArea+object.getDouble("TODATEAREA");
                    //gtotalTarget = gtotalTarget+object.getDouble("TRGSEEDQTY");
                    //gTotalOnDate = gTotalOnDate+object.getDouble("TRGGROW");
                    //gtotalToDate = gtotalToDate+object.getDouble("ACHIVSEEDQTY");

                    TableRow row = new TableRow(context);
                    row.setId(position + j);

                    TextView tv1 = new TextView(context);
                    tv1.setText(object.getString("UCODE")+" / "+object.getString("UNAME"));
                    tv1.setWidth(devicewidth);  tv1.setTypeface(typeface1);

                    tv1.setBackgroundResource(R.drawable.list_border);
                    tv1.setGravity(Gravity.LEFT);

                    TextView tv2 = new TextView(context);
                    tv2.setText(new DecimalFormat("0").format(object.getDouble("ONDATEPLOT")));
                    tv2.setWidth(devicewidth/2);
                    tv1.setWidth(devicewidth);  tv1.setTypeface(typeface1);
                    tv2.setBackgroundResource(R.drawable.list_border);
                    tv2.setGravity(Gravity.RIGHT);

                    TextView tv3 = new TextView(context);
                    tv3.setText(new DecimalFormat("0").format(object.getDouble("TODATEPLOT")));
                    tv3.setWidth(devicewidth/2);
                    tv3.setBackgroundResource(R.drawable.list_border);
                    tv3.setGravity(Gravity.RIGHT);
                    tv1.setWidth(devicewidth);
                    tv3.setTypeface(typeface1);
                    TextView tv4 = new TextView(context);
                    tv4.setText(new DecimalFormat("0.000").format(object.getDouble("ONDATEAREA")));
                    tv4.setWidth(devicewidth/2);
                    tv4.setBackgroundResource(R.drawable.list_border);
                    tv4.setGravity(Gravity.RIGHT);
                    tv1.setWidth(devicewidth);
                    tv1.setTypeface(typeface1);
                    TextView tv5 = new TextView(context);
                    tv5.setText(new DecimalFormat("0.000").format(object.getDouble("TODATEAREA")));
                    tv5.setWidth(devicewidth/2);
                    tv5.setBackgroundResource(R.drawable.list_border);
                    tv5.setGravity(Gravity.RIGHT);


                    if (j % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                        tv5.setTextColor(Color.parseColor("#000000"));
                    } else {
                        row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                        tv5.setTextColor(Color.parseColor("#000000"));
                    }
                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    row.addView(tv5);

                    tv2.setTypeface(typeface1);
                    tv3.setTypeface(typeface1);
                    tv4.setTypeface(typeface1);
                    tv5.setTypeface(typeface1);
                    dialogueTable.addView(row);
                } catch (Exception e) {

                }
            }
            TableRow row = new TableRow(context);
            row.setId(position + 1);
            row.setBackgroundColor(color);

            TextView tv1 = new TextView(context);
            tv1.setText("TOTAL");
            tv1.setTypeface(typeface1);
            tv1.setTextColor(Color.parseColor("#FFFFFF"));
            tv1.setWidth(devicewidth);

            tv1.setGravity(Gravity.LEFT);

            TextView tv2 = new TextView(context);
            tv2.setTextColor(Color.parseColor("#FFFFFF"));
            tv2.setText(new DecimalFormat("0").format(totalOnDatePlot));
            tv2.setWidth(devicewidth/2);
            tv2.setTypeface(typeface1);
            tv2.setGravity(Gravity.RIGHT);

            TextView tv3 = new TextView(context);
            tv3.setTextColor(Color.parseColor("#FFFFFF"));
            tv3.setText(new DecimalFormat("0").format(totalToDatePlot));
            tv3.setWidth(devicewidth/2);
            tv3.setGravity(Gravity.RIGHT);
            tv3.setTypeface(typeface1);
            TextView tv4 = new TextView(context);
            tv4.setTextColor(Color.parseColor("#FFFFFF"));
            tv4.setText(new DecimalFormat("0.000").format(totalOnDateArea));
            tv4.setWidth(devicewidth/2);
            tv4.setGravity(Gravity.RIGHT);
            tv4.setTypeface(typeface1);
            TextView tv5 = new TextView(context);
            tv5.setTextColor(Color.parseColor("#FFFFFF"));
            tv5.setText(new DecimalFormat("0.000").format(totalToDateArea));
            tv5.setWidth(devicewidth/2);
            tv5.setTypeface(typeface1);
            tv5.setGravity(Gravity.RIGHT);

            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            row.addView(tv4);
            row.addView(tv5);

            dialogueTable.addView(row);
            holder.layout_booking.removeAllViews();
            holder.layout_booking.addView(dialogueTable);

        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
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
