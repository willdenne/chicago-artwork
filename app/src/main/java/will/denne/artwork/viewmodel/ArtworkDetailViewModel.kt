package will.denne.artwork.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreenState
import will.denne.artwork.ui.artworkdetail.ArtworkDetailUiModel

class ArtworkDetailViewModel(
    private val artworkId: Int,
    private val artworkRepository: ArtworkRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ArtworkDetailScreenState> =
        MutableStateFlow(ArtworkDetailScreenState.Loading)
    val uiState: MutableStateFlow<ArtworkDetailScreenState> = _uiState

    init {
        getArtworkDetail()
    }

    fun retry() {
        getArtworkDetail()
    }

    private fun getArtworkDetail() {
        _uiState.value = ArtworkDetailScreenState.Loading
        viewModelScope.launch {
            try {
                val artwork = artworkRepository.getArtworkDetail(artworkId)
                _uiState.value = ArtworkDetailScreenState.Success(
                    ArtworkDetailUiModel.fromArtworkData(artwork)
                )
            } catch (e: Exception) {
                Timber.e(e, "Error getting artwork details")
                _uiState.value = ArtworkDetailScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
