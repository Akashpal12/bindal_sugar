package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.SupervisorSummary;


public class SupervisorSummeryAdapter extends RecyclerView.Adapter<SupervisorSummeryAdapter.MyHolder> {

    private Context context;
    List <SupervisorSummary> arrayList;


    public SupervisorSummeryAdapter(Context context, List <SupervisorSummary> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_supervisor_summery,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //,,,,,
        holder.sup_code.setText(arrayList.get(position).getSupervisorCode());
        holder.sup_name.setText(arrayList.get(position).getSupervisorName());
        holder.on_date_plot.setText(arrayList.get(position).getOnDatePlots());
        holder.on_date_area.setText(arrayList.get(position).getOnDateArea());
        holder.to_date_plot.setText(arrayList.get(position).getTotalPlot());
        holder.to_date_area.setText(arrayList.get(position).getTotalArea());
        holder.sup_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.sup_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.on_date_plot.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.on_date_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.to_date_plot.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.to_date_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        TextView sup_code,sup_name,on_date_plot,on_date_area,to_date_plot,to_date_area;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            sup_code=itemView.findViewById(R.id.sup_code);
            sup_name=itemView.findViewById(R.id.sup_name);
            on_date_plot=itemView.findViewById(R.id.on_date_plot);
            on_date_area=itemView.findViewById(R.id.on_date_area);
            to_date_plot=itemView.findViewById(R.id.to_date_plot);
            to_date_area=itemView.findViewById(R.id.to_date_area);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
