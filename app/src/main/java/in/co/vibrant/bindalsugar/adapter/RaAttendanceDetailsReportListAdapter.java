package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.DashboardAttendanceModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.ImageShowActivity;


public class RaAttendanceDetailsReportListAdapter extends RecyclerView.Adapter<RaAttendanceDetailsReportListAdapter.MyHolder> {

    private Context context;
    List <DashboardAttendanceModel> arrayList;
    AlertDialog Alertdialog;


    public RaAttendanceDetailsReportListAdapter(Context context, List <DashboardAttendanceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_attendance_details,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {
            holder.tvCount.setText(arrayList.get(position).getUserCode());
            holder.status.setText(arrayList.get(position).getStatus());
            holder.tv_name.setText(arrayList.get(position).getName());
            holder.in_time.setText(arrayList.get(position).getInTime());
            holder.out_time.setText(arrayList.get(position).getOutTime());
            holder.address_in.setText(arrayList.get(position).getInAddress());
            holder.address_out.setText(arrayList.get(position).getOutAddress());
            holder.address_out.setText(arrayList.get(position).getOutAddress());
            Glide.with(this.context)
                    .load(arrayList.get(position).getInImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.in_image);
            Glide.with(this.context)
                    .load(arrayList.get(position).getOutImage() )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.out_image);
            holder.in_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ImageShowActivity.class);
                    intent.putExtra("ImageUrl",arrayList.get(position).getInImage());
                    context.startActivity(intent);
                }
            });
            holder.out_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ImageShowActivity.class);
                    intent.putExtra("ImageUrl",arrayList.get(position).getOutImage());
                    context.startActivity(intent);
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
        ImageView in_image,out_image;
        RelativeLayout rl_text;
        TextView tvCount,tv_name,status,in_time,out_time,address_in,address_out;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            in_image=itemView.findViewById(R.id.in_image);
            out_image=itemView.findViewById(R.id.out_image);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
            status=itemView.findViewById(R.id.status);
            in_time=itemView.findViewById(R.id.in_time);
            out_time=itemView.findViewById(R.id.out_time);
            address_in=itemView.findViewById(R.id.address_in);
            address_out=itemView.findViewById(R.id.address_out);
            rl_text=itemView.findViewById(R.id.rl_text);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

