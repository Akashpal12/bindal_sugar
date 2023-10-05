package in.co.vibrant.bindalsugar.model;

public class TruckDetails {

    public static final String TABLE_NAME = "truck_details";
    public static final String COLUMN_ID = "id";
    public static final String TR_CODE = "TR_CODE";
    public static final String TR_NUMBER = "TR_NUMBER";
    public static final String TR_FACTORY = "TR_FACTORY";
    public static final String TRANSPORTER = "TRANSPORTER";
    public static final String TR_PHONE = "TR_PHONE";
    public static final String F_LAT = "F_LAT";
    public static final String F_LNG = "F_LNG";
    public static final String F_RADIOUSALLOWED = "F_RADIOUSALLOWED";
    public static final String UT_CODE = "UT_CODE";
    public static final String TR_DRIVER = "TR_DRIVER";
    public static final String MOBILE = "MOBILE";


    protected String id;
    protected String factoryCode;
    protected String truckName;
    protected String truckCode;
    protected String truckFactory;
    protected String truckTransporter;
    protected String truckTransporterPhone;
    protected String factoryLat;
    protected String factoryLng;
    protected String factoryRadious;
    protected String userType;
    protected String status;
    protected String driverName;
    protected String driverMobile;
    protected String color;
    protected String backgroundColor;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TR_CODE + " TEXT,"
                    + TR_NUMBER + " TEXT,"
                    + TR_FACTORY + " TEXT,"
                    + TRANSPORTER + " TEXT,"
                    + TR_PHONE + " TEXT,"
                    + F_LAT + " TEXT,"
                    + F_LNG + " TEXT,"
                    + F_RADIOUSALLOWED + " TEXT,"
                    + TR_DRIVER + " TEXT,"
                    + MOBILE + " TEXT,"
                    + UT_CODE + " TEXT"
                    + ")";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    public String getTruckCode() {
        return truckCode;
    }

    public void setTruckCode(String truckCode) {
        this.truckCode = truckCode;
    }

    public String getTruckFactory() {
        return truckFactory;
    }

    public void setTruckFactory(String truckFactory) {
        this.truckFactory = truckFactory;
    }

    public String getTruckTransporter() {
        return truckTransporter;
    }

    public void setTruckTransporter(String truckTransporter) {
        this.truckTransporter = truckTransporter;
    }

    public String getTruckTransporterPhone() {
        return truckTransporterPhone;
    }

    public void setTruckTransporterPhone(String truckTransporterPhone) {
        this.truckTransporterPhone = truckTransporterPhone;
    }

    public String getFactoryLat() {
        return factoryLat;
    }

    public void setFactoryLat(String factoryLat) {
        this.factoryLat = factoryLat;
    }

    public String getFactoryLng() {
        return factoryLng;
    }

    public void setFactoryLng(String factoryLng) {
        this.factoryLng = factoryLng;
    }

    public String getFactoryRadious() {
        return factoryRadious;
    }

    public void setFactoryRadious(String factoryRadious) {
        this.factoryRadious = factoryRadious;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }
}
