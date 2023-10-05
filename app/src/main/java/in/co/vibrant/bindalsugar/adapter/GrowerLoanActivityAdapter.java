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
import in.co.vibrant.bindalsugar.model.GrowerLoanActivityModel;


public class GrowerLoanActivityAdapter extends RecyclerView.Adapter<GrowerLoanActivityAdapter.MyHolder> {

    private Context context;
    List<GrowerLoanActivityModel> arrayList;


    public GrowerLoanActivityAdapter(Context context, List<GrowerLoanActivityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grower_loan_enquiry,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.txt1.setText(arrayList.get(position).getRecoveryCode());
        holder.txt2.setText(arrayList.get(position).getRecoveryName());
        holder.txt3.setText(arrayList.get(position).getCategCode());
        holder.txt4.setText(arrayList.get(position).getLoanAmount());
        holder.txt5.setText(arrayList.get(position).getRecovered());
        holder.txt6.setText(arrayList.get(position).getBalance());
        holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.txt6.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));


      /*  DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int device_width = displaymetrics.widthPixels;
        int devicewidth = 0;
        if(device_width<1100)
        {
            devicewidth=device_width/7;
        }
        else
        {
            devicewidth=device_width/7;
        }
        int lastWidth=device_width-(devicewidth*7);
        if(lastWidth<=devicewidth)
        {
            lastWidth=devicewidth;
        }

        holder.txt1.getLayoutParams().width = devicewidth;
        holder.txt2.getLayoutParams().width = devicewidth+devicewidth;
        holder.txt3.getLayoutParams().width = devicewidth;
        holder.txt4.getLayoutParams().width = devicewidth;
        holder.txt5.getLayoutParams().width = devicewidth;
        holder.txt6.getLayoutParams().width = lastWidth;
        holder.rl_text_1.getLayoutParams().height = 100;
*/


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_text_1;
        TextView txt1,txt2,txt3,txt4,txt5,txt6;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            txt4=itemView.findViewById(R.id.txt4);
            txt5=itemView.findViewById(R.id.txt5);
            txt6=itemView.findViewById(R.id.txt6);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
