package in.co.vibrant.bindalsugar.model;

import java.util.Objects;

public class AddFamilyMemDataModel {
    private int activity;
    private String activityName;
    private int activityNumber;
    private String date;
    private String description;
    private int activityMethod;
    private String activityMethodName;
    private String itemList ;
    private int MEMBERLIST;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddFamilyMemDataModel that = (AddFamilyMemDataModel) o;
        return activity == that.activity && activityMethod == that.activityMethod && date.equals(that.date) && itemList.equals(that.itemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, activityMethod, itemList);
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
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

    public int getActivityMethod() {
        return activityMethod;
    }

    public void setActivityMethod(int activityMethod) {
        this.activityMethod = activityMethod;
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

    public int getMEMBERLIST() {
        return MEMBERLIST;
    }

    public void setMEMBERLIST(int MEMBERLIST) {
        this.MEMBERLIST = MEMBERLIST;
    }
}
