package in.co.vibrant.bindalsugar.model;

public class IndentModel {

    public static final String TABLE_NAME = "indenting";
    public static final String COLUMN_ID = "Col_id";
    public static final String Col_Village = "Village";
    public static final String Col_Grower = "Grower";
    public static final String Col_Grower_name = "Grower_name";
    public static final String Col_Grower_father = "Grower_father";
    public static final String Col_PLOTVillage = "PLOTVillage";
    public static final String Col_PLOT_SR_NO = "PLOTSRNO";
    public static final String Col_Irrigationmode = "Irrigationmode";
    public static final String Col_SeedSource = "SeedSource";
    public static final String Col_SupplyMode = "SupplyMode";
    public static final String Col_Harvesting = "Harvesting";
    public static final String Col_Equipment = "Equipment";
    public static final String Col_LandType = "LandType";
    public static final String Col_SeedType = "SeedType";
    public static final String Col_NOFPLOTS = "NOFPLOTS";
    public static final String Col_INDAREA = "INDAREA";
    public static final String Col_InsertLAT = "InsertLAT";
    public static final String Col_InsertLON = "InsertLON";
    public static final String Col_Image = "Image";
    public static final String Col_SuperviserCode = "SuperviserCode";
    public static final String Col_Dim1 = "Dim1";
    public static final String Col_Dim2 = "Dim2";
    public static final String Col_Dim3 = "Dim3";
    public static final String Col_Dim4 = "Dim4";
    public static final String Col_Area = "Area";
    public static final String Col_LAT1 = "LAT1";
    public static final String Col_LON1 = "LON1";
    public static final String Col_LAT2 = "LAT2";
    public static final String Col_LON2 = "LON2";
    public static final String Col_LAT3 = "LAT3";
    public static final String Col_LON3 = "LON3";
    public static final String Col_LAT4 = "LAT4";
    public static final String Col_LON4 = "LON4";
    public static final String Col_SprayType = "SprayType";
    public static final String Col_PloughingType = "PloughingType";
    public static final String Col_MobilNO = "MobilNO";
    public static final String Col_MDATE = "MDATE";
    public static final String Col_VARIETY = "VARIETY";
    public static final String Col_Crop = "Crop";
    public static final String Col_PLANTINGTYPE = "PLANTINGTYPE";
    public static final String Col_PLANTATION = "PLANTATION";
    public static final String Col_SMathod = "SMathod";
    public static final String Col_ServerStatus = "ServerStatus";
    public static final String Col_Remark = "Remark";
    public static final String Col_CurrentDate = "CurrentDate";
    public static final String Col_ind_date = "ind_date";
    public static final String Col_is_ideal = "is_ideal";
    public static final String Col_is_nursery = "is_nursery";

