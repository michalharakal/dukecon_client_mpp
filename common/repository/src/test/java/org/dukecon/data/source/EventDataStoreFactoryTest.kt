package org.dukecon.data.source

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EventDataStoreFactoryTest {

    private val eventCacheDataStore = mock<EventCacheDataStore>()
    private val eventRemoteDataStore = mock<EventRemoteDataStore>()

    private val factory =
        EventDataStoreFactory(eventCacheDataStore, eventRemoteDataStore)

    @Test
    fun `get remote store retrieves remote data source`() {
        assert(factory.retrieveRemoteDataStore() is EventRemoteDataStore)
    }


    @Test
    fun `get local store retrieves local data source`() {
        assert(factory.retrieveCacheDataStore() is EventCacheDataStore)
    }

    @Test
    fun `get store retrieves local store for chached and valid data`() {
        assert(factory.retrieveDataStore(true, false, false) is EventCacheDataStore)
    }

    @Test
    fun `get store retrieves local store for chached data if no internet`() {
        assert(factory.retrieveDataStore(true, false, false) is EventCacheDataStore)
    }

    @Test
    fun `get store retrieves remote store for not chached data if no internet`() {
        assert(factory.retrieveDataStore(false, false, false) is EventRemoteDataStore)
    }
}