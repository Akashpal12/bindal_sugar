package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.DashboardAttendanceModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaAttendanceDetailsReport;


public class RaDashboardAttendanceReportListAdapter extends RecyclerView.Adapter<RaDashboardAttendanceReportListAdapter.MyHolder> {

    private Context context;
    List <DashboardAttendanceModel> arrayList;
    AlertDialog Alertdialog;


    public RaDashboardAttendanceReportListAdapter(Context context, List <DashboardAttendanceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_report_attendance_dashboard_list,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {
            final SpannableString content = new SpannableString(arrayList.get(position).getName());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            if(position==0)
            {
                holder.txt1.setText(arrayList.get(position).getName());
            }
            else
            {
                holder.txt1.setText(content);
                holder.txt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, RaAttendanceDetailsReport.class);
                        intent.putExtra("userCode",arrayList.get(position).getUserCode());
                        intent.putExtra("user",arrayList.get(position).getName());
                        context.startActivity(intent);
                    }
                });
            }

            holder.txt2.setText(arrayList.get(position).getTotalUser());
            holder.txt3.setText(arrayList.get(position).getPresent());
            holder.txt4.setText(arrayList.get(position).getAbsent());
            holder.txt5.setText(arrayList.get(position).getInRightTime());
            holder.txt6.setText(arrayList.get(position).getOutRightTime());

            holder.txt1.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt2.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt3.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt4.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt5.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.txt6.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

           /* DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels-100;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / 7;
            } else {
                devicewidth = device_width / 7;
            }
            int lastWidth = device_width - (devicewidth * 7);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }

            holder.txt1.getLayoutParams().width = devicewidth*2;
            holder.txt2.getLayoutParams().width = devicewidth;
            holder.txt3.getLayoutParams().width = devicewidth;
            holder.txt4.getLayoutParams().width = devicewidth;
            holder.txt5.getLayoutParams().width = devicewidth;
            holder.txt6.getLayoutParams().width = lastWidth;
            //holder.rl_text.getLayoutParams().height = 100;
        }*/}
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
            rl_text=itemView.findViewById(R.id.rl_text);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

