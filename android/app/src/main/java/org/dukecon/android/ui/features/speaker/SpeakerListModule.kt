package org.dukecon.android.ui.features.speaker

import dagger.Module
import dagger.Provides
import org.dukecon.presentation.feature.speakers.SpeakerListContract
import org.dukecon.presentation.feature.speakers.SpeakerListPresenter

@Module
class SpeakerListModule {

    @Provides
    fun speakerListPresenter(impl: SpeakerListPresenter): SpeakerListContract.Presenter {
        return impl
    }
}