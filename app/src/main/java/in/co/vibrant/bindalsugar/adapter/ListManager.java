package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.model.ChcekPlantingPolygonGrowerModel;
import in.co.vibrant.bindalsugar.model.GrowerModel;

public class ListManager {
    private static final String PREF_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public ListManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void setMyModelList(String key, List<GrowerModel> myModelList) {
        String json = gson.toJson(myModelList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public List<GrowerModel> getMyModelList(String key) {
        String json = sharedPreferences.getString(key, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<GrowerModel>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    public void setStringList(String key, List<String> stringList) {
        String json = gson.toJson(stringList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    // Retrieve a list of String objects
    public List<String> getStringList(String key) {
        String json = sharedPreferences.getString(key, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }



    public void setcheckPPGModelList(String key2, List<ChcekPlantingPolygonGrowerModel> checkPPGModelList) {
        String json = gson.toJson(checkPPGModelList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key2, json);
        editor.apply();
    }

    public List<ChcekPlantingPolygonGrowerModel> getcheckPPGModelList(String key2) {
        String json = sharedPreferences.getString(key2, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<ChcekPlantingPolygonGrowerModel>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

}
