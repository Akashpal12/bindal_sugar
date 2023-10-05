package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.SupervisorPerformanceModel;


public class SupervisePerformanceReportAdapetr extends RecyclerView.Adapter<SupervisePerformanceReportAdapetr.MyHolder> {

    private Context context;
    List<SupervisorPerformanceModel> arrayList;
    private List<SupervisorPerformanceModel> filteredItemList;
    AlertDialog Alertdialog;


    public SupervisePerformanceReportAdapetr(Context context, List<SupervisorPerformanceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredItemList = new ArrayList<>(arrayList);

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_performance_items, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //SrNo,plot_vill_Name,tottal_plots,total_area,plots_covered,percet_coverage,area_coverage,coverage_area;

        if (position < arrayList.size()) {
            holder.SrNo.setText(String.valueOf(position + 1));
            holder.plot_vill_Name.setText("" + arrayList.get(position).getVILLCODE() + " / " + arrayList.get(position).getVILLNAME());
            holder.tottal_plots.setText("" + arrayList.get(position).getPLOTS());
            holder.total_area.setText("" + arrayList.get(position).getAREA());
            holder.coverage_area.setText("" + arrayList.get(position).getAA_PRC());
            holder.area_coverage.setText("" + arrayList.get(position).getA_AREA());
            holder.percet_coverage.setText("" + arrayList.get(position).getAP_PRC());
            holder.plots_covered.setText("" + arrayList.get(position).getA_PLOTS());


        } else {
            //SrNo,plot_vill_Name,tottal_plots,total_area,plots_covered,percet_coverage,area_coverage,coverage_area;
            holder.SrNo.setText("");
            holder.plot_vill_Name.setText("   Total : ");
            double get_Total_plots = 0, get_Total_area = 0, get_Covered = 0, percent_coverage = 0;
            for (SupervisorPerformanceModel model : arrayList) {
                get_Total_plots += Double.parseDouble(model.getPLOTS());
                get_Total_area += Double.parseDouble(model.getAREA());
                get_Covered += Double.parseDouble(model.getA_PLOTS());
                percent_coverage += Double.parseDouble(model.getA_AREA());
            }
            holder.tottal_plots.setText(new DecimalFormat("#0").format(get_Total_plots));

            holder.total_area.setText(new DecimalFormat("#0.000").format(get_Total_area));
            holder.coverage_area.setText("");
            holder.area_coverage.setText("" + percent_coverage);
            holder.percet_coverage.setText("");
            holder.plots_covered.setText(new DecimalFormat("#0").format(get_Covered));

            holder.plot_vill_Name.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tottal_plots.setTypeface(Typeface.DEFAULT_BOLD);
            holder.total_area.setTypeface(Typeface.DEFAULT_BOLD);
            holder.area_coverage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.plots_covered.setTypeface(Typeface.DEFAULT_BOLD);


        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }
    public void filterList(ArrayList<SupervisorPerformanceModel> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }







/*

    @Override
    public Filter getFilter() {
        return ListFilter;
    }

    public final Filter ListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<SupervisorPerformanceModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SupervisorPerformanceModel model : arrayList) {
                    if (model.getVILLCODE().toLowerCase().contains(filterPattern) ||
                            model.getVILLNAME().toLowerCase().contains(filterPattern)) {
                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }



        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            filteredArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };

        public void updateList(List<SupervisorPerformanceModel> filteredList) {
            arrayList.clear();
            arrayList.addAll(filteredList);
            notifyDataSetChanged();
        }
*/


    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView SrNo, plot_vill_Name, tottal_plots, total_area, plots_covered, percet_coverage, area_coverage, coverage_area;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            SrNo = itemView.findViewById(R.id.SrNo);
            plot_vill_Name = itemView.findViewById(R.id.plot_vill_Name);
            tottal_plots = itemView.findViewById(R.id.tottal_plots);
            total_area = itemView.findViewById(R.id.total_area);
            plots_covered = itemView.findViewById(R.id.plots_covered);
            percet_coverage = itemView.findViewById(R.id.percet_coverage);
            area_coverage = itemView.findViewById(R.id.area_coverage);
            coverage_area = itemView.findViewById(R.id.coverage_area);


        }


    }


}
