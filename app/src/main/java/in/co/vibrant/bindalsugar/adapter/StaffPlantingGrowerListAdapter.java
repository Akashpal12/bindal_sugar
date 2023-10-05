package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class StaffPlantingGrowerListAdapter extends RecyclerView.Adapter<StaffPlantingGrowerListAdapter.MyHolder> {

    private Context context;
    List <GrowerModel> arrayList;
    AlertDialog Alertdialog;


    public StaffPlantingGrowerListAdapter(Context context, List <GrowerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grower_list,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {
            if(position==0)
            {
                holder.edit.setVisibility(View.GONE);
            }
            holder.txt1.setText(arrayList.get(position).getGrowerCode());
            holder.txt2.setText(arrayList.get(position).getGrowerName());
            holder.txt3.setText(arrayList.get(position).getGrowerFather());
            holder.txt4.setText(arrayList.get(position).getCentre());
            holder.txt5.setText(arrayList.get(position).getSociety());
            holder.txt6.setText(arrayList.get(position).getMobile());
            holder.txt7.setText(arrayList.get(position).getArea());
            /*holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, StaffPlantingGrowerPlotDetails.class);
                    intent.putExtra("vill",arrayList.get(position).getVillageCode());
                    intent.putExtra("grower",arrayList.get(position).getGrowerCode());
                    context.startActivity(intent);
                }
            });*/
            holder.edit.setVisibility(View.GONE);

            holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt6.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt7.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / 3;
            } else {
                devicewidth = device_width / 8;
            }
            int lastWidth = device_width - (devicewidth * 8);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }

            holder.txt1.getLayoutParams().width = devicewidth;
            holder.txt2.getLayoutParams().width = devicewidth;
            holder.txt3.getLayoutParams().width = devicewidth;
            holder.txt4.getLayoutParams().width = devicewidth;
            holder.txt5.getLayoutParams().width = devicewidth;
            holder.txt6.getLayoutParams().width = devicewidth;
            holder.txt7.getLayoutParams().width = lastWidth;
            holder.rl_text.getLayoutParams().height = 100;
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
        RelativeLayout rl_text;
        ImageView edit;
        TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            txt5=itemView.findViewById(R.id.txt5);
            txt6=itemView.findViewById(R.id.txt6);
            txt7=itemView.findViewById(R.id.txt7);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text=itemView.findViewById(R.id.rl_text);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

