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
import in.co.vibrant.bindalsugar.view.supervisor.AddCropObservation;
import in.co.vibrant.bindalsugar.view.supervisor.AddPlotEntryLog;
import in.co.vibrant.bindalsugar.view.supervisor.PlotActivityForm;
import in.co.vibrant.bindalsugar.view.supervisor.SoilSampleCollect;
import in.co.vibrant.bindalsugar.view.supervisor.StaffAddBrixPercentagee;
import in.co.vibrant.bindalsugar.view.supervisor.StaffAddCropGrowthObservation;


public class ListPlotListMasterAdapter extends RecyclerView.Adapter<ListPlotListMasterAdapter.MyHolder> {

    private Context context;
    List <GrowerFinderModel> arrayList;
    AlertDialog AlertdialogBox;


    public ListPlotListMasterAdapter(Context context, List <GrowerFinderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_plot_master,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //setTable(holder,arrayList.get(position).getJsonArray());
        holder.tvCount.setText(arrayList.get(position).getGrowerCode());
        holder.tv_grower_name.setText(arrayList.get(position).getGrowerName()+" ("+arrayList.get(position).getSuvType()+")");
        holder.tv_father_name.setText(arrayList.get(position).getFather());
        holder.tv_plot_village.setText(arrayList.get(position).getPlotVillageName()+" - "+arrayList.get(position).getPlotVillageCode());
        holder.tv_grower_village.setText(arrayList.get(position).getVillageName()+" - "+arrayList.get(position).getVillageCode());
        holder.tv_variety.setText("Variety: "+arrayList.get(position).getVariety());
        holder.tv_cane_type.setText("Cane Type: "+arrayList.get(position).getPrakar());
        holder.tv_cane_area.setText("Plot Area: "+arrayList.get(position).getGroupArea());
        holder.tv_variety_group.setText("Grower Area: "+arrayList.get(position).getCaneArea());
        holder.tv_get_grower_mobile.setText("Grower Mobile : "+arrayList.get(position).getMobile());
        holder.tv_PLOT_NO.setText("Plot No : "+arrayList.get(position).getPlotNo());
        holder.rl_tv_grower_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        if (arrayList.get(position).getMobile().toString().length()==10) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" +arrayList.get(position).getMobile()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // ISSUE THE sLOVED
                            context.startActivity(intent);
                        }
                    }
                    catch (SecurityException e)
                    {
                        new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                    }

                }

        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openMenu(position);
            }
        });
        if(arrayList.get(position).getSuvType().equalsIgnoreCase("CURRENT YEAR"))
        {
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#6A610F"));
        }
        else{
            holder.rlLayoutHeader.setBackgroundColor(Color.parseColor("#3F51B5"));
        }

    }

   /* private void setTable(MyHolder holder, JSONArray jsonArray)
    {
        //linear_activity
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int displayColumn=3;
            int totalColumn=8;
            int device_width = displaymetrics.widthPixels;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / displayColumn;
            } else {
                devicewidth = device_width / totalColumn;
            }
            int lastWidth = device_width - (devicewidth * totalColumn);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }


            TableLayout dialogueTable;
            //Intent intent = new Intent(Login.this, MainActivity.class);
            holder.linear_activity.removeAllViewsInLayout();
            dialogueTable = new TableLayout(context);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialogueTable.setShrinkAllColumns(false);
            dialogueTable.setStretchAllColumns(false);
            TableRow tableHeading = new TableRow(context);
            TextView th1 = new TextView(context);
            TextView th2 = new TextView(context);
            TextView th3 = new TextView(context);
            TextView th4 = new TextView(context);
            TextView th5 = new TextView(context);
            TextView th6 = new TextView(context);
            TextView th7 = new TextView(context);
            TextView th8 = new TextView(context);;
            th1.setText("Activity type");
            th2.setText("Crop Condition");
            th3.setText("Disease");
            th4.setText("Irrigation");
            th5.setText("Cane Earthing");
            th6.setText("Cane Proping");
            th7.setText("Activity Type");
            th8.setText("Activity Method");
            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            th3.setGravity(Gravity.CENTER);
            th4.setGravity(Gravity.CENTER);
            th5.setGravity(Gravity.CENTER);
            th6.setGravity(Gravity.CENTER);
            th7.setGravity(Gravity.CENTER);
            th8.setGravity(Gravity.CENTER);

            th1.setWidth(devicewidth);
            th2.setWidth(devicewidth);
            th3.setWidth(devicewidth);
            th4.setWidth(devicewidth);
            th5.setWidth(devicewidth);
            th6.setWidth(devicewidth);
            th7.setWidth(devicewidth);
            th8.setWidth(devicewidth);

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                th1.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th2.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th3.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th4.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th5.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th6.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);

                th7.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
                th8.setBackgroundResource(R.drawable.table_cell_space_black_bg_white_text);
            } else {
                th1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
                th8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_black_bg_white_text));
            }
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th3.setTextColor(Color.parseColor("#FFFFFF"));
            th4.setTextColor(Color.parseColor("#FFFFFF"));
            th5.setTextColor(Color.parseColor("#FFFFFF"));
            th6.setTextColor(Color.parseColor("#FFFFFF"));
            th7.setTextColor(Color.parseColor("#FFFFFF"));
            th8.setTextColor(Color.parseColor("#FFFFFF"));

            tableHeading.addView(th1);
            tableHeading.addView(th2);
            tableHeading.addView(th3);
            tableHeading.addView(th4);
            tableHeading.addView(th5);
            tableHeading.addView(th6);
            tableHeading.addView(th7);
            tableHeading.addView(th8);
            dialogueTable.addView(tableHeading);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(j);
                TableRow row = new TableRow(context);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    row.setBackgroundResource(R.drawable.table_row_bg);
                } else {
                    row.setBackground(ContextCompat.getDrawable(context, R.drawable.table_row_bg));
                }
                row.setId(j);
                TextView tv1 = new TextView(context);
                tv1.setText(jsonObject1.getString("ACT_NAME"));
                tv1.setWidth(devicewidth);
                tv1.setGravity(Gravity.LEFT);

                TextView tv2 = new TextView(context);
                tv2.setText(jsonObject1.getString("CROPCONDITION"));
                tv2.setWidth(devicewidth);
                tv2.setGravity(Gravity.CENTER);

                TextView tv3 = new TextView(context);
                tv3.setText(jsonObject1.getString("DISEASE"));
                tv3.setWidth(devicewidth);
                tv3.setGravity(Gravity.CENTER);

                TextView tv4 = new TextView(context);
                tv4.setText(jsonObject1.getString("IRRIGATION"));
                tv4.setWidth(devicewidth);
                tv4.setGravity(Gravity.CENTER);

                TextView tv5 = new TextView(context);
                tv5.setText(jsonObject1.getString("COJ_CANEERATHING"));
                tv5.setWidth(devicewidth);
                tv5.setGravity(Gravity.CENTER);

                TextView tv6 = new TextView(context);
                tv6.setText(jsonObject1.getString("COJ_CANEPROPPING"));
                tv6.setWidth(devicewidth);
                tv6.setGravity(Gravity.CENTER);

                TextView tv7 = new TextView(context);
                tv7.setText(jsonObject1.getString("ACTIVITYTYPE"));
                tv7.setWidth(devicewidth);
                tv7.setGravity(Gravity.CENTER);

                TextView tv8 = new TextView(context);
                tv8.setText(jsonObject1.getString("ACTIVITYMATHOD"));
                tv8.setWidth(devicewidth);
                tv8.setGravity(Gravity.CENTER);

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tv1.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv2.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv3.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv4.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv5.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv6.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv7.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);
                    tv8.setBackgroundResource(R.drawable.table_cell_space_white_bg_black_text);;
                } else {
                    tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv5.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv6.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv7.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));
                    tv8.setBackground(ContextCompat.getDrawable(context, R.drawable.table_cell_space_white_bg_black_text));;
                }
                tv1.setTextColor(Color.parseColor("#000000"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv5.setTextColor(Color.parseColor("#000000"));
                tv6.setTextColor(Color.parseColor("#000000"));
                tv7.setTextColor(Color.parseColor("#000000"));
                tv8.setTextColor(Color.parseColor("#000000"));;

                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                row.addView(tv5);
                row.addView(tv6);
                row.addView(tv7);
                row.addView(tv8);;
                dialogueTable.addView(row);
            }
            holder.linear_activity.addView(dialogueTable);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }*/

    public void openMenu(int position) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_plot_master_menu, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            RelativeLayout plot_activity = mView.findViewById(R.id.plot_activity);
            plot_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    if(varDb.size()==0)
                    {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                        return ;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, PlotActivityForm.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("V_NAME", arrayList.get(position).getVillageName());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("G_FATHER", arrayList.get(position).getFather());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("CANE_TYPE",arrayList.get(position).getPrakar());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("PLOT_VILL_NAME", arrayList.get(position).getPlotVillageName());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("SURTYPE", arrayList.get(position).getSuvType());
                    intent.putExtra("OLDSEAS", arrayList.get(position).getSeason());
                    intent.putExtra("OLDGHID", arrayList.get(position).getGhId());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());

                    context.startActivity(intent);
                }
            });
            RelativeLayout verital_check = mView.findViewById(R.id.verital_check);
            verital_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    List<MasterDropDown> catList=dbh.getMasterDropDown("28");
                    if(varDb.size()==0)
                    {

                            try {
                                new DownloadMasterData().DownloadMaster(context);
                            } catch (Exception e) {
                                new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                            }
                            return ;


                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, AddPlotEntryLog.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("SURTYPE", arrayList.get(position).getSuvType());
                    intent.putExtra("OLDSEAS", arrayList.get(position).getSeason());
                    intent.putExtra("OLDGHID", arrayList.get(position).getGhId());
                    intent.putExtra("VARIETY",arrayList.get(position).getVariety());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());
                    context.startActivity(intent);
                }
            });
            RelativeLayout crop_observation = mView.findViewById(R.id.crop_observation);
            crop_observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    if(varDb.size()==0)
                    {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                        return ;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, AddCropObservation.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("CANE_TYPE",arrayList.get(position).getPrakar());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());
                    intent.putExtra("GroupArea", arrayList.get(position).getGroupArea());
                    context.startActivity(intent);
                }
            });
            RelativeLayout soil_testing = mView.findViewById(R.id.soil_testing);
            soil_testing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    if(varDb.size()==0)
                    {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                        return ;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, SoilSampleCollect.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("V_NAME", arrayList.get(position).getVillageName());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());
                    context.startActivity(intent);
                }
            });
            RelativeLayout growth_observation = mView.findViewById(R.id.growth_observation);
            growth_observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    if(varDb.size()==0)
                    {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                        return ;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, StaffAddCropGrowthObservation.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());
                    context.startActivity(intent);
                }
            });
            RelativeLayout brix_percent = mView.findViewById(R.id.brix_percent);
            brix_percent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper dbh=new DBHelper(context);
                    List<MasterDropDown> varDb=dbh.getMasterDropDown("12");
                    if(varDb.size()==0)
                    {
                        try {
                            new DownloadMasterData().DownloadMaster(context);
                        } catch (Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error : "+e.getMessage());
                        }
                        return ;
                    }
                    AlertdialogBox.dismiss();
                    Intent intent = new Intent(context, StaffAddBrixPercentagee.class);
                    intent.putExtra("V_CODE", arrayList.get(position).getVillageCode());
                    intent.putExtra("G_CODE", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_SR_NO", arrayList.get(position).getPlotNo());
                    intent.putExtra("GSRNO", arrayList.get(position).getGrowerCode());
                    intent.putExtra("PLOT_VILL", arrayList.get(position).getPlotVillageCode());
                    intent.putExtra("AREA", arrayList.get(position).getCaneArea());
                    intent.putExtra("SHARE_AREA", arrayList.get(position).getPlotPercentage());
                    intent.putExtra("G_NAME", arrayList.get(position).getGrowerName());
                    intent.putExtra("MOBILE", arrayList.get(position).getMobile());
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

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rlLayout,rlLayoutHeader;
        ImageView ivEdit;
        TextView tvCount,tv_grower_name,tv_father_name,tv_plot_village,tv_grower_village,tv_variety,tv_cane_type,
                tv_cane_area,tv_variety_group,tv_get_grower_mobile,tv_PLOT_NO;
        RelativeLayout rl_tv_grower_number;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rlLayoutHeader=itemView.findViewById(R.id.rlLayoutHeader);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_father_name=itemView.findViewById(R.id.tv_father_name);
            tv_plot_village=itemView.findViewById(R.id.tv_plot_village);
            tv_grower_village=itemView.findViewById(R.id.tv_grower_village);
            tv_variety=itemView.findViewById(R.id.tv_variety);
            tv_cane_type=itemView.findViewById(R.id.tv_cane_type);
            tv_cane_area=itemView.findViewById(R.id.tv_cane_area);
            tv_variety_group=itemView.findViewById(R.id.tv_variety_group);
            tv_get_grower_mobile=itemView.findViewById(R.id.tv_grower_number);
            rl_tv_grower_number=itemView.findViewById(R.id.rl_tv_grower_number);
            tv_PLOT_NO=itemView.findViewById(R.id.tv_PLOT_NO);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
