package com.example.a1_akhilboda_34396268_fit2081.fruity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FruityViewModel(
    private val repo: FruityRepository
) : ViewModel() {
    private val _query = MutableStateFlow<String>("")
    val query = _query.asStateFlow()

    private val _fruit      = MutableStateFlow<Fruit?>(null)
    val fruit     = _fruit.asStateFlow()

    private val _error      = MutableStateFlow<String?>(null)
    val error     = _error.asStateFlow()

    private val _isLoading  = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadFruit() {
        viewModelScope.launch {
            _isLoading.value = true
            _fruit.value = null
            _error.value = null

            delay(500)
            try {
                _fruit.value = repo.fetchFruit(_query.value)
            } catch (e: Exception) {
                _error.value = "Could not find fruit with name “${_query.value}”."
            }
            _isLoading.value = false
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    fun clearQuery() {
        _query.value = ""
        _fruit.value = null
        _error.value = null
    }

    class Factory(private val repo: FruityRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FruityViewModel(repo) as T
        }

    }
}
