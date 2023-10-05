package in.co.vibrant.bindalsugar.model;

import androidx.annotation.NonNull;

public class MaterialMasterModel {
    public static final String TABLE_NAME = "MaterialMaster";  //for table name
    //For Table colume
    public static final String COLUMN_MATERIAL_MASTER_ID = "MaterialMasterId";
    public static final String MAT_CODE = "MatCode";
    public static final String MAT_NAME = "MatName";
    public static final String MATERIAL_UNIT_CODE = "MaterialUnitCode";
    public static final String MATERIAL_RATE = "MaterialRate";
    public static final String MATERIAL_STATUS = "MaterialStatus";
    public static final String COLUMN_FK_MATERIAL_MASTER = "FkMaterialMaster";

    private int m_Id;
    private String matCode;
    private String matName;
    private String materialUnitCode;
    private double materialRate;
    private String materialStatus;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_MATERIAL_MASTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MAT_CODE + " TEXT,"
                    + MAT_NAME + " TEXT,"
                    + MATERIAL_UNIT_CODE + " TEXT,"
                    + MATERIAL_RATE + " INTEGER,"
                    + MATERIAL_STATUS + " TEXT"
                    //+ COLUMN_FK_MATERIAL_MASTER + " INTEGER,"
                    //+ "FOREIGN KEY (" + COLUMN_FK_MATERIAL_MASTER + ")REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";



    public MaterialMasterModel() {
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public int getM_Id() {
        return m_Id;
    }

    public void setM_Id(int m_Id) {
        this.m_Id = m_Id;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getMaterialUnitCode() {
        return materialUnitCode;
    }

    public void setMaterialUnitCode(String materialUnitCode) {
        this.materialUnitCode = materialUnitCode;
    }

    public double getMaterialRate() {
        return materialRate;
    }

    public void setMaterialRate(double materialRate) {
        this.materialRate = materialRate;
    }

    @NonNull
    @Override
    public String toString() {
        return matCode.toString();
    }
}
