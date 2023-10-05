package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.view.supervisor.PlotActivityForm;


public class ListFindPlotActivityAdapter extends RecyclerView.Adapter<ListFindPlotActivityAdapter.MyHolder> {

    private Context context;
    List <PlotFarmerShareModel> arrayList;


    public ListFindPlotActivityAdapter(Context context, List <PlotFarmerShareModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plot_activity_finder,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        DBHelper dbh=new DBHelper(context);
        List<VillageSurveyModel> plotVillageSurveyModels=dbh.getSurveyVillageModel(arrayList.get(position).getPlotVillageCode());
        List<VillageSurveyModel> growerVillageSurveyModels=dbh.getSurveyVillageModel(arrayList.get(position).getVillageCode());
        List<MasterCaneSurveyModel> masterVarietyModelList=dbh.getMasterModelById("1",arrayList.get(position).getVarietyCode());
        List<MasterCaneSurveyModel> masterCaneTypeModelList=dbh.getMasterModelById("2",arrayList.get(position).getCaneType());
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName()+" ("+arrayList.get(position).getPlotFrom()+")");
        holder.tv_father_name.setText(arrayList.get(position).getGrowerFatherName());
        //holder.tv_plot_type.setText(arrayList.get(position).getPlotFrom());
        if(plotVillageSurveyModels.size()>0)
        {
            holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageCode()+" - "+plotVillageSurveyModels.get(0).getVillageName());
        }
        else{
            holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageCode());
        }
        if(plotVillageSurveyModels.size()>0)
        {
            holder.tv_grower_village.setText(arrayList.get(position).getVillageCode()+" - "+growerVillageSurveyModels.get(0).getVillageName());
        }
        else{
            holder.tv_grower_village.setText(arrayList.get(position).getVillageCode());
        }
        if(masterVarietyModelList.size()>0)
        {
            holder.tv_variety.setText("Variety: "+arrayList.get(position).getVarietyCode()+" - "+masterVarietyModelList.get(0).getName());
        }
        else{
            holder.tv_variety.setText("Variety: "+arrayList.get(position).getVarietyCode());
        }
        if(masterVarietyModelList.size()>0)
        {
            holder.tv_cane_type.setText("Cane Type: "+arrayList.get(position).getCaneType()+" - "+masterCaneTypeModelList.get(0).getName());
        }
        else{
            holder.tv_cane_type.setText("Cane Type: "+arrayList.get(position).getCaneType());
        }
        holder.tv_cane_area.setText("Plot Area: "+arrayList.get(position).getAreaHectare());
        double g_area=(Double.parseDouble(arrayList.get(position).getAreaHectare())*Double.parseDouble(arrayList.get(position).getShare()))/100;
        holder.tv_variety_group.setText("Grower Area: "+new DecimalFormat("0.000").format(g_area));
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ListFindPlotActivityAdapter.this.context, PlotActivityForm.class);
                intent.putExtra("V_CODE", ListFindPlotActivityAdapter.this.arrayList.get(position).getPlotVillageCode());
                intent.putExtra("PLOT_SR_NO", ListFindPlotActivityAdapter.this.arrayList.get(position).getPlotSrNo());
                intent.putExtra("PLOT_FROM", ListFindPlotActivityAdapter.this.arrayList.get(position).getPlotFrom());
                ListFindPlotActivityAdapter.this.context.startActivity(intent);
            }
        });
        if(arrayList.get(position).getPlotFrom().equalsIgnoreCase("Current Year Survey"))
        {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#FBED6A"));
        }
        else if(arrayList.get(position).getPlotFrom().equalsIgnoreCase("Last Year Survey"))
        {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        else{
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#E91E63"));
        }
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

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rlLayoutHeader;
        ImageView ivEdit;
        TextView tvCount,tv_grower_name,tv_father_name,tv_plot_village,tv_grower_village,tv_variety,tv_cane_type,
                tv_cane_area,tv_variety_group,tv_plot_type;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rlLayoutHeader=itemView.findViewById(R.id.rlLayoutHeader);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_father_name=itemView.findViewById(R.id.tv_father_name);
            tv_plot_village=itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village=itemView.findViewById(R.id.tv_grower_village);
            tv_variety=itemView.findViewById(R.id.tv_variety);
            tv_cane_type=itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area=itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group=itemView.findViewById(R.id.tv_variety_group);
            tv_plot_type=itemView.findViewById(R.id.tv_plot_type);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
