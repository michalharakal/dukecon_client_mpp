package org.dukecon.android.api;

import org.dukecon.android.api.service.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.dukecon.android.api.model.Conference;
import org.dukecon.android.api.model.Event;
import org.dukecon.android.api.model.Favorite;
import org.dukecon.android.api.model.Feedback;
import org.dukecon.android.api.model.Keycloak;
import org.dukecon.android.api.model.MetaData;
import org.dukecon.android.api.model.Speaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ConferencesApi {
  /**
   * returns conference
   * 
   * @param id  (required)
   * @return Call&lt;Conference&gt;
   */
  @GET("conferences/{id}")
  Call<Conference> getConference(
    @retrofit2.http.Path("id") String id
  );

  /**
   * returns list of conference events
   * 
   * @param id  (required)
   * @return Call&lt;List&lt;Event&gt;&gt;
   */
  @GET("conferences/{id}/events")
  Call<List<Event>> getEvents(
    @retrofit2.http.Path("id") String id
  );

  /**
   * returns list of favorites
   * 
   * @return Call&lt;List&lt;Favorite&gt;&gt;
   */
  @GET("preferences")
  Call<List<Favorite>> getFavorites();
    

  /**
   * returns keycloak setup
   * 
   * @param id  (required)
   * @return Call&lt;Keycloak&gt;
   */
  @GET("conferences/{id}/keycloak.json")
  Call<Keycloak> getKeyCloak(
    @retrofit2.http.Path("id") String id
  );

  /**
   * returns list of conference meta data
   * 
   * @param id  (required)
   * @return Call&lt;MetaData&gt;
   */
  @GET("conferences/{id}/metadata")
  Call<MetaData> getMeta(
    @retrofit2.http.Path("id") String id
  );

  /**
   * returns list of conference speakers
   * 
   * @param id  (required)
   * @return Call&lt;List&lt;Speaker&gt;&gt;
   */
  @GET("conferences/{id}/speakers")
  Call<List<Speaker>> getSpeakers(
    @retrofit2.http.Path("id") String id
  );

  /**
   * submit favorites
   * 
   * @param id conferenceId (required)
   * @param favorite Feedback object that needs to be updated (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("preferences")
  Call<Void> sendFavorites(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Body List<Favorite> favorite
  );

  /**
   * submit feedback to talk
   * 
   * @param id conferenceId (required)
   * @param sessionId session Id (required)
   * @param feedback Feedback object that needs to be updated (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("feedback/event/{id}/{sessionId}")
  Call<Void> updateFeedback(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Path("sessionId") String sessionId, @retrofit2.http.Body Feedback feedback
  );

}
