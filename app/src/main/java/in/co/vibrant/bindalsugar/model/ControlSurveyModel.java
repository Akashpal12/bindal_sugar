package in.co.vibrant.bindalsugar.model;

public class ControlSurveyModel {

    public static final String TABLE_NAME = "control";
    public static final String COLUMN_ID = "id";
    public static final String Col_khashraNo = "khashra_no";
    public static final String Col_gataNo = "gata_no";
    public static final String Col_is4G = "is_4G";
    public static final String Col_isARM9 = "is_ARM9";
    public static final String Col_MixCrop = "mix_crop";
    public static final String Col_Insect = "insect";
    public static final String Col_SeedSource = "seed_source";
    public static final String Col_AadharNo = "aadhar_no";
    public static final String Col_plant = "plant";
    public static final String Col_ratoon1 = "ratoon1";
    public static final String Col_ratoon2 = "ratoon2";
    public static final String Col_autumn = "autumn";
    public static final String Col_ishunPerShare = "is_hun_per_share";
    public static final String Col_FortNo = "fort_no";
    public static final String Col_Aadhar01 = "aadhar_01";
    public static final String Col_Extra = "extra";
    public static final String Col_siteCode = "site_code";
    public static final String Col_startHr = "start_hr";
    public static final String Col_stopHr = "stop_hr";
    public static final String Col_isOnline = "is_online";
    public static final String Col_ftpAddr = "ftp_addr";
    public static final String Col_ftpUser = "ftp_user";
    public static final String Col_ftpPass = "ftp_pass";
    public static final String Col_staticIp = "static_ip";
    public static final String Col_staticPort = "static_port";
    public static final String Col_hideArea = "hide_area";
    public static final String Col_isGpsPts = "is_gps_pts";
    public static final String Col_shareInPer = "share_in_per";
    public static final String Col_minLength = "min_length";
    public static final String Col_maxLength = "max_length";
    public static final String Col_sideDiffPer = "side_diff_per";
    public static final String Col_mobOPrCd = "mob_pr_cd";
    public static final String Col_PlantMethod = "plant_method";
    public static final String Col_CropCond = "crop_cond";
    public static final String Col_Disease = "disease";
    public static final String Col_SocClerk = "soc_cleark";
    public static final String Col_PlantDate = "plant_date";
    public static final String Col_Irrigation = "irrigation";
    public static final String Col_SoilType = "soil_type";
    public static final String Col_LandType = "land_type";
    public static final String Col_BorderCrop = "border_crop";
    public static final String Col_PlotType = "plot_type";
    public static final String Col_GhashtiNo = "ghashti_no";
    public static final String Col_SmsRecMobNo = "sms_rec_mob_no";
    public static final String Col_ftpHr1 = "ftp_hr_1";
    public static final String Col_ftpHr2 = "ftp_hr_2";
    public static final String Col_ftpHr3 = "ftp_hr_3";
    public static final String Col_ftpHr4= "ftp_hr_4";
    public static final String Col_factoryId= "factoryId";
    public static final String Col_InterCrop= "inter_crop";
    public static final String Col_mcCode= "mc_code";
    public static final String Col_isXml= "is_xml";
    public static final String Col_Insect1= "insect_1";
    public static final String Col_InterCrop1= "inter_crop_1";
    public static final String Col_MixCrop1= "mix_crop_1";
    public static final String Col_SeedSource1= "seed_source_1";


