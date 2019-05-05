package org.dukecon.android.ui.features.event

import android.text.format.DateUtils
import android.text.format.DateUtils.formatDateTime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_session.view.*
import org.dukecon.android.ui.R
import org.dukecon.android.ui.utils.DrawableUtils
import org.dukecon.domain.features.time.CurrentTimeProvider
import org.dukecon.presentation.model.EventView
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

internal class EventsAdapter(
        val currentTimeProvider: CurrentTimeProvider,
        private val onSessionSelectedListener: ((session: EventView) -> Unit)
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    val sessions: MutableList<EventView> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view, onSessionSelectedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val session = sessions[position]

        holder.session = session

        val st = 0L // session.startTime.toJvmDate().time;
        val startTime = formatDateTime(context, st, DateUtils.FORMAT_SHOW_TIME)
        holder.timeslot.text = String.format(context.getString(R.string.session_start_time), startTime)

        // Dim the session card once hte session is over
        val instant = Instant.ofEpochMilli(currentTimeProvider.currentTimeMillis())
        val now = instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        // TODO MPP
        /*
        if (session.endTime.isAfter(now)) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_bg))
        } else {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_finished_bg))
        }*/

        holder.title.text = session.title

        if (session.speakers.isEmpty()) {
            holder.speakers.visibility = View.GONE
        } else {
            holder.speakers.visibility = View.VISIBLE
            holder.speakers.text = session.speakers.joinToString { it.name }
            holder.room.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableUtils.create(context, R.drawable.ic_speaker),
                    null,
                    null,
                    null)
        }

        if (session.room.isNotEmpty()) {
            val duration = ""
            /// TODO MPP
            /*
            val duration = String.format(
                    context.getString(R.string.session_duration),
                    Duration.between(session.startTime, session.endTime).toMinutes())
             */
            holder.room.visibility = View.VISIBLE
            holder.room.text = context.getString(R.string.event_list_room_duration, session.room, duration)
            holder.room.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableUtils.create(context, R.drawable.ic_room),
                    null,
                    null,
                    null)
        } else {
            holder.room.visibility = View.GONE
        }
        // TODO MPP
        /*

        if (session.favorite.selected) {
            holder.favorite.visibility = View.VISIBLE
        } else {
            holder.favorite.visibility = View.GONE
        }
        */


        if (position > 0) {
            val previous = sessions[position - 1]
            // TODO MPP
            /*
            holder.timeslot.visibility = if (previous.startTime. == session.startTime) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }

             */
        } else {
            holder.timeslot.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    internal class ViewHolder(itemView: View, private val onSessionSelectedListener: ((session: EventView) -> Unit)) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var session: EventView? = null

        val card: CardView = super.itemView.card
        val timeslot: TextView = super.itemView.timeslot
        val title: TextView = super.itemView.title
        val speakers: TextView = super.itemView.speakers
        val room: TextView = super.itemView.room
        val favorite: ImageView = super.itemView.favorite

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (session != null) {
                onSessionSelectedListener(session!!)
            }
        }
    }
}