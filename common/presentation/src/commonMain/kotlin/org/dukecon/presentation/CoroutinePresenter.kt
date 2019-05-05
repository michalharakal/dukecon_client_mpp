package org.dukecon.presentation

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoroutinePresenter<V : BaseView> constructor(private val ioContextProvider: IoContextProvider) : CoroutineScope, BasePresenter<V> {

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        showError(throwable)
    }

    override val coroutineContext: CoroutineContext
        get() = ioContextProvider.getMain() + job + exceptionHandler

    open fun onDestroy() {
        job.cancel()
    }

    // TODO this could be a property instead of method ..
    fun getIoContext(): CoroutineContext = ioContextProvider.getIoContext()
}