    protected String khashraNo;
    protected String gataNo;
    protected String is4G;
    protected String isARM9;
    protected String MixCrop;
    protected String Insect;
    protected String SeedSource;
    protected String AadharNo;
    protected String plant;
    protected String ratoon1;
    protected String ratoon2;
    protected String autumn;
    protected String ishunPerShare;
    protected String FortNo;
    protected String Aadhar01;
    protected String Extra;
    protected String siteCode;
    protected String startHr;
    protected String stopHr;
    protected String isOnline;
    protected String ftpAddr;
    protected String ftpUser;
    protected String ftpPass;
    protected String staticIp;
    protected String staticPort;
    protected String hideArea;
    protected String isGpsPts;
    protected String shareInPer;
    protected String minLength;
    protected String maxLength;
    protected String sideDiffPer;
    protected String mobOPrCd;
    protected String PlantMethod;
    protected String CropCond;
    protected String Disease;
    protected String SocClerk;
    protected String PlantDate;
    protected String Irrigation;
    protected String SoilType;
    protected String LandType;
    protected String BorderCrop;
    protected String PlotType;
    protected String GhashtiNo;
    protected String SmsRecMobNo;
    protected String ftpHr1;
    protected String ftpHr2;
    protected String ftpHr3;
    protected String ftpHr4;
    protected String factoryId;
    protected String InterCrop;
    protected String mcCode;
    protected String isXml;
    protected String Insect1;
    protected String InterCrop1;
    protected String MixCrop1;
    protected String SeedSource1;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Col_khashraNo + " TEXT,"
                    + Col_gataNo + " TEXT,"
                    + Col_is4G + " TEXT,"
                    + Col_isARM9 + " TEXT,"
                    + Col_MixCrop + " TEXT,"
                    + Col_Insect + " TEXT,"
                    + Col_SeedSource + " TEXT,"
                    + Col_AadharNo + " TEXT,"
                    + Col_plant + " TEXT,"
                    + Col_ratoon1 + " TEXT,"
                    + Col_ratoon2 + " TEXT,"
                    + Col_autumn + " TEXT,"
                    + Col_ishunPerShare + " TEXT,"
                    + Col_FortNo + " TEXT,"
                    + Col_Aadhar01 + " TEXT,"
                    + Col_Extra + " TEXT,"
                    + Col_siteCode + " TEXT,"
                    + Col_startHr + " TEXT,"
                    + Col_stopHr + " TEXT,"
                    + Col_isOnline + " TEXT,"
                    + Col_ftpAddr + " TEXT,"
                    + Col_ftpUser + " TEXT,"
                    + Col_ftpPass + " TEXT,"
                    + Col_staticIp + " TEXT,"
                    + Col_staticPort + " TEXT,"
                    + Col_hideArea + " TEXT,"
                    + Col_isGpsPts + " TEXT,"
                    + Col_shareInPer + " TEXT,"
                    + Col_minLength + " TEXT,"
                    + Col_maxLength + " TEXT,"
                    + Col_sideDiffPer + " TEXT,"
                    + Col_mobOPrCd + " TEXT,"
                    + Col_PlantMethod + " TEXT,"
                    + Col_CropCond + " TEXT,"
                    + Col_Disease + " TEXT,"
                    + Col_SocClerk + " TEXT,"
                    + Col_PlantDate + " TEXT,"
                    + Col_Irrigation + " TEXT,"
                    + Col_SoilType + " TEXT,"
                    + Col_LandType + " TEXT,"
                    + Col_BorderCrop + " TEXT,"
                    + Col_PlotType + " TEXT,"
                    + Col_GhashtiNo + " TEXT,"
                    + Col_SmsRecMobNo + " TEXT,"
                    + Col_ftpHr1 + " TEXT,"
                    + Col_ftpHr2 + " TEXT,"
                    + Col_ftpHr3 + " TEXT,"
                    + Col_ftpHr4 + " TEXT,"
                    + Col_factoryId + " TEXT,"
                    + Col_InterCrop + " TEXT,"
                    + Col_mcCode + " TEXT,"
                    + Col_isXml + " TEXT,"
                    + Col_Insect1 + " TEXT,"
                    + Col_InterCrop1 + " TEXT,"
                    + Col_MixCrop1 + " TEXT,"
                    + Col_SeedSource1 + " TEXT"
                    + ")";


    public String getKhashraNo() {
        return khashraNo;
    }

    public void setKhashraNo(String khashraNo) {
        this.khashraNo = khashraNo;
    }

    public String getGataNo() {
        return gataNo;
    }

    public void setGataNo(String gataNo) {
        this.gataNo = gataNo;
    }

    public String getIs4G() {
        return is4G;
    }

    public void setIs4G(String is4G) {
        this.is4G = is4G;
    }

    public String getIsARM9() {
        return isARM9;
    }

    public void setIsARM9(String isARM9) {
        this.isARM9 = isARM9;
    }

    public String getMixCrop() {
        return MixCrop;
    }

    public void setMixCrop(String mixCrop) {
        MixCrop = mixCrop;
    }

    public String getInsect() {
        return Insect;
    }

    public void setInsect(String insect) {
        Insect = insect;
    }

    public String getSeedSource() {
        return SeedSource;
    }

