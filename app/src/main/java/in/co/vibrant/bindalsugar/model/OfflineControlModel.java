package in.co.vibrant.bindalsugar.model;


public class OfflineControlModel {


    public static final String TABLE_NAME = "offline_control";
    public static final String COLUMN_ID = "id";
    public static final String printer_mac = "printer_mac";


    protected String id;
    protected String printAddress;

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + printer_mac + " TEXT"
                    + ")";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrintAddress() {
        return printAddress;
    }

    public void setPrintAddress(String printAddress) {
        this.printAddress = printAddress;
    }
}