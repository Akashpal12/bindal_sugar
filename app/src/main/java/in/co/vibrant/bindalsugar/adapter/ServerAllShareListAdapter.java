package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.BluetoothPrint.BluetoothChat;
import in.co.vibrant.bindalsugar.BluetoothPrint.ESC;
import in.co.vibrant.bindalsugar.BluetoothPrint.Variables;
import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class ServerAllShareListAdapter extends RecyclerView.Adapter<ServerAllShareListAdapter.MyHolder> {

    List<PlotSurveyModel> uploadPlotSurveyModel;
    List<FarmerShareModel> uploadFarmerShareModel;
    List<UserDetailsModel> userDetailsModels;
    List<VillageSurveyModel> villageModelList;
    List<FarmerModel> farmerModelList;
    //List<FactoryModel> factoryModelList;
    private Context context;
    List<FarmerShareModel> arrayList;
    PlotSurveyModel uploadFarmerShareModel1;


    public ServerAllShareListAdapter(Context context, List<FarmerShareModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_server_data_share,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            if (position == 0) {
                holder.village_code.setText(arrayList.get(position).getVillageCode());
                holder.plot_sr_no.setText(arrayList.get(position).getGrowerCode());
                holder.survey_id.setText(arrayList.get(position).getSurveyId());
                holder.area_hec.setText(arrayList.get(position).getShare());
                holder.total_share.setText(arrayList.get(position).getServerStatus());
            } else {
                DBHelper dbHelper=new DBHelper(context);
                List<PlotSurveyModel> plotSurveyModels= dbHelper.getPlotSurveyModelById(arrayList.get(position).getSurveyId());
                if(plotSurveyModels.size()>0)
                {
                    holder.survey_id.setText(plotSurveyModels.get(0).getPlotSrNo()+" / "+arrayList.get(position).getSrNo());
                }
                holder.village_code.setText(arrayList.get(position).getVillageCode());
                holder.plot_sr_no.setText(arrayList.get(position).getGrowerCode());
                //holder.survey_id.setText(arrayList.get(position).getSurveyId());
                holder.area_hec.setText(arrayList.get(position).getShare());
                holder.total_share.setText(arrayList.get(position).getServerStatus());
                holder.rl_text_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertPopUp(arrayList.get(position).getServerStatusRemark());
                    }
                });
                holder.print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        printData(arrayList.get(position).getSurveyId(),""+arrayList.get(position).getColId());
                    }
                });
                holder.print.setVisibility(View.VISIBLE);
                /*holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        DBHelper dbh = new DBHelper(context);
                        farmerModelList = new ArrayList<>();
                        userDetailsModels = new ArrayList<>();
                        userDetailsModels = dbh.getUserDetailsModel();
                        //operatorModelList = dbh.getLoginOperatorModel();
                        villageModelList = dbh.getVillageModel("");
                        uploadFarmerShareModel = dbh.getFarmerShareUploadingDataById(arrayList.get(position).getColId());
                        if (uploadFarmerShareModel.size() > 0) {
                            uploadPlotSurveyModel = dbh.getPlotSurveyModelById(uploadFarmerShareModel.get(0).getSurveyId());
                            if (uploadPlotSurveyModel.size() > 0) {
                                if (new InternetCheck(context).isOnline()) {
                                    new UploadSurvey().execute();
                                } else {
                                    AlertPopUp("No internet connection");
                                }
                            }
                        }
                    }
                });
                */
                holder.edit.setVisibility(View.GONE);
            }
            holder.village_code.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.plot_sr_no.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.survey_id.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.area_hec.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.total_share.setTextColor(Color.parseColor(arrayList.get(position).getTextColor()));
            holder.rl_text_1.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
        }
        catch (Exception e)
        {
            //new GenerateLogFile(context).writeToFile("ServerPendingShareListAdapter.java"+e.toString());
        }

    }

    private void printData(String StrSurveyId,String formerShareId)
    {
        try {
            DBHelper dbh=new DBHelper(context);
            PlotSurveyModel uploadPlotSurveyModel=dbh.getPlotSurveyModelById(StrSurveyId).get(0);
            List<FarmerShareModel> uploadFarmerShareModelList=dbh.getFarmerShareModelById(formerShareId);
            FarmerShareModel uploadFarmerShareModel=uploadFarmerShareModelList.get(0);
            List<VillageSurveyModel> villageSurveyModelListprint = dbh.getVillageModel(uploadPlotSurveyModel.getVillageCode());
            List<MasterCaneSurveyModel> masterVarietyModelPrint = dbh.getMasterModelById("1", uploadPlotSurveyModel.getVarietyCode());
            List<MasterCaneSurveyModel> masterCaneTypeModelPrint = dbh.getMasterModelById("2", uploadPlotSurveyModel.getCaneType());
            List<FarmerModel> farmerDetails = dbh.getFarmerModel(uploadFarmerShareModel.getGrowerCode());
            //PrinterApi printerap = new PrinterApi();
            String printData = "SEASON: "+context.getString(R.string.season)+"\n";
            printData += "S.No. :" + uploadPlotSurveyModel.getPlotSrNo() + "\n";
            printData += "Date. :" + uploadPlotSurveyModel.getInsertDate() + "\n";
            printData += "Survey Village : " + villageSurveyModelListprint.get(0).getVillageName() + "\n";
            printData += "   DUPLICATE SLIP\n";
            printData += "========================\n";
            printData += "Plot Village: " + uploadPlotSurveyModel.getVillageCode() + "\n";
            printData += "Plot Sr No.:  " + uploadPlotSurveyModel.getPlotSrNo() + "\n";
            printData += "GrowerCode:" + farmerDetails.get(0).getFarmerCode() + "\n";
            printData += "UniqueCode:" + farmerDetails.get(0).getUniqueCode() + "\n";
            printData += "Name : " + uploadFarmerShareModel.getGrowerName() + "\n";
            printData += "Fath : " + uploadFarmerShareModel.getGrowerFatherName() + "\n";
            printData += "Vill : " + villageSurveyModelListprint.get(0).getVillageName() + "\n";
            printData += "East : " + uploadPlotSurveyModel.getEastSouthDistance() + "   West : " + uploadPlotSurveyModel.getWestNorthDistance() + "\n";
            printData += "North: " + uploadPlotSurveyModel.getEastNorthDistance() + "   South: " + uploadPlotSurveyModel.getWestSouthDistance() + "\n";
            printData += "Plot Share : " + uploadFarmerShareModel.getShare() + " : 100%\n";
            printData += "Variety: " + masterVarietyModelPrint.get(0).getName() + "\n";
            printData += "Cane Type : " + masterCaneTypeModelPrint.get(0).getName() + "\n";
            printData += "Cane Area : "+uploadPlotSurveyModel.getAreaHectare()+"\n";
            printData += "========================\n";
            printData += "\n";
            printData += "\n";
            //Bluetooth_Printer_2inch_Impact printapi = new Bluetooth_Printer_2inch_Impact();
            //	printerap.setPrintCommand(printapi.font_Double_Height_Width_Off());
            int i = 1;
            //while (true) {
            try {
                Intent intent = new Intent(context, BluetoothChat.class);
                intent.putExtra(Variables.PrintString, ESC.RESETPRINTER+ESC.NEWLINE+printData);
                intent.putExtra("printData", printData);
                context.startActivity(intent);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
        }
        catch (Exception e)
        {
            AlertPopUp("Error: "+e.toString());
        }
    }

    private class UploadSurvey extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name),
                    "Please wait", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String message="";
                String imei=new GetDeviceImei(context).GetDeviceImeiNumber();
                SoapObject request1 = new SoapObject(APIUrl.NAMESPACE, APIUrl.method_UpLoadSurveyNew);
                //request1.addProperty("EMEI", "864547042872734");//29-04-2022 11:48:22
                //request1.addProperty("imeiNo", "355844090708282");
                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                //Date today = Calendar.getInstance().getTime();
                Date dateObj2 = sdfIn.parse(uploadPlotSurveyModel.get(0).getInsertDate()+" 00:00:00");
                String currDate=dateFormat.format(dateObj2);
                request1.addProperty("FactId", userDetailsModels.get(0).getDivision());
                request1.addProperty("supvcd", uploadFarmerShareModel.get(0).getSupCode());
                request1.addProperty("survdttm", currDate);
                request1.addProperty("survvillcd", uploadPlotSurveyModel.get(0).getVillageCode());
                request1.addProperty("plotsrno", uploadPlotSurveyModel.get(0).getPlotSrNo());
                request1.addProperty("grwvillcd", uploadFarmerShareModel.get(0).getVillageCode());
                request1.addProperty("farmcd", uploadFarmerShareModel.get(0).getGrowerCode());
                request1.addProperty("isgpspts", "0");
                request1.addProperty("north", uploadPlotSurveyModel.get(0).getEastNorthDistance().replace(".0",""));
                request1.addProperty("east", uploadPlotSurveyModel.get(0).getEastSouthDistance().replace(".0",""));
                request1.addProperty("south", uploadPlotSurveyModel.get(0).getWestSouthDistance().replace(".0",""));
                request1.addProperty("west", uploadPlotSurveyModel.get(0).getWestNorthDistance().replace(".0",""));
                request1.addProperty("plotshareperc", uploadFarmerShareModel.get(0).getShare());
                request1.addProperty("shareno", uploadFarmerShareModel.get(0).getSrNo());
                request1.addProperty("vrtcd", uploadPlotSurveyModel.get(0).getVarietyCode());
                request1.addProperty("canetycd", uploadPlotSurveyModel.get(0).getCaneType());
                request1.addProperty("mobno", "");
                request1.addProperty("remark", uploadFarmerShareModel.get(0).getColId());
                request1.addProperty("gastino", uploadPlotSurveyModel.get(0).getGhashtiNumber());
                request1.addProperty("farmnm", uploadFarmerShareModel.get(0).getGrowerName());
                request1.addProperty("fathnm", uploadFarmerShareModel.get(0).getGrowerFatherName());
                request1.addProperty("aadharno", uploadFarmerShareModel.get(0).getGrowerAadharNumber());
                request1.addProperty("area", uploadPlotSurveyModel.get(0).getAreaHectare());
                request1.addProperty("plntmthcd", uploadPlotSurveyModel.get(0).getPlantMethod());
                request1.addProperty("cropcondcd", uploadPlotSurveyModel.get(0).getCropCondition());
                request1.addProperty("disecd", uploadPlotSurveyModel.get(0).getDisease());
                request1.addProperty("socclerkcd", uploadFarmerShareModel.get(0).getSupCode());
                request1.addProperty("plantationdt", uploadPlotSurveyModel.get(0).getPlantDate());
                request1.addProperty("irrcd", uploadPlotSurveyModel.get(0).getIrrigation());
                request1.addProperty("soilcd", uploadPlotSurveyModel.get(0).getSoilType());
                request1.addProperty("landcd", uploadPlotSurveyModel.get(0).getLandType());
                request1.addProperty("bordercropcd", uploadPlotSurveyModel.get(0).getBorderCrop());
                request1.addProperty("plottycd", uploadPlotSurveyModel.get(0).getPlotType());
                request1.addProperty("intercropcd", uploadPlotSurveyModel.get(0).getInterCrop());
                request1.addProperty("mixcropcd", uploadPlotSurveyModel.get(0).getMixCrop());
                request1.addProperty("insectcd", uploadPlotSurveyModel.get(0).getInsect());
                request1.addProperty("seedsourcecd", uploadPlotSurveyModel.get(0).getSeedSource());
                request1.addProperty("fort", "");
                request1.addProperty("isautumn", "0");
                request1.addProperty("sentsms", "0");
                request1.addProperty("isupdt", "0");
                request1.addProperty("nelatdeg",uploadPlotSurveyModel.get(0).getEastNorthLat());
                request1.addProperty("nelngdeg", uploadPlotSurveyModel.get(0).getEastNorthLng());
                request1.addProperty("selatdeg", uploadPlotSurveyModel.get(0).getEastSouthLat());
                request1.addProperty("selngdeg", uploadPlotSurveyModel.get(0).getEastSouthLng());
                request1.addProperty("swlatdeg", uploadPlotSurveyModel.get(0).getWestSouthLat());
                request1.addProperty("swlngdeg", uploadPlotSurveyModel.get(0).getWestSouthLng());
                request1.addProperty("nwlatdeg", uploadPlotSurveyModel.get(0).getWestNorthLat());
                request1.addProperty("nwlngdeg", uploadPlotSurveyModel.get(0).getWestNorthLng());
                request1.addProperty("KhasaraNo", uploadPlotSurveyModel.get(0).getKhashraNo());
                request1.addProperty("GataNo", uploadPlotSurveyModel.get(0).getGataNo());
                if(uploadPlotSurveyModel.get(0).getIsDelete()!=null)
                {
                    if(uploadPlotSurveyModel.get(0).getIsDelete().equalsIgnoreCase("1"))
                    {
                        request1.addProperty("ISDELETE", uploadPlotSurveyModel.get(0).getIsDelete());
                    }
                    else
                    {
                        request1.addProperty("ISDELETE", "0");
                    }
                }
                else
                {
                    request1.addProperty("ISDELETE", "0");
                }

                request1.addProperty("PTYPE", uploadPlotSurveyModel.get(0).getPlotType());
                request1.addProperty("PNO", uploadPlotSurveyModel.get(0).getOldPlotId());
                request1.addProperty("MachineID", imei);
                Log.d("", "doInBackground: " + request1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request1);
                envelope.implicitTypes = true;
                // Web method call

                HttpTransportSE androidHttpTransport = new HttpTransportSE(APIUrl.BASE_URL, 200000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(APIUrl.SOAP_ACTION_UpLoadSurveyNew, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault sf = (SoapFault) envelope.bodyIn;
                    message = sf.getMessage();
                } else {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    message = result.getPropertyAsString("UpLoadSurveyNewResult").toString();
                }

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("", "onPostExecute: " + result);
            if (message != null) {
                try{
                    Log.d("", message);
                    JSONObject jsonObject=new JSONObject(message);
                    DBHelper dbh = new DBHelper(context);
                    dbh.updateServerFarmerShareModel(jsonObject.getString("REMARK"),jsonObject.getString("STATUS"),jsonObject.getString("MESSAGE"));
                    AlertPopUpFinish(jsonObject.getString("MESSAGE"));
                }
                catch (Exception e)
                {
                    AlertPopUp(e.toString());
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } else {
                if (dialog.isShowing())
                    dialog.dismiss();
                //AlertPopUp(getString(R.string.technical_error));
            }
        }
    }

    private void AlertPopUp(String msg) {
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
        alertDialog.show();
    }

    private void AlertPopUpFinish(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(R.string.BTN_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(context, ServerAllShareListAdapter.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView edit,print;
        RelativeLayout rlLayout,rl_text_1;
        TextView village_code,plot_sr_no,survey_id,area_hec,total_share;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.edit);
            print=itemView.findViewById(R.id.print);
            village_code=itemView.findViewById(R.id.village_code);
            plot_sr_no=itemView.findViewById(R.id.plot_sr_no);
            survey_id=itemView.findViewById(R.id.survey_id);
            area_hec=itemView.findViewById(R.id.area_hec);
            total_share=itemView.findViewById(R.id.total_share);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
            //tv_date=itemView.findViewById(R.id.tv_status);
        }

        //public ImageView getImage(){ return this.imageView;}
    }



}
