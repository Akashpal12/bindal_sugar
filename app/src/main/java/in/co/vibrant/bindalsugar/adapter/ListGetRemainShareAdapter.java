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


public class ListGetRemainShareAdapter extends RecyclerView.Adapter<ListGetRemainShareAdapter.MyHolder> {

    private Context context;
    JSONArray jsonArray;
    double p1=0.00,p2=0.00,p3=0.00,a1=0.00,a2=0.00,a3=0.00,t1=0.00,t2=0.00;


    public ListGetRemainShareAdapter(Context context, JSONArray jsonArray) {
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

            th1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param1 = (TableRow.LayoutParams) th1.getLayoutParams();
            the_param1.span = 2;

            th1.setTypeface(typefaced);
            th1.setLayoutParams(the_param1);

            th2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableRow.LayoutParams the_param2 = (TableRow.LayoutParams) th2.getLayoutParams();
            the_param2.span = 2;
            th2.setTypeface(typefaced);
            th2.setLayoutParams(the_param2);




            //tableHeading.setBackgroundColor(Color.parseColor("#000000"));
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                th1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                th2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
            } else {
                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                th1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
            }
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th1.setText("Village");
            th2.setText("");
            th2.setTypeface(typefaced);
            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            tableHeading.addView(th1);
            tableHeading.addView(th2);
            dialogueTable.addView(tableHeading);


            TableRow tableHeadingl1 = new TableRow(context);
            TextView thl1 = new TextView(context);
            thl1.setTypeface(typefaced);
            TextView thl2 = new TextView(context);
            thl2.setTypeface(typefaced);
            TextView thl3 = new TextView(context);
            thl3.setTypeface(typefaced);
            TextView thl4 = new TextView(context);
            thl4.setTypeface(typefaced);

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                thl1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
                thl4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text) );
            } else {
                //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                thl1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                thl4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
             }

            thl1.setTextColor(Color.parseColor("#FFFFFF"));
            thl2.setTextColor(Color.parseColor("#FFFFFF"));
            thl3.setTextColor(Color.parseColor("#FFFFFF"));
            thl4.setTextColor(Color.parseColor("#FFFFFF"));

            thl1.setText("Code");
            thl2.setText("Name");
            thl3.setText("Plot No");
            thl4.setText("Share (%)");

            thl1.setGravity(Gravity.CENTER);
            thl2.setGravity(Gravity.CENTER);
            thl3.setGravity(Gravity.CENTER);
            thl4.setGravity(Gravity.CENTER);

            tableHeadingl1.addView(thl1);
            tableHeadingl1.addView(thl2);
            tableHeadingl1.addView(thl3);
            tableHeadingl1.addView(thl4);

            dialogueTable.addView(tableHeadingl1);

            for (int j = 0; j < villDataArray.length(); j++) {
                JSONObject jsonDataObject=villDataArray.getJSONObject(j);
                TableRow row = new TableRow(context);
                row.setId(j);

                TextView tv1 = new TextView(context);
                tv1.setText(jsonDataObject.getString("VCODE"));
                tv1.setTypeface(typefaced);
                tv1.setWidth(250);

                tv1.setGravity(Gravity.LEFT);
                tv1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));


                TextView tv2 = new TextView(context);
                tv2.setText(jsonDataObject.getString("VNAME"));
                tv2.setWidth(250);
                tv2.setGravity(Gravity.LEFT);
                tv2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv3 = new TextView(context);
                tv3.setText(jsonDataObject.getString("PLOTNO"));
                tv3.setWidth(250);
                tv3.setGravity(Gravity.RIGHT);
                tv3.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                TextView tv4 = new TextView(context);
                tv4.setText(jsonDataObject.getString("REMAINPERCENT"));
                tv4.setWidth(250);
                tv4.setGravity(Gravity.RIGHT);
                tv4.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                if(j%2==0)
                {

                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        //row.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray) );
                        tv1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                        tv4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text) );
                    } else {
                        //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg_gray));
                        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
                        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_gray_bg_black_text));
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
                    } else {
                        //row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg));
                        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    }
                    //row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                dialogueTable.addView(row);
            }
            holder.layout_booking.addView(dialogueTable);
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
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
        alertDialog.setCancelable(false);
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
            layout_booking=itemView.findViewById(R.id.layout_booking);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
