package in.co.vibrant.bindalsugar.model;

public class PlotActivitySaveModel {

    public static final String TABLE_NAME = "plot_activity";
    public static final String COLUMN_ID = "Col_id";
    public static final String plot_type = "plot_type";
    public static final String vill_code = "vill_code";
    public static final String grower_code = "grower_code";
    public static final String plot_serial_number = "plot_serial_number";
    public static final String plot_village_code = "plot_village_code";
    public static final String col_area = "area";
    public static final String col_mobile_number = "mobile_number";
    public static final String col_SURTYPE = "SURTYPE";
    public static final String col_OLDSEAS = "OLDSEAS";
    public static final String col_OLDGHID = "OLDGHID";
    public static final String json_array = "json_array";
    public static final String server_status = "server_status";
    public static final String col_remark = "remark";
    public static final String current_date = "current_date";
    public static final String meeting_type = "meeting_type";
    public static final String meeting_name = "meeting_name";
    public static final String meeting_number = "meeting_number";

    private String colId;
    private String plotType;
    private String villageCode;
    private String grwerCode;
    private String plotSerialNumber;
    private String plotVillage ;
    private String area;
    private String surveyType;
    private String oldSeason;
    private String oldGhid;
    private String jsonArrayData ;
    private String serverStatus ;
    private String remark ;
    private String currentDate ;
    private String meetingType ;
    private String meetingName ;
    private String meetingNumber ;
    private String mobileNumber ;
    private String affectedCondition;
    private String imageFormat;
    private String activityMethod;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + plot_type + " TEXT,"
                    + vill_code + " TEXT,"
                    + grower_code + " TEXT,"
                    + plot_serial_number + " TEXT,"
                    + plot_village_code + " TEXT,"
                    + col_area + " TEXT,"
                    + col_SURTYPE + " TEXT,"
                    + col_OLDSEAS + " TEXT,"
                    + col_OLDGHID + " TEXT,"
                    + json_array + " TEXT,"
                    + server_status + " TEXT,"
                    + col_remark + " TEXT,"
                    + current_date + " TEXT,"
                    + meeting_type + " TEXT,"
                    + meeting_name + " TEXT,"
                    + meeting_number + " TEXT"
                    + ")";


    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getGrwerCode() {
        return grwerCode;
    }

    public void setGrwerCode(String grwerCode) {
        this.grwerCode = grwerCode;
    }

    public String getPlotSerialNumber() {
        return plotSerialNumber;
    }

    public void setPlotSerialNumber(String plotSerialNumber) {
        this.plotSerialNumber = plotSerialNumber;
    }

    public String getPlotVillage() {
        return plotVillage;
    }

    public void setPlotVillage(String plotVillage) {
        this.plotVillage = plotVillage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getJsonArrayData() {
        return jsonArrayData;
    }

    public void setJsonArrayData(String jsonArrayData) {
        this.jsonArrayData = jsonArrayData;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public String getOldSeason() {
        return oldSeason;
    }

    public void setOldSeason(String oldSeason) {
        this.oldSeason = oldSeason;
    }

    public String getOldGhid() {
        return oldGhid;
    }

    public void setOldGhid(String oldGhid) {
        this.oldGhid = oldGhid;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAffectedCondition() {
        return affectedCondition;
    }

    public void setAffectedCondition(String affectedCondition) {
        this.affectedCondition = affectedCondition;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getActivityMethod() {
        return activityMethod;
    }

    public void setActivityMethod(String activityMethod) {
        this.activityMethod = activityMethod;
    }
}
