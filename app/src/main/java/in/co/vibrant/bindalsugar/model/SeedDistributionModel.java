package in.co.vibrant.bindalsugar.model;

public class SeedDistributionModel {

    public static final String TABLE_NAME = "seed_distribution";
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
    public static final String Col_PurVillage = "PurVillage";
    public static final String Col_PurGrower = "PurGrower";
    public static final String Col_PaymentMod = "PaymentMod";
    public static final String Col_MillPurchyNo = "MillPurchyNo";
    public static final String Col_SupervisorCode = "SupervisorCode";
    public static final String Col_Amount = "Amount";
    public static final String Col_OthAmount = "OthAmount";
    public static final String Col_ServerStatus = "ServerStatus";
    public static final String Col_Remark = "Remark";
    public static final String Col_CurrentDate = "CurrentDate";

    private String colId;
    private String Village;
    private String Grower;
    private String category ;
    private String variety ;
    private String quantity;
    private String rate ;
    private String transportation ;
    private String distance ;
    private String mobileNumber ;
    private String purchaseVillage ;
    private String purchaseGrower ;
    private String paymentMode ;
    private String millPurcheyNumber ;
    private String supervisorCode ;
    private String amount ;
    private String otherAmount ;
    private String ServerStatus;
    private String Remark ;
    private String CurrentDate ;

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
                    + Col_PurVillage + " TEXT,"
                    + Col_PurGrower + " TEXT,"
                    + Col_PaymentMod + " TEXT,"
                    + Col_MillPurchyNo + " TEXT,"
                    + Col_SupervisorCode + " TEXT,"
                    + Col_Amount + " TEXT,"
                    + Col_OthAmount + " TEXT,"
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

    public String getGrower() {
        return Grower;
    }

    public void setGrower(String grower) {
        Grower = grower;
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

    public String getPurchaseVillage() {
        return purchaseVillage;
    }

    public void setPurchaseVillage(String purchaseVillage) {
        this.purchaseVillage = purchaseVillage;
    }

    public String getPurchaseGrower() {
        return purchaseGrower;
    }

    public void setPurchaseGrower(String purchaseGrower) {
        this.purchaseGrower = purchaseGrower;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getMillPurcheyNumber() {
        return millPurcheyNumber;
    }

    public void setMillPurcheyNumber(String millPurcheyNumber) {
        this.millPurcheyNumber = millPurcheyNumber;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(String otherAmount) {
        this.otherAmount = otherAmount;
    }
}
