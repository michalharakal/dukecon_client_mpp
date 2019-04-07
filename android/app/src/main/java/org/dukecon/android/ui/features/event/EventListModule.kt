package org.dukecon.android.ui.features.event

import dagger.Module
import dagger.Provides
import org.dukecon.presentation.feature.event.EventDateListContract
import org.dukecon.presentation.feature.event.EventDatePresenter
import org.dukecon.presentation.feature.event.EventListContract
import org.dukecon.presentation.feature.event.EventListPresenter

@Module
class EventListModule {

    @Provides
    fun provideSessionDatePresenter(impl: EventDatePresenter): EventDateListContract.Presenter {
        return impl
    }

    @Provides
    fun sessionListPresenter(impl: EventListPresenter): EventListContract.Presenter {
        return impl
    }
}