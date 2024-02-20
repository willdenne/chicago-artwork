package will.denne.artwork.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import will.denne.artwork.data.NetworkResult
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreenState
import will.denne.artwork.ui.artworkdetail.ArtworkDetailUiModel

class ArtworkDetailViewModel(
    private val artworkId: Int,
    private val artworkRepository: ArtworkRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ArtworkDetailScreenState> =
        MutableStateFlow(ArtworkDetailScreenState.Loading)
    val uiState: StateFlow<ArtworkDetailScreenState> = _uiState

    init {
        getArtworkDetail()
    }

    fun retry() {
        getArtworkDetail()
    }

    private fun getArtworkDetail() {
        _uiState.value = ArtworkDetailScreenState.Loading
        viewModelScope.launch {
            when (val artworkResult = artworkRepository.getArtworkDetail(artworkId)) {
                is NetworkResult.Success -> {
                    _uiState.value = ArtworkDetailScreenState.Success(
                        ArtworkDetailUiModel.fromArtworkData(artworkResult.result)
                    )
                }
                is NetworkResult.Error -> {
                    Timber.e(artworkResult.exception, "Error getting artwork details")
                    _uiState.value = ArtworkDetailScreenState.Error(artworkResult.exception.message)
                }
            }
        }
    }
}
