package in.co.vibrant.bindalsugar.model;

public class ControlModel {

    public static final String TABLE_NAME = "control_details";
    public static final String COLUMN_ID = "id";
    public static final String Col_name = "name";
    public static final String Col_value = "value";
    public static final String Col_form_name = "form_name";

    private String name;
    private String value;
    private String formName;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_name + " TEXT,"
                    + Col_value + " TEXT,"
                    + Col_form_name + " TEXT"
                    + ")";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
