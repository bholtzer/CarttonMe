package com.carttonme.ui

import android.graphics.Bitmap
import com.carttonme.data.SmurfifyService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SmurfMeViewModel(
    private val smurfifyService: SmurfifyService = SmurfifyService()
) : ViewModel() {
    private val _selectedImage = MutableStateFlow<String?>(null)
    val selectedImage: StateFlow<String?> = _selectedImage.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private val _smurfifiedImageUrl = MutableStateFlow<String?>(null)
    val smurfifiedImageUrl: StateFlow<String?> = _smurfifiedImageUrl.asStateFlow()

    fun selectImage(fakeUri: String) {
        _selectedImage.value = fakeUri
        _selectedBitmap.value = null
        _smurfifiedImageUrl.value = null
        runSmurfify()
    }

    fun selectBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
        _selectedImage.value = null
        _smurfifiedImageUrl.value = null
        runSmurfify()
    }

    private fun runSmurfify() {
        viewModelScope.launch {
            _isProcessing.value = true
            _showAd.value = true
            val resultUrl = smurfifyService.smurfify(
                originalUri = _selectedImage.value,
                originalBitmap = _selectedBitmap.value
            )
            delay(500)
            _isProcessing.value = false
            _showAd.value = false
            _smurfifiedImageUrl.value = resultUrl
        }
    }

    fun dismissAd() {
        _showAd.value = false
    }
}