    public void setSeedSource(String seedSource) {
        SeedSource = seedSource;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getRatoon1() {
        return ratoon1;
    }

    public void setRatoon1(String ratoon1) {
        this.ratoon1 = ratoon1;
    }

    public String getRatoon2() {
        return ratoon2;
    }

    public void setRatoon2(String ratoon2) {
        this.ratoon2 = ratoon2;
    }

    public String getAutumn() {
        return autumn;
    }

    public void setAutumn(String autumn) {
        this.autumn = autumn;
    }

    public String getIshunPerShare() {
        return ishunPerShare;
    }

    public void setIshunPerShare(String ishunPerShare) {
        this.ishunPerShare = ishunPerShare;
    }

    public String getFortNo() {
        return FortNo;
    }

    public void setFortNo(String fortNo) {
        FortNo = fortNo;
    }

    public String getAadhar01() {
        return Aadhar01;
    }

    public void setAadhar01(String aadhar01) {
        Aadhar01 = aadhar01;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getStartHr() {
        return startHr;
    }

    public void setStartHr(String startHr) {
        this.startHr = startHr;
    }

    public String getStopHr() {
        return stopHr;
    }

    public void setStopHr(String stopHr) {
        this.stopHr = stopHr;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getFtpAddr() {
        return ftpAddr;
    }

    public void setFtpAddr(String ftpAddr) {
        this.ftpAddr = ftpAddr;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public void setFtpPass(String ftpPass) {
        this.ftpPass = ftpPass;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public void setStaticIp(String staticIp) {
        this.staticIp = staticIp;
    }

    public String getStaticPort() {
        return staticPort;
    }

    public void setStaticPort(String staticPort) {
        this.staticPort = staticPort;
    }

    public String getHideArea() {
        return hideArea;
    }

    public void setHideArea(String hideArea) {
        this.hideArea = hideArea;
    }

    public String getIsGpsPts() {
        return isGpsPts;
    }

    public void setIsGpsPts(String isGpsPts) {
        this.isGpsPts = isGpsPts;
    }

    public String getShareInPer() {
        return shareInPer;
    }

    public void setShareInPer(String shareInPer) {
        this.shareInPer = shareInPer;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getSideDiffPer() {
        return sideDiffPer;
    }

    public void setSideDiffPer(String sideDiffPer) {
        this.sideDiffPer = sideDiffPer;
    }

    public String getMobOPrCd() {
        return mobOPrCd;
    }

    public void setMobOPrCd(String mobOPrCd) {
        this.mobOPrCd = mobOPrCd;
    }

    public String getPlantMethod() {
        return PlantMethod;
    }

    public void setPlantMethod(String plantMethod) {
        PlantMethod = plantMethod;
    }

    public String getCropCond() {
        return CropCond;
    }

    public void setCropCond(String cropCond) {
        CropCond = cropCond;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getSocClerk() {
        return SocClerk;
    }

    public void setSocClerk(String socClerk) {
        SocClerk = socClerk;
    }

    public String getPlantDate() {
        return PlantDate;
    }

    public void setPlantDate(String plantDate) {
        PlantDate = plantDate;
    }

    public String getIrrigation() {
        return Irrigation;
    }

    public void setIrrigation(String irrigation) {
        Irrigation = irrigation;
    }

    public String getSoilType() {
        return SoilType;
    }

    public void setSoilType(String soilType) {
        SoilType = soilType;
    }

    public String getLandType() {
        return LandType;
    }

    public void setLandType(String landType) {
        LandType = landType;
    }

    public String getBorderCrop() {
        return BorderCrop;
    }

    public void setBorderCrop(String borderCrop) {
        BorderCrop = borderCrop;
    }

    public String getPlotType() {
        return PlotType;
    }

    public void setPlotType(String plotType) {
        PlotType = plotType;
    }

    public String getGhashtiNo() {
        return GhashtiNo;
    }

    public void setGhashtiNo(String ghashtiNo) {
        GhashtiNo = ghashtiNo;
    }

    public String getSmsRecMobNo() {
        return SmsRecMobNo;
    }

    public void setSmsRecMobNo(String smsRecMobNo) {
        SmsRecMobNo = smsRecMobNo;
    }

    public String getFtpHr1() {
        return ftpHr1;
    }

    public void setFtpHr1(String ftpHr1) {
        this.ftpHr1 = ftpHr1;
    }

    public String getFtpHr2() {
        return ftpHr2;
    }

    public void setFtpHr2(String ftpHr2) {
        this.ftpHr2 = ftpHr2;
    }

    public String getFtpHr3() {
        return ftpHr3;
    }

    public void setFtpHr3(String ftpHr3) {
        this.ftpHr3 = ftpHr3;
    }

    public String getFtpHr4() {
        return ftpHr4;
    }

    public void setFtpHr4(String ftpHr4) {
        this.ftpHr4 = ftpHr4;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getInterCrop() {
        return InterCrop;
    }

    public void setInterCrop(String interCrop) {
        InterCrop = interCrop;
    }

    public String getMcCode() {
        return mcCode;
    }

    public void setMcCode(String mcCode) {
        this.mcCode = mcCode;
    }

    public String getIsXml() {
        return isXml;
    }

    public void setIsXml(String isXml) {
        this.isXml = isXml;
    }

    public String getInsect1() {
        return Insect1;
    }

    public void setInsect1(String insect1) {
        Insect1 = insect1;
    }

    public String getInterCrop1() {
        return InterCrop1;
    }

    public void setInterCrop1(String interCrop1) {
        InterCrop1 = interCrop1;
    }

    public String getMixCrop1() {
        return MixCrop1;
    }

    public void setMixCrop1(String mixCrop1) {
        MixCrop1 = mixCrop1;
    }

    public String getSeedSource1() {
        return SeedSource1;
    }

    public void setSeedSource1(String seedSource1) {
        SeedSource1 = seedSource1;
    }
}
