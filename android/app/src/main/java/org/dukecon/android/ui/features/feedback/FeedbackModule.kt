package org.dukecon.android.ui.features.feedback

import dagger.Module
import dagger.Provides
import org.dukecon.presentation.feature.feedback.FeedbackMvp
import org.dukecon.presentation.feature.feedback.FeedbackPresenter

@Module
class FeedbackModule {
    @Provides
    fun presenter(impl: FeedbackPresenter): FeedbackMvp.Presenter {
        return impl
    }
}