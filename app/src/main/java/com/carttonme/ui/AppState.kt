package com.carttonme.ui

import com.carttonme.model.Smurf

sealed class AppDestination {
    data object Loading : AppDestination()
    data class Main(val smurfs: List<Smurf>) : AppDestination()
    data class SmurfDetail(val smurf: Smurf) : AppDestination()
    data class SmurfMe(val smurfs: List<Smurf>) : AppDestination()
}
