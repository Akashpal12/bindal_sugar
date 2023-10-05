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
import java.util.Map;

import in.co.vibrant.bindalsugar.R;


public class ListPlantingTempItemAdapter extends RecyclerView.Adapter<ListPlantingTempItemAdapter.MyHolder> {

    private Context context;
    List<Map<String, String>> arrayList;


    public ListPlantingTempItemAdapter(Context context, List<Map<String, String>> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_temp_planting,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_item_code.setText(arrayList.get(position).get("itemid"));
        holder.tv_item_name.setText(arrayList.get(position).get("itemcode"));
        holder.tv_qty.setText(arrayList.get(position).get("itemqty"));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).get("background")));

        holder.tv_item_code.setTextColor(Color.parseColor(arrayList.get(position).get("color")));
        holder.tv_item_name.setTextColor(Color.parseColor(arrayList.get(position).get("color")));
        holder.tv_qty.setTextColor(Color.parseColor(arrayList.get(position).get("color")));

    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        RelativeLayout rl_text_1;
        TextView tv_item_code,tv_item_name,tv_qty;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_code=itemView.findViewById(R.id.tv_item_code);
            tv_item_name=itemView.findViewById(R.id.tv_item_name);
            tv_qty=itemView.findViewById(R.id.tv_qty);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
