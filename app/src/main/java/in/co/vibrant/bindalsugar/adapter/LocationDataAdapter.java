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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.LocationDataModel;


public class LocationDataAdapter extends RecyclerView.Adapter<LocationDataAdapter.MyHolder> {

    private Context context;
    List<LocationDataModel> arrayList;


    public LocationDataAdapter(Context context, List<LocationDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_location_data,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            if (position == 0) {
                holder.txt1.setText("Srno");
                holder.txt2.setText("Lat");
                holder.txt3.setText("Lng");
                holder.txt4.setText("Dist");
                holder.txt5.setText("Acc");
                holder.txt1.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt2.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt3.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt4.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt5.setTextColor(Color.parseColor("#FFFFFF"));
                holder.rl_text_1.setBackgroundColor(Color.parseColor("#FFB10540"));
            } else {
                holder.txt1.setText(""+arrayList.get(position).getSerialNumber());
                holder.txt2.setText(""+arrayList.get(position).getLat());
                holder.txt3.setText(""+arrayList.get(position).getLng());
                holder.txt4.setText(""+arrayList.get(position).getDistance());
                holder.txt5.setText(""+arrayList.get(position).getAccuracy());
                holder.txt1.setTextColor(Color.parseColor("#000000"));
                holder.txt2.setTextColor(Color.parseColor("#000000"));
                holder.txt3.setTextColor(Color.parseColor("#000000"));
                holder.txt4.setTextColor(Color.parseColor("#000000"));
                holder.txt5.setTextColor(Color.parseColor("#000000"));
                holder.rl_text_1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int totalColumn = 9;
            int displayColumn = 9;
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
            holder.txt1.getLayoutParams().width = devicewidth;
            holder.txt2.getLayoutParams().width = devicewidth*3;
            holder.txt3.getLayoutParams().width = devicewidth*3;
            holder.txt4.getLayoutParams().width = devicewidth;
            holder.txt5.getLayoutParams().width = devicewidth;
            holder.rl_text_1.getLayoutParams().height = 100;
        }
        catch (Exception e)
        {
            //new GenerateLogFile(context).writeToFile("ServerPendingShareListAdapter.java"+e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        TextView txt1,txt2,txt3,txt4,txt5;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            txt5=itemView.findViewById(R.id.txt5);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }



}
