package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.DownloadMasterData;
import in.co.vibrant.bindalsugar.util.GenerateLogFile;
import in.co.vibrant.bindalsugar.view.grower.GrowerCaneDetailsDashboard;
import in.co.vibrant.bindalsugar.view.grower.GrowerPotatoDetailsDashboard;

public class StaffCaneDevelopmentDashboard extends AppCompatActivity {
    SQLiteDatabase db;
    Context context;
    AlertDialog dialogPopup;
    DBHelper dbh;
    String TAG = "StaffSurveyDownloadMaster";
    LocationRequest locationRequest;
    TextView txt_wtpdwtpr;
    AlertDialog alertDialog;
    double lat, lng, accuracy;
    Geocoder geocoder;
    TextView latLngAddress;
    List<UserDetailsModel> userDetailsModelList;
    List<MasterDropDown> masterDropDownList;
    List<VillageModal> villageModalList;
    List<ControlSurveyModel> controlSurveyModelList;
    List<FarmerModel> farmerModelList;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private void init(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        txt_wtpdwtpr = findViewById(R.id.txt_wtpdwtpr);

        setSupportActionBar(toolbar);
        context = StaffCaneDevelopmentDashboard.this;
        setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));
        toolbar.setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StaffDevelopmentDashboard.class);
                startActivity(intent);
                finish();
            }
        });

        dbh = new DBHelper(context);
        userDetailsModelList = new ArrayList<>();
        masterDropDownList = new ArrayList<>();
        controlSurveyModelList = new ArrayList<>();
        userDetailsModelList = dbh.getUserDetailsModel();
        masterDropDownList = dbh.getMasterDropDown("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_cane_development_dashboard);

        init();
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
                                if (APIUrl.isDebug) {
                                    lat = APIUrl.lat;
                                    lng = APIUrl.lng;
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

    public void openCaneDetails(View v) {
        Intent intent = new Intent(context, GrowerCaneDetailsDashboard.class);
        startActivity(intent);
    }

    public void openPotatoDetails(View v) {
        Intent intent = new Intent(context, GrowerPotatoDetailsDashboard.class);
        startActivity(intent);
    }

    public void openRemadial(View v) {
        Intent intent = new Intent(context, RemedialPlotsActivity.class);
        startActivity(intent);
    }

    public void openRemadialSummary(View v) {
        Intent intent = new Intent(context, GetRemedialSummaryReport.class);
        startActivity(intent);
    }

    public void openSeedIndenting(View v) {
        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {
                if (masterDropDownList.size() == 0) {
                    Intent intent = new Intent(context, StaffDownloadMasterData.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, "Your master data is not updated please update your master data by clicking \"Download Cane Deve. Master\"\n\nआपका मास्टर डेटा अपडेट नहीं किया गया है कृपया \"Download Cane Deve. Master\" पर क्लिक करके अपने मास्टर डेटा को अपडेट करें।", intent);
                    break CheckValidation;
                }


                Intent intent = new Intent(context, FieldStaffIndentingCaneArea.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openFieldStaffPlantingCaneArea(View v) {
        try {
            final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        new DownloadMasterData().DownloadMaster(context);
                                    }
                                    // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, FieldStaffPlantingCaneArea.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openFieldStaffPlotSeedMapping(View v) {

        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        new DownloadMasterData().DownloadMaster(context);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, FieldStaffPlotSeedMapping.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openFieldStaffPlotSeedReservation(View v) {
        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        new DownloadMasterData().DownloadMaster(context);
                                    }
                                    // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, FieldStaffPlotSeedReservation.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openStaffPlotSeedDistribution(View v) {
        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        new DownloadMasterData().DownloadMaster(context);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, StaffPlotSeedDistribution.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openStaffFieldServerDataActivity(View v) {
        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        new DownloadMasterData().DownloadMaster(context);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, StaffFieldServerDataActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void openFieldStaffPlotSpray(View v) {

        try {
            final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
            final List<PlotFarmerShareOldModel> farmerShareModelList = dbh.getPlotFarmerShareOldModelFilterData("", "");
            final List<PlantingModel> plantingModelList = dbh.getPlantingModel("", "", "", "", "");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0 || farmerShareModelList.size() == 0 ||
                        plantingModelList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master,Planting,Survey Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (masterDropDownList.size() == 0) {
                                            new DownloadMasterData().DownloadMaster(context);
                                        }
                                        if (farmerShareModelList.size() == 0) {
                                            new DownloadMasterData().GetDownloadSurvey(context);
                                            //new DownloadMasterData().downloadSurveyData(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                        }
                                        if (plantingModelList.size() == 0) {
                                            new DownloadMasterData().GetDownloadPlanting(context);
                                            //new DownloadMasterData().downloadPlantingData(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                        }

                                        // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;

                }

                Intent intent = new Intent(context, FieldStaffPlotSpray.class);
                startActivity(intent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openStaffCanePloughingEntry(View v) {

        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        new DownloadMasterData().DownloadMaster(context);
                                    }
                                    // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, StaffCanePloughingEntry.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openExportData(View v) {
        new GenerateLogFile(context).generateIndentData();
    }

    public void openImportData(View v) {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = getLayoutInflater().inflate(R.layout.dialogue_verify_password, null);
            dialogbilder.setView(mView);
            final EditText password = mView.findViewById(R.id.password);
            dialogPopup = dialogbilder.create();
            dialogPopup.show();
            dialogPopup.setCancelable(true);
            dialogPopup.setCanceledOnTouchOutside(true);
            Button btn_ok = mView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (password.getText().toString().length() == 0) {
                            new AlertDialogManager().RedDialog(context, "Please enter password");
                        } else {
                            if (password.getText().toString().equalsIgnoreCase("vispl")) {
                                new GenerateLogFile(context).importIndentData();
                            } else {
                                new AlertDialogManager().RedDialog(context, "Please enter valid password");
                            }
                        }
                    } catch (Exception e) {
                        new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                    }
                    dialogPopup.dismiss();
                }
            });
        } catch (SecurityException e) {

        }
    }

    public void openPlotFinderPathFinder(View v) {

        try {
            final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            final List<PlantingModel> plantingModelList = dbh.getPlantingModel("", "", "", "", "");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0 ||
                        plantingModelList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Planting,Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (masterDropDownList.size() == 0) {
                                            new DownloadMasterData().DownloadMaster(context);
                                        }

                                        if (plantingModelList.size() == 0) {
                                            new DownloadMasterData().GetDownloadPlanting(context);
                                            //new DownloadMasterData().downloadPlantingData(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                        }

                                        // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;

                }

                Intent intent = new Intent(context, StaffPlantingGrowerDetails.class);
                startActivity(intent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openGrowerEnquiry(View v) {
        try {

            Intent intent = new Intent(context, StaffDevelopmentGrowerFinder.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lng);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void openVeritalCheck(View v) {
        try {
            List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");

            CheckValidation:
            {

                if (masterDropDownList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        new DownloadMasterData().DownloadMaster(context);
                                    }
                                    // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                /*Intent intent = new Intent(context, StaffVerticalCheckMapview.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);*/


                /////--------------Survey VeritalCkeck Activity---------------///////////
                Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //------------------new activity-----------------------
    public void openCropObservation(View v) {


        try {
            final List<ControlSurveyModel> controlSurveyModelList = dbh.getControlSurveyModel("");
            final List<VillageSurveyModel> villageModel = dbh.getVillageModel("");
            final List<FarmerModel> farmerModel = dbh.getFarmerModel("");
            final List<MasterCaneSurveyModel> masterCaneSurveyModelList = dbh.getMasterModel("");
            CheckValidation:
            {

                if (controlSurveyModelList.size() == 0 || villageModel.size() == 0 ||
                        farmerModel.size() == 0 || masterCaneSurveyModelList.size() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Survey Master Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (controlSurveyModelList.size() == 0) {
                                            new DownloadMasterData().GetControlData(context);
                                            //new DownloadMasterData().GetControlData(context,userDetailsModelList.get(0).getDivision(),userDetailsModelList.get(0).getCode());
                                        }
                                        if (villageModel.size() == 0) {
                                            new DownloadMasterData().GetVillageData(context);
                                            //new DownloadMasterData().downloadVillageData(context,userDetailsModelList.get(0).getDivision(),userDetailsModelList.get(0).getCode());
                                        }
                                        if (farmerModel.size() == 0) {
                                            new DownloadMasterData().GetDownloadFarmer(context);
                                            //new DownloadMasterData().downloadfarmerData(context,userDetailsModelList.get(0).getDivision(),userDetailsModelList.get(0).getCode());
                                        }
                                        if (masterCaneSurveyModelList.size() == 0) {
                                            new DownloadMasterData().GetDownloadMaster(context);
                                            //new DownloadMasterData().downloadMasterData(context,userDetailsModelList.get(0).getDivision(),userDetailsModelList.get(0).getCode());
                                        }
                                        // new DownloadMasterData().downloadCaneMaster(context, userDetailsModelList.get(0).getDivision(), userDetailsModelList.get(0).getCode());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });


                    alertDialog.show();
                    break CheckValidation;
                }


                Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                //Intent intent = new Intent(context, StaffCropObservation.class);
                intent.putExtra("lat", "" + lat);
                intent.putExtra("lng", "" + lng);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openCropGrowthObservation(View v) {
        try {
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
    }

    public void openBrixPurcent(View v) {
        try {
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
    }

    //-----------------------end-----------------------------------


    public void openPlotActivity(View v) {
        try {
            final List<MasterDropDown> masterDropDownList = dbh.getMasterDropDown("");
            CheckValidation:
            {
                if (masterDropDownList.size() == 0) {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                            StaffCaneDevelopmentDashboard.this);
                    alertDialog.setMessage("Please Download The Cane Master , Planting Data");
                    alertDialog.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setNegativeButton("Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (masterDropDownList.size() == 0) {
                                            new DownloadMasterData().DownloadMaster(context);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            });

                    alertDialog.show();
                    break CheckValidation;
                }
                Intent intent = new Intent(context, PlotFinderMapViewMaster.class);
                startActivity(intent);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void RedDialog(final Context context, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.AlertDialogRed);
        //alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


}
