package org.dukecon.android.ui.features.speakerdetail

import dagger.Module
import dagger.Provides
import org.dukecon.presentation.feature.speakerdetail.SpeakerDetailContract
import org.dukecon.presentation.feature.speakerdetail.SpeakerDetailPresenter

@Module
class SpeakerDetailModule {
    @Provides
    fun speakerDetailPresenter(impl: SpeakerDetailPresenter): SpeakerDetailContract.Presenter {
        return impl
    }
}