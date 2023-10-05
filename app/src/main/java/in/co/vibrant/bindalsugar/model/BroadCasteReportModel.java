package in.co.vibrant.bindalsugar.model;

public class BroadCasteReportModel {

    public static final String TABLE_NAME = "broadcaste_table";
    public static final String COLUMN_ID = "id";
    public static final String Col_title = "title";
    public static final String Col_message = "message";
    public static final String Col_date_t = "date_t";
    public static final String Col_imageUrl = "image_Url";
    public static final String Col_totalView = "TOTVIEW";
    public static final String Col_totalSend = "TOTALSEND";
    public static final String Col_totalSucess = "TOTSUCCESS";
    public static final String Col_totalFailure = "TOTFAILURE";



    private String colId;
    private String title;
    private String message;
    private String dateTime;
    private String imageUrl;
    private String totalView;
    private String totalSend;
    private String totalSucess;
    private String totalFailure;



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_title + " TEXT,"
                    + Col_message + " TEXT,"
                    + Col_date_t + " TEXT,"
                    + Col_imageUrl + " TEXT,"
                    + Col_totalView + " TEXT,"
                    + Col_totalSucess + " TEXT,"
                    + Col_totalFailure + " TEXT,"
                    + Col_totalSend + " TEXT"
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


    public String getTotalView() {
        return totalView;
    }

    public void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getTotalSend() {
        return totalSend;
    }

    public void setTotalSend(String totalSend) {
        this.totalSend = totalSend;
    }

    public String getTotalSucess() {
        return totalSucess;
    }

    public void setTotalSucess(String totalSucess) {
        this.totalSucess = totalSucess;
    }

    public String getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(String totalFailure) {
        this.totalFailure = totalFailure;
    }
}