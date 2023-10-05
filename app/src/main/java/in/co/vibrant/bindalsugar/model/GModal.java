package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class GModal {

    private int caneSupplyPurchey;
    private String caneSupplyPurcheyName;
    private int villCode;
    private String villName;
    private int growCode;
    private String growerName;
    private int relation;
    private String relationName;
    private String purcheyNumber;
    private String issuedpurcheyNumber;
    private int modeOfSupply;
    private String modeOfSupplyName;
    private String vehicleNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GModal gModal = (GModal) o;
        return caneSupplyPurchey == gModal.caneSupplyPurchey && villCode == gModal.villCode && growCode == gModal.growCode && purcheyNumber.equals(gModal.purcheyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caneSupplyPurchey, villCode, growCode, purcheyNumber);
    }



    public int getCaneSupplyPurchey() {
        return caneSupplyPurchey;
    }

    public void setCaneSupplyPurchey(int caneSupplyPurchey) {
        this.caneSupplyPurchey = caneSupplyPurchey;
    }

    public String getCaneSupplyPurcheyName() {
        return caneSupplyPurcheyName;
    }

    public void setCaneSupplyPurcheyName(String caneSupplyPurcheyName) {
        this.caneSupplyPurcheyName = caneSupplyPurcheyName;
    }

    public int getVillCode() {
        return villCode;
    }

    public void setVillCode(int villCode) {
        this.villCode = villCode;
    }

    public String getVillName() {
        return villName;
    }

    public void setVillName(String villName) {
        this.villName = villName;
    }

    public int getGrowCode() {
        return growCode;
    }

    public void setGrowCode(int growCode) {
        this.growCode = growCode;
    }

    public String getGrowerName() {
        return growerName;
    }

    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getPurcheyNumber() {
        return purcheyNumber;
    }

    public void setPurcheyNumber(String purcheyNumber) {
        this.purcheyNumber = purcheyNumber;
    }

    public String getIssuedpurcheyNumber() {
        return issuedpurcheyNumber;
    }

    public void setIssuedpurcheyNumber(String issuedpurcheyNumber) {
        this.issuedpurcheyNumber = issuedpurcheyNumber;
    }

    public int getModeOfSupply() {
        return modeOfSupply;
    }

    public void setModeOfSupply(int modeOfSupply) {
        this.modeOfSupply = modeOfSupply;
    }

    public String getModeOfSupplyName() {
        return modeOfSupplyName;
    }

    public void setModeOfSupplyName(String modeOfSupplyName) {
        this.modeOfSupplyName = modeOfSupplyName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
