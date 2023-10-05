package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListRemedialActivityAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.RemedialJsonDataModel;
import in.co.vibrant.bindalsugar.model.RemedialJsonDataSaveModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;
import in.co.vibrant.bindalsugar.util.MiscleniousUtil;

public class AddRemadialActivity extends AppCompatActivity {
    private final int RC_CAMERA_REQUEST = 1001;
    ImageView image;
    JSONArray JsonData;
    Spinner remedial_spinner;
    String filename = "", pictureImagePath = "";
    EditText activityDate, description;
    Context context;
    List<UserDetailsModel> userDetailsModels;
    List<RemedialJsonDataSaveModel> remedialJsonDataSaveModelList;
    SQLiteDatabase db;
    DBHelper dbh;
    String plotTypeSelection, LAT, LNG, PLOT_SR_NO, D_CODE;
    String V_CODE, V_NAME, G_CODE, G_NAME, PLOT_VILL_CODE, D_NAME;
    Button saveBtn;
    TextView remedial_txt, remedial_des_txt;
    EditText village_code, grower_name, plot_sr_no;
    List<RemedialJsonDataModel> remedialJsonDataModelList;
    SessionConfig sessionConfig;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_remadial_log);
        toolbar();
    }


    void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Remedial");
        toolbar.setTitle("Remedial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        allIds();
    }

    void allIds() {
        try {
            context = AddRemadialActivity.this;
            dbh = new DBHelper(context);
            db = new DBHelper(this).getWritableDatabase();
            userDetailsModels = dbh.getUserDetailsModel();
            sessionConfig = new SessionConfig(context);
            image = findViewById(R.id.image);
            activityDate = findViewById(R.id.input_date);
            description = findViewById(R.id.input_description);
            saveBtn = findViewById(R.id.saveBtn);
            remedial_txt = findViewById(R.id.remedial_txt);
            remedial_des_txt = findViewById(R.id.remedial_des_txt);
            remedialJsonDataModelList = new ArrayList<>();
            remedial_spinner = findViewById(R.id.remedial_spinner);
            village_code = findViewById(R.id.village_code);
            grower_name = findViewById(R.id.grower_name);
            plot_sr_no = findViewById(R.id.plot_sr_no);
            remedialJsonDataSaveModelList = new ArrayList<>();

            intentDATA();
            intentDataSet();
            new GetDataList().execute(LAT, LNG);
            selectDate();
            allClickListeners();
            Toast.makeText(context, ""+sessionConfig.getSeason(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            new AlertDialogManager().showToast((Activity) context, "" + e.getMessage());

        }

    }

    private void intentDataSet() {
        village_code.setText(V_CODE + " / " + V_NAME);
        grower_name.setText(G_CODE + " / " + G_NAME);
        plot_sr_no.setText(PLOT_SR_NO);
        remedial_des_txt.setText(D_CODE+" / "+D_NAME);

    }

    private void intentDATA() {
        LAT = getIntent().getExtras().getString("LAT");
        LNG = getIntent().getExtras().getString("LNG");
        PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
        V_CODE = getIntent().getExtras().getString("V_CODE");
        V_NAME = getIntent().getExtras().getString("V_NAME");
        G_CODE = getIntent().getExtras().getString("G_CODE");
        G_NAME = getIntent().getExtras().getString("G_NAME");
        PLOT_VILL_CODE = getIntent().getExtras().getString("PLOT_VILL_CODE");
        D_NAME = getIntent().getExtras().getString("D_NAME");
        D_CODE = getIntent().getExtras().getString("D_CODE");


    }

    void allClickListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedataValidation();
            }
        });
    }

    public void openCam(View v) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            filename = "image" + currentDt + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/planting");
            dir.mkdirs();
            pictureImagePath = dir.getAbsolutePath() + "/" + filename;
            File file = new File(pictureImagePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void addActivity(View v) {
        CheckValidation:
        {
            try {
                if (remedial_spinner.getSelectedItemPosition() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select remedial plan");
                    break CheckValidation;
                }

                if (activityDate.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please select date");
                    break CheckValidation;
                }
                if (description.getText().toString().length() == 0) {
                    new AlertDialogManager().showToast((Activity) context, "Please enter description");
                    break CheckValidation;
                }
                RemedialJsonDataSaveModel plotActivityModel = new RemedialJsonDataSaveModel();
                plotActivityModel.setREMICODE(plotTypeSelection);
                plotActivityModel.setDATE(activityDate.getText().toString());
                plotActivityModel.setDIS(description.getText().toString());

                if (remedialJsonDataSaveModelList.contains(plotActivityModel)) {
                    new AlertDialogManager().showToast((Activity) context, "This item already added");
                    remedial_spinner.setSelection(0);
                    activityDate.setText("");
                    description.setText("");

                } else {
                    remedialJsonDataSaveModelList.add(plotActivityModel);
                    remedial_spinner.setSelection(0);
                    activityDate.setText("");
                    description.setText("");
                    setData();

                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
            }
        }
    }

    private void setData() {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            ListRemedialActivityAdapter listPloughingAdapter = new ListRemedialActivityAdapter(context, remedialJsonDataSaveModelList);
            recyclerView.setAdapter(listPloughingAdapter);
            String json = new Gson().toJson(remedialJsonDataSaveModelList);
            Log.d("GSONDATA", json);

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error : " + e.toString());
        }
    }


    void selectDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        //paymentDate.setText(currentDt);
        activityDate.setInputType(InputType.TYPE_NULL);
        activityDate.setTextIsSelectable(true);
        activityDate.setFocusable(false);
        activityDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    // Display Selected date in textbox
                    String temDate = "" + dayOfMonth;
                    if (temDate.length() == 1) {
                        temDate = "0" + temDate;
                    }
                    String temmonth = "" + (monthOfYear + 1);
                    if (temmonth.length() == 1) {
                        temmonth = "0" + temmonth;
                    }
                    activityDate.setText(year + "-" + temmonth + "-" + temDate);

                }
            }, mYear, mMonth, mDay);
            dpd.show();
        });

    }

    public void ExitBtn(View view) {
        finish();
    }

    void SavedataValidation() {
        if (remedialJsonDataSaveModelList.size() == 0) {
            new AlertDialogManager().showToast(AddRemadialActivity.this, "Please add activity");
        } else if (pictureImagePath.length() == 0) {
            new AlertDialogManager().showToast((Activity) context, "Please Capture a Image");
        } else {
            new SaveData().execute();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST) {
            try {
                File file = new File(pictureImagePath);
                if (file.exists()) {
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat dateFormatter2 = new SimpleDateFormat("HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    dateFormatter2.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = dateFormatter2.format(today);
                    Bitmap bmp = drawTextToBitmap(context, d, t);
                    FileOutputStream out = new FileOutputStream(pictureImagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
                    image.setImageBitmap(bitmap);
                } else {
                    image.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1) {
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);

            Bitmap.Config bitmapConfig =
                    bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);*/
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(120);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(gText, 0, gText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) - 250;
            int y = (bitmap.getHeight() + bounds.height()) - 250;

            canvas.drawText(gText, x, y, paint);
            paint.getTextBounds(gText1, 0, gText1.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) - 250;
            int y1 = (bitmap.getHeight() + bounds.height()) - 100;
            canvas.drawText(gText1, x1, y1, paint);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }

    public Bitmap ShrinkBitmap(String file, int width, int height) {
        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

            if (heightRatio > 1 || widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
        return null;
    }


    private class SaveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                Bitmap bitmap = ShrinkBitmap(pictureImagePath, 1500, 1200);//decodeFile(params[0]);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                System.out.println(imgFrmt);
                Log.d("Image : ", imgFrmt);

                String json = new Gson().toJson(remedialJsonDataSaveModelList);

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/saveRemedial";
                HttpClient httpClient = new DefaultHttpClient();
//2023-2024
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("SUP", "" + userDetailsModels.get(0).getCode()));
                entity.add(new BasicNameValuePair("PLVL", "" + PLOT_VILL_CODE));
                entity.add(new BasicNameValuePair("PLNO", "" + PLOT_SR_NO));
                entity.add(new BasicNameValuePair("DISEASE", "" + D_CODE));
                entity.add(new BasicNameValuePair("DATA", "" + json));
                entity.add(new BasicNameValuePair("IMAGEDATA", "" + imgFrmt));
                entity.add(new BasicNameValuePair("DIVN", "" + userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", ""+sessionConfig.getSeason()));

                Log.d("All", entity.toString());

                String debugData = new MiscleniousUtil().ListNameValueToString("saveRemedial", entity);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                //message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                //{"EXCEPTIONMSG":"","SAVEMSG":"Data Save","ACKID":"0"}
                JSONObject obj = new JSONObject(message);
                if (obj.getString("STATUS").equalsIgnoreCase("OK")) {
                    new AlertDialogManager().AlertPopUpFinish(context, "" + obj.getString("MSG"));
                } else {
                    new AlertDialogManager().RedDialog(context, "" + obj.getString("MSG"));
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context, "Error:" + e.getMessage());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }

    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            Log.d("LatLong :----->>>", "" + params[0]);
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(context);
                String url = APIUrl.BASE_URL + "/checkRemidialPolygonGrower_New1";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lng", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpGet, responseHandler);

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
            dialog.dismiss();
            try {
                JSONObject obj = new JSONObject(message);
                if (obj.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = obj.getJSONArray("DATA");
                    JsonData = jsonArray.getJSONObject(0).getJSONArray("JSONDATA");
                    if (jsonArray.length() == 0) {
                        new AlertDialogManager().RedDialog(context, "Error: No details found");
                    } else {
                        double totalNumber = 0;
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        for (int i = 0; i < JsonData.length(); i++) {

                            JSONObject jsonObject = JsonData.getJSONObject(i);
                            RemedialJsonDataModel remedialJsonDataModel = new RemedialJsonDataModel();

                            if (jsonObject.getString("STATUS").equalsIgnoreCase("PENDING")) {

                                remedialJsonDataModel.setREMICODE(jsonObject.getString("REMICODE"));
                                remedialJsonDataModel.setREMIDIALNAME(jsonObject.getString("REMIDIALNAME"));
                                remedialJsonDataModel.setDAYES(jsonObject.getString("DAYES"));
                                remedialJsonDataModel.setSTATUS(jsonObject.getString("STATUS"));  // DONE // PENDING
                                remedialJsonDataModelList.add(remedialJsonDataModel);

                            }


                        }
                        ArrayList getPocType = new ArrayList();
                        getPocType.add("Select Remedial Plans");
                        for (int i = 0; i < remedialJsonDataModelList.size(); i++) {
                            getPocType.add(remedialJsonDataModelList.get(i).getREMICODE() + " - " + remedialJsonDataModelList.get(i).getREMIDIALNAME());
                        }
                        ArrayAdapter<String> diseffectedTypeList = new ArrayAdapter<String>(context, R.layout.list_item, getPocType);
                        remedial_spinner.setAdapter(diseffectedTypeList);
                        remedial_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {
                                    remedial_txt.setText("");

                                } else {
                                    // Subtract 1 from the position to map it to the correct index in getpoctypeModelList
                                    int modelIndex = position - 1;
                                    if (modelIndex >= 0 && modelIndex < remedialJsonDataModelList.size()) {
                                        plotTypeSelection = remedialJsonDataModelList.get(modelIndex).getREMICODE();

                                        String selectedCodeName = remedialJsonDataModelList.get(modelIndex).getREMIDIALNAME();
                                        remedial_txt.setText(selectedCodeName);

                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                } else {
                    new AlertDialogManager().RedDialog(context, obj.getString("MSG"));
                }


            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(context, message);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());

            }
        }


    }


}
