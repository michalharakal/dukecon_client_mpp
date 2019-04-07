package org.dukecon.android.ui.features.info

import android.content.Context
import dagger.Module
import dagger.Provides
import org.dukecon.data.repository.LibrariesListRepository
import org.dukecon.domain.repository.LibrariesRepository
import org.dukecon.presentation.feature.info.InfoContract
import org.dukecon.presentation.feature.info.InfoPresenter
import org.dukecon.presentation.feature.info.WebNavigator

@Module
class InfoModule(private val context: Context) {
    @Provides
    fun libraryProvider(): LibrariesRepository {
        return LibrariesListRepository()
    }

    @Provides
    fun WebNavigator(): WebNavigator {
        return AndroidWebNavigator(context)
    }

    @Provides
    fun infoPresenter(impl: InfoPresenter
    ): InfoContract.Presenter {
        return impl
    }
}