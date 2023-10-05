package in.co.vibrant.bindalsugar.model;

public class FarmerShareModel {

    public static final String TABLE_NAME = "farmer_share";
    public static final String COLUMN_ID = "farmer_share_id";
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


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_surveyId + " TEXT,"
                    + Col_srNo + " TEXT,"
                    + Col_villageCode  + " TEXT,"
                    + Col_growerCode  + " TEXT,"
                    + Col_growerName  + " TEXT,"
                    + Col_growerFatherName  + " TEXT,"
                    + Col_growerAadhar_number  + " TEXT,"
                    + Col_share  + " TEXT,"
                    + Col_sup_code  + " TEXT,"
                    + Col_curDate  + " TEXT,"
                    + Col_ServerStatus  + " TEXT,"
                    + Col_ServerStatusRemark  + " TEXT"
                    + ")";


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

    public String getServerStatusRemark() {
        return serverStatusRemark;
    }

    public void setServerStatusRemark(String serverStatusRemark) {
        this.serverStatusRemark = serverStatusRemark;
    }
}
