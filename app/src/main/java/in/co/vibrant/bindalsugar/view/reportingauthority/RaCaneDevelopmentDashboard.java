package in.co.vibrant.bindalsugar.view.reportingauthority;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.PlotFinderMapViewMaster;
import in.co.vibrant.bindalsugar.view.supervisor.StaffDevelopmentGrowerFinder;
import in.co.vibrant.bindalsugar.view.supervisor.StaffDevelopmentVeritalCheck;
import in.co.vibrant.bindalsugar.view.supervisor.StaffPlantingGrowerDetails;


public class RaCaneDevelopmentDashboard extends AppCompatActivity  {

    SQLiteDatabase db;
    Context context;

    AlertDialog dialogPopup;
    double lat, lng, accuracy;

    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest request;
    private LocationCallback locationCallback;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_cane_development_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= RaCaneDevelopmentDashboard.this;
        setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));
        toolbar.setTitle(getString(R.string.MENU_CANE_DEVELOPMENT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest();
        request.setInterval(10 * 1000);
        request.setFastestInterval(5 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };
    }

    public void openPlotFinderPathFinder(View v)
    {
        Intent intent=new Intent(context, StaffPlantingGrowerDetails.class);
        startActivity(intent);
    }


    public void openRaGrowerEnquiry(View v)
    {
        try {
            Intent intent = new Intent(context, RaStaffGrowerFinderMapView.class);
            //Intent intent = new Intent(context, StaffCropObservation.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lng);
            startActivity(intent);
        } catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

       /* try
        {
            final List<Address> addresses=new ArrayList<>();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(RaCaneDevelopmentDashboard.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            try {
                                if (location != null) {
                                    // Logic to handle location object
                                    Geocoder geocoder;
                                    final List<Address> addresses;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    String address = "";
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    opendialogue(addresses,location.getLatitude(), location.getLongitude(),location.getAccuracy());
                                } else {
                                    new AlertDialogManager().RedDialog(context,"Sorry location not found please try again");
                                }
                            }
                            catch (Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                        }
                    });
        }
        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }*/
    }

    private void opendialogue(List<Address> addresses, final double Lat, final double Lng,final double accuracy)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        ArrayList<String> divData=new ArrayList<String>();
        final TextView err_msg=mView.findViewById(R.id.err_msg);
        if(addresses.size()>0)
        {
            err_msg.setText("\nLattitude : "+Lat+"\nLongitude : "+Lng+"\nAccuracy : "+new DecimalFormat("0.00").format(accuracy) +"\n\nAddress : "+ addresses.get(0).getAddressLine(0));
        }
        Button b1 = (Button) mView.findViewById(R.id.btn_ok);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    while (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(context, StaffDevelopmentGrowerFinder.class);
                        intent.putExtra("lat", "" + Lat);
                        intent.putExtra("lng", "" + Lng);
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

    public void openRaVeritalCheck(View v)
    {

        try {
            //Intent intent = new Intent(context, RaStaffVerticalCheckMapView.class);
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


       /* try
        {
            final List<Address> addresses=new ArrayList<>();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(RaCaneDevelopmentDashboard.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            try {
                                if (location != null) {
                                    // Logic to handle location object
                                    Geocoder geocoder;
                                    final List<Address> addresses;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    String address = "";
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    opendialogueVeritalCheck(addresses,location.getLatitude(), location.getLongitude(),location.getAccuracy());
                                } else {
                                    new AlertDialogManager().RedDialog(context,"Sorry location not found please try again");
                                }
                            }
                            catch (Exception e)
                            {
                                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                        }
                    });
        }
        catch (SecurityException e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }*/
    }

    private void opendialogueVeritalCheck(List<Address> addresses, final double Lat, final double Lng,final double accuracy)
    {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_grower_finder, null);
        dialogbilder.setView(mView);
        alertDialog = dialogbilder.create();
        ArrayList<String> divData=new ArrayList<String>();
        final TextView err_msg=mView.findViewById(R.id.err_msg);
        if(addresses.size()>0)
        {
            err_msg.setText("\nLattitude : "+Lat+"\nLongitude : "+Lng+"\nAccuracy : "+new DecimalFormat("0.00").format(accuracy) +"\n\nAddress : "+ addresses.get(0).getAddressLine(0));
        }
        Button b1 = (Button) mView.findViewById(R.id.btn_ok);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    while (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(context, StaffDevelopmentVeritalCheck.class);
                        intent.putExtra("lat", "" + Lat);
                        intent.putExtra("lng", "" + Lng);
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

}
