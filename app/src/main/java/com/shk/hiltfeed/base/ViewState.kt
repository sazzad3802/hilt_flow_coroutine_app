package com.shk.hiltfeed.base


sealed interface ViewState<out T> {
    data class Success<T>(val data:T?): ViewState<T>
    data class Error(val message:String?): ViewState<Nothing>
    data object Loading: ViewState<Nothing>
    data object PaginationLoading: ViewState<Nothing>

}
