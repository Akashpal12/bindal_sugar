package in.co.vibrant.bindalsugar.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ChcekPlantingPolygonGrowerModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;

public class SessionConfig {
    private Context context;
    private SharedPreferences sharedPreferences;
    private ListManager listManager;
    public SessionConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.MySharedPref), MODE_PRIVATE);;
        listManager = new ListManager(context);
    }
    public void setLat(String Lat) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.MyLat), Lat);
        editor.apply();
    }
    public String getLat() {
        return sharedPreferences.getString(context.getString(R.string.MyLat), "");
    }
    public void setLng(String Lan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.MyLng), Lan);
        editor.apply();
    }
    public String getLng() {
        return sharedPreferences.getString(context.getString(R.string.MyLng), "");
    }
    public void setDisease(String Lan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.MyDisease), Lan);
        editor.apply();
    }
    public String getDisease() {
        return sharedPreferences.getString(context.getString(R.string.MyDisease), "");
    }
    public void setSeason(String Lan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.MySeason), Lan);
        editor.apply();
    }
    public String getSeason() {
        return sharedPreferences.getString(context.getString(R.string.MySeason), "");
    }

    public void setMyModelList(List<GrowerModel> myModelList) {
        listManager.setMyModelList(context.getResources().getString(R.string.MyGrowerModelList), myModelList);
    }
    public List<GrowerModel> getMyModelList() {
        return listManager.getMyModelList(context.getResources().getString(R.string.MyGrowerModelList));
    }

    public void setListFound(String listFound) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.listFound), listFound);
        editor.apply();
    }
    public String getListFound() {
        return sharedPreferences.getString(context.getString(R.string.listFound), "");
    }
    public void setcheckPPGModelList(List<ChcekPlantingPolygonGrowerModel> checkPPGModelList) {
        listManager.setcheckPPGModelList(context.getResources().getString(R.string.ChcekPlantingPolygonGrowerModel), checkPPGModelList);
    }
    public List<ChcekPlantingPolygonGrowerModel> getcheckPPGModelList() {
        return listManager.getcheckPPGModelList(context.getResources().getString(R.string.ChcekPlantingPolygonGrowerModel));
    }











}
