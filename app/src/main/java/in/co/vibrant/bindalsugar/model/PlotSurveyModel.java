package in.co.vibrant.bindalsugar.model;

import org.json.JSONArray;

public class PlotSurveyModel {

    public static final String TABLE_NAME = "plot_survey";
    public static final String COLUMN_ID = "plot_survey_id";
    public static final String Col_PlotSrNo = "plot_sr_number";
    public static final String Col_khashraNo = "khashra_no";
    public static final String Col_gataNo = "gataNo";
    public static final String Col_villageCode = "villageCode";
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
    public static final String Col_is_delete= "is_delete";
    public static final String Col_old_plot_id= "old_plot_id";
    public static final String Col_old_plot_from= "old_plot_from";



    protected String ColId;
    protected String PlotSrNo;
    protected String khashraNo;
    protected String gataNo;
    protected String villageCode;
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
    protected String isDelete;
    protected String oldPlotId;
    protected String plotFrom;



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_PlotSrNo + " TEXT,"
                    + Col_khashraNo + " TEXT,"
                    + Col_gataNo + " TEXT,"
                    + Col_villageCode + " TEXT,"
                    + Col_areaMeter + " TEXT,"
                    + Col_areaHectare + " TEXT,"
                    + Col_mixCrop + " TEXT,"
                    + Col_insect + " TEXT,"
                    + Col_seedSource + " TEXT,"
                    + Col_aadharNumber + " TEXT,"
                    + Col_plantMethod + " TEXT,"
                    + Col_cropCondition + " TEXT,"
                    + Col_disease + " TEXT,"
                    + Col_plantDate + " TEXT,"
                    + Col_irrigation + " TEXT,"
                    + Col_soilType + " TEXT,"
                    + Col_landType + " TEXT,"
                    + Col_borderCrop + " TEXT,"
                    + Col_plotType + " TEXT,"
                    + Col_ghashtiNumber + " TEXT,"
                    + Col_interCrop + " TEXT,"
                    + Col_eastNorth_lat + " TEXT,"
                    + Col_eastNorth_lng + " TEXT,"
                    + Col_eastNorth_distance + " TEXT,"
                    + Col_eastNorth_accuracy + " TEXT,"
                    + Col_eastSouth_lat + " TEXT,"
                    + Col_eastSouth_lng + " TEXT,"
                    + Col_eastSouth_distance + " TEXT,"
                    + Col_eastSouth_accuracy + " TEXT,"
                    + Col_westSouth_lat + " TEXT,"
                    + Col_westSouth_lng + " TEXT,"
                    + Col_westSouth_distance + " TEXT,"
                    + Col_westSouth_accuracy + " TEXT,"
                    + Col_westNorth_lat + " TEXT,"
                    + Col_westNorth_lng + " TEXT,"
                    + Col_westNorth_distance + " TEXT,"
                    + Col_westNorth_accuracy + " TEXT,"
                    + Col_varietyCode + " TEXT,"
                    + Col_caneType + " TEXT,"
                    + Col_is_delete + " TEXT,"
                    + Col_old_plot_id + " TEXT,"
                    + Col_old_plot_from + " TEXT,"
                    + Col_insertDate + " date"
                    + ")";

    public String getColId() {
        return ColId;
    }

    public void setColId(String colId) {
        ColId = colId;
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

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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
