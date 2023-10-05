package in.co.vibrant.bindalsugar.model;

public class LastGpsDataModel {

    public static final String TABLE_NAME = "gps_location_last";

    public static final String COLUMN_ID = "id";
    public static final String Col_lat = "lat";
    public static final String Col_lng = "lng";
    public static final String Col_address = "address";
    public static final String Col_accuracy = "accuracy";
    public static final String Col_bearing = "bearing";
    public static final String Col_speed = "speed";
    public static final String Col_battery = "battery";
    public static final String Col_gpsprovider = "provider";
    public static final String Col_gpsStatus = "gps_status";
    public static final String Col_internet_status = "internet_status";
    public static final String Col_createdAt = "created_at";
    public static final String Col_app_version = "app_version";
    public static final String Col_charging = "charging";
    public static final String Col_server_sent = "server_sent";
    public static final String Col_error = "error";
    public static final String Col_distance = "distance";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_lat + " TEXT,"
                    + Col_lng + " TEXT,"
                    + Col_address + " TEXT,"
                    + Col_accuracy + " TEXT,"
                    + Col_bearing + " TEXT,"
                    + Col_speed + " TEXT,"
                    + Col_gpsprovider + " TEXT,"
                    + Col_battery + " TEXT,"
                    + Col_createdAt + " TEXT,"
                    + Col_app_version + " TEXT,"
                    + Col_internet_status + " TEXT,"
                    + Col_charging + " TEXT,"
                    + Col_server_sent + " TEXT,"
                    + Col_error + " TEXT,"
                    + Col_distance + " TEXT,"
                    + Col_gpsStatus + " TEXT"
                    + ")";
    protected String ColId;
    protected String lat;
    protected String lng;
    protected String address;
    protected String accuracy;
    protected String bearing;
    protected String speed;
    protected String battery;
    protected String provider;
    protected String gpsStatus;
    protected String internetStatus;
    protected String appVersion;
    protected String createdAt;
    protected String charging;
    protected String serverSent;
    protected String error;
    protected String distance;

    public String getColId() {
        return ColId;
    }

    public void setColId(String colId) {
        ColId = colId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getInternetStatus() {
        return internetStatus;
    }

    public void setInternetStatus(String internetStatus) {
        this.internetStatus = internetStatus;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCharging() {
        return charging;
    }

    public void setCharging(String charging) {
        this.charging = charging;
    }

    public String getServerSent() {
        return serverSent;
    }

    public void setServerSent(String serverSent) {
        this.serverSent = serverSent;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}