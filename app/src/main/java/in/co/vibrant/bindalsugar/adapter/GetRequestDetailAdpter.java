package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.GetRequestDetailModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class GetRequestDetailAdpter extends RecyclerView.Adapter<GetRequestDetailAdpter.MyHolder> {

    private Context context;
    List<GetRequestDetailModel> arrayList;
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;


    public GetRequestDetailAdpter(Context context, List<GetRequestDetailModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_reject_item_list, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        try {
            if (position == 0) {
                holder.all_heading.setVisibility(View.GONE);
                holder.all_details.setVisibility(View.GONE);
                holder.all_request.setVisibility(View.GONE);
                holder.main_layout.setVisibility(View.GONE);

            } else {
                final int pos = position - 1;

                holder.SuperVisorName.setText("" + arrayList.get(pos).getSupname());
                holder.Reason.setText("Reason         :   " + arrayList.get(pos).getReason());
                holder.Desription.setText("Description  :   " + arrayList.get(pos).getRDesc());
                holder.From_Date.setText("From Date    :   " + arrayList.get(pos).getRsdate());
                holder.To_Date.setText("To Date         :   " + arrayList.get(pos).getVsdate());


                dbh = new DBHelper(context);
                userDetailsModels = new ArrayList<>();
                userDetailsModels = dbh.getUserDetailsModel();
                holder.Approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.remarks_editText.getText().toString().isEmpty()) {
                            new AlertDialogManager().showToast((Activity) context, "Please Enter a Remark");

                        } else {
                            showConfirmationDialog1(pos, holder);

                        }


                    }
                });
                holder.Reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.remarks_editText.getText().toString().isEmpty()) {
                            new AlertDialogManager().showToast((Activity) context, "Please Enter a Remark");
                        } else {
                            showConfirmationDialog(pos, holder);

                        }

                    }
                });

            }
        } catch (Exception e) {

        }


    }


    private void showConfirmationDialog(final int pos, final MyHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to Reject this ");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked the "Yes" button, proceed with the deletion


            new RequestApproval().execute("2", "" + arrayList.get(pos).getId(), holder.remarks_editText.getText().toString());
            arrayList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, arrayList.size() + 1);
            holder.all_heading.setVisibility(View.GONE);
            holder.all_details.setVisibility(View.GONE);
            holder.all_request.setVisibility(View.GONE);
            holder.main_layout.setVisibility(View.GONE);
            holder.remarks_editText.setText("");

        });
        builder.setNegativeButton("No", null); // No action needed for "No" button

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showConfirmationDialog1(final int pos, final MyHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to Approve this ");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked the "Yes" button, proceed with the deletion

            new RequestApproval().execute("1", "" + arrayList.get(pos).getId(), holder.remarks_editText.getText().toString());
            arrayList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, arrayList.size() + 1);
            holder.all_heading.setVisibility(View.GONE);
            holder.all_details.setVisibility(View.GONE);
            holder.all_request.setVisibility(View.GONE);
            holder.main_layout.setVisibility(View.GONE);
            holder.remarks_editText.setText("");

        });
        builder.setNegativeButton("No", null); // No action needed for "No" button

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class RequestApproval extends AsyncTask<String, Void, Void> {
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
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/RequestApproval";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("RequestID", params[1]));
                entity.add(new BasicNameValuePair("Status", params[0]));
                entity.add(new BasicNameValuePair("EmpCode", "" + userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIS", params[2]));
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
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout Approved, Reject;
        TextView SuperVisorName, Reason, Desription, From_Date, To_Date;
        LinearLayout all_heading, all_details;
        RelativeLayout all_request;
        CardView main_layout;
        EditText remarks_editText;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Approved = itemView.findViewById(R.id.Approved);
            Reject = itemView.findViewById(R.id.Reject);
            SuperVisorName = itemView.findViewById(R.id.SuperVisorName);
            Reason = itemView.findViewById(R.id.Reason);
            Desription = itemView.findViewById(R.id.Desription);
            From_Date = itemView.findViewById(R.id.From_Date);
            To_Date = itemView.findViewById(R.id.To_Date);
            all_heading = itemView.findViewById(R.id.all_heading);
            all_details = itemView.findViewById(R.id.all_details);
            all_request = itemView.findViewById(R.id.all_request);
            main_layout = itemView.findViewById(R.id.main_layout);
            remarks_editText = itemView.findViewById(R.id.remarks_editText);

        }


    }

}



