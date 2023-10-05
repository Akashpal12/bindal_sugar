package in.co.vibrant.bindalsugar.model;

public class CRMSummaryReportModel {

    private String SUPCODE;
    private String SUPNAME;
    private String Total;
    private String Pending;
    private String Approved;
    private String Reject;
    private String NotAnswered;
    private String F_DATE;
    private String T_DATE;
    private String REPORT_TYPE;
    private String REPORT_TYPE_NAME;


    public String getSUPCODE() {
        return SUPCODE;
    }

    public void setSUPCODE(String SUPCODE) {
        this.SUPCODE = SUPCODE;
    }

    public String getSUPNAME() {
        return SUPNAME;
    }

    public void setSUPNAME(String SUPNAME) {
        this.SUPNAME = SUPNAME;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getPending() {
        return Pending;
    }

    public void setPending(String pending) {
        Pending = pending;
    }

    public String getApproved() {
        return Approved;
    }

    public void setApproved(String approved) {
        Approved = approved;
    }

    public String getReject() {
        return Reject;
    }

    public void setReject(String reject) {
        Reject = reject;
    }

    public String getNotAnswered() {
        return NotAnswered;
    }

    public void setNotAnswered(String notAnswered) {
        NotAnswered = notAnswered;
    }

    public String getF_DATE() {
        return F_DATE;
    }

    public void setF_DATE(String f_DATE) {
        F_DATE = f_DATE;
    }

    public String getT_DATE() {
        return T_DATE;
    }

    public void setT_DATE(String t_DATE) {
        T_DATE = t_DATE;
    }

    public String getREPORT_TYPE() {
        return REPORT_TYPE;
    }

    public void setREPORT_TYPE(String REPORT_TYPE) {
        this.REPORT_TYPE = REPORT_TYPE;
    }

    public String getREPORT_TYPE_NAME() {
        return REPORT_TYPE_NAME;
    }

    public void setREPORT_TYPE_NAME(String REPORT_TYPE_NAME) {
        this.REPORT_TYPE_NAME = REPORT_TYPE_NAME;
    }
}
