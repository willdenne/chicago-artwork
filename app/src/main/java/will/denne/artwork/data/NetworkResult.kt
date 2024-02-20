package will.denne.artwork.data

sealed interface NetworkResult<out T> {
    data class Success<T>(val result: T) : NetworkResult<T>
    data class Error(val exception: Throwable) : NetworkResult<Nothing>
}
