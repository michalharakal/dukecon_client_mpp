import kotlinx.coroutines.runBlocking
import org.dukecon.data.api.DukeconApi

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonDukeconApiTest {
    @Test
    fun testJavaland() {
        val api = DukeconApi("https://programm.javaland.eu/2019/rest", "javaland")
        runBlocking<Unit> {
            val conferences = api.getConference("javaland2019")
            assertTrue { conferences.events.isNotEmpty() }
        }
    }
    @Test
    fun testApachecon() {
        val api = DukeconApi("https://www.apachecon.com/acna19/s/rest/", "acna2019.json")
        runBlocking<Unit> {
            val conferences = api.getConference("acna2019.json")
            assertTrue { conferences.events.isNotEmpty() }
        }
    }
}
