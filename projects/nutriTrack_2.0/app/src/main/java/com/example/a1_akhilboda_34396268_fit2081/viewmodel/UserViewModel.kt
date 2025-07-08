package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.model.entities.UserEntity
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Holds:
 *  - the full list of users (for the dropdown)
 *  - which one is currently selected
 * All of the ID / UserEntity lookups live here now.
 */
class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    // all users
    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    // dropdown IDs
    val userIds: StateFlow<List<Int>> = _users
        .map { list -> list.map { it.id } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5_000), emptyList())

    // which full UserEntity is selected?
    private val _selectedUser = MutableStateFlow<UserEntity?>(null)
    val selectedUser: StateFlow<UserEntity?> = _selectedUser.asStateFlow()

    // food quality score
    val foodQualityScore: StateFlow<String> = _selectedUser
        .map { user ->
            user?.let {
                val score = if (it.sex == "Male") it.heifaTotalScoreMale else it.heifaTotalScoreFemale
                score.toString()
            } ?: "0"
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "0"
        )

    val fruitScore: StateFlow<List<Float>> =
        _selectedUser
            .map { user ->
                user?.let {
                    listOf(
                        it.fruitServeSize.toFloat(),
                        it.fruitVariationsScore.toFloat()
                    )
                } ?: listOf(0f, 0f)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf(0f, 0f)
            )


    /**
     * LOGIN BUNDLE
     */
    // First-time flag
    val isFirstTime: StateFlow<Boolean> = _selectedUser
        .map { it?.password == null }
        .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, false)

    // dropdown open/close state
    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()

    init {
        viewModelScope.launch {
            _users.value = repository.getAllUsers()
        }
    }

    /** Call this when the user picks an ID from the menu. */
    fun selectUserId(id: Int) {
        _expanded.value = false
        viewModelScope.launch(Dispatchers.IO) {
            _selectedUser.value = repository.getUserById(id)
        }
    }
    // LOGIN BUNDLE

    /** get user from database from userID */
    fun getUser(userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedUser.value = repository.getUserById(userID)
        }
    }

    fun clearSelectedUser() {
        _selectedUser.value = null
        _expanded.value = false
    }

    fun toggleDropdown() {
        _expanded.value = !_expanded.value
    }



    /** LOGOUT BUNDLE */
    private val _confirmLogout = MutableStateFlow(false)
    val confirmLogout: StateFlow<Boolean> = _confirmLogout.asStateFlow()

    fun showLogoutDialog() {
        _confirmLogout.value = true
    }

    fun hideLogoutDialog() {
        _confirmLogout.value = false
    }
    // LOGOUT BUNDLE

    /** CLINICIAN LOGIN BUNDLE */
    private val passkeyValue = "dollar-entry-apples"

    private val _showClinicianLoginDialog = MutableStateFlow(false)
    val showClinicianLoginDialog: StateFlow<Boolean> = _showClinicianLoginDialog.asStateFlow()

    private val _passkey = MutableStateFlow("")
    val passkey: StateFlow<String> = _passkey.asStateFlow()

    private val _validPasskey = MutableStateFlow(false)
    val validPasskey: StateFlow<Boolean> = _validPasskey.asStateFlow()

    private val _passkeyErrorMsg = MutableStateFlow("")
    val passkeyErrorMsg: StateFlow<String> = _passkeyErrorMsg.asStateFlow()


    fun showClinicianLoginDialog() {
        _showClinicianLoginDialog.value = true
    }

    fun hideClinicianLoginDialog() {
        _showClinicianLoginDialog.value = false
        clearPasskey()
    }

    fun updatePasskey(passkey: String) {
        _passkey.value = passkey
    }

    fun clearPasskey() {
        _passkey.value = ""
        _validPasskey.value = false
        _passkeyErrorMsg.value = ""
    }

    fun checkPasskey() {
        if (passkey.value == passkeyValue) {
            _validPasskey.value = true
            _passkeyErrorMsg.value = ""
        } else {
            _validPasskey.value = false
            _passkeyErrorMsg.value = "Invalid Passkey"
        }
    }


    // CLINICIAN LOGIN BUNDLE


    class Factory(private val repo: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}