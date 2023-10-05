package in.co.vibrant.bindalsugar.util;

import android.util.Log;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class KMLParser {

    public static String KMLtoJSON(String kmlString) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(kmlString));

            JSONArray coordinatesArray = new JSONArray();

           Log.d("KML parser kmlString:--->", ""+  parser.getText());
           Log.d("KML parser DOCUMENT:", ""+  XmlPullParser.END_DOCUMENT);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "coordinates".equals(parser.getName())) {
                    eventType = parser.next();
                    String[] coordinates = parser.getText().split("\\s+");
                    for (String coordinate : coordinates) {
                        String[] parts = coordinate.split(",");
                        if (parts.length == 3) {
                            double longitude = Double.parseDouble(parts[0]);
                            double latitude = Double.parseDouble(parts[1]);
                            double altitude = Double.parseDouble(parts[2]);
                            JSONObject point = new JSONObject();
                            point.put("longitude", longitude);
                            point.put("latitude", latitude);
                            point.put("altitude", altitude);
                            coordinatesArray.put(point);
                        }
                    }
                }
                eventType = parser.next();
            }

            JSONObject result = new JSONObject();
            result.put("coordinates", coordinatesArray);
            return result.toString();
        } catch (XmlPullParserException | IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void parseKML(String kmlFilePath) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new File(kmlFilePath));
            Element rootElement = document.getRootElement();
            Log.d("KML rootElement", "name :" + rootElement);
            // Assuming you want to extract Placemark elements
            List<Element> placemarks = rootElement.getChildren("Placemark");

            for (Element placemark : placemarks) {
                // Extract data from the Placemark element
                String name = placemark.getChildText("name");
                // String description = placemark.getChildText("description");
                Log.e("KML Content", "name :" + name);
                // Extract coordinates (if needed)
                Element point = placemark.getChild("Point");
                if (point != null) {
                    String coordinates = point.getChildText("coordinates");
                    // Process the coordinates as needed
                }

                // Do something with the extracted data
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

