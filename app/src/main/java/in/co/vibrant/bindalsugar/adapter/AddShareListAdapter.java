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
import in.co.vibrant.bindalsugar.model.FarmerShareModel;


public class AddShareListAdapter extends RecyclerView.Adapter<AddShareListAdapter.MyHolder> {

    private Context context;
    List<FarmerShareModel> arrayList;


    public AddShareListAdapter(Context context, List<FarmerShareModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pending_share,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.village_code.setText(arrayList.get(position).getVillageCode());
        holder.survey_id.setText(arrayList.get(position).getSurveyId());
        holder.plot_sr_no.setText(arrayList.get(position).getSrNo());
        holder.area_hec.setText(arrayList.get(position).getGrowerCode());
        holder.total_share.setText(arrayList.get(position).getShare());

        holder.village_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.survey_id.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.plot_sr_no.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.area_hec.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.total_share.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.edit.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView edit;
        RelativeLayout rlLayout,rl_text_1;
        TextView village_code,plot_sr_no,area_hec,total_share,survey_id;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            survey_id=itemView.findViewById(R.id.survey_id);
            village_code=itemView.findViewById(R.id.village_code);
            plot_sr_no=itemView.findViewById(R.id.plot_sr_no);
            area_hec=itemView.findViewById(R.id.area_hec);
            total_share=itemView.findViewById(R.id.total_share);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
