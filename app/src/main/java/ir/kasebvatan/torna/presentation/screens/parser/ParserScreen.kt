package ir.kasebvatan.torna.presentation.screens.parser

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.kasebvatan.torna.data.Util
import ir.kasebvatan.torna.presentation.theme.TornaTheme


@Composable
fun ParserScreen(
    backHandler: () -> Unit,
    viewModel: ParserViewModel = viewModel()
) {

    BackHandler { backHandler() }

    val isoMessage by viewModel.isoMessage.collectAsState()
    val parsedFields by viewModel.parsedFields.collectAsState()
    val showParsedMessage by viewModel.showParsedMessage.collectAsState()
    val error by viewModel.error.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Text("Parser Screen", fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))

        TextField(
            trailingIcon = {
                AnimatedVisibility(visible = isoMessage.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "CLear",
                        modifier = Modifier.clickable(true) {
                            viewModel.onIsoMessageChange("")
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = isoMessage,
            onValueChange = { viewModel.onIsoMessageChange(it) },
            label = { Text("ISO Message") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            Util.hideKeyboard(context)
            viewModel.parseMessage()
        }) {
            Text("Parse ISO8583")
        }

        if (showParsedMessage) {
            ShowParsedMessage(fields = parsedFields)
        }
    }
}

@Composable
fun ShowParsedMessage(fields: List<String>) {

    Spacer(Modifier.height(16.dp))

    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        items(fields) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                Text(
                    item, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true
)
@Preview(
    name = "Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun ParserScreenPreview() {
    TornaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            ParserScreen(backHandler = {})
        }
    }
}
