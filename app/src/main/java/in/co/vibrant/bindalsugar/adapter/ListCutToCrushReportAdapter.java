package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
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
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.view.supervisor.CutToCrushReport;


public class ListCutToCrushReportAdapter extends RecyclerView.Adapter<ListCutToCrushReportAdapter.MyHolder> {

    List<GrowerFinderModel> arrayList;
    private Context context;


    public ListCutToCrushReportAdapter(Context context, List<GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_crop_observationnew, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName());
        holder.tv_father_name.setText(arrayList.get(position).getFather());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName() + " - " + arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getVillageName() + " - " + arrayList.get(position).getVillageCode());
        holder.tv_variety.setText("Variety: " + arrayList.get(position).getVariety());
        holder.tv_cane_type.setText("Cane Type: " + arrayList.get(position).getPrakar());
        holder.tv_cane_area.setText("Area: " + arrayList.get(position).getCaneArea());
        holder.tv_variety_group.setText("Varietye Group: " + arrayList.get(position).getVarietyGroupCode());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ListCutToCrushReportAdapter.this.context, CutToCrushReport.class);
                intent.putExtra("PLOTVILL", ListCutToCrushReportAdapter.this.arrayList.get(position).getPlotVillageCode());
                intent.putExtra("PLOTVILLNAME", ListCutToCrushReportAdapter.this.arrayList.get(position).getPlotVillageName());
                intent.putExtra("PLOTNO", ListCutToCrushReportAdapter.this.arrayList.get(position).getPlotNo());

                intent.putExtra("GROWER_Village_CODE", ListCutToCrushReportAdapter.this.arrayList.get(position).getVillageCode());
                intent.putExtra("GROWER_Village_NAME", ListCutToCrushReportAdapter.this.arrayList.get(position).getVillageName());


                intent.putExtra("GROWCODE", ListCutToCrushReportAdapter.this.arrayList.get(position).getGrowerCode());
                intent.putExtra("GROWNAME", ListCutToCrushReportAdapter.this.arrayList.get(position).getGrowerName());
                intent.putExtra("GROWFATHER", ListCutToCrushReportAdapter.this.arrayList.get(position).getFather());
                intent.putExtra("AREA", ListCutToCrushReportAdapter.this.arrayList.get(position).getCaneArea());

                ListCutToCrushReportAdapter.this.context.startActivity(intent);
            }
        });
        /*holder.tv_id.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_mobile.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_factory.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));

        holder.tv_imei.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));*/
        //holder.tv_status.setText(arrayList.get(position).getTodate());
        /*if(arrayList.get(position).getStatus().equalsIgnoreCase("Stop")){
            holder.tv_status.setTextColor(Color.parseColor("#FFE91E63"));
        }
        else if(arrayList.get(position).getStatus().equalsIgnoreCase("Running"))
        {
            holder.tv_status.setTextColor(Color.parseColor("#FF4CAF50"));
        }*/

        /*if(arrayList.get(position).getName().equalsIgnoreCase("Total") ||
                arrayList.get(position).getName().equalsIgnoreCase("Avg") ||
                arrayList.get(position).getName().equalsIgnoreCase("Projected"))
        {
            holder.tv_name.setTypeface(null, Typeface.BOLD);
        }*/

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlLayout, rl_text_1;
        ImageView ivEdit;
        TextView tvCount, tv_grower_name, tv_father_name, tv_plot_village, tv_grower_village, tv_variety, tv_cane_type,
                tv_cane_area, tv_variety_group;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvCount = itemView.findViewById(R.id.tvCount);
            tv_grower_name = itemView.findViewById(R.id.tv_grower_name);
            tv_father_name = itemView.findViewById(R.id.tv_father_name);
            tv_plot_village = itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village = itemView.findViewById(R.id.tv_grower_village);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_cane_type = itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area = itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group = itemView.findViewById(R.id.tv_variety_group);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
