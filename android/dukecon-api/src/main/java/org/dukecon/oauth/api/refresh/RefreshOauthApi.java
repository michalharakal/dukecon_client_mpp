package org.dukecon.oauth.api.refresh;

import org.dukecon.oauth.api.refresh.service.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.dukecon.oauth.api.refresh.model.OAuthToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RefreshOauthApi {
  /**
   * submit refresh code to obtain oauth token
   * 
   * @param clientId client id (optional)
   * @param grantType grant type (optional)
   * @param scope scope (optional)
   * @param refreshToken refresh_token (optional)
   * @return Call&lt;OAuthToken&gt;
   */
  @retrofit2.http.FormUrlEncoded
  @POST("token")
  Call<OAuthToken> refresh(
    @retrofit2.http.Field("client_id") String clientId, @retrofit2.http.Field("grant_type") String grantType, @retrofit2.http.Field("scope") String scope, @retrofit2.http.Field("refresh_token") String refreshToken
  );

}
