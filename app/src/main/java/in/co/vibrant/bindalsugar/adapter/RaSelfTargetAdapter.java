package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.StaffTargetModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class RaSelfTargetAdapter extends RecyclerView.Adapter<RaSelfTargetAdapter.MyHolder> {

    List<StaffTargetModal> arrayList;
    private Context context;

    public RaSelfTargetAdapter(Context context, List<StaffTargetModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_ra_tracker_dashboard, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //,,,,,,,,,
        //                ,,,,,,textper,textper_grower
        try {
            holder.tvCount.setText(arrayList.get(position).getTargetCode());
            holder.tv_name.setText(arrayList.get(position).getTargetName());
            holder.start_date.setText(context.getString(R.string.LBL_COL_START_DATE) + arrayList.get(position).getStartDate());
            holder.end_date.setText(context.getString(R.string.LBL_COL_END_DATE) + arrayList.get(position).getEndDate());
            holder.target_area.setText(context.getString(R.string.LBL_COL_TARGET_AREA) + arrayList.get(position).getArea());
            holder.target_grower.setText(context.getString(R.string.LBL_COL_TARGET_GROWER) + arrayList.get(position).getGrowerCount());
            holder.complete_area.setText(context.getString(R.string.LBL_COL_COMPLETE_AREA) + arrayList.get(position).getAreaComplete());
            holder.complete_grower.setText(context.getString(R.string.LBL_COL_COMPLETE_GROWER) + arrayList.get(position).getGrowerComplete());
            holder.balance_area.setText(context.getString(R.string.LBL_COL_BALANCE_AREA) + arrayList.get(position).getAreaBalance());
            holder.balance_grower.setText(context.getString(R.string.LBL_COL_BALANCE_GROWER) + arrayList.get(position).getGrowerBalance());
            holder.area_exp.setText(context.getString(R.string.LBL_COL_COMPLETE_AREA_EXP_DAYS) + arrayList.get(position).getAreaExp());
            holder.grower_exp.setText(context.getString(R.string.LBL_COL_COMPLETE_GROWER_EXP_DAYS) + arrayList.get(position).getGrowerExp());
            holder.area_delay.setText(context.getString(R.string.LBL_COL_AREA) + arrayList.get(position).getAreaDelay());
            holder.grower_delay.setText(context.getString(R.string.LBL_COL_GROWER) + arrayList.get(position).getGrowerDelay());
            //int delay=Integer.parseInt(arrayList.get(position).getDelay());
            double progress = (Double.parseDouble(arrayList.get(position).getAreaComplete()) * 100) / Double.parseDouble(arrayList.get(position).getArea());
            double progressGrower = (Double.parseDouble(arrayList.get(position).getGrowerComplete()) * 100) / Double.parseDouble(arrayList.get(position).getGrowerCount());
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            holder.textper.setText(decimalFormat.format(progress) + "% ");
            holder.progressBar.setMax(100);
            holder.progressBar.setProgress((int) progress);

            holder.textper_grower.setText(decimalFormat.format(progressGrower) + "% ");
            holder.progressBar_grower.setMax(100);
            holder.progressBar_grower.setProgress((int) progressGrower);

            holder.tv_village.setText(context.getString(R.string.LBL_VILLAGE) + " : " + arrayList.get(position).getVillageCount());
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#355E37"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_village.setTextColor(Color.parseColor("#FFFFFF"));

            holder.rLayoutLine4.setVisibility(View.GONE);
            holder.rLayoutLine5.setVisibility(View.GONE);

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView tvCount, tv_name, tv_village, start_date, end_date, target_area, target_grower, complete_area, complete_grower, balance_area, balance_grower, area_exp, grower_exp, area_delay, grower_delay, textper, textper_grower;
        RelativeLayout rlLayoutHeader;
        LinearLayout rLayoutLine4, rLayoutLine5;
        ProgressBar progressBar, progressBar_grower;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rLayoutLine4 = itemView.findViewById(R.id.rLayoutLine4);
            rLayoutLine5 = itemView.findViewById(R.id.rLayoutLine5);
            rlLayoutHeader = itemView.findViewById(R.id.rlLayoutHeader);
            tvCount = itemView.findViewById(R.id.tvCount);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_village = itemView.findViewById(R.id.tv_village);
            start_date = itemView.findViewById(R.id.start_date);
            end_date = itemView.findViewById(R.id.end_date);
            target_area = itemView.findViewById(R.id.target_area);
            target_grower = itemView.findViewById(R.id.target_grower);
            complete_area = itemView.findViewById(R.id.complete_area);
            complete_grower = itemView.findViewById(R.id.complete_grower);
            balance_area = itemView.findViewById(R.id.balance_area);
            balance_grower = itemView.findViewById(R.id.balance_grower);
            area_exp = itemView.findViewById(R.id.area_exp);
            grower_exp = itemView.findViewById(R.id.grower_exp);
            area_delay = itemView.findViewById(R.id.area_delay);
            grower_delay = itemView.findViewById(R.id.grower_delay);
            textper = itemView.findViewById(R.id.textper);
            textper_grower = itemView.findViewById(R.id.textper_grower);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar_grower = itemView.findViewById(R.id.progressBar_grower);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
