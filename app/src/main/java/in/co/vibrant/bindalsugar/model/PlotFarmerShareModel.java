package in.co.vibrant.bindalsugar.model;

import org.json.JSONArray;

public class PlotFarmerShareModel {

    public static final String SHARE_TABLE_NAME = "farmer_share";
    public static final String SHARE_COLUMN_ID = "farmer_share_id";
    public static final String Col_surveyId = "survey_id";
    public static final String Col_srNo = "sr_no";
    public static final String Col_villageCode = "village_code";
    public static final String Col_growerCode = "grower_code";
    public static final String Col_growerName = "grower_name";
    public static final String Col_growerFatherName = "grower_father_code";
    public static final String Col_growerAadhar_number = "grower_aadhar_number";
    public static final String Col_share = "share";
    public static final String Col_sup_code = "sup_code";
    public static final String Col_curDate = "curr_date";
    public static final String Col_ServerStatus = "server_status";
    public static final String Col_ServerStatusRemark = "server_status_remark";

    public static final String PLOT_TABLE_NAME = "plot_survey";
    public static final String PLOT_COLUMN_ID = "plot_survey_id";
    public static final String Col_PlotSrNo = "plot_sr_number";
    public static final String Col_khashraNo = "khashra_no";
    public static final String Col_gataNo = "gataNo";
    public static final String Col_plotvillageCode = "villageCode";
    public static final String Col_areaMeter = "areaMeter";
    public static final String Col_areaHectare = "areaHectare";
    public static final String Col_mixCrop = "mixCrop";
    public static final String Col_insect = "insect";
    public static final String Col_seedSource = "seedSource";
    public static final String Col_aadharNumber = "aadharNumber";
    public static final String Col_plantMethod = "plantMethod";
    public static final String Col_cropCondition = "cropCondition";
    public static final String Col_disease = "disease";
    public static final String Col_plantDate = "plantDate";
    public static final String Col_irrigation = "irrigation";
    public static final String Col_soilType = "soilType";
    public static final String Col_landType = "landType";
    public static final String Col_borderCrop = "borderCrop";
    public static final String Col_plotType = "plotType";
    public static final String Col_ghashtiNumber = "ghashtiNumber";
    public static final String Col_interCrop = "interCrop";
    public static final String Col_eastNorth_lat = "east_lat";
    public static final String Col_eastNorth_lng = "east_lng";
    public static final String Col_eastNorth_distance = "east_distance";
    public static final String Col_eastNorth_accuracy = "east_accuracy";
    public static final String Col_eastSouth_lat = "south_lat";
    public static final String Col_eastSouth_lng = "south_lng";
    public static final String Col_eastSouth_distance = "south_distance";
    public static final String Col_eastSouth_accuracy = "south_accuracy";
    public static final String Col_westSouth_lat = "west_lat";
    public static final String Col_westSouth_lng = "west_lng";
    public static final String Col_westSouth_distance = "west_distance";
    public static final String Col_westSouth_accuracy = "west_accuracy";
    public static final String Col_westNorth_lat = "north_lat";
    public static final String Col_westNorth_lng = "north_lng";
    public static final String Col_westNorth_distance = "north_distance";
    public static final String Col_westNorth_accuracy = "north_accuracy";
    public static final String Col_varietyCode = "variety_code";
    public static final String Col_caneType = "cane_type";
    public static final String Col_insertDate= "insert_date";
    public static final String Col_old_plot_id= "old_plot_id";


    protected String ColId;
    protected String surveyId;
    protected String srNo;
    protected String villageCode;
    protected String growerCode;
    protected String growerName;
    protected String growerFatherName;
    protected String growerAadharNumber;
    protected String share;
    protected String supCode;
    protected String curDate;
    protected String serverStatus;
    protected String serverStatusRemark;
    protected String textColor;
    protected String color;


