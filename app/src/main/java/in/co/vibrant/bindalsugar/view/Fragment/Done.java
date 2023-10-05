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
import in.co.vibrant.bindalsugar.adapter.RemedialJsonDoneDataAdapter;
import in.co.vibrant.bindalsugar.adapter.SessionConfig;
import in.co.vibrant.bindalsugar.model.RemedialJsonDoneModel;
import in.co.vibrant.bindalsugar.model.RemedialJsonPendingModel;
import in.co.vibrant.bindalsugar.model.UserDetailsModel;
import in.co.vibrant.bindalsugar.util.APIUrl;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.GetDeviceImei;

public class Done extends Fragment {

    List<UserDetailsModel> userDetailsModels;
    DBHelper dbh;
    SQLiteDatabase db;
    View view;
    JSONArray JsonData;
    List<RemedialJsonDoneModel> DoneModelList;
    List<RemedialJsonPendingModel> PendingModelList;
    RecyclerView plot_list_recycler_view;
    SessionConfig sessionConfig;
    String Lat,Lng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_done, container, false);
        dbh = new DBHelper(getContext());
        db = new DBHelper(getContext()).getWritableDatabase();
        sessionConfig = new SessionConfig(getContext());
        userDetailsModels=new ArrayList<>();
        DoneModelList= new ArrayList<RemedialJsonDoneModel>();
        PendingModelList= new ArrayList<>();
        userDetailsModels=dbh.getUserDetailsModel();
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
                        for (int i = 0; i < JsonData.length(); i++) {

                            JSONObject jsonObject = JsonData.getJSONObject(i);
                            RemedialJsonDoneModel remedialJsonDoneModel = new RemedialJsonDoneModel();

                            if (jsonObject.getString("STATUS").equalsIgnoreCase("PENDING")) {}
                            else {
                                remedialJsonDoneModel.setREMICODE(jsonObject.getString("REMICODE"));
                                remedialJsonDoneModel.setREMIDIALNAME(jsonObject.getString("REMIDIALNAME"));
                                remedialJsonDoneModel.setDAYES(jsonObject.getString("DAYES"));
                                remedialJsonDoneModel.setSTATUS(jsonObject.getString("STATUS"));  // DONE // PENDING
                                DoneModelList.add(remedialJsonDoneModel);
                            }


                        }
                        plot_list_recycler_view = view.findViewById(R.id.plot_list_recycler_view);
                        GridLayoutManager manager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                        plot_list_recycler_view.setHasFixedSize(true);
                        plot_list_recycler_view.setLayoutManager(manager);
                        RemedialJsonDoneDataAdapter listGrowerFinderAdapter = new RemedialJsonDoneDataAdapter(getContext(), DoneModelList);
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