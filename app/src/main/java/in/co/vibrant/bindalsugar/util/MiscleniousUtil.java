package in.co.vibrant.bindalsugar.util;

import org.apache.http.NameValuePair;

import java.util.List;

public class MiscleniousUtil {
    public String ListNameValueToString(String method, List<NameValuePair> entity) {
        String paramString = method + "{";
        for (int i = 0; i < entity.size(); i++) {
            paramString += entity.get(i) + "; ";
        }
        paramString += " }";
        return paramString;
    }

}