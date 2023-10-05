package in.co.vibrant.bindalsugar.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.NotificationAdapter;
import in.co.vibrant.bindalsugar.model.NotificationModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class NotificationActivity extends AppCompatActivity {

    AlertDialog dialog;
    Toolbar toolbar;
    Context context;
    List<UserDetailsModel> loginUserDetailsList;
    List<NotificationModel> notificationModelList;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notification");
        toolbar.setTitle("Notification");
        context = NotificationActivity.this;
        dbh = new DBHelper(context);
        notificationModelList = new ArrayList<>();
        loginUserDetailsList = dbh.getUserDetailsModel();
        notificationModelList = dbh.getNotificationModel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new GetNotificationData().execute();

    }

    private void showData() {
        try {

            if (notificationModelList.size() > 0)
                notificationModelList.clear();
            notificationModelList = dbh.getNotificationModel();
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            NotificationAdapter myServiceAdapter = new NotificationAdapter(context, notificationModelList);
            recyclerView.setAdapter(myServiceAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setMessage("Are you sure you remove all notification");
        alertDialog.setPositiveButton("Yes Remove it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dbh.deleteNotificationModel("");
                        showData();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private class GetNotificationData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(NotificationActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(NotificationActivity.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetNotificationData);
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("u_code", loginUserDetailsList.get(0).getCode());
                request1.addProperty("DIVN", loginUserDetailsList.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call GetNotificationData{IMEINO=f6a7d6c3b291e7c6; u_code=10010; DIVN=GS; Season=2022-2023; }
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetNotificationData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetNotificationDataResult").toString();
                }

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                Gson gson = new Gson();
                dbh.deleteNotificationModel("");
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    new DBHelper(getApplicationContext()).deleteNotificationModel("");
                    dbh.deleteNotificationModel("");
                    ;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String jsonObj = object.getString("NEWJASONSTRING");
                        jsonObj = jsonObj.replace("\\r\\n", "");
                        jsonObj = jsonObj.replace("\\", "");
                        /*jsonObj=jsonObj.replace("="," : ");
                        jsonObj=jsonObj.replace("{","{\"");
                        jsonObj=jsonObj.replace(",","\",\"");
                        jsonObj=jsonObj.replace(" : ","\" : \"");
                        jsonObj=jsonObj.replace("\"{","{");
                        jsonObj=jsonObj.replace("}\"","\"}");
                        jsonObj=jsonObj.replace("}}","\"}}");*/
                        JSONObject jsonObject11 = new JSONObject(jsonObj);
                        JSONObject jsonObject1 = jsonObject11.getJSONObject("notification");
                        //System.out.println(str);
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setTitle(jsonObject1.getString("title"));
                        notificationModel.setMessage(jsonObject1.getString("tag"));
                        //  notificationModel.setSeen("Yes");
                        notificationModel.setSeen(object.getInt("VIEWFLG"));
                        notificationModel.setDateTime(object.getString("CRDATE"));
                        notificationModel.setSenduser(object.getInt("SENDUSER"));

                        //dbh.insertNotificationModel(notificationModel);
                        try {
                            JSONObject js = new JSONObject(jsonObject1.getString("tag"));
                            if (js.getString("intent").equalsIgnoreCase("BROADCAST")) {
                                if (js.getString("image_url").endsWith(".mp4")) {
                                    dbh.insertNotificationModel(notificationModel);
                                } else if (js.getString("image_url").endsWith(".mp3")) {
                                    dbh.insertNotificationModel(notificationModel);
                                } else if (js.getString("image_url").endsWith(".jpg")) {
                                    dbh.insertNotificationModel(notificationModel);
                                } else if (js.getString("body").length() > 0) {
                                    dbh.insertNotificationModel(notificationModel);
                                }
                            } else {
                                dbh.insertNotificationModel(notificationModel);
                            }
                        } catch (Exception e) {
                            //new AlertDialogManager().RedDialog(context,"Error:" + e.toString());

                        }
                       /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date today = Calendar.getInstance().getTime();
                        String currDate = dateFormat.format(today);
                        notificationModel.setDateTime(currDate);*/

                    }
                    showData();
                }
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(NotificationActivity.this, "Error:" + e.toString());
            }
        }
    }

}