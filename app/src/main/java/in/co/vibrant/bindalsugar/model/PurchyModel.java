package in.co.vibrant.bindalsugar.model;

public class PurchyModel {
    protected String Purchyno;
    protected String mode;
    protected String Category;
    protected String villcode;
    protected String villname;
    protected String grower_code;
    protected String grower_name;
    protected String  gfather;

    public String getPurchyno() {
        return Purchyno;
    }

    public void setPurchyno(String purchyno) {
        Purchyno = purchyno;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }


    public String getVillcode() {
        return villcode;
    }

    public void setVillcode(String villcode) {
        this.villcode = villcode;
    }

    public String getVillname() {
        return villname;
    }

    public void setVillname(String villname) {
        this.villname = villname;
    }

    public String getGrower_code() {
        return grower_code;
    }

    public void setGrower_code(String grower_code) {
        this.grower_code = grower_code;
    }

    public String getGrower_name() {
        return grower_name;
    }

    public void setGrower_name(String grower_name) {
        this.grower_name = grower_name;
    }

    public String getGfather() {
        return gfather;
    }

    public void setGfather(String gfather) {
        this.gfather = gfather;
    }
}
