package com.example.a1_akhilboda_34396268_fit2081.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a1_akhilboda_34396268_fit2081.hashSha256
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.FoodIntakeRepository
import com.example.a1_akhilboda_34396268_fit2081.model.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepo: UserRepository,
    private val foodRepo: FoodIntakeRepository
) : ViewModel() {
    // States
    private val _showLoginOverlay = MutableStateFlow(false)
    val showLoginOverlay = _showLoginOverlay.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    private val _userPhoneInput = MutableStateFlow("")
    val userPhoneInput = _userPhoneInput.asStateFlow()

    private val _phoneErr = MutableStateFlow<String?>(null)
    val phoneErr = _phoneErr.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _newPasswordError = MutableStateFlow<String?>(null)
    val newPasswordError = _newPasswordError.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    private val _existingPassword = MutableStateFlow("")
    val existingPassword = _existingPassword.asStateFlow()

    private val _existingPasswordError = MutableStateFlow<String?>(null)
    val existingPasswordError = _existingPasswordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _enableContinueButton = MutableStateFlow(false)
    val enableContinueButton = _enableContinueButton.asStateFlow()


    /** Functions */
    fun resetAll() {
        _userPhoneInput.value = ""
        _phoneErr.value = null
        _newPassword.value = ""
        _newPasswordError.value = null
        _confirmPassword.value = ""
        _confirmPasswordError.value = null
        _existingPassword.value = ""
        _existingPasswordError.value = null
        _isLoading.value = false
        _enableContinueButton.value = false

    }

    fun setUserId(userId: Int) {
        _userId.value = userId
    }

    fun showLoginOverlay() {
        resetAll()
        _showLoginOverlay.value = true
    }

    fun hideLoginOverlay() {
        _showLoginOverlay.value = false
        resetAll()
    }

    fun updatePhoneInput(userPhoneInput: String, registeredPhone: String) {
        _userPhoneInput.value = userPhoneInput

        _phoneErr.value = when {
            _userPhoneInput.value.length > 11 -> "Phone number must be 11 digits"
            _userPhoneInput.value.length == 11 && _userPhoneInput.value != registeredPhone -> "Phone number does not match"
            _userPhoneInput.value.length == 11 -> ""
            else -> null
        }
        refreshContinueButton()

    }

    fun clearPhone() {
        _userPhoneInput.value = ""

        refreshContinueButton()
    }

    fun updateNewPassword(newPassword: String) {
        _newPassword.value = newPassword
        val rules = "min 6 chars, 1 digit & 1 special"

        _newPasswordError.value = when {
            newPassword.isBlank() -> "Required. $rules"
            newPassword.length < 6 -> "Too short. $rules"
            !newPassword.any { it.isDigit() } -> "Need a digit. $rules"
            !newPassword.any { "!@#\$%^&*".contains(it) } -> "Need special char. $rules"
            else -> null
        }

        refreshContinueButton()
    }

    fun clearNewPassword() {
        _newPassword.value = ""

        refreshContinueButton()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        _confirmPasswordError.value = when {
            confirmPassword.isBlank() -> "A password is required"
            confirmPassword != _newPassword.value -> "Passwords do not match"
            else -> null
        }

        refreshContinueButton()
    }

    fun clearConfirmPassword() {
        _confirmPassword.value = ""
    }

    fun updateExistingPassword(existingPassword: String) {
        _existingPassword.value = existingPassword
        _existingPasswordError.value = when {
            existingPassword.isBlank() -> "A password is required"
            else -> null
        }

        refreshContinueButton()
    }

    fun clearExistingPassword() {
        _existingPassword.value = ""
        _existingPasswordError.value = null

        refreshContinueButton()
    }

    suspend fun hasForm(userId: Int): Boolean = withContext(Dispatchers.IO) {
        foodRepo.countFormsForUser(userId) > 0
    }

    fun continueNewUser(userId: Int) {
        // convert password into hash and store into database via repo
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val hashedPassword = hashSha256(_newPassword.value)
            userRepo.setPassword(userId, hashedPassword)

            delay(1000)
            _isLoading.value = false
        }
    }

    suspend fun verifyExistingUser(userId: Int): Boolean {
        _isLoading.value = true
        val user = withContext(Dispatchers.IO) { userRepo.getUserById(userId) }
        val hashedPassword = hashSha256(_existingPassword.value)
        delay(1000)

        var result = false

        if (user!!.password != hashedPassword) {
            result = false
            _existingPasswordError.value = "Password is incorrect"
        } else {
            result = true
            _existingPasswordError.value = null
        }
        _isLoading.value = false
        return result
    }

    fun refreshContinueButton() {
        val validNewPassword = (
                (_userPhoneInput.value.isNotBlank() && _phoneErr.value == "") &&
                        (_newPassword.value.isNotBlank() && _newPasswordError.value == null) &&
                        (_confirmPassword.value.isNotBlank() && _confirmPasswordError.value == null)
                )
        val validExistingPassword =
            _existingPassword.value.isNotBlank() && _existingPasswordError.value == null

        _enableContinueButton.value = validNewPassword || validExistingPassword
    }

    class Factory(
        private val userRepo: UserRepository,
        private val foodRepo: FoodIntakeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepo, foodRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

