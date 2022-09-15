package com.example.gitreposearch.presentation

sealed class UiState{
    object Loading: UiState()
    data class Success<T>(val data: T): UiState()
    data class Error(val message: String): UiState()
}
