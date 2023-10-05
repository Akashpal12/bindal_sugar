package in.co.vibrant.bindalsugar.util;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.NotificationModel;
import in.co.vibrant.bindalsugar.view.NotificationActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCommunityHistoryFormSupervisor;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    String message, title, imageUrl = "", timestamp;
    String isBackground;
    JSONObject payload;
    Bitmap bitmap;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            //int type = getSharedPreferences("login_info", MODE_PRIVATE).getInt("usertype", -1);
            Map<String, String> data = remoteMessage.getData();
            String description = remoteMessage.getNotification().getBody();
            String message = remoteMessage.getNotification().getTag();
            if(message==null)
            {
                message = remoteMessage.getNotification().getBody();
            }
            sendAction(message);
            JSONObject obj = new JSONObject(message);
            String title = remoteMessage.getNotification().getTitle();
            Uri imageUrl = remoteMessage.getNotification().getImageUrl();
            String isBackground = "";
            sendAction(message);
            //sendnotification(title,message);
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle(title);
            notificationModel.setMessage(message);
            notificationModel.setImageUrl(obj.getString("image_url"));
           // notificationModel.setSeen(obj.getInt("VIEWFLG"));
            notificationModel.setSeen(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String currDate = dateFormat.format(today);
            notificationModel.setDateTime(currDate);
            new DBHelper(getApplicationContext()).insertNotificationModel(notificationModel);
            if (obj.getString("image_url").length() > 10) {
                bitmap = getBitmapfromUrl(obj.getString("image_url"));
                sendNotificationImage(title, description, bitmap, isBackground);
                //sendnotification(obj.getString("title"), obj.getString("body"),obj);
            } else {
                sendnotification(title, description,obj);
            }
        }
        catch (Exception e) {

        }
    }

    private void sendnotification(String title, String message,JSONObject jsonObject) {
        try{
            Intent intent;
            if(jsonObject.getString("intent").equalsIgnoreCase("Community"))
            {
                intent = new Intent(getApplicationContext(), StaffCommunityHistoryFormSupervisor.class);
                //Intent intent=new Intent(context, StaffCommunityHistoryFormSupervisor.class);
                intent.putExtra("chatId",jsonObject.getString("COMUID"));
                intent.putExtra("villCode",jsonObject.getString("village"));
                intent.putExtra("villName",jsonObject.getString("villageName"));
                intent.putExtra("growCode",jsonObject.getString("grower"));
                intent.putExtra("growerName",jsonObject.getString("growerName"));
                intent.putExtra("growerFatherName",jsonObject.getString("growerFather"));
            }
            else{
                intent = new Intent(getApplicationContext(), NotificationActivity.class);
            }
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, PendingIntent.FLAG_IMMUTABLE);
            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();

                channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(
                            getApplicationContext(), "222")
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setLargeIcon(((BitmapDrawable) getDrawable(R.drawable.logo)).getBitmap())
                            .setSmallIcon(R.drawable.logo)
                            //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.electro))
                            .setContentText(message)
                            .setSmallIcon(R.drawable.logo)
                            .setContentIntent(pi);

            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            nm.notify(101, builder.build());
        }catch (Exception e)
        {

        }
    }


    private void sendNotificationImage(String title, String messageBody, Bitmap image, String TrueOrFalse) {
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(), "222")
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setLargeIcon(((BitmapDrawable) getDrawable(R.drawable.logo)).getBitmap())
                        .setSmallIcon(R.drawable.logo)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(image))
                        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.electro))
                        .setContentText(message)
                        .setSmallIcon(R.drawable.logo)
                        .setContentIntent(pi);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            /*URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;*/
            Bitmap image = Glide
                    .with(getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get();
            return image;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private void sendAction(String message) {
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }


}