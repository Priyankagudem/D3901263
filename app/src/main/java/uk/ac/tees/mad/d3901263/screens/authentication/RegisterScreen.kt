package uk.ac.tees.mad.d3901263.screens.authentication

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.repository.GoogleAuthClient
import uk.ac.tees.mad.d3901263.screens.authentication.viewmodel.AuthenticationViewModel
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

@Composable
fun RegisterScreen(
    onRegisterComplete: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val registerState = viewModel.registerState.collectAsState(initial = null)
    val loginState = viewModel.state.collectAsState().value
    val registerUiState = viewModel.registerUiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val loginResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onLoginWithGoogleResult(loginResult)
                }
            }
        }
    )
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fill your information below or register with your social account",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        }

        Spacer(modifier = Modifier.height(90.dp))

        OutlinedTextField(
            value = registerUiState.name,
            onValueChange = {
                viewModel.updateRegisterState(registerUiState.copy(name = it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Full Name")
            },
            maxLines = 1,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = "")
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = registerUiState.email,
            onValueChange = {
                viewModel.updateRegisterState(registerUiState.copy(email = it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Email")
            },
            maxLines = 1,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "")
            }

        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = registerUiState.password,
            onValueChange = {
                viewModel.updateRegisterState(registerUiState.copy(password = it))
            },

            modifier = Modifier.fillMaxWidth(),

            maxLines = 1,
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Outlined.Visibility
                else Icons.Outlined.VisibilityOff

                val description =
                    if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        description,
                    )
                }
            },
            placeholder = {
                Text(text = "Password")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone  = {
                focusManager.clearFocus()
            }),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Lock, contentDescription = "")
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                viewModel.registerUser(
                    username = registerUiState.name,
                    email = registerUiState.email,
                    password = registerUiState.password,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (registerState.value?.isLoading == true) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
            } else {
                Text(text = "Sign up", fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(56.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(smokeWhite)
            )
            Text(text = "or Login with")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(smokeWhite)
            )
        }
        Spacer(modifier = Modifier.height(56.dp))
        IconButton(
            onClick = {
                scope.launch {
                    val loginIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest
                            .Builder(
                                loginIntentSender ?: return@launch
                            )
                            .build()
                    )
                }
            },
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Gray), CircleShape)
                .padding(5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Sign with google",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ")
            Text(
                text = "Login",
                modifier = Modifier.clickable {
                    onLoginClick()
                },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(key1 = loginState.isLoginSuccessful) {
        if (loginState.isLoginSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()
            val user = googleAuthUiClient.getSignedInUser()
            if (user != null) {
                viewModel.saveUserInFirestore(user)
            }
            viewModel.resetState()
            onRegisterComplete()
        }
    }
    LaunchedEffect(key1 = registerState.value?.isSuccess) {
        scope.launch {
            if (registerState.value?.isSuccess?.isNotEmpty() == true) {
                onRegisterComplete()
            }
        }
    }

    LaunchedEffect(key1 = registerState.value?.isError) {
        scope.launch {
            if (registerState.value?.isError?.isNotEmpty() == true) {
                val error = registerState.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }

}

object Register : Navigation {
    override val route: String
        get() = "register"

    override val titleRes: Int
        get() = R.string.register
}