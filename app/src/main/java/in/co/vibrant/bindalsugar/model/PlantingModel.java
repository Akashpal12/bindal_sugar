package in.co.vibrant.bindalsugar.model;

public class PlantingModel {

    public static final String TABLE_NAME = "planting";
    public static final String COLUMN_ID = "Col_id";
    public static final String Col_Village = "Village";
    public static final String Col_Grower = "Grower";
    public static final String Col_PLOTVillage = "PLOTVillage";
    public static final String Col_Irrigationmode = "Irrigationmode";
    public static final String Col_SupplyMode = "SupplyMode";
    public static final String Col_Harvesting = "Harvesting";
    public static final String Col_Equipment = "Equipment";
    public static final String Col_LandType = "LandType";
    public static final String Col_SeedType = "SeedType";
    public static final String Col_BasalDose = "BasalDose";
    public static final String Col_SeedTreatment = "SeedTreatment";
    public static final String Col_SMethod = "SMethod";
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
    public static final String Col_Image = "Image";
    public static final String Col_SuperviserCode = "SuperviserCode";
    public static final String Col_ID = "ID";
    public static final String Col_InsertLAT = "InsertLAT";
    public static final String Col_InsertLON = "InsertLON";
    public static final String Col_SprayType = "SprayType";
    public static final String Col_PloughinType = "PloughinType";
    public static final String Col_MANUALAREA = "MANUALAREA";
    public static final String Col_MOBILENO = "MOBILENO";
    public static final String Col_MDATE = "MDATE";
    public static final String Col_VARIETY = "VARIETY";
    public static final String Col_Crop = "Crop";
    public static final String Col_PLANTINGTYPE = "PLANTINGTYPE";
    public static final String Col_PLANTATION = "PLANTATION";
    public static final String Col_SEEDTT = "SEEDTT";
    public static final String Col_SEEDSET = "SEEDSET";
    public static final String Col_SOILTR = "SOILTR";
    public static final String Col_ROWTOROW = "ROWTOROW";
    public static final String Col_ActualAreaType = "ActualAreaType";
    public static final String Col_IMAREA = "IMAREA";
    public static final String Col_SVillage = "SVillage";
    public static final String Col_SGrower = "SGrower";
    public static final String Col_SVariety = "SVariety";
    public static final String Col_STransporter = "STransporter";
    public static final String Col_SDistance = "SDistance";
    public static final String Col_SSeedQty = "SSeedQty";
    public static final String Col_Rate = "Rate";
    public static final String Col_OtherAmount = "OtherAmount";
    public static final String Col_PayAmount = "PayAmount";
    public static final String Col_PayMod = "PayMod";
    public static final String Col_MillPurchy = "MillPurchy";
    public static final String Col_ServerStatus = "ServerStatus";
    public static final String Col_Remark = "Remark";
    public static final String Col_PlotSerialNumber = "PlotSerialNumber";
    public static final String Col_ISIDEAL = "ISIDEAL";
    public static final String Col_ISNURSERY = "ISNURSERY";
    public static final String Col_seed_bag_qty = "input_seed_bag_qty";
    public static final String Col_CurrentDate = "CurrentDate";

    private String colId;
    private String Village;
    private String Grower;
    private String baselDose;
    private String seedTreatment;
    private String PLOTVillage ;
    private String Irrigationmode;
    private String SupplyMode ;
    private String Harvesting ;
    private String Equipment ;
    private String LandType ;
    private String SeedType ;
    private String smMethod ;
    private String mobileNumber ;
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
    private String soilType ;
    private String seedSource ;
    private String soilTreatment ;
    private String plantingType ;
    private String rowToRowDistance ;
    private String crop ;
    private String manualArea;
    private String mDate;
    private String VARIETY ;
    private String Crop ;
    private String plantation ;
    private String seedType ;
    private String seedSetType ;
    private String actualAreaType ;
    private String seedVillage ;
    private String seedGrower ;
    private String seedTransporter ;
    private String seedDistance ;
    private String seedQuantity ;
    private String seedRate ;
    private String seedOtherAmount ;
    private String seedPayAmount ;
    private String seedPayMode ;
    private String millPurchey ;
    private String Method ;
    private String id ;
    private String ServerStatus;
    private String Remark ;
    private String CurrentDate ;
    private String plotSerialNumber ;
    private String isIdeal ;
    private String seedBagQty ;
    private String isNursery ;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_Village + " TEXT,"
                    + Col_Grower + " TEXT,"
                    + Col_BasalDose + " TEXT,"
                    + Col_SeedTreatment + " TEXT,"
                    + Col_PLOTVillage + " TEXT,"
                    + Col_Irrigationmode + " TEXT,"
                    + Col_SupplyMode + " TEXT,"
                    + Col_Harvesting + " TEXT,"
                    + Col_Equipment + " TEXT,"
                    + Col_LandType + " TEXT,"
                    + Col_SeedType + " TEXT,"
                    + Col_SMethod + " TEXT,"
                    + Col_MOBILENO + " TEXT,"
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
                    + Col_PloughinType + " TEXT,"
                    + Col_MANUALAREA + " TEXT,"
                    + Col_MDATE + " TEXT,"
                    + Col_VARIETY + " TEXT,"
                    + Col_Crop + " TEXT,"
                    + Col_PLANTINGTYPE + " TEXT,"
                    + Col_PLANTATION + " TEXT,"
                    + Col_SEEDSET + " TEXT,"
                    + Col_SEEDTT + " TEXT,"
                    + Col_SOILTR + " TEXT,"
                    + Col_ROWTOROW + " TEXT,"
                    + Col_ActualAreaType + " TEXT,"
                    + Col_IMAREA + " TEXT,"
                    + Col_SVillage + " TEXT,"
                    + Col_SGrower + " TEXT,"
                    + Col_SVariety + " TEXT,"
                    + Col_STransporter + " TEXT,"
                    + Col_SDistance + " TEXT,"
                    + Col_SSeedQty + " TEXT,"
                    + Col_Rate + " TEXT,"
                    + Col_OtherAmount + " TEXT,"
                    + Col_PayAmount + " TEXT,"
                    + Col_PayMod + " TEXT,"
                    + Col_MillPurchy + " TEXT,"
                    + Col_ID + " TEXT,"
                    + Col_ServerStatus + " TEXT,"
                    + Col_Remark + " TEXT,"
                    + Col_PlotSerialNumber + " TEXT,"
                    + Col_ISIDEAL + " TEXT,"
                    + Col_ISNURSERY + " TEXT,"
                    + Col_seed_bag_qty + " TEXT,"
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

