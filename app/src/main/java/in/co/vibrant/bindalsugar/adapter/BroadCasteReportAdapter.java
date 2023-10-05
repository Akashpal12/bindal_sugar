package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.BroadCasteReportModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class BroadCasteReportAdapter extends RecyclerView.Adapter<BroadCasteReportAdapter.MyHolder> {

    private Context context;
    private List<BroadCasteReportModel> arrayList;
    DBHelper dbh;
    AlertDialog AlertdialogBox;
    AlertDialog Alertdialog;
    Bitmap selectedImageBitmap;
    List<UserDetailsModel> userDetailsModelList;
    String BROADCAST;
    String V_CODE;
    String G_CODE;

    public BroadCasteReportAdapter(Context context, List<BroadCasteReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbh = new DBHelper(context);
        userDetailsModelList = dbh.getUserDetailsModel();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_broadcaste_report, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        try {
            //{"title":"Text Title","body":"Text Body","village":"1","grower":"144","intent":"Community","image_url":"https://www.science.org/cms/10.1126/science.aat4208/asset/70d21f5f-d391-4eb3-92e0-e66a9b8352d0/assets/graphic/359_1476_f1.jpeg"}
            holder.tvCount.setText("" + (position + 1));
            final JSONObject jsonObject = new JSONObject(arrayList.get(position).getMessage());
            if (jsonObject.getString("intent").equalsIgnoreCase("BROADCAST")) {
                holder.title.setText(jsonObject.getString("title"));
                holder.msg.setText(jsonObject.getString("body"));
                holder.time_ago.setText(arrayList.get(position).getDateTime());
                holder.send.setText(arrayList.get(position).getTotalSend());
                holder.view.setText(arrayList.get(position).getTotalView());
                holder.sucess.setText(arrayList.get(position).getTotalSucess());
                holder.fail.setText(arrayList.get(position).getTotalFailure());
                holder.send.setTextColor(Color.parseColor("#000000"));
                holder.view.setTextColor(Color.parseColor("#000000"));
                holder.sucess.setTextColor(Color.parseColor("#000000"));
                holder.fail.setTextColor(Color.parseColor("#000000"));

            } /*else {
                holder.title.setText(jsonObject.getString("title"));
                holder.msg.setText(jsonObject.getString("body"));
                holder.time_ago.setText(arrayList.get(position).getDateTime());
                holder.send.setText(arrayList.get(position).getTotalSend());
                holder.view.setText(arrayList.get(position).getTotalView());
                holder.sucess.setText(arrayList.get(position).getTotalSucess());
                holder.fail.setText(arrayList.get(position).getTotalFailure());
                holder.send.setTextColor(Color.parseColor("#000000"));
                holder.view.setTextColor(Color.parseColor("#000000"));
                holder.sucess.setTextColor(Color.parseColor("#000000"));
                holder.fail.setTextColor(Color.parseColor("#000000"));
            }*/

            holder.txt.setText(jsonObject.getString("intent"));
            if (jsonObject.getString("image_url").length() > 10) {
                Glide.with(this.context)
                        .load(jsonObject.getString("image_url"))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.profile_image);
                holder.tvCount.setVisibility(View.GONE);
                holder.txt.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.VISIBLE);
            } else {
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
            }
            holder.main_layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (jsonObject.getString("intent").equalsIgnoreCase("BROADCAST"))
                            BROADCAST=jsonObject.getString("BCID");
                            new UpdateBroadcastData().execute();
                        if (jsonObject.getString("image_url").endsWith(".mp4")) {
                            ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                        }
                        else if (jsonObject.getString("image_url").endsWith(".mp3")) {
                            ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                        }
                        else if (jsonObject.getString("image_url").endsWith(".jpg")) {
                            ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                        } else {
                            ShowText(jsonObject.getString("body"), jsonObject.getString("body"));
                        }
                       /* else {
                            if (jsonObject.getString("intent").equalsIgnoreCase("BROADCAST"))
                                BROADCAST=jsonObject.getString("BCID");
                              new UpdateBroadcastData().execute();
                            if (jsonObject.getString("image_url").endsWith(".mp4")) {
                                ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                            }
                           else if (jsonObject.getString("image_url").endsWith(".mp3")) {
                                ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                            }
                            else if (jsonObject.getString("image_url").endsWith(".jpg")) {
                                ShowVideo(jsonObject.getString("image_url"), jsonObject.getString("body"));

                            } else {
                                ShowText(jsonObject.getString("body"), jsonObject.getString("body"));


                            }


                        }*/
                    }
                    catch (Exception e) {

                    }
                }
            });


        } catch (Exception e) {
            //new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void ShowVideo(String imageUrl, String text) {
        try {
            JSONObject jsonObject = new JSONObject(arrayList.get(0).getMessage());
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_video_play_ext1, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            WebView videoView = mView.findViewById(R.id.video1);
            videoView.loadUrl(imageUrl);
            TextView showtext = mView.findViewById(R.id.text);
            showtext.setText(text);
            Button closeMap = mView.findViewById(R.id.closeMap);
            closeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertdialogBox.dismiss();
                }
            });
            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

    }

    public void ShowText(String imageUrl, String text) {
        try {
            JSONObject jsonObject = new JSONObject(arrayList.get(0).getMessage());
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_image_play_ext1, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            TextView txt = mView.findViewById(R.id.txt);
            txt.setText(text);
            Button closeMap = mView.findViewById(R.id.closeMap);
            closeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertdialogBox.dismiss();
                }
            });
            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        RelativeLayout main_layout1;
        //Button delete,edit;

        TextView tvCount, title, msg, time_ago, txt,send,view,sucess,fail,send1;
        CircleImageView profile_image;
        ImageView check, check1;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
            check1 = itemView.findViewById(R.id.check1);
            txt = itemView.findViewById(R.id.txt);
            tvCount = itemView.findViewById(R.id.tvCount);
            main_layout1 = itemView.findViewById(R.id.main_layout);
            title = itemView.findViewById(R.id.title);
            msg = itemView.findViewById(R.id.msg);
            time_ago = itemView.findViewById(R.id.time_ago);
            profile_image = itemView.findViewById(R.id.profile_image);
            send = itemView.findViewById(R.id.send);
            view = itemView.findViewById(R.id.view);
            sucess = itemView.findViewById(R.id.sucess);
            fail = itemView.findViewById(R.id.fail);
            send1 = itemView.findViewById(R.id.send1);

        }

        //public ImageView getImage(){ return this.phone_call;}
    }



    private class UpdateBroadcastData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Cane Development");
            //dialog.setIndeterminate(false);
            dialog.setMessage("Please Wait");
            /* dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            dialog.setProgress(0);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_UPDATEBROADCASTVIEW);
                request1.addProperty("SUPCODE", 0);
                request1.addProperty("CID", BROADCAST);
                request1.addProperty("DIVN", userDetailsModelList.get(0).getDivision());
                //request1.addProperty("SEAS", getString(R.string.season));
                request1.addProperty("SEAS", "2022-2023");
                request1.addProperty("VILLCODE",V_CODE);
                request1.addProperty("GROWCODE", G_CODE);

                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_UPDATEBROADCASTVIEW, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    message = ((SoapFault) envelope.bodyIn).getMessage();
                    return null;
                }
                message = ((SoapObject) envelope.bodyIn).getPropertyAsString("UPDATEBROADCASTVIEWResult").toString();
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
                    //new AlertDialogManager().AlertPopUpFinish(context, jsonObject.getString("MSG"));

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
    //-----------------------------------end the service call -----------------------------------

}