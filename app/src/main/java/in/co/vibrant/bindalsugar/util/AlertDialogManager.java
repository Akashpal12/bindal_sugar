package in.co.vibrant.bindalsugar.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.co.vibrant.bindalsugar.R;


/**
 * Created by jaimatadi on 22-Apr-16.
 */
public class AlertDialogManager {

    public void AlertPopUp(Context context,String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
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

    public void AlertPopUpFinish(final Context context, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) context).finish();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void AlertPopUpFinishWithIntent(final Context context, String msg, final Intent intent) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) context).finish();
                        context.startActivity(intent);
                    }
                });
        alertDialog.setCancelable(false);

        alertDialog.show();
    }

    public void RedDialog(final Context context,String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context,R.style.AlertDialogRed);
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

    public void GreenDialog(final Context context,String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context,R.style.AlertDialogGreen);
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

    public void showToast(Activity activity, String message) {
        LayoutInflater li = activity.getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.custom_toast_layout, (ViewGroup) activity.findViewById(R.id.custom_toast_layout));
        TextView textView = layout.findViewById(R.id.custom_toast_message);
        textView.setText(message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();

    }

}