    protected String PlotSrNo;
    protected String khashraNo;
    protected String gataNo;
    protected String plotVillageCode;
    protected String areaMeter;
    protected String areaHectare;
    protected String mixCrop;
    protected String insect;
    protected String seedSource;
    protected String aadharNumber;
    protected String plantMethod;
    protected String cropCondition;
    protected String disease;
    protected String plantDate;
    protected String irrigation;
    protected String soilType;
    protected String landType;
    protected String borderCrop;
    protected String plotType;
    protected String ghashtiNumber;
    protected String interCrop;
    protected JSONArray sharePer;
    protected String eastNorthLat;
    protected String eastNorthLng;
    protected String eastNorthDistance;
    protected String eastNorthAccuracy;
    protected String eastSouthLat;
    protected String eastSouthLng;
    protected String eastSouthDistance;
    protected String eastSouthAccuracy;
    protected String westNorthLat;
    protected String westNorthLng;
    protected String westNorthDistance;
    protected String westNorthAccuracy;
    protected String westSouthLat;
    protected String westSouthLng;
    protected String westSouthDistance;
    protected String westSouthAccuracy;
    protected String varietyCode;
    protected String caneType;
    protected String insertDate;
    protected String oldPlotId;
    protected String plotFrom;


    public String getColId() {
        return ColId;
    }

    public void setColId(String colId) {
        ColId = colId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getGrowerCode() {
        return growerCode;
    }

    public void setGrowerCode(String growerCode) {
        this.growerCode = growerCode;
    }

    public String getGrowerName() {
        return growerName;
    }

    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }

    public String getGrowerFatherName() {
        return growerFatherName;
    }

    public void setGrowerFatherName(String growerFatherName) {
        this.growerFatherName = growerFatherName;
    }

    public String getGrowerAadharNumber() {
        return growerAadharNumber;
    }

