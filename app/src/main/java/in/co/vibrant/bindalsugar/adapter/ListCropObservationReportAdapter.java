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

import com.squareup.picasso.Picasso;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.CaneObservationReportModel;
import in.co.vibrant.bindalsugar.view.supervisor.ImageShowActivity;


public class ListCropObservationReportAdapter extends RecyclerView.Adapter<ListCropObservationReportAdapter.MyHolder> {

    private Context context;
    List <CaneObservationReportModel> arrayList;


    public ListCropObservationReportAdapter(Context context, List <CaneObservationReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_crop_observation_report,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName());
        holder.tv_father_name.setText(arrayList.get(position).getGrowerFatherName());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName()+" - "+arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getGrowerVillCode()+" - "+arrayList.get(position).getGrowerVillName());
        holder.tv_variety_group1.setText(""+arrayList.get(position).getVariety()+" / "+arrayList.get(position).getVarietyGroup() );
        holder.tv_variety.setText("Cane Type: "+arrayList.get(position).getCaneType());
        holder.tv_cane_type.setText("Cane Earthing: "+arrayList.get(position).getCaneEarthing());
        holder.tv_cane_area.setText("Cane Propping: "+arrayList.get(position).getCanePropping());
        holder.tv_variety_group.setText("Activity: "+arrayList.get(position).getActivity());
        holder.tv_crop_condition.setText("Crop Cond.: "+arrayList.get(position).getCropCondition());
        holder.tv_disease.setText("Disease: "+arrayList.get(position).getDisease());
        holder.tv_irrigation.setText("Irrigation: "+arrayList.get(position).getIrregation());
        holder.tv_date.setText("Date: "+arrayList.get(position).getEntryDate());
        holder.remark.setText("Remark: "+arrayList.get(position).getEntryDate());
        holder.tv_supervisor.setText("Sup: "+arrayList.get(position).getUserCode()+" / "+arrayList.get(position).getUserName());
        //
        /*Glide.with(this.context)
                .load(arrayList.get(position).getImage() )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageView);*/

        Picasso.with(context).load(arrayList.get(position).getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ImageShowActivity.class);
                intent.putExtra("ImageUrl",arrayList.get(position).getImage());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        ImageView imageView;
        TextView tvCount,tv_grower_name,tv_father_name,tv_plot_village,tv_grower_village,tv_variety,tv_cane_type,
                tv_cane_area,tv_variety_group,tv_crop_condition,tv_disease,tv_irrigation,tv_date,remark,tv_supervisor,tv_variety_group1;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_father_name=itemView.findViewById(R.id.tv_father_name);
            tv_plot_village=itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village=itemView.findViewById(R.id.tv_grower_village);
            tv_variety=itemView.findViewById(R.id.tv_variety);
            tv_cane_type=itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area=itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group=itemView.findViewById(R.id.tv_variety_group);
            tv_crop_condition=itemView.findViewById(R.id.tv_crop_condition);
            tv_disease=itemView.findViewById(R.id.tv_disease);
            tv_irrigation=itemView.findViewById(R.id.tv_irrigation);
            tv_date=itemView.findViewById(R.id.tv_date);
            remark=itemView.findViewById(R.id.remark);
            tv_supervisor=itemView.findViewById(R.id.tv_supervisor);
            tv_variety_group1=itemView.findViewById(R.id.tv_variety_group1);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
