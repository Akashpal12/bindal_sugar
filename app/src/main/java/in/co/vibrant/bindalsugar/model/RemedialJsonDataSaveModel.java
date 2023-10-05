package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class RemedialJsonDataSaveModel {

    private String REMICODE;
    private String DATE;
    private String DIS;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemedialJsonDataSaveModel that = (RemedialJsonDataSaveModel) o;
        return REMICODE == that.REMICODE && DATE.equals(that.DATE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(REMICODE, DATE);
    }

    public String getREMICODE() {
        return REMICODE;
    }

    public void setREMICODE(String REMICODE) {
        this.REMICODE = REMICODE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getDIS() {
        return DIS;
    }

    public void setDIS(String DIS) {
        this.DIS = DIS;
    }
}
