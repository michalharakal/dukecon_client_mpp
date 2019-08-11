package org.dukecon

import kotlinx.coroutines.runBlocking
import org.dukecon.repository.api.DukeconApi

fun main()  {
    val api = DukeconApi("https://www.apachecon.com/acna19/s/rest/", "acna2019.json")
    runBlocking<Unit> {
        val conferences = api.getConference("acna2019.json")
        println(conferences)
    }
}
