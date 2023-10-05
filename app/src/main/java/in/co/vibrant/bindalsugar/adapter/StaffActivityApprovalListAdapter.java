package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotActivityApprovalModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;


public class StaffActivityApprovalListAdapter extends RecyclerView.Adapter<StaffActivityApprovalListAdapter.MyHolder> {

    private Context context;
    List <PlotActivityApprovalModel> arrayList;
    AlertDialog Alertdialog;


    public StaffActivityApprovalListAdapter(Context context, List <PlotActivityApprovalModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_report_approval_list,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        try {

            if(position==0)
            {
                holder.txt1.setText("Activity type");
                holder.txt2.setText("Plot Village");
                holder.txt3.setText("Plot Number");
                holder.txt4.setText("Grower Village");
                holder.txt5.setText("Grower Name");
                holder.txt6.setText("Grower Father");
                holder.txt7.setText("Area");
                holder.txt8.setText("Activity");
                holder.txt9.setText("Activity Method");
                holder.txt10.setText("Meeting Type");
                holder.txt11.setText("Meeting Name");
                holder.txt12.setText("Meeting Number");
            }
            else
            {
                holder.checkBox.setChecked(arrayList.get(position).isChecked());
                holder.txt1.setText(arrayList.get(position).getActivityType());
                holder.txt2.setText(arrayList.get(position).getPlotVillageCode()+" / "+arrayList.get(position).getPlotVillageName());
                holder.txt3.setText(""+arrayList.get(position).getPlotNumber());
                holder.txt4.setText(arrayList.get(position).getGrowerVillageCode()+" / "+arrayList.get(position).getGrowerVillageName());
                holder.txt5.setText(arrayList.get(position).getGrowerCode()+" / "+arrayList.get(position).getGrowerName());
                holder.txt6.setText(arrayList.get(position).getGrowerFather());
                holder.txt7.setText(""+arrayList.get(position).getArea());
                holder.txt8.setText(arrayList.get(position).getActivity());
                holder.txt9.setText(arrayList.get(position).getActivityMethod());
                holder.txt10.setText(arrayList.get(position).getMeetingType());
                holder.txt11.setText(arrayList.get(position).getMeetingName());
                holder.txt12.setText(arrayList.get(position).getMeetingNumber());
                if(arrayList.get(position).getMeetingNumber().length()==10)
                {
                    holder.txt12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (arrayList.get(position).getMeetingNumber().length() == 10) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + arrayList.get(position).getMeetingNumber()));
                                    context.startActivity(intent);
                                }
                            }
                            catch (SecurityException e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
                            }
                            catch (Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.getMessage());
                            }

                        }
                    });
                }
            }


            if(position==0)
            {
                //holder.checkBox.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try{
                            arrayList.get(0).setChecked(isChecked);
                            for(int i=1;i< arrayList.size();i++)
                            {
                                if(isChecked)
                                {
                                    arrayList.get(i).setChecked(true);
                                    notifyItemChanged(position);
                                }
                                else{
                                    arrayList.get(i).setChecked(false);
                                    notifyItemChanged(position);
                                }
                                notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }
                });
            }
            else
            {
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(holder.checkBox.isChecked())
                        {
                            arrayList.get(position).setChecked(true);

                        }
                        else{
                            arrayList.get(position).setChecked(false);
                            notifyItemChanged(position);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
            //




            //holder.checkBox.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
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

            holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / 3;
            } else {
                devicewidth = device_width / 9;
            }
            int lastWidth = device_width - (devicewidth * 9);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
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

        }
        catch (Exception e)
        {
            //new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        RelativeLayout rl_text;
        TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
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
            rl_text=itemView.findViewById(R.id.rl_text);
            //tv_status=itemView.findViewById(R.id.tv_status);
        }
        //public ImageView getImage(){ return this.imageView;}
    }

}

