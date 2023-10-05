package in.co.vibrant.bindalsugar.model;

public class SuperviserAttendanceReportModel {

    private String DATE;
    private String SUPCODE;
    private String SUPNAME;
    private String CHECKIN;
    private String CHECKOUT;
    private String WORKHOUR;
    private String ATTENDANCE;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

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

    public String getCHECKIN() {
        return CHECKIN;
    }

    public void setCHECKIN(String CHECKIN) {
        this.CHECKIN = CHECKIN;
    }

    public String getCHECKOUT() {
        return CHECKOUT;
    }

    public void setCHECKOUT(String CHECKOUT) {
        this.CHECKOUT = CHECKOUT;
    }

    public String getWORKHOUR() {
        return WORKHOUR;
    }

    public void setWORKHOUR(String WORKHOUR) {
        this.WORKHOUR = WORKHOUR;
    }

    public String getATTENDANCE() {
        return ATTENDANCE;
    }

    public void setATTENDANCE(String ATTENDANCE) {
        this.ATTENDANCE = ATTENDANCE;
    }
}
