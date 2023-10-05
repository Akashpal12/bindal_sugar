package in.co.vibrant.bindalsugar.model;

public class MasterSubDropDown {


    public static final String TABLE_NAME = "sub_master_data";
    public static final String COLUMN_ID = "id";
    public static final String Col_code = "code";
    public static final String Col_name = "name";
    public static final String Col_type = "type";
    public static final String Col_extra_field = "extra_field";
    public static final String Col_master_code = "master_type";
    public static final String Col_item_flag = "item_flag";


    private String code;
    private String name;
    private String type;
    private String masterCode;
    private int extraField;
    private String itemFlag;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_code + " TEXT,"
                    + Col_name + " TEXT,"
                    + Col_type + " TEXT,"
                    + Col_master_code + " TEXT,"
                    + Col_extra_field + " TEXT,"
                    + Col_item_flag + " TEXT"
                    + ")";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public int getExtraField() {
        return extraField;
    }

    public void setExtraField(int extraField) {
        this.extraField = extraField;
    }

    public String getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
}
