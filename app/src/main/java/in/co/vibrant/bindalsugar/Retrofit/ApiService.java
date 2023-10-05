package in.co.vibrant.bindalsugar.Retrofit;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("checkPlantingPolygonGrower_New")
    Call<ResponseBody> checkPlantingPolygonGrower_New(
            @Query("Divn") String Divn,
            @Query("lat") String lat,
            @Query("lng") String lng );


    @GET("CHECKSEEDQUANTITY")
    Call<JsonObject> CHECKSEEDQUANTITY(
            @Query("FactId") String FactId,
            @Query("Village") String Village,
            @Query("Grower") String Grower,
            @Query("Variety") String Variety ,
            @Query("Type") String Type ,
            @Query("Season") String Season);




}
