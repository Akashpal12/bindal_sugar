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
import java.util.List;
import java.util.Random;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ReportMultiple;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class RaVillageWithVarietyAdapter extends RecyclerView.Adapter<RaVillageWithVarietyAdapter.MyHolder> {

    private Context context;
    List<ReportMultiple> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;
    //double gtotalTarget=0.00,gtotalToDate=0.00,gTotalOnDate=0.00;

    public RaVillageWithVarietyAdapter(Context context, List<ReportMultiple> arrayList) {
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
            holder.tvCount.setText("" + (position+1));
            holder.tv_name.setText(arrayList.get(position).getName().toUpperCase());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels-50;
            int totalColumn = 5;
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
            TableRow tableHeading = new TableRow(context);

            int color = Color.argb(255, new Random().nextInt(150), new Random().nextInt(150), new Random().nextInt(150));
            tableHeading.setBackgroundColor(color);

            TextView th1 = new TextView(context);
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th1.setText("");
            th1.setGravity(Gravity.LEFT);
            th1.setWidth(devicewidth);
            tableHeading.addView(th1);

            TextView th2 = new TextView(context);
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setText("TAR");
            th2.setGravity(Gravity.RIGHT);
            th2.setWidth(devicewidth);
            tableHeading.addView(th2);

            TextView th3 = new TextView(context);
            th3.setTextColor(Color.parseColor("#FFFFFF"));
            th3.setText("GET");
            th3.setGravity(Gravity.LEFT);
            th3.setWidth(devicewidth);
            tableHeading.addView(th3);

            TextView th4 = new TextView(context);
            th4.setTextColor(Color.parseColor("#FFFFFF"));
            th4.setText("ACHI");
            th4.setGravity(Gravity.RIGHT);
            th4.setWidth(devicewidth);
            tableHeading.addView(th4);

            TextView th5 = new TextView(context);
            th5.setTextColor(Color.parseColor("#FFFFFF"));
            th5.setText("EVED");
            th5.setGravity(Gravity.LEFT);
            th5.setWidth(devicewidth);
            tableHeading.addView(th5);

            dialogueTable.addView(tableHeading);

            TableRow tableHeading1 = new TableRow(context);

            //tableHeading1.setBackgroundColor(Color.parseColor("#000000"));
            tableHeading1.setBackgroundColor(color);

            TextView th11 = new TextView(context);
            th11.setTextColor(Color.parseColor("#FFFFFF"));
            th11.setText("Variety");
            th11.setGravity(Gravity.LEFT);
            th11.setWidth(devicewidth);
            tableHeading1.addView(th11);

            TextView th12 = new TextView(context);
            th12.setTextColor(Color.parseColor("#FFFFFF"));
            th12.setText("SEED (Qtl.)");
            th12.setGravity(Gravity.CENTER);
            th12.setWidth(devicewidth);
            tableHeading1.addView(th12);

            TextView th13 = new TextView(context);
            th13.setTextColor(Color.parseColor("#FFFFFF"));
            th13.setText("NO OF GROWER");
            th13.setGravity(Gravity.CENTER);
            th13.setWidth(devicewidth);
            tableHeading1.addView(th13);

            TextView th14 = new TextView(context);
            th14.setTextColor(Color.parseColor("#FFFFFF"));
            th14.setText("SEED (Qtl.)");
            th14.setGravity(Gravity.CENTER);
            th14.setWidth(devicewidth);
            tableHeading1.addView(th14);

            TextView th15 = new TextView(context);
            th15.setTextColor(Color.parseColor("#FFFFFF"));
            th15.setText("NO OF GROWER");
            th15.setGravity(Gravity.CENTER);
            th15.setWidth(devicewidth);
            tableHeading1.addView(th15);

            dialogueTable.addView(tableHeading1);

            JSONArray jsonArray = arrayList.get(position).getJsonArray();
            double totalTargetSeed=0.00,totalTargetGrower=0.00,totalAchievedSeed=0.00,totalAchievedGrower=0.00;
            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(j);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    totalTargetSeed = totalTargetSeed+object.getDouble("TRGSEEDQTY");
                    totalTargetGrower = totalTargetGrower+object.getDouble("TRGGROW");
                    totalAchievedSeed = totalAchievedSeed+object.getDouble("ACHIVSEEDQTY");
                    totalAchievedGrower = totalAchievedGrower+object.getDouble("ACHIVGROW");
                    //gtotalTarget = gtotalTarget+object.getDouble("TRGSEEDQTY");
                    //gTotalOnDate = gTotalOnDate+object.getDouble("TRGGROW");
                    //gtotalToDate = gtotalToDate+object.getDouble("ACHIVSEEDQTY");


                    TableRow row = new TableRow(context);
                    row.setId(position + j);

                    TextView tv1 = new TextView(context);
                    tv1.setText(object.getString("VRNAME"));
                    tv1.setWidth(devicewidth);
                    tv1.setBackgroundResource(R.drawable.list_border);
                    tv1.setGravity(Gravity.LEFT);

                    TextView tv2 = new TextView(context);
                    tv2.setText(new DecimalFormat("0.00").format(object.getDouble("TRGSEEDQTY")));
                    tv2.setWidth(devicewidth);
                    tv2.setBackgroundResource(R.drawable.list_border);
                    tv2.setGravity(Gravity.RIGHT);

                    TextView tv3 = new TextView(context);
                    tv3.setText(new DecimalFormat("0").format(object.getDouble("TRGGROW")));
                    tv3.setWidth(devicewidth);
                    tv3.setBackgroundResource(R.drawable.list_border);
                    tv3.setGravity(Gravity.RIGHT);

                    TextView tv4 = new TextView(context);
                    tv4.setText(new DecimalFormat("0.00").format(object.getDouble("ACHIVSEEDQTY")));
                    tv4.setWidth(lastWidth);
                    tv4.setBackgroundResource(R.drawable.list_border);
                    tv4.setGravity(Gravity.RIGHT);

                    TextView tv5= new TextView(context);
                    tv5.setText(new DecimalFormat("0").format(object.getDouble("ACHIVGROW")));
                    tv5.setWidth(lastWidth);
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

                    dialogueTable.addView(row);
                } catch (Exception e) {

                }
            }
            TableRow row = new TableRow(context);
            row.setId(position + 1);
            row.setBackgroundColor(color);

            TextView tv1 = new TextView(context);
            tv1.setText("TOTAL");
            tv1.setTextColor(Color.parseColor("#FFFFFF"));
            tv1.setWidth(devicewidth);
            tv1.setGravity(Gravity.LEFT);

            TextView tv2 = new TextView(context);
            tv2.setTextColor(Color.parseColor("#FFFFFF"));
            tv2.setText(new DecimalFormat("0.00").format(totalTargetSeed));
            tv2.setWidth(devicewidth);
            tv2.setGravity(Gravity.RIGHT);

            TextView tv3 = new TextView(context);
            tv3.setTextColor(Color.parseColor("#FFFFFF"));
            tv3.setText(new DecimalFormat("0").format(totalTargetGrower));
            tv3.setWidth(devicewidth);
            tv3.setGravity(Gravity.RIGHT);

            TextView tv4 = new TextView(context);
            tv4.setTextColor(Color.parseColor("#FFFFFF"));
            tv4.setText(new DecimalFormat("0.00").format(totalAchievedSeed));
            tv4.setWidth(lastWidth);
            tv4.setGravity(Gravity.RIGHT);

            TextView tv5 = new TextView(context);
            tv5.setTextColor(Color.parseColor("#FFFFFF"));
            tv5.setText(new DecimalFormat("0").format(totalAchievedGrower));
            tv5.setWidth(devicewidth);
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
