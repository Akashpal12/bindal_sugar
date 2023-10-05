package in.co.vibrant.bindalsugar.util;

import android.content.Context;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;


/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class UpdateDataInDatabase {

    Context context;
    DBHelper dbh;
    String centreCode;


    public UpdateDataInDatabase(Context context) {

        this.context = context;
        dbh=new DBHelper(context);
    }


    public String getPlotSurveyModel(PlotSurveyModel plotSurveyModelList)
    {
        String data="";
        data +=plotSurveyModelList.getColId()+" , ";
        data +=plotSurveyModelList.getPlotSrNo()+" , ";
        data +=plotSurveyModelList.getKhashraNo()+" , ";
        data +=plotSurveyModelList.getGataNo()+" , ";
        data +=plotSurveyModelList.getVillageCode()+" , ";
        data +=plotSurveyModelList.getAreaMeter()+" , ";
        data +=plotSurveyModelList.getAreaHectare()+" , ";
        data +=plotSurveyModelList.getMixCrop()+" , ";
        data +=plotSurveyModelList.getInsect()+" , ";
        data +=plotSurveyModelList.getSeedSource()+" , ";
        data +=plotSurveyModelList.getAadharNumber()+" , ";
        data +=plotSurveyModelList.getPlantMethod()+" , ";
        data +=plotSurveyModelList.getCropCondition()+" , ";
        data +=plotSurveyModelList.getDisease()+" , ";
        data +=plotSurveyModelList.getPlantDate()+" , ";
        data +=plotSurveyModelList.getIrrigation()+" , ";
        data +=plotSurveyModelList.getSoilType()+" , ";
        data +=plotSurveyModelList.getLandType()+" , ";
        data +=plotSurveyModelList.getBorderCrop()+" , ";
        data +=plotSurveyModelList.getPlotType()+" , ";
        data +=plotSurveyModelList.getGhashtiNumber()+" , ";
        data +=plotSurveyModelList.getInterCrop()+" , ";
        data +=plotSurveyModelList.getEastNorthLat()+" , ";
        data +=plotSurveyModelList.getEastNorthLng()+" , ";
        data +=plotSurveyModelList.getEastNorthAccuracy()+" , ";
        data +=plotSurveyModelList.getEastNorthDistance()+" , ";
        data +=plotSurveyModelList.getWestNorthLat()+" , ";
        data +=plotSurveyModelList.getWestNorthLng()+" , ";
        data +=plotSurveyModelList.getWestNorthAccuracy()+" , ";
        data +=plotSurveyModelList.getWestNorthDistance()+" , ";
        data +=plotSurveyModelList.getEastSouthLat()+" , ";
        data +=plotSurveyModelList.getEastSouthLng()+" , ";
        data +=plotSurveyModelList.getEastSouthAccuracy()+" , ";
        data +=plotSurveyModelList.getEastSouthDistance()+" , ";
        data +=plotSurveyModelList.getWestSouthLat()+" , ";
        data +=plotSurveyModelList.getWestSouthLng()+" , ";
        data +=plotSurveyModelList.getWestSouthAccuracy()+" , ";
        data +=plotSurveyModelList.getWestSouthDistance()+" , ";
        data +=plotSurveyModelList.getVarietyCode()+" , ";
        data +=plotSurveyModelList.getCaneType()+" , ";
        data +=plotSurveyModelList.getInsertDate()+"\n";
        return data;
    }

    public String getFarmerShareModel(FarmerShareModel farmerShareModelList)
    {
        String data="";
        data +=farmerShareModelList.getColId()+" , ";
        data +=farmerShareModelList.getSurveyId()+" , ";
        data +=farmerShareModelList.getSrNo()+" , ";
        data +=farmerShareModelList.getVillageCode()+" , ";
        data +=farmerShareModelList.getGrowerCode()+" , ";
        data +=farmerShareModelList.getGrowerName()+" , ";
        data +=farmerShareModelList.getGrowerFatherName()+" , ";
        data +=farmerShareModelList.getGrowerAadharNumber()+" , ";
        data +=farmerShareModelList.getShare()+" , ";
        data +=farmerShareModelList.getSupCode()+" , ";
        data +=farmerShareModelList.getCurDate()+" , ";
        data +=farmerShareModelList.getServerStatus()+" , ";
        data +=farmerShareModelList.getServerStatusRemark()+"\n";
        return data;
    }


}
