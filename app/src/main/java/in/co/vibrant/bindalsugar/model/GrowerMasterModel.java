package in.co.vibrant.bindalsugar.model;

import static in.co.vibrant.bindalsugar.model.GrowerModel.COLUMN_ID;

import androidx.annotation.NonNull;

public class GrowerMasterModel {
    public static final String TABLE_NAME = "GrowerMaster";  //for table name
    //For Table colume
    public static final String COLUMN_GROWER_ID = "GrowerMasterId";
    public static final String GROWER_UNIT_CODE = "GrowerUnitCode";
    public static final String GROWER_RYOT_CODE = "GrowerRyotCode";
    public static final String GROWER_RYOT_NAME = "GrowerRyotName";
    public static final String GROWER_FATHER_NAME = "GrowerFatherName";
    public static final String GROWER_MOBILE_NUMBER = "GrowerMobileNumber";
    public static final String GROWER_VILLAGE_CODE = "GrowerVillageCode";
    public static final String GROWER_STATUS = "GrowerStatus";
    public static final String COLUMN_FK_GROWER = "FkGrower";

    private int g_Id;
    private String growerUnitCode;
    private String growerRyotCode;
    private String GrowerRyotName;
    private String growerFatherName;
    private String growerMobileNumber;
    private String growerVillageCode;
    private String growerStatus;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_GROWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + GROWER_UNIT_CODE + " TEXT,"
                    + GROWER_RYOT_CODE + " TEXT,"
                    + GROWER_RYOT_NAME + " TEXT,"
                    + GROWER_FATHER_NAME + " TEXT,"
                    + GROWER_MOBILE_NUMBER + " TEXT,"
                    + GROWER_VILLAGE_CODE + " TEXT,"
                    + GROWER_STATUS + " TEXT,"
                    + COLUMN_FK_GROWER + " INTEGER,"
                    + "FOREIGN KEY (" + COLUMN_FK_GROWER + ")REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";

    public GrowerMasterModel() {
    }

    public String getGrowerStatus() {
        return growerStatus;
    }

    public void setGrowerStatus(String growerStatus) {
        this.growerStatus = growerStatus;
    }

    public int getG_Id() {
        return g_Id;
    }

    public void setG_Id(int g_Id) {
        this.g_Id = g_Id;
    }

    public String getGrowerUnitCode() {
        return growerUnitCode;
    }

    public void setGrowerUnitCode(String growerUnitCode) {
        this.growerUnitCode = growerUnitCode;
    }

    public String getGrowerRyotCode() {
        return growerRyotCode;
    }

    public void setGrowerRyotCode(String growerRyotCode) {
        this.growerRyotCode = growerRyotCode;
    }

    public String getGrowerRyotName() {
        return GrowerRyotName;
    }

    public void setGrowerRyotName(String growerRyotName) {
        GrowerRyotName = growerRyotName;
    }

    public String getGrowerFatherName() {
        return growerFatherName;
    }

    public void setGrowerFatherName(String growerFatherName) {
        this.growerFatherName = growerFatherName;
    }

    public String getGrowerVillageCode() {
        return growerVillageCode;
    }

    public void setGrowerVillageCode(String growerVillageCode) {
        this.growerVillageCode = growerVillageCode;
    }

    public String getGrowerMobileNumber() {
        return growerMobileNumber;
    }

    public void setGrowerMobileNumber(String growerMobileNumber) {
        this.growerMobileNumber = growerMobileNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return growerRyotCode.toString();
    }
}
