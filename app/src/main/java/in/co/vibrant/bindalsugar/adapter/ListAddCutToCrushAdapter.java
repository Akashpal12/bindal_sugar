package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ListAddCutToCrushAdapter extends RecyclerView.Adapter<ListAddCutToCrushAdapter.MyHolder> {

    private Context context;
    List <GModal> arrayList;


    public ListAddCutToCrushAdapter(Context context, List <GModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_cut_to_crush_model,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            holder.tv_name.setText(arrayList.get(position).getCaneSupplyPurcheyName());
            holder.vill_code.setText(arrayList.get(position).getVillCode()+" / "+arrayList.get(position).getVillName());
            holder.grow_code.setText(arrayList.get(position).getGrowCode()+" / "+arrayList.get(position).getGrowerName());
            holder.relation.setText(arrayList.get(position).getRelationName());
            holder.purchey_number.setText(arrayList.get(position).getPurcheyNumber());
            holder.issued_purchi.setText(arrayList.get(position).getIssuedpurcheyNumber());
            holder.supply_mode.setText(arrayList.get(position).getModeOfSupplyName());
            holder.vehicle_number.setText(arrayList.get(position).getVehicleNumber());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.remove(position);
                    //recycler.removeViewAt(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, arrayList.size());
                }
            });
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        LinearLayout rl_text_1;
        TextView tv_name,vill_code,grow_code,relation,purchey_number,supply_mode,vehicle_number,issued_purchi;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            vill_code=itemView.findViewById(R.id.vill_code);
            grow_code=itemView.findViewById(R.id.grow_code);
            relation=itemView.findViewById(R.id.relation);
            purchey_number=itemView.findViewById(R.id.purchey_number);
            supply_mode=itemView.findViewById(R.id.supply_mode);
            vehicle_number=itemView.findViewById(R.id.vehicle_number);
            issued_purchi=itemView.findViewById(R.id.issued_purchi);
            imageView=itemView.findViewById(R.id.imageView);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
