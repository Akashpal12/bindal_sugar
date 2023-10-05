package in.co.vibrant.bindalsugar.model;

public class NotificationModel {

    public static final String TABLE_NAME = "notification_table";
    public static final String COLUMN_ID = "id";
    public static final String Col_title = "title";
    public static final String Col_message = "message";
    public static final String Col_date_t = "date_t";
    public static final String Col_imageUrl = "image_Url";
    public static final String Col_seen = "seen";
    public static final String Col_senduser = "senduser";


    private String colId;
    private String title;
    private String message;
    private String dateTime;
    private String imageUrl;
    private int seen;
    private int senduser;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_title + " TEXT,"
                    + Col_message + " TEXT,"
                    + Col_date_t + " TEXT,"
                    + Col_imageUrl + " TEXT,"
                    + Col_seen + " TEXT,"
                    + Col_senduser + " TEXT"
                    + ")";

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getSenduser() {
        return senduser;
    }

    public void setSenduser(int senduser) {
        this.senduser = senduser;
    }
}