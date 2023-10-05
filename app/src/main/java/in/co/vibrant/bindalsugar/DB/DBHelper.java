package in.co.vibrant.bindalsugar.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.model.AddMaterialDataModel;
import in.co.vibrant.bindalsugar.model.AddSearchedMaterialModel;
import in.co.vibrant.bindalsugar.model.AgriInputFormModel;
import in.co.vibrant.bindalsugar.model.BroadCasteReportModel;
import in.co.vibrant.bindalsugar.model.ControlModel;
import in.co.vibrant.bindalsugar.model.ControlSurveyModel;
import in.co.vibrant.bindalsugar.model.FarmerModel;
import in.co.vibrant.bindalsugar.model.FarmerShareModel;
import in.co.vibrant.bindalsugar.model.FarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.IndentModel;
import in.co.vibrant.bindalsugar.model.LastGpsDataModel;
import in.co.vibrant.bindalsugar.model.LocationDataModel;
import in.co.vibrant.bindalsugar.model.MasterCaneSurveyModel;
import in.co.vibrant.bindalsugar.model.MasterDropDown;
import in.co.vibrant.bindalsugar.model.MasterSubDropDown;
import in.co.vibrant.bindalsugar.model.MaterialMasterModel;
import in.co.vibrant.bindalsugar.model.NotificationModel;
import in.co.vibrant.bindalsugar.model.OfflineControlModel;
import in.co.vibrant.bindalsugar.model.PlantingEquipmentModel;
import in.co.vibrant.bindalsugar.model.PlantingItemModel;
import in.co.vibrant.bindalsugar.model.PlantingModel;
import in.co.vibrant.bindalsugar.model.PlotActivitySaveModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareModel;
import in.co.vibrant.bindalsugar.model.PlotFarmerShareOldModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyModel;
import in.co.vibrant.bindalsugar.model.PlotSurveyOldModel;
import in.co.vibrant.bindalsugar.model.PurchiDetails;
import in.co.vibrant.bindalsugar.model.ReportCaneTypeSummeryModel;
import in.co.vibrant.bindalsugar.model.ReportGrowerPlotSummeryModel;
import in.co.vibrant.bindalsugar.model.SaveGrowerDocumentModel;
import in.co.vibrant.bindalsugar.model.SprayItemModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VModal;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.services.GpsDataModel;
import in.co.vibrant.bindalsugar.util.APIUrl;


public class DBHelper extends SQLiteOpenHelper {

    /* DB Info*/
    private static final String TAG = "DatabaseHelper";
    /* DB Info*/
    private static final int DATABASE_VERSION = 1;
    private static final File sdCard = Environment.getExternalStorageDirectory();
    private static final File dir = new File(sdCard.getAbsolutePath() + "/bindal_dev");
    private static final String DATABASE_NAME = "development_bindal_sugar";
    SQLiteDatabase database;
    private Context context;

    public DBHelper(Context context) {
        super(context, dir.getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(UserDetailsModel.CREATE_TABLE);
        database.execSQL(ControlSurveyModel.CREATE_TABLE);
        database.execSQL(PlotSurveyModel.CREATE_TABLE);
        database.execSQL(PlotSurveyOldModel.CREATE_TABLE);
        database.execSQL(VillageSurveyModel.CREATE_TABLE);
        database.execSQL(FarmerModel.CREATE_TABLE);
        database.execSQL(MasterCaneSurveyModel.CREATE_TABLE);
        database.execSQL(FarmerShareModel.CREATE_TABLE);
        database.execSQL(FarmerShareOldModel.CREATE_TABLE);
        database.execSQL(MasterDropDown.CREATE_TABLE);
        database.execSQL(VillageModal.CREATE_TABLE);
        database.execSQL(GrowerModel.CREATE_TABLE);
        database.execSQL(ControlModel.CREATE_TABLE);
        database.execSQL(IndentModel.CREATE_TABLE);
        database.execSQL(PlantingModel.CREATE_TABLE);
        database.execSQL(PlantingEquipmentModel.CREATE_TABLE);
        database.execSQL(MaterialMasterModel.CREATE_TABLE);
        database.execSQL(PlantingItemModel.CREATE_TABLE);
        database.execSQL(AgriInputFormModel.CREATE_TABLE);
        database.execSQL(AddMaterialDataModel.CREATE_TABLE);
        database.execSQL(SprayItemModel.CREATE_TABLE);
        database.execSQL(LocationDataModel.CREATE_TABLE);
        database.execSQL(PlotActivitySaveModel.CREATE_TABLE);
        database.execSQL(SaveGrowerDocumentModel.CREATE_TABLE);
        database.execSQL(MasterSubDropDown.CREATE_TABLE);
        database.execSQL(NotificationModel.CREATE_TABLE);
        database.execSQL(BroadCasteReportModel.CREATE_TABLE);
        database.execSQL(PurchiDetails.CREATE_TABLE);
        database.execSQL(VModal.CREATE_TABLE);
        database.execSQL(OfflineControlModel.CREATE_TABLE);
        database.execSQL(GpsDataModel.CREATE_TABLE);
        database.execSQL(LastGpsDataModel.CREATE_TABLE);
        //   database.close();

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + UserDetailsModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + ControlSurveyModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlotSurveyModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlotSurveyOldModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + VillageSurveyModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + FarmerModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + MasterCaneSurveyModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + FarmerShareModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + FarmerShareOldModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + MasterDropDown.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + VillageModal.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + GrowerModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + AddMaterialDataModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + MaterialMasterModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + ControlModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + AgriInputFormModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + IndentModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlantingModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlantingEquipmentModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlantingItemModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + SprayItemModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PlotActivitySaveModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + SaveGrowerDocumentModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + LocationDataModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + MasterSubDropDown.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + NotificationModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + BroadCasteReportModel.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + PurchiDetails.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + VModal.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + OfflineControlModel.TABLE_NAME);
        // Create tables again
        onCreate(database);
        //database.close();
    }

