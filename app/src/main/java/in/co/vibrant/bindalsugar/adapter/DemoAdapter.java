package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.SupervisorPerformanceModel;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.MyViewHolder> {
    Context context;
    List<SupervisorPerformanceModel> supervisorPerformanceModelList;

    public DemoAdapter(Context context, List<SupervisorPerformanceModel> supervisorPerformanceModelList) {
        this.context = context;
        this.supervisorPerformanceModelList = supervisorPerformanceModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_performance_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.plot_vill_Name.setText(""+supervisorPerformanceModelList.get(position).getVILLNAME());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView SrNo,plot_vill_Name,tottal_plots,total_area,plots_covered,percet_coverage,area_coverage,coverage_area;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            SrNo=itemView.findViewById(R.id.SrNo);
            plot_vill_Name=itemView.findViewById(R.id.plot_vill_Name);
            tottal_plots=itemView.findViewById(R.id.tottal_plots);
            total_area=itemView.findViewById(R.id.total_area);
            plots_covered=itemView.findViewById(R.id.plots_covered);
            percet_coverage=itemView.findViewById(R.id.percet_coverage);
            area_coverage=itemView.findViewById(R.id.area_coverage);
            coverage_area=itemView.findViewById(R.id.coverage_area);



        }
    }
}
