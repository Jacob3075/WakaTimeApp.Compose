package com.jacob.wakatimeapp.login.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.button
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.pageTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
@Destination
fun LoginPage(
    loginPageNavigator: LoginPageNavigator,
    snackbarHostState: SnackbarHostState,
    snackBarCoroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) = LoginPage(
    loginPageNavigator = loginPageNavigator,
    snackbarHostState = snackbarHostState,
    modifier = modifier,
    snackBarCoroutineScope = snackBarCoroutineScope,
    viewModel = hiltViewModel(),
)

@Composable
private fun LoginPage(
    loginPageNavigator: LoginPageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackBarCoroutineScope: CoroutineScope,
    viewModel: LoginPageViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    LaunchedEffect(viewState) {
        Timber.d("viewState: $viewState")
        when (val viewStateInstance = viewState) {
            is LoginPageState.NewLoginSuccess -> loginPageNavigator.toExtractUserDataPage()
            is LoginPageState.ExistingLoginSuccess -> loginPageNavigator.toHomePage()

            is LoginPageState.Error -> showSnackBar(
                viewStateInstance,
                snackBarCoroutineScope,
                snackbarHostState,
            )

            else -> Unit
        }
    }

    LoginPageContent(
        viewState = viewState,
        getLoginAuthIntent = viewModel::getAuthIntent,
        authDataNotFound = viewModel::authDataNotFound,
        exchangeToken = viewModel::exchangeToken,
        modifier = modifier,
    )
}

@Composable
private fun LoginPageContent(
    viewState: LoginPageState,
    getLoginAuthIntent: () -> Intent?,
    authDataNotFound: (Intent) -> Unit,
    exchangeToken: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val launcher = authActivityResultLauncher(
        authDataNotFound = authDataNotFound,
        exchangeToken = exchangeToken,
    )

    when (viewState) {
        is LoginPageState.Idle, is LoginPageState.Error -> LoginPageIdleState(
            onLoginButtonClicked = { launcher.launch(getLoginAuthIntent()) },
            modifier = modifier,
        )

        is LoginPageState.Loading -> LoginPageLoading()
        else -> Unit
    }
}

@Composable
private fun LoginPageLoading() {
    WtaAnimation(
        animation = MaterialTheme.assets.animations.randomLoadingAnimation,
        text = "Loading..",
        modifier = Modifier.fillMaxSize(),
    )
}

private fun showSnackBar(
    viewState: LoginPageState.Error,
    snackBarCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) = snackBarCoroutineScope.launch {
    snackbarHostState.showSnackbar(
        message = viewState.message,
        duration = SnackbarDuration.Long,
    )
}

@Composable
private fun LoginPageIdleState(
    onLoginButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = MaterialTheme.spacing

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = spacing.small, bottom = spacing.large)
            .padding(horizontal = spacing.small),
    ) {
        AppTitle()
        LoginButton(onClick = onLoginButtonClicked)
    }
}

@Composable
private fun AppTitle() = Text(
    text = "Wakatime Client",
    style = MaterialTheme.typography.pageTitle,
)

@Composable
private fun authActivityResultLauncher(
    authDataNotFound: (Intent) -> Unit,
    exchangeToken: (Intent) -> Unit,
) =
    rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val data = result.data
        if (data == null) {
            authDataNotFound(result.data!!)
            return@rememberLauncherForActivityResult
        }
        data.let(exchangeToken)
    }

@Composable
private fun LoginButton(
    onClick: () -> Unit,
) {
    val gradient = MaterialTheme.gradients.facebookMessenger
    val loginButtonGradient =
        Brush.horizontalGradient(gradient.colorList)
    val buttonShape = RoundedCornerShape(percent = 45)
    val spacing = MaterialTheme.spacing
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = buttonShape,
        contentPadding = PaddingValues(),
        modifier = Modifier
            .padding(horizontal = spacing.large)
            .shadow(elevation = 8.dp, shape = buttonShape, clip = false),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(loginButtonGradient, buttonShape)
                .padding(vertical = spacing.small),
        ) {
            Text(
                text = "Login to Wakatime".uppercase(),
                style = MaterialTheme.typography.button,
                color = gradient.onEndColor,
                modifier = Modifier.padding(spacing.medium),
            )
        }
    }
}

@WtaPreviews
@Composable
private fun LoginPagePreview(
    @PreviewParameter(LoginPagePreviewProvider::class) state: LoginPageState,
) = WakaTimeAppTheme {
    Surface {
        LoginPageContent(
            viewState = state,
            getLoginAuthIntent = { null },
            authDataNotFound = {},
            exchangeToken = {},
        )
    }
}

class LoginPagePreviewProvider : PreviewParameterProvider<LoginPageState> {
    override val values = sequenceOf(
        LoginPageState.Idle,
        LoginPageState.Loading,
        LoginPageState.Error("Error"),
    )
}
