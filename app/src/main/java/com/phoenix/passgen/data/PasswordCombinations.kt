package com.phoenix.passgen.data

fun getCharCombination(
    upperCase: Boolean,
    lowerCase: Boolean,
    numbers: Boolean,
    symbols: Boolean
): List<String> {
    val combination = mutableListOf<String>()
    if (upperCase) combination.addAll(('A'..'Z').toList().map { it.toString() })
    if (lowerCase) combination.addAll(('a'..'z').toList().map { it.toString() })
    if (numbers) combination.addAll((0..9).toList().map { it.toString() })
    if (symbols) combination.addAll("`~!@#$%^&*()_+-={}[]|\\;:'\"<>,./?".split(""))
    return combination
}