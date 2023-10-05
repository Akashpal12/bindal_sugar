package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GetRemedialViewAllReportModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class GetRemedialSummaryReportViewAdapter extends RecyclerView.Adapter<GetRemedialSummaryReportViewAdapter.MyHolder> {

    private Context context;
    List<GetRemedialViewAllReportModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    public GetRemedialSummaryReportViewAdapter(Context context, List<GetRemedialViewAllReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.plot_activity_summary_report_view_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        try {
            if (position == 0) {
                holder.remedial_activity.setText("  Remedial Activity");
                holder.remarks.setText("  Remarks");
                holder.remedial_code.setText("  Remedial Date");
                holder.details_txt.setText("Image");
                holder.SrNo.setText("Sr No");
                holder.details_img.setVisibility(View.GONE);

                holder.remedial_activity.setTypeface(Typeface.DEFAULT_BOLD);
                holder.remarks.setTypeface(Typeface.DEFAULT_BOLD);
                holder.remedial_code.setTypeface(Typeface.DEFAULT_BOLD);
                holder.details_txt.setTypeface(Typeface.DEFAULT_BOLD);
                holder.SrNo.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                final int pos = position - 1;
                holder.details_txt.setVisibility(View.GONE);
                holder.details_img.setVisibility(View.VISIBLE);
                TextView SrNo,details_txt,remedial_activity,remarks,remedial_code;
                holder.SrNo.setText("  "+arrayList.get(pos).getSRNO());
                holder.remedial_activity.setText(""+arrayList.get(pos).getREMNAME());
                holder.remarks.setText("  "+arrayList.get(pos).getDISP());
                holder.remedial_code.setText("  "+arrayList.get(pos).getDate());
                holder.SrNo.setText(String.valueOf(pos+1));
                holder.details_img.setOnClickListener(v -> OpenImage(pos));



            }
        } catch (Exception e) {

        }


    }

    void OpenImage(int pos){
        ZoomageView image_view;
        Button close;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View customLayout = inflater.inflate(R.layout.image_open, null);
        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        image_view=customLayout.findViewById(R.id.image_view);
        close=customLayout.findViewById(R.id.close);
        ProgressBar progressBar = customLayout.findViewById(R.id.progressBar); // Add this line
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(customLayout.getContext())
                    .load(arrayList.get(pos).getIMAGEURL())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(image_view);


        close.setOnClickListener(v -> {
         alertDialog.dismiss();
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView SrNo,details_txt,remedial_activity,remarks,remedial_code;
        ImageView details_img;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            remedial_activity = itemView.findViewById(R.id.remedial_activity);
            remarks = itemView.findViewById(R.id.remarks);
            remedial_code = itemView.findViewById(R.id.remedial_code);
            SrNo = itemView.findViewById(R.id.SrNo);
            details_img = itemView.findViewById(R.id.details_img);
            details_txt = itemView.findViewById(R.id.details_txt);


        }


    }

}