    public String getBaselDose() {
        return baselDose;
    }

    public void setBaselDose(String baselDose) {
        this.baselDose = baselDose;
    }

    public String getSeedTreatment() {
        return seedTreatment;
    }

    public void setSeedTreatment(String seedTreatment) {
        this.seedTreatment = seedTreatment;
    }

    public String getPLOTVillage() {
        return PLOTVillage;
    }

    public void setPLOTVillage(String PLOTVillage) {
        this.PLOTVillage = PLOTVillage;
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

    public String getSeedSetType() {
        return seedSetType;
    }

    public void setSeedSetType(String seedSetType) {
        this.seedSetType = seedSetType;
    }

    public String getActualAreaType() {
        return actualAreaType;
    }

    public void setActualAreaType(String actualAreaType) {
        this.actualAreaType = actualAreaType;
    }

    public String getSeedVillage() {
        return seedVillage;
    }

    public void setSeedVillage(String seedVillage) {
        this.seedVillage = seedVillage;
    }

    public String getSeedGrower() {
        return seedGrower;
    }

    public void setSeedGrower(String seedGrower) {
        this.seedGrower = seedGrower;
    }

    public String getSeedTransporter() {
        return seedTransporter;
    }

    public void setSeedTransporter(String seedTransporter) {
        this.seedTransporter = seedTransporter;
    }

    public String getSeedDistance() {
        return seedDistance;
    }

    public void setSeedDistance(String seedDistance) {
        this.seedDistance = seedDistance;
    }

    public String getSeedQuantity() {
        return seedQuantity;
    }

    public void setSeedQuantity(String seedQuantity) {
        this.seedQuantity = seedQuantity;
    }

    public String getSeedRate() {
        return seedRate;
    }

    public void setSeedRate(String seedRate) {
        this.seedRate = seedRate;
    }

    public String getSeedOtherAmount() {
        return seedOtherAmount;
    }

    public void setSeedOtherAmount(String seedOtherAmount) {
        this.seedOtherAmount = seedOtherAmount;
    }

    public String getSeedPayAmount() {
        return seedPayAmount;
    }

    public void setSeedPayAmount(String seedPayAmount) {
        this.seedPayAmount = seedPayAmount;
    }

    public String getSeedPayMode() {
        return seedPayMode;
    }

    public void setSeedPayMode(String seedPayMode) {
        this.seedPayMode = seedPayMode;
    }

    public String getMillPurchey() {
        return millPurchey;
    }

    public void setMillPurchey(String millPurchey) {
        this.millPurchey = millPurchey;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSmMethod() {
        return smMethod;
    }

    public void setSmMethod(String smMethod) {
        this.smMethod = smMethod;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    public String getSoilTreatment() {
        return soilTreatment;
    }

    public void setSoilTreatment(String soilTreatment) {
        this.soilTreatment = soilTreatment;
    }

    public String getPlantingType() {
        return plantingType;
    }

    public void setPlantingType(String plantingType) {
        this.plantingType = plantingType;
    }

    public String getRowToRowDistance() {
        return rowToRowDistance;
    }

    public void setRowToRowDistance(String rowToRowDistance) {
        this.rowToRowDistance = rowToRowDistance;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getPlantation() {
        return plantation;
    }

    public void setPlantation(String plantation) {
        this.plantation = plantation;
    }

    public String getManualArea() {
        return manualArea;
    }

    public void setManualArea(String manualArea) {
        this.manualArea = manualArea;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getVARIETY() {
        return VARIETY;
    }

    public void setVARIETY(String VARIETY) {
        this.VARIETY = VARIETY;
    }

    public String getPlotSerialNumber() {
        return plotSerialNumber;
    }

    public void setPlotSerialNumber(String plotSerialNumber) {
        this.plotSerialNumber = plotSerialNumber;
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

    public String getSeedBagQty() {
        return seedBagQty;
    }

    public void setSeedBagQty(String seedBagQty) {
        this.seedBagQty = seedBagQty;
    }
}
