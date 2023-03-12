package com.phoenix.passgen.ui.widget

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounterText(
    count: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit
) {
    var oldCount by remember { mutableStateOf(count) }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrElse(i) { ' ' }
            val newChar = countString[i]
            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (oldCount > count)
                        slideInVertically { -it } with slideOutVertically { it }
                    else
                        slideInVertically { it } with slideOutVertically { -it }
                }
            ) { character ->
                Text(
                    text = character.toString(),
                    fontSize = fontSize,
                    softWrap = false
                )
            }
        }
    }
}