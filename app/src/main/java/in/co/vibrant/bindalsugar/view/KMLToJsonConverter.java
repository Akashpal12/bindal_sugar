package in.co.vibrant.bindalsugar.view;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

public class KMLToJsonConverter {
    public static String convertKMLtoJSON(String kmlFilePath) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new File(kmlFilePath));
            Element rootElement = document.getRootElement();

            JSONObject json = new JSONObject();
            JSONArray placemarksArray = new JSONArray();

            List<Element> placemarks = rootElement.getChildren("Placemark");
            for (Element placemark : placemarks) {
                JSONObject placemarkObject = new JSONObject();
                placemarkObject.put("name", placemark.getChildText("name"));
                placemarkObject.put("description", placemark.getChildText("description"));

                // Add more data fields as needed
                // Example: placemarkObject.put("someKey", placemark.getChildText("someElement"));

                placemarksArray.put(placemarkObject);
            }

            json.put("placemarks", placemarksArray);

           /* StringWriter stringWriter = new StringWriter();
            json.write(stringWriter);*/
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
