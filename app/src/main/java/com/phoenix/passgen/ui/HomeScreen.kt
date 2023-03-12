package com.phoenix.passgen.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoenix.passgen.R
import com.phoenix.passgen.data.CheckboxEvent
import com.phoenix.passgen.ui.widget.AnimatedCounterText
import com.phoenix.passgen.ui.widget.LabeledCheckbox

@Composable
fun PasswordScreen(
    passwordViewModel: HomeViewModel = viewModel()
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (passwordRef, settingsRef) = createRefs()

        PasswordLayout(
            modifier = Modifier.constrainAs(passwordRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(settingsRef.top, 24.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            viewModel = passwordViewModel
        )
        SettingsLayout(
            modifier = Modifier.constrainAs(settingsRef) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            viewModel = passwordViewModel
        )
    }
}

@Composable
private fun PasswordLayout(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ConstraintLayout(modifier = modifier) {
        val (passwordRef, clearRef, copyRef, copiedRef) = createRefs()

        Text(
            modifier = Modifier.constrainAs(passwordRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, 32.dp)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 32.dp)
                width = Dimension.fillToConstraints
            },
            text = if (uiState.isPasswordGenerated)
                uiState.generatedPassword
            else if (uiState.errorMassage != "")
                uiState.errorMassage
            else
                "Tap Generate Password to create a new one",
            fontSize = 26.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )

        AnimatedVisibility(
            visible = uiState.isPasswordGenerated,
            modifier = Modifier
                .constrainAs(clearRef) {
                    top.linkTo(parent.top, 56.dp)
                    end.linkTo(parent.end, 16.dp)
                },
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(modifier = Modifier.size(56.dp), onClick = { viewModel.clearPassword() }) {
                Icon(Icons.Default.Clear, null)
            }
        }
        AnimatedVisibility(
            visible = uiState.isPasswordCopied,
            modifier = Modifier.constrainAs(copiedRef) {
                start.linkTo(copyRef.start)
                top.linkTo(copyRef.bottom, 16.dp)
                end.linkTo(copyRef.end)
            },
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            Text("Password copied!")
        }
        AnimatedVisibility(
            visible = uiState.isPasswordGenerated,
            modifier = Modifier.constrainAs(copyRef) {
                bottom.linkTo(parent.bottom, 56.dp)
                end.linkTo(parent.end, 32.dp)
            },
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { it } + fadeOut()
        ) {
            Button(
                onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("", uiState.generatedPassword)
                    clipboard?.setPrimaryClip(clip)
                    viewModel.onCopyEvent()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(painterResource(R.drawable.ic_content_copy), null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Copy Password")
            }
        }
    }
}

@Composable
private fun SettingsLayout(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
//    isUniqueChecked: Boolean,
//    onUniqueCheckClick: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LabeledCheckbox(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                checked = uiState.upperCase,
                onCheckedChange = { viewModel.onCheckboxEvent(CheckboxEvent.UPPER_CASE) },
                text = "Upper case"
            )

            Spacer(modifier = Modifier.width(8.dp))

            LabeledCheckbox(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                checked = uiState.lowerCase,
                onCheckedChange = { viewModel.onCheckboxEvent(CheckboxEvent.LOWER_CASE) },
                text = "Lower case"
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {

            LabeledCheckbox(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                checked = uiState.numbers,
                onCheckedChange = { viewModel.onCheckboxEvent(CheckboxEvent.NUMBERS) },
                text = "Numbers"
            )

            Spacer(modifier = Modifier.width(8.dp))

            LabeledCheckbox(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                checked = uiState.symbols,
                onCheckedChange = { viewModel.onCheckboxEvent(CheckboxEvent.SYMBOLS) },
                text = "Symbols"
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            LabeledCheckbox(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                checked = uiState.uniqueChars,
                onCheckedChange = { viewModel.onCheckboxEvent(CheckboxEvent.UNIQUE_CHARS) },
                text = "Unique Chars"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Length", fontSize = 14.sp)
                OutlinedButton(
                    onClick = { viewModel.decreaseLength() },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = "-")
                }
                AnimatedCounterText(count = uiState.passwordLength, fontSize = 14.sp)
                OutlinedButton(
                    onClick = { viewModel.increaseLength() },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = "+")
                }
            }
        }
        Button(modifier = Modifier
            .padding(start = 16.dp, bottom = 24.dp, end = 16.dp)
            .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            onClick = { viewModel.generateRandomPassword() }) {
            Text("Generate Password")
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PasswordScreenPreview() {
    PasswordScreen()
}