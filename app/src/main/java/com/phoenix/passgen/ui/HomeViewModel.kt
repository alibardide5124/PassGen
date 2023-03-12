package com.phoenix.passgen.ui

import androidx.lifecycle.ViewModel
import com.phoenix.passgen.data.CheckboxEvent
import com.phoenix.passgen.data.getCharCombination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun generateRandomPassword() {
        var password = ""
        val combination = getCombinations()
        if (combination.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    generatedPassword = "",
                    isPasswordGenerated = false,
                    isPasswordCopied = false,
                    errorMassage = "How should i make a password of nothing?"
                )
            }
            return
        }
        if (uiState.value.uniqueChars && combination.size < uiState.value.passwordLength) {
            _uiState.update { currentState ->
                currentState.copy(
                    generatedPassword = "",
                    isPasswordGenerated = false,
                    isPasswordCopied = false,
                    errorMassage = "I only have ${combination.size} chars i can\'t make a ${uiState.value.passwordLength} char password with all unique characters"
                )
            }
            return
        }
        while (password.length < _uiState.value.passwordLength) {
            val char = combination.random()
            if (_uiState.value.uniqueChars && password.contains(char))
                continue
            password += char
        }

        _uiState.update { currentState ->
            currentState.copy(
                generatedPassword = password,
                isPasswordGenerated = true,
                isPasswordCopied = false,
                errorMassage = ""
            )
        }
    }

    private fun getCombinations(): List<String> {
        return getCharCombination(
            _uiState.value.upperCase,
            _uiState.value.lowerCase,
            _uiState.value.numbers,
            _uiState.value.symbols
        )
    }

    fun clearPassword() {
        _uiState.update { currentState ->
            currentState.copy(
                generatedPassword = "",
                isPasswordGenerated = false,
                isPasswordCopied = false
            )
        }
    }

    fun increaseLength() {
        val passwordLength = _uiState.value.passwordLength
        if (passwordLength < 32)
            updateLength(passwordLength + 1)

    }

    fun decreaseLength() {
        val passwordLength = _uiState.value.passwordLength
        if (passwordLength > 8)
            updateLength(passwordLength - 1)
    }

    private fun updateLength(updatedLength: Int) {
        _uiState.update { currentState ->
            currentState.copy(passwordLength = updatedLength)
        }
    }

    fun onCopyEvent() {
        _uiState.update { currentState ->
            currentState.copy(isPasswordCopied = true)
        }
    }

    fun onCheckboxEvent(event: CheckboxEvent) {
        _uiState.update { currentState ->
            when (event) {
                CheckboxEvent.UPPER_CASE -> currentState.copy(upperCase = !_uiState.value.upperCase)
                CheckboxEvent.LOWER_CASE -> currentState.copy(lowerCase = !_uiState.value.lowerCase)
                CheckboxEvent.NUMBERS -> currentState.copy(numbers = !_uiState.value.numbers)
                CheckboxEvent.SYMBOLS -> currentState.copy(symbols = !_uiState.value.symbols)
                CheckboxEvent.UNIQUE_CHARS -> currentState.copy(uniqueChars = !_uiState.value.uniqueChars)
            }
        }

    }

}