package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.model.entities.FoodIntakeEntity
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.FoodIntakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalTime

class FoodIntakeViewModel(
    private val userId: Int,
    private val repo: FoodIntakeRepository
) : ViewModel() {
    // raw UI inputs
    private val _selectedFoods = MutableStateFlow<List<String>>(emptyList())
    private val _persona       = MutableStateFlow("")
    private val _timings       = MutableStateFlow(Triple("", "", ""))

    // TimePickers initial value
    private val _timingsInitials =
        MutableStateFlow<Map<Char, Triple<Int,Int,Boolean>>>(mapOf(
            'l' to Triple(12, 0, true),
            'w' to Triple(7,  0, true),
            'b' to Triple(22, 0, true)
        ))
    val timingsInitials: StateFlow<Map<Char, Triple<Int,Int,Boolean>>> =
        _timingsInitials.asStateFlow()

    init {
        viewModelScope.launch {
            // 1) Check whether a form row exists
            val count = withContext(Dispatchers.IO) {
                repo.countFormsForUser(userId)
            }

            if (count > 0) {
                // 2) Now collect the single entity from the Flow
                val form = withContext(Dispatchers.IO) {
                    repo.getUserForm(userId)
                        .first()        // suspend until the first FoodIntakeEntity is emitted
                }

                // 3) Populate your UI state from that entity:
                _selectedFoods.value = form.selectedFoods.split(",")
                _persona.value       = form.persona
                _timings.value       = Triple(
                    form.largestMealTime,
                    form.wakeUpTime,
                    form.bedTime
                )

                // helper to turn "HH:mm" into (hour,minute,is24h)
                fun parse(hm: String): Triple<Int,Int,Boolean> {
                    val (h,m) = hm.split(":").map(String::toInt)
                    return Triple(h, m, true)
                }

                _timingsInitials.value = mapOf(
                    'l' to parse(form.largestMealTime),
                    'w' to parse(form.wakeUpTime),
                    'b' to parse(form.bedTime)
                )
            }
            // if count == 0, all your StateFlows stay at their default empty values
        }
    }


    // saving state
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    // navigation event
    private val _saved = MutableSharedFlow<Unit>()
    val saved = _saved.asSharedFlow()

    // expose as StateFlow
    val selectedFoods = _selectedFoods.asStateFlow()
    val persona       = _persona.asStateFlow()
    val timings       = _timings.asStateFlow()

    /** 1. wake-up can only be picked once largest-meal is set */
    val canPickWakeUp: StateFlow<Boolean> = _timings
        .map { (l, _, _) -> l.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /** 2. bed-time can only be picked once wake-up is set */
    val canPickBedTime: StateFlow<Boolean> = _timings
        .map { (_, w, _) -> w.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /**
     * 3. Validate there’s at least 5 hours between wake-up → bed-time.
     *    If not, emit an error message; otherwise null.
     */
    val bedTimeError: StateFlow<String?> = _timings
        .map { (_, wakeStr, bedStr) ->
            if (wakeStr.isNotBlank() && bedStr.isNotBlank()) {
                try {
                    val wake = LocalTime.parse(wakeStr)
                    val bed  = LocalTime.parse(bedStr)

                    // compute forward duration
                    val diff = if ( !bed.isBefore(wake) ) {
                        Duration.between(wake, bed)
                    } else {
                        // bed next day
                        Duration.between(wake, LocalTime.MIDNIGHT).plus(Duration.between(LocalTime.MIN, bed))
                    }

                    if (diff.toHours() < 5) {
                        "Bedtime must be at least 5 hours after wake-up"
                    } else {
                        null
                    }
                } catch (ex: Exception) {
                    "Invalid time format"
                }
            } else {
                null
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // only enable Save when all sections valid AND no bed-time error
    val canSave: StateFlow<Boolean> = combine(
        _selectedFoods, _persona, _timings, bedTimeError
    ) { foods, p, (l, w, b), bedErr ->
        foods.isNotEmpty()
                && p.isNotBlank()
                && listOf(l, w, b).none { it.isBlank() }
                && bedErr == null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // setters
    fun setFoods(list: List<String>) { _selectedFoods.value = list }
    fun setPersona(p: String)        { _persona.value       = p    }
    fun setTimings(l: String, w: String, b: String) {
        _timings.value = Triple(l, w, b)
    }

    // clear form data
    fun clearForm() {
        _selectedFoods.value = emptyList()
        _persona.value       = ""
        _timings.value       = Triple("", "", "")
    }
    // persist and navigate
    fun save() {
        viewModelScope.launch {
            // Flag isSaving for circular progress indicator
            _isSaving.value = true

            // Check that all field are inputted
            if (!canSave.value) {
                _isSaving.value = false
                delay(2000)
                return@launch
            }

            val (l, w, b) = _timings.value
            repo.save(
                FoodIntakeEntity(
                    userId = userId,
                    selectedFoods   = _selectedFoods.value.joinToString(","),
                    persona         = _persona.value,
                    largestMealTime = l,
                    wakeUpTime      = w,
                    bedTime         = b
                )
            )
            delay(2000)
            _isSaving.value = false
            _saved.emit(Unit)
        }
    }

    class Factory(
        private val userId: Int,
        private val repo: FoodIntakeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(modelClass: Class<T>) =
            FoodIntakeViewModel(userId, repo) as T
    }
}
