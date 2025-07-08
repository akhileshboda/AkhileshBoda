package com.example.a1_akhilboda_34396268_fit2081.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    private val _userIds: StateFlow<List<Int>> =
        _users
            .map { list -> list.map { it.id } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _selectedUser = MutableStateFlow<UserEntity?>(null)
    val selectedUser: StateFlow<UserEntity?> = _selectedUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _users.value = repository.getAllUsers()
            } catch (ex: Exception) {
                _errorMessage.value = ex.localizedMessage ?: "Error fetching users"
            } finally {
                _isLoading.value = false
            }
        }
    }

    val userIds: StateFlow<List<Int>> get() = _userIds

    fun selectUser(userId: Int) {
        viewModelScope.launch {
            _selectedUser.value = repository.getUserById(userId)
        }
    }

    fun addUser(user: UserEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.upsert(user)
                fetchUsers()
                _selectedUser.value = null
            } catch (ex: Exception) {
                _errorMessage.value = ex.localizedMessage ?: "Error adding user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setPassword(userId: Int, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.setPassword(userId, password)
                fetchUsers()
                _selectedUser.value = null
            } catch (ex: Exception) {
            }
        }
    }

        fun deleteUser(user: UserEntity) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    repository.delete(user)
                    fetchUsers()
                    _selectedUser.value = null
                } catch (ex: Exception) {
                    _errorMessage.value = ex.localizedMessage ?: "Error deleting user"
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun clearError() {
            _errorMessage.value = null
        }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
