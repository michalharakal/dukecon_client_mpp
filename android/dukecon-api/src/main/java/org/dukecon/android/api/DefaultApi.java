package org.dukecon.android.api;

import org.dukecon.android.api.service.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.dukecon.android.api.model.Conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DefaultApi {
  /**
   * returns list of conferences
   * 
   * @return Call&lt;List&lt;Conference&gt;&gt;
   */
  @GET("conferences")
  Call<List<Conference>> getAllConferences();
    

  /**
   * Conference styles
   * 
   * @param id  (required)
   * @return Call&lt;String&gt;
   */
  @GET("conferences/{id}/styles.css")
  Call<String> getConferenceStyles(
    @retrofit2.http.Path("id") String id
  );

}
