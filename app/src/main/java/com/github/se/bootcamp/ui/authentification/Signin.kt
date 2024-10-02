package com.github.se.bootcamp.ui.authentification

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.bootcamp.R
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
  val scope = rememberCoroutineScope()
  return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      result ->
    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
      val account = task.getResult(ApiException::class.java)!!
      val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
      scope.launch {
        val authResult = Firebase.auth.signInWithCredential(credential).await()
        onAuthComplete(authResult)
      }
    } catch (e: ApiException) {
      onAuthError(e)
    }
  }
}

@Preview
@Composable
fun SignInScreen(navigationActions: NavigationActions) {

  var user by remember { mutableStateOf(Firebase.auth.currentUser) }

  val token = stringResource(R.string.default_web_client_id)
  val context = LocalContext.current

  val launcher =
      rememberFirebaseAuthLauncher(
          onAuthComplete = { result -> user = result.user }, onAuthError = { user = null })

  if (user == null) {

    Scaffold(modifier = Modifier.testTag("loginScreen")) { innerPadding ->
      println(innerPadding)
      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
            // Image
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "image description",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.width(189.dp).height(189.dp),
            )

            // Text label
            Text(
                text = "Welcome",

                // M3/display/large
                style =
                    TextStyle(
                        fontSize = 57.sp,
                        lineHeight = 64.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF191C1E),
                        textAlign = TextAlign.Center,
                    ),
                modifier = Modifier.width(258.dp).height(65.dp).testTag("loginTitle"))

            // Button

            Button(
                modifier =
                    Modifier.border(
                            width = 1.dp,
                            color = Color(0xFFDADCE0),
                            shape = RoundedCornerShape(size = 20.dp))
                        .width(250.dp)
                        .height(40.dp)
                        .background(
                            color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
                        .testTag("loginButton"),
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                  val gso =
                      GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                          .requestIdToken(token)
                          .requestEmail()
                          .build()
                  val googleSignInClient = GoogleSignIn.getClient(context, gso)
                  launcher.launch(googleSignInClient.signInIntent)
                }) {
                  Image(
                      painter = painterResource(id = R.drawable.google_logo),
                      contentDescription = "image description",
                      contentScale = ContentScale.Fit,
                      modifier = Modifier.padding(0.dp).width(20.dp).height(20.dp))

                  // Text part
                  Text(
                      text = "Sign in with Google",
                      style =
                          TextStyle(
                              fontSize = 14.sp,
                              lineHeight = 17.sp,
                              fontWeight = FontWeight(500),
                              color = Color(0xFF3C4043),
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.25.sp,
                          ),
                      modifier = Modifier.width(125.dp).height(17.dp))
                }
          }
    }
  } else {
    Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show()
    navigationActions.navigateTo(Screen.OVERVIEW)
  }
}
