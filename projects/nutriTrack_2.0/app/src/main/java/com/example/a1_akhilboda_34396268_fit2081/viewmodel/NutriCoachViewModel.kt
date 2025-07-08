package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.fruity.FruityRepository
import com.example.a1_akhilboda_34396268_fit2081.fruity.FruityViewModel
import com.example.a1_akhilboda_34396268_fit2081.model.entities.NutriCoachTipEntity
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.NutriCoachRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class NutriCoachViewModel(
    private val repo: NutriCoachRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Int?>(null)

    private val _showPreviousMessagesDialog = MutableStateFlow(false)
    val showPreviousMessagesDialog = _showPreviousMessagesDialog.asStateFlow()

    // All tips for current user:
    @OptIn(ExperimentalCoroutinesApi::class)
    val tips = _userId
        .flatMapLatest { id ->
            if (id != null) repo.getTips(id) else flowOf(emptyList<NutriCoachTipEntity>())
        }

    fun setUserId(userId: Int) {
        _userId.value = userId
    }

    fun insertNewTip(text: String) {
        viewModelScope.launch {
            repo.insertTip(_userId.value!!.toInt(), text)
        }
    }

    fun showPreviousMessagesDialog() {
        _showPreviousMessagesDialog.value = true
    }

    fun hidePreviousMessagesDialog() {
        _showPreviousMessagesDialog.value = false
    }

    class Factory(private val repo: NutriCoachRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NutriCoachViewModel(repo) as T
        }

    }
}
