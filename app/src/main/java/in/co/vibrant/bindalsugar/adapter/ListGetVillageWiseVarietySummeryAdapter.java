package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import in.co.vibrant.bindalsugar.R;


public class ListGetVillageWiseVarietySummeryAdapter extends RecyclerView.Adapter<ListGetVillageWiseVarietySummeryAdapter.MyHolder> {

    private Context context;
    JSONArray jsonArray;
    double p1=0.00,p2=0.00,p3=0.00,a1=0.00,a2=0.00,a3=0.00,t1=0.00,t2=0.00;


    public ListGetVillageWiseVarietySummeryAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_village_wise_cane_type_summery,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try{
            Typeface typefaced = ResourcesCompat.getFont(context, R.font.roboto_black);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            DecimalFormat decimalFormat0 = new DecimalFormat("##");
            DecimalFormat decimalFormat2 = new DecimalFormat("##.00");
            DecimalFormat decimalFormat3 = new DecimalFormat("##.000");

            JSONObject supData= jsonArray.getJSONObject(position);
            JSONArray villDataArray=supData.getJSONArray("villData");
            holder.tv_sup_name.setText(supData.getString("SUPVCODE")+" / "+supData.getString("SUPVNAME"));
            //holder.tv_sup_name.setText(supData.getString("supcode")+" / "+supData.getString("supname"));
            //JSONObject villJson=villDataArray.getJSONObject(position);
            /*TextView village = new TextView(context);
            village.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            village.setText(villJson.getString("villcode")+" / "+villJson.getString("villname"));
            village.setGravity(Gravity.LEFT);
            village.setTextColor(Color.parseColor("#FF9800"));
            village.setTypeface(village.getTypeface(), Typeface.BOLD);
            holder.layout_booking.addView(village);*/

            TableLayout dialogueTable = new TableLayout(context);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialogueTable.setShrinkAllColumns(false);
            dialogueTable.setStretchAllColumns(false);
            TableRow tableHeading = new TableRow(context);

            TextView th1 = new TextView(context);
            TextView th2 = new TextView(context);
            TextView th3 = new TextView(context);
            TextView th4 = new TextView(context);

            TextView th5 = new TextView(context);

            th1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param1 = (TableRow.LayoutParams) th1.getLayoutParams();
            the_param1.span = 2;
            th1.setLayoutParams(the_param1);

            th2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param2 = (TableRow.LayoutParams) th2.getLayoutParams();
            the_param2.span = 2;
            th2.setLayoutParams(the_param2);

            th3.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param3 = (TableRow.LayoutParams) th3.getLayoutParams();
            the_param3.span = 2;
            th3.setLayoutParams(the_param3);

            th4.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param4 = (TableRow.LayoutParams) th4.getLayoutParams();
            the_param4.span = 2;
            th4.setLayoutParams(the_param4);

            th5.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param5 = (TableRow.LayoutParams) th5.getLayoutParams();
            the_param5.span = 2;
            th5.setLayoutParams(the_param5);

            //tableHeading.setBackgroundColor(Color.parseColor("#000000"));
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                th1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                th2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                th3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                th4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                th5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
            } else {
                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                th1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
            }
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th3.setTextColor(Color.parseColor("#FFFFFF"));
            th4.setTextColor(Color.parseColor("#FFFFFF"));
            th5.setTextColor(Color.parseColor("#FFFFFF"));
            th1.setText("Variety");
            th2.setText("Plant");
            th3.setText("Autumn");
            th4.setText("Ratoon");
            th5.setText("Total");


            Typeface typeface1 = ResourcesCompat.getFont(context, R.font.roboto_black);
            th1.setTypeface(typeface1);

            Typeface typeface2 = ResourcesCompat.getFont(context, R.font.roboto_black);
            th2.setTypeface(typeface2);

            Typeface typeface3 = ResourcesCompat.getFont(context, R.font.roboto_black);
            th3.setTypeface(typeface3);

            Typeface typeface4 = ResourcesCompat.getFont(context, R.font.roboto_black);
            th4.setTypeface(typeface4);

