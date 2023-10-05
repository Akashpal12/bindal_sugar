package in.co.vibrant.bindalsugar.model;

public class ItemModel {


    protected String it_code;
    protected String it_name;
    protected String uom;
    protected String subsidy;
    protected double cgst;
    protected double cgstAmount;
    protected double sgst;
    protected double sgstAmount;
    protected String hsnno;
    protected double qty;
    protected double rate;
    protected double amount;
    protected double total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel itemModel = (ItemModel) o;
        return it_code.equals(itemModel.it_code);
    }

    public String getIt_code() {
        return it_code;
    }

    public void setIt_code(String it_code) {
        this.it_code = it_code;
    }

    public String getIt_name() {
        return it_name;
    }

    public void setIt_name(String it_name) {
        this.it_name = it_name;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public String getHsnno() {
        return hsnno;
    }

    public void setHsnno(String hsnno) {
        this.hsnno = hsnno;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
