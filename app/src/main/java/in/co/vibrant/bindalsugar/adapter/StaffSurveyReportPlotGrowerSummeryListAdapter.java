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
import in.co.vibrant.bindalsugar.model.ReportGrowerPlotSummeryModel;


public class StaffSurveyReportPlotGrowerSummeryListAdapter extends RecyclerView.Adapter<StaffSurveyReportPlotGrowerSummeryListAdapter.MyHolder> {

    private Context context;
    List<ReportGrowerPlotSummeryModel> arrayList;


    public StaffSurveyReportPlotGrowerSummeryListAdapter(Context context, List<ReportGrowerPlotSummeryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grower_plot_summery,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.p_sr_no.setText(arrayList.get(position).getPlotSrNo());
        holder.area.setText(arrayList.get(position).getArea());
        holder.variety.setText(arrayList.get(position).getVarietyName());
        holder.caneType.setText(arrayList.get(position).getCaneTypeName());

        holder.p_sr_no.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.variety.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.caneType.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
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
        TextView p_sr_no,area,variety,caneType;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            caneType=itemView.findViewById(R.id.caneType);
            variety=itemView.findViewById(R.id.variety);
            p_sr_no=itemView.findViewById(R.id.p_sr_no);
            area=itemView.findViewById(R.id.area);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
