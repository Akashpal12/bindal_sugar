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
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ListCropObservationTempItemAdapter extends RecyclerView.Adapter<ListCropObservationTempItemAdapter.MyHolder> {

    private Context context;
    List<Map<String, String>> arrayList;


    public ListCropObservationTempItemAdapter(Context context, List<Map<String, String>> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_temp_activity,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try{
            holder.tv_item_code.setText(arrayList.get(position).get("activityCode"));
            holder.tv_item_name.setText(arrayList.get(position).get("activityName"));
            holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).get("background")));
            holder.tv_item_code.setTextColor(Color.parseColor(arrayList.get(position).get("color")));
            holder.tv_item_name.setTextColor(Color.parseColor(arrayList.get(position).get("color")));
        }
        catch(Exception e)
        {
            new AlertDialogManager().RedDialog(context,e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        RelativeLayout rl_text_1;
        TextView tv_item_code,tv_item_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_code=itemView.findViewById(R.id.tv_item_code);
            tv_item_name=itemView.findViewById(R.id.tv_item_name);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
