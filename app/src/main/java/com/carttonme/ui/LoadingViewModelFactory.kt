package com.carttonme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carttonme.data.SmurfRepository

class LoadingViewModelFactory(private val repository: SmurfRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoadingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
