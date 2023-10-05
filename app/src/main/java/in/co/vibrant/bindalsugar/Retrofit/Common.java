package in.co.vibrant.bindalsugar.Retrofit;

public class Common {
    private Common(){}

    public static final String BASE_URL = "http://dev.bindalps.com/potatodovelopment.asmx/";
    public static final String BASE_URL2 = "http://dev.bindalps.com/CaneDevelopmentService.asmx/";
    public static final String BASE_URL3 = "http://dev.bindalps.com/AgriDistService.asmx/";
    public static ApiService getPatatoDevService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
   public static ApiService getCaneDevService() {
        return RetrofitClient.getClient(BASE_URL2).create(ApiService.class);
    }
    public static ApiService getAgriDistService() {
        return RetrofitClient.getClient(BASE_URL3).create(ApiService.class);
    }
}
