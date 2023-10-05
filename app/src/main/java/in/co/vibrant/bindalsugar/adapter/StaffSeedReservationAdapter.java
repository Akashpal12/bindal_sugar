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
import in.co.vibrant.bindalsugar.model.SeedReservationModel;


public class StaffSeedReservationAdapter extends RecyclerView.Adapter<StaffSeedReservationAdapter.MyHolder> {

    private Context context;
    List<SeedReservationModel> arrayList;


    public StaffSeedReservationAdapter(Context context, List<SeedReservationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public StaffSeedReservationAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_seed_reservation,null);
        StaffSeedReservationAdapter.MyHolder myHolder =new StaffSeedReservationAdapter.MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffSeedReservationAdapter.MyHolder holder, final int position) {
        DBHelper dbh=new DBHelper(context);
//village,grower,grower_father,variety,transporter,qty,rate;
        holder.village.setText(arrayList.get(position).getVillage()+" / "+arrayList.get(position).getVillageName());
        holder.grower.setText(arrayList.get(position).getGrower()+" / "+arrayList.get(position).getGrowerName());
        holder.grower_father.setText(arrayList.get(position).getGrowerFather());
        holder.variety.setText(arrayList.get(position).getVariety()+" / "+arrayList.get(position).getVarietyName());
        holder.transporter.setText(arrayList.get(position).getTransportation());
        holder.qty.setText(arrayList.get(position).getQuantity());
        holder.rate.setText(arrayList.get(position).getRate());

        holder.village.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.grower.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.grower_father.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.variety.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.transporter.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.qty.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rate.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        TextView village,grower,grower_father,variety,transporter,qty,rate;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            grower_father=itemView.findViewById(R.id.grower_father);
            village=itemView.findViewById(R.id.village);
            grower=itemView.findViewById(R.id.grower);
            variety=itemView.findViewById(R.id.variety);
            transporter=itemView.findViewById(R.id.transporter);
            qty=itemView.findViewById(R.id.qty);
            rate=itemView.findViewById(R.id.rate);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