            Typeface typeface5 = ResourcesCompat.getFont(context, R.font.roboto_black);
            th5.setTypeface(typeface5);

            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            th3.setGravity(Gravity.CENTER);
            th4.setGravity(Gravity.CENTER);
            th5.setGravity(Gravity.CENTER);
            tableHeading.addView(th1);
            tableHeading.addView(th2);
            tableHeading.addView(th3);
            tableHeading.addView(th4);
            tableHeading.addView(th5);
            dialogueTable.addView(tableHeading);


            TableRow tableHeadingl1 = new TableRow(context);
            TextView thl1 = new TextView(context);
            TextView thl2 = new TextView(context);
            TextView thl3 = new TextView(context);
            TextView thl4 = new TextView(context);
            TextView thl5 = new TextView(context);
            TextView thl6 = new TextView(context);
            TextView thl7 = new TextView(context);
            TextView thl8 = new TextView(context);
            TextView thl9 = new TextView(context);
            TextView thl10 = new TextView(context);

            //tableHeadingl1.setBackgroundColor(Color.parseColor("#000000"));
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                thl1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl6.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl7.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl8.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl9.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl10.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );

            } else {
                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                thl1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl9.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl10.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));

            }

            thl1.setTextColor(Color.parseColor("#FFFFFF"));
            thl2.setTextColor(Color.parseColor("#FFFFFF"));
            thl3.setTextColor(Color.parseColor("#FFFFFF"));
            thl4.setTextColor(Color.parseColor("#FFFFFF"));
            thl5.setTextColor(Color.parseColor("#FFFFFF"));
            thl6.setTextColor(Color.parseColor("#FFFFFF"));
            thl7.setTextColor(Color.parseColor("#FFFFFF"));
            thl8.setTextColor(Color.parseColor("#FFFFFF"));
            thl9.setTextColor(Color.parseColor("#FFFFFF"));
            thl10.setTextColor(Color.parseColor("#FFFFFF"));


            thl1.setText("Code");
            thl1.setTextColor(Color.parseColor("#FFFFFF"));
            thl1.setGravity(Gravity.LEFT);
            Typeface typeface41 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl1.setTypeface(typeface41);


            thl2.setText("Name");
            Typeface typeface12 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl2.setTypeface(typeface12);
            thl3.setText("No Of Plot");
            Typeface typefacue3 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl3.setTypeface(typefacue3);
            thl4.setText("Area");
            thl5.setText("No Of Plot");
            thl6.setText("Area");
            thl7.setText("No Of Plot");
            thl8.setText("Area");
            thl9.setText("No Of Plot");
            thl10.setText("Area");

            Typeface typefagce4 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl4.setTypeface(typefagce4);

            Typeface typefavce5= ResourcesCompat.getFont(context, R.font.roboto_black);
            thl5.setTypeface(typefavce5);

            Typeface typeface6 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl6.setTypeface(typeface6);

            Typeface typeface7 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl7.setTypeface(typeface7);

            Typeface typeface8 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl8.setTypeface(typeface8);

            Typeface typeface9 = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl9.setTypeface(typeface9);


            thl1.setGravity(Gravity.CENTER);
            thl2.setGravity(Gravity.CENTER);
            thl3.setGravity(Gravity.CENTER);
            thl4.setGravity(Gravity.CENTER);
            thl5.setGravity(Gravity.CENTER);
            thl6.setGravity(Gravity.CENTER);
            thl7.setGravity(Gravity.CENTER);
            thl8.setGravity(Gravity.CENTER);
            thl9.setGravity(Gravity.CENTER);
            thl10.setGravity(Gravity.CENTER);

            tableHeadingl1.addView(thl1);
            tableHeadingl1.addView(thl2);
            tableHeadingl1.addView(thl3);
            tableHeadingl1.addView(thl4);
            tableHeadingl1.addView(thl5);
            tableHeadingl1.addView(thl6);
            tableHeadingl1.addView(thl7);
            tableHeadingl1.addView(thl8);
            tableHeadingl1.addView(thl9);
            tableHeadingl1.addView(thl10);

            Typeface typefagce = ResourcesCompat.getFont(context, R.font.roboto_black);
            thl10.setTypeface(typefagce);

            dialogueTable.addView(tableHeadingl1);
            double totalPlantNoOfPlant=0,totalPlantArea=0,totalAutumnNoOfPlant=0,totalAutumnArea=0,totalRatoonNoOfPlant=0,
                    totalRatoonArea=0,TotalPlant=0,TotalArea=0;
            for (int j = 0; j < villDataArray.length(); j++) {
                JSONObject jsonDataObject=villDataArray.getJSONObject(j);
                TableRow row = new TableRow(context);

                row.setId(j);

                TextView tv1 = new TextView(context);
                tv1.setText(jsonDataObject.getString("VCODE"));
                tv1.setWidth(250);
                tv1.setTypeface(typefaced);
                tv1.setGravity(Gravity.LEFT);
                tv1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));


                TextView tv2 = new TextView(context);
                tv2.setText(jsonDataObject.getString("VNAME"));
                tv2.setWidth(250);
                tv2.setTypeface(typefaced);
                tv2.setGravity(Gravity.LEFT);
                tv2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv3 = new TextView(context);
                tv3.setText(jsonDataObject.getString("PLANTPLOTS"));
                tv3.setWidth(250);
                tv3.setTypeface(typefaced);
                tv3.setGravity(Gravity.RIGHT);
                tv3.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv4 = new TextView(context);
                tv4.setText(jsonDataObject.getString("PLANTAREA"));
                tv4.setWidth(250);
                tv4.setTypeface(typefaced);
                tv4.setGravity(Gravity.RIGHT);
                tv4.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv5 = new TextView(context);
                tv5.setText(jsonDataObject.getString("AUTUMNPLOTS"));
                tv5.setWidth(250);
                tv5.setTypeface(typefaced);
                tv5.setGravity(Gravity.RIGHT);
                tv5.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv6= new TextView(context);
                tv6.setText(jsonDataObject.getString("AUTUMNAREA"));
                tv6.setWidth(250);
                tv6.setTypeface(typefaced);
                tv6.setGravity(Gravity.RIGHT);
                tv6.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv7= new TextView(context);
                tv7.setText(jsonDataObject.getString("RATOONPLOTS"));
                tv7.setWidth(250);
                tv7.setTypeface(typefaced);
                tv7.setGravity(Gravity.RIGHT);
                tv7.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv8= new TextView(context);
                tv8.setText(jsonDataObject.getString("RATOONAREA"));
                tv8.setWidth(250);
                tv8.setTypeface(typefaced);
                tv8.setGravity(Gravity.RIGHT);
                tv8.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv9= new TextView(context);
                tv9.setText(jsonDataObject.getString("TotalPLOTS"));
                tv9.setWidth(250);
                tv9.setTypeface(typefaced);
                tv9.setGravity(Gravity.RIGHT);
                tv9.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv10= new TextView(context);
                tv10.setText(jsonDataObject.getString("TotalArea"));
                tv10.setWidth(250);
                tv10.setTypeface(typefaced);
                tv10.setGravity(Gravity.RIGHT);
                tv10.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                totalPlantNoOfPlant +=jsonDataObject.getDouble("PLANTPLOTS");
                totalPlantArea +=jsonDataObject.getDouble("PLANTAREA");
                totalAutumnNoOfPlant +=jsonDataObject.getDouble("AUTUMNPLOTS");
                totalAutumnArea +=jsonDataObject.getDouble("AUTUMNAREA");
                totalRatoonNoOfPlant +=jsonDataObject.getDouble("RATOONPLOTS");
                totalRatoonArea +=jsonDataObject.getDouble("RATOONAREA");
                TotalPlant +=jsonDataObject.getDouble("TotalPLOTS");
                TotalArea +=jsonDataObject.getDouble("TotalArea");
                p1 +=jsonDataObject.getDouble("PLANTPLOTS");
                a1 +=jsonDataObject.getDouble("PLANTAREA");
                p2 +=jsonDataObject.getDouble("AUTUMNPLOTS");
                a2 +=jsonDataObject.getDouble("AUTUMNAREA");
                p3 +=jsonDataObject.getDouble("RATOONPLOTS");
                a3 +=jsonDataObject.getDouble("RATOONAREA");
                t1 +=jsonDataObject.getDouble("TotalPLOTS");
                t2 +=jsonDataObject.getDouble("TotalArea");

                if(j%2==0)
                {
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                        tv1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv6.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv7.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv8.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv9.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv10.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                    } else {
                        //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv9.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv10.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                    }
                    //row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                }
                else
                {
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg) );
                        tv1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv6.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv7.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv8.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv9.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                        tv10.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text) );
                    } else {
                        //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg));
                        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv9.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv10.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    }
                    //row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                row.addView(tv5);
                row.addView(tv6);
                row.addView(tv7);
                row.addView(tv8);
                row.addView(tv9);
                row.addView(tv10);
                dialogueTable.addView(row);



            }
            TableRow footer = new TableRow(context);
            footer.setId(position);
            TextView tv1 = new TextView(context);
            tv1.setText("Surveyer Total");
            Typeface typefaceee = ResourcesCompat.getFont(context, R.font.roboto_black);
            tv1.setTypeface(typefaceee);



            tv1.setWidth(80);
            tv1.setGravity(Gravity.LEFT);

            tv1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams th1_the_param1 = (TableRow.LayoutParams) tv1.getLayoutParams();
            th1_the_param1.span = 2;
            tv1.setLayoutParams(th1_the_param1);

            TextView tv2 = new TextView(context);
            tv2.setText(decimalFormat0.format(totalPlantNoOfPlant));
            tv2.setWidth(50);
            tv2.setGravity(Gravity.RIGHT);
            TextView tv3 = new TextView(context);
            tv3.setText(decimalFormat3.format(totalPlantArea));
            tv3.setWidth(50);
            tv3.setGravity(Gravity.RIGHT);

            TextView tv4 = new TextView(context);
            tv4.setText(decimalFormat0.format(totalAutumnNoOfPlant));
            tv4.setWidth(50);
            tv4.setGravity(Gravity.RIGHT);
            TextView tv5 = new TextView(context);
            tv5.setText(decimalFormat3.format(totalAutumnArea));
            tv5.setWidth(50);
            tv5.setGravity(Gravity.RIGHT);

            TextView tv6 = new TextView(context);
            tv6.setText(decimalFormat0.format(totalRatoonNoOfPlant));
            tv6.setWidth(50);
            tv6.setGravity(Gravity.RIGHT);
            TextView tv7 = new TextView(context);
            tv7.setText(decimalFormat3.format(totalRatoonArea));
            tv7.setWidth(50);
            tv7.setGravity(Gravity.RIGHT);

            TextView tv8 = new TextView(context);
            tv8.setText(decimalFormat0.format(TotalPlant));
            tv8.setWidth(50);
            tv8.setGravity(Gravity.RIGHT);
            TextView tv9 = new TextView(context);
            tv9.setText(decimalFormat3.format(TotalArea));
            tv9.setWidth(50);
            tv9.setGravity(Gravity.RIGHT);

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                tv1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv6.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv7.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv8.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                tv9.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
            } else {
                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                tv9.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
            }
            //footer.setBackgroundColor(Color.parseColor("#000000"));
            tv1.setTextColor(Color.parseColor("#FFFFFF"));
            tv2.setTextColor(Color.parseColor("#FFFFFF"));
            tv3.setTextColor(Color.parseColor("#FFFFFF"));
            tv4.setTextColor(Color.parseColor("#FFFFFF"));
            tv5.setTextColor(Color.parseColor("#FFFFFF"));
            tv6.setTextColor(Color.parseColor("#FFFFFF"));
            tv7.setTextColor(Color.parseColor("#FFFFFF"));
            tv8.setTextColor(Color.parseColor("#FFFFFF"));
            tv9.setTextColor(Color.parseColor("#FFFFFF"));
            footer.addView(tv1);
            footer.addView(tv2);
            footer.addView(tv3);
            footer.addView(tv4);
            footer.addView(tv5);
            footer.addView(tv6);
            footer.addView(tv7);
            footer.addView(tv8);
            footer.addView(tv9);
            dialogueTable.addView(footer);
            holder.layout_booking.addView(dialogueTable);



            if(jsonArray.length()-1==position)
            {
                TableRow fffooter = new TableRow(context);
                fffooter.setId(position);
                TextView ftv1 = new TextView(context);
                ftv1.setText("Grand Total");


                ftv1.setWidth(80);
                ftv1.setGravity(Gravity.LEFT);

                ftv1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams fth1_the_param1 = (TableRow.LayoutParams) ftv1.getLayoutParams();
                fth1_the_param1.span = 2;
                ftv1.setLayoutParams(fth1_the_param1);

                TextView ftv2 = new TextView(context);
                ftv2.setText(decimalFormat0.format(p1));
                ftv2.setWidth(50);
                ftv2.setGravity(Gravity.RIGHT);
                TextView ftv3 = new TextView(context);
                ftv3.setText(decimalFormat3.format(a1));
                ftv3.setWidth(50);
                ftv3.setGravity(Gravity.RIGHT);

                TextView ftv4 = new TextView(context);
                ftv4.setText(decimalFormat0.format(p2));
                ftv4.setWidth(50);
                ftv4.setGravity(Gravity.RIGHT);
                TextView ftv5 = new TextView(context);
                ftv5.setText(decimalFormat3.format(a2));
                ftv5.setWidth(50);
                ftv5.setGravity(Gravity.RIGHT);

                TextView ftv6 = new TextView(context);
                ftv6.setText(decimalFormat0.format(p3));
                ftv6.setWidth(50);
                ftv6.setGravity(Gravity.RIGHT);
                TextView ftv7 = new TextView(context);
                ftv7.setText(decimalFormat3.format(a3));
                ftv7.setWidth(50);
                ftv7.setGravity(Gravity.RIGHT);

                TextView ftv8 = new TextView(context);
                ftv8.setText(decimalFormat0.format(t1));
                ftv8.setWidth(50);
                ftv8.setGravity(Gravity.RIGHT);
                TextView ftv9 = new TextView(context);
                ftv9.setText(decimalFormat3.format(t2));
                ftv9.setWidth(50);
                ftv9.setGravity(Gravity.RIGHT);
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                    ftv1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv5.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv6.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv7.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv8.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                    ftv9.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text) );
                } else {
                    //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                    ftv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                    ftv9.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_green_bg_black_text));
                }
                //footer.setBackgroundColor(Color.parseColor("#000000"));
                ftv1.setTextColor(Color.parseColor("#FFFFFF"));
                ftv2.setTextColor(Color.parseColor("#FFFFFF"));
                ftv3.setTextColor(Color.parseColor("#FFFFFF"));
                ftv4.setTextColor(Color.parseColor("#FFFFFF"));
                ftv5.setTextColor(Color.parseColor("#FFFFFF"));
                ftv6.setTextColor(Color.parseColor("#FFFFFF"));
                ftv7.setTextColor(Color.parseColor("#FFFFFF"));
                ftv8.setTextColor(Color.parseColor("#FFFFFF"));
                ftv9.setTextColor(Color.parseColor("#FFFFFF"));
                fffooter.addView(ftv1);
                fffooter.addView(ftv2);
                fffooter.addView(ftv3);
                fffooter.addView(ftv4);
                fffooter.addView(ftv5);
                fffooter.addView(ftv6);
                fffooter.addView(ftv7);
                fffooter.addView(ftv8);
                fffooter.addView(ftv9);
                dialogueTable.addView(fffooter);
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        //isDialogue=true;*/

        /*holder.tv_sup_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_sup_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_vill_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_vill_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_at_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_at_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_plots.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_grower.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));*/
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

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout_booking;
        RelativeLayout rlLayout,rl_text_1;
        TextView tv_sup_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_sup_name=itemView.findViewById(R.id.tv_sup_name);
            //tv_sup_area=itemView.findViewById(R.id.tv_sup_area);
            //tv_sup_plot=itemView.findViewById(R.id.tv_sup_plot);
            //tv_sup_grower=itemView.findViewById(R.id.tv_sup_grower);
            layout_booking=itemView.findViewById(R.id.layout_booking);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
