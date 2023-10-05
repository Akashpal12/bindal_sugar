package in.co.vibrant.bindalsugar.model;

public class VillageModal {

    public static final String TABLE_NAME = "village_details";
    public static final String COLUMN_ID = "id";
    public static final String Col_code = "code";
    public static final String Col_name = "name";
    public static final String Col_is_target = "is_target";
    public static final String Col_max_indent = "max_indent";
    public static final String Col_max_plant = "max_plant";

    private String code;
    private String name;
    private int isTarget;
    private String maxIndent;
    private String maxPlant;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_code + " TEXT,"
                    + Col_name + " TEXT,"
                    + Col_is_target + " TEXT,"
                    + Col_max_indent + " TEXT,"
                    + Col_max_plant + " TEXT"
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

    public String getMaxIndent() {
        return maxIndent;
    }

    public void setMaxIndent(String maxIndent) {
        this.maxIndent = maxIndent;
    }

    public String getMaxPlant() {
        return maxPlant;
    }

    public void setMaxPlant(String maxPlant) {
        this.maxPlant = maxPlant;
    }

    public int isTarget() {
        return isTarget;
    }

    public void setTarget(int target) {
        isTarget = target;
    }
}
