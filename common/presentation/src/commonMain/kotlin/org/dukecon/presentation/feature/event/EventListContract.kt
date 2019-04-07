package org.dukecon.presentation.feature.event

import io.ktor.util.date.GMTDate
import org.dukecon.presentation.BasePresenter
import org.dukecon.presentation.BaseView
import org.dukecon.presentation.model.EventView

/**
 * Defines a contract of operations between the Events Presenter and Events View
 */
interface EventListContract {

    interface View : BaseView {
        fun showNoSessions()
        fun showSessions(sessions: List<EventView>)
        fun scrollTo(index: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun setDate(conferenceDay: GMTDate, showFavoritesOnly: Boolean)
    }

}