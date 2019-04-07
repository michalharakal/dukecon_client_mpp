package org.dukecon.oauth.api.code;

import org.dukecon.oauth.api.code.service.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.dukecon.oauth.api.code.model.OAuthToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OauthApi {
  /**
   * submit refresh code to obtain oauth token
   * 
   * @param clientId client id (optional)
   * @param grantType grant type (optional)
   * @param redirectUri redirect uri (optional)
   * @param code code (optional)
   * @return Call&lt;OAuthToken&gt;
   */
  @retrofit2.http.FormUrlEncoded
  @POST("token")
  Call<OAuthToken> postCode(
    @retrofit2.http.Field("client_id") String clientId, @retrofit2.http.Field("grant_type") String grantType, @retrofit2.http.Field("redirect_uri") String redirectUri, @retrofit2.http.Field("code") String code
  );

}
