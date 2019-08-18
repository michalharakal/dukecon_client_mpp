package org.dukecon.presentation.feature.event

import kotlinx.coroutines.launch
import org.dukecon.domain.repository.ConferenceRepository
import org.dukecon.presentation.CoroutinePresenter
import org.dukecon.presentation.IoContextProvider


open class EventDatePresenter constructor(
        val conferenceRepository: ConferenceRepository,
        ioContextProvider:IoContextProvider)
    : CoroutinePresenter<EventDateListContract.View>(ioContextProvider), EventDateListContract.Presenter {

    override fun showError(error: Throwable) {
    }

    private lateinit var view: EventDateListContract.View

    private val onRefreshListener: () -> Unit = this::refreshDataFromRepo

    override fun onAttach(view: EventDateListContract.View) {
        //conferenceRepository.onRefreshListeners += onRefreshListener
        this.view = view
        refreshDataFromRepo()
    }

    override fun onDetach() {
        //conferenceRepository.onRefreshListeners -= onRefreshListener
    }

    private var initial: Boolean = true

    private fun refreshDataFromRepo() {
        launch {
            val dates = conferenceRepository.getEventDates()

            if (dates.isEmpty()) {
                view.showNoSessionDates()
            } else {
                view.let {
                    it.showSessionDates(dates)
                    if (initial) {
                        it.scrollToCurrentDay()
                        initial = false
                    }
                }
            }
        }
    }
}