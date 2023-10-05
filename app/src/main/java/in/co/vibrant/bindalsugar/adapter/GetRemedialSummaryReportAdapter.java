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

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GetRemedialSummaryReportModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.GetRemedialSummaryReportViewDetails;

public class GetRemedialSummaryReportAdapter extends RecyclerView.Adapter<GetRemedialSummaryReportAdapter.MyHolder> {

    List<GetRemedialSummaryReportModel> arrayList;
    private Context context;


    public GetRemedialSummaryReportAdapter(Context context, List<GetRemedialSummaryReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_remedial_summary_reportlist, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {

            if (position == 0) {
                holder.supervisoe_code_Name.setText("Superviser Code/Name");
                holder.plot_village_code_name.setText("Plot Village Code/Name");
                holder.plot_No.setText("Plot No");
                holder.disease.setText("Disease");
                holder.remedial_txt.setText("Remedials");
                holder.remedial_txt.setVisibility(View.VISIBLE);
                holder.remedial_img.setVisibility(View.GONE);

                holder.supervisoe_code_Name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_village_code_name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_No.setTypeface(Typeface.DEFAULT_BOLD);
                holder.disease.setTypeface(Typeface.DEFAULT_BOLD);
                holder.remedial_txt.setTypeface(Typeface.DEFAULT_BOLD);

            } else {
                final int pos = position - 1;
                holder.supervisoe_code_Name.setText(arrayList.get(pos).getSUPCODE() + " / " + arrayList.get(pos).getSUPNAME());
                holder.plot_village_code_name.setText(arrayList.get(pos).getPLOTVILL() + " / " + arrayList.get(pos).getVILLNAME());
                holder.plot_No.setText(arrayList.get(pos).getPLOTNO());
                holder.disease.setText(arrayList.get(pos).getDISEASE());
                holder.remedial_img.setVisibility(View.VISIBLE);
                holder.remedial_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GetRemedialSummaryReportViewDetails.class);
                        intent.putExtra("PLOT_VILL", arrayList.get(pos).getPLOTVILL());
                        intent.putExtra("PLOT_NO", arrayList.get(pos).getPLOTNO());
                        intent.putExtra("F_DATE", arrayList.get(pos).getF_DATE());
                        intent.putExtra("T_DATE", arrayList.get(pos).getT_DATE());
                        intent.putExtra("DISEASE", arrayList.get(pos).getDISEASE());
                        intent.putExtra("SUP_C_NAME", arrayList.get(pos).getSUPCODE() + " / " + arrayList.get(pos).getSUPNAME());
                        intent.putExtra("PLOT_V_N", arrayList.get(pos).getPLOTVILL() + " / " + arrayList.get(pos).getVILLNAME() + " / " + arrayList.get(pos).getPLOTNO());
                        context.startActivity(intent);
                    }
                });
            }


        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView supervisoe_code_Name, plot_village_code_name, plot_No, disease, remedial_txt;
        ImageView remedial_img;
        LinearLayout main_layout_Box;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            supervisoe_code_Name = itemView.findViewById(R.id.supervisoe_code_Name);
            plot_village_code_name = itemView.findViewById(R.id.plot_village_code_name);
            plot_No = itemView.findViewById(R.id.plot_No);
            disease = itemView.findViewById(R.id.disease);
            remedial_txt = itemView.findViewById(R.id.remedial_txt);
            remedial_img = itemView.findViewById(R.id.remedial_img);
            main_layout_Box = itemView.findViewById(R.id.main_layout_Box);


        }


    }


}
