package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;


public class AdminTargetDetailsAdapter extends RecyclerView.Adapter<AdminTargetDetailsAdapter.MyHolder> {

    private Context context;
    List<StaffTargetModal> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public AdminTargetDetailsAdapter(Context context, List<StaffTargetModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_admin_target,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if(position==0)
        {
            holder.tv_delay.setText("Delay : 3");
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#FFAD2016"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_delay.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(position==1)
        {
            holder.tv_delay.setText("Early : 3");
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#355E37"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_delay.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(position==2)
        {
            holder.tv_delay.setText("Delay : 0");
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#CECBCA"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_delay.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TextView tv_delay,tv_name;
        RelativeLayout rlLayoutHeader;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rlLayoutHeader=itemView.findViewById(R.id.rlLayoutHeader);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_delay=itemView.findViewById(R.id.tv_delay);

        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
