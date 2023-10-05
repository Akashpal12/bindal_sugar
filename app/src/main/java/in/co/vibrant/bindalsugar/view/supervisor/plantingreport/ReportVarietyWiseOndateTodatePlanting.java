package in.co.vibrant.bindalsugar.view.supervisor.plantingreport;


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
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class ReportVarietyWiseOndateTodatePlanting extends AppCompatActivity {

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
        context= ReportVarietyWiseOndateTodatePlanting.this;
        dbh=new DBHelper(context);
        setContentView(R.layout.report_table_with_filter);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Variety Wise Ondate and Todate Planting Report");
            toolbar.setTitle("Variety Wise Ondate and Todate Planting Report");
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
            typeOfPlantingList = dbh.getMasterDropDown("13");

            List<PlantingModel> plantingModelList=dbh.getPlantingModel("No","","","","");
            if(plantingModelList.size()>0)
            {
                TextView heading=findViewById(R.id.heading);
                heading.setText("Warning:- Your "+plantingModelList.size()+" planting data are pending to transfer for server\nचेतावनी:- आपका "+plantingModelList.size ()+" प्लांटिंग डाटा सर्वर पर नहीं गया है इसलिए आपका प्लांटिंग रिपोर्ट इन्कम्प्लीट हो सकता है।");
                new AlertDialogManager().RedDialog(context,"Warning:- Your "+plantingModelList.size()+" planting data are pending to transfer for server\nचेतावनी:- आपका "+plantingModelList.size ()+" प्लांटिंग डाटा सर्वर पर नहीं गया है इसलिए आपका प्लांटिंग रिपोर्ट इन्कम्प्लीट हो सकता है।");
                heading.setVisibility(View.GONE);
            }


            fixedHeaderTableLayout = findViewById(R.id.FixedHeaderTableLayout);
            pgsBar = findViewById(R.id.pBar);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            currentDate = dateFormat.format(today);
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

        final EditText fromdate=mView.findViewById(R.id.fromdate_select);
        final EditText todate=mView.findViewById(R.id.todate_selects);
        final Spinner cane_type=mView.findViewById(R.id.cane_type);


        ArrayList<String> datatypeOfPlanting = new ArrayList<String>();
        for (int i = 0; i < typeOfPlantingList.size(); i++) {
            datatypeOfPlanting.add(typeOfPlantingList.get(i).getName());
        }
        ArrayAdapter<String> adaptertypeOfPlanting = new ArrayAdapter<String>(ReportVarietyWiseOndateTodatePlanting.this,
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

        fromdate.setText(currentDate);
        fromdate.setInputType(InputType.TYPE_NULL);
        fromdate.setTextIsSelectable(true);
        fromdate.setFocusable(false);
        fromdate.setOnClickListener(new View.OnClickListener() {
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
                        fromdate.setText(year+"-"+temmonth+"-"+temDate );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        todate.setText(currentDate);
        todate.setInputType(InputType.TYPE_NULL);
        todate.setTextIsSelectable(true);
        todate.setFocusable(false);
        todate.setOnClickListener(new View.OnClickListener() {
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
                        todate.setText(year+"-"+temmonth+"-"+temDate );
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
                    new GetData().execute(typeOfPlantingList.get(defaultPlantingType).getCode(),fromdate.getText().toString(),todate.getText().toString());
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

        private final SoftReference<ReportVarietyWiseOndateTodatePlanting> activityReference;
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

            activityReference = new SoftReference<>(ReportVarietyWiseOndateTodatePlanting.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // get a reference to the activity if it is still there
            ReportVarietyWiseOndateTodatePlanting activity = activityReference.get();
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
            ReportVarietyWiseOndateTodatePlanting activity = activityReference.get();
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
            rowHeaderTable = new FixedHeaderSubTableLayout(mContext);
            double seedtarget=0.0,gn=0.0,ONDATESEEDQTY=0.0,ONDATEGROW=0.0,TODATESEEDQTY=0.0,TODATEGROW=0.0;
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
                Typeface typefacetotalToDateArea = ResourcesCompat.getFont(context, R.font.roboto_black);
                textView.setTypeface(typefacetotalToDateArea);
                textView.setTextColor(getResources().getColor(R.color.black));
                tableRowSrNoData.addView(textView);
                rowHeaderTable.addView(tableRowSrNoData);

                FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);

                TextView textView1 = new TextView(mContext);
                textView1.setGravity(Gravity.LEFT);
                textView1.setText(jsonObject.getString("VRCODE")+"/"+jsonObject.getString("VRNAME"));
                textView1.setBackgroundResource(R.drawable.list_border);
                textView1.setPadding(5 ,5,5,5);
                textView1.setTextSize(textSize);
                Typeface typefaceVar = ResourcesCompat.getFont(context, R.font.roboto_black);
                textView1.setTypeface(typefaceVar);
                textView1.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView1);

                TextView textView2 = new TextView(mContext);
                textView2.setGravity(Gravity.CENTER);
                textView2.setText(jsonObject.getString("TARGETQTY"));
                textView2.setBackgroundResource(R.drawable.list_border);
                textView2.setPadding(5 ,5,5,5);
                textView2.setTextSize(textSize);
                textView2.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView2);

                TextView textView3 = new TextView(mContext);
                textView3.setGravity(Gravity.CENTER);
                textView3.setText(jsonObject.getString("TARGETGROW"));
                textView3.setBackgroundResource(R.drawable.list_border);
                textView3.setPadding(5 ,5,5,5);
                textView3.setTextSize(textSize);
                textView3.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView3);

                TextView textView4 = new TextView(mContext);
                textView4.setGravity(Gravity.CENTER);
                textView4.setText(jsonObject.getString("ONDATESEEDQTY"));
                textView4.setBackgroundResource(R.drawable.list_border);
                textView4.setPadding(5 ,5,5,5);
                textView4.setTextSize(textSize);
                textView4.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView4);

                TextView textView5 = new TextView(mContext);
                textView5.setGravity(Gravity.CENTER);
                textView5.setText(jsonObject.getString("ONDATEGROW"));
                textView5.setBackgroundResource(R.drawable.list_border);
                textView5.setPadding(5 ,5,5,5);
                textView5.setTextSize(textSize);
                textView5.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView5);

                TextView textView6 = new TextView(mContext);
                textView6.setGravity(Gravity.CENTER);
                textView6.setText(jsonObject.getString("TODATESEEDQTY"));
                textView6.setBackgroundResource(R.drawable.list_border);
                textView6.setPadding(5 ,5,5,5);
                textView6.setTextSize(textSize);
                textView6.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView6);

                TextView textView7 = new TextView(mContext);
                textView7.setGravity(Gravity.CENTER);
                textView7.setText(jsonObject.getString("TODATEGROW"));
                textView7.setBackgroundResource(R.drawable.list_border);
                textView7.setPadding(5 ,5,5,5);
                textView7.setTextSize(textSize);
                textView7.setTextColor(getResources().getColor(R.color.black));
                tableRowData.addView(textView7);

                mainTable.addView(tableRowData);

                seedtarget +=jsonObject.getDouble("TARGETQTY");
                gn +=jsonObject.getDouble("TARGETGROW");
                ONDATESEEDQTY +=jsonObject.getDouble("ONDATESEEDQTY");
                ONDATEGROW +=jsonObject.getDouble("ONDATEGROW");
                TODATESEEDQTY +=jsonObject.getDouble("TODATESEEDQTY");
                TODATEGROW +=jsonObject.getDouble("TODATEGROW");
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
            textViewSub1.setGravity(Gravity.CENTER);
            textViewSub1.setText(new DecimalFormat("0.00").format(seedtarget));
            textViewSub1.setBackgroundResource(R.drawable.list_border_head);
            textViewSub1.setPadding(5 ,5,5,5);
            textViewSub1.setTextSize(textSize);
            textViewSub1.setTextColor(getResources().getColor(R.color.black));
            textViewSub1.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub1);

            TextView textViewSub2 = new TextView(mContext);
            textViewSub2.setGravity(Gravity.CENTER);
            textViewSub2.setText(new DecimalFormat("0.00").format(gn));
            textViewSub2.setBackgroundResource(R.drawable.list_border_head);
            textViewSub2.setPadding(5 ,5,5,5);
            textViewSub2.setTextSize(textSize);
            textViewSub2.setTextColor(getResources().getColor(R.color.black));
            textViewSub2.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub2);

            TextView textViewSub3 = new TextView(mContext);
            textViewSub3.setGravity(Gravity.CENTER);
            textViewSub3.setText(new DecimalFormat("0.00").format(ONDATESEEDQTY));
            textViewSub3.setBackgroundResource(R.drawable.list_border_head);
            textViewSub3.setPadding(5 ,5,5,5);
            textViewSub3.setTextSize(textSize);
            textViewSub3.setTextColor(getResources().getColor(R.color.black));
            textViewSub3.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub3);



            TextView textViewSub4 = new TextView(mContext);
            textViewSub4.setGravity(Gravity.CENTER);
            textViewSub4.setText(new DecimalFormat("0.00").format(ONDATEGROW));
            textViewSub4.setBackgroundResource(R.drawable.list_border_head);
            textViewSub4.setPadding(5 ,5,5,5);
            textViewSub4.setTextSize(textSize);
            textViewSub4.setTextColor(getResources().getColor(R.color.black));
            textViewSub4.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub4);


            TextView textViewSub5 = new TextView(mContext);
            textViewSub5.setGravity(Gravity.CENTER);
            textViewSub5.setText(new DecimalFormat("0.00").format(TODATESEEDQTY));
            textViewSub5.setBackgroundResource(R.drawable.list_border_head);
            textViewSub5.setPadding(5 ,5,5,5);
            textViewSub5.setTextSize(textSize);
            textViewSub5.setTextColor(getResources().getColor(R.color.black));
            textViewSub5.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub5);



            TextView textViewSub6 = new TextView(mContext);
            textViewSub6.setGravity(Gravity.CENTER);
            textViewSub6.setText(new DecimalFormat("0.00").format(TODATEGROW));
            textViewSub6.setBackgroundResource(R.drawable.list_border_head);
            textViewSub6.setPadding(5 ,5,5,5);
            textViewSub6.setTextSize(textSize);
            textViewSub6.setTextColor(getResources().getColor(R.color.black));
            textViewSub6.setTypeface(null, Typeface.BOLD);
            tableRowDataSubData.addView(textViewSub6);
            mainTable.addView(tableRowDataSubData);



            //----------------------------Grand Total End-------------------------------------------




            rowHeaderTable.setBackgroundResource(R.drawable.right_border);
            columnHeaderTable = new FixedHeaderSubTableLayout(mContext);

            // 2 x 5 in size
            FixedHeaderTableRow tableRowData1 = new FixedHeaderTableRow(mContext);
            TextView textView1 = new TextView(mContext);
            textView1.setGravity(Gravity.CENTER);
            textView1.setText("");
            Typeface typefaceAcghghh = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView1.setTypeface(typefaceAcghghh);
            //textView1.setBackgroundResource(R.drawable.list_border);
            textView1.setPadding(5 ,5,5,5);
            textView1.setTextSize(textSize*1.25f);
            textView1.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView1);

            TextView textView2 = new TextView(mContext);
            textView2.setGravity(Gravity.RIGHT);
            textView2.setText("Tar");
            Typeface typefaceTar = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView2.setTypeface(typefaceTar);
            textView2.setBackgroundResource(R.drawable.list_border_right_blank);
            textView2.setPadding(5 ,5,5,5);
            textView2.setTextSize(textSize*1.25f);
            textView2.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView2);

            TextView textView3 = new TextView(mContext);
            textView3.setGravity(Gravity.LEFT);
            textView3.setText("get");
            Typeface typefaceget = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView3.setTypeface(typefaceget);
            textView3.setBackgroundResource(R.drawable.list_border_left_blank);
            textView3.setPadding(5 ,5,5,5);
            textView3.setTextSize(textSize*1.25f);
            textView3.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView3);

            TextView textView41 = new TextView(mContext);
            textView41.setGravity(Gravity.RIGHT);
            textView41.setText("");
            Typeface typefacegett = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView41.setTypeface(typefacegett);
            textView41.setBackgroundResource(R.drawable.list_border_right_blank);
            textView41.setPadding(5 ,5,5,5);
            textView41.setTextSize(textSize*1.25f);
            textView41.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView41);

            TextView textView4 = new TextView(mContext);
            textView4.setGravity(Gravity.RIGHT);
            textView4.setText("Achi");
            Typeface typefaceAchi = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView4.setTypeface(typefaceAchi);
            textView4.setBackgroundResource(R.drawable.list_border_right_blank);
            textView4.setPadding(5 ,5,5,5);
            textView4.setTextSize(textSize*1.25f);
            textView4.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView4);

            TextView textView5 = new TextView(mContext);
            textView5.setGravity(Gravity.LEFT);
            textView5.setText("eved");
            Typeface typefaceeved = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView5.setTypeface(typefaceeved);
            textView5.setBackgroundResource(R.drawable.list_border_left_blank);
            textView5.setPadding(5 ,5,5,5);
            textView5.setTextSize(textSize*1.25f);
            textView5.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView5);

            TextView textView6 = new TextView(mContext);
            textView6.setGravity(Gravity.LEFT);
            textView6.setText("");
            Typeface typefaceeve = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView6.setTypeface(typefaceeve);
            textView6.setBackgroundResource(R.drawable.list_border_left_blank);
            textView6.setPadding(5 ,5,5,5);
            textView6.setTextSize(textSize*1.25f);
            textView6.setTextColor(getResources().getColor(R.color.black));
            tableRowData1.addView(textView6);

            columnHeaderTable.addView(tableRowData1);

            FixedHeaderTableRow tableRowData111 = new FixedHeaderTableRow(mContext);
            TextView textView111 = new TextView(mContext);
            textView111.setGravity(Gravity.CENTER);
            textView111.setText("Variety Name");
            Typeface typefaceVarietyName = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView111.setTypeface(typefaceVarietyName);
            //textView1.setBackgroundResource(R.drawable.list_border);
            textView111.setPadding(5 ,5,5,5);
            textView111.setTextSize(textSize*1.25f);
            textView111.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView111);

            TextView textView112 = new TextView(mContext);
            textView112.setGravity(Gravity.RIGHT);
            textView112.setText("");
            Typeface typefaceAci = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView112.setTypeface(typefaceAci);
            textView112.setBackgroundResource(R.drawable.list_border_right_blank);
            textView112.setPadding(5 ,5,5,5);
            textView112.setTextSize(textSize*1.25f);
            textView112.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView112);

            TextView textView113 = new TextView(mContext);
            textView113.setGravity(Gravity.CENTER);
            textView113.setText("");
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView113.setTypeface(typeface);
            textView113.setBackgroundResource(R.drawable.list_border_left_blank);
            textView113.setPadding(5 ,5,5,5);
            textView113.setTextSize(textSize*1.25f);
            textView113.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView113);

            TextView textView114 = new TextView(mContext);
            textView114.setGravity(Gravity.RIGHT);
            textView114.setText("On");
            Typeface typefaceOn = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView114.setTypeface(typefaceOn);
            textView114.setBackgroundResource(R.drawable.list_border_right_blank);
            textView114.setPadding(5 ,5,5,5);
            textView114.setTextSize(textSize*1.25f);
            textView114.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView114);

            TextView textView115 = new TextView(mContext);
            textView115.setGravity(Gravity.LEFT);
            textView115.setText("Date");
            Typeface typefaceDate = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView115.setTypeface(typefaceDate);
            textView115.setBackgroundResource(R.drawable.list_border_left_blank);
            textView115.setPadding(5 ,5,5,5);
            textView115.setTextSize(textSize*1.25f);
            textView115.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView115);

            TextView textView116 = new TextView(mContext);
            textView116.setGravity(Gravity.RIGHT);
            textView116.setText("To");
            Typeface typefaceTo = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView116.setTypeface(typefaceTo);
            textView116.setBackgroundResource(R.drawable.list_border_right_blank);
            textView116.setPadding(5 ,5,5,5);
            textView116.setTextSize(textSize*1.25f);
            textView116.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView116);

            TextView textView117 = new TextView(mContext);
            textView117.setGravity(Gravity.LEFT);
            Typeface typefacedate = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView117.setTypeface(typefacedate);
            textView117.setText("Date");
            textView117.setBackgroundResource(R.drawable.list_border_left_blank);
            textView117.setPadding(5 ,5,5,5);
            textView117.setTextSize(textSize*1.25f);
            textView117.setTextColor(getResources().getColor(R.color.black));
            tableRowData111.addView(textView117);

            columnHeaderTable.addView(tableRowData111);
            columnHeaderTable.setBackgroundResource(R.drawable.bottom_border);

            FixedHeaderTableRow tableRowData11 = new FixedHeaderTableRow(mContext);
            TextView textView11 = new TextView(mContext);
            textView11.setGravity(Gravity.CENTER);
            textView11.setText("");
            //textView11.setBackgroundResource(R.drawable.list_border);
            textView11.setPadding(5 ,5,5,5);
            textView11.setTextSize(textSize*1.25f);
            textView11.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView11);

            TextView textView12 = new TextView(mContext);
            textView12.setGravity(Gravity.CENTER);
            textView12.setText("Seed (Qntl.)");
            Typeface typefaceSeed = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView12.setTypeface(typefaceSeed);
            textView12.setBackgroundResource(R.drawable.list_border);
            textView12.setPadding(5 ,5,5,5);
            textView12.setTextSize(textSize*1.25f);
            textView12.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView12);

            TextView textView13 = new TextView(mContext);
            textView13.setGravity(Gravity.CENTER);
            Typeface typefaceofGrower = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView13.setTypeface(typefaceofGrower);
            textView13.setText("No of Grower");
            textView13.setBackgroundResource(R.drawable.list_border);
            textView13.setPadding(5 ,5,5,5);
            textView13.setTextSize(textSize*1.25f);
            textView13.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView13);

            TextView textView14 = new TextView(mContext);
            textView14.setGravity(Gravity.CENTER);
            Typeface typefaceQntl = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView14.setTypeface(typefaceQntl);
            textView14.setText("Seed (Qntl.)");
            textView14.setBackgroundResource(R.drawable.list_border);
            textView14.setPadding(5 ,5,5,5);
            textView14.setTextSize(textSize*1.25f);
            textView14.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView14);

            TextView textView15 = new TextView(mContext);
            textView15.setGravity(Gravity.CENTER);
            textView15.setText("No of Grower");
            Typeface typefaceNo = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView15.setTypeface(typefaceNo);
            textView15.setBackgroundResource(R.drawable.list_border);
            textView15.setPadding(5 ,5,5,5);
            textView15.setTextSize(textSize*1.25f);
            textView15.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView15);

            TextView textView16 = new TextView(mContext);
            textView16.setGravity(Gravity.CENTER);
            textView16.setText("Seed (Qntl.)");
            Typeface typefacSeed  = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView16.setTypeface(typefacSeed);
            textView16.setBackgroundResource(R.drawable.list_border);
            textView16.setPadding(5 ,5,5,5);
            textView16.setTextSize(textSize*1.25f);
            textView16.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView16);

            TextView textView17 = new TextView(mContext);
            textView17.setGravity(Gravity.CENTER);
            Typeface typefaceGrower = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView17.setTypeface(typefaceGrower);
            textView17.setText("No of Grower");
            textView17.setBackgroundResource(R.drawable.list_border);
            textView17.setPadding(5 ,5,5,5);
            textView17.setTextSize(textSize*1.25f);
            textView17.setTextColor(getResources().getColor(R.color.black));
            tableRowData11.addView(textView17);

            columnHeaderTable.addView(tableRowData11);
            columnHeaderTable.setBackgroundResource(R.drawable.bottom_border);



            cornerTable = new FixedHeaderSubTableLayout(mContext);
            // 2 x 1 in size
            FixedHeaderTableRow tableRowSrNoHeadData = new FixedHeaderTableRow(mContext);
            TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER);
            textView.setText("Sr No.");
            Typeface typefaceSrNo = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView.setTypeface(typefaceSrNo);
            //textView.setBackgroundResource(R.drawable.list_border);
            textView.setPadding(5 ,5,5,5);
            textView.setTextSize(textSize);
            textView.setTextColor(getResources().getColor(R.color.black));
            tableRowSrNoHeadData.addView(textView);
            cornerTable.addView(tableRowSrNoHeadData);

            FixedHeaderTableRow tableRowSrNoHeadData1 = new FixedHeaderTableRow(mContext);
            TextView textView1Sr = new TextView(mContext);
            textView1Sr.setGravity(Gravity.CENTER);
            textView1Sr.setText("");
            Typeface typeface1Sr = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView1Sr.setTypeface(typeface1Sr);
            //textView1Sr.setBackgroundResource(R.drawable.list_border);
            textView1Sr.setPadding(5 ,5,5,5);
            textView1Sr.setTextSize(textSize);
            textView1Sr.setTextColor(getResources().getColor(R.color.black));
            tableRowSrNoHeadData.addView(textView1Sr);
            cornerTable.addView(tableRowSrNoHeadData1);

            FixedHeaderTableRow tableRowSrNoHeadData2 = new FixedHeaderTableRow(mContext);
            TextView textView2Sr = new TextView(mContext);
            textView2Sr.setGravity(Gravity.CENTER);
            textView2Sr.setText("");
            Typeface typeface2Sr = ResourcesCompat.getFont(context, R.font.roboto_black);
            textView2Sr.setTypeface(typeface2Sr);
            //textView1Sr.setBackgroundResource(R.drawable.list_border);
            textView2Sr.setPadding(5 ,5,5,5);
            textView2Sr.setTextSize(textSize);
            textView2Sr.setTextColor(getResources().getColor(R.color.black));
            tableRowSrNoHeadData2.addView(textView2Sr);
            cornerTable.addView(tableRowSrNoHeadData2);

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
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/PLANTSUPVARIETYWISESEEDQTYACHIEVEDONTO";
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
