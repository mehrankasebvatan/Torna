package ir.kasebvatan.torna.presentation.screens.contact

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import ir.kasebvatan.torna.R
import ir.kasebvatan.torna.data.ContactActionType
import ir.kasebvatan.torna.data.ContactModel

class ContactViewModel : ViewModel() {

    val contactItems = listOf(
        ContactModel(
            id = 1,
            title = "Email",
            subtitle = "mehran.kasebvatan@gmail.com",
            icon = Icons.Default.Email,
            actionType = ContactActionType.EMAIL
        ),
        ContactModel(
            id = 2,
            title = "Phone",
            subtitle = "09216380039",
            icon = Icons.Default.Call,
            actionType = ContactActionType.CALL
        ),
        ContactModel(
            id = 2,
            title = "WhatsApp",
            subtitle = "https://wa.me/09216380039",
            iconRes = R.drawable.ic_whats_app,
            actionType = ContactActionType.LINK
        ),
        ContactModel(
            id = 3,
            title = "GitHub",
            subtitle = "https://github.com/mehrankasebvatan/Torna.git",
            iconRes = R.drawable.ic_github,
            actionType = ContactActionType.LINK
        )
    )

    fun handleContactAction(context: Context, item: ContactModel) {
        when (item.actionType) {
            ContactActionType.EMAIL -> sendEmail(context)
            ContactActionType.CALL -> call(context)
            ContactActionType.LINK -> openLink(context, item.subtitle)
        }
    }

    private fun sendEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("mehran.kasebvatan@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Torna System Task")
            putExtra(Intent.EXTRA_TEXT, "")
        }

        try {
            val chooser = Intent.createChooser(intent, "Send Email")
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun call(context: Context) {
        val phoneNumber = "09216380039"
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}