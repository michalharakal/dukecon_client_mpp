package org.dukecon.android.ui.features.eventdetail.di

import dagger.Module
import dagger.Provides
import org.dukecon.android.ui.features.speakerdetail.SpeakerNavigator
import org.dukecon.presentation.feature.eventdetail.EventDetailContract
import org.dukecon.presentation.feature.eventdetail.EventDetailPresenter

@Module
class EventDetailModule(val speakerNavigator: SpeakerNavigator) {

    @Provides
    fun provideSpeakerNavigator(): SpeakerNavigator {
        return speakerNavigator
    }

    @Provides
    fun provideEventDetailPresenter(impl: EventDetailPresenter): EventDetailContract.Presenter {
        return impl
    }
}