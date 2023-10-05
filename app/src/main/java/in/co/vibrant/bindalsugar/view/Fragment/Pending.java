package in.co.vibrant.bindalsugar.view.Fragment;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.RemedialJsonPendingDataAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.RemedialJsonPendingModel;
import in.co.vibrant.bindalsugar.model.RemedialJsonPendingNewModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class Pending extends Fragment {
    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;
    SQLiteDatabase db;
    View view;
    JSONArray JsonData;
    List<RemedialJsonPendingModel> PendingModelList;
    List<RemedialJsonPendingNewModel> PendingModelNewList;
    RecyclerView plot_list_recycler_view;
    SessionConfig sessionConfig;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_pending, container, false);

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_done, container, false);
        dbh = new DBHelper(getContext());
        db = new DBHelper(getContext()).getWritableDatabase();
        userDetailsModels=new ArrayList<>();
        PendingModelList= new ArrayList<>();
        PendingModelNewList= new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
        sessionConfig = new SessionConfig(getContext());
        new GetDataList().execute(sessionConfig.getLat(),sessionConfig.getLng());

        return view;
    }

    private class GetDataList extends AsyncTask<String, Void, Void> {
        String message;
        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getContext(), getString(R.string.app_name),
                    "Please Wait ", true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            Log.d("LatLong :----->>>", "" + params[0]);
            try {
                GetDeviceImei getDeviceImei = new GetDeviceImei(getContext());
                String url = APIUrl.BASE_URL + "/checkRemidialPolygonGrower_New1";
                //String url = "http://demo.asginnovations.in/teafarmservice.asmx/GETMASTER";
                //int i = Integer.parseInt(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                List<NameValuePair> entity = new LinkedList<NameValuePair>();
                //MultipartEntity entity = new MultipartEntity();
                entity.add(new BasicNameValuePair("lat", params[0]));
                entity.add(new BasicNameValuePair("lng", params[1]));
                entity.add(new BasicNameValuePair("Divn", userDetailsModels.get(0).getDivision()));
                String paramString = URLEncodedUtils.format(entity, "utf-8");
                HttpGet httpGet = new HttpGet(url + "?" + paramString);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                message = httpClient.execute(httpGet, responseHandler);

            } catch (SecurityException e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                message = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            try {
                JSONObject obj = new JSONObject(message);
                if (obj.getString("API_STATUS").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = obj.getJSONArray("DATA");
                    JsonData = jsonArray.getJSONObject(0).getJSONArray("JSONDATA");
                    if (jsonArray.length() == 0) {
                        new AlertDialogManager().RedDialog(getContext(), "Error: No details found");
                    } else {
                        double totalNumber = 0;
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            RemedialJsonPendingNewModel remedialJsonDoneNewModel = new RemedialJsonPendingNewModel();
                            remedialJsonDoneNewModel.setVillageCode(jsonObject.getString("VILLAGE_ID").replace(".0", "")); //--1
                            remedialJsonDoneNewModel.setVillageName(jsonObject.getString("V_NAME")); //--2
                            remedialJsonDoneNewModel.setGrowerCode(jsonObject.getString("G_CODE").replace(".0", "")); //--3
                            remedialJsonDoneNewModel.setPlotNo(jsonObject.getString("PLOT_SERIAL_NO").replace(".0", "")); //--4
                            remedialJsonDoneNewModel.setPlotVillageCode(jsonObject.getString("PLOTVILLAGE").replace(".0", "")); //--5
                            remedialJsonDoneNewModel.setGrowerName(jsonObject.getString("G_NAME")); //--6
                            remedialJsonDoneNewModel.setDeseaseCode(jsonObject.getString("DISEASECODE")); //--7
                            remedialJsonDoneNewModel.setDisease(jsonObject.getString("DISEASE")); //--8
                            remedialJsonDoneNewModel.setLAT(sessionConfig.getLat());
                            remedialJsonDoneNewModel.setLONG(sessionConfig.getLng());
                            PendingModelNewList.add(remedialJsonDoneNewModel);
                        }
                        for (int i = 0; i < JsonData.length(); i++) {
                            JSONObject jsonObject = JsonData.getJSONObject(i);
                            RemedialJsonPendingModel remedialJsonDoneModel = new RemedialJsonPendingModel();
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("PENDING")) {
                                remedialJsonDoneModel.setREMICODE(jsonObject.getString("REMICODE"));
                                remedialJsonDoneModel.setREMIDIALNAME(jsonObject.getString("REMIDIALNAME"));
                                remedialJsonDoneModel.setDAYES(jsonObject.getString("DAYES"));
                                remedialJsonDoneModel.setSTATUS(jsonObject.getString("STATUS"));  // DONE // PENDING
                                PendingModelList.add(remedialJsonDoneModel);

                            }
                        }
                        plot_list_recycler_view = view.findViewById(R.id.plot_list_recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                        plot_list_recycler_view.setHasFixedSize(true);
                        plot_list_recycler_view.setLayoutManager(manager);
                        RemedialJsonPendingDataAdapter listGrowerFinderAdapter = new RemedialJsonPendingDataAdapter(getContext(), PendingModelList,PendingModelNewList);
                        plot_list_recycler_view.setAdapter(listGrowerFinderAdapter);


                    }
                } else {
                    new AlertDialogManager().RedDialog(getContext(), obj.getString("MSG"));
                }


            } catch (JSONException e) {
                new AlertDialogManager().RedDialog(getContext(), message);
            } catch (Exception e) {
                new AlertDialogManager().RedDialog(getContext(), "Error:" + e.toString());

            }
        }


    }
}