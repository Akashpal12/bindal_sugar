package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotListDataModel;


public class FindPlotListAdapter extends RecyclerView.Adapter<FindPlotListAdapter.holder> {
    private Context context;
    List<PlotListDataModel> arrayList;
    AlertDialog AlertdialogBox;


    public FindPlotListAdapter(Context context, List<PlotListDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plot_list, null);
        holder holder = new holder(layout);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {
            //holder.tv_name.setText(userDetailsModelList.get(position).getGrowerCode());
            //holder.tv_Date.setText("Date : " + arrayList.get(position).getDATE());
            // holder.date.setText("" + arrayList.get(position).getCREATEDAT());
            SimpleDateFormat inputFormat = new SimpleDateFormat("M/d/yyyy h:mm:ss a");
            try {
                Date dateTime = inputFormat.parse(arrayList.get(position).getDATE());
                SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
                String dateStr = dateFormat.format(dateTime);
                String timeStr = timeFormat.format(dateTime);
                // Setting the date and time in the respective TextViews
                //holder.tv_Date.setText("Time : " + timeStr);
                holder.tv_Date.setText(""+dateStr+" : "+timeStr+"  ");


            } catch (Exception e) {
                e.getMessage();
            }
            holder.tvCount.setText(String.valueOf(position+1));
            holder.ActivityName.setText(arrayList.get(position).getACT_NAME());
            holder.CROPCONDITION.setText(arrayList.get(position).getCROPCONDITION());
            holder.DISEASE.setText(arrayList.get(position).getDISEASE());
            holder.IRRIGATION.setText(arrayList.get(position).getIRRIGATION());
            holder.COJ_CANEERATHING.setText(arrayList.get(position).getCOJ_CANEERATHING());
            holder.COJ_CANEPROPPING.setText(arrayList.get(position).getCOJ_CANEPROPPING());
            holder.ACTIVITYTYPE.setText(arrayList.get(position).getACTIVITYTYPE());
            holder.ACTIVITYMATHOD.setText(arrayList.get(position).getACTIVITYMATHOD());

           /* Glide.with(this.context)
                    .load(arrayList.get(position).getImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivimage);*/
/*
           Picasso.with(context).load(arrayList.get(position).getImagePath()).into(holder.ivimage);
*/

          /*
          *
          * */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void ShowVideo(String imageUrl)
    {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.single_item_plot_list, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            WebView videoView = mView.findViewById(R.id.video);
            videoView.loadUrl(imageUrl);


            Button closeMap = mView.findViewById(R.id.closeMap);
            closeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertdialogBox.dismiss();
                }
            });
            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }*/

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        ImageView ivimage, ivVideo;
        TextView tvCount, ActivityName, tv_Date, CROPCONDITION, DISEASE, IRRIGATION,
                COJ_CANEERATHING, COJ_CANEPROPPING, ACTIVITYTYPE, ACTIVITYMATHOD;

        public holder(@NonNull View itemView) {
            super(itemView);

            tvCount = itemView.findViewById(R.id.tvCount);
            ActivityName = itemView.findViewById(R.id.ActivityName);
            tv_Date = itemView.findViewById(R.id.tv_Date);
            CROPCONDITION = itemView.findViewById(R.id.CROPCONDITION);
            DISEASE = itemView.findViewById(R.id.DISEASE);
            IRRIGATION = itemView.findViewById(R.id.IRRIGATION);
            ivVideo = itemView.findViewById(R.id.ivVideo);
            COJ_CANEERATHING = itemView.findViewById(R.id.COJ_CANEERATHING);
            COJ_CANEPROPPING = itemView.findViewById(R.id.COJ_CANEPROPPING);
            ACTIVITYTYPE = itemView.findViewById(R.id.ACTIVITYTYPE);
            ACTIVITYMATHOD = itemView.findViewById(R.id.ACTIVITYMATHOD);

        }
    }
}
