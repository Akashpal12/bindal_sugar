package in.co.vibrant.bindalsugar.model;

public class MasterCaneSurveyModel {

    public static final String TABLE_NAME = "master_cane_survey";
    public static final String COLUMN_ID = "id";
    public static final String Col_mstCode = "mstcd";
    public static final String Col_code = "code";
    public static final String Col_name = "name";




    protected String ColId;
    protected String mstCode;
    protected String code;
    protected String name;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_mstCode + " TEXT,"
                    + Col_code + " TEXT,"
                    + Col_name  + " TEXT"
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

    public String getMstCode() {
        return mstCode;
    }

    public void setMstCode(String mstCode) {
        this.mstCode = mstCode;
    }

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
}
