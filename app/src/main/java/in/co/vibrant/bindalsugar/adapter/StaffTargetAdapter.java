package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;


public class StaffTargetAdapter extends RecyclerView.Adapter<StaffTargetAdapter.MyHolder> {

    private Context context;
    List<StaffTargetModal> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public StaffTargetAdapter(Context context, List<StaffTargetModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_staff_tracker,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        holder.tvCount.setText(arrayList.get(position).getTargetCode());
        holder.tv_name.setText(arrayList.get(position).getTargetName());
        holder.start_date.setText(context.getString(R.string.LBL_COL_START_DATE)+arrayList.get(position).getStartDate());
        holder.end_date.setText(context.getString(R.string.LBL_COL_END_DATE)+arrayList.get(position).getEndDate());
        holder.target.setText(context.getString(R.string.LBL_COL_TARGET)+arrayList.get(position).getTarget());
        holder.achievement.setText(context.getString(R.string.LBL_COL_ACHIEVEMENT)+arrayList.get(position).getAchievment());
        holder.total_days.setText(context.getString(R.string.LBL_COL_TOTAL_DAYS)+arrayList.get(position).getTotalDays());
        holder.days_passed.setText(context.getString(R.string.LBL_COL_DAYS_PASSED)+arrayList.get(position).getDaysPassed());
        holder.balance.setText(context.getString(R.string.LBL_COL_BALANCE)+arrayList.get(position).getBalance());
        holder.no_of_days_complete.setText(context.getString(R.string.LBL_COL_NO_OF_DAYS_TO_COMP)+arrayList.get(position).getNoOfDaysToComplete());
        int delay=Integer.parseInt(arrayList.get(position).getDelay());
        int progress=Integer.parseInt(arrayList.get(position).getAchievment());
        holder.textper.setText(progress+"%");
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(progress);
        if(delay>0)
        {
            holder.tv_delay.setText(context.getString(R.string.LBL_COL_DELAY)+arrayList.get(position).getDelay());
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#FFAD2016"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_delay.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(delay<1)
        {
            holder.tv_delay.setText(context.getString(R.string.LBL_COL_EARLY)+arrayList.get(position).getDelay());
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#355E37"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_delay.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(delay==0)
        {
            holder.tv_delay.setText(context.getString(R.string.LBL_COL_DELAY)+arrayList.get(position).getDelay());
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
        TextView tvCount,tv_name,tv_delay,start_date,end_date,target,achievement,total_days,days_passed,balance,no_of_days_complete,textper;
        RelativeLayout rlLayoutHeader;
        ProgressBar progressBar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rlLayoutHeader=itemView.findViewById(R.id.rlLayoutHeader);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_delay=itemView.findViewById(R.id.tv_delay);
            start_date=itemView.findViewById(R.id.start_date);
            end_date=itemView.findViewById(R.id.end_date);
            target=itemView.findViewById(R.id.target);
            achievement=itemView.findViewById(R.id.achievement);
            total_days=itemView.findViewById(R.id.total_days);
            days_passed=itemView.findViewById(R.id.days_passed);
            balance=itemView.findViewById(R.id.balance);
            no_of_days_complete=itemView.findViewById(R.id.no_of_days_complete);
            textper=itemView.findViewById(R.id.textper);
            progressBar=itemView.findViewById(R.id.progressBar);

        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
