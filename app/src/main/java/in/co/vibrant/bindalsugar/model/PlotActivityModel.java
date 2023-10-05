package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class PlotActivityModel {

    private int activity;
    private String activityName;
    private int activityNumber;
    private String moileNumber;
    private String date;
    private String description;
    private String area;
    private String activityMethod;
    private String activityMethodName;
    private String itemList;
    private String diseasefact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlotActivityModel that = (PlotActivityModel) o;
        return activity == that.activity && date.equals(that.date) && activityMethod.equals(that.activityMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, date, activityMethod);
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public String getMoileNumber() {
        return moileNumber;
    }

    public void setMoileNumber(String moileNumber) {
        this.moileNumber = moileNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityMethod() {
        return activityMethod;
    }

    public void setActivityMethod(String activityMethod) {
        this.activityMethod = activityMethod;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityMethodName() {
        return activityMethodName;
    }

    public void setActivityMethodName(String activityMethodName) {
        this.activityMethodName = activityMethodName;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDiseasefact() {
        return diseasefact;
    }

    public void setDiseasefact(String diseasefact) {
        this.diseasefact = diseasefact;
    }
}
