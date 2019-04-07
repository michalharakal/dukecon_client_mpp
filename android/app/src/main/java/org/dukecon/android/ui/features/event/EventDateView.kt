package org.dukecon.android.ui.features.event

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_sessions.view.*
import org.dukecon.android.ui.R
import org.dukecon.android.ui.ext.getComponent
import org.dukecon.android.ui.features.main.MainComponent
import org.dukecon.domain.features.time.CurrentTimeProvider
import org.dukecon.presentation.feature.event.EventDateListContract
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import javax.inject.Inject

class EventDateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle), EventDateListContract.View {
    override fun showError(throwable: Throwable) {

    }

    @Inject
    lateinit var currentTimeProvider: CurrentTimeProvider

    @Inject
    lateinit var presenter: EventDateListContract.Presenter

    private val adapter: SessionPagerAdapter

    var showFavoritesOnly: Boolean = false

    init {
        context.getComponent<MainComponent>().sessionListComponent().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_sessions, this, true)

        adapter = SessionPagerAdapter()
        pager.adapter = adapter

        tabs.setupWithViewPager(pager)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    override fun showNoSessionDates() {
        adapter.clear()
        adapter.notifyDataSetChanged()
    }

    override fun showSessionDates(sessionDates: List<OffsetDateTime>) {
        adapter.showEventDates(sessionDates, showFavoritesOnly)
        adapter.notifyDataSetChanged()

        if (sessionDates.size > 1) {
            tabs.visibility = View.VISIBLE
        }
    }

    override fun scrollToCurrentDay() {
        if (adapter.dates.isNotEmpty()) {

            val instant = Instant.ofEpochMilli(currentTimeProvider.currentTimeMillis())
            val now = instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
            val index = adapter.dates.indexOfFirst { now.dayOfMonth == it.dayOfMonth }
            if (index >= 0) {
                pager.setCurrentItem(index, false)
            }
        }
    }
}