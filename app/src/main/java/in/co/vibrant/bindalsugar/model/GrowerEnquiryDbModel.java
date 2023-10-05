package in.co.vibrant.bindalsugar.model;

public class GrowerEnquiryDbModel {

    public static final String TABLE_NAME = "grower_enquiry";
    public static final String COLUMN_ID = "id";
    public static final String factory_code = "factory_code";
    public static final String village_code = "village_code";
    public static final String grower_code = "grower_code";
    public static final String fin_year_code = "fin_year_code";
    public static final String fin_year = "fin_year";
    public static final String ryt_code = "ryt_code";


    private String factoryCode;
    private String villageCode;
    private String growerCode;
    private String finYearCode;
    private String finYear;
    private String rytCode;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + factory_code + " TEXT,"
                    + village_code + " TEXT,"
                    + grower_code + " TEXT,"
                    + fin_year_code + " TEXT,"
                    + fin_year + " TEXT,"
                    + ryt_code + " TEXT"
                    + ")";

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getGrowerCode() {
        return growerCode;
    }

    public void setGrowerCode(String growerCode) {
        this.growerCode = growerCode;
    }

    public String getFinYearCode() {
        return finYearCode;
    }

    public void setFinYearCode(String finYearCode) {
        this.finYearCode = finYearCode;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getRytCode() {
        return rytCode;
    }

    public void setRytCode(String rytCode) {
        this.rytCode = rytCode;
    }
}
