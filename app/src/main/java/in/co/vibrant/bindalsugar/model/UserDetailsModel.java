package in.co.vibrant.bindalsugar.model;

public class UserDetailsModel {


    public static final String TABLE_NAME = "user_details";
    public static final String COLUMN_ID = "id";
    public static final String U_CODE = "U_CODE";
    public static final String U_NAME = "U_NAME";
    public static final String U_PHONE = "U_PHONE";
    public static final String U_ID = "U_Id";
    public static final String UT_CODE = "UT_CODE";
    public static final String UT_NAME = "UT_NAME";
    public static final String DS_CODE = "DS_CODE";
    public static final String D_NAME = "D_NAME";
    public static final String Col_gpsAccuracy = "gpsAccuracy";
    public static final String DIVN = "DIVN";
    public static final String NM = "NM";
    public static final String USER_LAVEL = "USER_LAVEL";
    public static final String ISACTIVATE = "ISACTIVATE";
    public static final String ISUPDATEMASTER = "ISUPDATEMASTER";
    public static final String LEAVEFLG = "LEAVEFLG";
    public static final String Gps_wait_time = "Gps_wait_time";
    public static final String MODSUP = "MODSUP";
    public static final String ZONECODE = "ZONECODE";
    public static final String ZONENAME = "ZONENAME";
    public static final String TIMEFROM = "TIMEFROM";
    public static final String TIMETO = "TIMETO";
    public static final String Col_overtime_status = "overtime_status";


    protected String id;
    protected String code;
    protected String name;
    protected String phone;
    protected String userTypeCode;
    protected String userId;
    protected String userTypeName;
    protected String dsCode;
    protected String dsName;
    protected String division;
    protected double gpsAccuracy;
    protected int gpsWaitTime;
    protected String compantName;
    protected String userLavel;
    protected String modeSupply;
    protected String isActivate;
    protected int isUpdateMaster;
    protected String zoneCode;
    protected String zoneName;

    protected int leaveFlag;

    protected int overtimeStatus;

    protected int timeFrom;
    protected int timeTo;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + U_CODE + " TEXT,"
                    + U_NAME + " TEXT,"
                    + U_PHONE + " TEXT,"
                    + U_ID + " TEXT,"
                    + UT_CODE + " TEXT,"
                    + UT_NAME + " TEXT,"
                    + DS_CODE + " TEXT,"
                    + D_NAME + " TEXT,"
                    + Col_gpsAccuracy + " TEXT,"
                    + Gps_wait_time + " TEXT,"
                    + DIVN + " TEXT,"
                    + NM + " TEXT,"
                    + USER_LAVEL + " TEXT,"
                    + ISUPDATEMASTER + " TEXT,"
                    + LEAVEFLG + " TEXT,"
                    + TIMEFROM + " TEXT,"
                    + TIMETO + " TEXT,"
                    + Col_overtime_status + " INTEGER ,"
                    + MODSUP + " TEXT,"
                    + ZONECODE + " TEXT,"
                    + ZONENAME + " TEXT,"
                    + ISACTIVATE + " TEXT"
                    + ")";

    public int getLeaveFlag() {
        return leaveFlag;
    }

    public void setLeaveFlag(int leaveFlag) {
        this.leaveFlag = leaveFlag;
    }

    public int getOvertimeStatus() {
        return overtimeStatus;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public void setOvertimeStatus(int overtimeStatus) {
        this.overtimeStatus = overtimeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(double gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getDsCode() {
        return dsCode;
    }

    public void setDsCode(String dsCode) {
        this.dsCode = dsCode;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCompantName() {
        return compantName;
    }

    public void setCompantName(String compantName) {
        this.compantName = compantName;
    }

    public String getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(String isActivate) {
        this.isActivate = isActivate;
    }

    public String getUserLavel() {
        return userLavel;
    }

    public void setUserLavel(String userLavel) {
        this.userLavel = userLavel;
    }

    public int getIsUpdateMaster() {
        return isUpdateMaster;
    }

    public void setIsUpdateMaster(int isUpdateMaster) {
        this.isUpdateMaster = isUpdateMaster;
    }

    public int getGpsWaitTime() {
        return gpsWaitTime;
    }

    public void setGpsWaitTime(int gpsWaitTime) {
        this.gpsWaitTime = gpsWaitTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModeSupply() {
        return modeSupply;
    }

    public void setModeSupply(String modeSupply) {
        this.modeSupply = modeSupply;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
