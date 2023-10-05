package in.co.vibrant.bindalsugar.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class ListPendingIndentAdapter extends RecyclerView.Adapter<ListPendingIndentAdapter.MyHolder> {

    private Context context;
    List<IndentModel> arrayList;


    public ListPendingIndentAdapter(Context context, List<IndentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pending_indent,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if(position==0)
        {
            holder.tv_village_code.setText("Vill Code");
            holder.tv_village_name.setText("Farmer Name");
            holder.tv_code.setText("Plot Vill Code");
            holder.tv_name.setText("Area");
            holder.tv_server_status.setText("Server Status");
            holder.rl_text_1.setBackgroundColor(Color.parseColor("#000000"));

            holder.tv_village_code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_village_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_server_status.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            final int pos=position-1;
            holder.tv_village_code.setText(arrayList.get(pos).getVillage());
            holder.tv_village_name.setText(arrayList.get(pos).getGrower());
            holder.tv_code.setText(arrayList.get(pos).getPLOTVillage());
            holder.tv_name.setText(arrayList.get(pos).getArea());
            holder.tv_server_status.setText(arrayList.get(pos).getServerStatus());
            holder.rl_text_1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tv_village_code.setTextColor(Color.parseColor("#000000"));
            holder.tv_village_name.setTextColor(Color.parseColor("#000000"));
            holder.tv_code.setTextColor(Color.parseColor("#000000"));
            holder.tv_name.setTextColor(Color.parseColor("#000000"));
            holder.tv_server_status.setTextColor(Color.parseColor("#000000"));
            holder.rl_text_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertPopUp(arrayList.get(pos).getRemark());
                    //new ImageUploadTask().execute();
                }
            });
        }



    }


    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
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
        }
        catch (Exception e)
        {
            AlertPopUp("Error:"+e.toString());
        }
        return null;
    }


    class ImageUploadTask extends AsyncTask<String, Void, String> {

        String Content=null;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Uploading",
                    "Please wait while we transfer your data to server", true);
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String message;
                DBHelper dbh=new DBHelper(context);
                GetDeviceImei getDeviceImei=new GetDeviceImei(context);
                List<IndentModel> indentModelList=dbh.getIndentModel("No","","","","");
                List<UserDetailsModel> loginUserDetailsList=dbh.getUserDetailsModel();
                IndentModel indentModel=indentModelList.get(0);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/.CaneManagement/indent");
                String imgPath=dir.getAbsolutePath()+"/"+indentModel.getImage();
                String url= APIUrl.BASE_URL_CANE_DEVELOPMENT +"/SAVEINDENTINGOFFLINE";
                //String url = "http://192.168.10.103/AndroidFileUpload/fileUpload.php";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                //Bitmap bitmap = ShrinkBitmap(imgPath, 1500, 1200);//decodeFile(params[0]);

                File file=new File(imgPath);
                String imgFrmt="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";
                if(file.exists())
                {
                    Bitmap bitmap = ShrinkBitmap(imgPath, 1500, 1200);//decodeFile(params[0]);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    byte[] byteFormat = bao.toByteArray();
                    imgFrmt= Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                }


                //MultipartEntity entity = new MultipartEntity();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currentDt=dateFormat.format(today);
                /*ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] byteFormat = bao.toByteArray();
                String imgFrmt= Base64.encodeToString(byteFormat,Base64.NO_WRAP);*/
                entity.add(new BasicNameValuePair("Village",indentModel.getVillage()));
                entity.add(new BasicNameValuePair("Grower",indentModel.getGrower()));
                entity.add(new BasicNameValuePair("PLOTVillage",indentModel.getPLOTVillage()));
                entity.add(new BasicNameValuePair("Irrigationmode",indentModel.getIrrigationmode()));
                entity.add(new BasicNameValuePair("SupplyMode",indentModel.getSupplyMode()));
                entity.add(new BasicNameValuePair("Harvesting",indentModel.getHarvesting()));
                entity.add(new BasicNameValuePair("Equipment",indentModel.getEquipment()));
                entity.add(new BasicNameValuePair("LandType",indentModel.getLandType()));
                entity.add(new BasicNameValuePair("SeedType",indentModel.getSeedType()));
                entity.add(new BasicNameValuePair("SprayType",indentModel.getSprayType()));
                entity.add(new BasicNameValuePair("PloughingType",indentModel.getPloughingType()));
                entity.add(new BasicNameValuePair("NOFPLOTS",indentModel.getNOFPLOTS()));
                entity.add(new BasicNameValuePair("INDAREA",indentModel.getINDAREA()));
                entity.add(new BasicNameValuePair("InsertLAT",indentModel.getInsertLAT()));
                entity.add(new BasicNameValuePair("InsertLON",indentModel.getInsertLON()));
                entity.add(new BasicNameValuePair("MobilNO",indentModel.getMobilNO()));
                entity.add(new BasicNameValuePair("MDATE",indentModel.getMDATE()));
                entity.add(new BasicNameValuePair("VARIETY",indentModel.getVARIETY()));
                entity.add(new BasicNameValuePair("PLANTINGTYPE",indentModel.getPLANTINGTYPE()));
                entity.add(new BasicNameValuePair("SprayType",indentModel.getSprayType()));
                entity.add(new BasicNameValuePair("Crop",indentModel.getCrop()));
                entity.add(new BasicNameValuePair("PLANTATION",indentModel.getPLANTATION()));
                entity.add(new BasicNameValuePair("Dim1",indentModel.getDim1()));
                entity.add(new BasicNameValuePair("Dim2",indentModel.getDim2()));
                entity.add(new BasicNameValuePair("Dim3",indentModel.getDim3()));
                entity.add(new BasicNameValuePair("Dim4",indentModel.getDim4()));
                entity.add(new BasicNameValuePair("Area",indentModel.getArea()));
                entity.add(new BasicNameValuePair("LAT1",indentModel.getLAT1()));
                entity.add(new BasicNameValuePair("LON1",indentModel.getLON1()));
                entity.add(new BasicNameValuePair("LAT2",indentModel.getLAT2()));
                entity.add(new BasicNameValuePair("LON2",indentModel.getLON2()));
                entity.add(new BasicNameValuePair("LAT3",indentModel.getLAT3()));
                entity.add(new BasicNameValuePair("LON3",indentModel.getLON3()));
                entity.add(new BasicNameValuePair("LAT4",indentModel.getLAT4()));
                entity.add(new BasicNameValuePair("LON4",indentModel.getLON4()));
                entity.add(new BasicNameValuePair("SuperviserCode",indentModel.getSuperviserCode()));
                entity.add(new BasicNameValuePair("Image",imgFrmt));
                entity.add(new BasicNameValuePair("OTP",""));
                entity.add(new BasicNameValuePair("ACKID",indentModel.getColId()));
                entity.add(new BasicNameValuePair("SMathod",indentModel.getMethod()));
                entity.add(new BasicNameValuePair("GName",indentModel.getGrowerName()));
                entity.add(new BasicNameValuePair("GFName",indentModel.getGrowerFather()));

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                //httpPost.setEntity(entity);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = httpClient.execute(httpPost, responseHandler);
                //HttpResponse response = httpClient.execute(httpPost,localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("sdfsdsd : " + Content);

                //Content=response.getEntity().toString();
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Error : " + e.toString());
                AlertPopUp("Error: "+e.toString());
                //Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                JSONObject jsonObject = new JSONObject(Content);
                {
                    DBHelper dbh=new DBHelper(context);
                    dbh.updateServerStatusIndent(jsonObject.getString("EXCEPTIONMSG"),jsonObject.getString("ACKID"),"Failed");
                    AlertPopUp(jsonObject.getString("EXCEPTIONMSG"));
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.toString());
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        RelativeLayout rl_text_1;
        TextView tv_village_code,tv_village_name,tv_code,tv_name,tv_server_status;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_village_code=itemView.findViewById(R.id.tv_village_code);
            tv_village_name=itemView.findViewById(R.id.tv_village_name);
            tv_code=itemView.findViewById(R.id.tv_code);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_server_status=itemView.findViewById(R.id.tv_server_status);
            //upload_image=itemView.findViewById(R.id.upload_image);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
