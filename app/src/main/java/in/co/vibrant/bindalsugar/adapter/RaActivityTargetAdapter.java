package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GrowerActivityDetailsModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.reportingauthority.RaMainActivityOpen;


public class RaActivityTargetAdapter extends RecyclerView.Adapter<RaActivityTargetAdapter.MyHolder> {

    private Context context;
    List<GrowerActivityDetailsModel> arrayList;
    AlertDialog Alertdialog;
    FusedLocationProviderClient fusedLocationClient;


    public RaActivityTargetAdapter(Context context, List<GrowerActivityDetailsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_ra_activity_target,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //,,,,,,,,,,,;
        try {
            final int pos = position + 1;
            holder.tvCount.setText("" + arrayList.get(position).getEmployeeCode());
            holder.tv_name.setText(arrayList.get(position).getEmployeeName().toUpperCase());
            if (Integer.parseInt(arrayList.get(position).getLavel()) == 0) {
                holder.ivAction.setVisibility(View.GONE);
            } else {
                holder.ivAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, RaMainActivityOpen.class);
                        intent.putExtra("emp_code",arrayList.get(position).getEmployeeCode());
                        intent.putExtra("level",arrayList.get(position).getLavel());
                        intent.putExtra("name",arrayList.get(position).getEmployeeName());
                        if(arrayList.get(position).isOpenBy())
                        {
                            ((Activity)context).finish();
                        }
                        context.startActivity(intent);
                    }
                });
            }
            holder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (arrayList.get(position).getEmployeePhone().length() == 10 && arrayList.get(position).getEmployeePhone() != "0000000000") {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + arrayList.get(position).getEmployeePhone()));
                            context.startActivity(intent);
                        } else {
                            new AlertDialogManager().RedDialog(context, "Sorry invalid phone number found :" + arrayList.get(position).getEmployeePhone());
                        }
                    } catch (SecurityException e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                    }
                }
            });

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int device_width = displaymetrics.widthPixels;
            int totalColumn = 4;
            int displayColumn = 4;
            device_width = device_width - totalColumn;
            int devicewidth = 0;
            if (device_width < 1100) {
                devicewidth = device_width / displayColumn;
            } else {
                devicewidth = device_width / displayColumn;
            }
            int lastWidth = device_width - (devicewidth * displayColumn);
            if (lastWidth <= devicewidth) {
                lastWidth = devicewidth;
            }


            TableLayout dialogueTable = new TableLayout(context);
            dialogueTable.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialogueTable.setShrinkAllColumns(false);
            dialogueTable.setStretchAllColumns(false);
            TableRow tableHeading = new TableRow(context);

            tableHeading.setBackgroundColor(Color.parseColor("#000000"));

            TextView th1 = new TextView(context);
            th1.setTextColor(Color.parseColor("#FFFFFF"));
            th1.setText("Activity");
            th1.setGravity(Gravity.LEFT);
            th1.setWidth(devicewidth);
            tableHeading.addView(th1);

            TextView th2 = new TextView(context);
            th2.setTextColor(Color.parseColor("#FFFFFF"));
            th2.setText("% Achived");
            th2.setGravity(Gravity.LEFT);
            th2.setWidth(devicewidth);
            tableHeading.addView(th2);

            TextView th3 = new TextView(context);
            th3.setTextColor(Color.parseColor("#FFFFFF"));
            th3.setText("Early/Delay");
            th3.setGravity(Gravity.LEFT);
            th3.setWidth(devicewidth);
            tableHeading.addView(th3);

            TextView th4 = new TextView(context);
            th4.setTextColor(Color.parseColor("#FFFFFF"));
            th4.setText("");
            th4.setGravity(Gravity.LEFT);
            th4.setWidth(devicewidth);
            tableHeading.addView(th4);

            dialogueTable.addView(tableHeading);
            JSONArray jsonArray = arrayList.get(position).getGetActivityData();
            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(j);

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    double perAchived = (object.getDouble("AREACOMP") / object.getDouble("AREA")) * 100;


                    TableRow row = new TableRow(context);
                    row.setId(position + j);

                    TextView tv1 = new TextView(context);
                    tv1.setText(object.getString("TARGETNAME"));
                    tv1.setWidth(devicewidth);
                    tv1.setGravity(Gravity.LEFT);

                    TextView tv2 = new TextView(context);
                    tv2.setText(decimalFormat.format(perAchived));
                    tv2.setWidth(devicewidth / 2);
                    tv2.setGravity(Gravity.LEFT);

                    TextView tv3 = new TextView(context);
                    tv3.setText(object.getString("AREADF"));
                    tv3.setWidth(devicewidth * 2);
                    tv3.setGravity(Gravity.LEFT);

                    ImageView tv4 = new ImageView(context);
                    tv4.setImageResource(R.drawable.ic_baseline_apps_24);
                    //tv4.setWidth(devicewidth);


                    if (j % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor("#DFDFDF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                    } else {
                        row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        tv1.setTextColor(Color.parseColor("#000000"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                    }
                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);

                    dialogueTable.addView(row);
                } catch (Exception e) {

                }
            }

            holder.layout_booking.addView(dialogueTable);
            //
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }



    class TargetList extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context,
                    "Please wait", "Please wait while we getting details",true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/GetDashboardTargetReporting";
                HttpClient httpClient = new DefaultHttpClient();
                DBHelper dbh=new DBHelper(context);
                List<UserDetailsModel> userDetailsModels=dbh.getUserDetailsModel();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("FactId",userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("user",params[0]));
                entity.add(new BasicNameValuePair("Level",params[1]));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                System.out.println("sdfsdsd : " + Content);
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("ok"))
                {
                    if(arrayList.size()>0)
                        arrayList.clear();

                    JSONArray jsonArraySub=jsonObject.getJSONArray("SubReportingPerson");
                    for(int i=0;i<jsonArraySub.length();i++)
                    {
                        GrowerActivityDetailsModel growerActivityDetailsModel=new GrowerActivityDetailsModel();
                        JSONObject jsonObject1=jsonArraySub.getJSONObject(i);
                        growerActivityDetailsModel.setEmployeeCode(jsonObject1.getString("EmpCode"));
                        growerActivityDetailsModel.setEmployeeName(jsonObject1.getString("EmpName"));
                        growerActivityDetailsModel.setLavel(jsonObject1.getString("Level"));
                        growerActivityDetailsModel.setGetActivityData(jsonObject1.getJSONArray("SubTarget"));
                        arrayList.add(growerActivityDetailsModel);
                    }

                }

                notifyItemRangeChanged(0, arrayList.size());
                notifyDataSetChanged();

            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout_booking;
        ImageView ivAction,ivCall;
        TextView tvCount,tv_name,grower_father,plot_number,area,mix_crop,plot_village,variety,east,west,north,south;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            layout_booking=itemView.findViewById(R.id.layout_booking);
            ivCall=itemView.findViewById(R.id.ivCall);
            ivAction=itemView.findViewById(R.id.ivAction);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
            grower_father=itemView.findViewById(R.id.grower_father);
            plot_number=itemView.findViewById(R.id.plot_number);
            area=itemView.findViewById(R.id.area);
            mix_crop=itemView.findViewById(R.id.mix_crop);
            plot_village=itemView.findViewById(R.id.plot_village);
            variety=itemView.findViewById(R.id.variety);
            east=itemView.findViewById(R.id.east);
            west=itemView.findViewById(R.id.west);
            north=itemView.findViewById(R.id.north);
            south=itemView.findViewById(R.id.south);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
