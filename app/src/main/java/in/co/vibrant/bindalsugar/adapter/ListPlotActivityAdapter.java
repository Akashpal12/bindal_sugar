package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
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
import in.co.vibrant.bindalsugar.model.PlotActivityModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class ListPlotActivityAdapter extends RecyclerView.Adapter<ListPlotActivityAdapter.MyHolder> {

    private Context context;
    List <PlotActivityModel> arrayList;


    public ListPlotActivityAdapter(Context context, List <PlotActivityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_agro_input_model,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            holder.txt_2.setVisibility(View.GONE);
            if (position == 0) {
                holder.txt_1.setText("Activity");
                holder.txt_2.setText("Number");
                holder.txt_3.setText("Area");
                holder.txt_4.setText("Date");
                holder.txt_5.setText("Method");
                //holder.imageView.setVisibility(View.GONE);
                holder.rl_text_1.setBackgroundColor(Color.parseColor("#000000"));

                holder.txt_1.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt_2.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt_3.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt_4.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt_5.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                final int pos = position - 1;
                holder.txt_1.setText(""+arrayList.get(pos).getActivityName());
                holder.txt_2.setText(""+arrayList.get(pos).getActivityNumber());
                holder.txt_3.setText(""+arrayList.get(pos).getArea());
                holder.txt_4.setText(""+arrayList.get(pos).getDate());
                holder.txt_5.setText(""+arrayList.get(pos).getActivityMethodName());
                holder.rl_text_1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.txt_1.setTextColor(Color.parseColor("#000000"));
                holder.txt_2.setTextColor(Color.parseColor("#000000"));
                holder.txt_3.setTextColor(Color.parseColor("#000000"));
                holder.txt_4.setTextColor(Color.parseColor("#000000"));
                holder.txt_5.setTextColor(Color.parseColor("#000000"));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.remove(pos);
                        //recycler.removeViewAt(position);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, arrayList.size()+1);
                    }
                });
            }
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        LinearLayout rl_text_1;
        TextView txt_1,txt_2,txt_3,txt_4,txt_5;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt_1=itemView.findViewById(R.id.txt_1);
            txt_2=itemView.findViewById(R.id.txt_2);
            txt_3=itemView.findViewById(R.id.txt_3);
            txt_4=itemView.findViewById(R.id.txt_4);
            txt_5=itemView.findViewById(R.id.txt_5);
            imageView=itemView.findViewById(R.id.imageView);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
