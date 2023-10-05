package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.CRMSummaryReportModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.CRMApprovalReport;

public class CRMSummaryReportAdapter extends RecyclerView.Adapter<CRMSummaryReportAdapter.MyHolder> {

    private Context context;
    List<CRMSummaryReportModel> arrayList;


    public CRMSummaryReportAdapter(Context context, List<CRMSummaryReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.crm_summary_reportlist, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {

            // For Creating Heading in The 0 Index

            if (position == 0) {
                try {
                    holder.heading.setText("Bindal Sugar");
                    holder.heading.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.heading.setTextSize(16f);
                } catch (Exception e) {

                }



                // Position 1
            } else if (position == 1) {
                try {
                    holder.heading.setText("CRM Summary Report (Plot Activity) | Date : "+arrayList.get(0).getF_DATE()+"  To  "+arrayList.get(0).getT_DATE());
                    holder.heading.setTypeface(Typeface.DEFAULT_BOLD);
                    // holder.date.setBackgroundColor(Color.parseColor("#FFF37404"));
                    // holder.date.setTextColor(Color.parseColor("#FFFFFF"));

                } catch (Exception e) {

                }

            // Position 2


            }  else if (position == 2) {
                try {
                    holder.main_layout_Box.setVisibility(View.VISIBLE);
                    holder.supervisoe_code_Name.setText("  Supervisor's Code/Name");
                    holder.total.setText("  Total");
                    holder.approved.setText("  Approved");
                    holder.pending.setText("  Pending");
                    holder.reject.setText("  Reject");
                    holder.not_Answered.setText("  Not Answered");
                    holder.details_txt.setText("  Detail");

                    holder.details_img.setVisibility(View.GONE);
                    holder.details_txt.setVisibility(View.VISIBLE);

                    holder.supervisoe_code_Name.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.total.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.approved.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.pending.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.reject.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.not_Answered.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.details_txt.setTypeface(Typeface.DEFAULT_BOLD);

                } catch (Exception e) {

                }



                // Adition in the Last Index
            } else if (position == arrayList.size() + 3) {
                try {
                    // Display the total area in the last row
                    double get_Total = 0,get_Approved=0,get_Pending=0,get_Reject=0,getNot_Answered=0;
                    for (CRMSummaryReportModel model : arrayList) {
                        get_Total += Double.parseDouble(model.getTotal());
                        get_Approved += Double.parseDouble(model.getApproved());
                        get_Pending += Double.parseDouble(model.getPending());
                        get_Reject += Double.parseDouble(model.getReject());
                        getNot_Answered += Double.parseDouble(model.getNotAnswered());
                    }
                    holder.details_img.setVisibility(View.GONE);
                    holder.details_txt.setVisibility(View.GONE);
                    holder.main_layout_Box.setVisibility(View.VISIBLE);
                    holder.heading.setVisibility(View.GONE);
                    holder.supervisoe_code_Name.setText("Grand Total : ");
                    holder.total.setText(""+new DecimalFormat("#0").format(get_Total));
                    holder.approved.setText(""+new DecimalFormat("#0").format(get_Approved));
                    holder.pending.setText(""+new DecimalFormat("#0").format(get_Pending));
                    holder.reject.setText(""+new DecimalFormat("#0").format(get_Reject));
                    holder.not_Answered.setText(""+new DecimalFormat("#0").format(getNot_Answered));


                    holder.supervisoe_code_Name.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.total.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.approved.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.pending.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.reject.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.not_Answered.setTypeface(Typeface.DEFAULT_BOLD);





                } catch (Exception e) {

                }


            }else {
                try {


                    //   My Data Set After Three Headings

                    final int pos = position - 3;
                    holder.main_layout_Box.setVisibility(View.VISIBLE);
                    holder.details_img.setVisibility(View.VISIBLE);
                    holder.details_txt.setVisibility(View.GONE);

                    holder.supervisoe_code_Name.setText("" + arrayList.get(pos).getSUPCODE()+" / "+arrayList.get(pos).getSUPNAME());
                    holder.total.setText("" + arrayList.get(pos).getTotal());
                    holder.pending.setText("" + arrayList.get(pos).getPending());
                    holder.approved.setText("" + arrayList.get(pos).getApproved());
                    holder.not_Answered.setText("" + arrayList.get(pos).getNotAnswered());
                    holder.reject.setText("" + arrayList.get(pos).getReject());



                    holder.details_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CRMApprovalReport.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("F_DATE",arrayList.get(pos).getF_DATE());
                            intent.putExtra("T_DATE",arrayList.get(pos).getT_DATE());
                            intent.putExtra("REPORT_TYPE",arrayList.get(pos).getREPORT_TYPE());
                            intent.putExtra("S_CODE",arrayList.get(pos).getSUPCODE()+" / "+arrayList.get(pos).getSUPNAME());
                            intent.putExtra("T_NAME",arrayList.get(pos).getREPORT_TYPE_NAME());
                            context.startActivity(intent);
                        }
                    });

                } catch (Exception e) {

                }


            }
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 4;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView supervisoe_code_Name, total, approved, pending, reject, not_Answered, details_txt, heading;
        ImageView details_img;
        LinearLayout main_layout_Box;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            supervisoe_code_Name = itemView.findViewById(R.id.supervisoe_code_Name);
            total = itemView.findViewById(R.id.total);
            approved = itemView.findViewById(R.id.approved);
            pending = itemView.findViewById(R.id.pending);
            reject = itemView.findViewById(R.id.reject);
            not_Answered = itemView.findViewById(R.id.not_Answered);
            details_txt = itemView.findViewById(R.id.details_txt);
            details_img = itemView.findViewById(R.id.details_img);
            heading = itemView.findViewById(R.id.heading);
            main_layout_Box = itemView.findViewById(R.id.main_layout_Box);


        }


    }


}
