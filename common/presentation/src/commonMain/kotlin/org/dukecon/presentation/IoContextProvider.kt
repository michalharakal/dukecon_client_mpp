package org.dukecon.presentation

import kotlin.coroutines.CoroutineContext

interface IoContextProvider {
    fun getIoContext(): CoroutineContext
    fun getMain(): CoroutineContext
}
