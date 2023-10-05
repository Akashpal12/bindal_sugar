package in.co.vibrant.bindalsugar.model;

public class AddSearchedMaterialModel {
    private String agriInputId;
    private String matCode;
    private String materialName;
    private String materialUnitCode;
    private String materialRate;
    private String materialQuantity;
    private String matTotalAmount;

    public AddSearchedMaterialModel(String agriInputId, String matCode, String materialName, String materialUnitCode, String materialRate, String materialQuantity, String matTotalAmount) {
        this.agriInputId = agriInputId;
        this.matCode = matCode;
        this.materialName = materialName;
        this.materialUnitCode = materialUnitCode;
        this.materialRate = materialRate;
        this.materialQuantity = materialQuantity;
        this.matTotalAmount = matTotalAmount;
    }

    public String getAgriInputId() {
        return agriInputId;
    }

    public String getMatCode() {
        return matCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getMaterialUnitCode() {
        return materialUnitCode;
    }

    public String getMaterialRate() {
        return materialRate;
    }

    public String getMaterialQuantity() {
        return materialQuantity;
    }

    public String getMatTotalAmount() {
        return matTotalAmount;
    }
}
