package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.TruckInCentreModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class TruckInCentreAdapter extends RecyclerView.Adapter<TruckInCentreAdapter.MyHolder> {

    private Context context;
    List<TruckInCentreModal> arrayList;
    AlertDialog Alertdialog;


    public TruckInCentreAdapter(Context context, List<TruckInCentreModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_truck_in_centre,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.centre_code.setText(arrayList.get(position).getCentreCode());
        holder.centre_name.setText(arrayList.get(position).getCentreName());
        holder.truck_number.setText(arrayList.get(position).getTruckName());
        holder.driver_name.setText(arrayList.get(position).getDriverName());
        holder.driver_number.setText(arrayList.get(position).getDriverNumber());
        holder.arrival.setText(arrayList.get(position).getArrivalTime());
        holder.wait_time.setText(arrayList.get(position).getWaitTime());

        //holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.centre_code.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.centre_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.truck_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.driver_number.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.arrival.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.wait_time.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        if (arrayList.get(position).getDriverNumber().length() == 10) {
            holder.ivCall.setVisibility(View.VISIBLE);
        }
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (arrayList.get(position).getDriverNumber().length() == 10) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + arrayList.get(position).getDriverNumber()));
                        context.startActivity(intent);
                    }
                }
                catch (SecurityException e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }
                catch (Exception e)
                {
                    new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }

            }
        });
        //CentreTruckListActivity
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ivCall;
        TextView centre_code,centre_name,truck_number,driver_name,driver_number,arrival,wait_time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            centre_code=itemView.findViewById(R.id.centre_code);
            centre_name=itemView.findViewById(R.id.centre_name);
            driver_name=itemView.findViewById(R.id.driver_name);
            driver_number=itemView.findViewById(R.id.driver_number);
            truck_number=itemView.findViewById(R.id.truck_number);
            arrival=itemView.findViewById(R.id.arrival);
            wait_time=itemView.findViewById(R.id.wait_time);
            ivCall=itemView.findViewById(R.id.ivCall);
        }
//dialogue_single_truck_location_on_driver_name,map
        //public ImageView getImage(){ return this.imageView;}
    }

}
