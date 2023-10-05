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

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.IndentModel;


public class StaffSeedIndentAdapter extends RecyclerView.Adapter<StaffSeedIndentAdapter.MyHolder> {

    private Context context;
    List<IndentModel> arrayList;


    public StaffSeedIndentAdapter(Context context, List<IndentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public StaffSeedIndentAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_seed_indent,null);
        StaffSeedIndentAdapter.MyHolder myHolder =new StaffSeedIndentAdapter.MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffSeedIndentAdapter.MyHolder holder, final int position) {
        DBHelper dbh=new DBHelper(context);
//village,grower,grower_father,variety,transporter,qty,rate;
        holder.village.setText(arrayList.get(position).getVillage()+" / "+arrayList.get(position).getVillageName());
        holder.grower.setText(arrayList.get(position).getGrower()+" / "+arrayList.get(position).getGrowerName());
        holder.grower_father.setText(arrayList.get(position).getGrowerFather());
        holder.variety.setText(arrayList.get(position).getVARIETY()+" / "+arrayList.get(position).getVARIETYNAME());
        holder.gps_area.setText(arrayList.get(position).getArea());
        holder.manual_area.setText(arrayList.get(position).getINDAREA());
        holder.date.setText(arrayList.get(position).getCurrentDate());

        holder.village.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.grower.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.grower_father.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.variety.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.gps_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.manual_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.date.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        TextView village,grower,grower_father,variety,gps_area,manual_area,date;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            grower_father=itemView.findViewById(R.id.grower_father);
            village=itemView.findViewById(R.id.village);
            grower=itemView.findViewById(R.id.grower);
            variety=itemView.findViewById(R.id.variety);
            gps_area=itemView.findViewById(R.id.gps_area);
            manual_area=itemView.findViewById(R.id.manual_area);
            date=itemView.findViewById(R.id.date);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
