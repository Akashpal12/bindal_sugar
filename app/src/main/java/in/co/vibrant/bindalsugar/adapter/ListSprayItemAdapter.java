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

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.SprayItemModel;


public class ListSprayItemAdapter extends RecyclerView.Adapter<ListSprayItemAdapter.MyHolder> {

    private Context context;
    List<SprayItemModel> arrayList;


    public ListSprayItemAdapter(Context context, List<SprayItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plant_spray_item,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_item_code.setText(arrayList.get(position).getItem());
        holder.tv_item_name.setText(arrayList.get(position).getItemName());
        holder.tv_area.setText(arrayList.get(position).getArea());
        holder.tv_qty.setText(arrayList.get(position).getQty());
        holder.tv_unit.setText(arrayList.get(position).getUnit());
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

        holder.tv_item_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_item_name.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_area.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_qty.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.tv_unit.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));

        /*if(arrayList.get(position).getStatus().equalsIgnoreCase("Stop")){

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
        RelativeLayout rl_text_1;
        TextView tv_item_code,tv_item_name,tv_area,tv_qty,tv_unit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_code=itemView.findViewById(R.id.tv_item_code);
            tv_item_name=itemView.findViewById(R.id.tv_item_name);
            tv_area=itemView.findViewById(R.id.tv_area);
            tv_qty=itemView.findViewById(R.id.tv_qty);
            tv_unit=itemView.findViewById(R.id.tv_unit);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
