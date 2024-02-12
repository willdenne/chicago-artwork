package will.denne.artwork.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artwork.ArtworkScreenState
import will.denne.artwork.ui.artwork.ArtworkUiModel

class ArtworkViewModel(
    private val artworkRepository: ArtworkRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<ArtworkScreenState> = MutableStateFlow(ArtworkScreenState.Loading)
    val uiState: StateFlow<ArtworkScreenState> = _uiState

    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private var currentPage = 1

    init {
        getAllArtwork()
    }

    fun retry() {
        getAllArtwork()
    }

    fun searchArtwork() {
        currentPage = 1
        _uiState.value = ArtworkScreenState.Loading
        if (_searchText.value.isEmpty()) {
            getAllArtwork()
            return
        }
        viewModelScope.launch {
            try {
                val artwork = artworkRepository.searchArtwork(currentPage, _searchText.value)
                if (artwork.data.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value =
                        ArtworkScreenState.Empty
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        artwork.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artwork.pagination.totalPages <= currentPage
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting artwork")
                _uiState.value = ArtworkScreenState.Error(e)
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun loadMoreArtwork() {
        currentPage++
        Timber.d("WILL: current page loading $currentPage")
        viewModelScope.launch {
            try {
                val artwork = if (_searchText.value.isEmpty()) {
                    artworkRepository.getArtworkList(currentPage)
                } else {
                    artworkRepository.searchArtwork(currentPage, _searchText.value)
                }
                val oldList = (_uiState.value as ArtworkScreenState.Success).artwork.toMutableList()
                if (artwork.data.isEmpty() && oldList.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value = ArtworkScreenState.Empty
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        oldList + artwork.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artwork.pagination.totalPages <= currentPage
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting artwork")
                _uiState.value = ArtworkScreenState.Error(e)
            }
        }
    }

    private fun getAllArtwork() {
        currentPage = 1
        _uiState.value = ArtworkScreenState.Loading
        viewModelScope.launch {
            try {
                val artwork = artworkRepository.getArtworkList(currentPage)
                if (artwork.data.isEmpty()) {
                    Timber.e("Error getting artwork, list is empty")
                    _uiState.value =
                        ArtworkScreenState.Error(Exception("Whoops something went wrong"))
                } else {
                    _uiState.value = ArtworkScreenState.Success(
                        artwork.data.map {
                            ArtworkUiModel.fromArtworkData(it)
                        },
                        hasLoadedLastPage = artwork.pagination.totalPages <= currentPage
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting artwork")
                _uiState.value = ArtworkScreenState.Error(e)
            }
        }
    }
}
