package in.co.vibrant.bindalsugar.view.grower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.MixCropDialogueAdapter;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.LatLngUtil;


public class GrowerActivityForm extends AppCompatActivity  {

    Context context;
    ImageView image1,image2,image3,image4;
    String filename1,pictureImagePath1,filename2,pictureImagePath2,filename3,pictureImagePath3,filename4,pictureImagePath4;
    int RC_CAMERA_REQUEST_IMAGE_1=1001,RC_CAMERA_REQUEST_IMAGE_2=1002,RC_CAMERA_REQUEST_IMAGE_3=1003,RC_CAMERA_REQUEST_IMAGE_4=1004;
    TextView mixcrop;
    FusedLocationProviderClient fusedLocationClient;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_activities_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context= GrowerActivityForm.this;
        setTitle(getString(R.string.MENU_ACTIVITIES));
        toolbar.setTitle(getString(R.string.MENU_ACTIVITIES));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        saveBtn=findViewById(R.id.saveBtn);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);

        mixcrop=findViewById(R.id.mixcrop);
        mixcrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_mix_crop, null);
                dialogbilder.setView(mView);
                AlertDialog Alertdialog = dialogbilder.create();

                RecyclerView recycler_list=mView.findViewById(R.id.recycler_list);
                List<String> data=new ArrayList<>();
                data.add("Curliflower");
                data.add("Sugarcane");
                data.add("Potato");
                GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                //recyclerView.setLayoutManager(manager);
                //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                recycler_list.setHasFixedSize(true);
                recycler_list.setLayoutManager(manager);
                MixCropDialogueAdapter stockSummeryAdapter =new MixCropDialogueAdapter(context,data);
                recycler_list.setAdapter(stockSummeryAdapter);

                Alertdialog.show();
                Alertdialog.setCancelable(false);
                Alertdialog.setCanceledOnTouchOutside(true);
            }
        });
        checkGeofence();
    }

    private void checkGeofence()
    {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            boolean inside;
                            double lat=location.getLatitude();
                            double lng=location.getLongitude();
                            //26.873539, 81.015918
                            /*lat=26.873539;
                            lng=81.015918;
                            location.setLatitude(lat);
                            location.setLongitude(lng);*/
                            LatLng latlng=new LatLng(lat, lng);
                            List<LatLng> latLngList=new ArrayList<>();
                            latLngList.add(new LatLng(26.870510, 81.015314));
                            latLngList.add(new LatLng(26.870268, 81.015238));
                            latLngList.add(new LatLng(26.870235, 81.015395));
                            latLngList.add(new LatLng(26.870467, 81.015460));
                            if (location != null) {
                                inside = PolyUtil.containsLocation(latlng, latLngList, true);
                                if(!inside)
                                {
                                    LatLng nearPlotLatlng=new LatLngUtil().findNearestPoint(latlng,latLngList);
                                    Location nearLocation=new Location(LocationManager.GPS_PROVIDER);
                                    nearLocation.setLatitude(nearPlotLatlng.latitude);
                                    nearLocation.setLongitude(nearPlotLatlng.longitude);
                                    double distance=location.distanceTo(nearLocation);
                                    distance=distance/1000;
                                    DecimalFormat decimalFormat=new DecimalFormat("##.00");
                                    new AlertDialogManager().AlertPopUpFinish(context,"Sorry your are not inside your plot");
                                    saveBtn.setVisibility(View.GONE);
                                }
                                else
                                {
                                    saveBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        }
        catch(SecurityException e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        catch(Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
        //return inside;
    }

    public void captureImage1(View v)
    {
        openCam(1001);
    }

    public void captureImage2(View v)
    {
        openCam(1002);
    }

    public void captureImage3(View v)
    {
        openCam(1003);
    }

    public void captureImage4(View v)
    {
        openCam(1004);
    }

    public void openCam(int CAM_REQUEST)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            Date today = Calendar.getInstance().getTime();
            String currentDt=dateFormat.format(today);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.CaneDevelopment");
            dir.mkdirs();
            if(RC_CAMERA_REQUEST_IMAGE_1==CAM_REQUEST)
            {
                filename1 = "image_"+currentDt+".jpg";
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
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_1);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_2==CAM_REQUEST)
            {
                filename2 = "image_"+currentDt+".jpg";
                pictureImagePath2 = dir.getAbsolutePath() + "/" + filename2;
                File file = new File(pictureImagePath2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_2);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_3==CAM_REQUEST)
            {
                filename3 = "image_"+currentDt+".jpg";
                pictureImagePath3 = dir.getAbsolutePath() + "/" + filename3;
                File file = new File(pictureImagePath3);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_3);
            }
            else if(RC_CAMERA_REQUEST_IMAGE_4==CAM_REQUEST)
            {
                filename4 = "image_"+currentDt+".jpg";
                pictureImagePath4 = dir.getAbsolutePath() + "/" + filename4;
                File file = new File(pictureImagePath4);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                } else {
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent,RC_CAMERA_REQUEST_IMAGE_4);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_1) {
            try {
                File file = new File(pictureImagePath1);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath1);
                    image1.setImageBitmap(bmp);
                }
                else
                {
                    image1.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_2) {
            try {
                File file = new File(pictureImagePath2);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath2);
                    image2.setImageBitmap(bmp);
                }
                else
                {
                    image2.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_3) {
            try {
                File file = new File(pictureImagePath3);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath3);
                    image3.setImageBitmap(bmp);
                }
                else
                {
                    image3.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_4) {
            try {
                File file = new File(pictureImagePath4);
                if(file.exists())
                {
                    Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath4);
                    image4.setImageBitmap(bmp);
                }
                else
                {
                    image4.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                }
            } catch (Exception e) {

            }
        }
    }

}
