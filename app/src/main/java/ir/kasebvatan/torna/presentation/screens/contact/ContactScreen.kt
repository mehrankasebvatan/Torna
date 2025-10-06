package ir.kasebvatan.torna.presentation.screens.contact

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.kasebvatan.torna.data.ContactModel
import ir.kasebvatan.torna.presentation.theme.TornaTheme


@Composable
fun ContactScreen(
    backHandler: () -> Unit,
    viewModel: ContactViewModel = viewModel()
) {
    val context = LocalContext.current
    BackHandler { backHandler() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text("Contact Screen", fontSize = 22.sp)
        Spacer(Modifier.height(32.dp))

        Text(
            text = "This application has been developed for Torna System company's Android interview.\n\nDeveloper: Mehran Kasebvatan",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.contactItems) { contactItem ->
                ContactItemCard(
                    item = contactItem,
                    onClick = {
                        viewModel.handleContactAction(context, contactItem)
                    }
                )
            }
        }
    }
}

@Composable
private fun ContactItemCard(
    item: ContactModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(60.dp)
                )
            } else if (item.iconRes != null) {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.title,
                    modifier = Modifier.size(60.dp)
                )
            }

            Text(
                text = item.title,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
private fun ContactScreenPreview() {
    TornaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            ContactScreen(backHandler = {})
        }
    }
}