package in.co.vibrant.bindalsugar.Retrofit;

public class MyJsonExtractor {
    // Private constructor to prevent instantiation
    private MyJsonExtractor() {
    }

    public static String extractJsonObjectFromXml(String xmlResponse) {
        try {
            int startIndex = xmlResponse.indexOf("{");
            int endIndex = xmlResponse.lastIndexOf("}") + 1;

            if (startIndex != -1 && endIndex != -1) {
                return xmlResponse.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String extractJsonArrayFromXml(String xmlResponse) {
        try {
            int startIndex = xmlResponse.indexOf("[");
            int endIndex = xmlResponse.lastIndexOf("]") + 1;

            if (startIndex != -1 && endIndex != -1) {
                return xmlResponse.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
