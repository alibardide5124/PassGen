package com.phoenix.passgen.ui

data class HomeUiState(
    val generatedPassword: String = "",
    val upperCase: Boolean = true,
    val lowerCase: Boolean = true,
    val numbers: Boolean = true,
    val symbols: Boolean = true,
    val uniqueChars: Boolean = false,
    val passwordLength: Int = 16,
    val isPasswordGenerated: Boolean = false,
    val isPasswordCopied: Boolean = false,
    val errorMassage: String = ""
)