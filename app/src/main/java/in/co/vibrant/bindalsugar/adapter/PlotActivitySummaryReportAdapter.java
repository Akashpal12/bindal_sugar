package in.co.vibrant.bindalsugar.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PlotActivitySummaryReportModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.view.supervisor.PlotActivityGrowerWiseDetail;


public class PlotActivitySummaryReportAdapter extends RecyclerView.Adapter<PlotActivitySummaryReportAdapter.MyHolder> {

    private Context context;
    List<PlotActivitySummaryReportModel> arrayList;
    AlertDialog Alertdialog;
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;


    public PlotActivitySummaryReportAdapter(Context context, List<PlotActivitySummaryReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.plot_activity_report_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        if (position<arrayList.size()){
            //SrNo,plot_vill_Name,tottal_plots,total_area,plots_covered,percet_coverage,area_coverage,coverage_area;
            holder.SrNo.setText(String.valueOf(position + 1));
            holder.plot_vill_Name.setText("" + arrayList.get(position).getPLVILL() + " / " + arrayList.get(position).getVILLNAME());
            holder.plots_onDate.setText("" + arrayList.get(position).getOD_PLOTS());
            holder.area_onDate.setText("" + arrayList.get(position).getOD_AREA());
            holder.plots_todate.setText("" + arrayList.get(position).getTD_PLOTS());
            holder.area_todate.setText("" + arrayList.get(position).getTD_AREA());

            dbh = new DBHelper(context);
            userDetailsModels = new ArrayList<>();
            userDetailsModels = dbh.getUserDetailsModel();

            holder.details.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlotActivityGrowerWiseDetail.class);
                intent.putExtra("V_CODE",arrayList.get(position).getPLVILL());
                intent.putExtra("F_DATE",arrayList.get(position).getFROM_DATE());
                intent.putExtra("T_DATE",arrayList.get(position).getTO_DATE());
                intent.putExtra("S_CODE",arrayList.get(position).getS_CODE());
                intent.putExtra("V_NAME",arrayList.get(position).getVILLNAME());
                context.startActivity(intent);
                //showBottomSheetDialog(position);
                // new PlotActivityGrowerWiseDetail().execute( arrayList.get(position).getPLVILL(),arrayList.get(position).getFROM_DATE(),arrayList.get(position).getTO_DATE());

            });

        }else {
            holder.SrNo.setText("");
            holder.plot_vill_Name.setText("           Total ");

            holder.my_image.setVisibility(View.GONE);

            double plotsonDates = 0, areaonDates = 0, plotstodates = 0, areatodates = 0;
            for (PlotActivitySummaryReportModel model : arrayList) {
                plotsonDates += Double.parseDouble(model.getOD_PLOTS());
                areaonDates += Double.parseDouble(model.getOD_AREA());
                plotstodates += Double.parseDouble(model.getTD_PLOTS());
                areatodates += Double.parseDouble(model.getTD_AREA());
            }
            holder.plots_onDate.setText(new DecimalFormat("#0").format(plotsonDates));
            holder.area_onDate.setText(new DecimalFormat("#0.0").format(areaonDates));
            holder.plots_todate.setText(new DecimalFormat("#0").format(plotstodates));
            holder.area_todate.setText(new DecimalFormat("#0.000").format(areatodates));

            holder.plot_vill_Name.setTypeface(Typeface.DEFAULT_BOLD);
            holder.plots_onDate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.area_onDate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.plots_todate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.area_todate.setTypeface(Typeface.DEFAULT_BOLD);

        }




    }


    private void showBottomSheetDialog(final int pos) {
        LinearLayout ll;
        TableLayout dialogueTable;

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheets);

        ImageView imageView = bottomSheetDialog.findViewById(R.id.cross);
        imageView.setOnClickListener(v -> {

            bottomSheetDialog.cancel();
        });



        bottomSheetDialog.show();

    }

    public void ShowMaterialList() {

    }


    private class PlotActivityGrowerWiseDetail1 extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Bindal Sugar",
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/PlotActivityGrowerWiseDetail";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("plvlc", params[0]));
                entity.add(new BasicNameValuePair("Fdate", params[1]));
                entity.add(new BasicNameValuePair("Tdate", params[2]));
                entity.add(new BasicNameValuePair("divn", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("seas", "2023-2024"));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                new AlertDialogManager().GreenDialog(context, jsonObject.getString("DATA"));


            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            }
        }
    }

    private class PlotActivityActivityWiseDetail extends AsyncTask<String, Void, Void> {
        String Content = null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Bindal Sugar",
                    "Please wait", true);
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/PlotActivityActivityWiseDetail";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("RequestID", params[1]));
                entity.add(new BasicNameValuePair("Status", params[0]));
                entity.add(new BasicNameValuePair("EmpCode", "" + userDetailsModels.get(0).getCode()));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("SecurityException", e.getMessage());
                Content = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                Content = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                new AlertDialogManager().GreenDialog(context, jsonObject.getString("MSG"));


            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView SrNo, plot_vill_Name, plots_onDate, area_onDate, plots_todate, area_todate;
        LinearLayout details;
        ImageView my_image;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            SrNo = itemView.findViewById(R.id.SrNo);
            plot_vill_Name = itemView.findViewById(R.id.plot_vill_Name);
            plots_onDate = itemView.findViewById(R.id.plots_onDate);
            area_onDate = itemView.findViewById(R.id.area_onDate);
            plots_todate = itemView.findViewById(R.id.plots_todate);
            area_todate = itemView.findViewById(R.id.area_todate);
            details = itemView.findViewById(R.id.details);
            my_image = itemView.findViewById(R.id.my_image);


        }


    }


}
