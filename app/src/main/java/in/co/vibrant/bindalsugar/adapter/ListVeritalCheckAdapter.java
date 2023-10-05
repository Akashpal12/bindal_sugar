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

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.view.supervisor.AddPlotEntryLog;


public class ListVeritalCheckAdapter extends RecyclerView.Adapter<ListVeritalCheckAdapter.MyHolder> {

    private Context context;
    List <GrowerFinderModel> arrayList;


    public ListVeritalCheckAdapter(Context context, List <GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plot_master,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName()+" ("+arrayList.get(position).getSuvType()+")");
        holder.tv_father_name.setText(arrayList.get(position).getFather());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName()+" - "+arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getVillageName()+" - "+arrayList.get(position).getVillageCode());
        holder.tv_variety.setText("Variety: "+arrayList.get(position).getVariety());
        holder.tv_cane_type.setText("Cane Type: "+arrayList.get(position).getPrakar());
        holder.tv_cane_area.setText("Plot Area: "+arrayList.get(position).getGroupArea());
        holder.tv_variety_group.setText("Grower Area: "+arrayList.get(position).getCaneArea());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DBHelper dbh=new DBHelper(context);
                List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                if(varDb.size()==0)
                {
                    try {
                        new DownloadMasterData().DownloadMaster(context);
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                    }
                }
                Intent intent = new Intent(ListVeritalCheckAdapter.this.context, AddPlotEntryLog.class);
                intent.putExtra("V_CODE", ListVeritalCheckAdapter.this.arrayList.get(position).getVillageCode());
                intent.putExtra("G_CODE", ListVeritalCheckAdapter.this.arrayList.get(position).getGrowerCode());
                intent.putExtra("PLOT_SR_NO", ListVeritalCheckAdapter.this.arrayList.get(position).getPlotNo());
                intent.putExtra("GSRNO", ListVeritalCheckAdapter.this.arrayList.get(position).getGrowerCode());
                intent.putExtra("PLOT_VILL", ListVeritalCheckAdapter.this.arrayList.get(position).getPlotVillageCode());
                intent.putExtra("AREA", ListVeritalCheckAdapter.this.arrayList.get(position).getCaneArea());
                intent.putExtra("SHARE_AREA", ListVeritalCheckAdapter.this.arrayList.get(position).getPlotPercentage());
                intent.putExtra("G_NAME", ListVeritalCheckAdapter.this.arrayList.get(position).getGrowerName());
                intent.putExtra("SURTYPE", ListVeritalCheckAdapter.this.arrayList.get(position).getSuvType());
                intent.putExtra("OLDSEAS", ListVeritalCheckAdapter.this.arrayList.get(position).getSeason());
                intent.putExtra("OLDGHID", ListVeritalCheckAdapter.this.arrayList.get(position).getGhId());
                ListVeritalCheckAdapter.this.context.startActivity(intent);
            }
        });
        if(arrayList.get(position).getSuvType().equalsIgnoreCase("CURRENT YEAR"))
        {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#FBED6A"));
        }
        else{
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#3F51B5"));
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
                tv_cane_area,tv_variety_group,tv_grower_number;
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
            tv_grower_number=itemView.findViewById(R.id.tv_grower_number);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
