package org.dukecon.presentation.feature.speakerdetail

import kotlinx.coroutines.launch
import org.dukecon.domain.model.Speaker
import org.dukecon.domain.repository.ConferenceRepository
import org.dukecon.presentation.CoroutinePresenter
import org.dukecon.presentation.IoContextProvider
import org.dukecon.presentation.mapper.SpeakerDetailMapper

open class SpeakerDetailPresenter constructor(
        val conferenceRepository: ConferenceRepository,
        val speakerDetailMapper: SpeakerDetailMapper,
        ioContextProvider: IoContextProvider
) : CoroutinePresenter<SpeakerDetailContract.View>(ioContextProvider), SpeakerDetailContract.Presenter {
    override fun showError(error: Throwable) {
    }

    private var view: SpeakerDetailContract.View? = null

    override fun onAttach(view: SpeakerDetailContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null

    }

    override fun setSpeakerId(speakerId: String) {
        launch {
            val speaker = conferenceRepository.getSpeaker(speakerId)
            handleGetSpeakerSuccess(speaker)
        }
    }

    private fun handleGetSpeakerSuccess(event: Speaker) {
        this.view?.let {
            it.showSpeaker(speakerDetailMapper.mapToView(event))
        }
    }
}
