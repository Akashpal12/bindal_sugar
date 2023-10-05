package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerPaymentActivityModel;


public class GrowerPaymentActivityAdapter extends RecyclerView.Adapter<GrowerPaymentActivityAdapter.MyHolder> {

    private Context context;
    List<GrowerPaymentActivityModel> arrayList;


    public GrowerPaymentActivityAdapter(Context context, List<GrowerPaymentActivityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grower_payment_enquiry,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.txt1.setText(arrayList.get(position).getPurchyNumber());
        holder.txt2.setText(arrayList.get(position).getMillPurcheyNumber());
        holder.txt3.setText(arrayList.get(position).getSupplyDate());
        holder.txt4.setText(arrayList.get(position).getShiftDate());
        holder.txt5.setText(arrayList.get(position).getNetWeight());
        holder.txt6.setText(arrayList.get(position).getAmount());
        holder.txt7.setText(arrayList.get(position).getDecuction());
        holder.txt8.setText(arrayList.get(position).getNetPay());
        holder.txt9.setText(arrayList.get(position).getBillNumber());
        holder.txt10.setText(arrayList.get(position).getBillDate());
        holder.txt11.setText(arrayList.get(position).getAccountNumber());
        holder.txt12.setText(arrayList.get(position).getBranchCode());
        holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt6.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt7.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt8.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt9.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt10.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt11.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt12.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));


        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int device_width = displaymetrics.widthPixels;
        int devicewidth = 0;
        if(device_width<1100)
        {
            devicewidth=device_width/3;
        }
        else
        {
            devicewidth=device_width/8;
        }
        int lastWidth=device_width-(devicewidth*12);
        if(lastWidth<=devicewidth)
        {
            lastWidth=devicewidth;
        }

        holder.txt1.getLayoutParams().width = devicewidth;
        holder.txt2.getLayoutParams().width = devicewidth;
        holder.txt3.getLayoutParams().width = devicewidth;
        holder.txt4.getLayoutParams().width = devicewidth;
        holder.txt5.getLayoutParams().width = devicewidth;
        holder.txt6.getLayoutParams().width = devicewidth;
        holder.txt7.getLayoutParams().width = devicewidth;
        holder.txt8.getLayoutParams().width = devicewidth;
        holder.txt9.getLayoutParams().width = devicewidth;
        holder.txt10.getLayoutParams().width = devicewidth;
        holder.txt11.getLayoutParams().width = devicewidth;
        holder.txt12.getLayoutParams().width = lastWidth;
        holder.rl_text_1.getLayoutParams().height = 100;

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_text_1;
        TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            txt5=itemView.findViewById(R.id.txt5);
            txt6=itemView.findViewById(R.id.txt6);
            txt7=itemView.findViewById(R.id.txt7);
            txt8=itemView.findViewById(R.id.txt8);
            txt9=itemView.findViewById(R.id.txt9);
            txt10=itemView.findViewById(R.id.txt10);
            txt11=itemView.findViewById(R.id.txt11);
            txt12=itemView.findViewById(R.id.txt12);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
