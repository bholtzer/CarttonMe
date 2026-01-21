package com.carttonme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carttonme.data.SmurfRepository
import com.carttonme.ui.AppDestination
import com.carttonme.ui.theme.CarttonMeTheme
import com.carttonme.ui.LoadingScreen
import com.carttonme.ui.LoadingViewModel
import com.carttonme.ui.LoadingViewModelFactory
import com.carttonme.ui.MainScreen
import com.carttonme.ui.MainViewModel
import com.carttonme.ui.SmurfMeScreen
import com.carttonme.ui.SmurfMeViewModel
import com.carttonme.ui.SmurfScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = SmurfRepository()
        setContent {
            val configuration = LocalConfiguration.current
            CarttonMeTheme(useDarkTheme = (configuration.uiMode and 0x30) == 0x20) {
                val loadingViewModel: LoadingViewModel = viewModel(
                    factory = LoadingViewModelFactory(repository)
                )
                val smurfs by loadingViewModel.smurfs.collectAsState()
                val isLoading by loadingViewModel.isLoading.collectAsState()
                var destination by remember { mutableStateOf<AppDestination>(AppDestination.Loading) }

                when (destination) {
                    is AppDestination.Loading -> {
                        if (!isLoading && smurfs.isNotEmpty()) {
                            destination = AppDestination.Main(smurfs)
                        }
                        LoadingScreen(isLoading = isLoading)
                    }
                    is AppDestination.Main -> {
                        val mainViewModel: MainViewModel = viewModel()
                        val isGrid by mainViewModel.isGrid.collectAsState()
                        MainScreen(
                            smurfs = smurfs,
                            isGrid = isGrid,
                            onToggleLayout = mainViewModel::toggleLayout,
                            onSmurfSelected = { destination = AppDestination.SmurfDetail(it) },
                            onSmurfMe = { destination = AppDestination.SmurfMe(smurfs) }
                        )
                    }
                    is AppDestination.SmurfDetail -> {
                        val smurf = (destination as AppDestination.SmurfDetail).smurf
                        SmurfScreen(smurf = smurf, onBack = { destination = AppDestination.Main(smurfs) })
                    }
                    is AppDestination.SmurfMe -> {
                        val smurfMeViewModel: SmurfMeViewModel = viewModel()
                        SmurfMeScreen(
                            smurfs = smurfs,
                            viewModel = smurfMeViewModel,
                            onBack = { destination = AppDestination.Main(smurfs) }
                        )
                    }
                }
            }
        }
    }
}
