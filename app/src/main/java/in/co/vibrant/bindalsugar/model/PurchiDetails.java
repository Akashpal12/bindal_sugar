package in.co.vibrant.bindalsugar.model;

public class PurchiDetails {


    public static final String TABLE_NAME = "purchii_details";
    public static final String COLUMN_ID = "id";
    public static final String Col_code = "code";
    public static final String Col_name = "name";
    public static final String Col_purchino = "Purchyno";
    public static final String Col_mode = "mode";
    public static final String Col_category = "Category";

    private String code;
    private String name;
    private String purchiNo;
    private String mode;
    private String category;
    protected String color;
    protected String textColor;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_code + " TEXT,"
                    + Col_name + " TEXT,"
                    + Col_purchino + " TEXT,"
                    + Col_mode + " TEXT,"
                    + Col_category + " TEXT"
                    + ")";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchiDetails that = (PurchiDetails) o;
        return code.equals(that.code) && name.equals(that.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPurchiNo() {
        return purchiNo;
    }

    public void setPurchiNo(String purchiNo) {
        this.purchiNo = purchiNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
