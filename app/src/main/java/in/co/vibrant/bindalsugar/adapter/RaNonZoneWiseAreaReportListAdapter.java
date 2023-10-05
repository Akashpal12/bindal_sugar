package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.StaffTargetModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class RaNonZoneWiseAreaReportListAdapter extends RecyclerView.Adapter<RaNonZoneWiseAreaReportListAdapter.MyHolder> {

    private Context context;
    List <StaffTargetModel> arrayList;
    AlertDialog Alertdialog;


    public RaNonZoneWiseAreaReportListAdapter(Context context, List <StaffTargetModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_report_zone_wise_area_list,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {
            holder.txt1.setText(arrayList.get(position).getVillageCode());
            holder.txt2.setVisibility(View.GONE);
            holder.txt3.setText(arrayList.get(position).getOnManualArea());
            holder.txt4.setText(arrayList.get(position).getToManualArea());

            holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / 3;
            } else {
                devicewidth = device_width / 3;
            }
            int lastWidth = device_width - (devicewidth * 3);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }

            holder.txt1.getLayoutParams().width = devicewidth;
            holder.txt3.getLayoutParams().width = devicewidth;
            holder.txt4.getLayoutParams().width = lastWidth;
            //holder.rl_text.getLayoutParams().height = 100;
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
        RelativeLayout rl_text;
        TextView txt1,txt2,txt3,txt4;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text=itemView.findViewById(R.id.rl_text);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

