package com.carttonme.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SmurfMeViewModel : ViewModel() {
    private val _selectedImage = MutableStateFlow<String?>(null)
    val selectedImage: StateFlow<String?> = _selectedImage.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    fun selectImage(fakeUri: String) {
        _selectedImage.value = fakeUri
        _selectedBitmap.value = null
        runSmurfify()
    }

    fun selectBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
        _selectedImage.value = null
        runSmurfify()
    }

    private fun runSmurfify() {
        viewModelScope.launch {
            _isProcessing.value = true
            _showAd.value = true
            delay(2000)
            _isProcessing.value = false
            _showAd.value = false
        }
    }

    fun dismissAd() {
        _showAd.value = false
    }
}
