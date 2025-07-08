// FoodIntakeViewModel.kt
package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.data.FoodIntakeEntity
import com.example.a1_akhilboda_34396268_fit2081.data.FoodIntakeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FoodIntakeViewModel(
    private val userId: Int,
    private val repo: FoodIntakeRepository
) : ViewModel() {
    // raw UI inputs
    private val _selectedFoods = MutableStateFlow<List<String>>(emptyList())
    private val _persona       = MutableStateFlow("")
    private val _timings       = MutableStateFlow(Triple("--:--","--:--","--:--"))

    // expose as StateFlow for Compose
    val selectedFoods = _selectedFoods.asStateFlow()
    val persona       = _persona.asStateFlow()
    val timings       = _timings.asStateFlow()

    // has the user already submitted?
    val existing = repo.observeForUser(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // form validity
    val canSave = combine(_selectedFoods, _persona, _timings) { foods, p, times ->
        foods.isNotEmpty() && p.isNotBlank() &&
                listOf(times.first, times.second, times.third).all { it!="--:--" }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // save & navigate event
    private val _saved = MutableSharedFlow<Unit>()
    val saved = _saved.asSharedFlow()

    fun setFoods(list: List<String>)  { _selectedFoods.value = list }
    fun setPersona(p: String)         { _persona.value = p }
    fun setTimings(l: String, w: String, b: String) {
        _timings.value = Triple(l,w,b)
    }

    fun save() {
        viewModelScope.launch {
            val (l,w,b) = _timings.value
            repo.save(
                FoodIntakeEntity(
                    userId = userId,
                    selectedFoods = _selectedFoods.value.joinToString(","),
                    persona = _persona.value,
                    largestMealTime = l,
                    wakeUpTime = w,
                    bedTime = b
                )
            )
            _saved.emit(Unit)
        }
    }

    class Factory(
        private val userId: Int,
        private val repo: FoodIntakeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(c: Class<T>) =
            FoodIntakeViewModel(userId, repo) as T
    }
}
