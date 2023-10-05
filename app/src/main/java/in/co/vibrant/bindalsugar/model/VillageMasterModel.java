package in.co.vibrant.bindalsugar.model;

import static in.co.vibrant.bindalsugar.model.IndentModel.COLUMN_ID;

import androidx.annotation.NonNull;

public class VillageMasterModel {
    public static final String TABLE_NAME = "VillageMaster";  //for table name
    //For Table colume
    public static final String COLUMN_VID = "VillageId";
    public static final String V_UNIT_CODE = "VillageUnitCode";
    public static final String VILLAGE_CODE = "VillageCode";
    public static final String VILLAGE_NAME = "VillageName";
    public static final String VILLAGE_STATUS = "VillageStatus";
    public static final String COLUMN_FK_VILLAGE = "FkVillage";

    private int v_Id;
    private String village_unit_code;
    private String village_code;
    private String village_name;
    private String village_status;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_VID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + V_UNIT_CODE + " TEXT,"
                    + VILLAGE_CODE + " TEXT,"
                    + VILLAGE_NAME + " TEXT,"
                    + VILLAGE_STATUS + " TEXT,"
                    + COLUMN_FK_VILLAGE + " INTEGER,"
                    + "FOREIGN KEY (" + COLUMN_FK_VILLAGE + ")REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";



    public VillageMasterModel() {
    }

    public String getVillage_status() {
        return village_status;
    }

    public void setVillage_status(String village_status) {
        this.village_status = village_status;
    }

    public int getV_Id() {
        return v_Id;
    }

    public void setV_Id(int v_Id) {
        this.v_Id = v_Id;
    }

    public String getVillage_unit_code() {
        return village_unit_code;
    }

    public void setVillage_unit_code(String village_unit_code) {
        this.village_unit_code = village_unit_code;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }


    @NonNull
    @Override
    public String toString() {
        return village_code.toString();
    }
}
