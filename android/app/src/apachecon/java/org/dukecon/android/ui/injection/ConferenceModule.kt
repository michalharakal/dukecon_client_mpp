package org.dukecon.android.ui.injection

import dagger.Module
import dagger.Provides
import org.dukecon.configuration.ApacheConConfiguration
import org.dukecon.configuration.ConferenceConfiguration

@Module
open class ConferenceModule {

    @Provides
    fun provideConfiguration(): ConferenceConfiguration {
        return ApacheConConfiguration()
    }
}
