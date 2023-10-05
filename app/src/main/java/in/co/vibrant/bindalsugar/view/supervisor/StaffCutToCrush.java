package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PurchiDetails;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class StaffCutToCrush extends AppCompatActivity {
    Context context;
    String[] CaneCut = {"Select", "Seed", "Purchase"};
    String[] CaneSupplyPurchy = {"Self", "Family Member", "No Purchi"};
    DBHelper dbh;
    Spinner spinner_cane;
    Spinner spinner_cane_supply;
    Spinner relation;
    Spinner cropcondition;
    Spinner Category;
    Spinner canetype;
    Spinner spinner_mode;
    Spinner variety;
    Spinner issue_purchey;
    Spinner txt_purchii;
    String strsupply = "";
    String loadPurchi = "";
    List<MasterDropDown> supplyModeList;
    List<PurchiDetails> puchiDetails;
    EditText txt_VehicleNo, input_village_code, input_village_name, input_grower_code, input_grower_name, txt_village, txt_grower, village_code, grower_name, grower_code, plot_sr_no, txt_supply, txt_cut, txt_todaycut, txt_cutting;
    String filename1 = "", pictureImagePath1 = "", filename2 = "", pictureImagePath2 = "";
    int RC_CAMERA_REQUEST_IMAGE_1 = 1001, RC_CAMERA_REQUEST_IMAGE_2 = 1002;
    AlertDialog Alertdialog;
    List<UserDetailsModel> userDetailsModels;
    List<GrowerModel> growerModelList = new ArrayList<>();
    double Lat, Lng;
    String address = "";
    String G_CODE;
    String G_NAME;
    String PLOT_SR_NO;
    String PLOT_VILL;
    String V_CODE;
    RelativeLayout rl_canesupplypurchi, rl_issupurchino, rl_village, rl_grower, rl_relation, rl_Mempurchi, rl_madeofsupply, rl_vehicleNo, rl_ondaycut, rl_nextdaycut, rl_cuttingDayp;
    private List<VillageModal> villageModalList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_to_crush);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            context = StaffCutToCrush.this;
            setTitle(getString(R.string.MENU_CUT_TO_CRUSH));
            toolbar.setTitle(getString(R.string.MENU_CUT_TO_CRUSH));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            dbh = new DBHelper(context);
            userDetailsModels = dbh.getUserDetailsModel();


            supplyModeList = new ArrayList<>();
            puchiDetails = new ArrayList<>();
            puchiDetails = dbh.getPurchiDetails("");

            V_CODE = getIntent().getExtras().getString("V_CODE");
            G_CODE = getIntent().getExtras().getString("G_CODE");
            PLOT_SR_NO = getIntent().getExtras().getString("PLOT_SR_NO");
            PLOT_VILL = getIntent().getExtras().getString("PLOT_VILL");
            G_NAME = getIntent().getExtras().getString("G_NAME");

            txt_VehicleNo = findViewById(R.id.txt_VehicleNo);
            village_code = findViewById(R.id.village_code);
            grower_name = findViewById(R.id.grower_name);
            plot_sr_no = findViewById(R.id.plot_sr_no);


            cropcondition = findViewById(R.id.cropcondition);
            Category = findViewById(R.id.Category);
            canetype = findViewById(R.id.canetype);
            variety = findViewById(R.id.variety);

            spinner_cane_supply = findViewById(R.id.spinner_cane_supply);
            relation = findViewById(R.id.relation);
            issue_purchey = findViewById(R.id.issue_purchey);
            txt_purchii = findViewById(R.id.txt_purchii);
            //txt_supply = findViewById(R.id.txt_supply);
            txt_cut = findViewById(R.id.txt_cut);
            txt_todaycut = findViewById(R.id.txt_todaycut);
            txt_cutting = findViewById(R.id.txt_cutting);
            spinner_mode = findViewById(R.id.spinner_mode);
            grower_code = findViewById(R.id.grower_code);
            input_village_code = findViewById(R.id.input_village_code);
            input_village_name = findViewById(R.id.input_village_name);
            input_grower_code = findViewById(R.id.input_grower_code);
            input_grower_name = findViewById(R.id.input_grower_name);

            village_code.setText(V_CODE);
            //grower_name.setText(G_CODE + " / " + G_NAME);
            grower_code.setText(G_CODE);
            grower_name.setText(G_NAME);
            plot_sr_no.setText(PLOT_SR_NO);

            ArrayList<String> relationArray = new ArrayList<String>();
            relationArray.add("Select Relation");
            relationArray.add("Father");
            relationArray.add("Mother");
            relationArray.add("Son");
            relationArray.add("Daughter");
            relationArray.add("Sister");
            relationArray.add("Brother");
            relationArray.add("Wife/Husband");
            relationArray.add("Other");
            ArrayAdapter<String> adapterseedSource = new ArrayAdapter<String>(context,
                    R.layout.list_item, relationArray);
            relation.setAdapter(adapterseedSource);



           /* ArrayList<String> purchiDetails = new ArrayList<String>();
            purchiDetails.add("Purchi Details");
            for (int i = 0; i < puchiDetails.size(); i++) {
                purchiDetails.add(puchiDetails.get(i).getPurchiNo() + "_" + puchiDetails.get(i).getMode() + "_"+puchiDetails.get(i).getCategory());
                //purchiDetails.add(mastervarietyArray.getJSONObject(i).getString("Code") + " - " + mastervarietyArray.getJSONObject(i).getString("Name"));

            }*/


            rl_canesupplypurchi = findViewById(R.id.rl_canesupplypurchi);
            rl_issupurchino = findViewById(R.id.rl_issupurchino);
            rl_village = findViewById(R.id.rl_village);
            rl_grower = findViewById(R.id.rl_grower);
            rl_relation = findViewById(R.id.rl_relation);
            rl_Mempurchi = findViewById(R.id.rl_Mempurchi);
            rl_madeofsupply = findViewById(R.id.rl_madeofsupply);
            rl_vehicleNo = findViewById(R.id.rl_vehicleNo);
            rl_ondaycut = findViewById(R.id.rl_ondaycut);
            rl_nextdaycut = findViewById(R.id.rl_nextdaycut);
            rl_cuttingDayp = findViewById(R.id.rl_cuttingDay);

            supplyModeList = dbh.getMasterDropDown("2");

            ArrayList<String> datasupply = new ArrayList<String>();
            datasupply.add("Supply Mode");
            for (int i = 0; i < supplyModeList.size(); i++) {
                datasupply.add(supplyModeList.get(i).getCode() + "_" + supplyModeList.get(i).getName());
                //datavarietytype.add(mastervarietyArray.getJSONObject(i).getString("Code") + " - " + mastervarietyArray.getJSONObject(i).getString("Name"));

            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(StaffCutToCrush.this,
                    R.layout.list_item, datasupply);
            spinner_mode.setAdapter(adaptersupply);

            spinner_cane = findViewById(R.id.spinner_cane);
            ArrayList<String> caneArray = new ArrayList<String>();
            caneArray.add("Select Cane Cut For");
            caneArray.add("Seed");
            caneArray.add("Purchi");
            ArrayAdapter<String> adaptercaneSource = new ArrayAdapter<String>(context,
                    R.layout.list_item, caneArray);
            spinner_cane.setAdapter(adaptercaneSource);

            spinner_cane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                    if (spinner_cane.getSelectedItemPosition() == 0) {
                        rl_canesupplypurchi.setVisibility(View.GONE);
                        rl_issupurchino.setVisibility(View.GONE);
                        rl_village.setVisibility(View.GONE);
                        rl_grower.setVisibility(View.GONE);
                        rl_relation.setVisibility(View.GONE);
                        rl_Mempurchi.setVisibility(View.GONE);
                        rl_madeofsupply.setVisibility(View.GONE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);
                    } else if (spinner_cane.getSelectedItemPosition() == 1) {
                        rl_canesupplypurchi.setVisibility(View.GONE);
                        rl_issupurchino.setVisibility(View.GONE);
                        rl_village.setVisibility(View.GONE);
                        rl_grower.setVisibility(View.GONE);
                        rl_relation.setVisibility(View.GONE);
                        rl_Mempurchi.setVisibility(View.GONE);
                        rl_madeofsupply.setVisibility(View.GONE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);

                    } else if (spinner_cane.getSelectedItemPosition() == 2) {
                        rl_canesupplypurchi.setVisibility(View.VISIBLE);
                        rl_issupurchino.setVisibility(View.GONE);
                        rl_village.setVisibility(View.GONE);
                        rl_grower.setVisibility(View.GONE);
                        rl_relation.setVisibility(View.GONE);
                        rl_Mempurchi.setVisibility(View.GONE);
                        rl_madeofsupply.setVisibility(View.VISIBLE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });

            ArrayList<String> caneSupplyArray = new ArrayList<String>();
            caneSupplyArray.add("Select Cane Supply");
            caneSupplyArray.add("Self");
            caneSupplyArray.add("Family Member");
            caneSupplyArray.add("No Purchi");
            ArrayAdapter<String> adaptercaneSupply = new ArrayAdapter<String>(context,
                    R.layout.list_item, caneSupplyArray);
            spinner_cane_supply.setAdapter(adaptercaneSupply);

            spinner_cane_supply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                    /* if (spinner_cane_supply.getSelectedItemPosition()==0) {
                    rl_canesupplypurchi.setVisibility(View.GONE);
                    rl_issupurchino.setVisibility(View.GONE);
                    rl_village.setVisibility(View.GONE);
                    rl_grower.setVisibility(View.GONE);
                    rl_relation.setVisibility(View.GONE);
                    rl_Mempurchi.setVisibility(View.GONE);
                    rl_madeofsupply.setVisibility(View.GONE);
                    rl_vehicleNo.setVisibility(View.VISIBLE);
                    rl_ondaycut.setVisibility(View.VISIBLE);
                    rl_nextdaycut.setVisibility(View.VISIBLE);
                    rl_cuttingDayp.setVisibility(View.VISIBLE);
                }*/
                    if (spinner_cane_supply.getSelectedItemPosition() == 1) {

                        rl_canesupplypurchi.setVisibility(View.VISIBLE);
                        rl_issupurchino.setVisibility(View.VISIBLE);
                        rl_village.setVisibility(View.GONE);
                        rl_grower.setVisibility(View.GONE);
                        rl_relation.setVisibility(View.GONE);
                        rl_Mempurchi.setVisibility(View.GONE);
                        rl_madeofsupply.setVisibility(View.VISIBLE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);

                    } else if (spinner_cane_supply.getSelectedItemPosition() == 2) {

                        rl_canesupplypurchi.setVisibility(View.VISIBLE);
                        rl_issupurchino.setVisibility(View.GONE);
                        rl_village.setVisibility(View.VISIBLE);
                        rl_grower.setVisibility(View.VISIBLE);
                        rl_relation.setVisibility(View.VISIBLE);
                        rl_Mempurchi.setVisibility(View.VISIBLE);
                        rl_madeofsupply.setVisibility(View.VISIBLE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);

                    } else if (spinner_cane_supply.getSelectedItemPosition() == 3) {

                        rl_canesupplypurchi.setVisibility(View.VISIBLE);
                        rl_issupurchino.setVisibility(View.GONE);
                        rl_village.setVisibility(View.GONE);
                        rl_grower.setVisibility(View.GONE);
                        rl_relation.setVisibility(View.GONE);
                        rl_Mempurchi.setVisibility(View.GONE);
                        rl_madeofsupply.setVisibility(View.GONE);
                        rl_vehicleNo.setVisibility(View.VISIBLE);
                        rl_ondaycut.setVisibility(View.VISIBLE);
                        rl_nextdaycut.setVisibility(View.VISIBLE);
                        rl_cuttingDayp.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });

            /////////////----------Check Validation Grower Code--------///////////
           /* grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (village_code.getText().toString().length() > 0) {
                        if (grower_code.getText().toString().length() > 0) {
                            //checkPlot = false;
                            if (grower_code.getText().toString().equals("0")) {
                                grower_code.setEnabled(true);
                                grower_code.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                //new AlertDialogManager().RedDialog(context, "Please enter  grower code valid");
                            } else {
                                grower_code.setFocusable(false);
                                grower_code.setTextIsSelectable(false);
                                grower_code.setInputType(InputType.TYPE_NULL);
                                grower_name.setFocusable(false);
                                grower_name.setTextIsSelectable(false);
                                grower_name.setInputType(InputType.TYPE_NULL);
                                List<GrowerModel> growerModelList = dbh.getGrowerModel(village_code.getText().toString(), grower_code.getText().toString());
                                if (growerModelList.size() > 0) {
                                    grower_code.setText(growerModelList.get(0).getGrowerCode());
                                    grower_name.setText(growerModelList.get(0).getGrowerName());

                                } else {
                                    new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                    grower_name.setText("");
                                }

                            }

                        } else {


                        }
                    } else {
                        new AlertDialogManager().RedDialog(context, "Please enter village code First");
                        village_code.requestFocus();
                    }
                }
            });
*/
            input_village_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        //checkPlot = false;
                        List<VillageModal> villageModalList = dbh.getVillageModal(input_village_code.getText().toString());
                        if (villageModalList.size() > 0) {
                            input_village_code.setText(villageModalList.get(0).getCode());
                            input_village_name.setText(villageModalList.get(0).getName());

                        } else {
                            input_village_code.setText("");
                            input_village_name.setText("");
                            new AlertDialogManager().RedDialog(context, "Please enter valid village code");


                        }
                    }

                }

            });

            input_grower_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (input_village_code.getText().toString().length() > 0) {
                        if (input_grower_code.getText().toString().length() > 0) {
                            if (input_grower_code.getText().toString().equals("0")) {
                                input_grower_name.setFocusable(true);
                                input_grower_name.setTextIsSelectable(true);
                                input_grower_name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                            } else {
                                input_grower_name.setFocusable(false);
                                input_grower_name.setTextIsSelectable(false);
                                input_grower_name.setInputType(InputType.TYPE_NULL);

                                List<GrowerModel> growerModelList = dbh.getGrowerModel(input_village_code.getText().toString(), input_grower_code.getText().toString());
                                if (growerModelList.size() > 0) {
                                    input_grower_code.setText(growerModelList.get(0).getGrowerCode());
                                    input_grower_name.setText(growerModelList.get(0).getGrowerName());

                                } else {
                                    new AlertDialogManager().RedDialog(context, "Please enter valid grower code");
                                    input_grower_code.setText("");
                                    input_grower_name.setText("");
                                    //growerFatherName.setText("");
                                }
                                new GetPurchiDetails().execute();
                            }


                        }



                    } else {
                        Alertdialog.setTitle("Please enter village code");
                        input_village_code.requestFocus();
                    }


                }

            });


            /////------------------Validation---///////////////////////

            input_grower_code.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {


                    checkValidation:
                    {
                        if (grower_code.getText().toString().length() == 0) {
                            new AlertDialogManager().RedDialog(context, "please enter Grower Code !!");
                            break checkValidation;
                        }
                        if (input_grower_code.getText().toString().length() == 0) {
                            input_grower_code.requestFocus();
                            break checkValidation;
                        } else if (Integer.parseInt(grower_code.getText().toString()) == Integer.parseInt(input_grower_code.getText().toString())) {
                            new AlertDialogManager().RedDialog(context, "This grower Code already exit!!");
                            // running_kolhu.setText("");
                            input_grower_code.getText().clear();
                            input_village_code.requestFocus();
                            break checkValidation;
                        }

                    }


                }
            });


            new GetMaster().execute();


            findLocation();

        } finally {

        }
    }

    public void openCam(View v) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            filename1 = "image_" + currentDt + ".jpg";
            pictureImagePath1 = dir.getAbsolutePath() + "/" + filename1;
            File file = new File(pictureImagePath1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST_IMAGE_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRecorder(View v) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt = dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            filename2 = "image_" + currentDt + ".mp4";
            pictureImagePath2 = dir.getAbsolutePath() + "/" + filename2;
            File file = new File(pictureImagePath2);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, RC_CAMERA_REQUEST_IMAGE_2);
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_1) {
            try {
                File file = new File(pictureImagePath1);
                if (file.exists()) {
                    DateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today = new Date();
                    // java.util.Timer;
                    String d = dateFormatter1.format(today);
                    String t = "";//location
                    Bitmap bmp = drawTextToBitmap(context, d, t, pictureImagePath1);
                    FileOutputStream out = new FileOutputStream(pictureImagePath1);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath1);
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_image_view, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    ImageView d_image = mView.findViewById(R.id.d_image);
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);
                    d_image.setImageBitmap(bitmap);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                            filename1 = "";
                            pictureImagePath1 = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        } else if (requestCode == RC_CAMERA_REQUEST_IMAGE_2) {
            try {
                File file = new File(pictureImagePath2);
                if (file.exists()) {
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_video_view, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    final VideoView d_video = mView.findViewById(R.id.d_image);
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);

                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    getWindow().setFormat(PixelFormat.TRANSLUCENT);

                    MediaController mediaController = new MediaController(context);
                    mediaController.show();
                    Uri video = Uri.parse(pictureImagePath2);
                    d_video.setMediaController(mediaController);
                    d_video.setKeepScreenOn(true);
                    d_video.setVideoPath(pictureImagePath2);
                    d_video.requestFocus();
                    /*videoView.setVideoURI(video);
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            //mp.setLooping(true);
                            videoView.start();
                        }
                    });*/
                    d_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            d_video.start();
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog.dismiss();
                            filename2 = "";
                            pictureImagePath2 = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    public Bitmap drawTextToBitmap(Context gContext, String gText, String gText1, String path) {
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            //Bitmap bitmap =BitmapFactory.decodeResource(resources, gResId);
            Bitmap bitmap = BitmapFactory.decodeFile(path);

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
            int x = (bitmap.getWidth() - bounds.width()) - 200;
            int y = (bitmap.getHeight() + bounds.height()) - 260;

            canvas.drawText(gText, x, y, paint);
            paint.getTextBounds(gText1, 0, gText1.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) - 200;
            int y1 = (bitmap.getHeight() + bounds.height()) - 110;
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
            //new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        return null;
    }


    protected void findLocation() {

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            // Create the location request to start receiving updates
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //checkUpdate();
                }
            });
            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            new AlertDialogManager().AlertPopUpFinish(context, "Security Error: please enable gps service");
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity) context,
                                    1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location = locationResult.getLastLocation();
                            if (location.isFromMockProvider()) {
                                new AlertDialogManager().showToast((Activity) context, "Security Error : you can not use this application because we detected fake GPS ?");
                            } else {
                                try {
                                    List<Address> addresses = new ArrayList<>();
                                    Lat = location.getLatitude();
                                    Lng = location.getLongitude();
                                    Geocoder geocoder;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                                    address = addresses.get(0).getAddressLine(0);

                                } catch (SecurityException se) {
                                    address = "Error : Security error";
                                    new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
                                } catch (Exception se) {
                                    new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
                                }
                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Security Error:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().AlertPopUpFinish(context, "Error:" + se.toString());
        }

    }

    public void saveData(View v) {
        try {
            IndentModel indentModel = new IndentModel();
            indentModel.setSupplyMode(strsupply);

            CheckValidation:
            {
                if (cropcondition.getSelectedItemPosition() == 0)//seedSourceList
                {
                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please select Crop Condition");
                    break CheckValidation;
                }
                if (Category.getSelectedItemPosition() == 0)//seedSourceL0ist
                {

                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please select Category");
                    break CheckValidation;
                }
                if (canetype.getSelectedItemPosition() == 0)//seedSourceList
                {

                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please select cane type");
                    break CheckValidation;
                }
                if (variety.getSelectedItemPosition() == 0) {

                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please select Variety");
                    break CheckValidation;
                }
               /* if (issue_purchey.getSelectedItemPosition() == 0) {

                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please select issued purchey");
                    break CheckValidation;
                }*/
                if (txt_VehicleNo.getText().length() == 0) {
                    new AlertDialogManager().showToast(StaffCutToCrush.this, "Please enter Vehicle No");
                    break CheckValidation;
                }
                if (filename1.length() < 5 && filename2.length() < 5) {
                    new AlertDialogManager().RedDialog(context, "Please capture image or record video");
                    break CheckValidation;
                }
                if (spinner_cane_supply.getSelectedItemPosition() == 1) {
                    if (issue_purchey.getSelectedItemPosition() == 0) {
                        new AlertDialogManager().RedDialog(context, "Select Issued Purchy First!");
                        break CheckValidation;
                    }
                }
                if (spinner_cane_supply.getSelectedItemPosition() == 2) {
                    if (txt_purchii.getSelectedItemPosition() == 0) {
                        new AlertDialogManager().RedDialog(context, "Select Member Purchy First!");
                        break CheckValidation;
                    }
                }

                if (txt_cut.getText().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "On Date Cut % can't be blank!");
                    break CheckValidation;
                }
                if (txt_todaycut.getText().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Next Day Cut % can't be blank!");
                    break CheckValidation;
                }

                if ((Integer.parseInt(txt_cut.getText().toString()) + Integer.parseInt(txt_todaycut.getText().toString())) > 100) {
                    new AlertDialogManager().RedDialog(context, "On Date & Next Day Cut % can't be greator than 100");
                    break CheckValidation;
                }

                if (dbh.getControlStatus("SUPPLY MODE", "INDENTING").equalsIgnoreCase("2")) {
                   /* if (spinner_mode.getSelectedItemPosition() == 0) {
                        AlertPopUp("Please select Vehicle mode");

                    }*/
                    strsupply = supplyModeList.get(spinner_mode.getSelectedItemPosition() - 1).getCode();
                }


                String[] cropConditionArray = cropcondition.getSelectedItem().toString().split(" - ");
                String[] CategoryArray = Category.getSelectedItem().toString().split(" - ");
                String[] canetypeArray = canetype.getSelectedItem().toString().split(" - ");
                String[] varietyArray = variety.getSelectedItem().toString().split(" - ");
                String[] issuepArray = issue_purchey.getSelectedItem().toString().split(" - ");
                String[] supplyArray = spinner_mode.getSelectedItem().toString().split(" - ");
                String MemPurchi = "0";
                int val = txt_purchii.getSelectedItemPosition();
                if (txt_purchii.getSelectedItemPosition() > 0) {
                    String[] purchiArray = txt_purchii.getSelectedItem().toString().split(" - ");
                    MemPurchi = purchiArray[0];
                }


                new saveData().execute(cropConditionArray[0], CategoryArray[0], canetypeArray[0],
                        varietyArray[0], issuepArray[0], supplyArray[0], MemPurchi);

                ////----Sending On server----
                //String[] cropConditionStr=cropcondition.getSelectedItem().toString().split((" - "));
                //cropConditionStr[0]; // Jisme Code Hoge

                //Jisme nahi hoga cropcondition.getSelectedItem().toString()

            }
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }


    private class saveData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            //dialog.setIndeterminate(false);
            dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /* dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
               /* String modelst = spinner_mode.getSelectedItem().toString();
                String modecode = "";
                String[] zonesting = modelst.split(",");
                for (int i = 0; i < zonesting.length; i++) {
                    String[] zonecodename = zonesting[i].split("-");
                    if (i == 0) {
                        modecode = zonecodename[0].trim();
                    } else {
                        modecode = modecode + "," + zonecodename[0];
                    }
                }*/
                String imgFrmt = "";
                String videoFrmt = "";
                if (filename1.length() > 5) {
                    Bitmap bitmap1 = ShrinkBitmap(pictureImagePath1, 500, 500);//decodeFile(params[0]);
                    ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                    byte[] byteFormat1 = bao1.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat1, Base64.NO_WRAP);

                }
                if (filename2.length() > 5) {
                    FileInputStream fis = new FileInputStream(pictureImagePath2);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];

                    for (int readNum; (readNum = fis.read(b)) != -1; ) {
                        bos.write(b, 0, readNum);
                    }
                    byte[] byteFormat = bos.toByteArray();
                    videoFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                }

                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVECUTTOCRUSH);
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("VILLCODE", V_CODE);
                request1.addProperty("GROCODE", G_CODE);
                request1.addProperty("PLOTVILL", PLOT_VILL);
                request1.addProperty("PLOTNO", PLOT_SR_NO);
                request1.addProperty("CROPCONDITION", params[0]);
                request1.addProperty("CATEGORY", params[1]);
                request1.addProperty("CANETYPE", params[2]);
                request1.addProperty("VARIETY", params[3]);
                request1.addProperty("VECHILENO", txt_VehicleNo.getText().toString());
                request1.addProperty("IMAGENAME", imgFrmt);
                request1.addProperty("VEDIONAME", videoFrmt);
                request1.addProperty("LAT", "" + Lat);
                request1.addProperty("LON", "" + Lng);
                request1.addProperty("ADDRESS", address);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("SUPCODE", userDetailsModels.get(0).getCode());
                request1.addProperty("CANECUTFOR", spinner_cane.getSelectedItem());
                request1.addProperty("CANESUPFOR", spinner_cane_supply.getSelectedItem());
                request1.addProperty("ONDAYCUT", txt_cut.getText().toString());
                request1.addProperty("NEXTDAYCUT", txt_todaycut.getText().toString());
                request1.addProperty("CUTTINGDAY", txt_cutting.getText().toString());
                /*request1.addProperty("MEMVILL", input_village_code.getText().toString());
                request1.addProperty("MEMGROW", input_grower_code.getText().toString());
                request1.addProperty("MEMRELATION", relation.getSelectedItemPosition());
                request1.addProperty("MEMPURNO", params[6]);*/



                if (spinner_cane_supply.getSelectedItemPosition() == 1) {
                    request1.addProperty("PURCHYNO", params[4]);
                    request1.addProperty("ONDAYCUT", txt_cut.getText().toString());
                    request1.addProperty("NEXTDAYCUT", txt_todaycut.getText().toString());
                    request1.addProperty("CUTTINGDAY", txt_cutting.getText().toString());
                    request1.addProperty("MODSUP", supplyModeList.get(0).getCode());
                    request1.addProperty("MEMVILL", "0");
                    request1.addProperty("MEMGROW", "0");
                    request1.addProperty("MEMRELATION", "0");
                    request1.addProperty("MEMPURNO", "0");


                } else if (spinner_cane_supply.getSelectedItemPosition() == 2) {

                    request1.addProperty("PURCHYNO", "0");
                    request1.addProperty("ONDAYCUT", txt_cut.getText().toString());
                    request1.addProperty("NEXTDAYCUT", txt_todaycut.getText().toString());
                    request1.addProperty("CUTTINGDAY", txt_cutting.getText().toString());
                    request1.addProperty("MEMVILL", input_village_code.getText().toString());
                    request1.addProperty("MEMGROW", input_grower_code.getText().toString());
                    request1.addProperty("MEMRELATION", relation.getSelectedItemPosition());
                    request1.addProperty("MODSUP", supplyModeList.get(0).getCode());
                    request1.addProperty("MEMPURNO", params[6]);

                } else {

                    request1.addProperty("ONDAYCUT", txt_cut.getText().toString());
                    request1.addProperty("NEXTDAYCUT", txt_todaycut.getText().toString());
                    request1.addProperty("CUTTINGDAY", txt_cutting.getText().toString());
                    request1.addProperty("PURCHYNO", "0");
                    request1.addProperty("MEMVILL", "0");
                    request1.addProperty("MEMGROW", "0");
                    request1.addProperty("MEMPURNO", "0");
                    request1.addProperty("MEMRELATION", "0");
                    request1.addProperty("MODSUP", "0");

                }

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVECUTTOCRUSH, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVECUTTOCRUSHResult").toString();
                return null;
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
            catch (Exception e2) {
                Log.e("Exception", e2.getMessage());
                message = e2.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    Intent intent = new Intent(context, StaffMainActivity.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"), intent);
                } else {
                    Intent intent = new Intent(context, StaffCutToCrush.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"),intent);
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
            }
        }
    }

    private class GetMaster extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/CUTTOCRUSSMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();

                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("VILLCODE", V_CODE));
                entity.add(new BasicNameValuePair("GROWERCODE", G_CODE));
                entity.add(new BasicNameValuePair("lang", getString(R.string.language)));
                //entity.add(new BasicNameValuePair("LAT",params[0]));
                //entity.add(new BasicNameValuePair("LON",params[1]));


                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + message);


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
                JSONArray jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONArray masterCropConditionArray = jsonObject.getJSONArray("CropCondition");
                ArrayList<String> dataCropCondition = new ArrayList<>();
                dataCropCondition.add(" - Select - ");
                for (int i = 0; i < masterCropConditionArray.length(); i++) {
                    dataCropCondition.add(masterCropConditionArray.getJSONObject(i).getString("Code") + " - " + masterCropConditionArray.getJSONObject(i).getString("Name"));
                }
                cropcondition.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCropCondition));

                JSONArray masterCategoryArray = jsonObject.getJSONArray("CATEGORY");
                ArrayList<String> dataCategory = new ArrayList<>();
                dataCategory.add(" - Select - ");
                for (int i = 0; i < masterCategoryArray.length(); i++) {
                    dataCategory.add(masterCategoryArray.getJSONObject(i).getString("Code") + " - " + masterCategoryArray.getJSONObject(i).getString("Name"));
                }
                Category.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCategory));

                JSONArray mastercanetypeArray = jsonObject.getJSONArray("CANETYPE");
                ArrayList<String> datacanetype = new ArrayList<>();
                datacanetype.add(" - Select - ");
                for (int i = 0; i < mastercanetypeArray.length(); i++) {
                    datacanetype.add(mastercanetypeArray.getJSONObject(i).getString("Code") + " - " + mastercanetypeArray.getJSONObject(i).getString("Name"));
                }
                canetype.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, datacanetype));


                JSONArray mastervarietyArray = jsonObject.getJSONArray("VARIETY");
                ArrayList<String> datavarietytype = new ArrayList<>();
                datavarietytype.add(" - Select - ");
                for (int i = 0; i < mastervarietyArray.length(); i++) {
                    datavarietytype.add(mastervarietyArray.getJSONObject(i).getString("Code") + " - " + mastervarietyArray.getJSONObject(i).getString("Name"));
                }
                variety.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, datavarietytype));

                JSONArray masterissuepurcheyArray = jsonObject.getJSONArray("ISSUEDPURCHY");
                ArrayList<String> dataissuepurcheytype = new ArrayList<>();
                dataissuepurcheytype.add(" - Select - ");
                for (int i = 0; i < masterissuepurcheyArray.length(); i++) {
                    dataissuepurcheytype.add(masterissuepurcheyArray.getJSONObject(i).getString("Code") + " - " + masterissuepurcheyArray.getJSONObject(i).getString("Name"));
                }
                issue_purchey.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataissuepurcheytype));


                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


    private class GetPurchiDetails extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(getString(R.string.app_name_language));
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETPURCHYDETAILS";
                HttpClient httpClient = new DefaultHttpClient();

                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                entity.add(new BasicNameValuePair("Vill", input_village_code.getText().toString()));
                entity.add(new BasicNameValuePair("growcode", input_grower_code.getText().toString()));


                //entity.add(new BasicNameValuePair("LAT",params[0]));
                //entity.add(new BasicNameValuePair("LON",params[1]));

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + message);


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
                JSONArray jsonArray = new JSONArray(message);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                //ArrayList<String> data1 = new ArrayList<String>();
                //JSONObject jsonObject = jsonArray.getJSONObject(0);
                //JSONObject jsonObject=jsonArray.getJSONObject(0);
                //JSONArray purchiDetailsArray = jsonObject.getJSONArray("");
                ArrayList<String> dataCropCondition = new ArrayList<>();
                dataCropCondition.add(" - Select - ");
                for (int i = 0; i < jsonArray.length(); i++) {

                    dataCropCondition.add(jsonObject.getString("Purchyno") + " - " + jsonObject.getString("mode") + " - " + jsonObject.getString("Category"));
                }
                txt_purchii.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, dataCropCondition));


                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}



