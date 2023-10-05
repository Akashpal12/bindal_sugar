package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ReportCaneTypeSummeryModel;


public class ReportCaneTypeSummeryListAdapter extends RecyclerView.Adapter<ReportCaneTypeSummeryListAdapter.MyHolder> {

    private Context context;
    List<ReportCaneTypeSummeryModel> arrayList;


    public ReportCaneTypeSummeryListAdapter(Context context, List<ReportCaneTypeSummeryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_cane_type_summery1,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.cane_type.setText(arrayList.get(position).getVarietyName());
        holder.area.setText(arrayList.get(position).getArea());
        holder.no_of_plot.setText(arrayList.get(position).getTotalPlot());

        holder.cane_type.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.no_of_plot.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView edit;
        RelativeLayout rlLayout,rl_text_1;
        TextView cane_type,area,no_of_plot;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            no_of_plot=itemView.findViewById(R.id.no_of_plot);
            cane_type=itemView.findViewById(R.id.cane_type);
            area=itemView.findViewById(R.id.area);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
