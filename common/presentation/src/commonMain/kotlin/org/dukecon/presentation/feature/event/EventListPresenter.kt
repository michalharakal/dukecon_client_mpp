package org.dukecon.presentation.feature.event

import io.ktor.util.date.GMTDate
import kotlinx.coroutines.launch
import org.dukecon.domain.features.time.CurrentTimeProvider
import org.dukecon.domain.model.Event
import org.dukecon.domain.repository.ConferenceRepository
import org.dukecon.presentation.CoroutinePresenter
import org.dukecon.presentation.mapper.EventMapper

class EventListPresenter constructor(private val currentTimeProvider: CurrentTimeProvider,
                                             private val conferenceRepository: ConferenceRepository,
                                             private val eventsMapper: EventMapper) :
        CoroutinePresenter<EventListContract.View>(), EventListContract.Presenter {

    private val onRefreshListener: () -> Unit = this::refreshDataFromRepo

    override fun onAttach(view: EventListContract.View) {
        this.view = view
        conferenceRepository.onRefreshListeners += onRefreshListener
    }

    override fun onDetach() {
        conferenceRepository.onRefreshListeners -= onRefreshListener
    }


    override fun showError(error: Throwable) {
    }

    private lateinit var view: EventListContract.View
    private lateinit var date: GMTDate

    private var showFavoritesOnly: Boolean = false

    override fun setDate(conferenceDay: GMTDate, showFavoritesOnly: Boolean) {

        this.date = conferenceDay
        this.showFavoritesOnly = showFavoritesOnly
        launch {
            val events = conferenceRepository.getEvents(date.dayOfMonth)
            val filtered = events.filter {
                if (showFavoritesOnly) {
                    (it.favorite.selected)
                } else {
                    true
                }
            }
            handleGetEventsSuccess(filtered)
        }
    }

    private fun refreshDataFromRepo() {
        launch {
            val events = conferenceRepository.getEvents(date.dayOfMonth)
            val filtered = events.filter {
                if (showFavoritesOnly) {
                    (it.favorite.selected)
                } else {
                    true
                }
            }
            handleGetEventsSuccess(filtered)
        }
    }

    private fun findCurrentSessionIndex(sessions: List<Event>): Int {
        // find the current session index
        /*
        val instant = Instant.ofEpochMilli(currentTimeProvider.currentTimeMillis())
        val now = instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        return sessions.indexOfFirst {
            now.isAfter(it.startTime) && now.isBefore(it.endTime)
                    && (Duration.between(it.startTime, it.endTime).toMinutes() <= 100L)
        }
        */
        return 0
    }


    private fun handleGetEventsSuccess(events: List<Event>) {
        if (events.isEmpty()) {
            this.view.showNoSessions()
        } else {
            this.view.let {
                it.showSessions(events.map { event ->
                    eventsMapper.mapToView(event)
                })
                it.scrollTo(findCurrentSessionIndex(events))
            }
        }
    }
}