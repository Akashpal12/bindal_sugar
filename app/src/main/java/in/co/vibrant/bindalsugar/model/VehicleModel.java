package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class VehicleModel {

    public static final String TABLE_NAME = "grower_details";
    public static final String COLUMN_ID = "id";
    public static final String Col_vill_code = "vill_code";
    public static final String Col_grower_code = "grower_code";
    public static final String Col_capcity = "CAPCITY";
    public static final String Col_vehicle = "VECHILENO";
    public static final String Col_grower_name = "grower_name";
    public static final String AGRI_COLUMN_grower_json = "growerJson";
    public static final String Col_grower_father_name = "grower_father_name";

    protected String villageCode;
    protected String growerCode;
    protected String growerName;
    protected String growerFather;
    protected String centre;
    protected String society;
    protected String mobile;
    protected String capacity;
    protected String vehicleNo;
    protected String area;
    protected String plotSerial;
    protected String growerSerial;
    protected String plotVillageCode;
    protected String plotVillageName;
    protected String shareArea;
    private String growerJson;
    private String agrigrowerJson;
    protected String relation;
    protected double lat1;
    protected double lat2;
    protected double lat3;
    protected double lat4;
    protected double lng1;
    protected double lng2;
    protected double lng3;
    protected double lng4;
    protected double dim1;
    protected double dim2;
    protected double dim3;
    protected double dim4;
    protected String color;
    protected String textColor;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_vill_code + " TEXT,"
                    + AGRI_COLUMN_grower_json + " growerJson,"
                    + Col_grower_code + " TEXT,"
                    + Col_grower_name + " TEXT,"
                    + Col_grower_father_name + " TEXT"
                    + ")";



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleModel that = (VehicleModel) o;
        return villageCode.equals(that.villageCode) && growerCode.equals(that.growerCode) && relation.equals(that.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(villageCode, growerCode, relation);
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

    public String getGrowerName() {
        return growerName;
    }

    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }

    public String getGrowerFather() {
        return growerFather;
    }

    public void setGrowerFather(String growerFather) {
        this.growerFather = growerFather;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPlotSerial() {
        return plotSerial;
    }

    public void setPlotSerial(String plotSerial) {
        this.plotSerial = plotSerial;
    }

    public String getGrowerSerial() {
        return growerSerial;
    }

    public void setGrowerSerial(String growerSerial) {
        this.growerSerial = growerSerial;
    }

    public String getPlotVillageCode() {
        return plotVillageCode;
    }

    public void setPlotVillageCode(String plotVillageCode) {
        this.plotVillageCode = plotVillageCode;
    }

    public String getPlotVillageName() {
        return plotVillageName;
    }

    public void setPlotVillageName(String plotVillageName) {
        this.plotVillageName = plotVillageName;
    }

    public String getShareArea() {
        return shareArea;
    }

    public void setShareArea(String shareArea) {
        this.shareArea = shareArea;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }

    public double getLat3() {
        return lat3;
    }

    public void setLat3(double lat3) {
        this.lat3 = lat3;
    }

    public double getLat4() {
        return lat4;
    }

    public void setLat4(double lat4) {
        this.lat4 = lat4;
    }

    public double getLng1() {
        return lng1;
    }

    public void setLng1(double lng1) {
        this.lng1 = lng1;
    }

    public double getLng2() {
        return lng2;
    }

    public void setLng2(double lng2) {
        this.lng2 = lng2;
    }

    public double getLng3() {
        return lng3;
    }

    public void setLng3(double lng3) {
        this.lng3 = lng3;
    }

    public double getLng4() {
        return lng4;
    }

    public void setLng4(double lng4) {
        this.lng4 = lng4;
    }

    public double getDim1() {
        return dim1;
    }

    public void setDim1(double dim1) {
        this.dim1 = dim1;
    }

    public double getDim2() {
        return dim2;
    }

    public void setDim2(double dim2) {
        this.dim2 = dim2;
    }

    public double getDim3() {
        return dim3;
    }

    public void setDim3(double dim3) {
        this.dim3 = dim3;
    }

    public double getDim4() {
        return dim4;
    }

    public void setDim4(double dim4) {
        this.dim4 = dim4;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGrowerJson() {
        return growerJson;
    }

    public void setGrowerJson(String growerJson) {
        this.growerJson = growerJson;
    }

    public String getAgrigrowerJson() {
        return agrigrowerJson;
    }

    public void setAgrigrowerJson(String agrigrowerJson) {
        this.agrigrowerJson = agrigrowerJson;
    }


    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
