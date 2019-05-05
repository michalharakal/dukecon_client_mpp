package org.dukecon.android.ui.injection

import android.app.Application
import android.content.Context
import android.util.Log
import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dukecon.android.api.ConferencesApi
import org.dukecon.android.cache.ConferenceDataCacheImpl
import org.dukecon.android.cache.PreferencesHelper
import org.dukecon.android.cache.SharedPreferencesTokenStorage
import org.dukecon.android.cache.persistance.ConferenceCacheGsonSerializer
import org.dukecon.android.ui.BuildConfig
import org.dukecon.android.ui.features.login.DukeconAuthManager
import org.dukecon.data.mapper.*
import org.dukecon.data.repository.ConferenceDataCache
import org.dukecon.data.repository.DukeconDataRepository
import org.dukecon.data.repository.EventRemote
import org.dukecon.data.service.OAuthService
import org.dukecon.data.source.ConferenceConfiguration
import org.dukecon.data.source.EventCacheDataStore
import org.dukecon.data.source.EventRemoteDataStore
import org.dukecon.data.source.OAuthConfiguration
import org.dukecon.domain.aspects.auth.AuthManager
import org.dukecon.domain.aspects.twitter.TwitterLinks
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.oauth.api.code.OauthApi
import org.dukecon.oauth.api.refresh.RefreshOauthApi
import org.dukecon.presentation.IoContextProvider
import org.dukecon.presentation.mapper.SpeakerDetailMapper
import org.dukecon.remote.conference.EventRemoteImpl
import org.dukecon.remote.conference.mapper.*
import org.dukecon.remote.oauth.RetrofitOAuthService
import org.dukecon.remote.oauth.interceptor.OauthAuthorizationInterceptor
import org.dukecon.remote.oauth.interceptor.TokenRefreshAfterFailInterceptor
import org.dukecon.remote.oauth.mapper.OAuthTokenMapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Module used to provide dependencies at an application-level.
 */
