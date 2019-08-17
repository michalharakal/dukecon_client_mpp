package org.dukecon.date

import kotlin.test.Test
import org.dukecon.data.cache.*

class DateJvmTest {
    @Test
    fun testParseStringToDate() {
        val a = "2019-09-09T14:00:00".parseDate()
        assert(a.dayOfMonth == 9)
        assert(a.year == 2019)
        assert(a.hours == 14)
    }
}