    private String colId;
    private String Village;
    private String VillageName;
    private String Grower;
    private String GrowerName;
    private String GrowerFather;
    private String PLOTVillage ;
    private String plotSerialNumber;
    private String seedSource;
    private String Irrigationmode;
    private String SupplyMode ;
    private String Harvesting ;
    private String Equipment ;
    private String LandType ;
    private String SeedType ;
    private String NOFPLOTS ;
    private String INDAREA ;
    private String InsertLAT ;
    private String InsertLON ;
    private String Image;
    private String SuperviserCode ;
    private String Dim1 ;
    private String Dim2 ;
    private String Dim3 ;
    private String Dim4 ;
    private String Area ;
    private String LAT1 ;
    private String LON1 ;
    private String LAT2 ;
    private String LON2 ;
    private String LAT3 ;
    private String LON3 ;
    private String LAT4 ;
    private String LON4 ;
    private String SprayType ;
    private String PloughingType ;
    private String MobilNO;
    private String MDATE;
    private String VARIETY ;
    private String VARIETYNAME ;
    private String Crop ;
    private String PLANTINGTYPE;
    private String PLANTATION ;
    private String Method ;
    private String ServerStatus;
    private String Remark ;
    private String CurrentDate ;
    private String isIdeal ;
    private String isNursery ;
    private String indDate ;
    private String color ;
    private String textColor ;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_Village + " TEXT,"
                    + Col_Grower + " TEXT,"
                    + Col_Grower_name + " TEXT,"
                    + Col_Grower_father + " TEXT,"
                    + Col_PLOTVillage + " TEXT,"
                    + Col_PLOT_SR_NO + " TEXT,"
                    + Col_Irrigationmode + " TEXT,"
                    + Col_SeedSource + " TEXT,"
                    + Col_SupplyMode + " TEXT,"
                    + Col_Harvesting + " TEXT,"
                    + Col_Equipment + " TEXT,"
                    + Col_LandType + " TEXT,"
                    + Col_SeedType + " TEXT,"
                    + Col_NOFPLOTS + " TEXT,"
                    + Col_INDAREA + " TEXT,"
                    + Col_InsertLAT + " TEXT,"
                    + Col_InsertLON + " TEXT,"
                    + Col_Image + " TEXT,"
                    + Col_SuperviserCode + " TEXT,"
                    + Col_Dim1 + " TEXT,"
                    + Col_Dim2 + " TEXT,"
                    + Col_Dim3 + " TEXT,"
                    + Col_Dim4 + " TEXT,"
                    + Col_Area + " TEXT,"
                    + Col_LAT1 + " TEXT,"
                    + Col_LON1 + " TEXT,"
                    + Col_LAT2 + " TEXT,"
                    + Col_LON2 + " TEXT,"
                    + Col_LAT3 + " TEXT,"
                    + Col_LON3 + " TEXT,"
                    + Col_LAT4 + " TEXT,"
                    + Col_LON4 + " TEXT,"
                    + Col_SprayType + " TEXT,"
                    + Col_PloughingType + " TEXT,"
                    + Col_MobilNO + " TEXT,"
                    + Col_MDATE + " TEXT,"
                    + Col_VARIETY + " TEXT,"
                    + Col_Crop + " TEXT,"
                    + Col_PLANTINGTYPE + " TEXT,"
                    + Col_PLANTATION + " TEXT,"
                    + Col_SMathod + " TEXT,"
                    + Col_ServerStatus + " TEXT,"
                    + Col_Remark + " TEXT,"
                    + Col_is_ideal + " TEXT,"
                    + Col_ind_date + " TEXT,"
                    + Col_is_nursery + " TEXT,"
                    + Col_CurrentDate + " TEXT"
                    + ")";


    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getGrower() {
        return Grower;
    }

    public void setGrower(String grower) {
        Grower = grower;
    }

    public String getGrowerName() {
        return GrowerName;
    }

    public void setGrowerName(String growerName) {
        GrowerName = growerName;
    }

    public String getGrowerFather() {
        return GrowerFather;
    }

    public void setGrowerFather(String growerFather) {
        GrowerFather = growerFather;
    }

    public String getPLOTVillage() {
        return PLOTVillage;
    }

    public void setPLOTVillage(String PLOTVillage) {
        this.PLOTVillage = PLOTVillage;
    }

    public String getPlotSerialNumber() {
        return plotSerialNumber;
    }

    public void setPlotSerialNumber(String plotSerialNumber) {
        this.plotSerialNumber = plotSerialNumber;
    }

    public String getIrrigationmode() {
        return Irrigationmode;
    }

    public void setIrrigationmode(String irrigationmode) {
        Irrigationmode = irrigationmode;
    }

    public String getSupplyMode() {
        return SupplyMode;
    }

    public void setSupplyMode(String supplyMode) {
        SupplyMode = supplyMode;
    }

    public String getHarvesting() {
        return Harvesting;
    }

