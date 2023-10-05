package in.co.vibrant.bindalsugar.util;

import java.io.Serializable;

public class KmlModel implements Serializable {

    String name;
    String coordinates;

    public KmlModel(String name, String coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
