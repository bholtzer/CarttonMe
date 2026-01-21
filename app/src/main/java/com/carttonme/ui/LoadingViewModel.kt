package com.carttonme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carttonme.data.SmurfRepository
import com.carttonme.model.Smurf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoadingViewModel(private val repository: SmurfRepository) : ViewModel() {
    private val _smurfs = MutableStateFlow<List<Smurf>>(emptyList())
    val smurfs: StateFlow<List<Smurf>> = _smurfs.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSmurfs()
    }

    private fun loadSmurfs() {
        viewModelScope.launch {
            _isLoading.value = true
            _smurfs.value = repository.fetchSmurfs()
            _isLoading.value = false
        }
    }
}