    public void setHarvesting(String harvesting) {
        Harvesting = harvesting;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getLandType() {
        return LandType;
    }

    public void setLandType(String landType) {
        LandType = landType;
    }

    public String getSeedType() {
        return SeedType;
    }

    public void setSeedType(String seedType) {
        SeedType = seedType;
    }

    public String getNOFPLOTS() {
        return NOFPLOTS;
    }

    public void setNOFPLOTS(String NOFPLOTS) {
        this.NOFPLOTS = NOFPLOTS;
    }

    public String getINDAREA() {
        return INDAREA;
    }

    public void setINDAREA(String INDAREA) {
        this.INDAREA = INDAREA;
    }

    public String getInsertLAT() {
        return InsertLAT;
    }

    public void setInsertLAT(String insertLAT) {
        InsertLAT = insertLAT;
    }

    public String getInsertLON() {
        return InsertLON;
    }

    public void setInsertLON(String insertLON) {
        InsertLON = insertLON;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSuperviserCode() {
        return SuperviserCode;
    }

    public void setSuperviserCode(String superviserCode) {
        SuperviserCode = superviserCode;
    }

    public String getDim1() {
        return Dim1;
    }

    public void setDim1(String dim1) {
        Dim1 = dim1;
    }

    public String getDim2() {
        return Dim2;
    }

    public void setDim2(String dim2) {
        Dim2 = dim2;
    }

    public String getDim3() {
        return Dim3;
    }

    public void setDim3(String dim3) {
        Dim3 = dim3;
    }

    public String getDim4() {
        return Dim4;
    }

    public void setDim4(String dim4) {
        Dim4 = dim4;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getLAT1() {
        return LAT1;
    }

    public void setLAT1(String LAT1) {
        this.LAT1 = LAT1;
    }

    public String getLON1() {
        return LON1;
    }

    public void setLON1(String LON1) {
        this.LON1 = LON1;
    }

    public String getLAT2() {
        return LAT2;
    }

    public void setLAT2(String LAT2) {
        this.LAT2 = LAT2;
    }

    public String getLON2() {
        return LON2;
    }

    public void setLON2(String LON2) {
        this.LON2 = LON2;
    }

    public String getLAT3() {
        return LAT3;
    }

    public void setLAT3(String LAT3) {
        this.LAT3 = LAT3;
    }

    public String getLON3() {
        return LON3;
    }

    public void setLON3(String LON3) {
        this.LON3 = LON3;
    }

    public String getLAT4() {
        return LAT4;
    }

    public void setLAT4(String LAT4) {
        this.LAT4 = LAT4;
    }

    public String getLON4() {
        return LON4;
    }

    public void setLON4(String LON4) {
        this.LON4 = LON4;
    }

    public String getSprayType() {
        return SprayType;
    }

    public void setSprayType(String sprayType) {
        SprayType = sprayType;
    }

    public String getPloughingType() {
        return PloughingType;
    }

    public void setPloughingType(String ploughingType) {
        PloughingType = ploughingType;
    }

    public String getMobilNO() {
        return MobilNO;
    }

    public void setMobilNO(String mobilNO) {
        MobilNO = mobilNO;
    }

    public String getMDATE() {
        return MDATE;
    }

    public void setMDATE(String MDATE) {
        this.MDATE = MDATE;
    }

    public String getVARIETY() {
        return VARIETY;
    }

    public void setVARIETY(String VARIETY) {
        this.VARIETY = VARIETY;
    }

    public String getCrop() {
        return Crop;
    }

    public void setCrop(String crop) {
        Crop = crop;
    }

    public String getPLANTINGTYPE() {
        return PLANTINGTYPE;
    }

    public void setPLANTINGTYPE(String PLANTINGTYPE) {
        this.PLANTINGTYPE = PLANTINGTYPE;
    }

    public String getPLANTATION() {
        return PLANTATION;
    }

    public void setPLANTATION(String PLANTATION) {
        this.PLANTATION = PLANTATION;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(String serverStatus) {
        ServerStatus = serverStatus;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    public String getIsIdeal() {
        return isIdeal;
    }

    public void setIsIdeal(String isIdeal) {
        this.isIdeal = isIdeal;
    }

    public String getIsNursery() {
        return isNursery;
    }

    public void setIsNursery(String isNursery) {
        this.isNursery = isNursery;
    }

    public String getIndDate() {
        return indDate;
    }

    public void setIndDate(String indDate) {
        this.indDate = indDate;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getVARIETYNAME() {
        return VARIETYNAME;
    }

    public void setVARIETYNAME(String VARIETYNAME) {
        this.VARIETYNAME = VARIETYNAME;
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
}
