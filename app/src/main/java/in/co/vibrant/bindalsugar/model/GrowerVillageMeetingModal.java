package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class GrowerVillageMeetingModal {

    private int villCode;
    private String villName;
    private int growCode;
    private String growerName;
    private String father;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrowerVillageMeetingModal that = (GrowerVillageMeetingModal) o;
        return villCode == that.villCode && growCode == that.growCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(villCode, growCode);
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }
}
