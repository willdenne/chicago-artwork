package will.denne.artwork.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import will.denne.artwork.data.NetworkResult
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artwork.ArtworkScreenState
import will.denne.artwork.ui.artwork.ArtworkUiModel

class ArtworkViewModel(
    private val artworkRepository: ArtworkRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ArtworkScreenState> =
        MutableStateFlow(ArtworkScreenState.Loading)
    val uiState: StateFlow<ArtworkScreenState> = _uiState

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private var currentPage = 1

    init {
        getAllArtwork()
    }

    fun retry() {
        // reset page count just in case
        currentPage = 1
        if (_searchText.value.isEmpty()) {
            getAllArtwork()
        } else {
            searchArtwork()
        }
    }

    fun searchArtwork() {
        currentPage = 1
        _uiState.value = ArtworkScreenState.Loading
        if (_searchText.value.isEmpty()) {
            getAllArtwork()
            return
        }
        viewModelScope.launch {
            val artworkResult = artworkRepository.searchArtwork(currentPage, _searchText.value)
            if (artworkResult is NetworkResult.Success) {
                if (artworkResult.result.data.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value =
                        ArtworkScreenState.Empty
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        artworkResult.result.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artworkResult.result.pagination.totalPages <= currentPage
                    )
                }
            } else {
                artworkResult as NetworkResult.Error
                Timber.e(artworkResult.exception, "Error searching artwork")
                _uiState.value = ArtworkScreenState.Error(artworkResult.exception.message)
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun loadMoreArtwork() {
        currentPage++
        viewModelScope.launch {

            val artworkResult = if (_searchText.value.isEmpty()) {
                artworkRepository.getArtworkList(currentPage)
            } else {
                artworkRepository.searchArtwork(currentPage, _searchText.value)
            }
            val oldList = (_uiState.value as? ArtworkScreenState.Success)?.artwork?.toMutableList() ?: emptyList()
            if (artworkResult is NetworkResult.Success) {
                if (artworkResult.result.data.isEmpty() && oldList.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value =
                        ArtworkScreenState.Empty
                    return@launch
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        oldList + artworkResult.result.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artworkResult.result.pagination.totalPages <= currentPage
                    )
                }
            } else {
                artworkResult as NetworkResult.Error
                Timber.e(artworkResult.exception, "Error getting more artwork")
                _uiState.value = ArtworkScreenState.Error(artworkResult.exception.message)
                return@launch
            }
        }
    }

    private fun getAllArtwork() {
        currentPage = 1
        _uiState.value = ArtworkScreenState.Loading
        viewModelScope.launch {
            val artworkResult = artworkRepository.getArtworkList(currentPage)
            if (artworkResult is NetworkResult.Success) {
                if (artworkResult.result.data.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value =
                        ArtworkScreenState.Empty
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        artworkResult.result.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artworkResult.result.pagination.totalPages <= currentPage
                    )
                }
            } else {
                artworkResult as NetworkResult.Error
                Timber.e(artworkResult.exception, "Error getting artwork")
                _uiState.value = ArtworkScreenState.Error(artworkResult.exception.message)
            }
        }
    }
}
