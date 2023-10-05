package in.co.vibrant.bindalsugar.model;

public class UserAllInfo {
    public static final String TABLE_NAME = "IEMIDataTable";  //for table name
    //For Table colume
    public static final String COLUMN_ID = "Id";
    public static final String UNIT_CODE = "UnitCode";
    public static final String UNIT_NAME = "UnitName";
    public static final String USER_CODE = "UserCode";
    public static final String CIRCLE_CODE = "CircleCode";
    public static final String CIRCLE_NAME = "CircleName";
    public static final String IMEI = "PhoneIMEI";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_USER_PWD = "UserPassword";
    public static final String COLUMN_USER_STATUS = "UserStatus";


    private int id;
    private String unit_code;
    private String unit_name;
    private String user_code;
    private String circle_code;
    private String circle_name;
    private String imei;
    private String name;
    private String userPass;
    private String userStatus;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + UNIT_CODE + " TEXT,"
                    + UNIT_NAME + " TEXT,"
                    + USER_CODE + " TEXT,"
                    + CIRCLE_CODE + " TEXT,"
                    + CIRCLE_NAME + " TEXT,"
                    + IMEI + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_USER_PWD + " TEXT,"
                    + COLUMN_USER_STATUS + " TEXT"
                    + ")";

    public UserAllInfo() {
    }

    public static String getIMEI() {
        return IMEI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getCircle_code() {
        return circle_code;
    }

    public void setCircle_code(String circle_code) {
        this.circle_code = circle_code;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
