package in.co.vibrant.bindalsugar.view.supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.StaffPlantingGrowerListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.GrowerSurveyModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.model.VillageSurveyModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;


public class StaffPlantingGrowerDetails extends AppCompatActivity {

    DBHelper dbh;
    Context context;
    List<VillageSurveyModel> villageModelList;
    List<GrowerModel> growerModelList;
    List<GrowerSurveyModel> growerSurveyModelsList;
    Spinner village, grower;
    List<UserDetailsModel> userDetailsModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_grower_details_cm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = StaffPlantingGrowerDetails.this;
        setTitle(getString(R.string.MENU_PLOT_FINDER_PATH_FINDER));
        toolbar.setTitle(getString(R.string.MENU_PLOT_FINDER_PATH_FINDER));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        villageModelList = new ArrayList<>();
        growerModelList = new ArrayList<>();
        growerSurveyModelsList = new ArrayList<>();
        village = findViewById(R.id.village);
        grower = findViewById(R.id.grower);
        dbh = new DBHelper(context);
        userDetailsModels = dbh.getUserDetailsModel();

        getVillage();

        ArrayList<String> growerdata = new ArrayList<String>();
        growerdata.add("All Grower");
        ArrayAdapter<String> adaptergrower = new ArrayAdapter<String>(context,
                R.layout.list_item, growerdata);
        grower.setAdapter(adaptergrower);

