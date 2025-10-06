package ir.kasebvatan.torna.presentation.component

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kasebvatan.torna.R
import ir.kasebvatan.torna.presentation.theme.TornaTheme
import kotlin.random.Random


@Composable
fun TornaTextFieldNumeric(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {

        TextField(
            trailingIcon = {
                AnimatedVisibility(visible = value.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "CLear",
                        modifier = Modifier.clickable(true) {
                            onValueChange("")
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            label = { Text(label) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "${value.length}/$maxLength",
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Right
        )

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
private fun TornaTextFieldPreview() {
    TornaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            TornaTextFieldNumeric(
                Modifier,
                label = "Label",
                value = "1234",
                onValueChange = {},
                maxLength = 6)
        }
    }
}