@Module
open class ApplicationModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    internal fun providePreferencesHelper(context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    internal fun provideEventCache(
            application: Application,
            gson: Gson,
            preferencesHelper: PreferencesHelper
    ): ConferenceDataCache {
        val baseCacheFolder = application.filesDir.absolutePath
        return ConferenceDataCacheImpl(ConferenceCacheGsonSerializer(baseCacheFolder, gson), preferencesHelper)
    }

    @Provides
    @Singleton
    internal fun providetwitterLinkMapper(): TwitterLinks {
        return TwitterLinks()
    }

    @Provides
    internal fun provideEventMapper(): EventMapper {
        return EventMapper()
    }

    @Provides
    fun provideSpeakerMapper(): org.dukecon.presentation.mapper.SpeakerMapper {
        return org.dukecon.presentation.mapper.SpeakerMapper()
    }



    @Provides
    internal fun providePreEventMapper(speakersMapper: org.dukecon.presentation.mapper.SpeakerMapper):  org.dukecon.presentation.mapper.EventMapper {
        return  org.dukecon.presentation.mapper.EventMapper(speakersMapper)
    }

    @Provides
    internal fun provideEventRemote(
            service: ConferencesApi,
            factory: EventEntityMapper,
            speakerMapper: SpeakerEntityMapper,
            roomEntityMapper: RoomEntityMapper,
            feedbackEntityMapper: FeedbackEntityMapper,
            conferenceConfiguration: ConferenceConfiguration,
            keycloakEntityMapper: KeycloakEntityMapper,
            metaDataEntityMapper: MetaDataEntityMapper,
            favoritesEntityMapper: FavoritesEntityMapper
    ): EventRemote {
        return EventRemoteImpl(
                service, conferenceConfiguration.conferenceId, factory, feedbackEntityMapper,
                speakerMapper, roomEntityMapper, keycloakEntityMapper, metaDataEntityMapper,
                favoritesEntityMapper
        )
    }

    class DO_NOT_VERIFY_IMP : javax.net.ssl.HostnameVerifier {
        override fun verify(p0: String?, p1: javax.net.ssl.SSLSession?): Boolean {
            return true
        }
    }

    class XtmImp : javax.net.ssl.X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            val x509Certificates: Array<X509Certificate> = arrayOf()
            return x509Certificates
        }
    }

    @Singleton
    @Provides
    fun provideNonCachedOkHttpClient(application: Application): OkHttpClient {

        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val cache = Cache(application.getCacheDir(), cacheSize)

        val xtm = XtmImp()
        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        try {
            sslContext.init(null, arrayOf<javax.net.ssl.TrustManager>(xtm), java.security.SecureRandom())
        } catch (e: java.security.NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: java.security.KeyManagementException) {
            e.printStackTrace()
        }

        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { it ->
            if (BuildConfig.DEBUG) {
                Log.d("LOGGER", it)
            }
        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory(), xtm)
                .hostnameVerifier(DO_NOT_VERIFY_IMP())
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return Converters.registerOffsetDateTime(gsonBuilder).create()
    }

    @Provides
    @Singleton
    fun provideTokensStorage(application: Application): TokensStorage {
        return SharedPreferencesTokenStorage(application)
    }

    @Provides
    internal fun provideConferenceService(
            client: OkHttpClient,
            gson: Gson,
            conferenceConfiguration: ConferenceConfiguration,
            tokensStorage: TokensStorage,
            oAuthService: OAuthService,
            oAuthTokenMapper: OAuthTokenMapper
    ): ConferencesApi {
        val authorizingOkHttpClient = client.newBuilder()
                .addInterceptor(OauthAuthorizationInterceptor(tokensStorage))
                .addInterceptor(TokenRefreshAfterFailInterceptor(tokensStorage, oAuthService, oAuthTokenMapper))
                .build()
        val restAdapter = Retrofit.Builder()
                .baseUrl(conferenceConfiguration.baseUrl)
                .client(authorizingOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return restAdapter.create(ConferencesApi::class.java)
    }

    @Provides
    internal fun provideOAuthApi(
            client: OkHttpClient,
            gson: Gson,
            oAuthConfiguration: OAuthConfiguration
    ): OauthApi {
        val restAdapter = Retrofit.Builder()
                .baseUrl(oAuthConfiguration.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return restAdapter.create(OauthApi::class.java)
    }

    @Provides
    internal fun provideRefreshOAuthApi(
            client: OkHttpClient,
            gson: Gson,
            oAuthConfiguration: OAuthConfiguration
    ): RefreshOauthApi {
        val restAdapter = Retrofit.Builder()
                .baseUrl(oAuthConfiguration.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return restAdapter.create(RefreshOauthApi::class.java)
    }


    @Provides
    internal fun provideEventEntityMapper(): EventEntityMapper = EventEntityMapper()


    @Provides
    internal fun provideFeedbackEntityMapper(): FeedbackEntityMapper = FeedbackEntityMapper()


    @Provides
    internal fun provideEntityMapper(conferenceConfiguration: ConferenceConfiguration, twitterLinkMapper: TwitterLinks): SpeakerEntityMapper {
        return SpeakerEntityMapper(conferenceConfiguration, twitterLinkMapper)
    }

    @Provides
    internal fun provideEventCacheDataStoreEventCacheDataStore(conferenceDataCache: ConferenceDataCache):EventCacheDataStore {
        return EventCacheDataStore(conferenceDataCache)
    }

    @Provides
    internal fun provideEventCacheDataStoreRoomEntityMapper(): RoomEntityMapper = RoomEntityMapper()

    @Provides
    internal fun providekeycloakEntityMapper(): KeycloakEntityMapper = KeycloakEntityMapper()

    @Provides
    internal fun providemetaDataEntityMapper(): MetaDataEntityMapper = MetaDataEntityMapper()

    @Provides
    internal fun provideFavoritesEntityMapper(): FavoritesEntityMapper = FavoritesEntityMapper()

    @Provides
    internal fun provideEventRemoteDataStore( eventRemote: EventRemote):EventRemoteDataStore {
        return EventRemoteDataStore(eventRemote)
    }

    @Provides
    internal fun provideSpeakerMapper(twitterLinks: TwitterLinks):SpeakerMapper {
        return SpeakerMapper(twitterLinks)
    }

    @Provides
    internal fun provideSpeakerDetailMapper(twitterLinks: TwitterLinks): org.dukecon.presentation.mapper.SpeakerDetailMapper
            = org.dukecon.presentation.mapper.SpeakerDetailMapper(twitterLinks)

    @Provides
    internal fun provideioContextProvider(): IoContextProvider {
        return object:IoContextProvider {
            override fun getMain(): CoroutineContext {
                return Dispatchers.Main
            }

            override fun getIoContext(): CoroutineContext {
                return Dispatchers.IO
            }
        }
    }


    @Provides
    internal fun provideKeycloakMapper():KeycloakMapper {
        return KeycloakMapper()
    }

    @Provides
    internal fun provideFeedbackMapper(): FeedbackMapper {
        return FeedbackMapper()
    }

    @Provides
    internal fun provideFavoriteMapper():FavoriteMapper {
        return FavoriteMapper()
    }

    @Provides
    internal fun provideMetaDateMapper():MetaDateMapper {
        return MetaDateMapper()
    }

    @Provides
    internal fun provideDukeconDataRepository(
            remoteDataStore: EventRemoteDataStore,
            retrofitOauthService: RetrofitOAuthService,
            eventRemoteDataStore: EventRemoteDataStore,
            localDataStore: EventCacheDataStore,
            speakerMapper: SpeakerMapper,
            keycloakMapper: KeycloakMapper,
            feedbackMapper: FeedbackMapper,
            favoriteMapper: FavoriteMapper,
            metadataMapper: MetaDateMapper,
            eventMapper: EventMapper
            ): DukeconDataRepository {
        return DukeconDataRepository(
                remoteDataStore,
                localDataStore,
                eventMapper,
                speakerMapper,
                keycloakMapper,
                RoomMapper(),
                feedbackMapper,
                favoriteMapper,
                metadataMapper)
    }


    @Provides
    internal fun provideOAuthService(retrofitOauthService: RetrofitOAuthService): OAuthService {
        return retrofitOauthService
    }

    @Provides
    fun provideAuthManager(dukeconAuthManager: DukeconAuthManager): AuthManager {
        return dukeconAuthManager
    }

}

