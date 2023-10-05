package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.AddMaterialDataModel;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.AgriInput.AddMaterialDataActivity;



public class ListFormDataAdapter extends RecyclerView.Adapter<ListFormDataAdapter.MyHolder> {
    private List<AgriInputFormModel> arrayList;
    Context mContext;


    public ListFormDataAdapter(Context mContext, List<AgriInputFormModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_form_row_item, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            String name = arrayList.get(position).getAgriGrowerName().toLowerCase();
            String mName = name.substring(0, 1).toUpperCase() + name.substring(1);
            final DBHelper db = new DBHelper(mContext);
            final AgriInputFormModel agriInputFormModel=arrayList.get(position);
            final List<AddMaterialDataModel> addMaterialDataModels = db.getAllAgriInputDataMaterialById(arrayList.get(position).getAgri_input_Id());
            holder.tv_grower_name.setText(mName);
            double total = 0;
            for (int i = 0; i < addMaterialDataModels.size(); i++) {
                total += Double.parseDouble(addMaterialDataModels.get(i).getMatTotalAmount());
            }

            holder.tvtotalmaterial.setText("Total Material Qty : " + addMaterialDataModels.size());
            holder.tv_materialamount.setText("Total Material Amount : " + total);
            holder.tv_grower_code.setText(arrayList.get(position).getAgriGrowerCode());
            holder.tv_father_name.setText(arrayList.get(position).getAgriGrowerFather());
            holder.tv_grower_mobile.setText("Grower Mobile : " + arrayList.get(position).getAgriGrowerMobile());
            holder.tv_village_code_value.setText(arrayList.get(position).getAgriVillageCode());
            holder.tv_village_name_value.setText("Village Name : " + arrayList.get(position).getAgriVillageName());
            holder.tvCount.setText(String.valueOf(position + 1));
            if (arrayList.get(position).getServer_status().equalsIgnoreCase("No")) {
                holder.ivCloud.setVisibility(View.GONE);
            } else {
                holder.ivCloud.setVisibility(View.VISIBLE);
            }
            if (addMaterialDataModels.size() == 0) {
                holder.ivmateriallist.setImageResource(R.drawable.ic_baseline_playlist_add_24);
            } else {
                holder.ivmateriallist.setImageResource(R.drawable.ic_baseline_reorder_24);
            }


            holder.ivCloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addMaterialDataModels.size() > 0) {
                        arrayList=db.getAllAgriInputData();
                        notifyItemChanged (position, arrayList.size());
                        db.resentAgriInputFormModel("",""+agriInputFormModel.getAgri_input_Id(),"No");
                            /*new WTaskAddFactory().execute(arrayList.get(position).getAgriGrowerUnitCode(), arrayList.get(position).getAgriVillageCode(),
                                    arrayList.get(position).getAgriGrowerCode(), arrayList.get(position).getAgriGrowerMobile(),
                                    arrayList.get(position).getAgriPhoto_1(), arrayList.get(position).getAgriPhoto_2(), arrayList.get(position).getAgriSignature(),
                                    "" + arrayList.get(position).getAgri_input_Id());*/
                    } else {
                        new AlertDialogManager().showToast((Activity) mContext, "No material added in this grower");
                    }
                }
            });

            holder.ivmateriallist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addMaterialDataModels.size() == 0) {
                        Intent intent = new Intent(mContext, AddMaterialDataActivity.class);
                        intent.putExtra("agriInputId", ""+arrayList.get(position).getAgri_input_Id());
                        mContext.startActivity(intent);
                    } else {
                        try {
                            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(mContext);
                            View mView = LayoutInflater.from(mContext).inflate(R.layout.dialogue_material_list, null);
                            dialogbilder.setView(mView);
                            //private AlertDialog.Builder builder;
                            AlertDialog dialog = dialogbilder.create();
                            //holder.ll.removeAllViewsInLayout();
                            LinearLayout ll = (LinearLayout) mView.findViewById(R.id.layout_booking);
                            TableLayout dialogueTable = new TableLayout(mContext);
                            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            dialogueTable.setShrinkAllColumns(true);
                            dialogueTable.setStretchAllColumns(true);
                            TableRow tableHeading = new TableRow(mContext);
                            TextView th1 = new TextView(mContext);
                            TextView th2 = new TextView(mContext);
                            TextView th3 = new TextView(mContext);
                            TextView th4 = new TextView(mContext);
                            TextView th5 = new TextView(mContext);
                            th1.setText("Material");
                            th2.setText("Qty");
                            th3.setText("Rate");
                            th4.setText("Amount");
                            th5.setText("");
                            th1.setGravity(Gravity.CENTER);
                            th2.setGravity(Gravity.CENTER);
                            th3.setGravity(Gravity.CENTER);
                            th4.setGravity(Gravity.CENTER);
                            th5.setGravity(Gravity.CENTER);
                            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                tableHeading.setBackgroundResource(R.drawable.table_row_bg);
                            } else {
                                tableHeading.setBackground(ContextCompat.getDrawable(mContext, R.drawable.table_row_bg));
                            }
                            //tableHeading.setBackgroundResource(drawable.table_row_bg);
                            //tableHeading.addView(th1);
                            tableHeading.addView(th1);
                            tableHeading.addView(th2);
                            tableHeading.addView(th3);
                            tableHeading.addView(th4);
                            tableHeading.addView(th5);
                            dialogueTable.addView(tableHeading);
                            for (int j = 0; j < addMaterialDataModels.size(); j++) {
                                TableRow row = new TableRow(mContext);
                                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    row.setBackgroundResource(R.drawable.table_row_bg);
                                } else {
                                    row.setBackground(ContextCompat.getDrawable(mContext, R.drawable.table_row_bg));
                                }
                                row.setId(j);
                                TextView tv1 = new TextView(mContext);
                                tv1.setText(addMaterialDataModels.get(j).getMaterialName());
                                tv1.setWidth(80);
                                tv1.setGravity(Gravity.LEFT);
                                TextView tv2 = new TextView(mContext);
                                tv2.setText(addMaterialDataModels.get(j).getMaterialQuantity());
                                //tv2.setWidth(50);
                                tv2.setGravity(Gravity.CENTER);
                                TextView tv3 = new TextView(mContext);
                                tv3.setText(addMaterialDataModels.get(j).getMaterialRate());
                                //tv3.setWidth(50);
                                tv3.setGravity(Gravity.CENTER);
                                TextView tv4 = new TextView(mContext);
                                tv4.setText(addMaterialDataModels.get(j).getMatTotalAmount());
                                //tv4.setWidth(50);
                                tv4.setGravity(Gravity.CENTER);
                                row.addView(tv1);
                                row.addView(tv2);
                                row.addView(tv3);
                                row.addView(tv4);
                                dialogueTable.addView(row);
                            }
                            ll.addView(dialogueTable);
                            dialog.show();
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(true);
                            //isDialogue=true;
                        } catch (Exception e) {
                            Log.d("", e.toString());
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.d("",e.toString());
            Log.d("",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_grower_name, tv_father_name, tv_village_code_value, tv_grower_code, tv_grower_mobile, tv_village_name_value,
                tvCount,tvtotalmaterial,tv_materialamount;
        ImageView ivmateriallist,ivCloud;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvtotalmaterial = itemView.findViewById(R.id.tvtotalmaterial);
            tv_materialamount = itemView.findViewById(R.id.tv_materialamount);
            ivmateriallist = itemView.findViewById(R.id.ivmateriallist);
            tv_grower_name = itemView.findViewById(R.id.tv_grower_name);
            tv_father_name = itemView.findViewById(R.id.tv_father_name);
            tv_village_code_value = itemView.findViewById(R.id.tv_village_code_value);
            tv_grower_code = itemView.findViewById(R.id.tv_grower_code);
            tv_grower_mobile = itemView.findViewById(R.id.tvGrowerMobile);
            tv_village_name_value = itemView.findViewById(R.id.tv_village_name);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivCloud = itemView.findViewById(R.id.ivCloud);

        }
    }

}
