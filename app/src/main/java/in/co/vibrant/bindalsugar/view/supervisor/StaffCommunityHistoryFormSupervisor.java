package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
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
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.ListCommunityChatAdapter;
import in.co.vibrant.bindalsugar.model.CommunityChatDataModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.services.NotificationUtils;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.Config;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffCommunityHistoryFormSupervisor extends AppCompatActivity  {

    Context context;
    DBHelper dbh;
    EditText description;
    List<CommunityChatDataModel> expertHistoryModalList;
    String filename1 = "", pictureImagePath1 = "", filename2 = "", pictureImagePath2 = "";
    int RC_CAMERA_REQUEST_IMAGE_1 = 1001, RC_CAMERA_REQUEST_IMAGE_2 = 1002;
    AlertDialog Alertdialog;
    List<UserDetailsModel> userDetailsModels;
    String chatId="",villCode="",growCode="",villName="",growerName="",growerFatherName="";
    String COMUID;
    private final int FIVE_SECONDS = 30000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_ask_an_export_chat_form);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            context = StaffCommunityHistoryFormSupervisor.this;
            dbh = new DBHelper(context);
            userDetailsModels = dbh.getUserDetailsModel();
            expertHistoryModalList = new ArrayList<>();
            try{
                chatId=getIntent().getExtras().getString("chatId");
                villCode=getIntent().getExtras().getString("villCode");
                villName=getIntent().getExtras().getString("villName");
                growCode=getIntent().getExtras().getString("growCode");
                growerName=getIntent().getExtras().getString("growerName");
                growerFatherName=getIntent().getExtras().getString("growerFatherName");
                setTitle(growCode+" / "+growerName);
                toolbar.setTitle(growCode+" / "+growerName);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        //new AlertDialogManager().RedDialog(context,"Hello");         // this method will contain your almost-finished HTTP calls
                        handler.postDelayed(this, FIVE_SECONDS);
                    }
                }, FIVE_SECONDS);

                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // checking for type intent filter
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                            new getChatDetails().execute();
                            String message = intent.getStringExtra("refresh");
                            //Toast.makeText(getApplicationContext(), message+"123", Toast.LENGTH_LONG).show();

                        }
                    }
                };
            }
            catch (Exception e)
            {

            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            description = findViewById(R.id.description);

            new getChatDetails().execute();
            COMUID = getIntent().getExtras().getString("chatId");
            new  UpdateData().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    public void openCam (View v)
    {
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

    public void openRecorder (View v)
    {
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
        }
        catch (Exception e) {
            e.printStackTrace();
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
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

    public Bitmap drawTextToBitmap (Context gContext, String gText, String gText1, String path){
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

    public Bitmap ShrinkBitmap (String file,int width, int height)
    {
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


    public void saveDataOnServer (View v)
    {
        try {
            checkValidation:
            {
                if (description.getText().toString().length() == 0) {
                    new AlertDialogManager().RedDialog(context, "Please enter description ");
                    break checkValidation;
                }
                String imgFrmt1 ="";
                String imgFrmt ="";
                if (filename1.length() > 5) {
                    Bitmap bitmap1 = ShrinkBitmap(pictureImagePath1, 500, 500);//decodeFile(params[0]);
                    ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bao1);
                    byte[] byteFormat1 = bao1.toByteArray();
                    imgFrmt1 = Base64.encodeToString(byteFormat1, Base64.NO_WRAP);

                }
                if (filename2.length() > 5) {
                    FileInputStream fis = new FileInputStream(pictureImagePath2);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];

                    for (int readNum; (readNum = fis.read(b)) != -1; ) {
                        bos.write(b, 0, readNum);
                    }
                    byte[] byteFormat = bos.toByteArray();
                    imgFrmt = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    //new saveActivity().execute(issue_type.getSelectedItem().toString(), description.getText().toString(), imgFrmt1, imgFrmt);
                }
                new saveActivity().execute("", description.getText().toString(), imgFrmt1, imgFrmt);
            }
        }
        catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }
    }

    private class getChatDetails extends AsyncTask<Void, Integer, Void> {
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_GetCommunityHistoryData);
                request1.addProperty("ID", chatId);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber());
                request1.addProperty("u_code", userDetailsModels.get(0).getCode());
                request1.addProperty("lang", getString(R.string.language));
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_GetCommunityHistoryData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("GetCommunityHistoryDataResult").toString();
                }
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
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                    expertHistoryModalList=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        if(object.getString("DESCRIPTIONS").length()>0)
                        {
                            CommunityChatDataModel communityChatDataModel=new CommunityChatDataModel();
                            communityChatDataModel.setChatId(object.getString("ID"));
                            communityChatDataModel.setSenderId(object.getString("U_CODE"));
                            communityChatDataModel.setChatContentType("Text");
                            communityChatDataModel.setChatText(object.getString("DESCRIPTIONS"));
                            communityChatDataModel.setSender(userDetailsModels.get(0).getCode());
                            communityChatDataModel.setImageName(object.getString("IMAGE_NAME"));
                            communityChatDataModel.setImagePath(object.getString("IMAGE_PATH"));
                            communityChatDataModel.setVideoName(object.getString("VIDEO_NAME"));
                            communityChatDataModel.setVideoPath(object.getString("VIDEO_PATH"));
                            communityChatDataModel.setCreatedAt(object.getString("CDATE").replace("T"," "));
                            expertHistoryModalList.add(communityChatDataModel);
                        }
                        if(object.getString("IMAGE_NAME").length()>5)
                        {
                            CommunityChatDataModel communityChatDataModel=new CommunityChatDataModel();
                            communityChatDataModel.setChatId(object.getString("ID"));
                            communityChatDataModel.setSenderId(object.getString("U_CODE"));
                            communityChatDataModel.setChatContentType("Image");
                            communityChatDataModel.setChatText(object.getString("DESCRIPTIONS"));
                            communityChatDataModel.setSender(userDetailsModels.get(0).getCode());
                            communityChatDataModel.setImageName(object.getString("IMAGE_NAME"));
                            communityChatDataModel.setImagePath(object.getString("IMAGE_PATH"));
                            communityChatDataModel.setVideoName(object.getString("VIDEO_NAME"));
                            communityChatDataModel.setVideoPath(object.getString("VIDEO_PATH"));
                            communityChatDataModel.setCreatedAt(object.getString("CDATE").replace("T"," "));
                            expertHistoryModalList.add(communityChatDataModel);
                        }
                        if(object.getString("VIDEO_NAME").length()>5)
                        {
                            CommunityChatDataModel communityChatDataModel=new CommunityChatDataModel();
                            communityChatDataModel.setChatId(object.getString("ID"));
                            communityChatDataModel.setSenderId(object.getString("U_CODE"));
                            communityChatDataModel.setChatContentType("Video");
                            communityChatDataModel.setChatText(object.getString("DESCRIPTIONS"));
                            communityChatDataModel.setSender(userDetailsModels.get(0).getCode());
                            communityChatDataModel.setImageName(object.getString("IMAGE_NAME"));
                            communityChatDataModel.setImagePath(object.getString("IMAGE_PATH"));
                            communityChatDataModel.setVideoName(object.getString("VIDEO_NAME"));
                            communityChatDataModel.setVideoPath(object.getString("VIDEO_PATH"));
                            communityChatDataModel.setCreatedAt(object.getString("CDATE").replace("T"," "));
                            expertHistoryModalList.add(communityChatDataModel);
                        }
                    }

                    RecyclerView recyclerView =findViewById(R.id.recycler_list_history);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    ListCommunityChatAdapter stockSummeryAdapter =new ListCommunityChatAdapter(context,expertHistoryModalList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                    recyclerView.smoothScrollToPosition(recyclerView.getBottom());
                }
                /*else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }*/
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

    private class saveActivity extends AsyncTask<String, Integer, Void> {
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

                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_SaveCommunityHistoryData);
                request1.addProperty("divn", userDetailsModels.get(0).getDivision());
                request1.addProperty("u_code", userDetailsModels.get(0).getCode());
                request1.addProperty("id", chatId);
                request1.addProperty("descriptions", params[1]);
                request1.addProperty("image_name", params[2]);
                request1.addProperty("video_name", params[3]);
                request1.addProperty("vill_code", villCode);
                request1.addProperty("grower_code", growCode);
                request1.addProperty("IMEINO", imei);

                //request1.addProperty("PLOT_SR_NUMBER_ID", jsonData.getString("plotSerialId"));

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_SaveCommunityHistoryData, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("SaveCommunityHistoryDataResult").toString();
                }
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

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    new getChatDetails().execute();
                    description.setText("");
                    pictureImagePath1="";
                    pictureImagePath2="";
                    filename1="";
                    filename2="";
                    //Intent intent=new Intent(context,StaffMainActivity.class);
                    //new AlertDialogManager().AlertPopUpFinishWithIntent(context,jsonObject.getString("MSG"),intent);
                }
                else
                {
                    new AlertDialogManager().RedDialog(context,jsonObject.getString("MSG"));
                }
            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }


    ///////////--------------Update Notification Data-------------////////////

    private class UpdateData extends AsyncTask<String, Integer, Void> {
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

                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_UPDATENOTIFACTIONVIEW);
                request1.addProperty("SUPCODE", userDetailsModels.get(0).getCode());
                request1.addProperty("CID",COMUID);
                request1.addProperty("DIVN", userDetailsModels.get(0).getDivision());
                request1.addProperty("SEAS", getString(R.string.season));


                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_UPDATENOTIFACTIONVIEW, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("UPDATENOTIFACTIONVIEWResult").toString();
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
            if(dialog.isShowing())
                dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if(jsonObject.getString("API_STATUS").equalsIgnoreCase("OK"))
                {
                    // new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));


                }

            }
            catch(JSONException e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
            catch(Exception e)
            {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context,"Error:"+e.toString());
            }
        }
    }

}



