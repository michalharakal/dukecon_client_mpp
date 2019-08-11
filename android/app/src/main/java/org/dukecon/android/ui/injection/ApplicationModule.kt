package org.dukecon.android.ui.injection

import android.app.Application
import android.content.Context
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
import org.dukecon.android.ui.features.login.DummyDukeconAuthManager
import org.dukecon.android.ui.features.login.SettingsTokenStorage
import org.dukecon.android.ui.storage.AndroidStorage
import org.dukecon.common.data.DukeconDataKtorRepository
import org.dukecon.domain.aspects.auth.AuthManager
import org.dukecon.domain.aspects.storage.ApplicationStorage
import org.dukecon.domain.aspects.twitter.TwitterLinks
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.domain.repository.ConferenceRepository
import org.dukecon.presentation.IoContextProvider
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
    @Singleton
    internal fun providetwitterLinkMapper(): TwitterLinks {
        return TwitterLinks()
    }

    @Provides
    fun provideSpeakerMapper(): org.dukecon.presentation.mapper.SpeakerMapper {
        return org.dukecon.presentation.mapper.SpeakerMapper()
    }

    @Provides
    fun provideSpeakerDetailMapper(twitterLinks: TwitterLinks): org.dukecon.presentation.mapper.SpeakerDetailMapper {
        return org.dukecon.presentation.mapper.SpeakerDetailMapper(twitterLinks)
    }

    @Provides
    internal fun providePreEventMapper(speakersMapper: org.dukecon.presentation.mapper.SpeakerMapper): org.dukecon.presentation.mapper.EventMapper {
        return org.dukecon.presentation.mapper.EventMapper(speakersMapper)
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


    /*
    @Provides
    internal fun provideEventRemoteDataStore(eventRemote: EventRemote): EventRemoteDataStore {
        return EventRemoteDataStore(eventRemote)
    }

*/

    @Provides
    internal fun provideioContextProvider(): IoContextProvider {
        return object : IoContextProvider {
            override fun getMain(): CoroutineContext {
                return Dispatchers.Main
            }

            override fun getIoContext(): CoroutineContext {
                return Dispatchers.IO
            }
        }
    }


    @Provides
    fun provideAuthManager(dukeconAuthManager: DummyDukeconAuthManager): AuthManager {
        return dukeconAuthManager
    }


    @Provides
    @Singleton
    internal fun provideEventRepository(settings: ApplicationStorage): ConferenceRepository {
        return DukeconDataKtorRepository("", "", settings)
    }

    @Provides
    fun providesAppStorage(context: Context): ApplicationStorage {
        return AndroidStorage(context)
    }


    @Provides
    fun providesTokensStorage(settings: SettingsTokenStorage): TokensStorage {
        return settings
    }
}

