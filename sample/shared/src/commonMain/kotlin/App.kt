import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.logsnag.LogSnagSecrets
import com.logsnag.kotlin.LogSnag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val logSnag = remember {
            LogSnag(
                token = LogSnagSecrets.LogSnagToken ?: throw Exception("LogSnagToken is null"),
                project = LogSnagSecrets.LogSnagProject
                    ?: throw Exception("LogSnagProject is null"),
            )
        }
        var isTrackingEnabled by remember { mutableStateOf(true) }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "LogSnag Sample App",
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Project: '${logSnag.getProject()}'",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { sendTrack(coroutineScope, snackbarHostState, logSnag) }) {
                        Text("Send track event")
                    }
                    Button(onClick = { sendIdentify(coroutineScope, snackbarHostState, logSnag) }) {
                        Text("Send identify event")
                    }
                    Button(onClick = {
                        sendInsightTrack(
                            coroutineScope,
                            snackbarHostState,
                            logSnag
                        )
                    }) {
                        Text("Send insight track event")
                    }
                    Button(onClick = {
                        sendInsightIncrement(
                            coroutineScope,
                            snackbarHostState,
                            logSnag
                        )
                    }) {
                        Text("Send insight increment event")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tracking ${if (isTrackingEnabled) "enabled" else "disabled"}")
                        Switch(
                            checked = isTrackingEnabled,
                            onCheckedChange = {
                                isTrackingEnabled = it
                                if (isTrackingEnabled) {
                                    logSnag.enableTracking()
                                } else {
                                    logSnag.disableTracking()
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun sendTrack(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    logSnag: LogSnag
) {
    logSnag.track(
        channel = "sample",
        event = "Hello from $platform!",
        description = "This is a test event",
        icon = "🌱",
        userId = "user_123",
        tags = mapOf("platform" to platform),
    )

    coroutineScope.launch {
        snackbarHostState.showSnackbar("Log sent!")
    }
}

private fun sendIdentify(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    logSnag: LogSnag
) {
    logSnag.identify(
        userId = "user_123",
        properties = mapOf(
            "username" to "mattie",
            "email" to "mattie@gmail.com"
        ),
    )

    coroutineScope.launch {
        snackbarHostState.showSnackbar("Identify sent successfully!")
    }
}

private fun sendInsightTrack(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    logSnag: LogSnag
) {
    logSnag.insightTrack(
        title = "Insight track title",
        value = "test",
        icon = "🍉",
    )

    coroutineScope.launch {
        snackbarHostState.showSnackbar("Insight track sent successfully!")
    }
}

private fun sendInsightIncrement(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    logSnag: LogSnag
) {
    logSnag.insightIncrement(
        title = "Insight increment title",
        value = 1,
        icon = "🍗",
    )

    coroutineScope.launch {
        snackbarHostState.showSnackbar("Insight increment sent successfully!")
    }
}