        growerModelList = new ArrayList<>();


    }


    public void getVillage() {
        try {
            villageModelList = dbh.getVillageModel("");
            ArrayList<String> data = new ArrayList<String>();
            data.add("All Village");
            for (int i = 0; i < villageModelList.size(); i++) {
                data.add(villageModelList.get(i).getVillageCode()+" / "+villageModelList.get(i).getVillageName());
            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            village.setAdapter(adaptersupply);
            village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        getGrower("");
                    } else {
                        getGrower(""+villageModelList.get(village.getSelectedItemPosition() - 1).getVillageCode());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
        }

    }

    public void getGrower(final String villCode) {
        try {
            growerModelList = dbh.getGrowerModel(villCode, "");
            if (growerSurveyModelsList.size() > 0)
                growerSurveyModelsList.clear();
            GrowerSurveyModel growerSurveyModel1 = new GrowerSurveyModel();
            growerSurveyModel1.setVillageCode("");
            growerSurveyModel1.setGrowerCode("");
            growerSurveyModel1.setGrowerfatherName("");
            growerSurveyModel1.setGrowerName("All Grower");
            growerSurveyModelsList.add(growerSurveyModel1);
            ArrayList<String> data = new ArrayList<String>();
            data.add("All Grower");
            for (int i = 0; i < growerModelList.size(); i++) {
                GrowerSurveyModel growerSurveyModel = new GrowerSurveyModel();
                growerSurveyModel.setVillageCode(growerModelList.get(i).getVillageCode());
                growerSurveyModel.setGrowerCode(growerModelList.get(i).getGrowerCode());
                growerSurveyModel.setGrowerName(growerModelList.get(i).getGrowerName());
                growerSurveyModel.setGrowerfatherName(growerModelList.get(i).getGrowerFather());
                data.add(growerModelList.get(i).getGrowerCode()+" / "+growerModelList.get(i).getGrowerName()+"  /  "+growerModelList.get(i).getGrowerFather());
                growerSurveyModelsList.add(growerSurveyModel);
            }
            ArrayAdapter<String> adaptersupply = new ArrayAdapter<String>(context,
                    R.layout.list_item, data);
            grower.setAdapter(adaptersupply);
            grower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //new getGrowerDetails().execute(villageModelList.get(village.getSelectedItemPosition()).getVillageCode());
                    new getGrowerDetails().execute(villCode, growerSurveyModelsList.get(grower.getSelectedItemPosition()).getGrowerCode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getGrowerDetails extends AsyncTask<String, Integer, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override

        protected void onPreExecute() {
            if (village.getSelectedItemPosition() >0) {
                super.onPreExecute();
                dialog.setTitle(getString(R.string.app_name_language));
                //dialog.setIndeterminate(false);
                dialog.setMessage(getString(R.string.MSG_PLEASE_WAIT));
            /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);*/
                dialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                if (village.getSelectedItemPosition() >0) {
                    String url = APIUrl.BASE_URL_CANE_DEVELOPMENT + "/PlantingGrowerDetails";
                    HttpClient httpClient = new DefaultHttpClient();

                    //MultipartEntity entity = new MultipartEntity();
                    List<NameValuePair> entity = new LinkedList<NameValuePair>();
                    entity.add(new BasicNameValuePair("IMEINO", new GetDeviceImei(context).GetDeviceImeiNumber()));
                    entity.add(new BasicNameValuePair("DIVN", userDetailsModels.get(0).getDivision()));
                    entity.add(new BasicNameValuePair("V_Code", params[0]));
                    entity.add(new BasicNameValuePair("G_Code", params[1]));
                    /*  entity.add(new BasicNameValuePair("lang",getString(R.string.language)));*/
                    //entity.add(new BasicNameValuePair("LAT",params[0]));
                    //entity.add(new BasicNameValuePair("LON",params[1]));


                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(entity, "utf-8"));
                    //httpPost.setEntity(entity);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    message = httpClient.execute(httpPost, responseHandler);
                    //HttpResponse response = httpClient.execute(httpPost,localContext);
                    //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                    System.out.println("sdfsdsd : " + message);
                }
                } catch(SecurityException e){
                    Log.e("Exception", e.getMessage());
                    message = e.getMessage();
                } catch(Exception e){
                    Log.e("Exception", e.getMessage());
                    message = e.getMessage();
                }
                return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (village.getSelectedItemPosition() >0) {
                    growerModelList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(message);
                    if (jsonArray.length() == 0) {
                        new AlertDialogManager().AlertPopUp(context, "No data found");
                    } else {
                        GrowerModel header = new GrowerModel();
                        header.setVillageCode("Vill Code");
                        header.setGrowerCode("Grower Code");
                        header.setGrowerName("Grower Name");
                        header.setGrowerFather("Grower Father");
                        header.setCentre("Centre");
                        header.setSociety("Society");
                        header.setMobile("Mobile");
                        header.setArea("Area");
                        header.setColor("#000000");
                        header.setTextColor("#FFFFFF");
                        growerModelList.add(header);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            GrowerModel growerModel = new GrowerModel();
                            growerModel.setVillageCode(jsonObject1.getString("G_VILL"));
                            growerModel.setGrowerCode(jsonObject1.getString("G_NO"));
                            growerModel.setGrowerName(jsonObject1.getString("G_NAME"));
                            growerModel.setGrowerFather(jsonObject1.getString("G_FATHER"));
                            growerModel.setCentre(jsonObject1.getString("CN_NAME"));
                            growerModel.setSociety(jsonObject1.getString("SO_NAME"));
                            growerModel.setMobile(jsonObject1.getString("G_ENTERTAINMENT_TELEPHONE"));
                            growerModel.setArea(jsonObject1.getString("G_T_AREA"));
                            if (i % 2 == 0) {
                                growerModel.setColor("#DFDFDF");
                                growerModel.setTextColor("#000000");
                            } else {
                                growerModel.setColor("#FFFFFF");
                                growerModel.setTextColor("#000000");
                            }
                            growerModelList.add(growerModel);
                        }
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_list);
                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
                    //recyclerView.setLayoutManager(manager);
                    //gridLayoutManager=new GridLayoutManager(MainActivity.this,8,RecyclerView.HORIZONTAL,true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(manager);
                    StaffPlantingGrowerListAdapter stockSummeryAdapter = new StaffPlantingGrowerListAdapter(context, growerModelList);
                    recyclerView.setAdapter(stockSummeryAdapter);
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            } catch (JSONException e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, message);
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                //textView.setText("Error:"+e.toString());
                new AlertDialogManager().AlertPopUpFinish(context, "Error:" + e.toString());
                if (dialog.isShowing())
                    dialog.dismiss();
            }

        }
    }


}
