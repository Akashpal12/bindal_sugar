package in.co.vibrant.bindalsugar.model;

public class SeedReservationModel {

    public static final String TABLE_NAME = "seed_reservation";
    public static final String COLUMN_ID = "Col_id";
    public static final String Col_VillCode = "VillCode";
    public static final String Col_GrowCode = "GrowCode";
    public static final String Col_CATEGORY = "CATEGORY";
    public static final String Col_Variety = "Variety";
    public static final String Col_QTY = "QTY";
    public static final String Col_RATE = "RATE";
    public static final String Col_Transportation = "Transportation";
    public static final String Col_Distance = "Distance";
    public static final String Col_MobilNo = "MobilNo";
    public static final String Col_SupervisorCode = "SupervisorCode";
    public static final String Col_ServerStatus = "ServerStatus";
    public static final String Col_Remark = "Remark";
    public static final String Col_CurrentDate = "CurrentDate";

    private String colId;
    private String Village;
    private String VillageName;
    private String Grower;
    private String GrowerName;
    private String GrowerFather;
    private String category ;
    private String variety ;
    private String varietyName ;
    private String quantity;
    private String rate ;
    private String transportation ;
    private String distance ;
    private String mobileNumber ;
    private String supervisorCode ;
    private String ServerStatus;
    private String Remark ;
    private String CurrentDate ;
    private String color ;
    private String textColor ;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_VillCode + " TEXT,"
                    + Col_GrowCode + " TEXT,"
                    + Col_CATEGORY + " TEXT,"
                    + Col_Variety + " TEXT,"
                    + Col_QTY + " TEXT,"
                    + Col_RATE + " TEXT,"
                    + Col_Transportation + " TEXT,"
                    + Col_Distance + " TEXT,"
                    + Col_MobilNo + " TEXT,"
                    + Col_SupervisorCode + " TEXT,"
                    + Col_ServerStatus + " TEXT,"
                    + Col_Remark + " TEXT,"
                    + Col_CurrentDate + " TEXT"
                    + ")";


    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getGrower() {
        return Grower;
    }

    public void setGrower(String grower) {
        Grower = grower;
    }

    public String getGrowerName() {
        return GrowerName;
    }

    public void setGrowerName(String growerName) {
        GrowerName = growerName;
    }

    public String getGrowerFather() {
        return GrowerFather;
    }

    public void setGrowerFather(String growerFather) {
        GrowerFather = growerFather;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public String getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(String serverStatus) {
        ServerStatus = serverStatus;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
