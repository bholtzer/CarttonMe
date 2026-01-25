package com.carttonme.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _isGrid = MutableStateFlow(true)
    val isGrid: StateFlow<Boolean> = _isGrid.asStateFlow()

    fun toggleLayout() {
        _isGrid.value = !_isGrid.value
    }
}