    public long insertUserDetailsModel(UserDetailsModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(UserDetailsModel.U_CODE, model.getCode());
        values.put(UserDetailsModel.U_NAME, model.getName());
        values.put(UserDetailsModel.U_PHONE, model.getPhone());
        values.put(UserDetailsModel.UT_CODE, model.getUserTypeCode());
        values.put(UserDetailsModel.UT_NAME, model.getUserTypeName());
        values.put(UserDetailsModel.DS_CODE, model.getDsCode());
        values.put(UserDetailsModel.D_NAME, model.getDsName());
        values.put(UserDetailsModel.DIVN, model.getDivision());
        values.put(UserDetailsModel.Col_gpsAccuracy, "" + model.getGpsAccuracy());
        values.put(UserDetailsModel.Gps_wait_time, "" + model.getGpsWaitTime());
        values.put(UserDetailsModel.NM, model.getCompantName());
        values.put(UserDetailsModel.ISACTIVATE, model.getIsActivate());
        values.put(UserDetailsModel.USER_LAVEL, model.getUserLavel());
        values.put(UserDetailsModel.ISUPDATEMASTER, model.getIsUpdateMaster());
        values.put(UserDetailsModel.LEAVEFLG, model.getLeaveFlag());
        values.put(UserDetailsModel.ZONECODE, model.getZoneCode());
        values.put(UserDetailsModel.ZONENAME, model.getZoneName());
        values.put(UserDetailsModel.TIMEFROM, model.getTimeFrom());
        values.put(UserDetailsModel.TIMETO, model.getTimeTo());
        values.put(UserDetailsModel.Col_overtime_status, model.getOvertimeStatus());
        // insert row
        long id = db.insert(UserDetailsModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<UserDetailsModel> getUserDetailsModel() {
        List<UserDetailsModel> allData = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + UserDetailsModel.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserDetailsModel singleData = new UserDetailsModel();
                singleData.setId(cursor.getString(cursor.getColumnIndex(UserDetailsModel.COLUMN_ID)));
                singleData.setCode(cursor.getString(cursor.getColumnIndex(UserDetailsModel.U_CODE)));
                singleData.setName(cursor.getString(cursor.getColumnIndex(UserDetailsModel.U_NAME)));
                singleData.setPhone(cursor.getString(cursor.getColumnIndex(UserDetailsModel.U_PHONE)));
                singleData.setUserTypeCode(cursor.getString(cursor.getColumnIndex(UserDetailsModel.UT_CODE)));
                singleData.setUserTypeName(cursor.getString(cursor.getColumnIndex(UserDetailsModel.UT_NAME)));
                singleData.setDsCode(cursor.getString(cursor.getColumnIndex(UserDetailsModel.DS_CODE)));
                singleData.setDsName(cursor.getString(cursor.getColumnIndex(UserDetailsModel.D_NAME)));
                singleData.setDivision(cursor.getString(cursor.getColumnIndex(UserDetailsModel.DIVN)));
                singleData.setGpsAccuracy(cursor.getDouble(cursor.getColumnIndex(UserDetailsModel.Col_gpsAccuracy)));
                singleData.setGpsWaitTime(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.Gps_wait_time)));
                singleData.setCompantName(cursor.getString(cursor.getColumnIndex(UserDetailsModel.NM)));
                singleData.setIsActivate(cursor.getString(cursor.getColumnIndex(UserDetailsModel.ISACTIVATE)));
                singleData.setUserLavel(cursor.getString(cursor.getColumnIndex(UserDetailsModel.USER_LAVEL)));
                singleData.setIsUpdateMaster(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.ISUPDATEMASTER)));
                singleData.setLeaveFlag(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.LEAVEFLG)));
                singleData.setZoneCode(cursor.getString(cursor.getColumnIndex(UserDetailsModel.ZONECODE)));
                singleData.setZoneName(cursor.getString(cursor.getColumnIndex(UserDetailsModel.ZONENAME)));
                singleData.setTimeFrom(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.TIMEFROM)));
                singleData.setTimeTo(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.TIMETO)));
                singleData.setOvertimeStatus(cursor.getInt(cursor.getColumnIndex(UserDetailsModel.Col_overtime_status)));
                allData.add(singleData);
            } while (cursor.moveToNext());
        }
        db.close();
        return allData;
    }

    public void updateMasterDownload(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + UserDetailsModel.TABLE_NAME + " SET " + UserDetailsModel.ISUPDATEMASTER + "='" + status + "'");
        db.close();
    }

    public void deleteUserDetailsModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + UserDetailsModel.TABLE_NAME);
        db.execSQL(UserDetailsModel.CREATE_TABLE);
        db.execSQL("VACUUM");
        db.close();
    }


    ////////////////////Mode Data ////////////////////
    public long insertVillageModel(VillageSurveyModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(VillageSurveyModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(VillageSurveyModel.Col_villageName, modeDataModel.getVillageName());
        values.put(VillageSurveyModel.Col_centerName, modeDataModel.getCenterName());
        values.put(VillageSurveyModel.Col_plotSrNo, modeDataModel.getPlotSrNo());
        values.put(VillageSurveyModel.Col_plotSrNo1, modeDataModel.getPlotSrNo1());
        values.put(VillageSurveyModel.Col_plotSrNo2, modeDataModel.getPlotSrNo2());
        values.put(VillageSurveyModel.Col_stopSurvey, modeDataModel.getStopSurvey());
        values.put(VillageSurveyModel.Col_transSurvey, modeDataModel.getTransSurvey());
        long id = db.insert(VillageSurveyModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteVillageModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + VillageSurveyModel.TABLE_NAME);
        db.close();
    }

    public void setDefaultVillage(String villageCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Update " + VillageSurveyModel.TABLE_NAME + " SET " + VillageSurveyModel.Col_is_default + "='0'");
        db.execSQL("Update " + VillageSurveyModel.TABLE_NAME + " SET " + VillageSurveyModel.Col_is_default + "='1' WHERE " + VillageSurveyModel.Col_villageCode + "='" + villageCode + "'");
        db.close();
    }

    public List<VillageSurveyModel> getDefaultSurveyVillage() {
        List<VillageSurveyModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME + " WHERE " + VillageSurveyModel.Col_is_default + "='1' AND " + VillageSurveyModel.Col_stopSurvey + "='0'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VillageSurveyModel modeDataModel = new VillageSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.COLUMN_ID)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageCode)));
                modeDataModel.setVillageName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageName)));
                modeDataModel.setCenterName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_centerName)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo)));
                modeDataModel.setPlotSrNo1(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo1)));
                modeDataModel.setPlotSrNo2(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo2)));
                modeDataModel.setStopSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_stopSurvey)));
                modeDataModel.setTransSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_transSurvey)));
                modeDataModel.setIsDefault(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_is_default)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public void updateOvertime(int overtimeMode) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + UserDetailsModel.TABLE_NAME + " SET " + UserDetailsModel.Col_overtime_status + "='" + overtimeMode + "'");
        db.close();
    }

    @SuppressLint("Range")
    public int getTotalRecord() {
        int total = 0;
        String selectQuery = "SELECT  count(*) as cout FROM " + GpsDataModel.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                total = cursor.getInt(cursor.getColumnIndex("cout"));
            } while (cursor.moveToNext());
        }
        db.close();
        return total;
    }

    public List<VillageSurveyModel> getVillageModel(String id) {
        List<VillageSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME + " WHERE " + VillageSurveyModel.Col_villageCode + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VillageSurveyModel modeDataModel = new VillageSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.COLUMN_ID)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageCode)));
                modeDataModel.setVillageName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageName)));
                modeDataModel.setCenterName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_centerName)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo)));
                modeDataModel.setPlotSrNo1(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo1)));
                modeDataModel.setPlotSrNo2(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo2)));
                modeDataModel.setStopSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_stopSurvey)));
                modeDataModel.setTransSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_transSurvey)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<VillageSurveyModel> getVillageModelFiltered(String villageCode, String stopSurvey) {
        List<VillageSurveyModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + VillageSurveyModel.Col_villageCode + "='" + villageCode + "'";
        }
        if (stopSurvey.length() > 0) {
            selectQuery += " AND " + VillageSurveyModel.Col_stopSurvey + "='" + stopSurvey + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VillageSurveyModel modeDataModel = new VillageSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.COLUMN_ID)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageCode)));
                modeDataModel.setVillageName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageName)));
                modeDataModel.setCenterName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_centerName)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo)));
                modeDataModel.setPlotSrNo1(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo1)));
                modeDataModel.setPlotSrNo2(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo2)));
                modeDataModel.setStopSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_stopSurvey)));
                modeDataModel.setTransSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_transSurvey)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////MasterModel Data ////////////////////
    public List<VillageSurveyModel> getSurveyVillageModel(String id) {
        List<VillageSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME + " WHERE " + VillageSurveyModel.Col_stopSurvey + "='0'";
        } else {
            selectQuery = "SELECT  * FROM " + VillageSurveyModel.TABLE_NAME + " WHERE " + VillageSurveyModel.Col_villageCode + "='" + id + "' AND " + VillageSurveyModel.Col_stopSurvey + "='0'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VillageSurveyModel modeDataModel = new VillageSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.COLUMN_ID)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageCode)));
                modeDataModel.setVillageName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_villageName)));
                modeDataModel.setCenterName(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_centerName)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo)));
                modeDataModel.setPlotSrNo1(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo1)));
                modeDataModel.setPlotSrNo2(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_plotSrNo2)));
                modeDataModel.setStopSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_stopSurvey)));
                modeDataModel.setTransSurvey(cursor.getString(cursor.getColumnIndex(VillageSurveyModel.Col_transSurvey)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////MasterModel Data ////////////////////
    public long insertMasterModel(MasterCaneSurveyModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MasterCaneSurveyModel.Col_mstCode, modeDataModel.getMstCode());
        values.put(MasterCaneSurveyModel.Col_code, modeDataModel.getCode());
        values.put(MasterCaneSurveyModel.Col_name, modeDataModel.getName());
        long id = db.insert(MasterCaneSurveyModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteMasterModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + MasterCaneSurveyModel.TABLE_NAME);
        db.close();
    }

    public List<MasterCaneSurveyModel> getMasterModel(String id) {
        List<MasterCaneSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + MasterCaneSurveyModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + MasterCaneSurveyModel.TABLE_NAME + " WHERE " + MasterCaneSurveyModel.Col_mstCode + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterCaneSurveyModel modeDataModel = new MasterCaneSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.COLUMN_ID)));
                modeDataModel.setMstCode(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_mstCode)));
                modeDataModel.setCode(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_code)));
                modeDataModel.setName(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_name)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public long insertNotificationModel(NotificationModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(NotificationModel.Col_title, model.getTitle());
        values.put(NotificationModel.Col_message, model.getMessage());
        values.put(NotificationModel.Col_date_t, model.getDateTime());
        values.put(NotificationModel.Col_seen, model.getSeen());
        values.put(NotificationModel.Col_senduser, model.getSenduser());
        // insert row
        long id = db.insert(NotificationModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteOldPlotSurveyModelByVillage(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM farmer_share_old WHERE survey_id IN (SELECT plot_survey_id FROM plot_survey_old WHERE villageCode=" + id + ")");
        db.execSQL("DELETE FROM plot_survey_old WHERE villageCode='" + id + "'");
        db.close();
    }

    public List<PlotFarmerShareModel> getPlotActivityNearByLatLng(double lats, double lngs) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM farmer_share JOIN plot_survey ON farmer_share.survey_id=plot_survey.plot_survey_id WHERE farmer_share.sr_no=1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                modeDataModel.setPlotFrom("Current Year Survey");
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                Location source = new Location(LocationManager.GPS_PROVIDER);
                source.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                source.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));
                Location destination = new Location(LocationManager.GPS_PROVIDER);
                destination.setLatitude(lats);
                destination.setLongitude(lngs);
                double distance = source.distanceTo(destination);
                if (distance < APIUrl.commonDistance) {
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT  * FROM " + PlantingModel.TABLE_NAME + " ORDER BY " + PlantingModel.COLUMN_ID + " ASC";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel sprayItemModel = new PlotFarmerShareModel();
                    sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                    sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                    sprayItemModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                    sprayItemModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                    sprayItemModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                    sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                    sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                    sprayItemModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                    sprayItemModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                    sprayItemModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                    sprayItemModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                    sprayItemModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                    sprayItemModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                    sprayItemModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                    sprayItemModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                    sprayItemModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                    sprayItemModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                    sprayItemModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                    sprayItemModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                    sprayItemModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                    sprayItemModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                    //sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID )));
                    sprayItemModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                    sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                    sprayItemModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                    sprayItemModel.setPlotFrom("Planting");
                    Location source = new Location(LocationManager.GPS_PROVIDER);
                    source.setLatitude(Double.parseDouble(sprayItemModel.getEastNorthLat()));
                    source.setLongitude(Double.parseDouble(sprayItemModel.getEastNorthLng()));
                    Location destination = new Location(LocationManager.GPS_PROVIDER);
                    destination.setLatitude(lats);
                    destination.setLongitude(lngs);
                    double distance = source.distanceTo(destination);
                    if (distance < APIUrl.commonDistance) {
                        list.add(sprayItemModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT * FROM farmer_share_old JOIN plot_survey_old ON farmer_share_old.survey_id=plot_survey_old.plot_survey_id WHERE farmer_share_old.sr_no=1 and plot_survey_old.cane_type in (0,1,2) ";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    modeDataModel.setPlotFrom("Last Year Survey");
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    Location source = new Location(LocationManager.GPS_PROVIDER);
                    source.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                    source.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));
                    Location destination = new Location(LocationManager.GPS_PROVIDER);
                    destination.setLatitude(lats);
                    destination.setLongitude(lngs);
                    double distance = source.distanceTo(destination);
                    if (distance < APIUrl.commonDistance) {
                        list.add(modeDataModel);
                    }
                } while (cursor.moveToNext());
            }
        }


        db.close();
        return list;
    }

    public List<PlotFarmerShareModel> getPlotActivityModelByLatLngExats(double lats, double lngs, String villCode) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM farmer_share JOIN plot_survey ON farmer_share.survey_id=plot_survey.plot_survey_id WHERE farmer_share.sr_no=1 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                modeDataModel.setPlotFrom("Current Year Survey");
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                List<LatLng> latLngList = new ArrayList<>();
                if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                }
                if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                }
                LatLng latlng = new LatLng(lats, lngs);
                Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                if (inside) {
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT  * FROM " + PlantingModel.TABLE_NAME + " ORDER BY " + PlantingModel.COLUMN_ID + " ASC";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel sprayItemModel = new PlotFarmerShareModel();
                    sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                    sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                    sprayItemModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                    sprayItemModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                    sprayItemModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                    sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                    sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                    sprayItemModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                    sprayItemModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                    sprayItemModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                    sprayItemModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                    sprayItemModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                    sprayItemModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                    sprayItemModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                    sprayItemModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                    sprayItemModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                    sprayItemModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                    sprayItemModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                    sprayItemModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                    sprayItemModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                    sprayItemModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                    //sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID )));
                    sprayItemModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                    sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                    sprayItemModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                    sprayItemModel.setPlotFrom("Planting");
                    List<LatLng> latLngList = new ArrayList<>();
                    if (Double.parseDouble(sprayItemModel.getEastNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getEastNorthLat()), Double.parseDouble(sprayItemModel.getEastNorthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getEastSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getEastSouthLat()), Double.parseDouble(sprayItemModel.getEastSouthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getWestSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getWestSouthLat()), Double.parseDouble(sprayItemModel.getWestSouthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getWestNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getWestNorthLat()), Double.parseDouble(sprayItemModel.getWestNorthLng())));
                    }
                    LatLng latlng = new LatLng(lats, lngs);
                    Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                    if (inside) {
                        list.add(sprayItemModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT * FROM farmer_share_old JOIN plot_survey_old ON farmer_share_old.survey_id=plot_survey_old.plot_survey_id WHERE farmer_share_old.sr_no=1 and plot_survey_old.cane_type in (0,1,2) ";


            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    modeDataModel.setPlotFrom("Last Year Survey");
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    List<LatLng> latLngList = new ArrayList<>();
                    if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                    }
                    LatLng latlng = new LatLng(lats, lngs);
                    Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                    if (inside) {
                        list.add(modeDataModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return list;
    }

    public List<PlotFarmerShareModel> getPlotDetailsByTypeSrno(String plotSrNo, String type, String villCode) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM farmer_share JOIN plot_survey ON farmer_share.survey_id=plot_survey.plot_survey_id WHERE farmer_share.sr_no=1 AND plot_survey.plot_sr_number='" + plotSrNo + "' and plot_survey.villageCode='" + villCode + "'";
        if (type.equalsIgnoreCase("Last Year Survey")) {
            selectQuery = "SELECT * FROM farmer_share_old JOIN plot_survey_old ON farmer_share_old.survey_id=plot_survey_old.plot_survey_id WHERE farmer_share_old.sr_no=1 AND plot_survey_old.plot_sr_number='" + plotSrNo + "' and plot_survey_old.villageCode='" + villCode + "'";
        } else if (type.equalsIgnoreCase("Planting")) {
            selectQuery = "SELECT * FROM planting where PlotSerialNumber='" + plotSrNo + "' and village='" + villCode + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        if (type.equalsIgnoreCase("Planting")) {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel sprayItemModel = new PlotFarmerShareModel();
                    sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                    sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                    sprayItemModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                    sprayItemModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                    sprayItemModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                    sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                    sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                    sprayItemModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                    sprayItemModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                    sprayItemModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                    sprayItemModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                    sprayItemModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                    sprayItemModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                    sprayItemModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                    sprayItemModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                    sprayItemModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                    sprayItemModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                    sprayItemModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                    sprayItemModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                    sprayItemModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                    sprayItemModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                    //sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID )));
                    sprayItemModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                    sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                    sprayItemModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                    sprayItemModel.setPlotFrom("Planting");
                    list.add(sprayItemModel);
                } while (cursor.moveToNext());
            }
        } else {

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    modeDataModel.setPlotFrom(type);
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    list.add(modeDataModel);
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return list;
    }


    public void deletePlotSurveyModelByVillage(String id) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.Col_surveyId + " IN (SELECT " + PlotSurveyModel.COLUMN_ID + " FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "=" + id + ")";
        db.execSQL(query);
        db.execSQL("DELETE FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + id + "'");
        db.close();
    }

    public List<NotificationModel> getNotificationModel() {
        List<NotificationModel> loginUserDetailsses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + NotificationModel.TABLE_NAME + " ORDER BY " + NotificationModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                NotificationModel loginUserDetails = new NotificationModel();
                loginUserDetails.setColId(cursor.getString(cursor.getColumnIndex(NotificationModel.COLUMN_ID)));
                loginUserDetails.setTitle(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_title)));
                loginUserDetails.setMessage(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_message)));
                loginUserDetails.setDateTime(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_date_t)));
                loginUserDetails.setSeen(cursor.getInt(cursor.getColumnIndex(NotificationModel.Col_seen)));
                loginUserDetails.setSenduser(cursor.getInt(cursor.getColumnIndex(NotificationModel.Col_senduser)));
                loginUserDetailsses.add(loginUserDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return loginUserDetailsses;
    }

    public List<NotificationModel> getUnSeenNotificationModel() {
        List<NotificationModel> loginUserDetailsses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + NotificationModel.TABLE_NAME + " WHERE " + NotificationModel.Col_seen + "='No' ORDER BY " + NotificationModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                NotificationModel loginUserDetails = new NotificationModel();
                loginUserDetails.setColId(cursor.getString(cursor.getColumnIndex(NotificationModel.COLUMN_ID)));
                loginUserDetails.setTitle(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_title)));
                loginUserDetails.setMessage(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_message)));
                loginUserDetails.setDateTime(cursor.getString(cursor.getColumnIndex(NotificationModel.Col_date_t)));
                loginUserDetails.setSeen(cursor.getInt(cursor.getColumnIndex(NotificationModel.Col_seen)));
                loginUserDetails.setSenduser(cursor.getInt(cursor.getColumnIndex(NotificationModel.Col_senduser)));
                loginUserDetailsses.add(loginUserDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return loginUserDetailsses;
    }

    public void deleteNotificationModel(String id) {
        if (id.length() > 0) {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + NotificationModel.TABLE_NAME + " WHERE " + NotificationModel.COLUMN_ID + "='" + id + "'");
            db.close();
        } else {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + NotificationModel.TABLE_NAME);
            db.close();
        }
    }

    public void unSeenNotificationModel(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + NotificationModel.TABLE_NAME + " SET " + NotificationModel.Col_seen + "='Yes' WHERE " + NotificationModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }


    //////////////BroadCaste report///////

    public long insertBroadCasteReportModel(BroadCasteReportModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(BroadCasteReportModel.Col_title, model.getTitle());
        values.put(BroadCasteReportModel.Col_message, model.getMessage());
        values.put(BroadCasteReportModel.Col_date_t, model.getDateTime());
        values.put(BroadCasteReportModel.Col_totalView, model.getTotalView());
        values.put(BroadCasteReportModel.Col_totalSend, model.getTotalSend());
        values.put(BroadCasteReportModel.Col_totalSucess, model.getTotalSucess());
        values.put(BroadCasteReportModel.Col_totalFailure, model.getTotalFailure());
        // insert row
        long id = db.insert(BroadCasteReportModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<BroadCasteReportModel> getBroadCasteReportModel() {
        List<BroadCasteReportModel> loginUserDetailsses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BroadCasteReportModel.TABLE_NAME + " ORDER BY " + BroadCasteReportModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BroadCasteReportModel loginUserDetails = new BroadCasteReportModel();
                loginUserDetails.setColId(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.COLUMN_ID)));
                loginUserDetails.setTitle(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_title)));
                loginUserDetails.setMessage(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_message)));
                loginUserDetails.setDateTime(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_date_t)));
                loginUserDetails.setTotalView(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalView)));
                loginUserDetails.setTotalSend(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalSend)));
                loginUserDetails.setTotalSucess(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalSucess)));
                loginUserDetails.setTotalFailure(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalFailure)));
                loginUserDetailsses.add(loginUserDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return loginUserDetailsses;
    }

    public List<BroadCasteReportModel> getUnSeenBroadCasteReportModel() {
        List<BroadCasteReportModel> loginUserDetailsses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BroadCasteReportModel.TABLE_NAME + " WHERE " + BroadCasteReportModel.Col_totalView + "='No' ORDER BY " + BroadCasteReportModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BroadCasteReportModel loginUserDetails = new BroadCasteReportModel();
                loginUserDetails.setColId(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.COLUMN_ID)));
                loginUserDetails.setTitle(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_title)));
                loginUserDetails.setMessage(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_message)));
                loginUserDetails.setDateTime(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_date_t)));
                loginUserDetails.setTotalView(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalView)));
                loginUserDetails.setTotalSend(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalSend)));
                loginUserDetails.setTotalSucess(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalSucess)));
                loginUserDetails.setTotalFailure(cursor.getString(cursor.getColumnIndex(BroadCasteReportModel.Col_totalFailure)));
                loginUserDetailsses.add(loginUserDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return loginUserDetailsses;
    }

    public void deleteBroadCasteReportModel(String id) {
        if (id.length() > 0) {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + BroadCasteReportModel.TABLE_NAME + " WHERE " + BroadCasteReportModel.COLUMN_ID + "='" + id + "'");
            db.close();
        } else {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + BroadCasteReportModel.TABLE_NAME);
            db.close();
        }
    }

   /* public void unSeenBroadCasteReportModel(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + BroadCasteReportModel.TABLE_NAME + " SET " + BroadCasteReportModel.Col_seen + "='Yes' WHERE " + BroadCasteReportModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }*/

    //////////-----------BroadCasteReport End--------/////////////

    public List<MasterCaneSurveyModel> getMasterModelById(String masterId, String id) {
        List<MasterCaneSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + MasterCaneSurveyModel.TABLE_NAME + " WHERE " + MasterCaneSurveyModel.Col_mstCode + "='" + masterId + "'";
        } else {
            selectQuery = "SELECT  * FROM " + MasterCaneSurveyModel.TABLE_NAME + " WHERE " + MasterCaneSurveyModel.Col_mstCode + "='" + masterId + "' AND " + MasterCaneSurveyModel.Col_code + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterCaneSurveyModel modeDataModel = new MasterCaneSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.COLUMN_ID)));
                modeDataModel.setMstCode(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_mstCode)));
                modeDataModel.setCode(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_code)));
                modeDataModel.setName(cursor.getString(cursor.getColumnIndex(MasterCaneSurveyModel.Col_name)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////FarmerModel Data ////////////////////
    public long insertFarmerModel(FarmerModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(FarmerModel.Col_farmerCode, modeDataModel.getFarmerCode());
        values.put(FarmerModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(FarmerModel.Col_farmerName, modeDataModel.getFarmerName());
        values.put(FarmerModel.Col_fatherName, modeDataModel.getFatherName());
        values.put(FarmerModel.Col_uniqueCode, modeDataModel.getUniqueCode());
        values.put(FarmerModel.Col_des, modeDataModel.getDes());
        values.put(FarmerModel.Col_mobile, modeDataModel.getMobile());
        values.put(FarmerModel.Col_cultArea, modeDataModel.getCultArea());
        long id = db.insert(FarmerModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteFarmerModel(String VillageCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + FarmerModel.TABLE_NAME + " WHERE " + FarmerModel.Col_villageCode + "='" + VillageCode + "'");
        db.close();
    }

    public List<FarmerModel> getFarmerModel(String id) {
        List<FarmerModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + FarmerModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + FarmerModel.TABLE_NAME + " WHERE " + FarmerModel.Col_farmerCode + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerModel modeDataModel = new FarmerModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerModel.COLUMN_ID)));
                modeDataModel.setFarmerCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerCode)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_villageCode)));
                modeDataModel.setFarmerName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerName)));
                modeDataModel.setFatherName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_fatherName)));
                modeDataModel.setUniqueCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_uniqueCode)));
                modeDataModel.setDes(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_des)));
                modeDataModel.setMobile(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_mobile)));
                modeDataModel.setCultArea(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_cultArea)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerModel> getFarmerWithVillageModel(String village, String id) {
        List<FarmerModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + FarmerModel.TABLE_NAME + " WHERE " + FarmerModel.Col_villageCode + "='" + village + "'";
        } else {
            selectQuery = "SELECT  * FROM " + FarmerModel.TABLE_NAME + " WHERE " + FarmerModel.Col_villageCode + "='" + village + "' AND " + FarmerModel.Col_farmerCode + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerModel modeDataModel = new FarmerModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerModel.COLUMN_ID)));
                modeDataModel.setFarmerCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerCode)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_villageCode)));
                modeDataModel.setFarmerName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerName)));
                modeDataModel.setFatherName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_fatherName)));
                modeDataModel.setUniqueCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_uniqueCode)));
                modeDataModel.setDes(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_des)));
                modeDataModel.setMobile(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_mobile)));
                modeDataModel.setCultArea(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_cultArea)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<ReportCaneTypeSummeryModel> getReportCaneTypeSummery(String villageCode, String from, String to) throws Exception {
        List<ReportCaneTypeSummeryModel> list = new ArrayList<>();
        String selectQuery = "SELECT COUNT(" + PlotSurveyModel.Col_caneType + ") as totalPlot , SUM(" + PlotSurveyModel.Col_areaHectare + ") as area," + PlotSurveyModel.Col_caneType +
                "," + PlotSurveyModel.Col_villageCode + "  FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + villageCode + "' AND " +
                PlotSurveyModel.Col_insertDate + " BETWEEN '" + from + "' AND '" + to + "'" +
                " GROUP BY " + PlotSurveyModel.Col_caneType;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                List<VillageSurveyModel> villageModelList = getSurveyVillageModel(villageCode);
                List<MasterCaneSurveyModel> masterCaneTypeModel = getMasterModelById("2", cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                ReportCaneTypeSummeryModel modeDataModel = new ReportCaneTypeSummeryModel();
                modeDataModel.setTotalPlot(cursor.getString(cursor.getColumnIndex("totalPlot")));
                modeDataModel.setArea(cursor.getString(cursor.getColumnIndex("area")));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                modeDataModel.setVillageName(villageModelList.get(0).getVillageName());
                modeDataModel.setFromDate(from);
                modeDataModel.setToDate(to);
                modeDataModel.setVarietyName(masterCaneTypeModel.get(0).getName());
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<ReportCaneTypeSummeryModel> getReportVarietySummery(String villageCode, String from, String to) {
        List<ReportCaneTypeSummeryModel> list = new ArrayList<>();
        try {
            String selectQuery = "SELECT COUNT(" + PlotSurveyModel.Col_varietyCode + ") as totalPlot , SUM(" + PlotSurveyModel.Col_areaHectare + ") as area," + PlotSurveyModel.Col_varietyCode +
                    "," + PlotSurveyModel.Col_villageCode + "  FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + villageCode + "' AND " +
                    PlotSurveyModel.Col_insertDate + " BETWEEN '" + from + "' AND '" + to + "'" +
                    " GROUP BY " + PlotSurveyModel.Col_varietyCode;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    List<VillageSurveyModel> villageModelList = getSurveyVillageModel(villageCode);
                    List<MasterCaneSurveyModel> masterCaneTypeModel = getMasterModelById("1", cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                    ReportCaneTypeSummeryModel modeDataModel = new ReportCaneTypeSummeryModel();
                    modeDataModel.setTotalPlot(cursor.getString(cursor.getColumnIndex("totalPlot")));
                    modeDataModel.setArea(cursor.getString(cursor.getColumnIndex("area")));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                    modeDataModel.setVillageName(villageModelList.get(0).getVillageName());
                    modeDataModel.setFromDate(from);
                    modeDataModel.setToDate(to);
                    modeDataModel.setVarietyName(masterCaneTypeModel.get(0).getName());
                    list.add(modeDataModel);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            Log.d("", e.toString());
        }
        return list;
    }

    public List<ReportGrowerPlotSummeryModel> getReportGrowerPlotSummeryModel(String villageCode, String grower) {
        List<ReportGrowerPlotSummeryModel> list = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + PlotSurveyModel.TABLE_NAME + ".*," + FarmerShareModel.TABLE_NAME + ".* FROM " +
                    FarmerShareModel.TABLE_NAME + " JOIN " + PlotSurveyModel.TABLE_NAME + " ON " + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID +
                    "=" + FarmerShareModel.TABLE_NAME + "." + FarmerShareModel.Col_surveyId + " " +
                    "WHERE " + FarmerShareModel.TABLE_NAME + "." + FarmerShareModel.Col_villageCode + "='" + villageCode + "' AND " +
                    FarmerShareModel.TABLE_NAME + "." + FarmerShareModel.Col_growerCode + "='" + grower + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    List<VillageSurveyModel> villageModelList = getVillageModel(villageCode);
                    List<MasterCaneSurveyModel> masterCaneTypeModel = getMasterModelById("2", cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                    List<MasterCaneSurveyModel> masterVarietyTypeModel = getMasterModelById("1", cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                    ReportGrowerPlotSummeryModel modeDataModel = new ReportGrowerPlotSummeryModel();
                    modeDataModel.setArea(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                    modeDataModel.setVarietyName(masterVarietyTypeModel.get(0).getName());
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                    modeDataModel.setVillageName(villageModelList.get(0).getVillageName());
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFather(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_PlotSrNo)));
                    modeDataModel.setCaneTypeCode(masterCaneTypeModel.get(0).getCode());
                    modeDataModel.setCaneTypeName(masterCaneTypeModel.get(0).getName());
                    list.add(modeDataModel);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            Log.d("", e.toString());
        }
        return list;
    }

    public List<PlotSurveyModel> getPlotSurveyModelAllVillageCheckList(String villageCode, String fromDate, String toDate) {
        List<PlotSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        selectQuery = "SELECT  * FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + villageCode + "' AND " +
                PlotSurveyModel.Col_insertDate + " BETWEEN '" + fromDate + "' AND '" + toDate + "'" + " ORDER BY " + PlotSurveyModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotSurveyModel modeDataModel = new PlotSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_gataNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////ControlSurveyModel Data ////////////////////
    public long insertControlSurveyModel(ControlSurveyModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ControlSurveyModel.Col_khashraNo, modeDataModel.getKhashraNo());
        values.put(ControlSurveyModel.Col_gataNo, modeDataModel.getGataNo());
        values.put(ControlSurveyModel.Col_is4G, modeDataModel.getIs4G());
        values.put(ControlSurveyModel.Col_isARM9, modeDataModel.getIsARM9());
        values.put(ControlSurveyModel.Col_MixCrop, modeDataModel.getMixCrop());
        values.put(ControlSurveyModel.Col_Insect, modeDataModel.getInsect());
        values.put(ControlSurveyModel.Col_SeedSource, modeDataModel.getSeedSource());
        values.put(ControlSurveyModel.Col_AadharNo, modeDataModel.getAadharNo());
        values.put(ControlSurveyModel.Col_plant, modeDataModel.getPlant());
        values.put(ControlSurveyModel.Col_ratoon1, modeDataModel.getRatoon1());
        values.put(ControlSurveyModel.Col_ratoon2, modeDataModel.getRatoon2());
        values.put(ControlSurveyModel.Col_autumn, modeDataModel.getAutumn());
        values.put(ControlSurveyModel.Col_ishunPerShare, modeDataModel.getIshunPerShare());
        values.put(ControlSurveyModel.Col_FortNo, modeDataModel.getFortNo());
        values.put(ControlSurveyModel.Col_Aadhar01, modeDataModel.getAadhar01());
        values.put(ControlSurveyModel.Col_Extra, modeDataModel.getExtra());
        values.put(ControlSurveyModel.Col_siteCode, modeDataModel.getSiteCode());
        values.put(ControlSurveyModel.Col_startHr, modeDataModel.getStartHr());
        values.put(ControlSurveyModel.Col_stopHr, modeDataModel.getStopHr());
        values.put(ControlSurveyModel.Col_isOnline, modeDataModel.getIsOnline());
        values.put(ControlSurveyModel.Col_ftpAddr, modeDataModel.getFtpAddr());
        values.put(ControlSurveyModel.Col_ftpUser, modeDataModel.getFtpUser());
        values.put(ControlSurveyModel.Col_ftpPass, modeDataModel.getFtpPass());
        values.put(ControlSurveyModel.Col_staticIp, modeDataModel.getStaticIp());
        values.put(ControlSurveyModel.Col_staticPort, modeDataModel.getStaticPort());
        values.put(ControlSurveyModel.Col_hideArea, modeDataModel.getHideArea());
        values.put(ControlSurveyModel.Col_isGpsPts, modeDataModel.getIsGpsPts());
        values.put(ControlSurveyModel.Col_shareInPer, modeDataModel.getShareInPer());
        values.put(ControlSurveyModel.Col_minLength, modeDataModel.getMinLength());
        values.put(ControlSurveyModel.Col_maxLength, modeDataModel.getMaxLength());
        values.put(ControlSurveyModel.Col_sideDiffPer, modeDataModel.getSideDiffPer());
        values.put(ControlSurveyModel.Col_mobOPrCd, modeDataModel.getMobOPrCd());
        values.put(ControlSurveyModel.Col_PlantMethod, modeDataModel.getPlantMethod());
        values.put(ControlSurveyModel.Col_CropCond, modeDataModel.getCropCond());
        values.put(ControlSurveyModel.Col_Disease, modeDataModel.getDisease());
        values.put(ControlSurveyModel.Col_SocClerk, modeDataModel.getSocClerk());
        values.put(ControlSurveyModel.Col_PlantDate, modeDataModel.getPlantDate());
        values.put(ControlSurveyModel.Col_SoilType, modeDataModel.getSoilType());
        values.put(ControlSurveyModel.Col_Irrigation, modeDataModel.getIrrigation());
        values.put(ControlSurveyModel.Col_LandType, modeDataModel.getLandType());
        values.put(ControlSurveyModel.Col_BorderCrop, modeDataModel.getBorderCrop());
        values.put(ControlSurveyModel.Col_PlotType, modeDataModel.getPlotType());
        values.put(ControlSurveyModel.Col_GhashtiNo, modeDataModel.getGhashtiNo());
        values.put(ControlSurveyModel.Col_SmsRecMobNo, modeDataModel.getSmsRecMobNo());
        values.put(ControlSurveyModel.Col_ftpHr1, modeDataModel.getFtpHr1());
        values.put(ControlSurveyModel.Col_ftpHr2, modeDataModel.getFtpHr2());
        values.put(ControlSurveyModel.Col_ftpHr3, modeDataModel.getFtpHr3());
        values.put(ControlSurveyModel.Col_ftpHr4, modeDataModel.getFtpHr4());
        values.put(ControlSurveyModel.Col_factoryId, modeDataModel.getFactoryId());
        values.put(ControlSurveyModel.Col_InterCrop, modeDataModel.getInterCrop());
        values.put(ControlSurveyModel.Col_mcCode, modeDataModel.getMcCode());
        values.put(ControlSurveyModel.Col_isXml, modeDataModel.getIsXml());
        values.put(ControlSurveyModel.Col_Insect1, modeDataModel.getInsect1());
        values.put(ControlSurveyModel.Col_InterCrop1, modeDataModel.getInterCrop1());
        values.put(ControlSurveyModel.Col_MixCrop1, modeDataModel.getMixCrop1());
        values.put(ControlSurveyModel.Col_SeedSource1, modeDataModel.getSeedSource1());
        long id = db.insert(ControlSurveyModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteControlSurveyModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + ControlSurveyModel.TABLE_NAME);
        db.close();
    }

    public List<ControlSurveyModel> getControlSurveyModel(String id) {
        List<ControlSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + ControlSurveyModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + ControlSurveyModel.TABLE_NAME + " WHERE " + ControlSurveyModel.Col_factoryId + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ControlSurveyModel modeDataModel = new ControlSurveyModel();
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_gataNo)));
                modeDataModel.setIs4G(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_is4G)));
                modeDataModel.setIsARM9(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_isARM9)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_MixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_SeedSource)));
                modeDataModel.setAadharNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_AadharNo)));
                modeDataModel.setPlant(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_plant)));
                modeDataModel.setRatoon1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ratoon1)));
                modeDataModel.setRatoon2(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ratoon2)));
                modeDataModel.setAutumn(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_autumn)));
                modeDataModel.setIshunPerShare(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ishunPerShare)));
                modeDataModel.setFortNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_FortNo)));
                modeDataModel.setAadhar01(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Aadhar01)));
                modeDataModel.setExtra(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Extra)));
                modeDataModel.setSiteCode(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_siteCode)));
                modeDataModel.setStartHr(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_startHr)));
                modeDataModel.setStopHr(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_stopHr)));
                modeDataModel.setIsOnline(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_isOnline)));
                modeDataModel.setFtpAddr(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpAddr)));
                modeDataModel.setFtpUser(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpUser)));
                modeDataModel.setFtpPass(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpPass)));
                modeDataModel.setStaticIp(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_staticIp)));
                modeDataModel.setStaticPort(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_staticPort)));
                modeDataModel.setHideArea(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_hideArea)));
                modeDataModel.setIsGpsPts(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_isGpsPts)));
                modeDataModel.setShareInPer(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_shareInPer)));
                modeDataModel.setMinLength(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_minLength)));
                modeDataModel.setMaxLength(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_maxLength)));
                modeDataModel.setSideDiffPer(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_sideDiffPer)));
                modeDataModel.setMobOPrCd(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_mobOPrCd)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_PlantMethod)));
                modeDataModel.setCropCond(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_CropCond)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Disease)));
                modeDataModel.setSocClerk(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_SocClerk)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_PlantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_SoilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_LandType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_BorderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_PlotType)));
                modeDataModel.setGhashtiNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_GhashtiNo)));
                modeDataModel.setSmsRecMobNo(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_SmsRecMobNo)));
                modeDataModel.setFtpHr1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpHr1)));
                modeDataModel.setFtpHr2(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpHr2)));
                modeDataModel.setFtpHr3(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpHr3)));
                modeDataModel.setFtpHr4(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_ftpHr4)));
                modeDataModel.setFactoryId(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_factoryId)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_InterCrop)));
                modeDataModel.setMcCode(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_mcCode)));
                modeDataModel.setIsXml(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_isXml)));
                modeDataModel.setInsect1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_Insect1)));
                modeDataModel.setInterCrop1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_InterCrop1)));
                modeDataModel.setMixCrop1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_MixCrop1)));
                modeDataModel.setSeedSource1(cursor.getString(cursor.getColumnIndex(ControlSurveyModel.Col_SeedSource1)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////FarmerModel Data ////////////////////
    public long insertPlotSurveyModel(PlotSurveyModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PlotSurveyModel.Col_PlotSrNo, modeDataModel.getPlotSrNo());
        values.put(PlotSurveyModel.Col_khashraNo, modeDataModel.getKhashraNo());
        values.put(PlotSurveyModel.Col_gataNo, modeDataModel.getGataNo());
        values.put(PlotSurveyModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(PlotSurveyModel.Col_areaMeter, modeDataModel.getAreaMeter());
        values.put(PlotSurveyModel.Col_areaHectare, modeDataModel.getAreaHectare());
        values.put(PlotSurveyModel.Col_mixCrop, modeDataModel.getMixCrop());
        values.put(PlotSurveyModel.Col_insect, modeDataModel.getInsect());
        values.put(PlotSurveyModel.Col_seedSource, modeDataModel.getSeedSource());
        values.put(PlotSurveyModel.Col_aadharNumber, modeDataModel.getAadharNumber());
        values.put(PlotSurveyModel.Col_plantMethod, modeDataModel.getPlantMethod());
        values.put(PlotSurveyModel.Col_cropCondition, modeDataModel.getCropCondition());
        values.put(PlotSurveyModel.Col_disease, modeDataModel.getDisease());
        values.put(PlotSurveyModel.Col_plantDate, modeDataModel.getPlantDate());
        values.put(PlotSurveyModel.Col_irrigation, modeDataModel.getIrrigation());
        values.put(PlotSurveyModel.Col_soilType, modeDataModel.getSoilType());
        values.put(PlotSurveyModel.Col_landType, modeDataModel.getLandType());
        values.put(PlotSurveyModel.Col_borderCrop, modeDataModel.getBorderCrop());
        values.put(PlotSurveyModel.Col_plotType, modeDataModel.getPlotType());
        values.put(PlotSurveyModel.Col_ghashtiNumber, modeDataModel.getGhashtiNumber());
        values.put(PlotSurveyModel.Col_interCrop, modeDataModel.getInterCrop());
        values.put(PlotSurveyModel.Col_eastNorth_lat, modeDataModel.getEastNorthLat());
        values.put(PlotSurveyModel.Col_eastNorth_lng, modeDataModel.getEastNorthLng());
        values.put(PlotSurveyModel.Col_eastNorth_distance, modeDataModel.getEastNorthDistance());
        values.put(PlotSurveyModel.Col_eastNorth_accuracy, modeDataModel.getEastNorthAccuracy());
        values.put(PlotSurveyModel.Col_westNorth_lat, modeDataModel.getWestNorthLat());
        values.put(PlotSurveyModel.Col_westNorth_lng, modeDataModel.getWestNorthLng());
        values.put(PlotSurveyModel.Col_westNorth_distance, modeDataModel.getWestNorthDistance());
        values.put(PlotSurveyModel.Col_westNorth_accuracy, modeDataModel.getWestNorthAccuracy());
        values.put(PlotSurveyModel.Col_eastSouth_lat, modeDataModel.getEastSouthLat());
        values.put(PlotSurveyModel.Col_eastSouth_lng, modeDataModel.getEastSouthLng());
        values.put(PlotSurveyModel.Col_eastSouth_distance, modeDataModel.getEastSouthDistance());
        values.put(PlotSurveyModel.Col_eastSouth_accuracy, modeDataModel.getEastSouthAccuracy());
        values.put(PlotSurveyModel.Col_westSouth_lat, modeDataModel.getWestSouthLat());
        values.put(PlotSurveyModel.Col_westSouth_lng, modeDataModel.getWestSouthLng());
        values.put(PlotSurveyModel.Col_westSouth_distance, modeDataModel.getWestSouthDistance());
        values.put(PlotSurveyModel.Col_westSouth_accuracy, modeDataModel.getWestSouthAccuracy());
        values.put(PlotSurveyModel.Col_varietyCode, modeDataModel.getVarietyCode());
        values.put(PlotSurveyModel.Col_caneType, modeDataModel.getCaneType());
        values.put(PlotSurveyModel.Col_insertDate, modeDataModel.getInsertDate());
        values.put(PlotSurveyModel.Col_old_plot_id, modeDataModel.getOldPlotId());
        values.put(PlotSurveyModel.Col_old_plot_from, modeDataModel.getPlotFrom());
        long id = db.insert(PlotSurveyModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public long insertPlotSurveyOldModel(PlotSurveyOldModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PlotSurveyOldModel.Col_PlotSrNo, modeDataModel.getPlotSrNo());
        values.put(PlotSurveyOldModel.Col_khashraNo, modeDataModel.getKhashraNo());
        values.put(PlotSurveyOldModel.Col_gataNo, modeDataModel.getGataNo());
        values.put(PlotSurveyOldModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(PlotSurveyOldModel.Col_areaMeter, modeDataModel.getAreaMeter());
        values.put(PlotSurveyOldModel.Col_areaHectare, modeDataModel.getAreaHectare());
        values.put(PlotSurveyOldModel.Col_mixCrop, modeDataModel.getMixCrop());
        values.put(PlotSurveyOldModel.Col_insect, modeDataModel.getInsect());
        values.put(PlotSurveyOldModel.Col_seedSource, modeDataModel.getSeedSource());
        values.put(PlotSurveyOldModel.Col_aadharNumber, modeDataModel.getAadharNumber());
        values.put(PlotSurveyOldModel.Col_plantMethod, modeDataModel.getPlantMethod());
        values.put(PlotSurveyOldModel.Col_cropCondition, modeDataModel.getCropCondition());
        values.put(PlotSurveyOldModel.Col_disease, modeDataModel.getDisease());
        values.put(PlotSurveyOldModel.Col_plantDate, modeDataModel.getPlantDate());
        values.put(PlotSurveyOldModel.Col_irrigation, modeDataModel.getIrrigation());
        values.put(PlotSurveyOldModel.Col_soilType, modeDataModel.getSoilType());
        values.put(PlotSurveyOldModel.Col_landType, modeDataModel.getLandType());
        values.put(PlotSurveyOldModel.Col_borderCrop, modeDataModel.getBorderCrop());
        values.put(PlotSurveyOldModel.Col_plotType, modeDataModel.getPlotType());
        values.put(PlotSurveyOldModel.Col_ghashtiNumber, modeDataModel.getGhashtiNumber());
        values.put(PlotSurveyOldModel.Col_interCrop, modeDataModel.getInterCrop());
        values.put(PlotSurveyOldModel.Col_eastNorth_lat, modeDataModel.getEastNorthLat());
        values.put(PlotSurveyOldModel.Col_eastNorth_lng, modeDataModel.getEastNorthLng());
        values.put(PlotSurveyOldModel.Col_eastNorth_distance, modeDataModel.getEastNorthDistance());
        values.put(PlotSurveyOldModel.Col_eastNorth_accuracy, modeDataModel.getEastNorthAccuracy());
        values.put(PlotSurveyOldModel.Col_westNorth_lat, modeDataModel.getWestNorthLat());
        values.put(PlotSurveyOldModel.Col_westNorth_lng, modeDataModel.getWestNorthLng());
        values.put(PlotSurveyOldModel.Col_westNorth_distance, modeDataModel.getWestNorthDistance());
        values.put(PlotSurveyOldModel.Col_westNorth_accuracy, modeDataModel.getWestNorthAccuracy());
        values.put(PlotSurveyOldModel.Col_eastSouth_lat, modeDataModel.getEastSouthLat());
        values.put(PlotSurveyOldModel.Col_eastSouth_lng, modeDataModel.getEastSouthLng());
        values.put(PlotSurveyOldModel.Col_eastSouth_distance, modeDataModel.getEastSouthDistance());
        values.put(PlotSurveyOldModel.Col_eastSouth_accuracy, modeDataModel.getEastSouthAccuracy());
        values.put(PlotSurveyOldModel.Col_westSouth_lat, modeDataModel.getWestSouthLat());
        values.put(PlotSurveyOldModel.Col_westSouth_lng, modeDataModel.getWestSouthLng());
        values.put(PlotSurveyOldModel.Col_westSouth_distance, modeDataModel.getWestSouthDistance());
        values.put(PlotSurveyOldModel.Col_westSouth_accuracy, modeDataModel.getWestSouthAccuracy());
        values.put(PlotSurveyOldModel.Col_varietyCode, modeDataModel.getVarietyCode());
        values.put(PlotSurveyOldModel.Col_caneType, modeDataModel.getCaneType());
        values.put(PlotSurveyOldModel.Col_insertDate, modeDataModel.getInsertDate());
        long id = db.insert(PlotSurveyOldModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<PlotSurveyOldModel> getPlotSurveyOldModelId(String villageCode, String PlotSrNo) {
        List<PlotSurveyOldModel> list = new ArrayList<>();
        String selectQuery = "";
        selectQuery = "SELECT  * FROM " + PlotSurveyOldModel.TABLE_NAME + " WHERE " + PlotSurveyOldModel.Col_villageCode + "='" + villageCode + "' AND " + PlotSurveyOldModel.Col_PlotSrNo + "='" + PlotSrNo + "' " + " ORDER BY " + PlotSurveyOldModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotSurveyOldModel modeDataModel = new PlotSurveyOldModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.COLUMN_ID)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_gataNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_villageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotSurveyOldModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public long importFarmerShareOldModel(FarmerShareOldModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(FarmerShareOldModel.COLUMN_ID, modeDataModel.getColId());
        values.put(FarmerShareOldModel.Col_surveyId, modeDataModel.getSurveyId());
        values.put(FarmerShareOldModel.Col_srNo, modeDataModel.getSrNo());
        values.put(FarmerShareOldModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(FarmerShareOldModel.Col_growerCode, modeDataModel.getGrowerCode());
        values.put(FarmerShareOldModel.Col_growerName, modeDataModel.getGrowerName());
        values.put(FarmerShareOldModel.Col_growerFatherName, modeDataModel.getGrowerFatherName());
        values.put(FarmerShareOldModel.Col_growerAadhar_number, modeDataModel.getGrowerAadharNumber());
        values.put(FarmerShareOldModel.Col_share, modeDataModel.getShare());
        values.put(FarmerShareOldModel.Col_sup_code, modeDataModel.getSupCode());
        values.put(FarmerShareOldModel.Col_curDate, modeDataModel.getCurDate());
        values.put(FarmerShareOldModel.Col_ServerStatus, modeDataModel.getServerStatus());
        long id = db.insert(FarmerShareOldModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void truncatePlotSurveyModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlotSurveyModel.TABLE_NAME);
        db.execSQL(PlotSurveyModel.CREATE_TABLE);
        db.close();
    }

    public List<PlotFarmerShareModel> getIdentifyPlotSurveyPlantingModelByLatLngExats(double lats, double lngs, String villCode) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_villageCode + "='" + villCode + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                modeDataModel.setPlotFrom("Current Year Survey");
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                List<LatLng> latLngList = new ArrayList<>();
                if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                }
                if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                }
                LatLng latlng = new LatLng(lats, lngs);
                Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                if (inside) {
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }

        if (list.size() == 0) {
            selectQuery = "SELECT  * FROM " + PlantingModel.TABLE_NAME + " WHERE 1=1 ";
            if (villCode.length() > 0) {
                selectQuery += " AND " + PlantingModel.Col_Village + "='" + villCode + "' ";
            }
            selectQuery += "ORDER BY " + PlantingModel.COLUMN_ID + " ASC";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel sprayItemModel = new PlotFarmerShareModel();
                    sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                    sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                    sprayItemModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                    sprayItemModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                    sprayItemModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                    sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                    sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                    sprayItemModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                    sprayItemModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                    sprayItemModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                    sprayItemModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                    sprayItemModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                    sprayItemModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                    sprayItemModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                    sprayItemModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                    sprayItemModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                    sprayItemModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                    sprayItemModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                    sprayItemModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                    sprayItemModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                    sprayItemModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                    //sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID )));
                    sprayItemModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                    sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                    sprayItemModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                    sprayItemModel.setPlotFrom("Planting");
                    List<LatLng> latLngList = new ArrayList<>();
                    if (Double.parseDouble(sprayItemModel.getEastNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getEastNorthLat()), Double.parseDouble(sprayItemModel.getEastNorthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getEastSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getEastSouthLat()), Double.parseDouble(sprayItemModel.getEastSouthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getWestSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getWestSouthLat()), Double.parseDouble(sprayItemModel.getWestSouthLng())));
                    }
                    if (Double.parseDouble(sprayItemModel.getWestNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(sprayItemModel.getWestNorthLat()), Double.parseDouble(sprayItemModel.getWestNorthLng())));
                    }
                    LatLng latlng = new LatLng(lats, lngs);
                    Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                    if (inside) {
                        list.add(sprayItemModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        if (list.size() == 0) {
            selectQuery = "SELECT * FROM " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareOldModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_surveyId + "=" +
                    PlotFarmerShareOldModel.PLOT_TABLE_NAME + "." + PlotFarmerShareOldModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
            if (villCode.length() > 0) {
                selectQuery += " AND " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_villageCode + "='" + villCode + "'";
            }

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    modeDataModel.setPlotFrom("Last Year Survey");
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    List<LatLng> latLngList = new ArrayList<>();
                    if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                    }
                    LatLng latlng = new LatLng(lats, lngs);
                    Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                    if (inside) {
                        list.add(modeDataModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return list;
    }

    public List<PlotFarmerShareModel> getIdentifyPlotSurveyPlantingModelByLatLngNearBy(double lats, double lngs, String villCode) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_villageCode + "='" + villCode + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                modeDataModel.setPlotFrom("Current Year Survey");
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                Location source = new Location(LocationManager.GPS_PROVIDER);
                source.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                source.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));
                Location destination = new Location(LocationManager.GPS_PROVIDER);
                destination.setLatitude(lats);
                destination.setLongitude(lngs);
                double distance = source.distanceTo(destination);
                if (distance < APIUrl.commonDistance) {
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT * FROM " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareOldModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_surveyId + "=" +
                    PlotFarmerShareOldModel.PLOT_TABLE_NAME + "." + PlotFarmerShareOldModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
            if (villCode.length() > 0) {
                selectQuery += " AND " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_villageCode + "='" + villCode + "'";
            }

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    modeDataModel.setPlotFrom("Last Year Survey");
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    Location source = new Location(LocationManager.GPS_PROVIDER);
                    source.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                    source.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));
                    Location destination = new Location(LocationManager.GPS_PROVIDER);
                    destination.setLatitude(lats);
                    destination.setLongitude(lngs);
                    double distance = source.distanceTo(destination);
                    if (distance < APIUrl.commonDistance) {
                        list.add(modeDataModel);
                    }
                } while (cursor.moveToNext());
            }
        }

        //if(list.size()==0)
        {
            selectQuery = "SELECT  * FROM " + PlantingModel.TABLE_NAME + " WHERE 1=1 ";
            if (villCode.length() > 0) {
                selectQuery += " AND " + PlantingModel.Col_Village + "='" + villCode + "' ";
            }
            selectQuery += "ORDER BY " + PlantingModel.COLUMN_ID + " ASC";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel sprayItemModel = new PlotFarmerShareModel();
                    sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                    sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                    sprayItemModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                    sprayItemModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                    sprayItemModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                    sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                    sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                    sprayItemModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                    sprayItemModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                    sprayItemModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                    sprayItemModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                    sprayItemModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                    sprayItemModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                    sprayItemModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                    sprayItemModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                    sprayItemModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                    sprayItemModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                    sprayItemModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                    sprayItemModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                    sprayItemModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                    sprayItemModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                    //sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID )));
                    sprayItemModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                    sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                    sprayItemModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                    sprayItemModel.setPlotFrom("Planting");
                    Location source = new Location(LocationManager.GPS_PROVIDER);
                    source.setLatitude(Double.parseDouble(sprayItemModel.getEastNorthLat()));
                    source.setLongitude(Double.parseDouble(sprayItemModel.getEastNorthLng()));
                    Location destination = new Location(LocationManager.GPS_PROVIDER);
                    destination.setLatitude(lats);
                    destination.setLongitude(lngs);
                    double distance = source.distanceTo(destination);
                    if (distance < APIUrl.commonDistance) {
                        list.add(sprayItemModel);
                    }
                } while (cursor.moveToNext());
            }
        }


        db.close();
        return list;
    }


    ////////////////////FarmerModel Data ////////////////////
    public long importPlotSurveyModel(PlotSurveyModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PlotSurveyModel.COLUMN_ID, modeDataModel.getColId());
        values.put(PlotSurveyModel.Col_PlotSrNo, modeDataModel.getPlotSrNo());
        values.put(PlotSurveyModel.Col_khashraNo, modeDataModel.getKhashraNo());
        values.put(PlotSurveyModel.Col_gataNo, modeDataModel.getGataNo());
        values.put(PlotSurveyModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(PlotSurveyModel.Col_areaMeter, modeDataModel.getAreaMeter());
        values.put(PlotSurveyModel.Col_areaHectare, modeDataModel.getAreaHectare());
        values.put(PlotSurveyModel.Col_mixCrop, modeDataModel.getMixCrop());
        values.put(PlotSurveyModel.Col_insect, modeDataModel.getInsect());
        values.put(PlotSurveyModel.Col_seedSource, modeDataModel.getSeedSource());
        values.put(PlotSurveyModel.Col_aadharNumber, modeDataModel.getAadharNumber());
        values.put(PlotSurveyModel.Col_plantMethod, modeDataModel.getPlantMethod());
        values.put(PlotSurveyModel.Col_cropCondition, modeDataModel.getCropCondition());
        values.put(PlotSurveyModel.Col_disease, modeDataModel.getDisease());
        values.put(PlotSurveyModel.Col_plantDate, modeDataModel.getPlantDate());
        values.put(PlotSurveyModel.Col_irrigation, modeDataModel.getIrrigation());
        values.put(PlotSurveyModel.Col_soilType, modeDataModel.getSoilType());
        values.put(PlotSurveyModel.Col_landType, modeDataModel.getLandType());
        values.put(PlotSurveyModel.Col_borderCrop, modeDataModel.getBorderCrop());
        values.put(PlotSurveyModel.Col_plotType, modeDataModel.getPlotType());
        values.put(PlotSurveyModel.Col_ghashtiNumber, modeDataModel.getGhashtiNumber());
        values.put(PlotSurveyModel.Col_interCrop, modeDataModel.getInterCrop());
        values.put(PlotSurveyModel.Col_eastNorth_lat, modeDataModel.getEastNorthLat());
        values.put(PlotSurveyModel.Col_eastNorth_lng, modeDataModel.getEastNorthLng());
        values.put(PlotSurveyModel.Col_eastNorth_distance, modeDataModel.getEastNorthDistance());
        values.put(PlotSurveyModel.Col_eastNorth_accuracy, modeDataModel.getEastNorthAccuracy());
        values.put(PlotSurveyModel.Col_westNorth_lat, modeDataModel.getWestNorthLat());
        values.put(PlotSurveyModel.Col_westNorth_lng, modeDataModel.getWestNorthLng());
        values.put(PlotSurveyModel.Col_westNorth_distance, modeDataModel.getWestNorthDistance());
        values.put(PlotSurveyModel.Col_westNorth_accuracy, modeDataModel.getWestNorthAccuracy());
        values.put(PlotSurveyModel.Col_eastSouth_lat, modeDataModel.getEastSouthLat());
        values.put(PlotSurveyModel.Col_eastSouth_lng, modeDataModel.getEastSouthLng());
        values.put(PlotSurveyModel.Col_eastSouth_distance, modeDataModel.getEastSouthDistance());
        values.put(PlotSurveyModel.Col_eastSouth_accuracy, modeDataModel.getEastSouthAccuracy());
        values.put(PlotSurveyModel.Col_westSouth_lat, modeDataModel.getWestSouthLat());
        values.put(PlotSurveyModel.Col_westSouth_lng, modeDataModel.getWestSouthLng());
        values.put(PlotSurveyModel.Col_westSouth_distance, modeDataModel.getWestSouthDistance());
        values.put(PlotSurveyModel.Col_westSouth_accuracy, modeDataModel.getWestSouthAccuracy());
        values.put(PlotSurveyModel.Col_varietyCode, modeDataModel.getVarietyCode());
        values.put(PlotSurveyModel.Col_caneType, modeDataModel.getCaneType());
        values.put(PlotSurveyModel.Col_insertDate, modeDataModel.getInsertDate());
        long id = db.insert(PlotSurveyModel.TABLE_NAME, null, values);
        // close db connection>|
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<PlotFarmerShareModel> getNearByPlotList(double lats, double lngs) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        Location currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude(lats);
        currentLocation.setLongitude(lngs);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));

                Location eastNorthLatLng = new Location(LocationManager.GPS_PROVIDER);
                eastNorthLatLng.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                eastNorthLatLng.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));

                Location getEastSouthLatLng = new Location(LocationManager.GPS_PROVIDER);
                getEastSouthLatLng.setLatitude(Double.parseDouble(modeDataModel.getEastSouthLat()));
                getEastSouthLatLng.setLongitude(Double.parseDouble(modeDataModel.getEastSouthLng()));

                Location getWestSouthLatLng = new Location(LocationManager.GPS_PROVIDER);
                getWestSouthLatLng.setLatitude(Double.parseDouble(modeDataModel.getWestSouthLat()));
                getWestSouthLatLng.setLongitude(Double.parseDouble(modeDataModel.getWestSouthLng()));

                Location getWestNorthLatLng = new Location(LocationManager.GPS_PROVIDER);
                getWestNorthLatLng.setLatitude(Double.parseDouble(modeDataModel.getWestNorthLat()));
                getWestNorthLatLng.setLongitude(Double.parseDouble(modeDataModel.getWestNorthLng()));

                exitsFromThisRegion:
                {
                    if (currentLocation.distanceTo(eastNorthLatLng) < APIUrl.NEARBY_RADIOUS) {
                        list.add(modeDataModel);
                        break exitsFromThisRegion;
                    }
                    if (currentLocation.distanceTo(getEastSouthLatLng) < APIUrl.NEARBY_RADIOUS) {
                        list.add(modeDataModel);
                        break exitsFromThisRegion;
                    }
                    if (currentLocation.distanceTo(getWestSouthLatLng) < APIUrl.NEARBY_RADIOUS) {
                        list.add(modeDataModel);
                        break exitsFromThisRegion;
                    }
                    if (currentLocation.distanceTo(getWestNorthLatLng) < APIUrl.NEARBY_RADIOUS) {
                        list.add(modeDataModel);
                        break exitsFromThisRegion;
                    }
                }


            } while (cursor.moveToNext());
        }

        if (list.size() == 0) {
            selectQuery = "SELECT * FROM " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareOldModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_surveyId + "=" +
                    PlotFarmerShareOldModel.PLOT_TABLE_NAME + "." + PlotFarmerShareOldModel.PLOT_COLUMN_ID + " WHERE 1=1 ";


            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    Location eastNorthLatLng = new Location(LocationManager.GPS_PROVIDER);
                    eastNorthLatLng.setLatitude(Double.parseDouble(modeDataModel.getEastNorthLat()));
                    eastNorthLatLng.setLongitude(Double.parseDouble(modeDataModel.getEastNorthLng()));

                    Location getEastSouthLatLng = new Location(LocationManager.GPS_PROVIDER);
                    getEastSouthLatLng.setLatitude(Double.parseDouble(modeDataModel.getEastSouthLat()));
                    getEastSouthLatLng.setLongitude(Double.parseDouble(modeDataModel.getEastSouthLng()));

                    Location getWestSouthLatLng = new Location(LocationManager.GPS_PROVIDER);
                    getWestSouthLatLng.setLatitude(Double.parseDouble(modeDataModel.getWestSouthLat()));
                    getWestSouthLatLng.setLongitude(Double.parseDouble(modeDataModel.getWestSouthLng()));

                    Location getWestNorthLatLng = new Location(LocationManager.GPS_PROVIDER);
                    getWestNorthLatLng.setLatitude(Double.parseDouble(modeDataModel.getWestNorthLat()));
                    getWestNorthLatLng.setLongitude(Double.parseDouble(modeDataModel.getWestNorthLng()));

                    exitsFromThisRegion:
                    {
                        if (currentLocation.distanceTo(eastNorthLatLng) < APIUrl.NEARBY_RADIOUS) {
                            list.add(modeDataModel);
                            break exitsFromThisRegion;
                        }
                        if (currentLocation.distanceTo(getEastSouthLatLng) < APIUrl.NEARBY_RADIOUS) {
                            list.add(modeDataModel);
                            break exitsFromThisRegion;
                        }
                        if (currentLocation.distanceTo(getWestSouthLatLng) < APIUrl.NEARBY_RADIOUS) {
                            list.add(modeDataModel);
                            break exitsFromThisRegion;
                        }
                        if (currentLocation.distanceTo(getWestNorthLatLng) < APIUrl.NEARBY_RADIOUS) {
                            list.add(modeDataModel);
                            break exitsFromThisRegion;
                        }
                    }

                } while (cursor.moveToNext());
            }
        }
        db.close();
        return list;
    }

    public List<PlotFarmerShareModel> getPlotFarmerShareModel(String villageCode, String growerCode) {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_villageCode + "='" + villageCode + "'";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_growerCode + "='" + growerCode + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<PlotFarmerShareModel> getSurveyFilterData(String villageCode, String growerCode) {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_villageCode + "='" + villageCode + "'";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_growerCode + "='" + growerCode + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<PlotSurveyModel> getPlotSurveyId(String villageCode, String PlotSrNo) {
        List<PlotSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        selectQuery = "SELECT  * FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + villageCode + "' AND " + PlotSurveyModel.Col_PlotSrNo + "='" + PlotSrNo + "' " + " ORDER BY " + PlotSurveyModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotSurveyModel modeDataModel = new PlotSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_gataNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public void truncatePlotSurveyOldModel(String villageCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + PlotSurveyOldModel.TABLE_NAME + " WHERE " + PlotSurveyOldModel.Col_villageCode + "='" + villageCode + "'");
        db.execSQL("delete from farmer_share_old where survey_id not in (select plot_survey_id from plot_survey_old)");
        db.close();
    }

    public int getTotalShareUsed(String surveyId) {
        int totalShare = 0;
        String selectQuery = "SELECT IFNULL(SUM(cast(share AS int)),0) AS total_share FROM farmer_share WHERE survey_id=" + surveyId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                totalShare = cursor.getInt(cursor.getColumnIndex("total_share"));
            } while (cursor.moveToNext());
        }
        db.close();
        return totalShare;
    }

    public List<PlotFarmerShareOldModel> getPlotFarmerShareOldModelFilterData(String villageCode, String growerCode) {
        List<PlotFarmerShareOldModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareOldModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_surveyId + "=" +
                PlotFarmerShareOldModel.PLOT_TABLE_NAME + "." + PlotFarmerShareOldModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_villageCode + "='" + villageCode + "'";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_growerCode + "='" + growerCode + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareOldModel modeDataModel = new PlotFarmerShareOldModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareOldModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public void deletePlotSurveyModel(String id, String village) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public List<PlotSurveyModel> getPlotSurveyModel(String id) {
        List<PlotSurveyModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + PlotSurveyModel.TABLE_NAME + " ORDER BY " + PlotSurveyModel.COLUMN_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.Col_villageCode + "='" + id + "' " + " ORDER BY " + PlotSurveyModel.COLUMN_ID + " DESC";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotSurveyModel modeDataModel = new PlotSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_gataNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insertDate)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<PlotSurveyModel> getPlotSurveyModelById(String id) {
        List<PlotSurveyModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PlotSurveyModel.TABLE_NAME + " WHERE 1=1 ";
        if (id.length() > 0) {
            selectQuery += " AND " + PlotSurveyModel.COLUMN_ID + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotSurveyModel modeDataModel = new PlotSurveyModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_gataNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_insertDate)));
                modeDataModel.setIsDelete(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_is_delete)));
                modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_old_plot_id)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_old_plot_from)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getPendingFarmerShareZeroModel(String villCode) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT  IFNULL(SUM(" + FarmerShareModel.Col_share + "),0) as " + FarmerShareModel.Col_share + ",COUNT(*) AS " + FarmerShareModel.Col_srNo +
                "," + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID + "," + PlotSurveyModel.Col_villageCode + "," + PlotSurveyModel.Col_areaHectare + " FROM " +
                PlotSurveyModel.TABLE_NAME + " LEFT JOIN " + FarmerShareModel.TABLE_NAME + " ON " + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID +
                "=" + FarmerShareModel.TABLE_NAME + "." + FarmerShareModel.Col_surveyId + " WHERE 1=1 ";

        if (villCode.length() > 0) {
            selectQuery = selectQuery + " AND " + PlotSurveyModel.Col_villageCode + "='" + villCode + "' ";
        }
        selectQuery = selectQuery + " GROUP BY " + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share))) == 0) {
                    FarmerShareModel modeDataModel = new FarmerShareModel();
                    //modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public void deletePlotSurveyModelById(String plotSerialId) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + PlotSurveyModel.TABLE_NAME + " WHERE " + PlotSurveyModel.COLUMN_ID + "='" + plotSerialId + "'");
        db.close();
    }

    public List<PlotFarmerShareModel> getPlotFarmerShareModelForMap(String villageCode, String growerCode) {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery;
        if (growerCode.length() == 0) {
            selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                    PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE " +
                    PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.Col_plotvillageCode + "='" + villageCode + "'";
        } else {
            selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                    PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE " +
                    PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.Col_plotvillageCode + "='" + villageCode + "' AND " +
                    PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_growerCode + "='" + growerCode + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                List<MasterCaneSurveyModel> masterCaneTypeModel = getMasterModelById("2", cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                List<MasterCaneSurveyModel> masterVarietyTypeModel = getMasterModelById("1", cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                if (masterVarietyTypeModel.size() > 0) {
                    modeDataModel.setVarietyCode(masterVarietyTypeModel.get(0).getName());
                } else {
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                }
                if (masterVarietyTypeModel.size() > 0) {
                    modeDataModel.setCaneType(masterCaneTypeModel.get(0).getName());
                } else {
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                }
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    ////////////////////FarmerModel Data ////////////////////
    public long insertFarmerShareModel(FarmerShareModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(FarmerShareModel.Col_surveyId, modeDataModel.getSurveyId());
        values.put(FarmerShareModel.Col_srNo, modeDataModel.getSrNo());
        values.put(FarmerShareModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(FarmerShareModel.Col_growerCode, modeDataModel.getGrowerCode());
        values.put(FarmerShareModel.Col_growerName, modeDataModel.getGrowerName());
        values.put(FarmerShareModel.Col_growerFatherName, modeDataModel.getGrowerFatherName());
        values.put(FarmerShareModel.Col_growerAadhar_number, modeDataModel.getGrowerAadharNumber());
        values.put(FarmerShareModel.Col_share, modeDataModel.getShare());
        values.put(FarmerShareModel.Col_sup_code, modeDataModel.getSupCode());
        values.put(FarmerShareModel.Col_curDate, modeDataModel.getCurDate());
        values.put(FarmerShareModel.Col_ServerStatus, "No");
        long id = db.insert(FarmerShareModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    ////////////////////FarmerModel Data ////////////////////
    public long importFarmerShareModel(FarmerShareModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(FarmerShareModel.COLUMN_ID, modeDataModel.getColId());
        values.put(FarmerShareModel.Col_surveyId, modeDataModel.getSurveyId());
        values.put(FarmerShareModel.Col_srNo, modeDataModel.getSrNo());
        values.put(FarmerShareModel.Col_villageCode, modeDataModel.getVillageCode());
        values.put(FarmerShareModel.Col_growerCode, modeDataModel.getGrowerCode());
        values.put(FarmerShareModel.Col_growerName, modeDataModel.getGrowerName());
        values.put(FarmerShareModel.Col_growerFatherName, modeDataModel.getGrowerFatherName());
        values.put(FarmerShareModel.Col_growerAadhar_number, modeDataModel.getGrowerAadharNumber());
        values.put(FarmerShareModel.Col_share, modeDataModel.getShare());
        values.put(FarmerShareModel.Col_sup_code, modeDataModel.getSupCode());
        values.put(FarmerShareModel.Col_curDate, modeDataModel.getCurDate());
        values.put(FarmerShareModel.Col_ServerStatus, modeDataModel.getServerStatus());
        long id = db.insert(FarmerShareModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void truncateFarmerShareSurveyModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + FarmerShareModel.TABLE_NAME);
        db.execSQL(FarmerShareModel.CREATE_TABLE);
        db.close();
    }

    public void deleteFarmerShareModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + FarmerShareModel.TABLE_NAME);
        db.close();
    }

    public void updateAllServerFarmerShareModel(String status, String message) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + FarmerShareModel.TABLE_NAME + " SET " + FarmerShareModel.Col_ServerStatus + "='" + status + "', " + FarmerShareModel.Col_ServerStatusRemark + "='" + message + "' WHERE " +
                FarmerShareModel.Col_ServerStatus + "='FAILED'");
        db.close();
    }

    public void updateServerFarmerShareModel(String id, String status, String message) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + FarmerShareModel.TABLE_NAME + " SET " + FarmerShareModel.Col_ServerStatus + "='" + status + "', " + FarmerShareModel.Col_ServerStatusRemark + "='" + message + "' WHERE " +
                FarmerShareModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public void updateAllServerFarmerShareModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + FarmerShareModel.TABLE_NAME + " SET " + FarmerShareModel.Col_ServerStatus + "='No'");
        db.close();
    }

    public List<FarmerShareModel> getFarmerShareModel(String id) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  " + FarmerShareModel.TABLE_NAME + ".* FROM " + FarmerShareModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.Col_surveyId + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatusRemark)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getFarmerShareModelById(String id) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "";
        if (id.equalsIgnoreCase("")) {
            selectQuery = "SELECT  " + FarmerShareModel.TABLE_NAME + ".* FROM " + FarmerShareModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.COLUMN_ID + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatusRemark)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getPendingFarmerShareModel(String villCode) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT  IFNULL(SUM(" + FarmerShareModel.Col_share + "),0) as " + FarmerShareModel.Col_share + ",COUNT(*) AS " + FarmerShareModel.Col_srNo +
                "," + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID + "," + PlotSurveyModel.Col_villageCode + "," + PlotSurveyModel.Col_areaHectare + " FROM " +
                PlotSurveyModel.TABLE_NAME + " LEFT JOIN " + FarmerShareModel.TABLE_NAME + " ON " + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID +
                "=" + FarmerShareModel.TABLE_NAME + "." + FarmerShareModel.Col_surveyId + " WHERE 1=1 ";
        if (villCode.length() > 0) {
            selectQuery = selectQuery + " AND " + PlotSurveyModel.Col_villageCode + "='" + villCode + "' ";
        }
        selectQuery = selectQuery + " GROUP BY " + PlotSurveyModel.TABLE_NAME + "." + PlotSurveyModel.COLUMN_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share))) < 100) {
                    FarmerShareModel modeDataModel = new FarmerShareModel();
                    //modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.COLUMN_ID)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotSurveyModel.Col_areaHectare)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }


    public void deleteFarmerShareModel(String id) {
        if (id.length() == 0) {//openDeleteSharePlot
            //SQLiteDatabase db = this.getReadableDatabase();
            //db.execSQL("UPDATE "+ PlotSurveyModel.TABLE_NAME+" SET "+PlotSurveyModel.Col_is_delete+"='1' WHERE "+PlotSurveyModel.COLUMN_ID+"='"+id+"'");
            //db.execSQL("DELETE FROM "+ FarmerShareModel.TABLE_NAME);
            //db.close();
        } else {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("UPDATE " + PlotSurveyModel.TABLE_NAME + " SET " + PlotSurveyModel.Col_is_delete + "='1' WHERE " + PlotSurveyModel.COLUMN_ID + "='" + id + "'");
            db.execSQL("DELETE FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.Col_surveyId + "='" + id + "'");
            db.close();
        }

    }


    public List<FarmerShareModel> getDeleteFarmerShareModel() {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "";
        selectQuery = "SELECT farmer_share.* FROM farmer_share LEFT JOIN plot_survey ON plot_survey.plot_survey_id=farmer_share.survey_id WHERE farmer_share.village_code='' OR farmer_share.grower_code=''";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatusRemark)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getFarmerShareUploadingData() {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.Col_ServerStatus +
                "='No' AND (" + FarmerShareModel.Col_villageCode + "!='' OR " + FarmerShareModel.Col_growerCode + "!='') ORDER BY " + FarmerShareModel.COLUMN_ID + " ASC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getFarmerSharePendingFailedData() {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.Col_ServerStatus +
                "='No' OR " + FarmerShareModel.Col_ServerStatus + "='FAILED' ORDER BY " + FarmerShareModel.COLUMN_ID + " ASC LIMIT 100";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatusRemark)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<FarmerShareModel> getFarmerShareUploadingDataById(String id) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + FarmerShareModel.TABLE_NAME + " WHERE " + FarmerShareModel.COLUMN_ID + "='" + id + "'"
                + " ORDER BY " + FarmerShareModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

////////////-------------Purchi Details--------------////////////

    public long insertPurchiDetails(PurchiDetails villageModal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PurchiDetails.Col_code, villageModal.getCode());
        values.put(PurchiDetails.Col_name, villageModal.getName());
        values.put(PurchiDetails.Col_purchino, villageModal.getPurchiNo());
        values.put(PurchiDetails.Col_mode, villageModal.getMode());
        values.put(PurchiDetails.Col_category, villageModal.getCategory());
        // insert row
        long id = db.insert(PurchiDetails.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<PurchiDetails> getPurchiDetails(String type) {
        List<PurchiDetails> purchiDetailsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PurchiDetails.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + PurchiDetails.Col_category + "='" + type + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PurchiDetails PurchiDetail = new PurchiDetails();
                PurchiDetail.setCode(cursor.getString(cursor.getColumnIndex(PurchiDetails.Col_code)));
                PurchiDetail.setName(cursor.getString(cursor.getColumnIndex(PurchiDetails.Col_name)));
                PurchiDetail.setPurchiNo(cursor.getString(cursor.getColumnIndex(PurchiDetails.Col_purchino)));
                PurchiDetail.setMode(cursor.getString(cursor.getColumnIndex(PurchiDetails.Col_mode)));
                PurchiDetail.setCategory(cursor.getString(cursor.getColumnIndex(PurchiDetails.Col_category)));
                purchiDetailsList.add(PurchiDetail);
            } while (cursor.moveToNext());
        }
        db.close();
        return purchiDetailsList;
    }


    //CANE Development start

    public long insertMasterData(MasterDropDown villageModal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MasterDropDown.Col_code, villageModal.getCode());
        values.put(MasterDropDown.Col_name, villageModal.getName());
        values.put(MasterDropDown.Col_type, villageModal.getType());
        // insert row
        long id = db.insert(MasterDropDown.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<MasterDropDown> getMasterDropDown(String type) {
        List<MasterDropDown> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MasterDropDown.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + MasterDropDown.Col_type + "='" + type + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterDropDown masterDropDown = new MasterDropDown();
                masterDropDown.setCode(cursor.getString(cursor.getColumnIndex(MasterDropDown.Col_code)));
                masterDropDown.setName(cursor.getString(cursor.getColumnIndex(MasterDropDown.Col_name)));
                masterDropDownList.add(masterDropDown);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }


    //--------------new--------------------------------------------------------
    public List<MasterDropDown> getMasterDropDowncode(String type, String code) {
        List<MasterDropDown> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MasterDropDown.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + MasterDropDown.Col_type + "='" + type + "'";
        }
        if (code.length() > 0) {
            selectQuery += " AND " + MasterDropDown.Col_code + "='" + code + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterDropDown masterDropDown = new MasterDropDown();
                masterDropDown.setCode(cursor.getString(cursor.getColumnIndex(MasterDropDown.Col_code)));
                masterDropDown.setName(cursor.getString(cursor.getColumnIndex(MasterDropDown.Col_name)));
                masterDropDownList.add(masterDropDown);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }


    public long insertMasterSubDropDown(MasterSubDropDown villageModal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MasterSubDropDown.Col_code, villageModal.getCode());
        values.put(MasterSubDropDown.Col_name, villageModal.getName());
        values.put(MasterSubDropDown.Col_type, villageModal.getType());
        values.put(MasterSubDropDown.Col_master_code, villageModal.getMasterCode());
        values.put(MasterSubDropDown.Col_extra_field, villageModal.getExtraField());
        values.put(MasterSubDropDown.Col_item_flag, villageModal.getItemFlag());
        // insert row
        long id = db.insert(MasterSubDropDown.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<MasterSubDropDown> getMasterSubDropDown(String type, String masterCode) {
        List<MasterSubDropDown> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MasterSubDropDown.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + MasterSubDropDown.Col_type + "='" + type + "' ";
        }
        if (masterCode.length() > 0) {
            selectQuery += " AND " + MasterSubDropDown.Col_master_code + "='" + masterCode + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterSubDropDown masterDropDown = new MasterSubDropDown();
                masterDropDown.setCode(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_code)));
                masterDropDown.setName(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_name)));
                masterDropDown.setMasterCode(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_master_code)));
                masterDropDown.setExtraField(cursor.getInt(cursor.getColumnIndex(MasterSubDropDown.Col_extra_field)));
                masterDropDown.setItemFlag(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_item_flag)));
                masterDropDownList.add(masterDropDown);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }

    public List<MasterSubDropDown> getMasterSubDropDownActivity(String type, String masterCode) {
        List<MasterSubDropDown> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MasterSubDropDown.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + MasterSubDropDown.Col_type + "='" + type + "' ";
        }
        if (masterCode.length() > 0) {
            selectQuery += " AND " + MasterSubDropDown.Col_master_code + " IN ('" + masterCode + "',99) ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MasterSubDropDown masterDropDown = new MasterSubDropDown();
                masterDropDown.setCode(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_code)));
                masterDropDown.setName(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_name)));
                masterDropDown.setMasterCode(cursor.getString(cursor.getColumnIndex(MasterSubDropDown.Col_master_code)));
                masterDropDown.setExtraField(cursor.getInt(cursor.getColumnIndex(MasterSubDropDown.Col_extra_field)));
                masterDropDownList.add(masterDropDown);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }

    public long insertVillageModal(VillageModal villageModal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(VillageModal.Col_code, villageModal.getCode());
        values.put(VillageModal.Col_name, villageModal.getName());
        values.put(VillageModal.Col_is_target, villageModal.isTarget());
        values.put(VillageModal.Col_max_indent, villageModal.getMaxIndent());
        values.put(VillageModal.Col_max_plant, villageModal.getMaxPlant());
        // insert row
        long id = db.insert(VillageModal.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<VillageModal> getVillageModal(String type) {
        List<VillageModal> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + VillageModal.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + VillageModal.Col_code + "='" + type + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VillageModal villageModal = new VillageModal();
                villageModal.setCode(cursor.getString(cursor.getColumnIndex(VillageModal.Col_code)));
                villageModal.setName(cursor.getString(cursor.getColumnIndex(VillageModal.Col_name)));
                villageModal.setTarget(cursor.getInt(cursor.getColumnIndex(VillageModal.Col_is_target)));
                villageModal.setMaxIndent(cursor.getString(cursor.getColumnIndex(VillageModal.Col_max_indent)));
                villageModal.setMaxPlant(cursor.getString(cursor.getColumnIndex(VillageModal.Col_max_plant)));
                masterDropDownList.add(villageModal);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }

    /////////------------Village Name Cut To Crush-----------//////////


    public long insertVModal(VModal vModal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(VModal.Col_villCode, vModal.getVillageCode());
        values.put(VModal.Col_villName, vModal.getVillName());


        // insert row
        long id = db.insert(VModal.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<VModal> getVillnameModal(String type) {
        List<VModal> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + VModal.TABLE_NAME + " WHERE 1=1 ";
        if (type.length() > 0) {
            selectQuery += " AND " + VModal.Col_villName + "='" + type + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VModal vModal = new VModal();
                vModal.setVillageCode(cursor.getInt(cursor.getColumnIndex(VModal.Col_villCode)));
                vModal.setVillName(cursor.getString(cursor.getColumnIndex(VModal.Col_villName)));
                masterDropDownList.add(vModal);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }

    public long insertGrowerModel(GrowerModel growerModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(GrowerModel.Col_vill_code, growerModel.getVillageCode());
        values.put(GrowerModel.Col_grower_code, growerModel.getGrowerCode());
        values.put(GrowerModel.Col_grower_name, growerModel.getGrowerName());
        values.put(GrowerModel.Col_grower_father_name, growerModel.getGrowerFather());
        // insert row
        long id = db.insert(GrowerModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<GrowerModel> getGrowerModel(String villageCode, String growerCode) {
        List<GrowerModel> masterDropDownList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GrowerModel.TABLE_NAME + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + GrowerModel.Col_vill_code + "='" + villageCode + "' ";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + GrowerModel.Col_grower_code + "='" + growerCode + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                GrowerModel villageModal = new GrowerModel();
                villageModal.setGrowerCode(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_code)));
                villageModal.setGrowerName(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_name)));
                villageModal.setGrowerFather(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_father_name)));
                masterDropDownList.add(villageModal);
            } while (cursor.moveToNext());
        }
        db.close();
        return masterDropDownList;
    }

    public long insertControlModel(ControlModel controlModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ControlModel.Col_name, controlModel.getName());
        values.put(ControlModel.Col_value, controlModel.getValue());
        values.put(ControlModel.Col_form_name, controlModel.getFormName());
        // insert row
        long id = db.insert(ControlModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public String getControlStatus(String field, String formName) {
        String returnData = "false";
        String selectQuery = "SELECT  * FROM " + ControlModel.TABLE_NAME + " WHERE 1=1 ";
        if (field.length() > 0) {
            selectQuery += " AND " + ControlModel.Col_name + "='" + field + "' ";
        }
        if (formName.length() > 0) {
            selectQuery += " AND " + ControlModel.Col_form_name + "='" + formName + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                returnData = cursor.getString(cursor.getColumnIndex(ControlModel.Col_value));
            } while (cursor.moveToNext());
        }
        db.close();
        return returnData;
    }

    public long insertIndentModel(IndentModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(IndentModel.Col_Village, indentModel.getVillage());
        values.put(IndentModel.Col_Grower, indentModel.getGrower());
        values.put(IndentModel.Col_Grower_name, indentModel.getGrowerName());
        values.put(IndentModel.Col_Grower_father, indentModel.getGrowerFather());
        values.put(IndentModel.Col_PLOTVillage, indentModel.getPLOTVillage());
        values.put(IndentModel.Col_Irrigationmode, indentModel.getIrrigationmode());
        values.put(IndentModel.Col_SupplyMode, indentModel.getSupplyMode());
        values.put(IndentModel.Col_Harvesting, indentModel.getHarvesting());
        values.put(IndentModel.Col_Equipment, indentModel.getEquipment());
        values.put(IndentModel.Col_SeedSource, indentModel.getSeedSource());
        values.put(IndentModel.Col_LandType, indentModel.getLandType());
        values.put(IndentModel.Col_SeedType, indentModel.getSeedType());
        values.put(IndentModel.Col_NOFPLOTS, indentModel.getNOFPLOTS());
        values.put(IndentModel.Col_INDAREA, indentModel.getINDAREA());
        values.put(IndentModel.Col_InsertLAT, indentModel.getInsertLAT());
        values.put(IndentModel.Col_InsertLON, indentModel.getInsertLON());
        values.put(IndentModel.Col_Image, indentModel.getImage());
        values.put(IndentModel.Col_SuperviserCode, indentModel.getSuperviserCode());
        values.put(IndentModel.Col_Dim1, indentModel.getDim1());
        values.put(IndentModel.Col_Dim2, indentModel.getDim2());
        values.put(IndentModel.Col_Dim3, indentModel.getDim3());
        values.put(IndentModel.Col_Dim4, indentModel.getDim4());
        values.put(IndentModel.Col_Area, indentModel.getArea());
        values.put(IndentModel.Col_LAT1, indentModel.getLAT1());
        values.put(IndentModel.Col_LON1, indentModel.getLON1());
        values.put(IndentModel.Col_LAT2, indentModel.getLAT2());
        values.put(IndentModel.Col_LON2, indentModel.getLON2());
        values.put(IndentModel.Col_LAT3, indentModel.getLAT3());
        values.put(IndentModel.Col_LON3, indentModel.getLON3());
        values.put(IndentModel.Col_LAT4, indentModel.getLAT4());
        values.put(IndentModel.Col_LON4, indentModel.getLON4());
        values.put(IndentModel.Col_SprayType, indentModel.getSprayType());
        values.put(IndentModel.Col_PloughingType, indentModel.getPloughingType());
        values.put(IndentModel.Col_MobilNO, indentModel.getMobilNO());
        values.put(IndentModel.Col_MDATE, indentModel.getMDATE());
        values.put(IndentModel.Col_VARIETY, indentModel.getVARIETY());
        values.put(IndentModel.Col_Crop, indentModel.getCrop());
        values.put(IndentModel.Col_PLANTINGTYPE, indentModel.getPLANTINGTYPE());
        values.put(IndentModel.Col_PLANTATION, indentModel.getPLANTATION());
        values.put(IndentModel.Col_SMathod, indentModel.getMethod());
        values.put(IndentModel.Col_ServerStatus, indentModel.getServerStatus());
        values.put(IndentModel.Col_Remark, "");
        values.put(IndentModel.Col_CurrentDate, currentDt);
        values.put(IndentModel.Col_PLOT_SR_NO, indentModel.getPlotSerialNumber());
        values.put(IndentModel.Col_is_ideal, indentModel.getIsIdeal());
        values.put(IndentModel.Col_is_nursery, indentModel.getIsNursery());
        values.put(IndentModel.Col_ind_date, indentModel.getIndDate());

        // insert row
        long id = db.insert(IndentModel.TABLE_NAME, null, values);
        // close db connection

        db.close();

        SQLiteDatabase db1 = this.getReadableDatabase();
        db1.execSQL("UPDATE " + VillageModal.TABLE_NAME + " SET " + VillageModal.Col_max_indent + "='" + indentModel.getPlotSerialNumber() + "'" +
                " WHERE " + VillageModal.Col_code + "='" + indentModel.getPLOTVillage() + "'");
        db1.close();
        // return newly inserted row id
        return id;
    }

    public void truncateIndentModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + IndentModel.TABLE_NAME);
        db.execSQL(IndentModel.CREATE_TABLE);
        db.close();
    }

    public long importIndentModel(IndentModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(IndentModel.COLUMN_ID, indentModel.getColId());
        values.put(IndentModel.Col_Village, indentModel.getVillage());
        values.put(IndentModel.Col_Grower, indentModel.getGrower());
        values.put(IndentModel.Col_Grower_name, indentModel.getGrowerName());
        values.put(IndentModel.Col_Grower_father, indentModel.getGrowerFather());
        values.put(IndentModel.Col_PLOTVillage, indentModel.getPLOTVillage());
        values.put(IndentModel.Col_Irrigationmode, indentModel.getIrrigationmode());
        values.put(IndentModel.Col_SupplyMode, indentModel.getSupplyMode());
        values.put(IndentModel.Col_Harvesting, indentModel.getHarvesting());
        values.put(IndentModel.Col_Equipment, indentModel.getEquipment());
        values.put(IndentModel.Col_LandType, indentModel.getLandType());
        values.put(IndentModel.Col_SeedType, indentModel.getSeedType());
        values.put(IndentModel.Col_NOFPLOTS, indentModel.getNOFPLOTS());
        values.put(IndentModel.Col_INDAREA, indentModel.getINDAREA());
        values.put(IndentModel.Col_InsertLAT, indentModel.getInsertLAT());
        values.put(IndentModel.Col_InsertLON, indentModel.getInsertLON());
        values.put(IndentModel.Col_Image, indentModel.getImage());
        values.put(IndentModel.Col_SuperviserCode, indentModel.getSuperviserCode());
        values.put(IndentModel.Col_Dim1, indentModel.getDim1());
        values.put(IndentModel.Col_Dim2, indentModel.getDim2());
        values.put(IndentModel.Col_Dim3, indentModel.getDim3());
        values.put(IndentModel.Col_Dim4, indentModel.getDim4());
        values.put(IndentModel.Col_Area, indentModel.getArea());
        values.put(IndentModel.Col_LAT1, indentModel.getLAT1());
        values.put(IndentModel.Col_LON1, indentModel.getLON1());
        values.put(IndentModel.Col_LAT2, indentModel.getLAT2());
        values.put(IndentModel.Col_LON2, indentModel.getLON2());
        values.put(IndentModel.Col_LAT3, indentModel.getLAT3());
        values.put(IndentModel.Col_LON3, indentModel.getLON3());
        values.put(IndentModel.Col_LAT4, indentModel.getLAT4());
        values.put(IndentModel.Col_LON4, indentModel.getLON4());
        values.put(IndentModel.Col_SprayType, indentModel.getSprayType());
        values.put(IndentModel.Col_PloughingType, indentModel.getPloughingType());
        values.put(IndentModel.Col_MobilNO, indentModel.getMobilNO());
        values.put(IndentModel.Col_MDATE, indentModel.getMDATE());
        values.put(IndentModel.Col_VARIETY, indentModel.getVARIETY());
        values.put(IndentModel.Col_Crop, indentModel.getCrop());
        values.put(IndentModel.Col_PLANTINGTYPE, indentModel.getPLANTINGTYPE());
        values.put(IndentModel.Col_PLANTATION, indentModel.getPLANTATION());
        values.put(IndentModel.Col_SMathod, indentModel.getMethod());
        values.put(IndentModel.Col_ServerStatus, indentModel.getServerStatus());
        values.put(IndentModel.Col_Remark, indentModel.getRemark());
        values.put(IndentModel.Col_CurrentDate, indentModel.getCurrentDate());
        values.put(IndentModel.Col_PLOT_SR_NO, indentModel.getPlotSerialNumber());
        values.put(IndentModel.Col_is_ideal, indentModel.getIsIdeal());
        values.put(IndentModel.Col_is_nursery, indentModel.getIsNursery());
        values.put(IndentModel.Col_ind_date, indentModel.getIndDate());

        // insert row
        long id = db.insert(IndentModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<IndentModel> getIndentModel(String serverStatus, String ItemId, String villCode, String growerCode, String plotVillCode) {
        List<IndentModel> indentModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + IndentModel.TABLE_NAME + " WHERE 1=1 ";
        if (serverStatus.length() > 0) {
            selectQuery += " AND " + IndentModel.Col_ServerStatus + " LIKE '%" + serverStatus + "%' ";
        }
        if (ItemId.length() > 0) {
            selectQuery += " AND " + IndentModel.COLUMN_ID + "='" + ItemId + "' ";
        }
        if (villCode.length() > 0) {
            selectQuery += " AND " + IndentModel.Col_Village + "='" + villCode + "' ";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + IndentModel.Col_Grower + "='" + growerCode + "' ";
        }
        if (plotVillCode.length() > 0) {
            selectQuery += " AND " + IndentModel.Col_PLOTVillage + "='" + plotVillCode + "' ";
        }
        selectQuery += "ORDER BY " + IndentModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                IndentModel sprayItemModel = new IndentModel();
                sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(IndentModel.COLUMN_ID)));
                sprayItemModel.setVillage(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Village)));
                sprayItemModel.setGrower(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Grower)));
                sprayItemModel.setGrowerName(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Grower_name)));
                sprayItemModel.setGrowerFather(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Grower_father)));
                sprayItemModel.setPLOTVillage(cursor.getString(cursor.getColumnIndex(IndentModel.Col_PLOTVillage)));
                sprayItemModel.setIrrigationmode(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Irrigationmode)));
                sprayItemModel.setSupplyMode(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SupplyMode)));
                sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SeedSource)));
                sprayItemModel.setHarvesting(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Harvesting)));
                sprayItemModel.setEquipment(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Equipment)));
                sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LandType)));
                sprayItemModel.setSeedType(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SeedType)));
                sprayItemModel.setNOFPLOTS(cursor.getString(cursor.getColumnIndex(IndentModel.Col_NOFPLOTS)));
                sprayItemModel.setINDAREA(cursor.getString(cursor.getColumnIndex(IndentModel.Col_INDAREA)));
                sprayItemModel.setInsertLAT(cursor.getString(cursor.getColumnIndex(IndentModel.Col_InsertLAT)));
                sprayItemModel.setInsertLON(cursor.getString(cursor.getColumnIndex(IndentModel.Col_InsertLON)));
                sprayItemModel.setImage(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Image)));
                sprayItemModel.setSuperviserCode(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SuperviserCode)));
                sprayItemModel.setDim1(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Dim1)));
                sprayItemModel.setDim2(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Dim2)));
                sprayItemModel.setDim3(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Dim3)));
                sprayItemModel.setDim4(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Dim4)));
                sprayItemModel.setArea(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Area)));
                sprayItemModel.setLAT1(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LAT1)));
                sprayItemModel.setLON1(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LON1)));
                sprayItemModel.setLAT2(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LAT2)));
                sprayItemModel.setLON2(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LON2)));
                sprayItemModel.setLAT3(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LAT3)));
                sprayItemModel.setLON3(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LON3)));
                sprayItemModel.setLAT4(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LAT4)));
                sprayItemModel.setLON4(cursor.getString(cursor.getColumnIndex(IndentModel.Col_LON4)));
                sprayItemModel.setSprayType(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SprayType)));
                sprayItemModel.setPloughingType(cursor.getString(cursor.getColumnIndex(IndentModel.Col_PloughingType)));
                sprayItemModel.setMobilNO(cursor.getString(cursor.getColumnIndex(IndentModel.Col_MobilNO)));
                sprayItemModel.setMDATE(cursor.getString(cursor.getColumnIndex(IndentModel.Col_MDATE)));
                sprayItemModel.setVARIETY(cursor.getString(cursor.getColumnIndex(IndentModel.Col_VARIETY)));
                sprayItemModel.setCrop(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Crop)));
                sprayItemModel.setPLANTINGTYPE(cursor.getString(cursor.getColumnIndex(IndentModel.Col_PLANTINGTYPE)));
                sprayItemModel.setPLANTATION(cursor.getString(cursor.getColumnIndex(IndentModel.Col_PLANTATION)));
                sprayItemModel.setMethod(cursor.getString(cursor.getColumnIndex(IndentModel.Col_SMathod)));
                sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(IndentModel.Col_ServerStatus)));
                sprayItemModel.setRemark(cursor.getString(cursor.getColumnIndex(IndentModel.Col_Remark)));
                sprayItemModel.setCurrentDate(cursor.getString(cursor.getColumnIndex(IndentModel.Col_CurrentDate)));
                sprayItemModel.setPlotSerialNumber(cursor.getString(cursor.getColumnIndex(IndentModel.Col_PLOT_SR_NO)));
                sprayItemModel.setIsIdeal(cursor.getString(cursor.getColumnIndex(IndentModel.Col_is_ideal)));
                sprayItemModel.setIsNursery(cursor.getString(cursor.getColumnIndex(IndentModel.Col_is_nursery)));
                sprayItemModel.setIndDate(cursor.getString(cursor.getColumnIndex(IndentModel.Col_ind_date)));
                indentModelList.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return indentModelList;
    }

    public void updateServerStatusIndent(String remark, String id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + IndentModel.TABLE_NAME + " SET " + IndentModel.Col_ServerStatus + "='" + status + "', " +
                IndentModel.Col_Remark + "='" + remark + "' WHERE " + IndentModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public void resetServerStatusIndent() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + IndentModel.TABLE_NAME + " SET " + IndentModel.Col_ServerStatus + "='No' WHERE " + IndentModel.Col_ServerStatus + "='FAILED'");
        db.execSQL("UPDATE " + IndentModel.TABLE_NAME + " SET " + IndentModel.Col_ServerStatus + "='No' WHERE " + IndentModel.Col_ServerStatus + "='Failed'");
        db.close();
    }


    public long insertPlantingModel(PlantingModel indentModel, List<Map<String, String>> equipmentObjectList, List<Map<String, String>> objectList) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(PlantingModel.Col_Village, indentModel.getVillage());
        values.put(PlantingModel.Col_Grower, indentModel.getGrower());
        values.put(PlantingModel.Col_PLOTVillage, indentModel.getPLOTVillage());
        values.put(PlantingModel.Col_Irrigationmode, indentModel.getIrrigationmode());
        values.put(PlantingModel.Col_SupplyMode, indentModel.getSupplyMode());
        values.put(PlantingModel.Col_Harvesting, indentModel.getHarvesting());
        values.put(PlantingModel.Col_Equipment, indentModel.getEquipment());
        values.put(PlantingModel.Col_LandType, indentModel.getLandType());
        values.put(PlantingModel.Col_SeedType, indentModel.getSeedSource());
        values.put(PlantingModel.Col_BasalDose, indentModel.getBaselDose());
        values.put(PlantingModel.Col_SeedTreatment, indentModel.getSeedTreatment());
        values.put(PlantingModel.Col_SMethod, indentModel.getSmMethod());
        values.put(PlantingModel.Col_Dim1, indentModel.getDim1());
        values.put(PlantingModel.Col_Dim2, indentModel.getDim2());
        values.put(PlantingModel.Col_Dim3, indentModel.getDim3());
        values.put(PlantingModel.Col_Dim4, indentModel.getDim4());
        values.put(PlantingModel.Col_Area, indentModel.getArea());
        values.put(PlantingModel.Col_LAT1, indentModel.getLAT1());
        values.put(PlantingModel.Col_LON1, indentModel.getLON1());
        values.put(PlantingModel.Col_LAT2, indentModel.getLAT2());
        values.put(PlantingModel.Col_LON2, indentModel.getLON2());
        values.put(PlantingModel.Col_LAT3, indentModel.getLAT3());
        values.put(PlantingModel.Col_LON3, indentModel.getLON3());
        values.put(PlantingModel.Col_LAT4, indentModel.getLAT4());
        values.put(PlantingModel.Col_LON4, indentModel.getLON4());
        values.put(PlantingModel.Col_InsertLAT, indentModel.getInsertLAT());
        values.put(PlantingModel.Col_InsertLON, indentModel.getInsertLON());
        values.put(PlantingModel.Col_Image, indentModel.getImage());
        values.put(PlantingModel.Col_SuperviserCode, indentModel.getSuperviserCode());
        values.put(PlantingModel.Col_ID, indentModel.getId());
        values.put(PlantingModel.Col_SprayType, indentModel.getSprayType());
        values.put(PlantingModel.Col_PloughinType, indentModel.getPloughingType());
        values.put(PlantingModel.Col_MANUALAREA, indentModel.getManualArea());
        values.put(PlantingModel.Col_MOBILENO, indentModel.getMobileNumber());
        values.put(PlantingModel.Col_MDATE, indentModel.getmDate());
        values.put(PlantingModel.Col_VARIETY, indentModel.getVARIETY());
        values.put(PlantingModel.Col_Crop, indentModel.getCrop());
        values.put(PlantingModel.Col_PLANTINGTYPE, indentModel.getPlantingType());
        values.put(PlantingModel.Col_PLANTATION, indentModel.getPlantation());
        values.put(PlantingModel.Col_SEEDTT, indentModel.getSeedType());
        values.put(PlantingModel.Col_SEEDSET, indentModel.getSeedSetType());
        values.put(PlantingModel.Col_SOILTR, indentModel.getSoilTreatment());
        values.put(PlantingModel.Col_ROWTOROW, indentModel.getRowToRowDistance());
        values.put(PlantingModel.Col_ActualAreaType, indentModel.getActualAreaType());
        values.put(PlantingModel.Col_IMAREA, indentModel.getArea());
        values.put(PlantingModel.Col_SVillage, indentModel.getSeedVillage());
        values.put(PlantingModel.Col_SGrower, indentModel.getSeedGrower());
        values.put(PlantingModel.Col_STransporter, indentModel.getSeedTransporter());
        values.put(PlantingModel.Col_SDistance, indentModel.getSeedDistance());
        values.put(PlantingModel.Col_SSeedQty, indentModel.getSeedQuantity());
        values.put(PlantingModel.Col_Rate, indentModel.getSeedRate());
        values.put(PlantingModel.Col_OtherAmount, indentModel.getSeedOtherAmount());
        values.put(PlantingModel.Col_PayAmount, indentModel.getSeedPayAmount());
        values.put(PlantingModel.Col_PayMod, indentModel.getSeedPayMode());
        values.put(PlantingModel.Col_MillPurchy, indentModel.getMillPurchey());
        values.put(PlantingModel.Col_ServerStatus, indentModel.getServerStatus());
        values.put(PlantingModel.Col_Remark, "");
        values.put(PlantingModel.Col_CurrentDate, currentDt);
        values.put(PlantingModel.Col_PlotSerialNumber, indentModel.getPlotSerialNumber());
        values.put(PlantingModel.Col_ISIDEAL, indentModel.getIsIdeal());
        values.put(PlantingModel.Col_ISNURSERY, indentModel.getIsNursery());
        values.put(PlantingModel.Col_seed_bag_qty, indentModel.getSeedBagQty());

        // insert row
        long id = db.insert(PlantingModel.TABLE_NAME, null, values);
        // close db connection

        for (int i = 1; i < objectList.size(); i++) {
            Map<String, String> stringStringMapTemp = objectList.get(i);
            ContentValues valuesItem = new ContentValues();
            valuesItem.put(PlantingItemModel.Col_Code, stringStringMapTemp.get("itemid"));
            valuesItem.put(PlantingItemModel.Col_QTY, stringStringMapTemp.get("itemqty"));
            valuesItem.put(PlantingItemModel.Col_planting_id, "" + id);
            long idItem = db.insert(PlantingItemModel.TABLE_NAME, null, valuesItem);
        }

        for (int i = 1; i < equipmentObjectList.size(); i++) {
            Map<String, String> stringStringMapTemp = equipmentObjectList.get(i);
            ContentValues valuesEqup = new ContentValues();
            valuesEqup.put(PlantingEquipmentModel.Col_Code, stringStringMapTemp.get("itemid"));
            valuesEqup.put(PlantingEquipmentModel.Col_planting_id, "" + id);
            long idItem = db.insert(PlantingEquipmentModel.TABLE_NAME, null, valuesEqup);
        }

        db.close();

        SQLiteDatabase db1 = this.getReadableDatabase();
        db1.execSQL("UPDATE " + VillageModal.TABLE_NAME + " SET " + VillageModal.Col_max_plant + "='" + indentModel.getPlotSerialNumber() + "'" +
                " WHERE " + VillageModal.Col_code + "='" + indentModel.getPLOTVillage() + "'");
        db1.close();
        // return newly inserted row id
        return id;
    }

    public void truncatePlantingModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlantingModel.TABLE_NAME);
        db.execSQL(PlantingModel.CREATE_TABLE);
        db.close();
    }

    public long importPlantingModel(PlantingModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(PlantingModel.COLUMN_ID, indentModel.getColId());
        values.put(PlantingModel.Col_Village, indentModel.getVillage());
        values.put(PlantingModel.Col_Grower, indentModel.getGrower());
        values.put(PlantingModel.Col_PLOTVillage, indentModel.getPLOTVillage());
        values.put(PlantingModel.Col_Irrigationmode, indentModel.getIrrigationmode());
        values.put(PlantingModel.Col_SupplyMode, indentModel.getSupplyMode());
        values.put(PlantingModel.Col_Harvesting, indentModel.getHarvesting());
        values.put(PlantingModel.Col_Equipment, indentModel.getEquipment());
        values.put(PlantingModel.Col_LandType, indentModel.getLandType());
        values.put(PlantingModel.Col_SeedType, indentModel.getSeedSource());
        values.put(PlantingModel.Col_BasalDose, indentModel.getBaselDose());
        values.put(PlantingModel.Col_SeedTreatment, indentModel.getSeedTreatment());
        values.put(PlantingModel.Col_SMethod, indentModel.getSmMethod());
        values.put(PlantingModel.Col_Dim1, indentModel.getDim1());
        values.put(PlantingModel.Col_Dim2, indentModel.getDim2());
        values.put(PlantingModel.Col_Dim3, indentModel.getDim3());
        values.put(PlantingModel.Col_Dim4, indentModel.getDim4());
        values.put(PlantingModel.Col_Area, indentModel.getArea());
        values.put(PlantingModel.Col_LAT1, indentModel.getLAT1());
        values.put(PlantingModel.Col_LON1, indentModel.getLON1());
        values.put(PlantingModel.Col_LAT2, indentModel.getLAT2());
        values.put(PlantingModel.Col_LON2, indentModel.getLON2());
        values.put(PlantingModel.Col_LAT3, indentModel.getLAT3());
        values.put(PlantingModel.Col_LON3, indentModel.getLON3());
        values.put(PlantingModel.Col_LAT4, indentModel.getLAT4());
        values.put(PlantingModel.Col_LON4, indentModel.getLON4());
        values.put(PlantingModel.Col_InsertLAT, indentModel.getInsertLAT());
        values.put(PlantingModel.Col_InsertLON, indentModel.getInsertLON());
        values.put(PlantingModel.Col_Image, indentModel.getImage());
        values.put(PlantingModel.Col_SuperviserCode, indentModel.getSuperviserCode());
        values.put(PlantingModel.Col_ID, indentModel.getId());
        values.put(PlantingModel.Col_SprayType, indentModel.getSprayType());
        values.put(PlantingModel.Col_PloughinType, indentModel.getPloughingType());
        values.put(PlantingModel.Col_MANUALAREA, indentModel.getManualArea());
        values.put(PlantingModel.Col_MOBILENO, indentModel.getMobileNumber());
        values.put(PlantingModel.Col_MDATE, indentModel.getmDate());
        values.put(PlantingModel.Col_VARIETY, indentModel.getVARIETY());
        values.put(PlantingModel.Col_Crop, indentModel.getCrop());
        values.put(PlantingModel.Col_PLANTINGTYPE, indentModel.getPlantingType());
        values.put(PlantingModel.Col_PLANTATION, indentModel.getPlantation());
        values.put(PlantingModel.Col_SEEDTT, indentModel.getSeedType());
        values.put(PlantingModel.Col_SEEDSET, indentModel.getSeedSetType());
        values.put(PlantingModel.Col_SOILTR, indentModel.getSoilTreatment());
        values.put(PlantingModel.Col_ROWTOROW, indentModel.getRowToRowDistance());
        values.put(PlantingModel.Col_ActualAreaType, indentModel.getActualAreaType());
        values.put(PlantingModel.Col_IMAREA, indentModel.getArea());
        values.put(PlantingModel.Col_SVillage, indentModel.getSeedVillage());
        values.put(PlantingModel.Col_SGrower, indentModel.getSeedGrower());
        values.put(PlantingModel.Col_STransporter, indentModel.getSeedTransporter());
        values.put(PlantingModel.Col_SDistance, indentModel.getSeedDistance());
        values.put(PlantingModel.Col_SSeedQty, indentModel.getSeedQuantity());
        values.put(PlantingModel.Col_Rate, indentModel.getSeedRate());
        values.put(PlantingModel.Col_OtherAmount, indentModel.getSeedOtherAmount());
        values.put(PlantingModel.Col_PayAmount, indentModel.getSeedPayAmount());
        values.put(PlantingModel.Col_PayMod, indentModel.getSeedPayMode());
        values.put(PlantingModel.Col_MillPurchy, indentModel.getMillPurchey());
        values.put(PlantingModel.Col_PlotSerialNumber, indentModel.getPlotSerialNumber());
        values.put(PlantingModel.Col_ISIDEAL, indentModel.getIsIdeal());
        values.put(PlantingModel.Col_ISNURSERY, indentModel.getIsNursery());
        values.put(PlantingModel.Col_ServerStatus, indentModel.getServerStatus());
        values.put(PlantingModel.Col_Remark, "");
        values.put(PlantingModel.Col_CurrentDate, currentDt);

        // insert row
        long id = db.insert(PlantingModel.TABLE_NAME, null, values);
        // close db connection

        /*for(int i=1;i<objectList.size();i++)
        {
            Map<String, String> stringStringMapTemp = objectList.get(i);
            ContentValues valuesItem = new ContentValues();
            valuesItem.put(PlantingItemModel.Col_Code, stringStringMapTemp.get("itemid"));
            valuesItem.put(PlantingItemModel.Col_QTY, stringStringMapTemp.get("itemqty"));
            valuesItem.put(PlantingItemModel.Col_planting_id, ""+id);
            long idItem = db.insert(PlantingItemModel.TABLE_NAME, null, valuesItem);
        }

        for(int i=1;i<equipmentObjectList.size();i++)
        {
            Map<String, String> stringStringMapTemp = equipmentObjectList.get(i);
            ContentValues valuesEqup = new ContentValues();
            valuesEqup.put(PlantingEquipmentModel.Col_Code, stringStringMapTemp.get("itemid"));
            valuesEqup.put(PlantingEquipmentModel.Col_planting_id, ""+id);
            long idItem = db.insert(PlantingEquipmentModel.TABLE_NAME, null, valuesEqup);
        }*/

        db.close();
        // return newly inserted row id
        return id;
    }

    public void truncatePlanting() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlantingModel.TABLE_NAME);
        db.execSQL(PlantingModel.CREATE_TABLE);
        db.close();
    }

    public long saveDownloadedPlantingModel(PlantingModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PlantingModel.COLUMN_ID, indentModel.getColId());
        values.put(PlantingModel.Col_Village, indentModel.getVillage());
        values.put(PlantingModel.Col_Grower, indentModel.getGrower());
        values.put(PlantingModel.Col_PLOTVillage, indentModel.getPLOTVillage());
        values.put(PlantingModel.Col_Irrigationmode, indentModel.getIrrigationmode());
        values.put(PlantingModel.Col_SupplyMode, indentModel.getSupplyMode());
        values.put(PlantingModel.Col_Harvesting, indentModel.getHarvesting());
        values.put(PlantingModel.Col_Equipment, indentModel.getEquipment());
        values.put(PlantingModel.Col_LandType, indentModel.getLandType());
        values.put(PlantingModel.Col_SeedType, indentModel.getSeedSource());
        values.put(PlantingModel.Col_BasalDose, indentModel.getBaselDose());
        values.put(PlantingModel.Col_SeedTreatment, indentModel.getSeedTreatment());
        values.put(PlantingModel.Col_SMethod, indentModel.getSmMethod());
        values.put(PlantingModel.Col_Dim1, indentModel.getDim1());
        values.put(PlantingModel.Col_Dim2, indentModel.getDim2());
        values.put(PlantingModel.Col_Dim3, indentModel.getDim3());
        values.put(PlantingModel.Col_Dim4, indentModel.getDim4());
        values.put(PlantingModel.Col_Area, indentModel.getArea());
        values.put(PlantingModel.Col_LAT1, indentModel.getLAT1());
        values.put(PlantingModel.Col_LON1, indentModel.getLON1());
        values.put(PlantingModel.Col_LAT2, indentModel.getLAT2());
        values.put(PlantingModel.Col_LON2, indentModel.getLON2());
        values.put(PlantingModel.Col_LAT3, indentModel.getLAT3());
        values.put(PlantingModel.Col_LON3, indentModel.getLON3());
        values.put(PlantingModel.Col_LAT4, indentModel.getLAT4());
        values.put(PlantingModel.Col_LON4, indentModel.getLON4());
        values.put(PlantingModel.Col_InsertLAT, indentModel.getInsertLAT());
        values.put(PlantingModel.Col_InsertLON, indentModel.getInsertLON());
        values.put(PlantingModel.Col_Image, indentModel.getImage());
        values.put(PlantingModel.Col_SuperviserCode, indentModel.getSuperviserCode());
        values.put(PlantingModel.Col_ID, indentModel.getId());
        values.put(PlantingModel.Col_SprayType, indentModel.getSprayType());
        values.put(PlantingModel.Col_PloughinType, indentModel.getPloughingType());
        values.put(PlantingModel.Col_MANUALAREA, indentModel.getManualArea());
        values.put(PlantingModel.Col_MOBILENO, indentModel.getMobileNumber());
        values.put(PlantingModel.Col_MDATE, indentModel.getmDate());
        values.put(PlantingModel.Col_VARIETY, indentModel.getVARIETY());
        values.put(PlantingModel.Col_Crop, indentModel.getCrop());
        values.put(PlantingModel.Col_PLANTINGTYPE, indentModel.getPlantingType());
        values.put(PlantingModel.Col_PLANTATION, indentModel.getPlantation());
        values.put(PlantingModel.Col_SEEDTT, indentModel.getSeedType());
        values.put(PlantingModel.Col_SEEDSET, indentModel.getSeedSetType());
        values.put(PlantingModel.Col_SOILTR, indentModel.getSoilTreatment());
        values.put(PlantingModel.Col_ROWTOROW, indentModel.getRowToRowDistance());
        values.put(PlantingModel.Col_ActualAreaType, indentModel.getActualAreaType());
        values.put(PlantingModel.Col_IMAREA, indentModel.getArea());
        values.put(PlantingModel.Col_SVillage, indentModel.getSeedVillage());
        values.put(PlantingModel.Col_SGrower, indentModel.getSeedGrower());
        values.put(PlantingModel.Col_STransporter, indentModel.getSeedTransporter());
        values.put(PlantingModel.Col_SDistance, indentModel.getSeedDistance());
        values.put(PlantingModel.Col_SSeedQty, indentModel.getSeedQuantity());
        values.put(PlantingModel.Col_Rate, indentModel.getSeedRate());
        values.put(PlantingModel.Col_OtherAmount, indentModel.getSeedOtherAmount());
        values.put(PlantingModel.Col_PayAmount, indentModel.getSeedPayAmount());
        values.put(PlantingModel.Col_PayMod, indentModel.getSeedPayMode());
        values.put(PlantingModel.Col_MillPurchy, indentModel.getMillPurchey());
        values.put(PlantingModel.Col_PlotSerialNumber, indentModel.getPlotSerialNumber());
        values.put(PlantingModel.Col_ISIDEAL, indentModel.getIsIdeal());
        values.put(PlantingModel.Col_ISNURSERY, indentModel.getIsNursery());
        values.put(PlantingModel.Col_ServerStatus, indentModel.getServerStatus());
        values.put(PlantingModel.Col_Remark, "");

        // insert row
        long id = db.insert(PlantingModel.TABLE_NAME, null, values);

        db.close();
        // return newly inserted row id
        return id;
    }

    public List<PlantingModel> getPlantingModel(String serverStatus, String ItemId, String villCode, String growerCode, String plotVillCode) {
        List<PlantingModel> indentModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PlantingModel.TABLE_NAME + " WHERE 1=1 ";
        if (serverStatus.length() > 0) {
            selectQuery += " AND " + PlantingModel.Col_ServerStatus + " LIKE '%" + serverStatus + "%' ";
        }
        if (ItemId.length() > 0) {
            selectQuery += " AND " + PlantingModel.COLUMN_ID + "='" + ItemId + "' ";
        }
        if (villCode.length() > 0) {
            selectQuery += " AND " + PlantingModel.Col_Village + "='" + villCode + "' ";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + PlantingModel.Col_Grower + "='" + growerCode + "' ";
        }
        if (plotVillCode.length() > 0) {
            selectQuery += " AND " + PlantingModel.Col_PLOTVillage + "='" + plotVillCode + "' ";
        }
        selectQuery += "ORDER BY " + PlantingModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlantingModel sprayItemModel = new PlantingModel();
                sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingModel.COLUMN_ID)));
                sprayItemModel.setVillage(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Village)));
                sprayItemModel.setGrower(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Grower)));
                sprayItemModel.setPLOTVillage(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLOTVillage)));
                sprayItemModel.setIrrigationmode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Irrigationmode)));
                sprayItemModel.setSupplyMode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SupplyMode)));
                sprayItemModel.setHarvesting(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Harvesting)));
                sprayItemModel.setEquipment(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Equipment)));
                sprayItemModel.setLandType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LandType)));
                sprayItemModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedType)));
                sprayItemModel.setBaselDose(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_BasalDose)));
                sprayItemModel.setSeedTreatment(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SeedTreatment)));
                sprayItemModel.setSmMethod(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SMethod)));
                sprayItemModel.setDim1(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim1)));
                sprayItemModel.setDim2(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim2)));
                sprayItemModel.setDim3(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim3)));
                sprayItemModel.setDim4(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Dim4)));
                sprayItemModel.setArea(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Area)));
                sprayItemModel.setLAT1(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT1)));
                sprayItemModel.setLON1(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON1)));
                sprayItemModel.setLAT2(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT2)));
                sprayItemModel.setLON2(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON2)));
                sprayItemModel.setLAT3(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT3)));
                sprayItemModel.setLON3(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON3)));
                sprayItemModel.setLAT4(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LAT4)));
                sprayItemModel.setLON4(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_LON4)));
                sprayItemModel.setInsertLAT(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_InsertLAT)));
                sprayItemModel.setInsertLON(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_InsertLON)));
                sprayItemModel.setImage(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Image)));
                sprayItemModel.setSuperviserCode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SuperviserCode)));
                sprayItemModel.setId(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ID)));
                sprayItemModel.setSprayType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SprayType)));
                sprayItemModel.setPloughingType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PloughinType)));
                sprayItemModel.setManualArea(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_MANUALAREA)));
                sprayItemModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_MOBILENO)));
                sprayItemModel.setmDate(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_MDATE)));
                sprayItemModel.setVARIETY(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_VARIETY)));
                sprayItemModel.setCrop(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Crop)));
                sprayItemModel.setPlantingType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLANTINGTYPE)));
                sprayItemModel.setPlantation(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PLANTATION)));
                sprayItemModel.setSeedType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SEEDTT)));
                sprayItemModel.setSeedSetType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SEEDSET)));
                sprayItemModel.setSoilTreatment(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SOILTR)));
                sprayItemModel.setRowToRowDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ROWTOROW)));
                sprayItemModel.setActualAreaType(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ActualAreaType)));
                sprayItemModel.setManualArea(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_IMAREA)));
                sprayItemModel.setSeedVillage(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SVillage)));
                sprayItemModel.setSeedGrower(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SGrower)));
                sprayItemModel.setSeedTransporter(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_STransporter)));
                sprayItemModel.setSeedDistance(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SDistance)));
                sprayItemModel.setSeedQuantity(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_SSeedQty)));
                sprayItemModel.setSeedRate(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Rate)));
                sprayItemModel.setSeedOtherAmount(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_OtherAmount)));
                sprayItemModel.setSeedPayAmount(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PayAmount)));
                sprayItemModel.setSeedPayMode(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PayMod)));
                sprayItemModel.setMillPurchey(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_MillPurchy)));
                sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ServerStatus)));
                sprayItemModel.setRemark(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_Remark)));
                sprayItemModel.setCurrentDate(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_CurrentDate)));
                sprayItemModel.setPlotSerialNumber(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_PlotSerialNumber)));
                sprayItemModel.setIsIdeal(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ISIDEAL)));
                sprayItemModel.setIsNursery(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_ISNURSERY)));
                sprayItemModel.setSeedBagQty(cursor.getString(cursor.getColumnIndex(PlantingModel.Col_seed_bag_qty)));
                indentModelList.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return indentModelList;
    }


    public void truncatePlantingEquipmentModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlantingEquipmentModel.TABLE_NAME);
        db.execSQL(PlantingEquipmentModel.CREATE_TABLE);
        db.close();
    }

    public long importPlantingEquipmentModel(PlantingEquipmentModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(PlantingEquipmentModel.COLUMN_ID, indentModel.getColId());
        values.put(PlantingEquipmentModel.Col_Code, indentModel.getCode());
        values.put(PlantingEquipmentModel.Col_planting_id, indentModel.getPlantingCode());
        // insert row
        long id = db.insert(PlantingEquipmentModel.TABLE_NAME, null, values);
        // close db connection
        /*for(int i=1;i<objectList.size();i++)
        {
            Map<String, String> stringStringMapTemp = objectList.get(i);
            ContentValues valuesItem = new ContentValues();
            valuesItem.put(PlantingItemModel.Col_Code, stringStringMapTemp.get("itemid"));
            valuesItem.put(PlantingItemModel.Col_QTY, stringStringMapTemp.get("itemqty"));
            valuesItem.put(PlantingItemModel.Col_planting_id, ""+id);
            long idItem = db.insert(PlantingItemModel.TABLE_NAME, null, valuesItem);
        }
        */

        db.close();
        // return newly inserted row id
        return id;
    }


    public List<PlantingEquipmentModel> getPlantingEquipmentModel(String plantingId) {
        List<PlantingEquipmentModel> indentModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PlantingEquipmentModel.TABLE_NAME + " WHERE 1=1 ";
        if (plantingId.length() > 0) {
            selectQuery += " AND " + PlantingEquipmentModel.Col_planting_id + "='" + plantingId + "' ";
        }
        selectQuery += "ORDER BY " + PlantingEquipmentModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlantingEquipmentModel sprayItemModel = new PlantingEquipmentModel();
                sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingEquipmentModel.COLUMN_ID)));
                sprayItemModel.setCode(cursor.getString(cursor.getColumnIndex(PlantingEquipmentModel.Col_Code)));
                sprayItemModel.setPlantingCode(cursor.getString(cursor.getColumnIndex(PlantingEquipmentModel.Col_planting_id)));
                indentModelList.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return indentModelList;
    }

    public void truncatePlantingItemModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlantingItemModel.TABLE_NAME);
        db.execSQL(PlantingItemModel.CREATE_TABLE);
        db.close();
    }

    public long importPlantingItemModel(PlantingItemModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(PlantingItemModel.COLUMN_ID, indentModel.getColId());
        values.put(PlantingItemModel.Col_Code, indentModel.getCode());
        values.put(PlantingItemModel.Col_QTY, indentModel.getQty());
        values.put(PlantingItemModel.Col_planting_id, indentModel.getPlantingCode());
        // insert row
        long id = db.insert(PlantingItemModel.TABLE_NAME, null, values);
        // close db connection

        db.close();
        // return newly inserted row id
        return id;
    }

    public List<PlantingItemModel> getPlantingItemModel(String plantingId) {
        List<PlantingItemModel> indentModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PlantingItemModel.TABLE_NAME + " WHERE 1=1 ";
        if (plantingId.length() > 0) {
            selectQuery += " AND " + PlantingItemModel.Col_planting_id + "='" + plantingId + "' ";
        }
        selectQuery += "ORDER BY " + PlantingItemModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlantingItemModel sprayItemModel = new PlantingItemModel();
                sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlantingItemModel.COLUMN_ID)));
                sprayItemModel.setCode(cursor.getString(cursor.getColumnIndex(PlantingItemModel.Col_Code)));
                sprayItemModel.setQty(cursor.getString(cursor.getColumnIndex(PlantingItemModel.Col_QTY)));
                sprayItemModel.setPlantingCode(cursor.getString(cursor.getColumnIndex(PlantingItemModel.Col_planting_id)));
                indentModelList.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return indentModelList;
    }

    public void updateServerStatusPlanting(String remark, String id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + PlantingModel.TABLE_NAME + " SET " + PlantingModel.Col_ServerStatus + "='" + status + "', " +
                PlantingModel.Col_Remark + "='" + remark.replace("'", "") + "' WHERE " + PlantingModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public void resetServerStatusPlanting() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + PlantingModel.TABLE_NAME + " SET " + PlantingModel.Col_ServerStatus + "='No' WHERE " + PlantingModel.Col_ServerStatus + "='FAILED'");
        db.execSQL("UPDATE " + PlantingModel.TABLE_NAME + " SET " + PlantingModel.Col_ServerStatus + "='No' WHERE " + PlantingModel.Col_ServerStatus + "='Failed'");
        db.close();
    }


    public long insertSprayItem(SprayItemModel sprayItemModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SprayItemModel.Col_item, sprayItemModel.getItem());
        values.put(SprayItemModel.Col_item_name, sprayItemModel.getItemName());
        values.put(SprayItemModel.Col_unit, sprayItemModel.getUnit());
        values.put(SprayItemModel.Col_qty, sprayItemModel.getQty());
        values.put(SprayItemModel.Col_area, sprayItemModel.getArea());
        // insert row
        long id = db.insert(SprayItemModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }


    public List<SprayItemModel> getSprayItem(String ItemId) {
        List<SprayItemModel> loginUserDetailsses = new ArrayList<>();
        String selectQuery = "";
        if (ItemId.length() == 0) {
            selectQuery = "SELECT  * FROM " + SprayItemModel.TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + SprayItemModel.TABLE_NAME + " WHERE " + SprayItemModel.Col_item + "='" + ItemId + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SprayItemModel sprayItemModel = new SprayItemModel();
                sprayItemModel.setId(cursor.getInt(cursor.getColumnIndex(SprayItemModel.COLUMN_ID)));
                sprayItemModel.setItem(cursor.getString(cursor.getColumnIndex(SprayItemModel.Col_item)));
                sprayItemModel.setItemName(cursor.getString(cursor.getColumnIndex(SprayItemModel.Col_item_name)));
                sprayItemModel.setUnit(cursor.getString(cursor.getColumnIndex(SprayItemModel.Col_unit)));
                sprayItemModel.setQty(cursor.getString(cursor.getColumnIndex(SprayItemModel.Col_qty)));
                sprayItemModel.setArea(cursor.getString(cursor.getColumnIndex(SprayItemModel.Col_area)));
                loginUserDetailsses.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return loginUserDetailsses;
    }

    public void deleteSprayItem(String id) {
        if (id.length() == 0) {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + SprayItemModel.TABLE_NAME);
            db.close();
        } else {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DELETE FROM " + SprayItemModel.TABLE_NAME + " WHERE " + SprayItemModel.Col_item + "=" + id);
            db.close();
        }

    }


    public long insertPlotActivitySaveModel(PlotActivitySaveModel indentModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.execSQL(PlotActivitySaveModel.CREATE_TABLE);
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        values.put(PlotActivitySaveModel.plot_type, indentModel.getPlotType());
        values.put(PlotActivitySaveModel.vill_code, indentModel.getVillageCode());
        values.put(PlotActivitySaveModel.grower_code, indentModel.getGrwerCode());
        values.put(PlotActivitySaveModel.plot_serial_number, indentModel.getPlotSerialNumber());
        values.put(PlotActivitySaveModel.plot_village_code, indentModel.getPlotVillage());
        values.put(PlotActivitySaveModel.col_area, indentModel.getArea());
        values.put(PlotActivitySaveModel.col_SURTYPE, indentModel.getSurveyType());
        values.put(PlotActivitySaveModel.col_OLDSEAS, indentModel.getOldSeason());
        values.put(PlotActivitySaveModel.col_OLDGHID, indentModel.getOldGhid());
        values.put(PlotActivitySaveModel.json_array, indentModel.getJsonArrayData());
        values.put(PlotActivitySaveModel.server_status, indentModel.getServerStatus());
        values.put(PlotActivitySaveModel.col_remark, indentModel.getRemark());
        values.put(PlotActivitySaveModel.current_date, indentModel.getCurrentDate());
        values.put(PlotActivitySaveModel.meeting_type, indentModel.getMeetingType());
        values.put(PlotActivitySaveModel.meeting_name, indentModel.getMeetingName());
        values.put(PlotActivitySaveModel.meeting_number, indentModel.getMeetingNumber());
        values.put(PlotActivitySaveModel.col_mobile_number, indentModel.getMobileNumber());
        // insert row
        long id = db.insert(PlotActivitySaveModel.TABLE_NAME, null, values);
        // close db connection

        db.close();
        return id;
    }

    public void truncatePlotActivitySaveModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlotActivitySaveModel.TABLE_NAME);
        db.execSQL(PlotActivitySaveModel.CREATE_TABLE);
        db.close();
    }

    public List<PlotActivitySaveModel> getPlotActivitySaveModel(String serverStatus) {
        List<PlotActivitySaveModel> indentModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PlotActivitySaveModel.TABLE_NAME + " WHERE 1=1 ";
        if (serverStatus.length() > 0) {
            selectQuery += " AND " + PlotActivitySaveModel.server_status + " LIKE '%" + serverStatus + "%' ";
        }
        selectQuery += "ORDER BY " + PlotActivitySaveModel.COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotActivitySaveModel sprayItemModel = new PlotActivitySaveModel();
                sprayItemModel.setColId(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.COLUMN_ID)));
                sprayItemModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.plot_type)));
                sprayItemModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.vill_code)));
                sprayItemModel.setGrwerCode(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.grower_code)));
                sprayItemModel.setPlotSerialNumber(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.plot_serial_number)));
                sprayItemModel.setPlotVillage(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.plot_village_code)));
                sprayItemModel.setArea(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_area)));
                sprayItemModel.setJsonArrayData(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.json_array)));
                sprayItemModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.server_status)));
                sprayItemModel.setRemark(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_remark)));
                sprayItemModel.setCurrentDate(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.current_date)));
                sprayItemModel.setSurveyType(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_SURTYPE)));
                sprayItemModel.setOldSeason(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_OLDSEAS)));
                sprayItemModel.setOldGhid(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_OLDGHID)));
                sprayItemModel.setMeetingType(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.meeting_type)));
                sprayItemModel.setMeetingName(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.meeting_name)));
                sprayItemModel.setMeetingNumber(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.meeting_number)));
                sprayItemModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PlotActivitySaveModel.col_mobile_number)));
                indentModelList.add(sprayItemModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return indentModelList;
    }

    public void updateServerStatusPlotActivitySaveModel(String remark, String id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + PlotActivitySaveModel.TABLE_NAME + " SET " + PlotActivitySaveModel.server_status + "='" + status + "', " +
                PlotActivitySaveModel.col_remark + "='" + remark + "' WHERE " + PlotActivitySaveModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public void resetServerStatusPlotActivitySaveModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + PlotActivitySaveModel.TABLE_NAME + " SET " + PlotActivitySaveModel.server_status + "='No' WHERE " + PlotActivitySaveModel.server_status + "='FAILED'");
        db.execSQL("UPDATE " + PlotActivitySaveModel.TABLE_NAME + " SET " + PlotActivitySaveModel.server_status + "='No' WHERE " + PlotActivitySaveModel.server_status + "='Failed'");
        db.close();
    }


    public void truncateLocationDataModel() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + LocationDataModel.TABLE_NAME);
        db.close();
    }

    public List<LocationDataModel> getLocationDataModel(String id) {
        List<LocationDataModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LocationDataModel.TABLE_NAME + " WHERE 1=1 ";
        if (id.length() > 0) {
            selectQuery += " AND " + LocationDataModel.Col_SrNo + "='" + id + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(LocationDataModel.CREATE_TABLE);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LocationDataModel modeDataModel = new LocationDataModel();
                modeDataModel.setId(cursor.getInt(cursor.getColumnIndex(LocationDataModel.COLUMN_ID)));
                modeDataModel.setSerialNumber(cursor.getInt(cursor.getColumnIndex(LocationDataModel.Col_SrNo)));
                modeDataModel.setLat(cursor.getDouble(cursor.getColumnIndex(LocationDataModel.Col_Lat)));
                modeDataModel.setLng(cursor.getDouble(cursor.getColumnIndex(LocationDataModel.Col_Lng)));
                modeDataModel.setDistance(cursor.getDouble(cursor.getColumnIndex(LocationDataModel.Col_Distance)));
                modeDataModel.setAccuracy(cursor.getDouble(cursor.getColumnIndex(LocationDataModel.Col_Accuracy)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public long insertLocationDataModel(LocationDataModel modeDataModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(LocationDataModel.Col_SrNo, modeDataModel.getSerialNumber());
        values.put(LocationDataModel.Col_Lat, modeDataModel.getLat());
        values.put(LocationDataModel.Col_Lng, modeDataModel.getLng());
        values.put(LocationDataModel.Col_Distance, modeDataModel.getDistance());
        values.put(LocationDataModel.Col_Accuracy, modeDataModel.getAccuracy());
        long id = db.insert(LocationDataModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void deleteLocationDataModel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + LocationDataModel.TABLE_NAME + " WHERE " + LocationDataModel.Col_SrNo + "='" + id + "'");
        db.close();
    }

    public void updateLocationDataModelDistance(double distance, int serial) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("UPDATE " + LocationDataModel.TABLE_NAME + " SET " + LocationDataModel.Col_Distance + "='" + distance + "' WHERE " + LocationDataModel.Col_SrNo + "='" + serial + "'");
            db.close();
        } catch (Exception e) {
        }
    }

    public List<PlotFarmerShareModel> getIdentifyPlotSurveyModelByLatLng(double lats, double lngs, String villCode) throws Exception {
        List<PlotFarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PlotFarmerShareModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareModel.PLOT_TABLE_NAME +
                " ON " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_surveyId + "=" +
                PlotFarmerShareModel.PLOT_TABLE_NAME + "." + PlotFarmerShareModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
        if (villCode.length() > 0) {
            selectQuery += " AND " + PlotFarmerShareModel.SHARE_TABLE_NAME + "." + PlotFarmerShareModel.Col_villageCode + "='" + villCode + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                List<LatLng> latLngList = new ArrayList<>();
                if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                }
                if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                }
                if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                    latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                }
                LatLng latlng = new LatLng(lats, lngs);
                Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                if (inside) {
                    list.add(modeDataModel);
                }
            } while (cursor.moveToNext());
        }

        if (list.size() == 0) {
            selectQuery = "SELECT * FROM " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + " JOIN " + PlotFarmerShareOldModel.PLOT_TABLE_NAME +
                    " ON " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_surveyId + "=" +
                    PlotFarmerShareOldModel.PLOT_TABLE_NAME + "." + PlotFarmerShareOldModel.PLOT_COLUMN_ID + " WHERE 1=1 ";
            if (villCode.length() > 0) {
                selectQuery += " AND " + PlotFarmerShareOldModel.SHARE_TABLE_NAME + "." + PlotFarmerShareOldModel.Col_villageCode + "='" + villCode + "'";
            }

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    PlotFarmerShareModel modeDataModel = new PlotFarmerShareModel();
                    modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.SHARE_COLUMN_ID)));
                    modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_surveyId)));
                    modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_srNo)));
                    modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_villageCode)));
                    modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerCode)));
                    modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerName)));
                    modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerFatherName)));
                    modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_growerAadhar_number)));
                    modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_share)));
                    modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_sup_code)));
                    modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_curDate)));
                    modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatus)));
                    modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ServerStatusRemark)));


                    modeDataModel.setPlotSrNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_PlotSrNo)));
                    modeDataModel.setKhashraNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_khashraNo)));
                    modeDataModel.setGataNo(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_gataNo)));
                    modeDataModel.setPlotVillageCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotvillageCode)));
                    modeDataModel.setAreaMeter(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaMeter)));
                    modeDataModel.setAreaHectare(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_areaHectare)));
                    modeDataModel.setMixCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_mixCrop)));
                    modeDataModel.setInsect(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insect)));
                    modeDataModel.setSeedSource(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_seedSource)));
                    modeDataModel.setAadharNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_aadharNumber)));
                    modeDataModel.setPlantMethod(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantMethod)));
                    modeDataModel.setCropCondition(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_cropCondition)));
                    modeDataModel.setDisease(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_disease)));
                    modeDataModel.setPlantDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plantDate)));
                    modeDataModel.setIrrigation(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_irrigation)));
                    modeDataModel.setSoilType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_soilType)));
                    modeDataModel.setLandType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_landType)));
                    modeDataModel.setBorderCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_borderCrop)));
                    modeDataModel.setPlotType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_plotType)));
                    modeDataModel.setGhashtiNumber(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_ghashtiNumber)));
                    modeDataModel.setInterCrop(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_interCrop)));
                    modeDataModel.setEastNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lat)));
                    modeDataModel.setEastNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_lng)));
                    modeDataModel.setEastNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_distance)));
                    modeDataModel.setEastNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastNorth_accuracy)));
                    modeDataModel.setWestNorthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lat)));
                    modeDataModel.setWestNorthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_lng)));
                    modeDataModel.setWestNorthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_distance)));
                    modeDataModel.setWestNorthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westNorth_accuracy)));
                    modeDataModel.setEastSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lat)));
                    modeDataModel.setEastSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_lng)));
                    modeDataModel.setEastSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_distance)));
                    modeDataModel.setEastSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_eastSouth_accuracy)));
                    modeDataModel.setWestSouthLat(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lat)));
                    modeDataModel.setWestSouthLng(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_lng)));
                    modeDataModel.setWestSouthDistance(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_distance)));
                    modeDataModel.setWestSouthAccuracy(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_westSouth_accuracy)));
                    modeDataModel.setVarietyCode(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_varietyCode)));
                    modeDataModel.setCaneType(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_caneType)));
                    modeDataModel.setInsertDate(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_insertDate)));
                    //modeDataModel.setOldPlotId(cursor.getString(cursor.getColumnIndex(PlotFarmerShareModel.Col_old_plot_id)));
                    List<LatLng> latLngList = new ArrayList<>();
                    if (Double.parseDouble(modeDataModel.getEastNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastNorthLat()), Double.parseDouble(modeDataModel.getEastNorthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getEastSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getEastSouthLat()), Double.parseDouble(modeDataModel.getEastSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestSouthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestSouthLat()), Double.parseDouble(modeDataModel.getWestSouthLng())));
                    }
                    if (Double.parseDouble(modeDataModel.getWestNorthLat()) > 0) {
                        latLngList.add(new LatLng(Double.parseDouble(modeDataModel.getWestNorthLat()), Double.parseDouble(modeDataModel.getWestNorthLng())));
                    }
                    LatLng latlng = new LatLng(lats, lngs);
                    Boolean inside = PolyUtil.containsLocation(latlng, latLngList, true);
                    if (inside) {
                        list.add(modeDataModel);
                    }
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return list;
    }

    public boolean columnExistsInTable(String table, String columnToCheck) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            //query a row. don't acquire db lock
            cursor = db.rawQuery("SELECT * FROM " + table + " LIMIT 0", null);
            // getColumnIndex()  will return the index of the column
            //in the table if it exists, otherwise it will return -1
            if (cursor.getColumnIndex(columnToCheck) != -1) {
                //great, the column exists
                return false;
            } else {
                //sorry, the column does not exist
                return true;
            }
        } catch (SQLiteException Exp) {
            //Something went wrong with SQLite.
            //If the table exists and your query was good,
            //the problem is likely that the column doesn't exist in the table.
            return false;
        } finally {
            //close the db  if you no longer need it
            if (db != null) db.close();
            //close the cursor
            if (cursor != null) cursor.close();
        }
    }

    public boolean checkTable(String table) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            //query a row. don't acquire db lock
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "';", null);
            // getColumnIndex()  will return the index of the column
            //in the table if it exists, otherwise it will return -1
            if (cursor.moveToFirst()) {
                //great, the column exists
                return true;
            } else {
                //sorry, the column does not exist
                return false;
            }
        } catch (SQLiteException Exp) {
            //Something went wrong with SQLite.
            //If the table exists and your query was good,
            //the problem is likely that the column doesn't exist in the table.
            return false;
        } finally {
            //close the db  if you no longer need it
            if (db != null) db.close();
            //close the cursor
            if (cursor != null) cursor.close();
        }
    }

    public void alterTableAddColumn(String table, String columnName) {
        SQLiteDatabase dbsql = this.getReadableDatabase();
        dbsql.execSQL("ALTER TABLE " + table + " ADD COLUMN " + columnName + " TEXT;");
        dbsql.close();
    }

    // Cane development end


    ////////////----------------------------SAVEGROWERDOCUMENT------------//////////////


    public long insertSaveGrowerDocumentModel(String VillageCode, String VillageName, String GrowerCode, String GrowerName, String GrowerUnitCode, String GrowerFather, String GrowerMobile, byte[] photo_1, byte[] photo_2, byte[] signature, byte[] Thumb, int FkAgriInput) {
        // get writable database as we want to write data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE, VillageCode);
        values.put(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME, VillageName);
        values.put(SaveGrowerDocumentModel.AGRI_GROWER_CODE, GrowerCode);
        values.put(SaveGrowerDocumentModel.AGRI_GROWER_NAME, GrowerName);
        values.put(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE, GrowerUnitCode);
        values.put(SaveGrowerDocumentModel.AGRI_GROWER_FATHER, GrowerFather);
        values.put(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE, GrowerMobile);
        values.put(SaveGrowerDocumentModel.AGRI_COLUMN_PHOTO_1, photo_1);
        values.put(SaveGrowerDocumentModel.AGRI_COLUMN_PHOTO_2, photo_2);
        values.put(SaveGrowerDocumentModel.AGRI_COLUMN_SIGNATURE, signature);
        values.put(SaveGrowerDocumentModel.AGRI_COLUMN_THUMB_IMPRESSION, Thumb);
        values.put(SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS, "No");
        values.put(SaveGrowerDocumentModel.AGRI_CREATED_AT, currentDt);
        values.put(SaveGrowerDocumentModel.COLUMN_FK_AGRI_INPUT, FkAgriInput);
        // insert row  2022-03-01
        long id = db.insert(SaveGrowerDocumentModel.TABLE_NAME, null, values);
        //if(id==-1)return false(Table is not created);elsere turn true(Table is created);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<SaveGrowerDocumentModel> getAllGrowerInputData(String villageCode, String growerCode) {
        List<SaveGrowerDocumentModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + "(" + SaveGrowerDocumentModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + SaveGrowerDocumentModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveGrowerDocumentModel agriInputFormModel = new SaveGrowerDocumentModel();
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public List<SaveGrowerDocumentModel> getAllGrowerInputData() {
        List<SaveGrowerDocumentModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " ORDER BY " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveGrowerDocumentModel agriInputFormModel = new SaveGrowerDocumentModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }


    public List<SaveGrowerDocumentModel> getFilterGrowerInputData(String DateStr, String toDateSTr, String VillageCode, String GrowerCode) {
        List<SaveGrowerDocumentModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery;
        if (DateStr.length() == 0) {
            selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + SaveGrowerDocumentModel.AGRI_GROWER_NAME + " LIKE '%" + GrowerCode + "%' AND " + SaveGrowerDocumentModel.AGRI_VILLAGE_NAME + " LIKE '%" + VillageCode + "%' ORDER BY " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + SaveGrowerDocumentModel.AGRI_CREATED_AT + ">='" + DateStr + "' AND " + SaveGrowerDocumentModel.AGRI_CREATED_AT + "<='" + toDateSTr + "' AND " + SaveGrowerDocumentModel.AGRI_GROWER_NAME + " LIKE '%" + GrowerCode + "%'  AND " + SaveGrowerDocumentModel.AGRI_VILLAGE_NAME + " LIKE '%" + VillageCode + "%' ORDER BY " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        }
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveGrowerDocumentModel agriInputFormModel = new SaveGrowerDocumentModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public List<SaveGrowerDocumentModel> getAllGrowerInputDataById(String id) {
        List<SaveGrowerDocumentModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + "=" + id;
        Log.d(TAG, "AddMaterialDataModel: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveGrowerDocumentModel agriInputFormModel = new SaveGrowerDocumentModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                //agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                //agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModels.add(agriInputFormModel);
            }
            while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public Bitmap getRetrivePhoto3(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + "(" + SaveGrowerDocumentModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + SaveGrowerDocumentModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_PHOTO_1));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    //For Retriving photoe2 from the database using id
    public Bitmap getRetrivePhoto4(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + "(" + SaveGrowerDocumentModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + SaveGrowerDocumentModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_PHOTO_2));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    //For Retriving Signature using id
    public Bitmap getRetriveSignature1(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + "(" + SaveGrowerDocumentModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + SaveGrowerDocumentModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_SIGNATURE));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    public Bitmap getRetriveThumb1(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " + "(" + SaveGrowerDocumentModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + SaveGrowerDocumentModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_THUMB_IMPRESSION));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }


    public List<SaveGrowerDocumentModel> getAllGrowerInputPendingDataById() {
        List<SaveGrowerDocumentModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME + " WHERE " +
                SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='No'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveGrowerDocumentModel agriInputFormModel = new SaveGrowerDocumentModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                //agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                //agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));

            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public void updateSaveGrowerDocumentModel(String remark, String id, String status) {
        String query = "UPDATE " + SaveGrowerDocumentModel.TABLE_NAME + " SET " + SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='" + status + "', " +
                SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_REMARK + "='" + remark.replace("'", "") + "' WHERE " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + "='" + id + "'";
        SQLiteDatabase dbe = this.getReadableDatabase();
        dbe.execSQL(query);
        dbe.close();
    }

    public void resentSaveGrowerDocumentModel(String remark, String id, String status) {
        String query = "UPDATE " + SaveGrowerDocumentModel.TABLE_NAME + " SET " + SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='" + status + "', " +
                SaveGrowerDocumentModel.AGRI_COLUMN_SERVER_UPLOAD_REMARK + "='" + remark.replace("'", "") + "' WHERE " + SaveGrowerDocumentModel.COLUMN_AGRI_INPUT_FORM_ID + "='" + id + "'";
        SQLiteDatabase dbe = this.getReadableDatabase();
        dbe.execSQL(query);
        dbe.close();
    }


    /* public long insertSaveGrowerDocumentModel(SaveGrowerDocumentModel model) {
          // get writable database as we want to write data
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues values = new ContentValues();
          // `id` and `timestamp` will be inserted automatically.
          // no need to add them
          values.put(SaveGrowerDocumentModel.COLUMN_GROWER_DOCUMENT_FORM_ID, model.getGrowerDocumentId());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_VILLAGE_CODE, model.getGrowerDomumrntVillageCode());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_VILLAGE_NAME, model.getGrowerDomumrntVillageName());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_CODE, model.getGrowerDomumrntGrowerCode());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_NAME, model.getGrowerDomumrntGrowerName());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_UNIT_CODE, model.getGrowerDomumrntGrowerUnitCode());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_FATHER, model.getGrowerDomumrntGrowerFather());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_MOBILE, model.getGrowerDomumrntGrowerMobile());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_PHOTO_1, "" + model.getGrowerDomumrntPhoto_1());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_PHOTO_2, "" + model.getGrowerDomumrntPhoto_2());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SIGNATURE, model.getGrowerDomumrntSignature());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_THUMB_IMPRESSION, model.getGrowerDomumrntThumb());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SERVER_UPLOAD_STATUS, model.getServer_status());
          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SERVER_UPLOAD_REMARK, model.getServer_remark());
          values.put(SaveGrowerDocumentModel.COLUMN_FK_GROWER_DOCUMENT, model.getACKID());

          values.put(SaveGrowerDocumentModel.GROWER_DOCUMENT_CREATED_AT, model.getCreated_at());


          // insert row
          long id = db.insert(SaveGrowerDocumentModel.TABLE_NAME, null, values);
          // close db connection
          db.close();
          // return newly inserted row id
          return id;
      }

      public List<SaveGrowerDocumentModel> getSaveGrowerDocumentModel(String villageCode, String growerCode) {
          List<SaveGrowerDocumentModel> allGrowerData = new ArrayList<>();
          String selectQuery = "SELECT  * FROM " + SaveGrowerDocumentModel.TABLE_NAME;
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor cursor = db.rawQuery(selectQuery, null);
          if (cursor.moveToFirst()) {
              do {
                  SaveGrowerDocumentModel singleData = new SaveGrowerDocumentModel();
                  singleData.setGrowerDocumentId(cursor.getInt(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_GROWER_DOCUMENT_FORM_ID)));
                  singleData.setGrowerDomumrntVillageCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_VILLAGE_CODE)));
                  singleData.setGrowerDomumrntVillageName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_VILLAGE_NAME)));
                  singleData.setGrowerDomumrntGrowerCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_CODE)));
                  singleData.setGrowerDomumrntGrowerName(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_NAME)));
                  singleData.setGrowerDomumrntGrowerUnitCode(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_UNIT_CODE)));
                  singleData.setGrowerDomumrntGrowerFather(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_FATHER)));
                  singleData.setGrowerDomumrntGrowerMobile(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_GROWER_MOBILE)));
                  singleData.setGrowerDomumrntPhoto_1(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_PHOTO_1)));
                  singleData.setGrowerDomumrntPhoto_2(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_PHOTO_2)));
                  singleData.setGrowerDomumrntSignature(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SIGNATURE)));
                  singleData.setGrowerDomumrntThumb(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_THUMB_IMPRESSION)));
                  singleData.setServer_status(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SERVER_UPLOAD_STATUS)));
                  singleData.setServer_remark(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_COLUMN_SERVER_UPLOAD_REMARK)));
                  singleData.setCreated_at(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.GROWER_DOCUMENT_CREATED_AT)));
                  singleData.setACKID(cursor.getString(cursor.getColumnIndex(SaveGrowerDocumentModel.COLUMN_FK_GROWER_DOCUMENT)));
                  allGrowerData.add(singleData);
              } while (cursor.moveToNext());
          }
          db.close();
          return allGrowerData;
      }

  */
    public List<FarmerModel> getFarmerModel(String villageCode, String growerCode) {
        List<FarmerModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + FarmerModel.TABLE_NAME + " WHERE 1=1 ";
        if (villageCode.length() > 0) {
            selectQuery += " AND " + FarmerModel.Col_villageCode + "='" + villageCode + "' ";
        }
        if (growerCode.length() > 0) {
            selectQuery += " AND " + FarmerModel.Col_farmerCode + "='" + growerCode + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerModel modeDataModel = new FarmerModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerModel.COLUMN_ID)));
                modeDataModel.setFarmerCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerCode)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_villageCode)));
                modeDataModel.setFarmerName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_farmerName)));
                modeDataModel.setFatherName(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_fatherName)));
                modeDataModel.setUniqueCode(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_uniqueCode)));
                modeDataModel.setDes(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_des)));
                modeDataModel.setMobile(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_mobile)));
                modeDataModel.setCultArea(cursor.getString(cursor.getColumnIndex(FarmerModel.Col_cultArea)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public String getSumAreaFarmer(String VillageCode, String GrowerCode) {
        double area = 0;
        String selectQuery = "select IFNULL(SUM((plot_survey.areaHectare*farmer_share.share)/100),0) as sum_area from farmer_share join plot_survey on farmer_share.survey_id=plot_survey.plot_survey_id where farmer_share.village_code=" + VillageCode + " and farmer_share.grower_code='" + GrowerCode + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                area = cursor.getDouble(cursor.getColumnIndex("sum_area"));
            } while (cursor.moveToNext());
        }
        db.close();
        return new DecimalFormat("0.000").format(area);
    }

    public List<FarmerShareModel> getFarmerShareModelList(String surveyId, String surveySrNo, String VillageCode, String GrowerCode) {
        List<FarmerShareModel> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + FarmerShareModel.TABLE_NAME + " WHERE 1=1 ";
        if (surveyId.length() > 0) {
            selectQuery += " AND " + FarmerShareModel.Col_surveyId + "='" + surveyId + "' ";
        }
        if (surveySrNo.length() > 0) {
            selectQuery += " AND " + FarmerShareModel.Col_srNo + "='" + surveySrNo + "' ";
        }
        if (VillageCode.length() > 0) {
            selectQuery += " AND " + FarmerShareModel.Col_villageCode + "='" + VillageCode + "' ";
        }
        if (GrowerCode.length() > 0) {
            selectQuery += " AND " + FarmerShareModel.Col_growerCode + "='" + GrowerCode + "' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FarmerShareModel modeDataModel = new FarmerShareModel();
                modeDataModel.setColId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.COLUMN_ID)));
                modeDataModel.setSurveyId(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_surveyId)));
                modeDataModel.setSrNo(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_srNo)));
                modeDataModel.setVillageCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_villageCode)));
                modeDataModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerCode)));
                modeDataModel.setGrowerName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerName)));
                modeDataModel.setGrowerFatherName(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerFatherName)));
                modeDataModel.setGrowerAadharNumber(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_growerAadhar_number)));
                modeDataModel.setShare(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_share)));
                modeDataModel.setSupCode(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_sup_code)));
                modeDataModel.setCurDate(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_curDate)));
                modeDataModel.setServerStatus(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatus)));
                modeDataModel.setServerStatusRemark(cursor.getString(cursor.getColumnIndex(FarmerShareModel.Col_ServerStatusRemark)));
                list.add(modeDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }


    ///////////////Start New AgriInput-----------////////////

    public long insertAgriInputFormData(String VillageCode, String VillageName, String GrowerCode, String GrowerName, String GrowerUnitCode, String GrowerFather, String GrowerMobile, byte[] photo_1, byte[] photo_2, byte[] signature, byte[] Thumb, int FkAgriInput) {
        // get writable database as we want to write data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AgriInputFormModel.AGRI_VILLAGE_CODE, VillageCode);
        values.put(AgriInputFormModel.AGRI_VILLAGE_NAME, VillageName);
        values.put(AgriInputFormModel.AGRI_GROWER_CODE, GrowerCode);
        values.put(AgriInputFormModel.AGRI_GROWER_NAME, GrowerName);
        values.put(AgriInputFormModel.AGRI_GROWER_UNIT_CODE, GrowerUnitCode);
        values.put(AgriInputFormModel.AGRI_GROWER_FATHER, GrowerFather);
        values.put(AgriInputFormModel.AGRI_GROWER_MOBILE, GrowerMobile);
        values.put(AgriInputFormModel.AGRI_COLUMN_PHOTO_1, photo_1);
        values.put(AgriInputFormModel.AGRI_COLUMN_PHOTO_2, photo_2);
        values.put(AgriInputFormModel.AGRI_COLUMN_SIGNATURE, signature);
        values.put(AgriInputFormModel.AGRI_COLUMN_THUMB_IMPRESSION, Thumb);
        values.put(AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS, "No");
        values.put(AgriInputFormModel.AGRI_CREATED_AT, currentDt);
        values.put(AgriInputFormModel.COLUMN_FK_AGRI_INPUT, FkAgriInput);
        // insert row  2022-03-01
        long id = db.insert(AgriInputFormModel.TABLE_NAME, null, values);
        //if(id==-1)return false(Table is not created);elsere turn true(Table is created);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<AgriInputFormModel> getAllAgriInputData(String villageCode, String growerCode) {
        List<AgriInputFormModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + "(" + AgriInputFormModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + AgriInputFormModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AgriInputFormModel agriInputFormModel = new AgriInputFormModel();
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_MOBILE)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public List<AgriInputFormModel> getAllAgriInputData() {
        List<AgriInputFormModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " ORDER BY " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AgriInputFormModel agriInputFormModel = new AgriInputFormModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    //For Retriving photoe1 from the database using id
    public Bitmap getRetrivePhoto1(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + "(" + AgriInputFormModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + AgriInputFormModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    //For Retriving photoe2 from the database using id
    public Bitmap getRetrivePhoto2(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + "(" + AgriInputFormModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + AgriInputFormModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    //For Retriving Signature using id
    public Bitmap getRetriveSignature(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + "(" + AgriInputFormModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + AgriInputFormModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    public Bitmap getRetriveThumb(String villageCode, String growerCode) {
        Bitmap bitmap = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + "(" + AgriInputFormModel.AGRI_VILLAGE_CODE + "=" + villageCode + " AND " + AgriInputFormModel.AGRI_GROWER_CODE + "=" + growerCode + ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_THUMB_IMPRESSION));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } while (cursor.moveToNext());
        }
        db.close();
        return bitmap;
    }

    public List<AddMaterialDataModel> getAllAgriInputDataMaterialById(int id) {
        List<AddMaterialDataModel> addMaterialDataModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AddMaterialDataModel.TABLE_NAME + " WHERE (" + AddMaterialDataModel.AGRI_INPUT_ID + "=" + id + ")";
        Log.d(TAG, "AddMaterialDataModel: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        //if(cursor!=null)
        if (cursor.moveToFirst()) {
            do {
                AddMaterialDataModel addMaterialDataModel = new AddMaterialDataModel();
                addMaterialDataModel.setAgriInputId(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.AGRI_INPUT_ID)));
                addMaterialDataModel.setMatCode(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MAT_CODE)));
                addMaterialDataModel.setMaterialName(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_NAME)));
                addMaterialDataModel.setMaterialQuantity(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_QUANTITY)));
                addMaterialDataModel.setMaterialRate(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_RATE)));
                addMaterialDataModel.setMaterialStatus(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_STATUS)));
                addMaterialDataModel.setMatTotalAmount(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MAT_TOTAL_AMOUNT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                addMaterialDataModels.add(addMaterialDataModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return addMaterialDataModels;
    }

    public List<GrowerModel> getGrowerMasterVillageWiseAllData(String villageCode) {
        List<GrowerModel> growerMasterModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + GrowerModel.TABLE_NAME + " WHERE " + GrowerModel.Col_vill_code + "=" + villageCode;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GrowerModel growerMasterModel = new GrowerModel();
                growerMasterModel.setGrowerCode(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_code)));
                growerMasterModel.setGrowerName(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_name)));
                growerMasterModel.setGrowerFather(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_grower_father_name)));
                growerMasterModel.setVillageCode(cursor.getString(cursor.getColumnIndex(GrowerModel.Col_vill_code)));
                growerMasterModels.add(growerMasterModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return growerMasterModels;
    }

    public List<AgriInputFormModel> getFilterAgriInputData(String DateStr, String toDateSTr, String VillageCode, String GrowerCode) {
        List<AgriInputFormModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery;
        if (DateStr.length() == 0) {
            selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + AgriInputFormModel.AGRI_GROWER_NAME + " LIKE '%" + GrowerCode + "%' AND " + AgriInputFormModel.AGRI_VILLAGE_NAME + " LIKE '%" + VillageCode + "%' ORDER BY " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + AgriInputFormModel.AGRI_CREATED_AT + ">='" + DateStr + "' AND " + AgriInputFormModel.AGRI_CREATED_AT + "<='" + toDateSTr + "' AND " + AgriInputFormModel.AGRI_GROWER_NAME + " LIKE '%" + GrowerCode + "%'  AND " + AgriInputFormModel.AGRI_VILLAGE_NAME + " LIKE '%" + VillageCode + "%' ORDER BY " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + " DESC";
        }
        Log.d(TAG, "getAllAgriInputData: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AgriInputFormModel agriInputFormModel = new AgriInputFormModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public List<AgriInputFormModel> getAllAgriInputDataById(String id) {
        List<AgriInputFormModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + "=" + id;
        Log.d(TAG, "AddMaterialDataModel: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AgriInputFormModel agriInputFormModel = new AgriInputFormModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_UNIT_CODE)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                //agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                //agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                agriInputFormModels.add(agriInputFormModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public List<AddMaterialDataModel> getAllAgriInputDataMaterial() {
        List<AddMaterialDataModel> addMaterialDataModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AddMaterialDataModel.TABLE_NAME;
        Log.d(TAG, "AddMaterialDataModel: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddMaterialDataModel addMaterialDataModel = new AddMaterialDataModel();
                addMaterialDataModel.setAgriInputId(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.AGRI_INPUT_ID)));
                addMaterialDataModel.setMatCode(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MAT_CODE)));
                addMaterialDataModel.setMaterialName(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_NAME)));
                addMaterialDataModel.setMaterialQuantity(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_QUANTITY)));
                addMaterialDataModel.setMaterialRate(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_RATE)));
                addMaterialDataModel.setMaterialStatus(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MATERIAL_STATUS)));
                addMaterialDataModel.setMatTotalAmount(cursor.getString(cursor.getColumnIndex(AddMaterialDataModel.MAT_TOTAL_AMOUNT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                // agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                // agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                addMaterialDataModels.add(addMaterialDataModel);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return addMaterialDataModels;
    }

    public List<MaterialMasterModel> getMaterialMasterAllData() {
        List<MaterialMasterModel> matMasterArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MaterialMasterModel.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MaterialMasterModel model = new MaterialMasterModel();
                model.setMatCode(cursor.getString(cursor.getColumnIndex(MaterialMasterModel.MAT_CODE)));
                model.setMatName(cursor.getString(cursor.getColumnIndex(MaterialMasterModel.MAT_NAME)));
                model.setMaterialUnitCode(cursor.getString(cursor.getColumnIndex(MaterialMasterModel.MATERIAL_UNIT_CODE)));
                model.setMaterialRate(cursor.getInt(cursor.getColumnIndex(MaterialMasterModel.MATERIAL_RATE)));
                matMasterArrayList.add(model);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return arrar list
        return matMasterArrayList;
    }

    public long insertMaterialSearched(List<AddSearchedMaterialModel> materialArrayList, String materialStatus, int fk_add_material) {
        int aSize = materialArrayList.size();
        long id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < aSize; i++) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                values.put(AddMaterialDataModel.AGRI_INPUT_ID, materialArrayList.get(i).getAgriInputId());
                values.put(AddMaterialDataModel.MAT_CODE, materialArrayList.get(i).getMatCode());
                values.put(AddMaterialDataModel.MATERIAL_NAME, materialArrayList.get(i).getMaterialName());
                values.put(AddMaterialDataModel.MATERIAL_RATE, materialArrayList.get(i).getMaterialRate());
                values.put(AddMaterialDataModel.MATERIAL_QUANTITY, materialArrayList.get(i).getMaterialQuantity());
                values.put(AddMaterialDataModel.MAT_TOTAL_AMOUNT, materialArrayList.get(i).getMatTotalAmount());
                values.put(AddMaterialDataModel.MATERIAL_STATUS, materialStatus);
                values.put(AddMaterialDataModel.CREATED_AT, dateFormat.format(date));
                values.put(AddMaterialDataModel.COLUMN_FK_ADD_MATERIAL, fk_add_material);
                // insert row
                id = db.insert(AddMaterialDataModel.TABLE_NAME, null, values);
                Log.d(TAG, "AddMaterialDataModel: " + String.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
        // return newly inserted row id
        return id;
    }

    public List<AgriInputFormModel> getAllAgriInputPendingDataById() {
        List<AgriInputFormModel> agriInputFormModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AgriInputFormModel.TABLE_NAME + " WHERE " +
                AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='No'";
        Log.d(TAG, "AddMaterialDataModel: " + ">><><" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AgriInputFormModel agriInputFormModel = new AgriInputFormModel();
                agriInputFormModel.setAgri_input_Id(cursor.getInt(cursor.getColumnIndex(AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID)));
                agriInputFormModel.setAgriVillageCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_CODE)));
                agriInputFormModel.setAgriVillageName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_VILLAGE_NAME)));
                agriInputFormModel.setAgriGrowerCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_CODE)));
                agriInputFormModel.setAgriGrowerName(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_NAME)));
                agriInputFormModel.setAgriGrowerFather(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_FATHER)));
                agriInputFormModel.setAgriGrowerMobile(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_MOBILE)));
                agriInputFormModel.setAgriGrowerUnitCode(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_GROWER_UNIT_CODE)));
                agriInputFormModel.setCreated_at(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_CREATED_AT)));
                //agriInputFormModel.setAgriPhoto_1(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_1)));
                //agriInputFormModel.setAgriPhoto_2(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_PHOTO_2)));
                //agriInputFormModel.setAgriSignature(cursor.getBlob(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SIGNATURE)));
                agriInputFormModel.setServer_status(cursor.getString(cursor.getColumnIndex(AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS)));
                List<AddMaterialDataModel> materialMasterModels = getAllAgriInputDataMaterialById(agriInputFormModel.getAgri_input_Id());
                if (materialMasterModels.size() > 0) {
                    agriInputFormModels.add(agriInputFormModel);
                }
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return  list
        return agriInputFormModels;
    }

    public void updateAgriInputFormModel(String remark, String id, String status) {
        String query = "UPDATE " + AgriInputFormModel.TABLE_NAME + " SET " + AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='" + status + "', " +
                AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_REMARK + "='" + remark.replace("'", "") + "' WHERE " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + "='" + id + "'";
        SQLiteDatabase dbe = this.getReadableDatabase();
        dbe.execSQL(query);
        dbe.close();
    }

    public void resentAgriInputFormModel(String remark, String id, String status) {
        String query = "UPDATE " + AgriInputFormModel.TABLE_NAME + " SET " + AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_STATUS + "='" + status + "', " +
                AgriInputFormModel.AGRI_COLUMN_SERVER_UPLOAD_REMARK + "='" + remark.replace("'", "") + "' WHERE " + AgriInputFormModel.COLUMN_AGRI_INPUT_FORM_ID + "='" + id + "'";
        SQLiteDatabase dbe = this.getReadableDatabase();
        dbe.execSQL(query);
        dbe.close();
    }

    public long insertMaterialMasterModel(MaterialMasterModel materialMasterModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MaterialMasterModel.MAT_CODE, materialMasterModel.getMatCode());
        values.put(MaterialMasterModel.MAT_NAME, materialMasterModel.getMatName());
        values.put(MaterialMasterModel.MATERIAL_RATE, materialMasterModel.getMaterialRate());
        values.put(MaterialMasterModel.MATERIAL_UNIT_CODE, materialMasterModel.getMaterialUnitCode());
        values.put(MaterialMasterModel.MATERIAL_STATUS, materialMasterModel.getMaterialStatus());
        // insert row
        long id = db.insert(MaterialMasterModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }


    public long insertOfflineControlModel(OfflineControlModel model) {
        SQLiteDatabase factoryDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OfflineControlModel.printer_mac, model.getPrintAddress());
        long id = factoryDb.insert(OfflineControlModel.TABLE_NAME, null, values);
        factoryDb.close();
        return id;
    }
    //---------------------------------------------------------------------------------------------


    //------------------------List----------------------------------------------------------------
    public List<OfflineControlModel> getOfflineControlModel() {
        List<OfflineControlModel> factoryData = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + OfflineControlModel.TABLE_NAME;
        SQLiteDatabase factoryDb = this.getWritableDatabase();
        Cursor cursor = factoryDb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OfflineControlModel singleData = new OfflineControlModel();
                singleData.setId(cursor.getString(cursor.getColumnIndex(OfflineControlModel.COLUMN_ID)));
                singleData.setPrintAddress(cursor.getString(cursor.getColumnIndex(OfflineControlModel.printer_mac)));
                factoryData.add(singleData);
            } while (cursor.moveToNext());
        }
        factoryDb.close();
        return factoryData;

    }

    public void updatePrinter(String mac) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("UPDATE " + OfflineControlModel.TABLE_NAME + " SET " + OfflineControlModel.printer_mac + "='" + mac + "'");
            db.close();
        } catch (Exception e) {
            //
        }
    }

    public void deleteOfflineControlModel() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + OfflineControlModel.TABLE_NAME);
        db.execSQL(OfflineControlModel.CREATE_TABLE);
        db.execSQL("VACUUM");
        db.close();
    }

    @SuppressLint("Range")
    public List<GpsDataModel> getGpsDataModel(String getdata) {
        List<GpsDataModel> gpsDataModels = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GpsDataModel.TABLE_NAME + " LIMIT 0," + getdata;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                GpsDataModel gpsDataModel = new GpsDataModel();
                gpsDataModel.setColId(cursor.getString(cursor.getColumnIndex(GpsDataModel.COLUMN_ID)));
                gpsDataModel.setLat(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_lat)));
                gpsDataModel.setLng(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_lng)));
                gpsDataModel.setAddress(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_address)));
                gpsDataModel.setAccuracy(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_accuracy)));
                gpsDataModel.setBearing(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_bearing)));
                gpsDataModel.setSpeed(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_speed)));
                gpsDataModel.setBattery(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_battery)));
                gpsDataModel.setProvider(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_gpsprovider)));
                gpsDataModel.setInternetStatus(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_internet_status)));
                gpsDataModel.setGpsStatus(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_gpsStatus)));
                gpsDataModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_createdAt)));
                gpsDataModel.setCharging(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_charging)));
                gpsDataModel.setAppVersion(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_app_version)));
                gpsDataModel.setUserName(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_user_name)));
                gpsDataModel.setDistance(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_distance)));
                gpsDataModel.setOvertime_status(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_overtime_status)));
                gpsDataModels.add(gpsDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return gpsDataModels;
    }

    public long insertGpsDataModel(GpsDataModel gpsDataModel) {
        List<LastGpsDataModel> gpsLastRecord = this.gpsLastRecord();
        SQLiteDatabase sqldb = this.getWritableDatabase();
        long id = 0;
        try {

            // get writable database as we want to write data
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(GpsDataModel.Col_lat, gpsDataModel.getLat());
            values.put(GpsDataModel.Col_lng, gpsDataModel.getLng());
            values.put(GpsDataModel.Col_address, gpsDataModel.getAddress());
            values.put(GpsDataModel.Col_accuracy, gpsDataModel.getAccuracy());
            values.put(GpsDataModel.Col_bearing, gpsDataModel.getBearing());
            values.put(GpsDataModel.Col_speed, gpsDataModel.getSpeed());
            values.put(GpsDataModel.Col_gpsprovider, gpsDataModel.getProvider());
            values.put(GpsDataModel.Col_battery, gpsDataModel.getBattery());
            values.put(GpsDataModel.Col_createdAt, gpsDataModel.getCreatedAt());
            values.put(GpsDataModel.Col_gpsStatus, gpsDataModel.getGpsStatus());
            values.put(GpsDataModel.Col_internet_status, gpsDataModel.getInternetStatus());
            values.put(GpsDataModel.Col_charging, gpsDataModel.getCharging());
            values.put(GpsDataModel.Col_app_version, gpsDataModel.getAppVersion());
            values.put(GpsDataModel.Col_distance, gpsDataModel.getDistance());
            values.put(GpsDataModel.Col_overtime_status, gpsDataModel.getOvertime_status());
            // insert row
            id = sqldb.insert(GpsDataModel.TABLE_NAME, null, values);
            // close db connection
            if (gpsLastRecord.size() > 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String currentDt = dateFormat.format(today);
                String query = "UPDATE " + LastGpsDataModel.TABLE_NAME + " SET " +
                        LastGpsDataModel.Col_lat + "='" + gpsDataModel.getLat() + "' ," +
                        LastGpsDataModel.Col_lng + "='" + gpsDataModel.getLng() + "' ," +
                        LastGpsDataModel.Col_address + "='" + gpsDataModel.getAddress() + "' ," +
                        LastGpsDataModel.Col_accuracy + "='" + gpsDataModel.getAccuracy() + "' ," +
                        LastGpsDataModel.Col_bearing + "='" + gpsDataModel.getBearing() + "' ," +
                        LastGpsDataModel.Col_speed + "='" + gpsDataModel.getSpeed() + "' ," +
                        LastGpsDataModel.Col_battery + "='" + gpsDataModel.getBattery() + "' ," +
                        LastGpsDataModel.Col_gpsprovider + "='" + gpsDataModel.getProvider() + "' ," +
                        LastGpsDataModel.Col_gpsStatus + "='" + gpsDataModel.getGpsStatus() + "' ," +
                        LastGpsDataModel.Col_internet_status + "='" + gpsDataModel.getInternetStatus() + "' ," +
                        LastGpsDataModel.Col_createdAt + "='" + gpsDataModel.getCreatedAt() + "' ," +
                        LastGpsDataModel.Col_charging + "='" + gpsDataModel.getCharging() + "' ," +
                        LastGpsDataModel.Col_distance + "='" + gpsDataModel.getDistance() + "' " +
                        " WHERE " + LastGpsDataModel.COLUMN_ID + "='" + gpsLastRecord.get(0).getColId() + "'";
                //db.execSQL("UPDATE "+TruckDetails.TABLE_NAME+" SET "+TruckDetails.user_type+"='"+currentDt+"'");
                sqldb.execSQL(query);
                sqldb.close();
            } else {
                ContentValues values1 = new ContentValues();
                // `id` and `timestamp` will be inserted automatically.
                // no need to add them
                values1.put(LastGpsDataModel.Col_lat, gpsDataModel.getLat());
                values1.put(LastGpsDataModel.Col_lng, gpsDataModel.getLng());
                values1.put(LastGpsDataModel.Col_address, gpsDataModel.getAddress());
                values1.put(LastGpsDataModel.Col_accuracy, gpsDataModel.getAccuracy());
                values1.put(LastGpsDataModel.Col_bearing, gpsDataModel.getBearing());
                values1.put(LastGpsDataModel.Col_speed, gpsDataModel.getSpeed());
                values1.put(LastGpsDataModel.Col_gpsprovider, gpsDataModel.getProvider());
                values1.put(LastGpsDataModel.Col_battery, gpsDataModel.getBattery());
                values1.put(LastGpsDataModel.Col_createdAt, gpsDataModel.getCreatedAt());
                values1.put(LastGpsDataModel.Col_gpsStatus, gpsDataModel.getGpsStatus());
                values1.put(LastGpsDataModel.Col_internet_status, gpsDataModel.getInternetStatus());
                values1.put(LastGpsDataModel.Col_charging, gpsDataModel.getCharging());
                values1.put(LastGpsDataModel.Col_app_version, gpsDataModel.getAppVersion());
                values1.put(LastGpsDataModel.Col_distance, gpsDataModel.getDistance());
                long sid = sqldb.insert(LastGpsDataModel.TABLE_NAME, null, values1);
                Log.d("Sid", "" + sid);
            }
        } catch (SQLiteException e) {
            Log.d("", e.toString());
        } catch (Exception e) {
            Log.d("", e.toString());
        }
        sqldb.close();
        // return newly inserted row id
        return id;
    }

    @SuppressLint("Range")
    public List<LastGpsDataModel> gpsLastRecord() {
        List<LastGpsDataModel> gpsDataModels = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LastGpsDataModel.TABLE_NAME + " ORDER BY " + LastGpsDataModel.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LastGpsDataModel gpsDataModel = new LastGpsDataModel();
                gpsDataModel.setColId(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.COLUMN_ID)));
                gpsDataModel.setLat(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_lat)));
                gpsDataModel.setLng(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_lng)));
                gpsDataModel.setAddress(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_address)));
                gpsDataModel.setAccuracy(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_accuracy)));
                gpsDataModel.setBearing(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_bearing)));
                gpsDataModel.setSpeed(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_speed)));
                gpsDataModel.setBattery(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_battery)));
                gpsDataModel.setProvider(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_gpsprovider)));
                gpsDataModel.setInternetStatus(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_internet_status)));
                gpsDataModel.setGpsStatus(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_gpsStatus)));
                gpsDataModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_createdAt)));
                gpsDataModel.setCharging(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_charging)));
                gpsDataModel.setAppVersion(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_app_version)));
                gpsDataModel.setServerSent(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_server_sent)));
                gpsDataModel.setError(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_error)));
                gpsDataModel.setDistance(cursor.getString(cursor.getColumnIndex(LastGpsDataModel.Col_distance)));
                //gpsDataModel.setUserName(cursor.getString(cursor.getColumnIndex(GpsDataModel.Col_user_name)));
                gpsDataModels.add(gpsDataModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return gpsDataModels;
    }

    public void updateServerSentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + LastGpsDataModel.TABLE_NAME + " SET " + LastGpsDataModel.Col_server_sent + "='" + currentDt + "'";
        db.execSQL(query);
        //db.execSQL("DELETE FROM "+ GpsDataModel.TABLE_NAME+" WHERE "+GpsDataModel.COLUMN_ID+"='"+id+"'");
        db.close();
    }

    public void updateError(String error) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        SQLiteDatabase db = this.getReadableDatabase();
        error = error.replace("'", " ");
        error = error.replace("\"", " ");
        db.execSQL("UPDATE " + LastGpsDataModel.TABLE_NAME + " SET " + LastGpsDataModel.Col_error + "= '" + error + "' ");
        //db.execSQL("DELETE FROM "+ GpsDataModel.TABLE_NAME+" WHERE "+GpsDataModel.COLUMN_ID+"='"+id+"'");
        db.close();
    }

    public void deleteGpsDataModel(String id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDt = dateFormat.format(today);
        SQLiteDatabase db = this.getReadableDatabase();
        //db.execSQL("UPDATE "+TruckDetails.TABLE_NAME+" SET "+TruckDetails.user_type+"='"+currentDt+"'");
        db.execSQL("DELETE FROM " + GpsDataModel.TABLE_NAME + " WHERE " + GpsDataModel.COLUMN_ID + "='" + id + "'");
        db.close();
    }

    public void open() throws SQLException {
        close();
        this.getWritableDatabase();
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
