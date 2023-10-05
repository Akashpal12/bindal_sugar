package in.co.vibrant.bindalsugar.view;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.KmlModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    List<KmlModel> kmlModelList = new ArrayList<>();
    boolean isKmlData = false;
    ProgressDialog dialog;
    private GoogleMap mMap;

    private JSONArray kmlToJSON(String kmlString) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(new StringReader(kmlString)));
            document.getDocumentElement().normalize();

            NodeList placemarks = document.getElementsByTagName("Placemark");
            List<JSONObject> placemarkList = new ArrayList<>();

            for (int i = 0; i < placemarks.getLength(); i++) {
                Element placemarkElement = (Element) placemarks.item(i);
                String name = placemarkElement.getElementsByTagName("name").item(0).getTextContent();
                String description = placemarkElement.getElementsByTagName("description").item(0).getTextContent();
                String coordinates = placemarkElement.getElementsByTagName("coordinates").item(0).getTextContent();
                //  String Polygon = placemarkElement.getElementsByTagName("Polygon").item(0).getTextContent();
                // String MultiGeometry = placemarkElement.getElementsByTagName("MultiGeometry").item(0).getTextContent();

                JSONObject placemarkJSON = new JSONObject();
                placemarkJSON.put("name", name);
                //  placemarkJSON.put("description", description);
                placemarkJSON.put("coordinates", coordinates);
                //  placemarkJSON.put("polygon", Polygon);

                placemarkList.add(placemarkJSON);
            }

            JSONArray jsonResult = new JSONArray(placemarkList);

            return jsonResult;

        } catch (Exception e) {
            new AlertDialogManager().RedDialog(MainActivity.this, e.getMessage());
            Log.d("KMLJSON Exception", "\n" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Print JavaVersion
        String javaVersion = System.getProperty("java.version");
        Log.d("JavaVersion", "Java Version: " + javaVersion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        AppCompatButton btnRefresh = findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(view -> {
            finish();
        });

        dialog = ProgressDialog.show(MainActivity.this, getString(R.string.app_name), "Please Wait ", true);
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 5000, null);
        LatLng def = new LatLng(28.8180, 78.1858);
        CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(def, 19);
        mMap.animateCamera(locations);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //  mMap.setMyLocationEnabled(true);
        //  GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        //  downloadKMZFile("https://wavedev.vibsugar.com//uploads/data/KMZ/diseasedplots.kmz",mMap);
          //downloadKmzFile(mMap);

        downloadAndSaveKMZFile();


        // kmlFileMap(mMap);
    }

    private void downloadAndSaveKMZFile() {
        String kmzUrl = "https://wavedev.vibsugar.com//uploads/data/KMZ/diseasedplots.kmz";
        String kmzFileName = "diseasedplots.kmz";
        deleteExistingKMZFile(kmzFileName);
        new DownloadTask(this,kmzFileName).execute(kmzUrl);
    }

    private void downloadKmzFile(GoogleMap mMap) {
        String kmzUrl = "https://wavedev.vibsugar.com//uploads/data/KMZ/diseasedplots.kmz";
        String kmzFileName = "diseasedplots.kmz";

       // deleteExistingKMZFile(kmzFileName);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(kmzUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, kmzFileName);

        request.setTitle("Download File");
        request.setDescription("diseased plots");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Enqueue the download request
        long downloadId = downloadManager.enqueue(request);

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File kmzFile = new File(directory, kmzFileName);

        if (kmzFile.exists()) {
            kmlFileMap(kmzFile);
        } else {
            Toast.makeText(this, "file doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteExistingKMZFile(String kmzFileName) {
        String fileName = kmzFileName;
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        if (file.exists()) {
            file.delete();
        }
    }


    private void kmlFileMap(File kmzFile) {

        ZipInputStream zipInputStream = null;

        try {

            FileInputStream kmzInputStream = new FileInputStream(kmzFile);

           // InputStream kmzInputStream = getResources().openRawResource(R.raw.allkmz);
            //  InputStream kmzInputStream = getResources().openRawResource(R.raw.ndvi);
            zipInputStream = new ZipInputStream(kmzInputStream);
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".kml")) {

                    String kmlData = readInputStream(zipInputStream); // Read the KML data
                    //  kmlData= DataString.kmlString;

                    Log.e("KML Content", kmlData);

                    byte[] kmlBytes = kmlData.getBytes(StandardCharsets.UTF_8);
                    KmlLayer kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(kmlBytes), getApplicationContext());
                    kmlLayer.addLayerToMap();

                    // Set up a click listener for polygons (KML shapes)
                    mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                        @Override
                        public void onPolygonClick(Polygon polygon) {

                            LatLng clickedLatLng = polygon.getPoints().get(0); // Get the first point of the polygon

                            Log.e("KML LatLng", "" + clickedLatLng);

                            Toast.makeText(MainActivity.this, "" + clickedLatLng, Toast.LENGTH_SHORT).show();
                        }
                    });

                    //convert KML string to JSONArray
                    JSONArray kmlJson = kmlToJSON(kmlData);
                    dialog.dismiss();
                    Log.d("Response kmlJson :", "---->" + kmlJson);
                    for (int i = 0; i < kmlJson.length(); i++) {

                        JSONObject jsonObject = kmlJson.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String coordinates = jsonObject.getString("coordinates");

                        kmlModelList.add(new KmlModel(name, coordinates));

                        Log.d("Response kml :name :", "---->" + name);
                        Log.d("Response kml :coordinates :", "---->" + coordinates);
                    }

                    if (kmlModelList.isEmpty()) {
                        new AlertDialogManager().AlertPopUp(this, "Data not found..!");
                    } else {
                        new AlertDialogManager().AlertPopUp(this, "" + kmlJson);
                        // new AlertDialogManager().AlertPopUp(this,""+kmlModelList.get(0).getName());
                    }
                }
            }

            //  zipInputStream.close();
        } catch (IOException e) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.e("KML Display Error", "Error displaying KML file: " + e.getMessage());

            // You can also show a notification or toast message to inform the user
            Toast.makeText(getApplicationContext(), "Error displaying KML file", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            dialog.dismiss();
            throw new RuntimeException(e);
        } /*finally {
             dialog.dismiss();
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        inputStreamReader.close();

        return stringBuilder.toString();
    }

    private void getMap(GoogleMap googleMap) {

        mMap = googleMap;
        //  mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        try {
           /* boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.style_json_dark));
            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }else{*/
            // for default camera focus
            mMap = googleMap;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 5000, null);
            //LatLng def = new LatLng(12.9716, 77.5946); //bangalore
            LatLng def = new LatLng(28.8180, 78.1858); //bangalore
            CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(def, 19);
            mMap.animateCamera(locations);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            InputStream kmzInputStream = getResources().openRawResource(R.raw.ndvi);
            Log.d("KML kmzInputStream :", "-->" + kmzInputStream);
            // Create a ZipInputStream to extract KML data
            ZipInputStream zipInputStream = new ZipInputStream(kmzInputStream);
            Log.d("KML zipInputStream :", "-->" + zipInputStream);
            ///   String kmlData = readInputStream(zipInputStream); // Read the KML data
            String kmlData = "";
           /* kmlData = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <kml xmlns="http://www.opengis.net/kml/2.2">
                            <Placemark>
                                <name>Sample Placemark</name>
                                <description><![CDATA[
                                    <h1>This is a sample placemark</h1>
                                    <p>This is a description with <a href="https://www.example.com">a link</a>.</p>
                                    <p>You can add line breaks:<br>Like this.</p>
                                ]]></description>
                                <Point>
                                    <coordinates>-122.084143,37.422006,0</coordinates>
                                </Point>
                            </Placemark>
                        </kml>
                    """.trim();*/

            Log.d("KML Data :", "-->" + kmlData);
            Toast.makeText(getApplicationContext(), "-->" + kmlData, Toast.LENGTH_LONG).show();

            //fetch KML Layer from local file
            //  KmlLayer layer = new KmlLayer(googleMap, R.raw.all, getApplicationContext());
            KmlLayer layer = new KmlLayer(googleMap, new ByteArrayInputStream(kmlData.getBytes()), getApplicationContext());

            //add KML Layer on map
            layer.addLayerToMap();
            //KML layer click listener
            layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(com.google.maps.android.data.Feature feature) {
                    Toast.makeText(getApplicationContext(), "Name: " + feature.getProperty("name") + "\nDescription: " + feature.getProperty("description"), Toast.LENGTH_LONG).show();
                }
            });
            //fetch name and description from local KML Layer file
            for (KmlContainer containers : layer.getContainers()) {
                for (KmlPlacemark placemark : containers.getPlacemarks()) {
                    KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
                    for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
                        builder.include(latLng);
                    }
                }
            }
            //for camera focus
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels - 40;
            int height = getResources().getDisplayMetrics().heightPixels - 40;
            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
            //  }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private String kmzFileName;

        DownloadTask(Context context, String kmzFileName) {
            this.context = context;
            this.kmzFileName = kmzFileName;
        }

        @Override
        protected Boolean doInBackground(String... urls) {


            try {
                String downloadUrl = urls[0];

                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Create a file in the external storage directory
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(directory, kmzFileName);

                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.close();
                inputStream.close();
                connection.disconnect();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(context, "Download and save successful", Toast.LENGTH_SHORT).show();
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File kmzFile = new File(directory, kmzFileName);

                if (kmzFile.exists()) {
                    kmlFileMap(kmzFile);
                } else {
                    Toast.makeText(MainActivity.this, "file doesn't exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Download and save failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isKmlData = false;
    }

    private void downloadKMZFile(String kmzUrl, GoogleMap mMap) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(kmzUrl)
                    .build();


            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    // Save the KMZ file locally
                    unzipKMZ(body.bytes(), mMap);
                }
            } else {
                // Handle download failure
                Toast.makeText(this, "download failed", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            new AlertDialogManager().RedDialog(this, e.getMessage());
            // Handle download failure
        }
    }

    private void unzipKMZ(byte[] kmzData, GoogleMap mMap) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(kmzData);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream);
             ByteArrayOutputStream kmlOutputStream = new ByteArrayOutputStream()) {

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".kml")) {
                    // Found a KML file inside the KMZ
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        kmlOutputStream.write(buffer, 0, bytesRead);
                    }
                    // Process the KML data in kmlOutputStream
                    String kmlContent = kmlOutputStream.toString("UTF-8");
                    // Now you can use the KML content
                    processKMLContent(kmlContent, mMap);

                    // kmlFileMap(kmlContent,mMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle unzip and processing errors
        }
    }

    private void processKMLContent(String kmlContent, GoogleMap mMap) {
        // Parse and process the KML content as needed
        // You may use libraries or custom code to handle KML data.
        byte[] kmlBytes = kmlContent.getBytes(StandardCharsets.UTF_8);
        KmlLayer kmlLayer = null;
        try {
            kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(kmlBytes), getApplicationContext());

            kmlLayer.addLayerToMap();

            // Set up a click listener for polygons (KML shapes)
            mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(Polygon polygon) {

                    LatLng clickedLatLng = polygon.getPoints().get(0); // Get the first point of the polygon

                    Log.e("KML LatLng", "" + clickedLatLng);

                    Toast.makeText(MainActivity.this, "" + clickedLatLng, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}