package com.example.project_yum.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_yum.R
import com.example.project_yum.viewmodel.RegisterViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.project_yum.model.User


@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(
        viewModel.name,
        viewModel.lastName,
        viewModel.email,
        viewModel.username,
        viewModel.password
    ) {
        viewModel.validate()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 24.dp)
            )

            val fieldModifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp)

            OutlinedTextField(
                value = viewModel.name,
                onValueChange = {
                    viewModel.name = it
                    viewModel.nameTouched = true
                },
                label = { Text("First Name") },
                singleLine = true,
                isError = viewModel.nameTouched && viewModel.nameError != null,
                modifier = fieldModifier
            )
            if (viewModel.nameTouched && viewModel.nameError != null)
                Text(viewModel.nameError!!, color = MaterialTheme.colorScheme.error, modifier = fieldModifier)

            OutlinedTextField(
                value = viewModel.lastName,
                onValueChange = {
                    viewModel.lastName = it
                    viewModel.lastNameTouched = true
                },
                label = { Text("Last Name") },
                singleLine = true,
                isError = viewModel.lastNameTouched && viewModel.lastNameError != null,
                modifier = fieldModifier
            )
            if (viewModel.lastNameTouched && viewModel.lastNameError != null)
                Text(viewModel.lastNameError!!, color = MaterialTheme.colorScheme.error, modifier = fieldModifier)

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.email = it
                    viewModel.emailTouched = true
                },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = viewModel.emailTouched && viewModel.emailError != null,
                modifier = fieldModifier
            )
            if (viewModel.emailTouched && viewModel.emailError != null)
                Text(viewModel.emailError!!, color = MaterialTheme.colorScheme.error, modifier = fieldModifier)

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = {
                    viewModel.username = it
                    viewModel.usernameTouched = true
                },
                label = { Text("Username") },
                singleLine = true,
                isError = viewModel.usernameTouched && viewModel.usernameError != null,
                modifier = fieldModifier
            )
            if (viewModel.usernameTouched && viewModel.usernameError != null)
                Text(viewModel.usernameError!!, color = MaterialTheme.colorScheme.error, modifier = fieldModifier)

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
                modifier = fieldModifier
            )
            if (viewModel.passwordTouched && viewModel.passwordError != null)
                Text(viewModel.passwordError!!, color = MaterialTheme.colorScheme.error, modifier = fieldModifier)

            Button(
                onClick = {
                    viewModel.nameTouched = true
                    viewModel.lastNameTouched = true
                    viewModel.emailTouched = true
                    viewModel.usernameTouched = true
                    viewModel.passwordTouched = true
                    viewModel.validate()
                    if (viewModel.allValid) {
                        viewModel.registerUser(
                            user = User(
                                name = viewModel.name,
                                lastName = viewModel.lastName,
                                email = viewModel.email,
                                username = viewModel.username,
                                password = viewModel.password
                            ),
                            onSuccess = {
                                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = viewModel.allValid,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 8.dp)
            ) {
                Text("Register")
            }
        }
    }
}