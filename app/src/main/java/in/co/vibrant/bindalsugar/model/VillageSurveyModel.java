package in.co.vibrant.bindalsugar.model;

public class VillageSurveyModel {

    public static final String TABLE_NAME = "village";
    public static final String COLUMN_ID = "id";
    public static final String Col_villageCode = "village_code";
    public static final String Col_villageName = "village_name";
    public static final String Col_centerName = "center_name";
    public static final String Col_plotSrNo = "plot_sr_no";
    public static final String Col_plotSrNo1 = "plot_sr_no_1";
    public static final String Col_plotSrNo2 = "plot_sr_no_2";
    public static final String Col_stopSurvey = "stop_survey";
    public static final String Col_transSurvey = "trans_survey";
    public static final String Col_is_default = "is_default";



    protected String ColId;
    protected String villageCode;
    protected String villageName;
    protected String centerName ;
    protected String plotSrNo ;
    protected String plotSrNo1 ;
    protected String plotSrNo2 ;
    protected String stopSurvey ;
    protected String transSurvey ;
    protected String isDefault ;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_villageCode + " TEXT,"
                    + Col_villageName + " TEXT,"
                    + Col_centerName  + " TEXT,"
                    + Col_plotSrNo  + " TEXT,"
                    + Col_plotSrNo1  + " TEXT,"
                    + Col_plotSrNo2  + " TEXT,"
                    + Col_stopSurvey  + " TEXT,"
                    + Col_transSurvey  + " TEXT,"
                    + Col_is_default  + " TEXT"
                    + ")";


    public String getColId() {
        return ColId;
    }

    public void setColId(String colId) {
        ColId = colId;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getPlotSrNo() {
        return plotSrNo;
    }

    public void setPlotSrNo(String plotSrNo) {
        this.plotSrNo = plotSrNo;
    }

    public String getPlotSrNo1() {
        return plotSrNo1;
    }

    public void setPlotSrNo1(String plotSrNo1) {
        this.plotSrNo1 = plotSrNo1;
    }

    public String getPlotSrNo2() {
        return plotSrNo2;
    }

    public void setPlotSrNo2(String plotSrNo2) {
        this.plotSrNo2 = plotSrNo2;
    }

    public String getStopSurvey() {
        return stopSurvey;
    }

    public void setStopSurvey(String stopSurvey) {
        this.stopSurvey = stopSurvey;
    }

    public String getTransSurvey() {
        return transSurvey;
    }

    public void setTransSurvey(String transSurvey) {
        this.transSurvey = transSurvey;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
