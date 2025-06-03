package com.example.project_yum.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.project_yum.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.project_yum.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.username, viewModel.password) {
        viewModel.validate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = {
                    viewModel.username = it
                    viewModel.usernameTouched = true
                },
                label = { Text("Username") },
                singleLine = true,
                isError = viewModel.usernameTouched && viewModel.usernameError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            if (viewModel.usernameTouched && viewModel.usernameError != null)
                Text(
                    viewModel.usernameError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, bottom = 6.dp)
                )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.password = it
                    viewModel.passwordTouched = true
                },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = viewModel.passwordTouched && viewModel.passwordError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            if (viewModel.passwordTouched && viewModel.passwordError != null)
                Text(
                    viewModel.passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, bottom = 16.dp)
                )

            Button(
                onClick = {
                    viewModel.usernameTouched = true
                    viewModel.passwordTouched = true
                    viewModel.validate()
                    if (viewModel.allValid) {
                        viewModel.login(
                            onSuccess = { user ->
                                navController.navigate("logged/${user.id}/${user.username}")
                                onLoginClick(user.id ?: "", user.username)
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = viewModel.allValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log in")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                TextButton(onClick = onSignUpClick) {
                    Text("Sign up", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}