package org.dukecon.android.ui.injection

import dagger.Module
import dagger.Provides
import org.dukecon.data.repository.DukeconDataRepository
import org.dukecon.domain.repository.ConferenceRepository
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    internal fun provideEventRepository(dukeconDataRepository: DukeconDataRepository): ConferenceRepository {
        return dukeconDataRepository
    }
}