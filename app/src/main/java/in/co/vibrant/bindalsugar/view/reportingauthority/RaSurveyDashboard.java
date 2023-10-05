package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.FarmerAutoSearchAdapter;
import in.co.vibrant.bindalsugar.adapter.SupervisorAutoSearchAdapter;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.SupervisorModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.CaneTypeSummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.GetSupervisorSummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.PlotOverLapping;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.RaSurveyReportMap;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.RemainShare;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.SummaryCaneTypeAndCategory;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.VarietyGroupSummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.VarietySummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.VillageWiseCaneTypeSummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.VillageWiseVariertyGroupSummery;
import in.co.vibrant.bindalsugar.view.reportingauthority.canesurvey.VillageWiseVarietySummery;
import in.co.vibrant.bindalsugar.view.supervisor.PlotFinderMapViewMaster;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCanePendingShareListActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCropObservationCheckMapview;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCropObservationReport;
import in.co.vibrant.bindalsugar.view.supervisor.survey.NewPlotEntryCane;


public class RaSurveyDashboard extends AppCompatActivity {

    DBHelper dbh;
    SQLiteDatabase db;
    Context context;
    List<UserDetailsModel> userDetailsModelList;
    List<FarmerModel> farmerModelList;
    FarmerAutoSearchAdapter farmerAutoSearchAdapter;
    AlertDialog alertDialog;
    List<MasterDropDown> supervisorList;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;

    double lat, lng, accuracy;
    Geocoder geocoder;
    TextView latLngAddress;

    List<MasterDropDown> villageList, landTypeList, borderTypeList, ploughingMethodList;
    private int mYear, mMonth, mDay, mHour, mMinute;
    List<SupervisorModel> supervisorModelList, villageModelList;
    AutoCompleteTextView report_super_wiser_code_from, report_super_wiser_code_to, report_super_wiser_village_from,
            report_super_wiser_village_to, report_variety_from, report_variety_to;
    SupervisorAutoSearchAdapter supervisorAutoSearchAdapter;

