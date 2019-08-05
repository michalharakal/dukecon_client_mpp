package org.dukecon

import kotlinx.coroutines.runBlocking
import org.dukecon.repository.api.DukeconApi

fun main()  {
    val api = DukeconApi("https://programm.javaland.eu/2019/rest", "javaland")
    runBlocking<Unit> {
        val conferences = api.getConference("javaland2019")
        println(conferences)
    }
}
