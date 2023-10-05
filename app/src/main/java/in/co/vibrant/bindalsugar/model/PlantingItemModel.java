package in.co.vibrant.bindalsugar.model;

public class PlantingItemModel {

    public static final String TABLE_NAME = "planting_item";
    public static final String COLUMN_ID = "Col_id";
    public static final String Col_Code = "Code";
    public static final String Col_QTY = "QTY";
    public static final String Col_planting_id = "planting_id";


    private String colId;
    private String code;
    private String qty;
    private String plantingCode;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_Code + " TEXT,"
                    + Col_QTY + " TEXT,"
                    + Col_planting_id + " TEXT"
                    + ")";


    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlantingCode() {
        return plantingCode;
    }

    public void setPlantingCode(String plantingCode) {
        this.plantingCode = plantingCode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
