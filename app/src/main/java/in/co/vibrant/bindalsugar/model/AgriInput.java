package in.co.vibrant.bindalsugar.model;

public class AgriInput {
    String villCode;
    String VillName;
    String GroCode;
    String GroName;
    String bleachingPowder;
    String trichormePowder;
    String trichormeLiquid;
    String emida;
    String corajen;
    String date;
    String ferrosSulphate;
    String hexaStop;

    public AgriInput() {
    }

    public AgriInput(String villCode, String villName, String groCode, String groName, String bleachingPowder, String trichormePowder, String trichormeLiquid, String emida, String corajen, String date, String ferrosSulphate, String hexaStop) {

        this.villCode = villCode;
        VillName = villName;
        GroCode = groCode;
        GroName = groName;
        this.bleachingPowder = bleachingPowder;
        this.trichormePowder = trichormePowder;
        this.trichormeLiquid = trichormeLiquid;
        this.emida = emida;
        this.corajen = corajen;
        this.date = date;
        this.ferrosSulphate = ferrosSulphate;
        this.hexaStop = hexaStop;
    }

    public String getVillCode() {
        return villCode;
    }

    public void setVillCode(String villCode) {
        this.villCode = villCode;
    }

    public String getVillName() {
        return VillName;
    }

    public void setVillName(String villName) {
        VillName = villName;
    }

    public String getGroCode() {
        return GroCode;
    }

    public void setGroCode(String groCode) {
        GroCode = groCode;
    }

    public String getGroName() {
        return GroName;
    }

    public void setGroName(String groName) {
        GroName = groName;
    }

    public String getBleachingPowder() {
        return bleachingPowder;
    }

    public void setBleachingPowder(String bleachingPowder) {
        this.bleachingPowder = bleachingPowder;
    }

    public String getTrichormePowder() {
        return trichormePowder;
    }

    public void setTrichormePowder(String trichormePowder) {
        this.trichormePowder = trichormePowder;
    }

    public String getTrichormeLiquid() {
        return trichormeLiquid;
    }

    public void setTrichormeLiquid(String trichormeLiquid) {
        this.trichormeLiquid = trichormeLiquid;
    }

    public String getEmida() {
        return emida;
    }

    public void setEmida(String emida) {
        this.emida = emida;
    }

    public String getCorajen() {
        return corajen;
    }

    public void setCorajen(String corajen) {
        this.corajen = corajen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFerrosSulphate() {
        return ferrosSulphate;
    }

    public void setFerrosSulphate(String ferrosSulphate) {
        this.ferrosSulphate = ferrosSulphate;
    }

    public String getHexaStop() {
        return hexaStop;
    }

    public void setHexaStop(String hexaStop) {
        this.hexaStop = hexaStop;
    }
}
