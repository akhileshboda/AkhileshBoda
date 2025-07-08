package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.model.entities.UserEntity
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Insights screen.
 * Loads the UserEntity and exposes total HEIFA score and per‑category scores from Room.
 */
class InsightsViewModel(
    private val userRepo: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _totalScore = MutableStateFlow("0")
    val totalScore: StateFlow<String> = _totalScore.asStateFlow()

    private val _metricScores = MutableStateFlow<Map<String, String>>(emptyMap())
    val metricScores: StateFlow<Map<String, String>> = _metricScores.asStateFlow()

    /**
     * Load the user by ID and compute the total and per‑category HEIFA scores.
     */
    fun getUser(userId: Int) = viewModelScope.launch {
        val user = userRepo.getUserById(userId)!!
        _user.value = user
        // Total score
        val total = if (user.sex == "Male") user.heifaTotalScoreMale else user.heifaTotalScoreFemale
        _totalScore.value = total.toString()

        // Per-category mapping
        val map = mapOf(
            "Discretionary" to if (user.sex=="Male") user.discretionaryHeifaScoreMale else user.discretionaryHeifaScoreMale,
            "Vegetables" to if (user.sex=="Male") user.vegetablesHeifaScoreMale else user.vegetablesHeifaScoreFemale,
            "Fruit" to if (user.sex=="Male") user.fruitHeifaScoreMale else user.fruitHeifaScoreMale,
            "Grains and Cereals" to if (user.sex=="Male") user.grainsAndCerealsHeifaScoreMale else user.grainsAndCerealsHeifaScoreFemale,
            "Wholegrains" to if (user.sex=="Male") user.wholeGrainsHeifaScoreMale else user.wholeGrainsHeifaScoreFemale,
            "Meat and Alternatives" to if (user.sex=="Male") user.meatAndAlternativesHeifaScoreMale else user.meatAndAlternativesHeifaScoreFemale,
            "Dairy and Alternatives" to if (user.sex=="Male") user.dairyAndAlternativesHeifaScoreMale else user.dairyAndAlternativesHeifaScoreFemale,
            "Sodium" to if (user.sex=="Male") user.sodiumHeifaScoreMale else user.sodiumHeifaScoreFemale,
            "Alcohol" to if (user.sex=="Male") user.alcoholHeifaScoreMale else user.alcoholHeifaScoreFemale,
            "Water" to if (user.sex=="Male") user.waterHeifaScoreMale else user.waterHeifaScoreFemale,
            "Added Sugars" to if (user.sex=="Male") user.sugarHeifaScoreMale else user.sugarHeifaScoreFemale,
            "Saturated Fat" to if (user.sex=="Male") user.saturatedFatHeifaScoreMale else user.saturatedFatHeifaScoreFemale,
            "Unsaturated Fat" to if (user.sex=="Male") user.unsaturatedFatServeSize else user.unsaturatedFatHeifaScoreFemale
        )
        _metricScores.value = map
    }

    /**
     * Factory for creating an InsightsViewModel with injected UserRepository.
     */
    class Factory(
        private val repo: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InsightsViewModel::class.java)) {
                return InsightsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}