package in.co.vibrant.bindalsugar.view.supervisor;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class BroadCastingDemo extends AppCompatActivity {

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    // string variable is created for storing a file name
    private static String mFileName = null;
    double lat, lng;
    String pictureImagePathCamera = "", pictureVideoPathCamera = "", pictureImagePathGallery = "", pictureVideoPathGallery = "";
    Bitmap selectedImageBitmap;
    int RC_CAMERA_REQUEST_IMAGE_1 = 1001, RC_CAMERA_VIDEO_REQUEST = 1002;
    int IMAGE_PICKER_SELECT = 1003, VIDEO_PICKER_SELECT = 1004;
    ImageView image, img_gallery, recdplay, recdstop, recdpause, recddelete;
    AlertDialog Alertdialog;
    BottomSheetDialog bottomSheetDialog;
    Context context;
    TextView listzone, list_CDO, list_village, list_grower, list_sup;
    EditText description, list_subject;
    DBHelper dbh;
    CheckBox chkgrower, chkemp;
    String SENDTYPE = "TEXT";
    LinearLayout linearLayout;
    boolean[] selected_zone, selected_CDO, select_supervisor, select_village, select_Grower;

    ArrayList<Integer> langList_circle = new ArrayList<>();
    ArrayList<Integer> langList_zone = new ArrayList<>();
    ArrayList<Integer> langList_village = new ArrayList<>();
    ArrayList<Integer> langList_supervisor = new ArrayList<>();
    ArrayList<Integer> langList_grower = new ArrayList<>();
    List<UserDetailsModel> userDetailsModelList;
    String zoneList = "";

    FusedLocationProviderClient fusedLocationClient;
    boolean isRecordingPath = false;
    boolean isPlayingPath = false;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    int dummySeconds = 0;
    int playableSeconds = 0;
    int seconds = 0;
    String path = null;
    Handler handler;
    LottieAnimationView lav_playing;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String address;
    private TextView startTV, stopTV, playTV, stopplayTV, statusTV, btndelete, recd;
    // creating a variable for medi recorder object class.
    private MediaRecorder mRecorder;
    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;
    private String pharmacyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_casting_demo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.Broad_Caste));
        toolbar.setTitle(getString(R.string.Broad_Caste));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = BroadCastingDemo.this;
        dbh = new DBHelper(this);
        userDetailsModelList = dbh.getUserDetailsModel();
        listzone = findViewById(R.id.listzone);
        list_sup = findViewById(R.id.list_sup);
        list_village = findViewById(R.id.list_village);
        list_grower = findViewById(R.id.list_grower);
        list_CDO = findViewById(R.id.list_CDO);
        description = findViewById(R.id.description);
        chkgrower = findViewById(R.id.chkgrower);
        chkemp = findViewById(R.id.chkemp);
        list_subject = findViewById(R.id.list_subject);
        image = findViewById(R.id.image);
        img_gallery = findViewById(R.id.img_gallery);
        recdplay = findViewById(R.id.recdplay);
        recdstop = findViewById(R.id.recdstop);
        recdpause = findViewById(R.id.recdpause);
        recddelete = findViewById(R.id.recddelete);
        {

            new GetZoneData().execute();
        }
        statusTV = findViewById(R.id.idTVstatus);
        startTV = findViewById(R.id.btnRecord);
        stopTV = findViewById(R.id.btnStop);
        playTV = findViewById(R.id.btnPlay);
        stopplayTV = findViewById(R.id.btnStopPlay);
        btndelete = findViewById(R.id.btndelete);
        recd = findViewById(R.id.recd);
        lav_playing = findViewById(R.id.lav_playing);
        linearLayout = findViewById(R.id.ll_recd);
        linearLayout.setVisibility(View.GONE);
        statusTV.setVisibility(View.GONE);
        lav_playing.setVisibility(View.GONE);

        recd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermissions()) {
                    if ((!isRecordingPath)) {
                        isRecordingPath = true;
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {

                                mediaRecorder = new MediaRecorder();
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                mediaRecorder.setOutputFile(getRecordingFilepath());
                                path = getRecordingFilepath();
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                try {
                                    mediaRecorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mediaRecorder.start();
                                startRecording();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recd.setVisibility(View.VISIBLE);
                                        startTV.setVisibility(View.VISIBLE);
                                        stopTV.setVisibility(View.VISIBLE);
                                        stopplayTV.setVisibility(View.GONE);
                                        btndelete.setVisibility(View.GONE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        statusTV.setVisibility(View.VISIBLE);
                                        lav_playing.setVisibility(View.GONE);
                                        playTV.setVisibility(View.VISIBLE);
                                        playableSeconds = 0;
                                        seconds = 0;
                                        dummySeconds = 0;
                                        runTimer();
                                    }

                                });

                            }


                        });
                    }


                } else {
                    RequestPermissions();
                }
            }
        });


        stopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause Recording method will
                // pause the recording of audio.
                recd.setVisibility(View.GONE);
                startTV.setVisibility(View.GONE);
                stopTV.setVisibility(View.GONE);
                stopplayTV.setVisibility(View.GONE);
                playTV.setVisibility(View.VISIBLE);
                btndelete.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                statusTV.setVisibility(View.VISIBLE);
                lav_playing.setVisibility(View.GONE);
                pauseRecording();

            }
        });
        playTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play audio method will play
                // the audio which we have recorded
                recd.setVisibility(View.GONE);
                startTV.setVisibility(View.GONE);
                stopTV.setVisibility(View.GONE);
                stopplayTV.setVisibility(View.VISIBLE);
                playTV.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                btndelete.setVisibility(View.VISIBLE);
                statusTV.setVisibility(View.VISIBLE);
                lav_playing.setVisibility(View.VISIBLE);


                playAudio();
            }
        });
        stopplayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause play method will
                // pause the play of audio
                recd.setVisibility(View.GONE);
                startTV.setVisibility(View.GONE);
                stopTV.setVisibility(View.GONE);
                stopplayTV.setVisibility(View.GONE);
                playTV.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                btndelete.setVisibility(View.VISIBLE);
                statusTV.setVisibility(View.VISIBLE);
                lav_playing.setVisibility(View.GONE);
                pausePlaying();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause play method will
                // pause the play of audio
                recd.setVisibility(View.VISIBLE);
                startTV.setVisibility(View.GONE);
                stopTV.setVisibility(View.GONE);
                stopplayTV.setVisibility(View.GONE);
                playTV.setVisibility(View.GONE);
                btndelete.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                statusTV.setVisibility(View.GONE);
                lav_playing.setVisibility(View.GONE);
                //deletePlaying();

                //pausePlaying();
            }
        });


    }

    private void startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record nd store the audio.
        if (CheckPermissions()) {

            // setbackgroundcolor method will change
            // the background color of text view.
           /* stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
            startTV.setBackgroundColor(getResources().getColor(R.color.gray));
            playTV.setBackgroundColor(getResources().getColor(R.color.gray));
            stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));*/
            //mFileName += "/AudioRecording.3gp";
            // we are here initializing our filename variable
            // with the path of the recorded audio file.
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/AudioRecording.mp3";
            SENDTYPE = "VOICERECORDER";
            // below method is used to initialize
            // the media recorder class
            mRecorder = new MediaRecorder();

            // below method is used to set the audio
            // source which we are using a mic.
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // below method is used to set
            // the output format of the audio.
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // below method is used to set the
            // audio encoder for our recorded audio.
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // below method is used to set the
            // output file location for our recorded audio
            mRecorder.setOutputFile(mFileName);
            try {
                // below method will prepare
                // our audio recorder class
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            // start method will start
            // the audio recording.
            mRecorder.start();
            statusTV.setText("Recording Started");
        } else {
            // if audio recording permissions are
            // not granted by user below method will
            // ask for runtime permission for mic and storage.
            RequestPermissions();
        }
    }

    private void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                startTV.setText(time);

                if (isRecordingPath || (isPlayingPath && playableSeconds != -1)) {
                    seconds++;
                    playableSeconds--;
                    if (playableSeconds == -1 && isPlayingPath) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        recd.setVisibility(View.VISIBLE);
                        //playTV.setImageDrawable(ContextCompat.getDrawable(StaffAnim.this, R.drawable.pause));
                        return;
                    }
                }
                handler.postDelayed(this, 1000);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(BroadCastingDemo.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    public void playAudio() {
        /*stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.gray));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));*/

        // for playing our recorded audio
        // we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer.setDataSource(mFileName);

            // below method will prepare our media player
            mPlayer.prepare();

            // below method will start our media player.
            mPlayer.start();
            statusTV.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
        /*stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.gray));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));*/

        // below method will stop
        // the audio recording.
        mRecorder.stop();

        // below method will release
        // the media recorder class.
        mRecorder.release();
        mRecorder = null;
        statusTV.setText("Recording Stopped");
    }

    public void pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;
       /* stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.gray));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));*/
        statusTV.setText("Recording Play Stopped");
    }

    public void deletePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;
       /* stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.gray));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));*/
        statusTV.setText("Recording Play Delete");
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
            String filename1 = "image_" + currentDt + ".jpg";
            pictureImagePathCamera = dir.getAbsolutePath() + "/" + filename1;
            File file = new File(pictureImagePathCamera);
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
            String filename2 = "image_" + currentDt + ".mp4";
            pictureVideoPathCamera = dir.getAbsolutePath() + "/" + filename2;
            File file = new File(pictureVideoPathCamera);
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
            startActivityForResult(intent, RC_CAMERA_VIDEO_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    public void opengallery(View v) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_SELECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openVideoGallery(View v) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_PICKER_SELECT);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void document(View v) {
        new AlertDialogManager().showToast((Activity) context, "Under construction");
    }

    public void contact(View v) {
        new AlertDialogManager().showToast((Activity) context, "Under construction");
    }

    public void openlocation(View v) {
       // new AlertDialogManager().showToast((Activity) context, "Under construction");
       /*Intent intent = new Intent(context, StaffBroadcasteMapView.class);
        intent.putExtra("lat", "" + lat);
        intent.putExtra("lng", "" + lng);
        startActivity(intent);*/
        // ShowMap();
        Intent intent = new Intent(context,LiveLocationMap.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CAMERA_REQUEST_IMAGE_1) { // For camera Image
            try {
                File file = new File(pictureImagePathCamera);
                if (file.exists()) {
                    selectedImageBitmap = BitmapFactory.decodeFile(pictureImagePathCamera);
                    AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_image_view, null);
                    dialogbilder.setView(mView);
                    Alertdialog = dialogbilder.create();
                    ImageView d_image = mView.findViewById(R.id.d_image);
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);
                    d_image.setImageBitmap(selectedImageBitmap);
                    SENDTYPE = "CAMERAIMAGE";
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
                            pictureImagePathCamera = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                pictureImagePathCamera = "";
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        } else if (requestCode == RC_CAMERA_VIDEO_REQUEST) {/// For Camera Video
            try {
                File file = new File(pictureVideoPathCamera);
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
                    Uri video = Uri.parse(pictureVideoPathCamera);
                    d_video.setMediaController(mediaController);
                    d_video.setKeepScreenOn(true);
                    d_video.setVideoPath(pictureVideoPathCamera);
                    d_video.requestFocus();
                    SENDTYPE = "CAMERAVIDEO";
                    d_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            d_video.start();
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.dismiss();
                            Alertdialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.dismiss();
                            Alertdialog.dismiss();
                            pictureVideoPathCamera = "";
                        }
                    });
                    Alertdialog.show();
                    Alertdialog.setCancelable(false);
                    Alertdialog.setCanceledOnTouchOutside(false);
                }
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        } else if (requestCode == IMAGE_PICKER_SELECT) {
            try {
                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialog_image_view, null);
                dialogbilder.setView(mView);
                Alertdialog = dialogbilder.create();
                ImageView d_image = mView.findViewById(R.id.d_image);
                Uri selectedImageUri = data.getData();
                try {
                    pictureImagePathGallery = getPath(selectedImageUri);
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    d_image.setImageBitmap(selectedImageBitmap);
                    SENDTYPE = "GALLERYIMAGE";
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Button save = mView.findViewById(R.id.save);
                Button cancel = mView.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Alertdialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Alertdialog.dismiss();
                    }
                });
                Alertdialog.show();
                Alertdialog.setCancelable(false);
                Alertdialog.setCanceledOnTouchOutside(false);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Sorry no image find");
            }

        } else if (requestCode == VIDEO_PICKER_SELECT) {
            try {
                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialog_video_view, null);
                dialogbilder.setView(mView);
                Alertdialog = dialogbilder.create();
                final VideoView d_video = mView.findViewById(R.id.d_image);
                Uri contentURI = data.getData();
                pictureVideoPathGallery = getPath(contentURI);
                Log.d("path", pictureVideoPathGallery);
                File currentFile = new File(pictureVideoPathGallery);

                SENDTYPE = "GALLERYVIDEO";
                Button save = mView.findViewById(R.id.save);
                Button cancel = mView.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Alertdialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Alertdialog.dismiss();
                        pictureVideoPathGallery = "";
                    }
                });
                Alertdialog.show();
                Alertdialog.setCancelable(false);
                Alertdialog.setCanceledOnTouchOutside(false);
                d_video.setVideoURI(contentURI);
                d_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        try {
                            int vidTime = d_video.getDuration();
                            if (vidTime > 30000) {
                                new AlertDialogManager().showToast((Activity) context, "Video length should be less than 30 second");
                                pictureVideoPathGallery = "";
                            } else {
                                d_video.requestFocus();
                                d_video.start();

                            }
                        } catch (Exception e) {
                            pictureVideoPathGallery = "";
                            new AlertDialogManager().showToast((Activity) context, "Video length should be less than 30 second");
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openalldocuments(View v) {
        bottomSheetDialog = new BottomSheetDialog(BroadCastingDemo.this, R.style.BottomSheet);
        View view1 = LayoutInflater.from(BroadCastingDemo.this).inflate(R.layout.activity_broad_cast_chat,
                (LinearLayout) findViewById(R.id.sheet));
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
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


    ////////////---------------Voicerecording------------//////////////////


    public void saveDataOnServer(View v) {
        try {
            String zonecode = "";
            String circlecode = "";
            String supcode = "";
            String villcode = "";
            String growcode = "";
            CheckValidation:
            {

                if (list_subject.getText().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please Enter Subject");
                    break CheckValidation;
                }

                if (listzone.getText().length() == 0) {
                    zonecode = "0";
                } else {
                    String zonelst = listzone.getText().toString();
                    String[] zonesting = zonelst.split(",");
                    for (int i = 0; i < zonesting.length; i++) {
                        String[] zonecodename = zonesting[i].split("-");
                        if (i == 0) {
                            zonecode = zonecodename[0].trim();

                        } else {
                            zonecode = zonecode + "," + zonecodename[0];
                        }
                    }
                }
                if (list_CDO.getText().length() == 0) {
                    circlecode = "0";
                   /* new AlertDialogManager().showToast(BroadCasting.this, "Please type CDO ");
                    break CheckValidation;*/
                } else {
                    String circlelst = list_CDO.getText().toString();
                    String[] circlesting = circlelst.split(",");
                    for (int i = 0; i < circlesting.length; i++) {
                        String[] circlecodename = circlesting[i].split("-");
                        if (i == 0) {
                            circlecode = circlecodename[0].trim();
                        } else {
                            circlecode = circlecode + "," + circlecodename[0];
                        }
                    }
                }
                if (list_sup.getText().length() == 0) {
                    supcode = "0";
                    /*new AlertDialogManager().showToast(BroadCasting.this, "Please type Supervisor ");
                    break CheckValidation;*/
                } else {
                    String suplst = list_sup.getText().toString();
                    String[] supervisor = suplst.split(",");
                    for (int i = 0; i < supervisor.length; i++) {
                        String[] supcodename = supervisor[i].split("-");
                        if (i == 0) {
                            supcode = supcodename[0].trim();
                        } else {
                            supcode = supcode + "," + supcodename[0];
                        }
                    }
                }
                if (list_village.getText().length() == 0) {
                    villcode = "0";
                    /*new AlertDialogManager().showToast(BroadCasting.this, "Please type Supervisor ");
                    break CheckValidation;*/
                } else {
                    String villlst = list_village.getText().toString();
                    String[] Village = villlst.split(",");
                    for (int i = 0; i < Village.length; i++) {
                        String[] villcodename = Village[i].split("-");
                        if (i == 0) {
                            villcode = villcodename[0].trim();
                        } else {
                            villcode = villcode + "," + villcodename[0];
                        }
                    }
                }
                if (list_grower.getText().length() == 0) {
                    growcode = "0";
                    /*new AlertDialogManager().showToast(BroadCasting.this, "Please type Supervisor ");
                    break CheckValidation;*/
                } else {
                    String growlst = list_grower.getText().toString();
                    String[] Grower = growlst.split(",");
                    for (int i = 0; i < Grower.length; i++) {
                        String[] growcodename = Grower[i].split("-");
                        if (i == 0) {
                            growcode = growcodename[0].trim();
                        } else {
                            growcode = growcode + "," + growcodename[0];
                        }
                    }
                }
                new saveData().execute(zonecode, circlecode, supcode, villcode, growcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRecordingFilepath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        // File file = new File(music, "testfile" + ".mp4");
        File file = new File(Environment.getExternalStorageDirectory() + "/hello-2.mp3");
        return file.getPath();

    }

    public void ShowMap() {
        AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_broadcast_on_map, null);
        dialogbilder.setView(mView);
        Alertdialog = dialogbilder.create();
        com.google.android.gms.maps.GoogleMap googleMap;
        //holder.ll.removeAllViewsInLayout();
        MapView mMapView = (MapView) mView.findViewById(R.id.mapView);
        MapsInitializer.initialize(context);
        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(Alertdialog.onSaveInstanceState());


        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
        Button closeMap = mView.findViewById(R.id.closeMap);
        closeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Alertdialog.dismiss();
            }
        });
        Button saveMap = mView.findViewById(R.id.share);
        saveMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Alertdialog.dismiss();
            }
        });
        Alertdialog.show();
        Alertdialog.setCancelable(false);
        Alertdialog.setCanceledOnTouchOutside(true);

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
                String SendGrow = "0";
                if (chkgrower.isChecked()) {
                    SendGrow = "1";
                }
                String SendEmp = "0";
                if (chkemp.isChecked()) {
                    SendEmp = "1";
                }
                String imei = new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SAVEBROADCAST);
                request1.addProperty("ZONE", params[0]);
                request1.addProperty("CIRCLE", params[1]);
                request1.addProperty("SUPCODE", params[2]);
                request1.addProperty("VILLAGE", params[3]);
                request1.addProperty("GROWER", params[4]);
                request1.addProperty("UCODE", userDetailsModelList.get(0).getCode());
                request1.addProperty("SENDGROWER", SendGrow);
                request1.addProperty("SENDSTOP", SendEmp);
                request1.addProperty("SENDTEXT", description.getText().toString());
                request1.addProperty("SUBJECT", list_subject.getText().toString());
                if (SENDTYPE == "CAMERAIMAGE") {
                    if (selectedImageBitmap != null) {
                        Bitmap bitmap1 = ShrinkBitmap(pictureImagePathCamera, 500, 500);//decodeFile(params[0]);
                        ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                        byte[] byteFormat1 = bao1.toByteArray();

                        String imgFrmt = Base64.encodeToString(byteFormat1, Base64.NO_WRAP);
                        request1.addProperty("DATA", imgFrmt);
                        request1.addProperty("SENDTYPE", "IMAGE");
                    }
                } else if (SENDTYPE == "CAMERAVIDEO") {
                    if (pictureVideoPathCamera.length() > 5) {
                        FileInputStream fis = new FileInputStream(pictureVideoPathCamera);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];
                        for (int readNum; (readNum = fis.read(b)) != -1; ) {
                            bos.write(b, 0, readNum);
                        }
                        byte[] byteFormat = bos.toByteArray();
                        String videoFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                        //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                        request1.addProperty("DATA", videoFrmt);
                        request1.addProperty("SENDTYPE", "VIDEO");
                    }
                } else if (SENDTYPE == "GALLERYIMAGE") {
                    Bitmap bitmap1 = ShrinkBitmap(pictureImagePathGallery, 500, 500);//decodeFile(params[0]);
                    ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                    byte[] byteFormat1 = bao1.toByteArray();

                    String imgFrmt = Base64.encodeToString(byteFormat1, Base64.NO_WRAP);
                    request1.addProperty("DATA", imgFrmt);
                    request1.addProperty("SENDTYPE", "IMAGE");
                } else if (SENDTYPE == "GALLERYVIDEO") {
                    if (pictureVideoPathGallery.length() > 5) {
                        FileInputStream fis = new FileInputStream(pictureVideoPathGallery);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];
                        for (int readNum; (readNum = fis.read(b)) != -1; ) {
                            bos.write(b, 0, readNum);
                        }
                        byte[] byteFormat = bos.toByteArray();
                        String videoFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                        //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                        request1.addProperty("DATA", videoFrmt);
                        request1.addProperty("SENDTYPE", "VIDEO");
                    }
                } else if (SENDTYPE == "VOICERECORDER") {
                    if
                    (path.length() > 5) {
                        //isRecordingPath = true;
                        FileInputStream fis = new FileInputStream((path));
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];
                        for (int readNum; (readNum = fis.read(b)) != -1; ) {
                            bos.write(b, 0, readNum);
                        }
                        byte[] byteFormat = bos.toByteArray();
                        String audioFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                        //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                        request1.addProperty("DATA", audioFrmt);
                        request1.addProperty("SENDTYPE", "AUDIO");
                    }
                } else {
                    request1.addProperty("DATA", "");
                    request1.addProperty("SENDTYPE", "TEXT");
                }
                request1.addProperty("DIVN", userDetailsModelList.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL_CANE_DEVELOPMENT, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SAVEBROADCAST, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("SAVEBROADCASTResult").toString();
                return null;
            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (!dialog.isShowing()) {
                    return null;
                }
                dialog.dismiss();
                return null;
            } catch (Exception e2) {
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
                    Intent intent = new Intent(context, BroadCastingDemo.class);
                    new AlertDialogManager().AlertPopUpFinishWithIntent(context, jsonObject.getString("MSG"), intent);
                } else {
                    new AlertDialogManager().RedDialog(context, jsonObject.getString("MSG"));
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
            }
        }
    }

    private class GetZoneData extends AsyncTask<String, Integer, Void> {
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

                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETZONEMASTER";
                HttpClient httpClient = new DefaultHttpClient();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("USERID", userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("", message);
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
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("0 - ALL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        data.add(object.getString("Code") + " - " + object.getString("Name"));

                    }
                    int size = data.size();
                    final String[] ListViewItems = data.toArray(new String[size]);
                    selected_zone = new boolean[ListViewItems.length];


                    listzone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(BroadCastingDemo.this);
                            // set title

                            //builder.setTitle("Select Zone");
                            // set dialog non cancelable
                            builder.setCancelable(false);

                            builder.setMultiChoiceItems(ListViewItems, selected_zone, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {


                                    // check condition
                                    if (b) {
                                        langList_zone.add(i);
                                        // Sort array list
                                        Collections.sort(langList_zone);
                                    } else {

                                        langList_zone.remove(Integer.valueOf(i));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // clear language list
                                    langList_circle.clear();
                                    StringBuilder stringBuilder = new StringBuilder();

                                    if (i == 0) {
                                        zoneList = "";
                                        //getGrower("");
                                    } else {
                                        String[] zone = listzone.getText().toString().split(" - ");
                                        zoneList = zone[0].trim();
                                        new GetCircleData().execute(zoneList);
                                    }
                                    //    new GetBlockData().execute();
                                    // use for loop
                                    for (int j = 0; j < langList_zone.size(); j++) {
                                        // concat array value
                                        stringBuilder.append(ListViewItems[langList_zone.get(j)]);
                                        // check condition
                                        if (j != langList_zone.size() - 1) {
                                            stringBuilder.append(",");
                                        }


                                    }
                                    // set text on textView
                                    listzone.setText(stringBuilder.toString());

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < selected_zone.length; j++) {
                                        // remove all selection
                                        selected_zone[j] = false;
                                        // clear language list
                                        langList_zone.clear();
                                        // clear text view value
                                        listzone.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();
                        }
                    });


                } else {
                    new AlertDialogManager().showToast((Activity) context, jsonObject.getString("MSG"));
                    if (dialog.isShowing())
                        dialog.dismiss();

                }

            } catch (JSONException e) {
                // new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }

    }

    private class GetCircleData extends AsyncTask<String, Integer, Void> {
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

                String zonelst = listzone.getText().toString();
                String zonecode = "";
                String[] zonesting = zonelst.split(",");
                for (int i = 0; i < zonesting.length; i++) {
                    String[] zonecodename = zonesting[i].split("-");
                    if (i == 0) {
                        zonecode = zonecodename[0].trim();
                    } else {
                        zonecode = zonecode + "," + zonecodename[0];
                    }
                }
                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETCIRCLEMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("USERID", userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("ZONE", zonecode));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("", message);
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
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("0 - ALL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        data.add(object.getString("Code") + " - " + object.getString("Name"));

                    }
                    int size = data.size();
                    final String[] ListViewItems = data.toArray(new String[size]);
                    selected_CDO = new boolean[ListViewItems.length];


                    list_CDO.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(BroadCastingDemo.this);
                            // set title
                            builder.setTitle("Select CDO");
                            // set dialog non cancelable
                            builder.setCancelable(false);

                            builder.setMultiChoiceItems(ListViewItems, selected_CDO, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    // check condition
                                    if (b) {
                                        langList_circle.add(i);
                                        // Sort array list
                                        Collections.sort(langList_circle);
                                    } else {

                                        // when checkbox unselected
                                        // Remove position from langList
                                        langList_circle.remove(Integer.valueOf(i));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // clear language list
                                    langList_supervisor.clear();
                                    // clear text view value
                                    // list_block.setText("");
                                    // Initialize string builder
                                    StringBuilder stringBuilder = new StringBuilder();
                                    new GETSUPMASTER().execute();
                                    // use for loop
                                    for (int j = 0; j < langList_circle.size(); j++) {
                                        // concat array value
                                        stringBuilder.append(ListViewItems[langList_circle.get(j)]);
                                        // check condition
                                        if (j != langList_circle.size() - 1) {
                                            stringBuilder.append(", ");
                                        }


                                    }
                                    // set text on textView
                                    list_CDO.setText(stringBuilder.toString());

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < selected_CDO.length; j++) {
                                        // remove all selection
                                        selected_CDO[j] = false;
                                        // clear language list
                                        langList_circle.clear();
                                        // clear text view value
                                        list_CDO.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();
                        }
                    });


                } else {
                    new AlertDialogManager().showToast((Activity) context, jsonObject.getString("MSG"));
                    if (dialog.isShowing())
                        dialog.dismiss();

                }

            } catch (JSONException e) {
                // new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }

    }

    private class GETSUPMASTER extends AsyncTask<String, Integer, Void> {
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

                String zonelst = listzone.getText().toString();
                String zonecode = "";
                String[] zonesting = zonelst.split(",");
                for (int i = 0; i < zonesting.length; i++) {
                    String[] zonecodename = zonesting[i].split("-");
                    if (i == 0) {
                        zonecode = zonecodename[0].trim();
                    } else {
                        zonecode = zonecode + "," + zonecodename[0];
                    }
                }

                //-------------------split block---------------

                String blocklst = list_CDO.getText().toString();
                String blockcode = "";
                String[] blocksting = blocklst.split(",");
                for (int i = 0; i < blocksting.length; i++) {
                    String[] blockcodename = blocksting[i].split("-");
                    if (i == 0) {
                        blockcode = blockcodename[0].trim();
                    } else {
                        blockcode = blockcode + "," + blockcodename[0];
                    }
                }


                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETSUPMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("USERID", userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("ZONE", zonecode));
                entity.add(new BasicNameValuePair("BLOCK", blockcode));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("", message);
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
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("0 - ALL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        data.add(object.getString("Code") + " - " + object.getString("Name"));

                    }
                    int size = data.size();
                    final String[] ListViewItems = data.toArray(new String[size]);
                    select_supervisor = new boolean[ListViewItems.length];


                    list_sup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(BroadCastingDemo.this);
                            // set title
                            builder.setTitle("Select Supervisor");
                            // set dialog non cancelable
                            builder.setCancelable(false);

                            builder.setMultiChoiceItems(ListViewItems, select_supervisor, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    // check condition
                                    if (b) {
                                        langList_supervisor.add(i);
                                        // Sort array list
                                        Collections.sort(langList_supervisor);
                                    } else {

                                        // when checkbox unselected
                                        // Remove position from langList
                                        langList_supervisor.remove(Integer.valueOf(i));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // clear language list
                                    langList_village.clear();
                                    // clear text view value
                                    // list_block.setText("");
                                    // Initialize string builder
                                    StringBuilder stringBuilder = new StringBuilder();
                                    new GETVILLAGEMASTER().execute();
                                    // use for loop
                                    for (int j = 0; j < langList_supervisor.size(); j++) {
                                        // concat array value
                                        stringBuilder.append(ListViewItems[langList_supervisor.get(j)]);
                                        // check condition
                                        if (j != langList_supervisor.size() - 1) {
                                            stringBuilder.append(", ");
                                        }


                                    }
                                    // set text on textView
                                    list_sup.setText(stringBuilder.toString());

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < select_supervisor.length; j++) {
                                        // remove all selection
                                        select_supervisor[j] = false;
                                        // clear language list
                                        langList_supervisor.clear();
                                        // clear text view value
                                        list_sup.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();
                        }
                    });


                } else {
                    new AlertDialogManager().showToast((Activity) context, jsonObject.getString("MSG"));
                    if (dialog.isShowing())
                        dialog.dismiss();

                }

            } catch (JSONException e) {
                // new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }

    }

    private class GETVILLAGEMASTER extends AsyncTask<String, Integer, Void> {
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

                String zonelst = listzone.getText().toString();
                String zonecode = "";
                String[] zonesting = zonelst.split(",");
                for (int i = 0; i < zonesting.length; i++) {
                    String[] zonecodename = zonesting[i].split("-");
                    if (i == 0) {
                        zonecode = zonecodename[0].trim();
                    } else {
                        zonecode = zonecode + "," + zonecodename[0];
                    }
                }

                //-------------------split block---------------


                String circlelst = list_CDO.getText().toString();
                String circlecode = "";
                String[] circlesting = circlelst.split(",");
                for (int i = 0; i < circlesting.length; i++) {
                    String[] circlecodename = circlesting[i].split("-");
                    if (i == 0) {
                        circlecode = circlecodename[0].trim();
                    } else {
                        circlecode = circlecode + "," + circlecodename[0];
                    }
                }

                String suplst = list_sup.getText().toString();
                String supcode = "";
                String[] supervisor = suplst.split(",");
                for (int i = 0; i < supervisor.length; i++) {
                    String[] supcodename = supervisor[i].split("-");
                    if (i == 0) {
                        supcode = supcodename[0].trim();
                    } else {
                        supcode = supcode + "," + supcodename[0];
                    }
                }

                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETVILLAGEMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("USERID", userDetailsModelList.get(0).getCode()));
                entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("ZONE", zonecode));
                entity.add(new BasicNameValuePair("CIRCLE", circlecode));
                entity.add(new BasicNameValuePair("SUPCODE", supcode));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("", message);
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
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("0 - ALL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        data.add(object.getString("Code") + " - " + object.getString("Name"));

                    }
                    int size = data.size();
                    final String[] ListViewItems = data.toArray(new String[size]);
                    select_village = new boolean[ListViewItems.length];


                    list_village.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(BroadCastingDemo.this);
                            // set title
                            builder.setTitle("Select Village");
                            // set dialog non cancelable
                            builder.setCancelable(false);

                            builder.setMultiChoiceItems(ListViewItems, select_village, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    // check condition
                                    if (b) {
                                        langList_village.add(i);
                                        // Sort array list
                                        Collections.sort(langList_village);
                                    } else {

                                        // when checkbox unselected
                                        // Remove position from langList
                                        langList_village.remove(Integer.valueOf(i));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // clear language list
                                    langList_grower.clear();
                                    // clear text view value
                                    //list_grower.setText("");
                                    // Initialize string builder
                                    StringBuilder stringBuilder = new StringBuilder();
                                    new GETGROWERMASTER().execute();
                                    // use for loop
                                    for (int j = 0; j < langList_village.size(); j++) {
                                        // concat array value
                                        stringBuilder.append(ListViewItems[langList_village.get(j)]);
                                        // check condition
                                        if (j != langList_village.size() - 1) {
                                            stringBuilder.append(", ");
                                        }


                                    }
                                    // set text on textView
                                    list_village.setText(stringBuilder.toString());

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < select_village.length; j++) {
                                        // remove all selection
                                        select_village[j] = false;
                                        // clear language list
                                        langList_village.clear();
                                        // clear text view value
                                        list_village.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();
                        }
                    });


                } else {
                    new AlertDialogManager().showToast((Activity) context, jsonObject.getString("MSG"));
                    if (dialog.isShowing())
                        dialog.dismiss();

                }

            } catch (JSONException e) {
                // new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }

    }

    private class GETGROWERMASTER extends AsyncTask<String, Integer, Void> {
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


                String zonelst = listzone.getText().toString();
                String zonecode = "";
                String[] zonesting = zonelst.split(",");
                for (int i = 0; i < zonesting.length; i++) {
                    String[] zonecodename = zonesting[i].split("-");
                    if (i == 0) {
                        zonecode = zonecodename[0].trim();
                    } else {
                        zonecode = zonecode + "," + zonecodename[0];
                    }
                }

                //-------------------split block---------------


                String circlelst = list_CDO.getText().toString();
                String circlecode = "";
                String[] circlesting = circlelst.split(",");
                for (int i = 0; i < circlesting.length; i++) {
                    String[] circlecodename = circlesting[i].split("-");
                    if (i == 0) {
                        circlecode = circlecodename[0].trim();
                    } else {
                        circlecode = circlecode + "," + circlecodename[0];
                    }
                }

                String suplst = list_sup.getText().toString();
                String supcode = "";
                String[] supervisor = suplst.split(",");
                for (int i = 0; i < supervisor.length; i++) {
                    String[] supcodename = supervisor[i].split("-");
                    if (i == 0) {
                        supcode = supcodename[0].trim();
                    } else {
                        supcode = supcode + "," + supcodename[0];
                    }
                }

                String villlst = list_village.getText().toString();
                String villcode = "";
                String[] Village = villlst.split(",");
                for (int i = 0; i < Village.length; i++) {
                    String[] villcodename = Village[i].split("-");
                    if (i == 0) {
                        villcode = villcodename[0].trim();
                    } else {
                        villcode = villcode + "," + villcodename[0];
                    }
                }

                String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/GETGROWERMASTER";
                HttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                entity.add(new BasicNameValuePair("DIVN", userDetailsModelList.get(0).getDivision()));
                entity.add(new BasicNameValuePair("VILLAGE", villcode));
                entity.add(new BasicNameValuePair("ZONE", zonecode));
                entity.add(new BasicNameValuePair("CIRCLE", circlecode));
                entity.add(new BasicNameValuePair("SUPCODE", supcode));
                entity.add(new BasicNameValuePair("SEAS", getString(R.string.season)));
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpPost, responseHandler);
                Log.d("", message);
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
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("0-ALL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        data.add(object.getString("Code") + " - " + object.getString("Name"));
                    }
                    int size = data.size();
                    final String[] ListViewItems = data.toArray(new String[size]);
                    select_Grower = new boolean[ListViewItems.length];
                    list_grower.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Initialize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(BroadCastingDemo.this);
                            // set title
                            builder.setTitle("Select Grower");
                            // set dialog non cancelable
                            builder.setCancelable(false);

                            builder.setMultiChoiceItems(ListViewItems, select_Grower, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    // check condition
                                    if (b) {
                                        langList_grower.add(i);
                                        // Sort array list
                                        Collections.sort(langList_grower);
                                    } else {

                                        // when checkbox unselected
                                        // Remove position from langList
                                        langList_grower.remove(Integer.valueOf(i));
                                    }
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // clear language list
                                    //langList_grower.clear();
                                    // clear text view value
                                    //     list_grower.setText("");
                                    // Initialize string builder
                                    StringBuilder stringBuilder = new StringBuilder();
                                    //    new GetBlockData().execute();
                                    // use for loop
                                    for (int j = 0; j < langList_grower.size(); j++) {
                                        // concat array value
                                        stringBuilder.append(ListViewItems[langList_grower.get(j)]);
                                        // check condition
                                        if (j != langList_grower.size() - 1) {
                                            stringBuilder.append(", ");
                                        }


                                    }
                                    // set text on textView
                                    list_grower.setText(stringBuilder.toString());

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // use for loop
                                    for (int j = 0; j < select_Grower.length; j++) {
                                        // remove all selection
                                        select_Grower[j] = false;
                                        // clear language list
                                        langList_village.clear();
                                        // clear text view value
                                        list_village.setText("");
                                    }
                                }
                            });
                            // show dialog
                            builder.show();
                        }
                    });


                } else {
                    new AlertDialogManager().showToast((Activity) context, jsonObject.getString("MSG"));
                    if (dialog.isShowing())
                        dialog.dismiss();

                }

            } catch (JSONException e) {
                // new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }

    }


}













