package com.phoenix.passgen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.phoenix.passgen.ui.PasswordScreen
import com.phoenix.passgen.ui.theme.PassgenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassgenTheme {
                Surface {
                    PasswordScreen()
                }
            }
        }
    }
}