package in.co.vibrant.bindalsugar.model;

public class LocationDataModel {

    public static final String TABLE_NAME = "temp_location";
    public static final String COLUMN_ID = "id";
    public static final String Col_SrNo = "srno";
    public static final String Col_Lat = "lat";
    public static final String Col_Lng = "lng";
    public static final String Col_Distance = "distance";
    public static final String Col_Accuracy = "accuracy";



    protected int id;
    protected int serialNumber;
    protected double lat;
    protected double lng;
    protected double distance;
    protected double accuracy;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_SrNo + " INTEGER,"
                    + Col_Lat + " TEXT,"
                    + Col_Lng + " TEXT,"
                    + Col_Distance + " DECIMAL(12,2),"
                    + Col_Accuracy + " DECIMAL(12,2)"
                    + ")";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
