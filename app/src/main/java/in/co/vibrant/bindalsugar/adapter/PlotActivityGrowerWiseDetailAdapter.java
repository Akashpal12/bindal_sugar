package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotActivityGrowerWiseDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.view.supervisor.PlotActivityActivityWiseDetail;


public class PlotActivityGrowerWiseDetailAdapter extends RecyclerView.Adapter<PlotActivityGrowerWiseDetailAdapter.MyHolder> {

    private Context context;
    List<PlotActivityGrowerWiseDetailModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;


    public PlotActivityGrowerWiseDetailAdapter(Context context, List<PlotActivityGrowerWiseDetailModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.plot_activity_grower_wise_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        try {
            if (position == 0) {
                holder.grower_Village.setText("   Grower Village");
                holder.grower_Name.setText("  Grower Name");
                holder.plot_village.setText("  Plot Village");
                holder.plot_no.setText("Plot No");
                holder.plot_type_variety.setText("  Plot Type/Variety");
                holder.shared_area.setText("Shared Area");
                holder.SrNo.setText("Sr No");
                holder.details_img.setVisibility(View.GONE);



                holder.grower_Village.setTypeface(Typeface.DEFAULT_BOLD);
                holder.grower_Name.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_village.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_no.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_type_variety.setTypeface(Typeface.DEFAULT_BOLD);
                holder.shared_area.setTypeface(Typeface.DEFAULT_BOLD);
                holder.SrNo.setTypeface(Typeface.DEFAULT_BOLD);


            }  else if (position == arrayList.size() + 1) {

                // Display the total area in the last row
                double totalArea = 0.0;
                for (PlotActivityGrowerWiseDetailModel model : arrayList) {
                    totalArea += Double.parseDouble(model.getSHAREDAREA());
                }

                holder.grower_Village.setText("  Total ");
                holder.grower_Name.setText("");
                holder.plot_village.setText("");
                holder.plot_no.setText("   "+String.valueOf(arrayList.size()));
                holder.plot_type_variety.setText("");
                holder.shared_area.setText("   "+new DecimalFormat("#0.000").format(totalArea));
                holder.SrNo.setText("");
                holder.details_img.setVisibility(View.GONE);
                holder.details_txt.setVisibility(View.GONE);

                holder.grower_Village.setTypeface(Typeface.DEFAULT_BOLD);
                holder.plot_no.setTypeface(Typeface.DEFAULT_BOLD);
                holder.shared_area.setTypeface(Typeface.DEFAULT_BOLD);

            }else {
                final int pos = position - 1;
                holder.details_txt.setVisibility(View.GONE);
                holder.details_img.setVisibility(View.VISIBLE);




                holder.grower_Village.setText("  "+arrayList.get(pos).getGVILLCODE()+" / "+arrayList.get(pos).getGVILLNAME());
                holder.grower_Name.setText("  "+arrayList.get(pos).getGCODE()+" / "+arrayList.get(pos).getGNAME());
                holder.plot_village.setText("  "+arrayList.get(pos).getPLVLCODE()+" / "+arrayList.get(pos).getPLVLNAME());
                holder.plot_no.setText("  "+arrayList.get(pos).getPLNO());
                holder.SrNo.setText(String.valueOf(pos+1));
                holder.shared_area.setText(arrayList.get(pos).getSHAREDAREA());
                holder.plot_type_variety.setText("  "+arrayList.get(pos).getPLOTTYPE()+" / "+arrayList.get(pos).getVARIETY());
                holder.details_img.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PlotActivityActivityWiseDetail.class);
                    intent.putExtra("F_DATE",arrayList.get(pos).getF_DATE());
                    intent.putExtra("T_DATE",arrayList.get(pos).getT_DATE());
                    intent.putExtra("PLOT_NO",arrayList.get(pos).getPLNO());
                    intent.putExtra("PLOT_VL_CODE",arrayList.get(pos).getGVILLCODE());
                    intent.putExtra("G_VILLCODE",arrayList.get(pos).getGVILLCODE()+" / "+arrayList.get(pos).getGVILLNAME());
                    context.startActivity(intent);


                });


            }
        } catch (Exception e) {

        }


    }



    @Override
    public int getItemCount() {
        return arrayList.size() + 2;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView grower_Village,grower_Name,plot_village,plot_no,plot_type_variety,shared_area,SrNo,details_txt;
        ImageView details_img;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            grower_Village = itemView.findViewById(R.id.grower_Village);
            grower_Name = itemView.findViewById(R.id.grower_Name);
            plot_village = itemView.findViewById(R.id.plot_village);
            plot_no = itemView.findViewById(R.id.plot_no);
            plot_type_variety = itemView.findViewById(R.id.plot_type_variety);
            shared_area = itemView.findViewById(R.id.shared_area);
            SrNo = itemView.findViewById(R.id.SrNo);
            details_img = itemView.findViewById(R.id.details_img);
            details_txt = itemView.findViewById(R.id.details_txt);


        }


    }

}



