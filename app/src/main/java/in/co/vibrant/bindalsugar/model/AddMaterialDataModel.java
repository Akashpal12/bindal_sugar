package in.co.vibrant.bindalsugar.model;


public class AddMaterialDataModel {
    public static final String TABLE_NAME = "AddMaterialData";
    //For Table colume
    public static final String COLUMN_ADD_MATERIAL_ID = "AddMaterialId";
    public static final String AGRI_INPUT_ID = "AgriInputId";
    public static final String MAT_CODE = "MatCode";
    public static final String MATERIAL_NAME = "MaterialName";
    public static final String MATERIAL_RATE = "MaterialRate";
    public static final String MATERIAL_QUANTITY = "MaterialQuantity";
    public static final String MAT_TOTAL_AMOUNT = "MatTotalAmount";
    public static final String MATERIAL_STATUS = "MaterialStatus";
    public static final String CREATED_AT = "CreatedAt";
    public static final String COLUMN_FK_ADD_MATERIAL = "FkAddMaterial";

    private int add_material_Id;
    private String agriInputId;
    private String matCode;
    private String materialName;
    private String materialRate;
    private String materialQuantity;
    private String matTotalAmount;
    private String materialStatus;
    private String createdAt;
    private String fkAddMaterial;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ADD_MATERIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + AGRI_INPUT_ID + " TEXT,"
                    + MAT_CODE + " TEXT,"
                    + MATERIAL_NAME + " TEXT,"
                    + MATERIAL_RATE + " TEXT,"
                    + MATERIAL_QUANTITY + " TEXT,"
                    + MAT_TOTAL_AMOUNT + " TEXT,"
                    + MATERIAL_STATUS + " TEXT,"
                    + CREATED_AT + " TEXT,"
                    + COLUMN_FK_ADD_MATERIAL + " INTEGER"
                    //+ "FOREIGN KEY (" + COLUMN_FK_ADD_MATERIAL + ")REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";

    public AddMaterialDataModel() {
    }

    public AddMaterialDataModel(String agriInputId, int add_material_Id, String matCode, String materialName, String materialRate, String materialQuantity, String matTotalAmount, String materialStatus, String createdAt, String fkAddMaterial) {
        this.add_material_Id = add_material_Id;
        this.agriInputId = agriInputId;
        this.matCode = matCode;
        this.materialName = materialName;
        this.materialRate = materialRate;
        this.materialQuantity = materialQuantity;
        this.matTotalAmount = matTotalAmount;
        this.materialStatus = materialStatus;
        this.createdAt = createdAt;
        this.fkAddMaterial = fkAddMaterial;
    }

    public int getAdd_material_Id() {
        return add_material_Id;
    }

    public void setAdd_material_Id(int add_material_Id) {
        this.add_material_Id = add_material_Id;
    }

    public String getAgriInputId() {
        return agriInputId;
    }

    public void setAgriInputId(String agriInputId) {
        this.agriInputId = agriInputId;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialRate() {
        return materialRate;
    }

    public void setMaterialRate(String materialRate) {
        this.materialRate = materialRate;
    }

    public String getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(String materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public String getMatTotalAmount() {
        return matTotalAmount;
    }

    public void setMatTotalAmount(String matTotalAmount) {
        this.matTotalAmount = matTotalAmount;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFkAddMaterial() {
        return fkAddMaterial;
    }

    public void setFkAddMaterial(String fkAddMaterial) {
        this.fkAddMaterial = fkAddMaterial;
    }
}
