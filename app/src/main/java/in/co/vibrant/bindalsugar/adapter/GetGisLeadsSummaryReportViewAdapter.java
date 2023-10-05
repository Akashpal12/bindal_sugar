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
import in.co.vibrant.bindalsugar.model.GetGisLeadsReportModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;


public class GetGisLeadsSummaryReportViewAdapter extends RecyclerView.Adapter<GetGisLeadsSummaryReportViewAdapter.MyHolder> {

    List<GetGisLeadsReportModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    private Context context;

    public GetGisLeadsSummaryReportViewAdapter(Context context, List<GetGisLeadsReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.plot_getgis_leads_summary_report_view_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        try {
            if (position == 0) {

                //   SrNo,details_txt,supervisor_code_name,plot_vilage_code_name,gis_leads,result,final_result,done;

                holder.supervisor_code_name.setText("  Supervisor");
                holder.plot_vilage_code_name.setText("  Plot Village");
                holder.plot_no.setText("  Plot No ");
                holder.gis_leads.setText("  GIS Leads ");
                holder.result.setText("  Result ");
                holder.final_result.setText("  Final Result ");
                holder.done.setText("  Date");
                holder.details_txt.setText("Image");
                holder.SrNo.setText("Sr No");
                holder.details_img.setVisibility(View.GONE);

                holder.supervisor_code_name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_vilage_code_name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_no.setTypeface(Typeface.DEFAULT_BOLD);
                holder.gis_leads.setTypeface(Typeface.DEFAULT_BOLD);
                holder.final_result.setTypeface(Typeface.DEFAULT_BOLD);
                holder.result.setTypeface(Typeface.DEFAULT_BOLD);
                holder.done.setTypeface(Typeface.DEFAULT_BOLD);
                holder.details_txt.setTypeface(Typeface.DEFAULT_BOLD);
                holder.SrNo.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                final int pos = position - 1;
                holder.details_txt.setVisibility(View.GONE);
                holder.details_img.setVisibility(View.VISIBLE);
                holder.supervisor_code_name.setText("" + arrayList.get(pos).getSUPCODE()+" / "+arrayList.get(pos).getSUPNAME());
                holder.plot_vilage_code_name.setText("" + arrayList.get(pos).getVILLCODE()+" / "+arrayList.get(pos).getVILLNAME());
                holder.plot_no.setText("  " + arrayList.get(pos).getPLOTNO());
                holder.gis_leads.setText("  " + arrayList.get(pos).getGIS_LEADS());
                holder.result.setText("  " + arrayList.get(pos).getRESULT());
                holder.final_result.setText("  " + arrayList.get(pos).getFINALRESULT());
                holder.done.setText("  " + arrayList.get(pos).getDATE());
                holder.SrNo.setText(String.valueOf(pos + 1));
                holder.details_img.setOnClickListener(v -> OpenImage(pos));
            }
        } catch (Exception e) {

        }


    }

    void OpenImage(int pos) {
        ZoomageView image_view;
        Button close;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View customLayout = inflater.inflate(R.layout.image_open, null);
        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        image_view = customLayout.findViewById(R.id.image_view);
        close = customLayout.findViewById(R.id.close);
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

        TextView SrNo, details_txt, supervisor_code_name, plot_vilage_code_name, gis_leads, result, final_result, done, plot_no;
        ImageView details_img;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            supervisor_code_name = itemView.findViewById(R.id.supervisor_code_name);
            plot_vilage_code_name = itemView.findViewById(R.id.plot_vilage_code_name);
            gis_leads = itemView.findViewById(R.id.gis_leads);
            result = itemView.findViewById(R.id.result);
            final_result = itemView.findViewById(R.id.final_result);
            done = itemView.findViewById(R.id.done);
            SrNo = itemView.findViewById(R.id.SrNo);
            details_img = itemView.findViewById(R.id.details_img);
            details_txt = itemView.findViewById(R.id.details_txt);
            plot_no = itemView.findViewById(R.id.plot_no);


        }


    }

}



