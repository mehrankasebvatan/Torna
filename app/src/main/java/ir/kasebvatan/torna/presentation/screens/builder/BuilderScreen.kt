package ir.kasebvatan.torna.presentation.screens.builder

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.kasebvatan.torna.R
import ir.kasebvatan.torna.data.FieldModel
import ir.kasebvatan.torna.data.Util
import ir.kasebvatan.torna.presentation.component.TornaTextFieldNumeric
import ir.kasebvatan.torna.presentation.theme.TornaTheme


@Composable
fun BuilderScreen(
    backHandler: () -> Unit,
    viewModel: BuilderViewModel = viewModel()
) {

    BackHandler { backHandler() }

    val context = LocalContext.current

    var amount by rememberSaveable {
        mutableStateOf("5000")
    }
    var pan by rememberSaveable {
        mutableStateOf("6274123456789012")
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(32.dp))

        Text("Builder Screen", fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))

        TornaTextFieldNumeric(
            label = "Primary Account Number",
            value = pan,
            onValueChange = { pan = it; viewModel.clear() },
            maxLength = 16
        )
        Spacer(Modifier.height(8.dp))

        TornaTextFieldNumeric(
            label = "Amount",
            value = amount,
            onValueChange = { amount = it; viewModel.clear() },
            maxLength = 12
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                Util.hideKeyboard(context)
                if (pan.length == 16 && amount.isNotEmpty()) {
                    viewModel.buildIsoMessage(pan, amount, context)
                }
            }) {
            Text("Calculate ISO8583")
        }

        viewModel.isoMessage?.let {
            PrintIso(it, viewModel.fields)
        }


    }

}


@Composable
fun PrintIso(isoMessage: String, fields: List<FieldModel>) {

    val context = LocalContext.current

    Spacer(Modifier.height(32.dp))

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("ISO Message:", modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_copy),
            contentDescription = "Copy ISO",
            modifier = Modifier
                .size(32.dp)
                .clickable(true) {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("", isoMessage)
                    clipboardManager.setPrimaryClip(clip)
                    Toast.makeText(context, "Copy Successful!", Toast.LENGTH_SHORT).show()
                }
        )

    }

    Spacer(Modifier.height(8.dp))

    Text(
        text = isoMessage,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    )

    Spacer(Modifier.height(16.dp))

    LazyColumn(
        Modifier.fillMaxWidth()
    ) {

        items(fields) { item ->
            FieldItem(item)
        }

    }

}


@Composable
fun FieldItem(item: FieldModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 32.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    "Field${item.fieldNumber}:",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Text(
                    "(${item.fieldType})",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                item.fieldTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
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
private fun BuilderScreenPreview() {
    TornaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            //BuilderScreen()
            FieldItem(FieldModel(4, "test", "type"))
        }
    }
}
