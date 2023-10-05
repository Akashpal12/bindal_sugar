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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerFinderModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.view.supervisor.AddRemadialActivity;


public class ListRemadialMasterAdapter extends RecyclerView.Adapter<ListRemadialMasterAdapter.MyHolder> {

    List<GrowerFinderModel> arrayList;
    AlertDialog AlertdialogBox;
    private Context context;


    public ListRemadialMasterAdapter(Context context, List<GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plot_master, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //setTable(holder,arrayList.get(position).getJsonArray());
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName() + " (" + arrayList.get(position).getSuvType() + ")");
        holder.tv_father_name.setText(arrayList.get(position).getFather());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName() + " - " + arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getVillageName() + " - " + arrayList.get(position).getVillageCode());
        holder.tv_variety.setText("Variety: " + arrayList.get(position).getVariety());
        holder.tv_cane_type.setText("Cane Type: " + arrayList.get(position).getPrakar());
        holder.tv_cane_area.setText("Plot Area: " + arrayList.get(position).getGroupArea());
        holder.tv_variety_group.setText("Grower Area: " + arrayList.get(position).getCaneArea());
        holder.tv_get_grower_mobile.setText("Grower Mobile : " + arrayList.get(position).getMobile());
        holder.tv_PLOT_NO.setText("Plot No : " + arrayList.get(position).getPlotNo());
        holder.rl_tv_grower_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (arrayList.get(position).getMobile().toString().length() == 10) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + arrayList.get(position).getMobile()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // ISSUE THE sLOVED
                        context.startActivity(intent);
                    }
                } catch (SecurityException e) {
                    new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                }

            }

        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DBHelper dbh = new DBHelper(context);
                List<MasterDropDown> varDb = dbh.getMasterDropDown("12");
                if (varDb.size() == 0) {
                    try {
                        new DownloadMasterData().DownloadMaster(context);
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
                    }
                    return;
                }

               // openMenu(position);
                Intent intent = new Intent(context, AddRemadialActivity.class);
                intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                intent.putExtra("V_NAME", arrayList.get(position).getVillageName());
                intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                intent.putExtra("PLOT_VILL_CODE", arrayList.get(position).getPlotVillageCode());
                intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                intent.putExtra("LAT", arrayList.get(position).getLAT());
                intent.putExtra("LNG", arrayList.get(position).getLONG());
                intent.putExtra("D_CODE", arrayList.get(position).getDeseaseCode());
                intent.putExtra("D_NAME", arrayList.get(position).getDisease());
                context.startActivity(intent);


            }
        });
        if (arrayList.get(position).getSuvType().equalsIgnoreCase("CURRENT YEAR")) {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#6A610F"));
        } else {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#3F51B5"));
        }

    }


    public void openMenu(int position) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_remadial_menu, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            RelativeLayout crop_observation = mView.findViewById(R.id.crop_observation);
            crop_observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh = new DBHelper(context);
                    List<MasterDropDown> varDb = dbh.getMasterDropDown("12");
                    if (varDb.size() == 0) {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context, "Error : " + e.getMessage());
                        }
                        return;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, AddRemadialActivity.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("V_NAME", arrayList.get(position).getVillageName());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("PLOT_VILL_CODE", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("LAT", arrayList.get(position).getLAT());
                    intent.putExtra("LNG", arrayList.get(position).getLONG());
                    intent.putExtra("D_CODE", arrayList.get(position).getDeseaseCode());
                    intent.putExtra("D_NAME", arrayList.get(position).getDisease());
                    context.startActivity(intent);
                }
            });


            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlLayout, rlLayoutHeader;
        ImageView ivEdit;
        TextView tvCount, tv_grower_name, tv_father_name, tv_plot_village, tv_grower_village, tv_variety, tv_cane_type,
                tv_cane_area, tv_variety_group, tv_get_grower_mobile, tv_PLOT_NO;
        RelativeLayout rl_tv_grower_number;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rlLayoutHeader = itemView.findViewById(R.id.rlLayoutHeader);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvCount = itemView.findViewById(R.id.tvCount);
            tv_grower_name = itemView.findViewById(R.id.tv_grower_name);
            tv_father_name = itemView.findViewById(R.id.tv_father_name);
            tv_plot_village = itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village = itemView.findViewById(R.id.tv_grower_village);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_cane_type = itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area = itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group = itemView.findViewById(R.id.tv_variety_group);
            tv_get_grower_mobile = itemView.findViewById(R.id.tv_grower_number);
            rl_tv_grower_number = itemView.findViewById(R.id.rl_tv_grower_number);
            tv_PLOT_NO = itemView.findViewById(R.id.tv_PLOT_NO);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