    public void setGrowerAadharNumber(String growerAadharNumber) {
        this.growerAadharNumber = growerAadharNumber;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerStatusRemark() {
        return serverStatusRemark;
    }

    public void setServerStatusRemark(String serverStatusRemark) {
        this.serverStatusRemark = serverStatusRemark;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlotSrNo() {
        return PlotSrNo;
    }

    public void setPlotSrNo(String plotSrNo) {
        PlotSrNo = plotSrNo;
    }

    public String getKhashraNo() {
        return khashraNo;
    }

    public void setKhashraNo(String khashraNo) {
        this.khashraNo = khashraNo;
    }

    public String getGataNo() {
        return gataNo;
    }

    public void setGataNo(String gataNo) {
        this.gataNo = gataNo;
    }

    public String getPlotVillageCode() {
        return plotVillageCode;
    }

    public void setPlotVillageCode(String plotVillageCode) {
        this.plotVillageCode = plotVillageCode;
    }

    public String getAreaMeter() {
        return areaMeter;
    }

    public void setAreaMeter(String areaMeter) {
        this.areaMeter = areaMeter;
    }

    public String getAreaHectare() {
        return areaHectare;
    }

    public void setAreaHectare(String areaHectare) {
        this.areaHectare = areaHectare;
    }

    public String getMixCrop() {
        return mixCrop;
    }

    public void setMixCrop(String mixCrop) {
        this.mixCrop = mixCrop;
    }

    public String getInsect() {
        return insect;
    }

    public void setInsect(String insect) {
        this.insect = insect;
    }

    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPlantMethod() {
        return plantMethod;
    }

    public void setPlantMethod(String plantMethod) {
        this.plantMethod = plantMethod;
    }

    public String getCropCondition() {
        return cropCondition;
    }

    public void setCropCondition(String cropCondition) {
        this.cropCondition = cropCondition;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPlantDate() {
        return plantDate;
    }

    public void setPlantDate(String plantDate) {
        this.plantDate = plantDate;
    }

    public String getIrrigation() {
        return irrigation;
    }

    public void setIrrigation(String irrigation) {
        this.irrigation = irrigation;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getBorderCrop() {
        return borderCrop;
    }

    public void setBorderCrop(String borderCrop) {
        this.borderCrop = borderCrop;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String getGhashtiNumber() {
        return ghashtiNumber;
    }

    public void setGhashtiNumber(String ghashtiNumber) {
        this.ghashtiNumber = ghashtiNumber;
    }

    public String getInterCrop() {
        return interCrop;
    }

    public void setInterCrop(String interCrop) {
        this.interCrop = interCrop;
    }

    public JSONArray getSharePer() {
        return sharePer;
    }

    public void setSharePer(JSONArray sharePer) {
        this.sharePer = sharePer;
    }

    public String getEastNorthLat() {
        return eastNorthLat;
    }

    public void setEastNorthLat(String eastNorthLat) {
        this.eastNorthLat = eastNorthLat;
    }

    public String getEastNorthLng() {
        return eastNorthLng;
    }

    public void setEastNorthLng(String eastNorthLng) {
        this.eastNorthLng = eastNorthLng;
    }

    public String getEastNorthDistance() {
        return eastNorthDistance;
    }

    public void setEastNorthDistance(String eastNorthDistance) {
        this.eastNorthDistance = eastNorthDistance;
    }

    public String getEastNorthAccuracy() {
        return eastNorthAccuracy;
    }

    public void setEastNorthAccuracy(String eastNorthAccuracy) {
        this.eastNorthAccuracy = eastNorthAccuracy;
    }

    public String getEastSouthLat() {
        return eastSouthLat;
    }

    public void setEastSouthLat(String eastSouthLat) {
        this.eastSouthLat = eastSouthLat;
    }

    public String getEastSouthLng() {
        return eastSouthLng;
    }

    public void setEastSouthLng(String eastSouthLng) {
        this.eastSouthLng = eastSouthLng;
    }

    public String getEastSouthDistance() {
        return eastSouthDistance;
    }

    public void setEastSouthDistance(String eastSouthDistance) {
        this.eastSouthDistance = eastSouthDistance;
    }

    public String getEastSouthAccuracy() {
        return eastSouthAccuracy;
    }

    public void setEastSouthAccuracy(String eastSouthAccuracy) {
        this.eastSouthAccuracy = eastSouthAccuracy;
    }

    public String getWestNorthLat() {
        return westNorthLat;
    }

    public void setWestNorthLat(String westNorthLat) {
        this.westNorthLat = westNorthLat;
    }

    public String getWestNorthLng() {
        return westNorthLng;
    }

    public void setWestNorthLng(String westNorthLng) {
        this.westNorthLng = westNorthLng;
    }

    public String getWestNorthDistance() {
        return westNorthDistance;
    }

    public void setWestNorthDistance(String westNorthDistance) {
        this.westNorthDistance = westNorthDistance;
    }

    public String getWestNorthAccuracy() {
        return westNorthAccuracy;
    }

    public void setWestNorthAccuracy(String westNorthAccuracy) {
        this.westNorthAccuracy = westNorthAccuracy;
    }

    public String getWestSouthLat() {
        return westSouthLat;
    }

    public void setWestSouthLat(String westSouthLat) {
        this.westSouthLat = westSouthLat;
    }

    public String getWestSouthLng() {
        return westSouthLng;
    }

    public void setWestSouthLng(String westSouthLng) {
        this.westSouthLng = westSouthLng;
    }

    public String getWestSouthDistance() {
        return westSouthDistance;
    }

    public void setWestSouthDistance(String westSouthDistance) {
        this.westSouthDistance = westSouthDistance;
    }

    public String getWestSouthAccuracy() {
        return westSouthAccuracy;
    }

    public void setWestSouthAccuracy(String westSouthAccuracy) {
        this.westSouthAccuracy = westSouthAccuracy;
    }

    public String getVarietyCode() {
        return varietyCode;
    }

    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }

    public String getCaneType() {
        return caneType;
    }

    public void setCaneType(String caneType) {
        this.caneType = caneType;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getOldPlotId() {
        return oldPlotId;
    }

    public void setOldPlotId(String oldPlotId) {
        this.oldPlotId = oldPlotId;
    }

    public String getPlotFrom() {
        return plotFrom;
    }

    public void setPlotFrom(String plotFrom) {
        this.plotFrom = plotFrom;
    }
}
