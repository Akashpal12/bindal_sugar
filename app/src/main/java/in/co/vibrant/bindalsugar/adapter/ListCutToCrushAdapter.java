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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;


public class ListCutToCrushAdapter extends RecyclerView.Adapter<ListCutToCrushAdapter.MyHolder> {

    private Context context;
    List <GrowerFinderModel> arrayList;


    public ListCutToCrushAdapter(Context context, List <GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_cut_to_crush_report,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        Typeface typefaced = ResourcesCompat.getFont(context, R.font.roboto_black);
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
       // holder.tv_ctc.setText("CTC DATE : " +arrayList.get(position).getTOTCTC()+    "Area :  "+arrayList.get(position).getCaneArea());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName());
        holder.tv_area.setText(arrayList.get(position).getCaneArea());
        holder.tv_ctc.setText("CTC DATE : " +arrayList.get(position).getTOTCTC());
        holder.tv_area.setText("Area : "+arrayList.get(position).getCaneArea());
        JSONArray jsonArray = arrayList.get(position).getJsonData();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int device_width = displaymetrics.widthPixels-50;
        int totalColumn = 6;
        int displayColumn = 5;
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
        if(jsonArray.length()>0)
        {

            //JSONObject jsonObject=arrayList.getJSONObject(position);




            TableLayout dialogueTable = new TableLayout(context);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialogueTable.setShrinkAllColumns(false);
            dialogueTable.setStretchAllColumns(false);
            TableRow tableHeading = new TableRow(context);

            tableHeading.setBackgroundColor(Color.parseColor("#000000"));

            TextView th1 = new TextView(context);
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th1.setText("Supplier");
            th1.setTypeface(typefaced);
            th1.setGravity(Gravity.LEFT);
            th1.setWidth(200);
            tableHeading.addView(th1);

            TextView th2 = new TextView(context);
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setText("Vill");
            th2.setGravity(Gravity.LEFT);
            th2.setTypeface(typefaced);
            th2.setWidth(200);
            tableHeading.addView(th2);

            TextView th3 = new TextView(context);
            th3.setTextColor(Color.parseColor("#FFFFFF"));
            th3.setText("Grow");
            th3.setGravity(Gravity.LEFT);
            th3.setTypeface(typefaced);
            th3.setWidth(200);
            tableHeading.addView(th3);

            TextView th4 = new TextView(context);
            th4.setTextColor(Color.parseColor("#FFFFFF"));
            th4.setText("Mode");
            th4.setTypeface(typefaced);
            th4.setGravity(Gravity.LEFT);
            th4.setWidth(250);
            tableHeading.addView(th4);

            TextView th5 = new TextView(context);
            th5.setTextColor(Color.parseColor("#FFFFFF"));
            th5.setText("Purchy");
            th5.setTypeface(typefaced);
            th5.setGravity(Gravity.LEFT);
            th5.setWidth(300);
            tableHeading.addView(th5);

            TextView th6 = new TextView(context);
            th6.setTextColor(Color.parseColor("#FFFFFF"));
            th6.setText("Vehicle");
            th6.setTypeface(typefaced);
            th6.setGravity(Gravity.LEFT);
            th6.setWidth(300);
            tableHeading.addView(th6);

            dialogueTable.addView(tableHeading);

            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(j);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    TableRow row = new TableRow(context);
                    row.setId(position + j);

                    TextView tv1 = new TextView(context);
                    tv1.setText(object.getString("SUPPLYPUR"));
                    tv1.setWidth(200);
                    tv1.setTypeface(typefaced);
                    tv1.setGravity(Gravity.LEFT);

                    TextView tv2 = new TextView(context);
                    tv2.setText(object.getString("Vill"));
                    tv2.setWidth(200);
                    tv2.setTypeface(typefaced);
                    tv2.setGravity(Gravity.LEFT);

                    TextView tv3 = new TextView(context);
                    tv3.setText(object.getString("Grow"));
                    tv3.setWidth(200);
                    tv3.setTypeface(typefaced);
                    tv3.setGravity(Gravity.LEFT);

                    TextView tv4 = new TextView(context);
                    tv4.setText(object.getString("MODE"));
                    tv4.setWidth(250);
                    tv4.setTypeface(typefaced);
                    tv4.setGravity(Gravity.LEFT);

                    TextView tv5 = new TextView(context);
                    tv5.setText(object.getString("PURCHYNO"));
                    tv5.setWidth(300);
                    tv5.setTypeface(typefaced);
                    tv5.setGravity(Gravity.LEFT);

                    TextView tv6 = new TextView(context);
                    tv6.setText(object.getString("VECHILE"));
                    tv6.setWidth(300);
                    tv6.setTypeface(typefaced);
                    tv6.setGravity(Gravity.LEFT);

                    if (j % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                        tv5.setTextColor(Color.parseColor("#000000"));
                        tv6.setTextColor(Color.parseColor("#000000"));
                    } else {
                        row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv4.setTextColor(Color.parseColor("#000000"));
                        tv5.setTextColor(Color.parseColor("#000000"));
                        tv6.setTextColor(Color.parseColor("#000000"));
                    }
                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    row.addView(tv5);
                    row.addView(tv6);

                    dialogueTable.addView(row);


                } catch (Exception e) {

                }
            }
            holder.layout_booking.removeAllViews();
            holder.layout_booking.addView(dialogueTable);
        }
        else{
            TextView th6 = new TextView(context);
            th6.setTextColor(Color.parseColor("#000000"));
            th6.setText("Without Purchy CTC");
            th6.setTypeface(typefaced);
            th6.setGravity(Gravity.CENTER);
            //th6.setWidth(device_width);
            th6.setLayoutParams(new ViewGroup.LayoutParams(ActionMenuView.LayoutParams.MATCH_PARENT, ActionMenuView.LayoutParams.WRAP_CONTENT));
            holder.layout_booking.removeAllViews();
            holder.layout_booking.addView(th6);
        }

       /* holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ListCutToCrushAdapter.this.context, CutToCrush.class);
                intent.putExtra("V_CODE", ListCutToCrushAdapter.this.arrayList.get(position).getPlotVillageCode());
                intent.putExtra("V_NAME", ListCutToCrushAdapter.this.arrayList.get(position).getPlotVillageName());
                intent.putExtra("G_CODE", ListCutToCrushAdapter.this.arrayList.get(position).getGrowerCode());
                intent.putExtra("PLOT_SR_NO", ListCutToCrushAdapter.this.arrayList.get(position).getPlotNo());
                intent.putExtra("PLOT_VILL", ListCutToCrushAdapter.this.arrayList.get(position).getPlotVillageCode());
                intent.putExtra("G_NAME", ListCutToCrushAdapter.this.arrayList.get(position).getGrowerName());
                ((Activity) context ).finish();
                ListCutToCrushAdapter.this.context.startActivity(intent);
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        LinearLayout layout_booking;
        TextView tvCount,tv_ctc,tv_grower_name,tv_area;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_ctc=itemView.findViewById(R.id.tv_ctc);
            tv_area=itemView.findViewById(R.id.tv_area);
            layout_booking=itemView.findViewById(R.id.layout_booking);

            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
