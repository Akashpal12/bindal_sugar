package in.co.vibrant.bindalsugar.model;

public class SprayItemModel {

    public static final String TABLE_NAME = "spray_item";
    public static final String COLUMN_ID = "id";
    public static final String Col_item = "item";
    public static final String Col_item_name = "item_name";
    public static final String Col_unit = "unit";
    public static final String Col_qty = "qty";
    public static final String Col_area = "area";


    private int id;
    private String item;
    private String itemName;
    private String unit;
    private String qty;
    private String area;
    private String color;
    private String textColor;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_item + " TEXT,"
                    + Col_item_name + " TEXT,"
                    + Col_unit + " TEXT,"
                    + Col_qty + " TEXT,"
                    + Col_area + " TEXT"
                    + ")";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public static String getTableName() {
        return TABLE_NAME;
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
