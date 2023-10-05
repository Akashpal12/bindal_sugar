package in.co.vibrant.bindalsugar.model;

public class FarmerModel {

    public static final String TABLE_NAME = "farmer";
    public static final String COLUMN_ID = "id";
    public static final String Col_farmerCode = "farmer_code";
    public static final String Col_villageCode = "village_code";
    public static final String Col_farmerName = "farmer_name";
    public static final String Col_fatherName = "father_name";
    public static final String Col_uniqueCode = "unique_code";
    public static final String Col_des = "desc";
    public static final String Col_mobile = "mobile";
    public static final String Col_cultArea = "cult_area";




    protected String ColId;
    protected String farmerCode;
    protected String villageCode;
    protected String farmerName;
    protected String fatherName;
    protected String uniqueCode;
    protected String des;
    protected String mobile;
    protected String cultArea;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_farmerCode + " TEXT,"
                    + Col_villageCode + " TEXT,"
                    + Col_farmerName  + " TEXT,"
                    + Col_fatherName  + " TEXT,"
                    + Col_uniqueCode  + " TEXT,"
                    + Col_des  + " TEXT,"
                    + Col_mobile  + " TEXT,"
                    + Col_cultArea  + " TEXT"
                    + ")";


    public static String getTableName() {
        return TABLE_NAME;
    }

    public String getColId() {
        return ColId;
    }

    public void setColId(String colId) {
        ColId = colId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCultArea() {
        return cultArea;
    }

    public void setCultArea(String cultArea) {
        this.cultArea = cultArea;
    }
}
