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
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import in.co.vibrant.bindalsugar.R;


public class ListGetVarietySummeryAdapter extends RecyclerView.Adapter<ListGetVarietySummeryAdapter.MyHolder> {

    private Context context;
    JSONArray jsonArray;


    public ListGetVarietySummeryAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_cane_type_summery,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try{
            DecimalFormat decimalFormat0 = new DecimalFormat("##");
            DecimalFormat decimalFormat2 = new DecimalFormat("##.00");
            DecimalFormat decimalFormat3 = new DecimalFormat("##.000");

            JSONObject supData= jsonArray.getJSONObject(position);
            JSONArray villDataArray=supData.getJSONArray("villData");
            holder.tv_sup_name.setText(supData.getString("SUPVCODE")+" / "+supData.getString("SUPVNAME"));
            //holder.tv_sup_name.setText(supData.getString("supcode")+" / "+supData.getString("supname"));
            double supTotalArea=0,supTotalPlot=0,supTotalGrower=0;
            for(int i=0;i<villDataArray.length();i++)
            {
                JSONObject villJson=villDataArray.getJSONObject(i);
                TextView village = new TextView(context);
                village.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                village.setText(villJson.getString("villcode")+" / "+villJson.getString("villname"));
                village.setGravity(Gravity.LEFT);
                village.setTextColor(Color.parseColor("#FF9800"));
                village.setTypeface(village.getTypeface(), Typeface.BOLD);
                holder.layout_booking.addView(village);

                TableLayout dialogueTable = new TableLayout(context);
                dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dialogueTable.setShrinkAllColumns(true);
                dialogueTable.setStretchAllColumns(true);
                TableRow tableHeading = new TableRow(context);
                TextView th1 = new TextView(context);
                TextView th2 = new TextView(context);
                TextView th3 = new TextView(context);
                TextView th4 = new TextView(context);
                tableHeading.setBackgroundColor(Color.parseColor("#000000"));
                th1.setTextColor(Color.parseColor("#FFFFFF"));
                th2.setTextColor(Color.parseColor("#FFFFFF"));
                th3.setTextColor(Color.parseColor("#FFFFFF"));
                th4.setTextColor(Color.parseColor("#FFFFFF"));
                th1.setText("Variety");
                th2.setText("Area");
                th3.setText("Plot");
                th4.setText("Grower");
                th1.setGravity(Gravity.LEFT);
                th2.setGravity(Gravity.RIGHT);
                th3.setGravity(Gravity.RIGHT);
                th4.setGravity(Gravity.RIGHT);
                /*if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tableHeading.setBackgroundResource(R.drawable.table_row_bg);
                } else {
                    tableHeading.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg));
                }*/
                //tableHeading.setBackgroundResource(drawable.table_row_bg);
                //tableHeading.addView(th1);
                tableHeading.addView(th1);
                tableHeading.addView(th2);
                tableHeading.addView(th3);
                tableHeading.addView(th4);
                dialogueTable.addView(tableHeading);
                double totalArea=0,totalPlot=0,totalGrower=0;
                JSONObject jsonvillDataObject=villJson.getJSONObject("villData");
                JSONArray villArray=jsonvillDataObject.getJSONArray("values");
                for (int j = 0; j < villArray.length(); j++) {
                    JSONObject jsonDataObject1=villArray.getJSONObject(j);
                    JSONObject jsonDataObject=jsonDataObject1.getJSONObject("nameValuePairs");
                    TableRow row = new TableRow(context);
                    row.setId(position+j);
                    TextView tv1 = new TextView(context);
                    tv1.setText(jsonDataObject.getString("ATNAME"));
                    tv1.setWidth(80);
                    tv1.setGravity(Gravity.LEFT);
                    TextView tv2 = new TextView(context);
                    tv2.setText(jsonDataObject.getString("AREA"));
                    //tv2.setWidth(50);
                    tv2.setGravity(Gravity.RIGHT);
                    TextView tv3 = new TextView(context);
                    tv3.setText(jsonDataObject.getString("PLOTS"));
                    //tv3.setWidth(50);
                    tv3.setGravity(Gravity.RIGHT);
                    TextView tv4 = new TextView(context);
                    tv4.setText(jsonDataObject.getString("GROWCOUNT"));
                    totalArea +=jsonDataObject.getDouble("AREA");
                    totalPlot +=jsonDataObject.getDouble("PLOTS");
                    totalGrower +=jsonDataObject.getDouble("GROWCOUNT");
                    //tv4.setWidth(50);
                    supTotalArea +=jsonDataObject.getDouble("AREA");
                    supTotalPlot +=jsonDataObject.getDouble("PLOTS");
                    supTotalGrower +=jsonDataObject.getDouble("GROWCOUNT");
                    tv4.setGravity(Gravity.RIGHT);
                    if(j%2==0)
                    {
                        row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                    }
                    else
                    {
                        row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                    }

                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    dialogueTable.addView(row);
                }
                TableRow footer = new TableRow(context);
                footer.setId(position);
                TextView tv1 = new TextView(context);
                tv1.setText("Total");
                tv1.setWidth(80);
                tv1.setGravity(Gravity.LEFT);
                TextView tv2 = new TextView(context);
                tv2.setText(decimalFormat3.format(totalArea));
                //tv2.setWidth(50);
                tv2.setGravity(Gravity.RIGHT);
                TextView tv3 = new TextView(context);
                tv3.setText(decimalFormat0.format(totalPlot));
                //tv3.setWidth(50);
                tv3.setGravity(Gravity.RIGHT);
                TextView tv4 = new TextView(context);
                tv4.setText(decimalFormat0.format(totalGrower));
                //tv4.setWidth(50);
                tv4.setGravity(Gravity.RIGHT);
                footer.setBackgroundColor(Color.parseColor("#000000"));
                tv1.setTextColor(Color.parseColor("#FFFFFF"));
                tv2.setTextColor(Color.parseColor("#FFFFFF"));
                tv3.setTextColor(Color.parseColor("#FFFFFF"));
                tv4.setTextColor(Color.parseColor("#FFFFFF"));
                footer.addView(tv1);
                footer.addView(tv2);
                footer.addView(tv3);
                footer.addView(tv4);
                dialogueTable.addView(footer);
                holder.layout_booking.addView(dialogueTable);
            }
            //holder.tv_sup_area.setText(decimalFormat3.format(supTotalArea));
            //holder.tv_sup_plot.setText(decimalFormat0.format(supTotalPlot));
            //holder.tv_sup_grower.setText(decimalFormat0.format(supTotalGrower));
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
