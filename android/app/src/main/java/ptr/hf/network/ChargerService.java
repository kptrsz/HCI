package ptr.hf.network;

import com.google.maps.model.DirectionsResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ptr.hf.model.Reservation;
import ptr.hf.model.ReservationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ChargerService {
//    @GET("openchargemap/v2/poi/")
//    Call<ArrayList<Station>> getStations(
//            @Query("compact") Boolean compact,
//            @Query("verbose") Boolean verbose,
//            @Query("latitude") Double latitude,
//            @Query("longitude") Double longitude,
//            @Query("distance") Integer distance,
////            @Query("countrycode") String countryCode,
//            @Query("maxresults") int maxResults);

//    @GET("openchargemap/v2/poi/")
//    Call<ArrayList<Station>> getStationsHU(
//            @Query("countrycode") String countryCode,
//            @Query("maxresults") int maxResults);

    @POST("reservation")
    Call<ArrayList<ReservationResponse>> postReservation(
            @Body ReservationRequest reservationRequest);


    @GET("reservation")
    Call<ReservationResponse> getReservation(
            @Query("userId") String userId);


    @PUT("reservation")
    Call<ArrayList<ReservationResponse>> putReservation(
            @Body ReservationRequest reservationRequest);

    @DELETE("reservation")
    Call<ReservationResponse> deleteReservation(
            @Query("id") String id);


    @POST("maps/route")
    Call<DirectionsResult> getRoute(
            @Body RouteRequest routeRequest);
//            @Body String routeRequest);

    @GET("settings")
    Call<UserSettings> getSettings(
            @Body String userId);

    @POST("settings")
    Call<UserSettings> postSettings(
            @Body UserSettings userSettings);

    @GET("stations")
    Call<List<Station>> getStationsHu(
//            @Body StationRequest stationRequest);
//            @Query("compact") Boolean compact,
//            @Query("verbose") Boolean verbose,
//            @Query("latitude") Double latitude,
//            @Query("longitude") Double longitude,
//            @Query("distance") Integer distance,
            @Query("countrycode") String countryCode,
            @Query("maxresults") int maxResults);

    @GET("stations")
    Call<List<Station>> getStations(
            @Query("compact") Boolean compact,
            @Query("verbose") Boolean verbose,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude,
            @Query("distance") Integer distance,
//            @Query("countrycode") String countryCode);
            @Query("maxresults") int maxResults);
}
