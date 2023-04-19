package entity;

import java.util.List;

public class Point {

    private List<Double> values;
    private String nameOfRow;
    private boolean isNoise = false;

    public Point(List<Double> values, String nameOfRow) {
        this.values = values;
        this.nameOfRow = nameOfRow;
    }

    public Point(){}

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setNameOfRow (String nameOfRow) {
        this.nameOfRow = nameOfRow;
    }

    public String getNameOfRow() {
        return nameOfRow;
    }

    public boolean isNoise() {
        return isNoise;
    }

    public void setNoise() {
        isNoise = true;
    }

}
