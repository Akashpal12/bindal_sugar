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
import in.co.vibrant.bindalsugar.model.CaneTypeCategorySummeryModel;


public class SupervisorCaneTypeCategorySummeryAdapter extends RecyclerView.Adapter<SupervisorCaneTypeCategorySummeryAdapter.MyHolder> {

    private Context context;
    List <CaneTypeCategorySummeryModel> arrayList;


    public SupervisorCaneTypeCategorySummeryAdapter(Context context, List <CaneTypeCategorySummeryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_supervisor_summery,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.category.setText(arrayList.get(position).getCategory());
        holder.ratoon.setText(arrayList.get(position).getTodayRatoon());
        holder.plant.setText(arrayList.get(position).getTodayPlant());
        holder.autumn.setText(arrayList.get(position).getTodayAutumn());
        holder.total.setText(arrayList.get(position).getTodayArea());
        holder.per.setText(arrayList.get(position).getTodayPer());
        holder.ratoon1.setText(arrayList.get(position).getToDateRatoon());
        holder.plant1.setText(arrayList.get(position).getToDatePlant());
        holder.autumn1.setText(arrayList.get(position).getToDateAutumn());
        holder.total1.setText(arrayList.get(position).getToDateArea());
        holder.per1.setText(arrayList.get(position).getToDatePer());
        holder.category.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.ratoon.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.plant.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.autumn.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.total.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.per.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.ratoon1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.plant1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.autumn1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.total1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.per1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
        holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rl_text_1;
        TextView category,ratoon,plant,autumn,total,per,ratoon1,plant1,autumn1,total1,per1;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.category);
            ratoon=itemView.findViewById(R.id.ratoon);
            plant=itemView.findViewById(R.id.plant);
            autumn=itemView.findViewById(R.id.autumn);
            total=itemView.findViewById(R.id.total);
            per=itemView.findViewById(R.id.per);
            ratoon1=itemView.findViewById(R.id.ratoon1);
            plant1=itemView.findViewById(R.id.plant1);
            autumn1=itemView.findViewById(R.id.autumn1);
            total1=itemView.findViewById(R.id.total1);
            per1=itemView.findViewById(R.id.per1);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
