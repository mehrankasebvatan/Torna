package ir.kasebvatan.torna.data

import androidx.compose.ui.graphics.vector.ImageVector

data class ContactModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector? = null,
    val iconRes: Int? = null,
    val actionType: ContactActionType
)

enum class ContactActionType {
    EMAIL,
    CALL,
    LINK
}