    JSONArray jsonArray, jsonVillageArray, jsonVarietyArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_survey_dashboard);
        dbh = new DBHelper(this);
        db = new DBHelper(this).getWritableDatabase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = RaSurveyDashboard.this;
        setTitle(getString(R.string.MENU_SURVEY));
        toolbar.setTitle(getString(R.string.MENU_SURVEY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userDetailsModelList = dbh.getUserDetailsModel();
        supervisorList = new ArrayList<>();
        villageList = new ArrayList<>();
        landTypeList = new ArrayList<>();
        borderTypeList = new ArrayList<>();
        ploughingMethodList = new ArrayList<>();
        startLocationUpdates();
    }


    protected void startLocationUpdates() {

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

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
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
                            try {
                                Location location = locationResult.getLastLocation();
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                accuracy = location.getAccuracy();
                                if(APIUrl.isDebug)
                                {
                                    lat=APIUrl.lat;
                                    lng=APIUrl.lng;
                                }
                                final List<Address> addresses;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                String address = "";
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latLngAddress.setText("\nLattitude : " + lat + "\nLongitude : " + lng + "\nAccuracy : " + new DecimalFormat("0.00").format(accuracy) + "\n\nAddress : " + addresses.get(0).getAddressLine(0));
                            } catch (Exception e) {

                            }
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException se) {
            new AlertDialogManager().RedDialog(this, "Security Error:" + se.toString());
        } catch (Exception se) {
            new AlertDialogManager().RedDialog(this, "Error:" + se.toString());
        }

    }

    public void openNewPlotSurvey(View v) {
        List<VillageSurveyModel> villageSurveyModelList = dbh.getSurveyVillageModel("");
        List<MasterCaneSurveyModel> masterCaneSurveyModels = dbh.getMasterModel("");
        if (villageSurveyModelList.size() > 0) {
            if (masterCaneSurveyModels.size() > 0) {
                Intent intent = new Intent(context, NewPlotEntryCane.class);
                startActivity(intent);
            } else {
                new AlertDialogManager().RedDialog(context, "Please download master data first ...");
            }
        } else {
            new AlertDialogManager().RedDialog(context, "Please download village data first ...");
        }

    }

    public void openPlotFinderPathFinder(View v) {
        Intent intent = new Intent(context, RaSurveyStaffGrowerDetails.class);
        startActivity(intent);
    }

    public void openStaffCanePendingShareListActivity(View v) {
        Intent intent = new Intent(context, StaffCanePendingShareListActivity.class);
        startActivity(intent);
    }

    public void openGrowerEnquiry(View v)
    {

        try {
            Intent intent = new Intent(context, RaStaffSurveyGrowerFinderMapView.class);
            //Intent intent = new Intent(context, StaffCropObservation.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lng);
            startActivity(intent);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }



       /* try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
            dialogbilder.setView(mView);
            alertDialog = dialogbilder.create();
            ArrayList<String> divData = new ArrayList<String>();
            latLngAddress = mView.findViewById(R.id.err_msg);
            latLngAddress.setText("\nLattitude : --\nLongitude : --\nAccuracy : --\n\nAddress : --");
            Button b1 = (Button) mView.findViewById(R.id.btn_ok);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, StaffGrowerFinder.class);
                            intent.putExtra("lat", "" + lat);
                            intent.putExtra("lng", "" + lng);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "ErrorL:" + e.toString());
                    }
                }
            });
            Button recheck = (Button) mView.findViewById(R.id.recheck);
            recheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "ErrorL:" + e.toString());
                    }
                }
            });
            alertDialog.show();
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }*/
    }

    private void opendialogue(List<Address> addresses, final double Lat, final double Lng, final double accuracy) {

    }

    public void openVeritalCheck(View v) {
        try {
            // Intent intent = new Intent(context, StaffVerticalCheckMapview.class);
            Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
            //Intent intent = new Intent(context, StaffCropObservation.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lng);
            startActivity(intent);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

           /* AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
            dialogbilder.setView(mView);
            alertDialog = dialogbilder.create();
            ArrayList<String> divData = new ArrayList<String>();
            latLngAddress = mView.findViewById(R.id.err_msg);
            latLngAddress.setText("\nLattitude : --\nLongitude : --\nAccuracy : --\n\nAddress : --");
            Button b1 = (Button) mView.findViewById(R.id.btn_ok);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, StaffVeritalCheck.class);
                            intent.putExtra("lat", "" + lat);
                            intent.putExtra("lng", "" + lng);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "ErrorL:" + e.toString());
                    }
                }
            });
            Button recheck = (Button) mView.findViewById(R.id.recheck);
            recheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "ErrorL:" + e.toString());
                    }
                }
            });
            alertDialog.show();
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }*/
    }

    public void openVillageWiseVarietySummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_variety_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Village Wise Variety Summary");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_variety_from = mView.findViewById(R.id.rl_report_variety_from);
        final TextInputLayout rl_report_variety_to = mView.findViewById(R.id.rl_report_variety_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        report_variety_from = mView.findViewById(R.id.report_variety_from);
        report_variety_to = mView.findViewById(R.id.report_variety_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        report_variety_from.setText("0");
        report_variety_to.setText("9999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_variety_from.getText().toString().length() == 0 || report_variety_from.getText().toString().length() > 4) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(true);
                    rl_report_variety_from.setError("Please enter from variety code max 4 digits");
                } else if (report_variety_to.getText().toString().length() == 0 || report_variety_to.getText().toString().length() > 4) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(false);
                    rl_report_variety_to.setErrorEnabled(true);
                    rl_report_variety_to.setError("Please enter to variety code max 4 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(false);
                    rl_report_variety_to.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, VillageWiseVarietySummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("vrtfrom", report_variety_from.getText().toString());
                        intent.putExtra("vrtto", report_variety_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openVillageWiseVariertyGroupSummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Village Wise Variety Group Summary");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");

        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, VillageWiseVariertyGroupSummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openVillageWiseCaneTypeSummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Village Wise Cane Type SUmmary");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);
        ArrayList<String> divData = new ArrayList<String>();

        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, VillageWiseCaneTypeSummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openSupervisorSummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        List<UserDetailsModel> factoryListModalList = new ArrayList<>();
        factoryListModalList = dbh.getUserDetailsModel();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Supervisor Summary");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);
        rl_report_super_wiser_village_from.setVisibility(View.GONE);
        rl_report_super_wiser_village_to.setVisibility(View.GONE);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        report_from_date.setText(dateFormat.format(today));
        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setText(dateFormat.format(today));
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_from_date.getText().toString().length() == 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter from date");
                } else if (report_to_date.getText().toString().length() == 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to date");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, GetSupervisorSummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openRemainShare(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Remain Share");

        //final TextInputLayout rl_report_factory = mView.findViewById(R.id.rl_report_factory);
        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);
        rl_report_from_date.setVisibility(View.GONE);
        rl_report_to_date.setVisibility(View.GONE);

        //final Spinner report_factory = mView.findViewById(R.id.report_factory);
        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);


        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, RemainShare.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openSummeryCaneTypeCategory(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Summary Cane Type & Category");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);
        rl_report_super_wiser_village_from.setVisibility(View.GONE);
        rl_report_super_wiser_village_to.setVisibility(View.GONE);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        report_from_date.setText(dateFormat.format(today));
        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setText(dateFormat.format(today));
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, SummaryCaneTypeAndCategory.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openVarityWiseCenterRunning(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_variety_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_variety_from = mView.findViewById(R.id.rl_report_variety_from);
        final TextInputLayout rl_report_variety_to = mView.findViewById(R.id.rl_report_variety_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        report_variety_from = mView.findViewById(R.id.report_variety_from);
        report_variety_to = mView.findViewById(R.id.report_variety_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        report_variety_from.setText("0");
        report_variety_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        report_from_date.setText(dateFormat.format(today));
        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setText(dateFormat.format(today));
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_variety_from.getText().toString().length() == 0 || report_variety_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(true);
                    rl_report_variety_from.setError("Please enter from variety code max 6 digits");
                } else if (report_variety_to.getText().toString().length() == 0 || report_variety_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(false);
                    rl_report_variety_to.setErrorEnabled(true);
                    rl_report_variety_to.setError("Please enter to variety code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_variety_from.setErrorEnabled(false);
                    rl_report_variety_to.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, VarietySummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("vrtfrom", report_variety_from.getText().toString());
                        intent.putExtra("vrtto", report_variety_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openCaneTypeSummery(View v) {
        try{
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        report_from_date.setText(dateFormat.format(today));
        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setText(dateFormat.format(today));
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, CaneTypeSummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
    }

    public void openVarietyGroupSummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cane_type_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        TextView textView = mView.findViewById(R.id.textView);
        textView.setText("Search Variety Group Summary");

        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_from_date = mView.findViewById(R.id.rl_report_from_date);
        final TextInputLayout rl_report_to_date = mView.findViewById(R.id.rl_report_to_date);

        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_from_date = mView.findViewById(R.id.report_from_date);
        final EditText report_to_date = mView.findViewById(R.id.report_to_date);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");
        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date today = Calendar.getInstance().getTime();
        report_from_date.setText(dateFormat.format(today));
        report_from_date.setInputType(InputType.TYPE_NULL);
        report_from_date.setTextIsSelectable(true);
        report_from_date.setFocusable(false);
        report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_from_date.setText(temDate + "-" + temmonth + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        report_to_date.setText(dateFormat.format(today));
        report_to_date.setInputType(InputType.TYPE_NULL);
        report_to_date.setTextIsSelectable(true);
        report_to_date.setFocusable(false);
        report_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this, new DatePickerDialog.OnDateSetListener() {

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
                        report_to_date.setText(temDate + "-" + temmonth + "-" + year
                        );
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_to_date.getText().toString().length() == 0 && report_from_date.getText().toString().length() > 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_to_date.setErrorEnabled(true);
                    rl_report_to_date.setError("Please enter to date");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(RaSurveyDashboard.this, VarietyGroupSummery.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openPlotOverlappingSummery(View v) {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialog_overlap_plot_summery, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();


        final TextInputLayout rl_report_factory = mView.findViewById(R.id.rl_report_factory);
        final TextInputLayout rl_report_super_wiser_code_from = mView.findViewById(R.id.rl_report_super_wiser_code_from);
        final TextInputLayout rl_report_super_wiser_code_to = mView.findViewById(R.id.rl_report_super_wiser_code_to);
        final TextInputLayout rl_report_super_wiser_village_from = mView.findViewById(R.id.rl_report_super_wiser_village_from);
        final TextInputLayout rl_report_super_wiser_village_to = mView.findViewById(R.id.rl_report_super_wiser_village_to);
        final TextInputLayout rl_report_percent = mView.findViewById(R.id.rl_report_percent);

        final Spinner report_factory = mView.findViewById(R.id.report_factory);
        report_super_wiser_code_from = mView.findViewById(R.id.report_super_wiser_code_from);
        report_super_wiser_code_to = mView.findViewById(R.id.report_super_wiser_code_to);
        report_super_wiser_village_from = mView.findViewById(R.id.report_super_wiser_village_from);
        report_super_wiser_village_to = mView.findViewById(R.id.report_super_wiser_village_to);
        final EditText report_percent = mView.findViewById(R.id.report_percent);
        rl_report_super_wiser_code_from.setVisibility(View.GONE);
        rl_report_super_wiser_code_to.setVisibility(View.GONE);
        report_super_wiser_code_from.setText("0");
        report_super_wiser_code_to.setText("9999");
        report_super_wiser_village_from.setText("0");
        report_super_wiser_village_to.setText("999999");

        rl_report_super_wiser_code_from.setVisibility(View.VISIBLE);
        rl_report_super_wiser_code_to.setVisibility(View.VISIBLE);

        //e1.setBackground(getDrawable(R.color.color_yellow));
        Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_super_wiser_village_from.getText().toString().length() == 0 || report_super_wiser_village_from.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_from.setError("Please enter from village code max 6 digits");
                } else if (report_super_wiser_village_to.getText().toString().length() == 0 || report_super_wiser_village_to.getText().toString().length() > 6) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(true);
                    rl_report_super_wiser_village_to.setError("Please enter to village code max 6 digits");
                } else if (report_percent.getText().toString().length() == 0) {
                    rl_report_super_wiser_code_from.setErrorEnabled(false);
                    rl_report_super_wiser_code_to.setErrorEnabled(false);
                    rl_report_super_wiser_village_from.setErrorEnabled(false);
                    rl_report_percent.setErrorEnabled(true);
                    rl_report_percent.setError("Please enter percent");
                } else {
                    if (report_super_wiser_code_from.getText().toString().length() == 0 || report_super_wiser_code_from.getText().toString().length() > 4) {
                        rl_report_factory.setErrorEnabled(false);
                        rl_report_super_wiser_code_from.setErrorEnabled(true);
                        rl_report_super_wiser_code_from.setError("Please enter from supervisor code max 4 digits");
                    } else if (report_super_wiser_code_to.getText().toString().length() == 0 || report_super_wiser_code_to.getText().toString().length() > 4) {
                        rl_report_factory.setErrorEnabled(false);
                        rl_report_super_wiser_code_from.setErrorEnabled(false);
                        rl_report_super_wiser_code_to.setErrorEnabled(true);
                        rl_report_super_wiser_code_to.setError("Please enter to supervisor code max 4 digits");
                    } else {
                        Intent intent = new Intent(context, PlotOverLapping.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("SupvcodeFrom", report_super_wiser_code_from.getText().toString());
                        intent.putExtra("SupvcodeTo", report_super_wiser_code_to.getText().toString());
                        intent.putExtra("VillageFrom", report_super_wiser_village_from.getText().toString());
                        intent.putExtra("VillageTo", report_super_wiser_village_to.getText().toString());
                        intent.putExtra("report_percent", report_percent.getText().toString());
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                }
            }
        });
        alertDialog.show();
        //alertDialog.setCancelable(false);
        //alertDialog.setCanceledOnTouchOutside(false);
    }

    public void openStaffSurveyReportMap(View v) {
        new GetVillageData().execute();
    }


    private void openMap(JSONArray jsonVillage) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_report_map_draw_grower, null);
            dialogbilder.setView(mView);
            final Spinner village = mView.findViewById(R.id.tv_village);
            final Spinner landType = mView.findViewById(R.id.tv_land_type);
            final Spinner borderTree = mView.findViewById(R.id.tv_border_tree);
            final Spinner method = mView.findViewById(R.id.tv_method);
            //warehouseModalList;
            final AutoCompleteTextView tv_grower = mView.findViewById(R.id.tv_grower);

            //List<VillageSurveyModel> villageModelList=new ArrayList<>();
            //villageModelList=dbh.getVillageModel("");
            ArrayList<String> dataVillage = new ArrayList<String>();
            ArrayList<String> dataLandType = new ArrayList<String>();
            ArrayList<String> dataBorderTree = new ArrayList<String>();
            ArrayList<String> dataMethod = new ArrayList<String>();
            MasterDropDown masterDropDown1 = new MasterDropDown();
            masterDropDown1.setCode("0");
            masterDropDown1.setName("");
            dataLandType.add("All Land Type");
            dataBorderTree.add("All Border Tree");
            dataMethod.add("All Method");
            villageList.add(masterDropDown1);
            landTypeList.add(masterDropDown1);
            borderTypeList.add(masterDropDown1);
            ploughingMethodList.add(masterDropDown1);
            for (int i = 0; i < jsonVillage.length(); i++) {
                JSONObject jsonObject = jsonVillage.getJSONObject(i);
                MasterDropDown masterDropDown = new MasterDropDown();
                masterDropDown.setCode(jsonObject.getString("CODE").replace(".0", ""));
                masterDropDown.setName(jsonObject.getString("NAME"));
                if (jsonObject.getString("MSTCD").equalsIgnoreCase("VILLAGE")) {
                    dataVillage.add(jsonObject.getString("CODE").replace(".0", "") + "/" + jsonObject.getString("NAME"));
                    villageList.add(masterDropDown);
                }
                if (jsonObject.getString("MSTCD").equalsIgnoreCase("LANDTYPE")) {
                    dataLandType.add(jsonObject.getString("CODE").replace(".0", "") + "/" + jsonObject.getString("NAME"));
                    landTypeList.add(masterDropDown);
                }
                if (jsonObject.getString("MSTCD").equalsIgnoreCase("BORDERTREE")) {
                    dataBorderTree.add(jsonObject.getString("CODE").replace(".0", "") + "/" + jsonObject.getString("NAME"));
                    borderTypeList.add(masterDropDown);
                }
                if (jsonObject.getString("MSTCD").equalsIgnoreCase("SHOWINGMETHOD")) {
                    dataMethod.add(jsonObject.getString("CODE").replace(".0", "") + "/" + jsonObject.getString("NAME"));
                    ploughingMethodList.add(masterDropDown);
                }

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.list_item, dataVillage);
            village.setAdapter(adapter);
            ArrayAdapter<String> adapterLandType = new ArrayAdapter<String>(context,
                    R.layout.list_item, dataLandType);
            landType.setAdapter(adapterLandType);
            ArrayAdapter<String> adapterBorderTree = new ArrayAdapter<String>(context,
                    R.layout.list_item, dataBorderTree);
            borderTree.setAdapter(adapterBorderTree);
            ArrayAdapter<String> adapterMethod = new ArrayAdapter<String>(context,
                    R.layout.list_item, dataMethod);
            method.setAdapter(adapterMethod);


            alertDialog = dialogbilder.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    try {
                        Intent intent = new Intent(context, RaSurveyReportMap.class);
                        intent.putExtra("village_code", villageList.get(village.getSelectedItemPosition()).getCode());
                        intent.putExtra("land_type", landTypeList.get(landType.getSelectedItemPosition()).getCode());
                        intent.putExtra("border_tree", borderTypeList.get(borderTree.getSelectedItemPosition()).getCode());
                        intent.putExtra("method", ploughingMethodList.get(method.getSelectedItemPosition()).getCode());
                        intent.putExtra("tv_grower", tv_grower.getText().toString());
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    private class GetVillageData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(RaSurveyDashboard.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaSurveyDashboard.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_MAPViLLDATADATA_NEW);
                request1.addProperty("Divn", userDetailsModelList.get(0).getDivision());
                request1.addProperty("User", userDetailsModelList.get(0).getCode());
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_MAPViLLDATADATA_NEW, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("MAPViLLDATADATA_NEWResult").toString();
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
            //dialog.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            dialog.dismiss();
            try {
                jsonVillageArray = new JSONArray(message);
                openMap(jsonVillageArray);
            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyDashboard.this, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyDashboard.this, "Error:" + e.toString());
            }
        }
    }

    private class GetMasterData extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(RaSurveyDashboard.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RaSurveyDashboard.this, getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetMasterList);
                request1.addProperty("factory", params[0]);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetMasterList, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetMasterListResult").toString();
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
            //dialog.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            dialog.dismiss();
            try {
                JSONObject jsonMasterObject = new JSONObject(message);
                jsonArray = jsonMasterObject.getJSONArray("Supervisor");
                jsonVillageArray = jsonMasterObject.getJSONArray("Village");
                jsonVarietyArray = jsonMasterObject.getJSONArray("Variety");
                setSupervisorList(jsonArray);
                setVillageList(jsonVillageArray);
                setVarietyList(jsonVarietyArray);
            } catch (JSONException e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyDashboard.this, message);
                //AlertPopUpFinish(message);
            } catch (Exception e) {
                new AlertDialogManager().AlertPopUpFinish(RaSurveyDashboard.this, "Error:" + e.toString());
            }
        }
    }

    private void setSupervisorList(JSONArray jsonArray) {
        try {
            supervisorModelList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SupervisorModel supervisorModel = new SupervisorModel();
                supervisorModel.setCode(jsonObject.getString("SUPVCODE").replace(".0", ""));
                supervisorModel.setName(jsonObject.getString("SUPVNAME"));
                supervisorModelList.add(supervisorModel);
            }
            supervisorAutoSearchAdapter = new SupervisorAutoSearchAdapter(RaSurveyDashboard.this, R.layout.all_list_row_item_search, supervisorModelList);
            report_super_wiser_code_from.setThreshold(1);
            report_super_wiser_code_from.setAdapter(supervisorAutoSearchAdapter);
            report_super_wiser_code_from.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                            report_super_wiser_code_from.setText(dataModels.getCode());
                        }
                    });

            report_super_wiser_code_from.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    supervisorAutoSearchAdapter.getFilter().filter(s);
                }
            });

            report_super_wiser_code_to.setThreshold(1);
            report_super_wiser_code_to.setAdapter(supervisorAutoSearchAdapter);
            report_super_wiser_code_to.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                            report_super_wiser_code_to.setText(dataModels.getCode());
                        }
                    });

            report_super_wiser_code_to.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    supervisorAutoSearchAdapter.getFilter().filter(s);
                }
            });
        } catch (Exception e) {

        }
    }

    private void setVillageList(JSONArray jsonVillageArray) {
        try {
            supervisorModelList = new ArrayList<>();
            for (int i = 0; i < jsonVillageArray.length(); i++) {
                JSONObject jsonObject = jsonVillageArray.getJSONObject(i);
                SupervisorModel supervisorModel = new SupervisorModel();
                supervisorModel.setCode(jsonObject.getString("VCODE").replace(".0", ""));
                supervisorModel.setName(jsonObject.getString("VNAME"));
                supervisorModelList.add(supervisorModel);
            }
            supervisorAutoSearchAdapter = new SupervisorAutoSearchAdapter(RaSurveyDashboard.this, R.layout.all_list_row_item_search, supervisorModelList);
            report_super_wiser_village_from.setThreshold(1);
            report_super_wiser_village_from.setAdapter(supervisorAutoSearchAdapter);
            report_super_wiser_village_from.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                            report_super_wiser_village_from.setText(dataModels.getCode());
                        }
                    });

            report_super_wiser_village_from.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    supervisorAutoSearchAdapter.getFilter().filter(s);
                }
            });

            report_super_wiser_village_to.setThreshold(1);
            report_super_wiser_village_to.setAdapter(supervisorAutoSearchAdapter);
            report_super_wiser_village_to.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                            report_super_wiser_village_to.setText(dataModels.getCode());
                        }
                    });

            report_super_wiser_village_to.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    supervisorAutoSearchAdapter.getFilter().filter(s);
                }
            });
        } catch (Exception e) {

        }
    }

    private void setVarietyList(JSONArray jsonVarietyArray) {
        try {
            if (report_variety_from != null) {

                supervisorModelList = new ArrayList<>();
                for (int i = 0; i < jsonVarietyArray.length(); i++) {
                    JSONObject jsonObject = jsonVarietyArray.getJSONObject(i);
                    SupervisorModel supervisorModel = new SupervisorModel();
                    supervisorModel.setCode(jsonObject.getString("VRCODE").replace(".0", ""));
                    supervisorModel.setName(jsonObject.getString("VRNAME"));
                    supervisorModelList.add(supervisorModel);
                }
                supervisorAutoSearchAdapter = new SupervisorAutoSearchAdapter(RaSurveyDashboard.this, R.layout.all_list_row_item_search, supervisorModelList);
                report_variety_from.setThreshold(1);
                report_variety_from.setAdapter(supervisorAutoSearchAdapter);
                report_variety_from.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                                report_variety_from.setText(dataModels.getCode());
                            }
                        });

                report_variety_from.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        supervisorAutoSearchAdapter.getFilter().filter(s);
                    }
                });

                report_variety_to.setThreshold(1);
                report_variety_to.setAdapter(supervisorAutoSearchAdapter);
                report_variety_to.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SupervisorModel dataModels = (SupervisorModel) parent.getItemAtPosition(position);
                                report_variety_to.setText(dataModels.getCode());
                            }
                        });

                report_super_wiser_village_to.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        supervisorAutoSearchAdapter.getFilter().filter(s);
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    public void openCropObservation(View v) {

        try {
            Intent intent = new Intent(context, StaffCropObservationCheckMapview.class);
            //Intent intent = new Intent(context, StaffCropObservation.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lng);
            startActivity(intent);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }


    }
        /*
        try
        {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
            dialogbilder.setView(mView);
            alertDialog = dialogbilder.create();
            ArrayList<String> divData=new ArrayList<String>();
            latLngAddress=mView.findViewById(R.id.err_msg);
            latLngAddress.setText("\nLattitude : --\nLongitude : --\nAccuracy : --\n\nAddress : --");
            Button b1 = (Button) mView.findViewById(R.id.btn_ok);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, StaffCropObservation.class);
                            intent.putExtra("lat", "" + lat);
                            intent.putExtra("lng", "" + lng);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"ErrorL:"+e.toString());
                    }
                }
            });
            Button recheck = (Button) mView.findViewById(R.id.recheck);
            recheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    catch (Exception e)
                    {
                        new AlertDialogManager().RedDialog(context,"ErrorL:"+e.toString());
                    }
                }
            });
            alertDialog.show();
        }
        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }*/



    public void openCropObservationReport(View v)
    {
        try{
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(RaSurveyDashboard.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_cane_observation_admin, null);
            dialogbilder.setView(mView);
            alertDialog = dialogbilder.create();
            TextView textView=mView.findViewById(R.id.textView);
            textView.setText("Search Cane Observation");


            TextInputLayout rl_report_super_wiser_code=mView.findViewById(R.id.rl_report_super_wiser_code);
            rl_report_super_wiser_code.setVisibility(View.VISIBLE);
            final TextView err_msg = mView.findViewById(R.id.err_msg);
            final EditText report_from_date = mView.findViewById(R.id.report_from_date);
            final EditText report_to_date = mView.findViewById(R.id.report_to_date);
            final EditText report_vill_code = mView.findViewById(R.id.report_vill_code);
            final EditText report_grower_code = mView.findViewById(R.id.report_grower_code);
            final Spinner report_supervisor_code = mView.findViewById(R.id.report_supervisor_code);


            List<MasterDropDown> supervisorList1=dbh.getMasterDropDown("23");
            MasterDropDown staffTargetModal1=new MasterDropDown();
            staffTargetModal1.setCode("");
            staffTargetModal1.setName("All Supervisor");
            supervisorList.add(staffTargetModal1);
            ArrayList<String> data=new ArrayList<String>();
            data.add("All Supervisor");
            for(int i=0;i<supervisorList1.size();i++)
            {
                MasterDropDown staffTargetModal=new MasterDropDown();
                staffTargetModal.setCode(supervisorList1.get(i).getCode());
                staffTargetModal.setName(supervisorList1.get(i).getName());
                data.add(supervisorList1.get(i).getCode()+" - "+supervisorList1.get(i).getName());
                supervisorList.add(staffTargetModal);
            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            report_supervisor_code.setAdapter(adaptersupply);
            report_from_date.setInputType(InputType.TYPE_NULL);
            report_from_date.setTextIsSelectable(true);
            report_from_date.setFocusable(false);
            report_from_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this,new DatePickerDialog.OnDateSetListener() {

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
                            report_from_date.setText(temDate+"-"+temmonth+"-"+year );
                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            report_to_date.setInputType(InputType.TYPE_NULL);
            report_to_date.setTextIsSelectable(true);
            report_to_date.setFocusable(false);
            report_to_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(RaSurveyDashboard.this,new DatePickerDialog.OnDateSetListener() {

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
                            report_to_date.setText(temDate+"-"+temmonth+"-"+year
                            );
                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
            //e1.setBackground(getDrawable(R.color.color_yellow));
            Button b1 = (Button) mView.findViewById(R.id.btn_sugar_submit);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(report_from_date.getText().toString().length()==0)
                    {
                        err_msg.setText("Please enter from date");
                    }
                    else if(report_to_date.getText().toString().length()==0)
                    {
                        err_msg.setText("Please enter to date");
                    }
                    else if(report_to_date.getText().toString().length()==0 && report_from_date.getText().toString().length()>0)
                    {
                        err_msg.setText("Please enter to date");
                    }
                    else
                    {
                        Intent intent = new Intent(RaSurveyDashboard.this, StaffCropObservationReport.class);
                        intent.putExtra("factory", userDetailsModelList.get(0).getDivision());
                        intent.putExtra("Village", report_vill_code.getText().toString());
                        intent.putExtra("Grower", report_grower_code.getText().toString());
                        intent.putExtra("DateFrom", report_from_date.getText().toString());
                        intent.putExtra("DateTo", report_to_date.getText().toString());
                        intent.putExtra("Supervisor", supervisorList.get(report_supervisor_code.getSelectedItemPosition()).getCode());
                        intent.putExtra("season", "");
                        startActivity(intent);
                        //finish();
                        while (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            });
            alertDialog.show();
            //alertDialog.setCancelable(false);
            //alertDialog.setCanceledOnTouchOutside(false);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error : "+e.toString());
        }
    }

}
