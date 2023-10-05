package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotActivityActivityWiseDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class PlotActivityActivityWiseDetailAdapter extends RecyclerView.Adapter<PlotActivityActivityWiseDetailAdapter.MyHolder> {

    private Context context;
    List<PlotActivityActivityWiseDetailModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;


    public PlotActivityActivityWiseDetailAdapter(Context context, List<PlotActivityActivityWiseDetailModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.plot_activity_activity_wise_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //int lastNonBlankRowIndex = arrayList.size();

        try {
            if (position == 0) {
                holder.grower_Code_name.setText("   Grower Code / Name");
                holder.plot_No.setText("  Plot No");
                holder.plot_type_variety.setText("  Plot Type / Variety");
                holder.activity.setText("Activity");
                holder.method.setText("  Method");
                holder.item.setText(" Item");
                holder.area.setText(" Area");
                holder.date.setText(" Date");
                holder.SrNo.setText(" Sr No");


                holder.grower_Code_name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_No.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_type_variety.setTypeface(Typeface.DEFAULT_BOLD);
                holder.activity.setTypeface(Typeface.DEFAULT_BOLD);
                holder.method.setTypeface(Typeface.DEFAULT_BOLD);
                holder.item.setTypeface(Typeface.DEFAULT_BOLD);
                holder.area.setTypeface(Typeface.DEFAULT_BOLD);
                holder.date.setTypeface(Typeface.DEFAULT_BOLD);
                holder.SrNo.setTypeface(Typeface.DEFAULT_BOLD);



            } else if (position == arrayList.size() + 1) {

                // Display the total area in the last row
                double totalArea = 0.0;
                for (PlotActivityActivityWiseDetailModel model : arrayList) {
                    totalArea += Double.parseDouble(model.getAREA());
                }



                holder.grower_Code_name.setText("  Total");
                holder.plot_No.setText("   "+String.valueOf(arrayList.size()));
                holder.plot_type_variety.setText("");
                holder.activity.setText("");
                holder.SrNo.setText("");
                holder.method.setText("");
                holder.item.setText("");
                holder.date.setText("");
                holder.area.setText(new DecimalFormat("#0.000").format(totalArea));

                holder.area.setTypeface(Typeface.DEFAULT_BOLD);
                holder.grower_Code_name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_No.setTypeface(Typeface.DEFAULT_BOLD);
                // ... set other TextViews as needed ...
            } else {

                final int pos = position - 1;
                holder.grower_Code_name.setText("  "+arrayList.get(pos).getGCODE()+" / "+arrayList.get(pos).getGNAME());
                holder.plot_No.setText("  "+arrayList.get(pos).getPLNO());
                holder.plot_type_variety.setText("  "+arrayList.get(pos).getPLOTTYPE()+" / "+arrayList.get(pos).getVARIETY());
                holder.activity.setText("  "+arrayList.get(pos).getACTIVITY());
                holder.SrNo.setText(String.valueOf(pos+1));
                holder.method.setText(arrayList.get(pos).getMETHOD());
                holder.item.setText(arrayList.get(pos).getITEM());
                holder.area.setText(arrayList.get(pos).getAREA());
                holder.date.setText(arrayList.get(pos).getDATE());



            }
        } catch (Exception e) {

        }


    }



    @Override
    public int getItemCount() {
        return arrayList.size() + 2;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView SrNo,grower_Code_name,plot_No,plot_type_variety,activity,method,item,area,date;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            grower_Code_name = itemView.findViewById(R.id.grower_Code_name);
            plot_No = itemView.findViewById(R.id.plot_No);
            activity = itemView.findViewById(R.id.activity);
            method = itemView.findViewById(R.id.method);
            plot_type_variety = itemView.findViewById(R.id.plot_type_variety);
            item = itemView.findViewById(R.id.item);
            SrNo = itemView.findViewById(R.id.SrNo);
            area = itemView.findViewById(R.id.area);
            date = itemView.findViewById(R.id.date);


        }


    }

}



