package in.co.vibrant.bindalsugar.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.PlantingEquipmentModel;
import in.co.vibrant.bindalsugar.model.PlantingItemModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;


public class GenerateLogFile {

    Context context;
    private ProgressDialog dialogBox ;

    public GenerateLogFile(Context context) {
        this.context = context;
    }

    public void writeToFile(String data) {
        File file = new File(context.getFilesDir(),"CaneSurvey");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, "Log.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();
        }catch (Exception e){

        }
    }


    public void generateIndentData() {
        try{
            new exportIndentData().execute();
        }catch (Exception e){
            AlertPopUp("Error : "+e.toString());
        }
    }

    public void generateSurveyData() {

        try{
            new exportPlotDataBackground().execute();
        }catch (Exception e){
            AlertPopUp("Error : "+e.toString());
        }
    }

    public void importSurveyData() {
        try{
            new importSurveyDataBackground().execute();
        }catch (Exception e){
            AlertPopUp("Error : "+e.toString());
        }
    }

    public void importIndentData() {
        try{
            new importDataBackground().execute();
        }catch (Exception e){
            AlertPopUp("Error : "+e.toString());
        }
    }

    private class exportIndentData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<IndentModel> indentModelList;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            indentModelList=dbh.getIndentModel("","","","","");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up indent list ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(indentModelList.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                String data="";
                data +="Id"+" , ";
                data +="Village"+" , ";
                data +="Grower"+" , ";
                data +="Grower Name"+" , ";
                data +="Grower Father"+" , ";
                data +="Plot Village"+" , ";
                data +="Irrigation"+" , ";
                data +="Supply"+" , ";
                data +="Harvesting"+" , ";
                data +="Equipment"+" , ";
                data +="Land Type"+" , ";
                data +="Seed Type"+" , ";
                data +="No Of Plots"+" , ";
                data +="Ind Area"+" , ";
                data +="Ins Lat"+" , ";
                data +="Ins Lng"+" , ";
                data +="Imagee"+" , ";
                data +="Superwisor"+" , ";
                data +="Dimension 1"+" , ";
                data +="Dimension 2"+" , ";
                data +="Dimension 3"+" , ";
                data +="Dimension 4"+" , ";
                data +="Area"+" , ";
                data +="Lat 1"+" , ";
                data +="Lng 1"+" , ";
                data +="Lat 2"+" , ";
                data +="Lng 2"+" , ";
                data +="Lat 3"+" , ";
                data +="Lng 3"+" , ";
                data +="Lat 4"+" , ";
                data +="Lng 4"+" , ";
                data +="Spray"+" , ";
                data +="Ploughing"+" , ";
                data +="Mobile"+" , ";
                data +="Date"+" , ";
                data +="Variety"+" , ";
                data +="Crop"+" , ";
                data +="Planting Type"+" , ";
                data +="Plantation"+" , ";
                data +="Method"+" , ";
                data +="Server Status"+" , ";
                data +="Server Remark"+" , ";
                data +="Current Date"+" , ";
                data +="Plot Sr Number"+" , ";
                data +="Is Ideal"+" , ";
                data +="Is Nursery"+" , ";
                data +="Ind Date"+"\n";
                for(int i=0;i<indentModelList.size();i++)
                {
                    publishProgress((int)i);
                    data +=indentModelList.get(i).getColId()+" , ";
                    data +=indentModelList.get(i).getVillage()+" , ";
                    data +=indentModelList.get(i).getGrower()+" , ";
                    data +=indentModelList.get(i).getGrowerName()+" , ";
                    data +=indentModelList.get(i).getGrowerFather()+" , ";
                    data +=indentModelList.get(i).getPLOTVillage()+" , ";
                    data +=indentModelList.get(i).getIrrigationmode()+" , ";
                    data +=indentModelList.get(i).getSupplyMode()+" , ";
                    data +=indentModelList.get(i).getHarvesting()+" , ";
                    data +=indentModelList.get(i).getEquipment()+" , ";
                    data +=indentModelList.get(i).getLandType()+" , ";
                    data +=indentModelList.get(i).getSeedType()+" , ";
                    data +=indentModelList.get(i).getNOFPLOTS()+" , ";
                    data +=indentModelList.get(i).getINDAREA()+" , ";
                    data +=indentModelList.get(i).getInsertLAT()+" , ";
                    data +=indentModelList.get(i).getInsertLON()+" , ";
                    data +=indentModelList.get(i).getImage()+" , ";
                    data +=indentModelList.get(i).getSuperviserCode()+" , ";
                    data +=indentModelList.get(i).getDim1()+" , ";
                    data +=indentModelList.get(i).getDim2()+" , ";
                    data +=indentModelList.get(i).getDim3()+" , ";
                    data +=indentModelList.get(i).getDim4()+" , ";
                    data +=indentModelList.get(i).getArea()+" , ";
                    data +=indentModelList.get(i).getLAT1()+" , ";
                    data +=indentModelList.get(i).getLON1()+" , ";
                    data +=indentModelList.get(i).getLAT2()+" , ";
                    data +=indentModelList.get(i).getLON2()+" , ";
                    data +=indentModelList.get(i).getLAT3()+" , ";
                    data +=indentModelList.get(i).getLON3()+" , ";
                    data +=indentModelList.get(i).getLAT4()+" , ";
                    data +=indentModelList.get(i).getLON4()+" , ";
                    data +=indentModelList.get(i).getSprayType()+" , ";
                    data +=indentModelList.get(i).getPloughingType()+" , ";
                    data +=indentModelList.get(i).getMobilNO()+" , ";
                    data +=indentModelList.get(i).getMDATE()+" , ";
                    data +=indentModelList.get(i).getVARIETY()+" , ";
                    data +=indentModelList.get(i).getCrop()+" , ";
                    data +=indentModelList.get(i).getPLANTINGTYPE()+" , ";
                    data +=indentModelList.get(i).getPLANTATION()+" , ";
                    data +=indentModelList.get(i).getMethod()+" , ";
                    data +=indentModelList.get(i).getServerStatus()+" , ";
                    data +=indentModelList.get(i).getRemark()+" , ";
                    data +=indentModelList.get(i).getCurrentDate()+" , ";
                    data +=indentModelList.get(i).getPlotSerialNumber()+" , ";
                    data +=indentModelList.get(i).getIsIdeal()+" , ";
                    data +=indentModelList.get(i).getIsNursery()+" , ";
                    data +=indentModelList.get(i).getIndDate()+"\n";
                }
                File gpxfile = new File(dir, "indent.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error : "+e.toString());
            }
            return null;
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
                AlertPopUp(indentModelList.size()+" indent data successfully exported");
                new exportPlantingData().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class exportPlantingData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<PlantingModel> plantings;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            plantings=dbh.getPlantingModel("","","","","");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up planting list ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(plantings.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                String data="";
                data +="Id"+" , ";
                data +="Village"+" , ";
                data +="Grower"+" , ";
                data +="Plot Village"+" , ";
                data +="Irrigation"+" , ";
                data +="Supply"+" , ";
                data +="Harvesting"+" , ";
                data +="Equipment"+" , ";
                data +="Land Type"+" , ";
                data +="Seed Type"+" , ";
                data +="Basal Dose"+" , ";
                data +="Sead Treatment"+" , ";
                data +="Method"+" , ";
                data +="Dimension 1"+" , ";
                data +="Dimension 2"+" , ";
                data +="Dimension 3"+" , ";
                data +="Dimension 4"+" , ";
                data +="Area"+" , ";
                data +="Lat 1"+" , ";
                data +="Lng 1"+" , ";
                data +="Lat 2"+" , ";
                data +="Lng 2"+" , ";
                data +="Lat 3"+" , ";
                data +="Lng 3"+" , ";
                data +="Lat 4"+" , ";
                data +="Lng 4"+" , ";
                data +="Ins Lat"+" , ";
                data +="Ins Lng"+" , ";
                data +="Imagee"+" , ";
                data +="Superwisor"+" , ";
                data +="Spray"+" , ";
                data +="Ploughing"+" , ";
                data +="Manual Area"+" , ";
                data +="Mobile"+" , ";
                data +="Date"+" , ";
                data +="Variety"+" , ";
                data +="Crop"+" , ";
                data +="Planting Type"+" , ";
                data +="Plantation"+" , ";
                data +="Seed TT"+" , ";
                data +="Seed Set"+" , ";
                data +="Soil Treatment"+" , ";
                data +="Row To Row"+" , ";
                data +="Actual Area Type"+" , ";
                data +="Im Area"+" , ";
                data +="S Village"+" , ";
                data +="S Grower"+" , ";
                data +="S Transporter"+" , ";
                data +="Distance"+" , ";
                data +="Seed Qty"+" , ";
                data +="Rate"+" , ";
                data +="Other Amount"+" , ";
                data +="Pay Amount"+" , ";
                data +="Pay Mode"+" , ";
                data +="Mill Purchey"+" , ";
                data +="Server Status"+" , ";
                data +="Server Remark"+" , ";
                data +="Current Date"+" , ";
                data +="Plot SR Number"+" , ";
                data +="Is Ideal"+" , ";
                data +="Is Nursery"+" , ";
                data +="Indent Id"+"\n";
                for(int i=0;i<plantings.size();i++)
                {
                    publishProgress((int)i);
                    data +=plantings.get(i).getColId()+" , ";
                    data +=plantings.get(i).getVillage()+" , ";
                    data +=plantings.get(i).getGrower()+" , ";
                    data +=plantings.get(i).getPLOTVillage()+" , ";
                    data +=plantings.get(i).getIrrigationmode()+" , ";
                    data +=plantings.get(i).getSupplyMode()+" , ";
                    data +=plantings.get(i).getHarvesting()+" , ";
                    data +=plantings.get(i).getEquipment()+" , ";
                    data +=plantings.get(i).getLandType()+" , ";
                    data +=plantings.get(i).getSeedType()+" , ";
                    data +=plantings.get(i).getBaselDose()+" , ";
                    data +=plantings.get(i).getSeedTreatment()+" , ";
                    data +=plantings.get(i).getSmMethod()+" , ";
                    data +=plantings.get(i).getDim1()+" , ";
                    data +=plantings.get(i).getDim2()+" , ";
                    data +=plantings.get(i).getDim3()+" , ";
                    data +=plantings.get(i).getDim4()+" , ";
                    data +=plantings.get(i).getArea()+" , ";
                    data +=plantings.get(i).getLAT1()+" , ";
                    data +=plantings.get(i).getLON1()+" , ";
                    data +=plantings.get(i).getLAT2()+" , ";
                    data +=plantings.get(i).getLON2()+" , ";
                    data +=plantings.get(i).getLAT3()+" , ";
                    data +=plantings.get(i).getLON3()+" , ";
                    data +=plantings.get(i).getLAT4()+" , ";
                    data +=plantings.get(i).getLON4()+" , ";
                    data +=plantings.get(i).getInsertLAT()+" , ";
                    data +=plantings.get(i).getInsertLON()+" , ";
                    data +=plantings.get(i).getImage()+" , ";
                    data +=plantings.get(i).getSuperviserCode()+" , ";
                    data +=plantings.get(i).getSprayType()+" , ";
                    data +=plantings.get(i).getPloughingType()+" , ";
                    data +=plantings.get(i).getManualArea()+" , ";
                    data +=plantings.get(i).getMobileNumber()+" , ";
                    data +=plantings.get(i).getmDate()+" , ";
                    data +=plantings.get(i).getVARIETY()+" , ";
                    data +=plantings.get(i).getCrop()+" , ";
                    data +=plantings.get(i).getPlantingType()+" , ";
                    data +=plantings.get(i).getPlantation()+" , ";
                    data +=plantings.get(i).getSeedType()+" , ";
                    data +=plantings.get(i).getSeedSetType()+" , ";
                    data +=plantings.get(i).getSoilTreatment()+" , ";
                    data +=plantings.get(i).getRowToRowDistance()+" , ";
                    data +=plantings.get(i).getActualAreaType()+" , ";
                    data +=plantings.get(i).getArea()+" , ";
                    data +=plantings.get(i).getSeedVillage()+" , ";
                    data +=plantings.get(i).getSeedGrower()+" , ";
                    data +=plantings.get(i).getSeedTransporter()+" , ";
                    data +=plantings.get(i).getSeedDistance()+" , ";
                    data +=plantings.get(i).getSeedQuantity()+" , ";
                    data +=plantings.get(i).getSeedRate()+" , ";
                    data +=plantings.get(i).getSeedOtherAmount()+" , ";
                    data +=plantings.get(i).getSeedPayAmount()+" , ";
                    data +=plantings.get(i).getSeedPayMode()+" , ";
                    data +=plantings.get(i).getMillPurchey()+" , ";
                    data +=plantings.get(i).getServerStatus()+" , ";
                    data +=plantings.get(i).getRemark()+" , ";
                    data +=plantings.get(i).getCurrentDate()+" , ";
                    data +=plantings.get(i).getPlotSerialNumber()+" , ";
                    data +=plantings.get(i).getIsIdeal()+" , ";
                    data +=plantings.get(i).getIsNursery()+" , ";
                    data +=plantings.get(i).getId()+"\n";
                }
                File gpxfile = new File(dir, "planting.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error : "+e.toString());
            }
            return null;
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
                AlertPopUp(plantings.size()+" planting data successfully exported");
                new exportEquipmentData().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class exportEquipmentData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<PlantingEquipmentModel> plantingsEquipment;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            plantingsEquipment=dbh.getPlantingEquipmentModel("");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up equipment list ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(plantingsEquipment.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                String data="";
                data +="Id"+" , ";
                data +="Code"+" , ";
                data +="Planting"+"\n";
                for(int i=0;i<plantingsEquipment.size();i++)
                {
                    publishProgress((int)i);
                    data +=plantingsEquipment.get(i).getColId()+" , ";
                    data +=plantingsEquipment.get(i).getCode()+" , ";
                    data +=plantingsEquipment.get(i).getPlantingCode()+"\n";
                }
                File gpxfile = new File(dir, "planting_equipment.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error : "+e.toString());
            }
            return null;
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
                AlertPopUp(plantingsEquipment.size()+" planting equipment data successfully exported");
                new exportPlantingItemData().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class exportPlantingItemData extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<PlantingItemModel> plantingItemModels;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            plantingItemModels=dbh.getPlantingItemModel("");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up planting item list ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(plantingItemModels.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                String data="";
                data +="Id"+" , ";
                data +="Code"+" , ";
                data +="Qty"+" , ";
                data +="Planting"+"\n";
                for(int i=0;i<plantingItemModels.size();i++)
                {
                    publishProgress((int)i);
                    data +=plantingItemModels.get(i).getColId()+" , ";
                    data +=plantingItemModels.get(i).getCode()+" , ";
                    data +=plantingItemModels.get(i).getQty()+" , ";
                    data +=plantingItemModels.get(i).getPlantingCode()+"\n";
                }
                File gpxfile = new File(dir, "planting_item.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error : "+e.toString());
            }
            return null;
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
                AlertPopUp(plantingItemModels.size()+" planting item data successfully exported");
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class exportPlotDataBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<PlotSurveyModel> plotSurveyModelList;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            plotSurveyModelList=dbh.getPlotSurveyModel("");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up plot list...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(plotSurveyModelList.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                String data="";
                data +="Id"+" , ";
                data +="Plot Sr No"+" , ";
                data +="Khashra Number"+" , ";
                data +="Gata Number"+" , ";
                data +="Village Code"+" , ";
                data +="Area Meter"+" , ";
                data +="Area Hectare"+" , ";
                data +="Mix Crop"+" , ";
                data +="Insect"+" , ";
                data +="Seed Source"+" , ";
                data +="Aadhar Number"+" , ";
                data +="Plant Method"+" , ";
                data +="Crop Condition"+" , ";
                data +="Disease"+" , ";
                data +="Plant Date"+" , ";
                data +="Irrigation"+" , ";
                data +="Soil Type"+" , ";
                data +="Land Type"+" , ";
                data +="Border Crop"+" , ";
                data +="Plot Type"+" , ";
                data +="Ghashti Number"+" , ";
                data +="Inter Crop"+" , ";
                data +="East North Lat"+" , ";
                data +="East North Lng"+" , ";
                data +="East North Accuracy"+" , ";
                data +="East North Distance"+" , ";
                data +="West North Lat"+" , ";
                data +="West North Lng"+" , ";
                data +="West North Accuracy"+" , ";
                data +="West North Distance"+" , ";
                data +="East South Lat"+" , ";
                data +="East South Lng"+" , ";
                data +="East South Accuracy"+" , ";
                data +="East South Distance"+" , ";
                data +="West South Lat"+" , ";
                data +="West South Lng"+" , ";
                data +="West South Accuracy"+" , ";
                data +="West South Distance"+" , ";
                data +="Variety Code"+" , ";
                data +="Cane Type"+" , ";
                data +="Insert Time"+"\n";
                for(int i=0;i<plotSurveyModelList.size();i++)
                {
                    publishProgress((int)i);
                    data +=new UpdateDataInDatabase(context).getPlotSurveyModel(plotSurveyModelList.get(i));
                }
                File gpxfile = new File(dir, "plot_backup.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(plotSurveyModelList.size()+" plot details successfully exported");
                new exportShareDataBackground().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class exportShareDataBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();
        List<FarmerShareModel> farmerShareModelList;
        DBHelper dbh = new DBHelper(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            farmerShareModelList=dbh.getFarmerShareModel("");
            dialog.setTitle("Please wait ...");
            dialog.setIndeterminate(false);
            dialog.setMessage("Backing up grower list...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(farmerShareModelList.size());
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }

                String data="";
                data +="Id"+" , ";
                data +="Survey Plot Id"+" , ";
                data +="Sr No"+" , ";
                data +="Village Code"+" , ";
                data +="Grower Code"+" , ";
                data +="Grower Name"+" , ";
                data +="Grower Father"+" , ";
                data +="Grower Aadhar"+" , ";
                data +="Share "+" , ";
                data +="Sup Code "+" , ";
                data +="Date "+" , ";
                data +="Server Status "+" , ";
                data +="Server Remark"+"\n";
                for(int i=0;i<farmerShareModelList.size();i++)
                {
                    publishProgress((int)i);
                    /*data +=farmerShareModelList.get(i).getColId()+" , ";
                    data +=farmerShareModelList.get(i).getSurveyId()+" , ";
                    data +=farmerShareModelList.get(i).getSrNo()+" , ";
                    data +=farmerShareModelList.get(i).getVillageCode()+" , ";
                    data +=farmerShareModelList.get(i).getGrowerCode()+" , ";
                    data +=farmerShareModelList.get(i).getGrowerName()+" , ";
                    data +=farmerShareModelList.get(i).getGrowerFatherName()+" , ";
                    data +=farmerShareModelList.get(i).getGrowerAadharNumber()+" , ";
                    data +=farmerShareModelList.get(i).getShare()+" , ";
                    data +=farmerShareModelList.get(i).getSupCode()+" , ";
                    data +=farmerShareModelList.get(i).getCurDate()+" , ";
                    data +=farmerShareModelList.get(i).getServerStatus()+" , ";
                    data +=farmerShareModelList.get(i).getServerStatusRemark()+"\n";*/
                    data +=new UpdateDataInDatabase(context).getFarmerShareModel(farmerShareModelList.get(i));
                }
                File gpxfile = new File(dir, "plot_share_backup.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(farmerShareModelList.size()+" plot share percentage data successfully exported");
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importDataBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "indent.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring indent list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"indent.txt");
                if(file.exists())
                {
                    dbh.truncateIndentModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            IndentModel indentModel=new IndentModel();
                            indentModel.setColId(tokens[0]);
                            indentModel.setVillage(tokens[1]);
                            indentModel.setGrower(tokens[2]);
                            indentModel.setGrowerName(tokens[3]);
                            indentModel.setGrowerFather(tokens[4]);
                            indentModel.setPLOTVillage(tokens[5]);
                            indentModel.setIrrigationmode(tokens[6]);
                            indentModel.setSupplyMode(tokens[7]);
                            indentModel.setHarvesting(tokens[8]);
                            indentModel.setEquipment(tokens[9]);
                            indentModel.setLandType(tokens[10]);
                            indentModel.setSeedType(tokens[11]);
                            indentModel.setNOFPLOTS(tokens[12]);
                            indentModel.setINDAREA(tokens[13]);
                            indentModel.setInsertLAT(tokens[14]);
                            indentModel.setInsertLON(tokens[15]);
                            indentModel.setImage(tokens[16]);
                            indentModel.setSuperviserCode(tokens[17]);
                            indentModel.setDim1(tokens[18]);
                            indentModel.setDim2(tokens[19]);
                            indentModel.setDim3(tokens[20]);
                            indentModel.setDim4(tokens[21]);
                            indentModel.setArea(tokens[22]);
                            indentModel.setLAT1(tokens[23]);
                            indentModel.setLON1(tokens[24]);
                            indentModel.setLAT2(tokens[25]);
                            indentModel.setLON2(tokens[26]);
                            indentModel.setLAT3(tokens[27]);
                            indentModel.setLON3(tokens[28]);
                            indentModel.setLAT4(tokens[29]);
                            indentModel.setLON4(tokens[30]);
                            indentModel.setSprayType(tokens[31]);
                            indentModel.setPloughingType(tokens[32]);
                            indentModel.setMobilNO(tokens[33]);
                            indentModel.setMDATE(tokens[34]);
                            indentModel.setVARIETY(tokens[35]);
                            indentModel.setCrop(tokens[36]);
                            indentModel.setPLANTINGTYPE(tokens[37]);
                            indentModel.setPLANTATION(tokens[38]);
                            indentModel.setMethod(tokens[39]);
                            indentModel.setServerStatus(tokens[40]);
                            indentModel.setRemark(tokens[41]);
                            indentModel.setCurrentDate(tokens[42]);
                            indentModel.setPlotSerialNumber(tokens[43]);
                            indentModel.setIsIdeal(tokens[44]);
                            indentModel.setIsNursery(tokens[45]);
                            indentModel.setIndDate(tokens[46]);
                            i++;
                            dbh.importIndentModel(indentModel);
                        }

                    }
                    br.close();
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" indent data successfully import");
                new importPlantingBackground().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importPlantingBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "planting.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring planting list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"planting.txt");
                if(file.exists())
                {
                    dbh.truncatePlantingModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            PlantingModel plantings=new PlantingModel();
                            plantings.setColId(tokens[0]);
                            plantings.setVillage(tokens[1]);
                            plantings.setGrower(tokens[2]);
                            plantings.setPLOTVillage(tokens[3]);
                            plantings.setIrrigationmode(tokens[4]);
                            plantings.setSupplyMode(tokens[5]);
                            plantings.setHarvesting(tokens[6]);
                            plantings.setEquipment(tokens[7]);
                            plantings.setLandType(tokens[8]);
                            plantings.setSeedType(tokens[9]);
                            plantings.setBaselDose(tokens[10]);
                            plantings.setSeedTreatment(tokens[11]);
                            plantings.setMethod(tokens[12]);
                            plantings.setDim1(tokens[13]);
                            plantings.setDim2(tokens[14]);
                            plantings.setDim3(tokens[15]);
                            plantings.setDim4(tokens[16]);
                            plantings.setArea(tokens[17]);
                            plantings.setLAT1(tokens[18]);
                            plantings.setLON1(tokens[19]);
                            plantings.setLAT2(tokens[20]);
                            plantings.setLON2(tokens[21]);
                            plantings.setLAT3(tokens[22]);
                            plantings.setLON3(tokens[23]);
                            plantings.setLAT4(tokens[24]);
                            plantings.setLON4(tokens[25]);
                            plantings.setInsertLAT(tokens[26]);
                            plantings.setInsertLON(tokens[27]);
                            plantings.setImage(tokens[28]);
                            plantings.setSuperviserCode(tokens[29]);
                            plantings.setSprayType(tokens[30]);
                            plantings.setPloughingType(tokens[31]);
                            plantings.setManualArea(tokens[32]);
                            plantings.setMobileNumber(tokens[33]);
                            plantings.setmDate(tokens[34]);
                            plantings.setVARIETY(tokens[35]);
                            plantings.setCrop(tokens[36]);
                            plantings.setPlantingType(tokens[37]);
                            plantings.setPlantation(tokens[38]);
                            plantings.setSeedType(tokens[39]);
                            plantings.setSeedSetType(tokens[40]);
                            plantings.setSoilTreatment(tokens[41]);
                            plantings.setRowToRowDistance(tokens[42]);
                            plantings.setActualAreaType(tokens[43]);
                            plantings.setArea(tokens[44]);
                            plantings.setSeedVillage(tokens[45]);
                            plantings.setSeedGrower(tokens[46]);
                            plantings.setSeedTransporter(tokens[47]);
                            plantings.setSeedDistance(tokens[48]);
                            plantings.setSeedQuantity(tokens[49]);
                            plantings.setSeedRate(tokens[50]);
                            plantings.setSeedOtherAmount(tokens[51]);
                            plantings.setSeedPayAmount(tokens[52]);
                            plantings.setSeedPayMode(tokens[53]);
                            plantings.setMillPurchey(tokens[54]);
                            plantings.setServerStatus(tokens[55]);
                            plantings.setRemark(tokens[56]);
                            plantings.setCurrentDate(tokens[57]);
                            plantings.setPlotSerialNumber(tokens[58]);
                            plantings.setIsIdeal(tokens[59]);
                            plantings.setIsNursery(tokens[60]);
                            plantings.setId(tokens[61]);
                            i++;
                            dbh.importPlantingModel(plantings);
                        }

                    }
                    br.close();
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" planting data successfully import");
                new importPlantingEquipmentBackground().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importPlantingEquipmentBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "planting_equipment.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring planting equipment list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"planting_equipment.txt");
                if(file.exists())
                {
                    dbh.truncatePlantingEquipmentModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            PlantingEquipmentModel plantingEquipmentModel=new PlantingEquipmentModel();
                            plantingEquipmentModel.setColId(tokens[0]);
                            plantingEquipmentModel.setCode(tokens[1]);
                            plantingEquipmentModel.setPlantingCode(tokens[2]);
                            i++;
                            dbh.importPlantingEquipmentModel(plantingEquipmentModel);
                        }

                    }
                    br.close();
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" planting equipment data successfully import");
                new importPlantingItemModel().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importPlantingItemModel extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "planting_item.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring planting item list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"planting_item.txt");
                if(file.exists())
                {
                    dbh.truncatePlantingItemModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            PlantingItemModel plantingItemModel=new PlantingItemModel();
                            plantingItemModel.setColId(tokens[0]);
                            plantingItemModel.setCode(tokens[1]);
                            plantingItemModel.setQty(tokens[2]);
                            plantingItemModel.setPlantingCode(tokens[3]);
                            i++;
                            dbh.importPlantingItemModel(plantingItemModel);
                        }

                    }
                    br.close();
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" planting item data successfully import");
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importSurveyDataBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "plot_backup.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring plot list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {
                new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"plot_backup.txt");
                if(file.exists())
                {
                    dbh.truncatePlotSurveyModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            PlotSurveyModel plotSurveyModel=new PlotSurveyModel();
                            plotSurveyModel.setColId(tokens[0]);
                            plotSurveyModel.setPlotSrNo(tokens[1]);
                            plotSurveyModel.setAreaMeter(tokens[5]);
                            plotSurveyModel.setAreaHectare(tokens[6]);
                            plotSurveyModel.setKhashraNo(tokens[2]);
                            plotSurveyModel.setGataNo(tokens[3]);
                            plotSurveyModel.setVillageCode(tokens[4]);
                            plotSurveyModel.setMixCrop(tokens[7]);
                            plotSurveyModel.setInsect(tokens[8]);
                            plotSurveyModel.setSeedSource(tokens[9]);
                            plotSurveyModel.setPlantMethod(tokens[11]);
                            plotSurveyModel.setCropCondition(tokens[12]);
                            plotSurveyModel.setDisease(tokens[13]);
                            plotSurveyModel.setIrrigation(tokens[15]);
                            plotSurveyModel.setSoilType(tokens[16]);
                            plotSurveyModel.setLandType(tokens[17]);
                            plotSurveyModel.setBorderCrop(tokens[18]);
                            plotSurveyModel.setPlotType(tokens[19]);
                            plotSurveyModel.setInterCrop(tokens[21]);
                            plotSurveyModel.setAadharNumber(tokens[10]);
                            plotSurveyModel.setPlantDate(tokens[14]);
                            plotSurveyModel.setGhashtiNumber(tokens[20]);
                            plotSurveyModel.setEastNorthLat(tokens[22]);
                            plotSurveyModel.setEastNorthLng(tokens[23]);
                            plotSurveyModel.setEastNorthAccuracy(tokens[24]);
                            plotSurveyModel.setEastNorthDistance(tokens[25]);
                            plotSurveyModel.setWestNorthLat(tokens[26]);
                            plotSurveyModel.setWestNorthLng(tokens[27]);
                            plotSurveyModel.setWestNorthAccuracy(tokens[28]);
                            plotSurveyModel.setWestNorthDistance(tokens[29]);
                            plotSurveyModel.setEastSouthLat(tokens[30]);
                            plotSurveyModel.setEastSouthLng(tokens[31]);
                            plotSurveyModel.setEastSouthAccuracy(tokens[32]);
                            plotSurveyModel.setEastSouthDistance(tokens[33]);
                            plotSurveyModel.setWestSouthLat(tokens[34]);
                            plotSurveyModel.setWestSouthLng(tokens[35]);
                            plotSurveyModel.setWestSouthAccuracy(tokens[36]);
                            plotSurveyModel.setWestSouthDistance(tokens[37]);
                            plotSurveyModel.setVarietyCode(tokens[38]);
                            plotSurveyModel.setCaneType(tokens[39]);
                            if(tokens.length==40)
                            {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date today = Calendar.getInstance().getTime();
                                String currDate=dateFormat.format(today);
                                plotSurveyModel.setInsertDate(currDate);
                            }
                            else
                            {
                                plotSurveyModel.setInsertDate(tokens[40]);
                            }
                            i++;
                            dbh.importPlotSurveyModel(plotSurveyModel);
                        }
                    }
                    br.close();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_HH_mm");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);
                    File renamefilec = new File(dir,"plot_backup_"+currentDt+".txt");
                    file.renameTo(renamefilec);
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" plot survey data successfully import");
                new importShareDataBackground().execute();
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private class importShareDataBackground extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);
        List<String[]> flist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir, "plot_share_backup.txt");
                int li=countLinesNew(file.getAbsolutePath().toString());
                dialog.setTitle("Please wait ...");
                dialog.setIndeterminate(false);
                dialog.setMessage("Restoring share percent list...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(li);
                dialog.setProgress(0);
                dialog.show();
            }
            catch (Exception e)
            {

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                DBHelper dbh = new DBHelper(context);
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");
                File file = new File(dir,"plot_share_backup.txt");
                if(file.exists())
                {
                    dbh.truncateFarmerShareSurveyModel();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    int i=0;
                    while ((line = br.readLine()) != null) {

                        String[] tokens = line.split(" , ");
                        if(i==0)
                        {
                            i++;
                        }
                        else
                        {
                            publishProgress((int)i);
                            flist.add(tokens);
                            FarmerShareModel plotSurveyModel=new FarmerShareModel();
                            plotSurveyModel.setColId(tokens[0]);
                            plotSurveyModel.setSurveyId(tokens[1]);
                            plotSurveyModel.setSrNo(tokens[2]);
                            plotSurveyModel.setVillageCode(tokens[3]);
                            plotSurveyModel.setGrowerCode(tokens[4]);
                            plotSurveyModel.setGrowerName(tokens[5]);
                            plotSurveyModel.setGrowerFatherName(tokens[6]);
                            plotSurveyModel.setGrowerAadharNumber(tokens[7]);
                            plotSurveyModel.setShare(tokens[8]);
                            plotSurveyModel.setSupCode(tokens[9]);
                            plotSurveyModel.setCurDate(tokens[10]);
                            plotSurveyModel.setServerStatus(tokens[11]);
                            if(tokens.length==12)
                            {
                                plotSurveyModel.setServerStatusRemark("");
                            }
                            else
                            {
                                plotSurveyModel.setServerStatusRemark(tokens[12]);
                            }
                            i++;
                            dbh.importFarmerShareModel(plotSurveyModel);
                        }

                    }
                    br.close();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_HH_mm");
                    Date today = Calendar.getInstance().getTime();
                    String currentDt = dateFormat.format(today);
                    File renamefilec = new File(dir,"plot_share_backup_"+currentDt+".txt");
                    file.renameTo(renamefilec);
                }
                else
                {
                    AlertPopUp("Backup file not found...");
                }
            }catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                AlertPopUp("Error:"+e.toString());
            }
            return null;
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
                AlertPopUp(flist.size()+" plot share percentage data successfully import");
            }
            catch (Exception e)
            {
                AlertPopUp("Error: "+e.toString());
            }
        }
    }

    private void AlertPopUp(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public static int countLinesNew(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }

    public void exportSingleBackup(PlotSurveyModel plotSurveyModel)
    {
        try {
            String data="";
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/Download");
            File gpxfile = new File(dir, "plot_data_auto.txt");
            if(!gpxfile.exists())
            {
                dir.mkdirs();
                data +="Id"+" , ";
                data +="Plot Sr No"+" , ";
                data +="Khashra Number"+" , ";
                data +="Gata Number"+" , ";
                data +="Village Code"+" , ";
                data +="Area Meter"+" , ";
                data +="Area Hectare"+" , ";
                data +="Mix Crop"+" , ";
                data +="Insect"+" , ";
                data +="Seed Source"+" , ";
                data +="Aadhar Number"+" , ";
                data +="Plant Method"+" , ";
                data +="Crop Condition"+" , ";
                data +="Disease"+" , ";
                data +="Plant Date"+" , ";
                data +="Irrigation"+" , ";
                data +="Soil Type"+" , ";
                data +="Land Type"+" , ";
                data +="Border Crop"+" , ";
                data +="Plot Type"+" , ";
                data +="Ghashti Number"+" , ";
                data +="Inter Crop"+" , ";
                data +="East North Lat"+" , ";
                data +="East North Lng"+" , ";
                data +="East North Accuracy"+" , ";
                data +="East North Distance"+" , ";
                data +="West North Lat"+" , ";
                data +="West North Lng"+" , ";
                data +="West North Accuracy"+" , ";
                data +="West North Distance"+" , ";
                data +="East South Lat"+" , ";
                data +="East South Lng"+" , ";
                data +="East South Accuracy"+" , ";
                data +="East South Distance"+" , ";
                data +="West South Lat"+" , ";
                data +="West South Lng"+" , ";
                data +="West South Accuracy"+" , ";
                data +="West South Distance"+" , ";
                data +="Variety Code"+" , ";
                data +="Cane Type"+" , ";
                data +="Insert Time"+"\n";
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();

            }
            data=new UpdateDataInDatabase(context).getPlotSurveyModel(plotSurveyModel);
            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(gpxfile,true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            buffered_writer.write(data);
            buffered_writer.close();

        }catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

    public void exportSingleFarmerBackup(FarmerShareModel plotSurveyModel)
    {
        try {
            String data="";
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/Download");
            File gpxfile = new File(dir, "plot_share_backup_auto.txt");
            if(!gpxfile.exists())
            {
                dir.mkdirs();
                data +="Id"+" , ";
                data +="Survey Plot Id"+" , ";
                data +="Sr No"+" , ";
                data +="Village Code"+" , ";
                data +="Grower Code"+" , ";
                data +="Grower Name"+" , ";
                data +="Grower Father"+" , ";
                data +="Grower Aadhar"+" , ";
                data +="Share "+" , ";
                data +="Sup Code "+" , ";
                data +="Date "+" , ";
                data +="Server Status "+" , ";
                data +="Server Remark"+"\n";
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();

            }
            data=new UpdateDataInDatabase(context).getFarmerShareModel(plotSurveyModel);

            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(gpxfile,true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            buffered_writer.write(data);
            buffered_writer.close();

        }catch (Exception e) {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }
    }

}
