package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class MasterDropDown {


    public static final String TABLE_NAME = "master_data";
    public static final String COLUMN_ID = "id";
    public static final String Col_code = "code";
    public static final String Col_name = "name";
    public static final String Col_type = "type";

    private String code;
    private String name;
    private String type;
    protected String color;
    protected String textColor;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_code + " TEXT,"
                    + Col_name + " TEXT,"
                    + Col_type + " TEXT"
                    + ")";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterDropDown that = (MasterDropDown) o;
        return code.equals(that.code) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
