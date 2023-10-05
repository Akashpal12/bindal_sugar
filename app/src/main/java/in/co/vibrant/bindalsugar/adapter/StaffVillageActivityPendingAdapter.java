package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.StaffVillageActivityPending;
import in.co.vibrant.bindalsugar.view.supervisor.StaffPendingActivityDetails;


public class StaffVillageActivityPendingAdapter extends RecyclerView.Adapter<StaffVillageActivityPendingAdapter.MyHolder> {

    private Context context;
    List<StaffVillageActivityPending> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public StaffVillageActivityPendingAdapter(Context context, List<StaffVillageActivityPending> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_staff_pending_village_activity,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if(position==0)
        {
            holder.sr_no.setText("Sr No");
            holder.village_code.setText(arrayList.get(position).getVillCode());
            holder.village_name.setText(arrayList.get(position).getVillName());
            holder.grower_count.setText(arrayList.get(position).getGrowerCount());
            holder.plot_count.setText(arrayList.get(position).getPlotCount());
            holder.edit.setVisibility(View.GONE);
        }
        else
        {
            holder.sr_no.setText(""+position);
            holder.village_code.setText(arrayList.get(position).getVillCode());
            holder.village_name.setText(arrayList.get(position).getVillName());
            holder.grower_count.setText(arrayList.get(position).getGrowerCount());
            holder.plot_count.setText(arrayList.get(position).getPlotCount());
        }

        holder.sr_no.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.village_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.village_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.grower_count.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.plot_count.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, StaffPendingActivityDetails.class);
                intent.putExtra("villCode",arrayList.get(position).getVillCode());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_text_1;
        ImageView edit;
        TextView sr_no,village_code,village_name,grower_count,plot_count;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            edit=itemView.findViewById(R.id.edit);
            sr_no=itemView.findViewById(R.id.sr_no);
            village_code=itemView.findViewById(R.id.village_code);
            village_name=itemView.findViewById(R.id.village_name);
            grower_count=itemView.findViewById(R.id.grower_count);
            plot_count=itemView.findViewById(R.id.plot_count);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
