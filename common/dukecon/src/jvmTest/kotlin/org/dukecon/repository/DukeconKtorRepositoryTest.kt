package org.dukecon.repository

import kotlinx.coroutines.runBlocking
import org.dukecon.data.repository.DukeconDataKtorRepository
import org.dukecon.domain.aspects.storage.ApplicationStorage
import kotlin.test.Test
import kotlin.test.assertTrue

class DukeconDataKtorRepositoryTest {

    val storage: ApplicationStorage = ApplicationStorage()

    @Test
    fun testJavaland() {
        val repository = DukeconDataKtorRepository("https://www.apachecon.com/acna19/s/rest/",
                "acna2019.json",
                storage)
        runBlocking<Unit> {
            repository.update()
            val events = repository.getEvents(9)
            assertTrue { events.isNotEmpty() }
        }
    }
}

