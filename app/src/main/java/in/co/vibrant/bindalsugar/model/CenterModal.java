package in.co.vibrant.bindalsugar.model;

public class CenterModal {

    public static final String TABLE_NAME = "centre_list";
    public static final String COLUMN_ID = "id";
    public static final String centre_code = "centre_code";
    public static final String centre_name = "centre_name";
    public static final String col_distance = "distance";
    public static final String col_radious = "radious";
    public static final String col_lat = "lat";
    public static final String col_lng = "lng";
    public static final String col_is_gate = "is_gate";


    protected String id;
    protected String code;
    protected String name;
    protected String distance;
    protected String radious;
    protected String lat;
    protected String lng;
    protected String isGate;
    protected String color;
    protected String backgroundColor;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + centre_code + " INTEGER,"
                    + centre_name + " TEXT,"
                    + col_distance + " NUMERIC,"
                    + col_radious + " NUMERIC,"
                    + col_lat + " NUMERIC,"
                    + col_lng + " NUMERIC,"
                    + col_is_gate + " VARCHAR(20)"
                    + ")";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRadious() {
        return radious;
    }

    public void setRadious(String radious) {
        this.radious = radious;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getIsGate() {
        return isGate;
    }

    public void setIsGate(String isGate) {
        this.isGate = isGate;
    }
}
