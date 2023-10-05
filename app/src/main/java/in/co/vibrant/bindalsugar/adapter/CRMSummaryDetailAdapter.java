package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.CrmSummaryDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class CRMSummaryDetailAdapter extends RecyclerView.Adapter<CRMSummaryDetailAdapter.MyHolder> {

    private Context context;
    List<CrmSummaryDetailModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;


    public CRMSummaryDetailAdapter(Context context, List<CrmSummaryDetailModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.crm_summary_report_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //int lastNonBlankRowIndex = arrayList.size();

        try {
            if (position == 0) {
                try {
                    holder.activity_Name.setText("  Activity Name");
                    holder.supervisor.setText("  Supervisor");
                    holder.village.setText("Village");
                    holder.grower.setText("Grower");
                    holder.plot_Village.setText("  Plot Village");
                    holder.plot_No.setText("Plot No");
                    holder.date_Time.setText(" Date Time");
                    holder.cdo_Approval.setText(" CDO Approval");
                    holder.crm_Approval.setText(" CRM Approval");

                    holder.activity_Name.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.supervisor.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.village.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.grower.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.plot_Village.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.plot_No.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.date_Time.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);

                } catch (Exception e) {

                }


            } else {
                try {
                    final int pos = position - 1;
                    holder.activity_Name.setText("  " + arrayList.get(pos).getACT_NAME());
                    holder.supervisor.setText("  " + arrayList.get(pos).getU_CODE() + " / " + arrayList.get(pos).getU_NAME());
                    holder.village.setText("  " + arrayList.get(pos).getGV_CODE() + " / " + arrayList.get(pos).getGV_NAME());
                    holder.grower.setText("  " + arrayList.get(pos).getG_CODE() + " / " + arrayList.get(pos).getG_NAME());
                    holder.plot_Village.setText("  "+arrayList.get(pos).getPV_CODE() + " / " + arrayList.get(pos).getPV_NAME());
                    holder.plot_No.setText(arrayList.get(pos).getPL_NO());
                    holder.date_Time.setText("  "+arrayList.get(pos).getDATE());
                    holder.cdo_Approval.setText(arrayList.get(pos).getCDO_STATUS());
                    if (arrayList.get(pos).getCDO_STATUS().equalsIgnoreCase("Pending")) {
                        holder.cdo_Approval.setTextColor(Color.parseColor("#FB4209"));
                        holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (arrayList.get(pos).getCDO_STATUS().equalsIgnoreCase("Reject")) {
                        holder.cdo_Approval.setTextColor(Color.parseColor("#E11C85"));
                        holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (arrayList.get(pos).getCDO_STATUS().equalsIgnoreCase("Call Not Answered")) {
                        holder.cdo_Approval.setTextColor(Color.parseColor("#F1801D"));
                        holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    }else if (arrayList.get(pos).getCDO_STATUS().equalsIgnoreCase("Unknown")) {
                        holder.cdo_Approval.setTextColor(Color.parseColor("#0CB2BA"));
                        holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        holder.cdo_Approval.setTextColor(Color.parseColor("#3AB30B"));
                        holder.cdo_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    holder.crm_Approval.setText(arrayList.get(pos).getCRM_STATUS());

                    if (arrayList.get(pos).getCRM_STATUS().equalsIgnoreCase("Pending")) {
                        holder.crm_Approval.setTextColor(Color.parseColor("#FB4209"));
                        holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (arrayList.get(pos).getCRM_STATUS().equalsIgnoreCase("Reject")) {
                        holder.crm_Approval.setTextColor(Color.parseColor("#E11C85"));
                        holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (arrayList.get(pos).getCRM_STATUS().equalsIgnoreCase("Call Not Answered")) {
                        holder.crm_Approval.setTextColor(Color.parseColor("#F1801D"));
                        holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (arrayList.get(pos).getCRM_STATUS().equalsIgnoreCase("Unknown")) {
                        holder.crm_Approval.setTextColor(Color.parseColor("#0CB2BA"));//0CB2BA
                        holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        holder.crm_Approval.setTextColor(Color.parseColor("#3AB30B"));
                        holder.crm_Approval.setTypeface(Typeface.DEFAULT_BOLD);
                    }

                } catch (Exception e) {

                }


            }
        } catch (Exception e) {

        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView activity_Name, supervisor, village, grower, plot_Village, plot_No, date_Time, cdo_Approval, crm_Approval;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            activity_Name = itemView.findViewById(R.id.activity_Name);
            supervisor = itemView.findViewById(R.id.supervisor);
            village = itemView.findViewById(R.id.village);
            grower = itemView.findViewById(R.id.grower);
            plot_Village = itemView.findViewById(R.id.plot_Village);
            plot_No = itemView.findViewById(R.id.plot_No);
            date_Time = itemView.findViewById(R.id.date_Time);
            cdo_Approval = itemView.findViewById(R.id.cdo_Approval);
            crm_Approval = itemView.findViewById(R.id.crm_Approval);


        }


    }

}



