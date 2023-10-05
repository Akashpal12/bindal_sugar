package in.co.vibrant.bindalsugar.model;

public class VModal {
    public static final String TABLE_NAME = "village_Name";
    public static final String COLUMN_ID = "id";
    public static final String Col_villCode = "VillCode";
    public static final String Col_villName = "VName";


    private int villageCode;
    private String villName;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_villCode + " TEXT,"
                    + Col_villName + " TEXT"
                    + ")";


    public int getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(int villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillName() {
        return villName;
    }

    public void setVillName(String villName) {
        this.villName = villName;
    }
}
