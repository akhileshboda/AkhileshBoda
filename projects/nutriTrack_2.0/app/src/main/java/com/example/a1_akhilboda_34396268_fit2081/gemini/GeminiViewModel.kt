package com.example.a1_akhilboda_34396268_fit2081.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.BuildConfig
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for driving a Compose UI that asks questions of Gemini.
 */
class GeminiViewModel(
    private val userRepo: UserRepository
) : ViewModel(
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY,
    )


    private val _query = "Generate a short encouraging message to help someone improve their nutritional value given their data and the following guidelines. Please provide fresh and helpful advice.: \n "

    private var _userToString = MutableStateFlow<String>("")
    private val _dietaryGuidelines: Map<String, Float> = mapOf(
        "Discretionary" to 10f,
        "Vegetables" to 10f,
        "Fruit" to 10f,
        "Grains and Cereals" to 5f,
        "Wholegrains" to 5f,
        "Meat and Alternatives" to 10f,
        "Dairy and Alternatives" to 10f,
        "Sodium" to 10f,
        "Alcohol" to 5f,
        "Water" to 5f,
        "Added Sugars" to 10f,
        "Saturated Fat" to 5f,
        "Unsaturated Fat" to 5f
    )

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun provideUserToString(userToString: String) {
        _userToString.value = userToString
    }

    fun askGemini() {
        viewModelScope.launch {
            _isLoading.value = true
            val appendedQuery = _query + "Data: " + _userToString.value + "\nGuidelines: " + _dietaryGuidelines.toString()
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(appendedQuery)
                    }
                )
                response.text?.let { outputContent ->
                    _answer.value = outputContent.toString()
                }
            } catch (e: Exception) {
                _answer.value = "Error: $e"
            } finally {
                delay(500)
                _isLoading.value = false

            }
        }
    }

    fun askGeminiClinicianQuery(userData: String) {
        val clinicianQuery = "Generate a unique one liner observation regarding any interesting pattern according to this patient data: \n"
        viewModelScope.launch {
            val appendedQuery = clinicianQuery +
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(clinicianQuery)
                    }
                )
                response.text?.let { outputContent ->
                    _answer.value = outputContent.toString()
                }
            } catch (e: Exception) {
                _answer.value = "Error: $e"
            } finally {
                delay(500)
                _isLoading.value = false

            }
        }

    }

    class Factory(private val userRepo: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GeminiViewModel::class.java)) {
                return GeminiViewModel(userRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}