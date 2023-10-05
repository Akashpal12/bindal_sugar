package in.co.vibrant.bindalsugar.view.reportingauthority.platingreport;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.github.zardozz.FixedHeaderTableLayout.FixedHeaderSubTableLayout;
import com.github.zardozz.FixedHeaderTableLayout.FixedHeaderTableLayout;
import com.github.zardozz.FixedHeaderTableLayout.FixedHeaderTableRow;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class RaTypeOfPlanting extends AppCompatActivity {

    Context context;
    DBHelper dbh;
    List<UserDetailsModel> userDetailsModelList;
    private FixedHeaderTableLayout fixedHeaderTableLayout;
    private ProgressBar pgsBar;
    String currentDate="";
    int selectedZone=0,selectedItem=0;

    AlertDialog dialog;

    FixedHeaderSubTableLayout mainTable;
    FixedHeaderSubTableLayout columnHeaderTable;
    FixedHeaderSubTableLayout rowHeaderTable;
    FixedHeaderSubTableLayout cornerTable;
    int defaultPlantingType=0;
    List<MasterDropDown> typeOfPlantingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= RaTypeOfPlanting.this;
        dbh=new DBHelper(context);
        setContentView(R.layout.report_table_with_filter);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Type of Planting");
            toolbar.setTitle("Type of Planting");
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dbh = new DBHelper(this);
            userDetailsModelList=dbh.getUserDetailsModel();

            fixedHeaderTableLayout = findViewById(R.id.FixedHeaderTableLayout);
            pgsBar = findViewById(R.id.pBar);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            currentDate = dateFormat.format(today);

            typeOfPlantingList = dbh.getMasterDropDown("13");
            if(typeOfPlantingList.size()>0)
                new GetData().execute(typeOfPlantingList.get(0).getCode(),currentDate,currentDate);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }

        // Really this should be done in the background as generating such a big layout takes time



    }

    public void openFiler(View v)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_cane_type_date_filter, null);
        dialogbilder.setView(mView);

        final EditText fromdate_select=mView.findViewById(R.id.fromdate_select);
        final EditText todate_selects=mView.findViewById(R.id.todate_selects);
        final Spinner cane_type=mView.findViewById(R.id.cane_type);

        ArrayList<String> datatypeOfPlanting = new ArrayList<String>();
        for (int i = 0; i < typeOfPlantingList.size(); i++) {
            datatypeOfPlanting.add(typeOfPlantingList.get(i).getName());
        }
        ArrayAdapter<String> adaptertypeOfPlanting = new ArrayAdapter<String>(RaTypeOfPlanting.this,
                R.layout.list_item, datatypeOfPlanting);
        cane_type.setAdapter(adaptertypeOfPlanting);
        cane_type.setSelection(defaultPlantingType);
        cane_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                defaultPlantingType=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromdate_select.setText(currentDate);
        fromdate_select.setInputType(InputType.TYPE_NULL);
        fromdate_select.setTextIsSelectable(true);
        fromdate_select.setFocusable(false);
        fromdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        String yr=""+year;
                        //yr=yr.substring(2);
                        currentDate=year+"-"+temmonth+"-"+temDate;
                        fromdate_select.setText(year+"-"+temmonth+"-"+temDate );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        todate_selects.setText(currentDate);
        todate_selects.setInputType(InputType.TYPE_NULL);
        todate_selects.setTextIsSelectable(true);
        todate_selects.setFocusable(false);
        todate_selects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        String temDate =""+dayOfMonth;
                        if(temDate.length()==1){
                            temDate="0"+temDate;
                        }
                        String temmonth =""+(monthOfYear + 1);
                        if(temmonth.length()==1){
                            temmonth="0"+temmonth;
                        }
                        String yr=""+year;
                        //yr=yr.substring(2);
                        currentDate=year+"-"+temmonth+"-"+temDate;
                        todate_selects.setText(year+"-"+temmonth+"-"+temDate );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        dialog = dialogbilder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btn_ok=mView.findViewById(R.id.btn_ok);
        ImageView close=mView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    new GetData().execute(typeOfPlantingList.get(defaultPlantingType).getCode(),fromdate_select.getText().toString(),todate_selects.getText().toString());
                }
                catch(Exception e)
                {

                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Redraw screen calculating the new boundaries without new pan or scale
            fixedHeaderTableLayout.calculatePanScale(0,0,1f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // Redraw screen calculating the new boundaries without new pan or scale
            fixedHeaderTableLayout.calculatePanScale(0,0,1f);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GenerateTables extends AsyncTask<Void, Integer, Void> {

        private final SoftReference<RaTypeOfPlanting> activityReference;
        private final Context mContext;
        private JSONArray jsonArray;

        // only retain a soft reference to the activity
        GenerateTables(Context context, String data) {
            mContext = context;
            try{
                if(data==null)
                {
                    jsonArray=new JSONArray("[]");
                }else
                {
                    jsonArray=new JSONArray(data);
                }
            }
            catch (Exception e)
            {

            }

            activityReference = new SoftReference<>(RaTypeOfPlanting.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // get a reference to the activity if it is still there
            RaTypeOfPlanting activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            pgsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            createTable(mContext,jsonArray);
            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            // get a reference to the activity if it is still there
            RaTypeOfPlanting activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            // Setup FixHeader Table
            fixedHeaderTableLayout.removeAllViews();
            fixedHeaderTableLayout.addViews(mainTable, columnHeaderTable, rowHeaderTable, cornerTable);
            pgsBar.setVisibility(View.GONE);
        }

    }


    private static final String ALLOWED_CHARACTERS ="qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(int maxLength)
    {
        final Random random=new Random();
        final int sizeOfRandomString = random.nextInt(maxLength);
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void createTable(Context mContext,JSONArray jsonArray){
        try{
            // Create our 4 Sub Tables
            mainTable = new FixedHeaderSubTableLayout(mContext);
            // 25 x 5 in size
            float textSize = 15.0f;
            double target=0.0,ondate=0.0,todate=0.0;
            rowHeaderTable = new FixedHeaderSubTableLayout(mContext);
            for (int i = 0; i <jsonArray.length(); i++) {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                // 25 x 1 in size
                FixedHeaderTableRow tableRowSrNoData = new FixedHeaderTableRow(mContext);
                // Add some data
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                textView.setText(""+(i+1)+".");
                textView.setBackgroundResource(R.drawable.list_border);
                textView.setPadding(5 ,5,5,5);
                textView.setTextSize(textSize);
                textView.setTextColor(getResources().getColor(R.color.black));
                tableRowSrNoData.addView(textView);
                rowHeaderTable.addView(tableRowSrNoData);

                FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);

                TextView textView1 = new TextView(mContext);
                textView1.setGravity(Gravity.LEFT);
                textView1.setText(jsonObject.getString("TRCODE")+"/"+jsonObject.getString("TRNAME"));
                textView1.setBackgroundResource(R.drawable.list_border);
                textView1.setPadding(5 ,5,5,5);
                textView1.setTextSize(textSize);
                Typeface typefaced9 = ResourcesCompat.getFont(context, R.font.roboto_black);
                textView1.setTypeface(typefaced9);

                textView1.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView1);

                TextView textView2 = new TextView(mContext);
                textView2.setGravity(Gravity.CENTER);
                textView2.setText(jsonObject.getString("TARGET"));
                textView2.setBackgroundResource(R.drawable.list_border);
                textView2.setPadding(5 ,5,5,5);
                textView2.setTextSize(textSize);
                Typeface typefaced5 = ResourcesCompat.getFont(context, R.font.roboto_black);
                textView2.setTypeface(typefaced5);
                textView2.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView2);

                TextView textView3 = new TextView(mContext);
                textView3.setGravity(Gravity.CENTER);
                textView3.setText(jsonObject.getString("ACHIVEMENT"));
                textView3.setBackgroundResource(R.drawable.list_border);
                textView3.setPadding(5 ,5,5,5);
                textView3.setTextSize(textSize);
                Typeface typefaced4 = ResourcesCompat.getFont(context, R.font.roboto_black);
                textView3.setTypeface(typefaced4);
                textView3.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView3);
                target +=jsonObject.getDouble("TARGET");
                ondate +=jsonObject.getDouble("ACHIVEMENT");
                mainTable.addView(tableRowData);
            }


            //-----------------------------Grand Total Start----------------------------------------

            FixedHeaderTableRow tableRowSrNoDataSubTotal = new FixedHeaderTableRow(mContext);
            // Add some data
            TextView textViewSub = new TextView(mContext);
            textViewSub.setGravity(Gravity.CENTER);
            textViewSub.setText("");
            textViewSub.setBackgroundResource(R.drawable.list_border);
            textViewSub.setPadding(5 ,5,5,5);
            textViewSub.setTextSize(textSize);
            textViewSub.setTextColor(getResources().getColor(R.color.black));
            textViewSub.setTypeface(null, Typeface.BOLD);
            tableRowSrNoDataSubTotal.addView(textViewSub);
            rowHeaderTable.addView(tableRowSrNoDataSubTotal);

            FixedHeaderTableRow tableRowDataSubData = new FixedHeaderTableRow(mContext);
            TextView textViewSub0 = new TextView(mContext);
            textViewSub0.setGravity(Gravity.LEFT);
            textViewSub0.setText("Total");
            textViewSub0.setBackgroundResource(R.drawable.list_border_head);
            textViewSub0.setPadding(5 ,5,5,5);
            textViewSub0.setTextSize(textSize);
            textViewSub0.setTextColor(getResources().getColor(R.color.black));
            textViewSub0.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub0);

            TextView textViewSub1 = new TextView(mContext);
            textViewSub1.setGravity(Gravity.LEFT);
            textViewSub1.setText(new DecimalFormat("0.00").format(target));
            textViewSub1.setBackgroundResource(R.drawable.list_border_head);
            textViewSub1.setPadding(5 ,5,5,5);
            textViewSub1.setTextSize(textSize);
            textViewSub1.setTextColor(getResources().getColor(R.color.black));
            textViewSub1.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub1);

            TextView textViewSub2 = new TextView(mContext);
            textViewSub2.setGravity(Gravity.LEFT);
            textViewSub2.setText(new DecimalFormat("0.000").format(ondate));
            textViewSub2.setBackgroundResource(R.drawable.list_border_head);
            textViewSub2.setPadding(5 ,5,5,5);
            textViewSub2.setTextSize(textSize);
            textViewSub2.setTextColor(getResources().getColor(R.color.black));
            textViewSub2.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub2);

            TextView textViewSub3 = new TextView(mContext);
            textViewSub3.setGravity(Gravity.LEFT);
            textViewSub3.setText(new DecimalFormat("0.000").format(todate));
            textViewSub3.setBackgroundResource(R.drawable.list_border_head);
            textViewSub3.setPadding(5 ,5,5,5);
            textViewSub3.setTextSize(textSize);
            textViewSub3.setTextColor(getResources().getColor(R.color.black));
            textViewSub3.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub3);

            mainTable.addView(tableRowDataSubData);



            //----------------------------Grand Total End-------------------------------------------













            rowHeaderTable.setBackgroundResource(R.drawable.right_border);
            columnHeaderTable = new FixedHeaderSubTableLayout(mContext);

            // 2 x 5 in size
            FixedHeaderTableRow tableRowData1 = new FixedHeaderTableRow(mContext);
            TextView textView1 = new TextView(mContext);
            textView1.setGravity(Gravity.CENTER);
            textView1.setText("Type Of Planting");
            Typeface typefaced4 = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView1.setTypeface(typefaced4);
            //textView1.setBackgroundResource(R.drawable.list_border);
            textView1.setPadding(5 ,5,5,5);
            textView1.setTextSize(textSize*1.25f);
            textView1.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView1);

            TextView textView2 = new TextView(mContext);
            textView2.setGravity(Gravity.RIGHT);
            textView2.setText("Target");
            Typeface typefaced3 = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView2.setTypeface(typefaced3);
            textView2.setBackgroundResource(R.drawable.list_border_right_blank);
            textView2.setPadding(5 ,5,5,5);
            textView2.setTextSize(textSize*1.25f);
            textView2.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView2);

            TextView textView4 = new TextView(mContext);
            textView4.setGravity(Gravity.RIGHT);
            Typeface typefaced2 = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView4.setTypeface(typefaced2);
            textView4.setText("Achieved Area");
            textView4.setBackgroundResource(R.drawable.list_border_right_blank);
            textView4.setPadding(5 ,5,5,5);
            textView4.setTextSize(textSize*1.25f);
            textView4.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView4);

            columnHeaderTable.addView(tableRowData1);
            columnHeaderTable.setBackgroundResource(R.drawable.bottom_border);

            cornerTable = new FixedHeaderSubTableLayout(mContext);
            // 2 x 1 in size
            FixedHeaderTableRow tableRowSrNoHeadData = new FixedHeaderTableRow(mContext);
            TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER);
            textView.setText("Sr No.");
            Typeface typefaced1 = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView.setTypeface(typefaced1);
            //textView.setBackgroundResource(R.drawable.list_border);
            textView.setPadding(5 ,5,5,5);
            textView.setTextSize(textSize);
            textView.setTextColor(getResources().getColor(R.color.black));
            tableRowSrNoHeadData.addView(textView);
            cornerTable.addView(tableRowSrNoHeadData);

            cornerTable.setBackgroundResource(R.drawable.corner_border);

            fixedHeaderTableLayout.setMinScale(0.1f);
        }
        catch(Exception e){
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
    }

    private class GetData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/PLANTTYPESUPWISETARGETCDO";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN",userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("PLANTTYPE",params[0]));
                entity.add(new BasicNameValuePair("SUPCODE",userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("Fdate",params[1]));
                entity.add(new BasicNameValuePair("Tdate",params[2]));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("",message);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                JSONArray obj=new JSONArray(message);
                GenerateTables generateTables = new GenerateTables(context,message);
                generateTables.execute();
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                GenerateTables generateTables = new GenerateTables(context,null);
                generateTables.execute();
                new AlertDialogManager().RedDialog(context,message);
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
                if(dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }

}
