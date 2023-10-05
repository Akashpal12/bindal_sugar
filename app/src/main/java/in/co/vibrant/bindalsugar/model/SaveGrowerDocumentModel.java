package in.co.vibrant.bindalsugar.model;

public class SaveGrowerDocumentModel {
    public static final String TABLE_NAME = "GrowerDocument";
    public static final String COLUMN_AGRI_INPUT_FORM_ID = "AgriInputFormId";
    public static final String AGRI_VILLAGE_CODE = "VIllageCode";
    public static final String AGRI_VILLAGE_NAME = "VillageName";
    public static final String AGRI_GROWER_CODE = "GrowerCode";
    public static final String AGRI_GROWER_NAME = "GrowerName";
    public static final String AGRI_GROWER_UNIT_CODE = "GrowerUnitCode";
    public static final String AGRI_GROWER_FATHER = "GrowerFather";
    public static final String AGRI_GROWER_MOBILE = "Mobile";
    public static final String AGRI_COLUMN_PHOTO_1 = "photo_1";
    public static final String AGRI_COLUMN_PHOTO_2 = "photo_2";
    public static final String AGRI_COLUMN_SIGNATURE = "signature";
    public static final String AGRI_COLUMN_THUMB_IMPRESSION = "thumbImpression";
    public static final String AGRI_COLUMN_SERVER_UPLOAD_STATUS = "server_status";
    public static final String AGRI_COLUMN_SERVER_UPLOAD_REMARK = "server_remark";
    public static final String AGRI_CREATED_AT = "created_at";
    public static final String COLUMN_FK_AGRI_INPUT = "FkAgriInput";

    private int agri_input_Id;
    private String agriVillageCode;
    private String agriVillageName;
    private String agriGrowerCode;
    private String agriGrowerName;
    private String agriGrowerUnitCode;
    private String agriGrowerFather;
    private String agriGrowerMobile;
    private String UserId;
    private String agriPhoto_1;
    private String agriPhoto_2;
    private String agriSignature;
    private String agriThumb;
    private String server_status;
    private String server_remark;
    private String created_at;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_AGRI_INPUT_FORM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + AGRI_VILLAGE_CODE + " TEXT,"
                    + AGRI_VILLAGE_NAME + " TEXT,"
                    + AGRI_GROWER_CODE + " TEXT,"
                    + AGRI_GROWER_NAME + " TEXT,"
                    + AGRI_GROWER_UNIT_CODE + " TEXT,"
                    + AGRI_GROWER_FATHER + " TEXT,"
                    + AGRI_GROWER_MOBILE + " TEXT,"
                    + AGRI_COLUMN_PHOTO_1 + " BLOB,"
                    + AGRI_COLUMN_PHOTO_2 + " BLOB,"
                    + AGRI_COLUMN_SIGNATURE + " BLOB,"
                    + AGRI_COLUMN_THUMB_IMPRESSION + " BLOB,"
                    + AGRI_COLUMN_SERVER_UPLOAD_STATUS + " Text,"
                    + AGRI_COLUMN_SERVER_UPLOAD_REMARK + " Text,"
                    + AGRI_CREATED_AT + " Date,"
                    + COLUMN_FK_AGRI_INPUT + " INTEGER"
                    //+ "FOREIGN KEY (" + COLUMN_FK_AGRI_INPUT + ")REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";

    public SaveGrowerDocumentModel() {
    }


    public SaveGrowerDocumentModel(int agri_input_Id, String agriVillageCode, String agriVillageName, String agriGrowerCode, String agriGrowerName, String agriGrowerUnitCode, String agriGrowerFather, String agriGrowerMobile, String agriPhoto_1, String agriPhoto_2, String agriSignature, String agriThumb, String server_status) {
        this.agri_input_Id = agri_input_Id;
        this.agriVillageCode = agriVillageCode;
        this.agriVillageName = agriVillageName;
        this.agriGrowerCode = agriGrowerCode;
        this.agriGrowerName = agriGrowerName;
        this.agriGrowerUnitCode = agriGrowerUnitCode;
        this.agriGrowerFather = agriGrowerFather;
        this.agriGrowerMobile = agriGrowerMobile;
        this.agriPhoto_1 = agriPhoto_1;
        this.agriPhoto_2 = agriPhoto_2;
        this.agriSignature = agriSignature;
        this.agriThumb = agriThumb;
        this.server_status = server_status;
        this.server_remark = server_remark;
    }

    public String getAgriVillageName() {
        return agriVillageName;
    }

    public void setAgriVillageName(String agriVillageName) {
        this.agriVillageName = agriVillageName;
    }

    public String getAgriGrowerName() {
        return agriGrowerName;
    }

    public void setAgriGrowerName(String agriGrowerName) {
        this.agriGrowerName = agriGrowerName;
    }

    public String getAgriGrowerUnitCode() {
        return agriGrowerUnitCode;
    }

    public void setAgriGrowerUnitCode(String agriGrowerUnitCode) {
        this.agriGrowerUnitCode = agriGrowerUnitCode;
    }

    public String getAgriGrowerFather() {
        return agriGrowerFather;
    }

    public void setAgriGrowerFather(String agriGrowerFather) {
        this.agriGrowerFather = agriGrowerFather;
    }

    public String getAgriGrowerMobile() {
        return agriGrowerMobile;
    }

    public void setAgriGrowerMobile(String agriGrowerMobile) {
        this.agriGrowerMobile = agriGrowerMobile;
    }

    public int getAgri_input_Id() {
        return agri_input_Id;
    }

    public void setAgri_input_Id(int agri_input_Id) {
        this.agri_input_Id = agri_input_Id;
    }

    public String getAgriVillageCode() {
        return agriVillageCode;
    }

    public void setAgriVillageCode(String agriVillageCode) {
        this.agriVillageCode = agriVillageCode;
    }

    public String getAgriGrowerCode() {
        return agriGrowerCode;
    }

    public void setAgriGrowerCode(String agriGrowerCode) {
        this.agriGrowerCode = agriGrowerCode;
    }

    public String getAgriPhoto_1() {
        return agriPhoto_1;
    }

    public void setAgriPhoto_1(String agriPhoto_1) {
        this.agriPhoto_1 = agriPhoto_1;
    }

    public String getAgriPhoto_2() {
        return agriPhoto_2;
    }

    public void setAgriPhoto_2(String agriPhoto_2) {
        this.agriPhoto_2 = agriPhoto_2;
    }

    public String getAgriSignature() {
        return agriSignature;
    }

    public void setAgriSignature(String agriSignature) {
        this.agriSignature = agriSignature;
    }

    public String getAgriThumb() {
        return agriThumb;
    }

    public void setAgriThumb(String agriThumb) {
        this.agriThumb = agriThumb;
    }

    public String getServer_status() {
        return server_status;
    }

    public void setServer_status(String server_status) {
        this.server_status = server_status;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getServer_remark() {
        return server_remark;
    }

    public void setServer_remark(String server_remark) {
        this.server_remark = server_remark;
    }
}
