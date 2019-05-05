package org.dukecon.presentation.feature.speakers

import kotlinx.coroutines.launch
import org.dukecon.domain.model.Speaker
import org.dukecon.domain.repository.ConferenceRepository
import org.dukecon.presentation.CoroutinePresenter
import org.dukecon.presentation.IoContextProvider
import org.dukecon.presentation.mapper.SpeakerMapper

open class SpeakerListPresenter constructor(val conferenceRepository: ConferenceRepository,
                                            val speakersMapper: SpeakerMapper,
                                            ioContextProvider: IoContextProvider
) : CoroutinePresenter<SpeakerListContract.View>(ioContextProvider), SpeakerListContract.Presenter {
    override fun showError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var view: SpeakerListContract.View? = null

    override fun onAttach(view: SpeakerListContract.View) {
        this.view = view
        launch {
            val speakers = conferenceRepository.getSpeakers()
            handleGetSpeakersSuccess(speakers)
        }
    }

    override fun onDetach() {
        this.view = null
    }

    internal fun handleGetSpeakersSuccess(speakers: List<Speaker>) {

        if (speakers.isNotEmpty()) {
            view?.showSpeakers(
                    speakers
                            .map { speakersMapper.mapToView(it) }

            )
        }
    }
}