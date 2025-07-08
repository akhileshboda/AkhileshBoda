package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClinicianViewModel(
    private val userRepo: UserRepository
): ViewModel() {
    private val _averageHeifaMale = MutableStateFlow(0f)
    val averageHeifaMale: StateFlow<Float> = _averageHeifaMale.asStateFlow()

    private val _averageHeifaFemale = MutableStateFlow(0f)
    val averageHeifaFemale: StateFlow<Float> = _averageHeifaFemale.asStateFlow()

    fun calculateAverageHeifaScores() {
        viewModelScope.launch(Dispatchers.IO) {
            _averageHeifaMale.value =
                userRepo.getHeifaScoresMale().map { it.toFloat() }.average().toFloat()
            _averageHeifaFemale.value =
                userRepo.getHeifaScoresFemale().map { it.toFloat() }.average().toFloat()
        }
    }


    class Factory(private val userRepo: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClinicianViewModel::class.java)) {
                return ClinicianViewModel(userRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}