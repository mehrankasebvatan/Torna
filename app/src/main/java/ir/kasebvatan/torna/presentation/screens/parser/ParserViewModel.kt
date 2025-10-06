package ir.kasebvatan.torna.presentation.screens.parser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.BASE24Packager

class ParserViewModel : ViewModel() {

    private val _isoMessage = MutableStateFlow("") //MutableStateFlow("020052200000000000001662741234567890120000000050000713190702000013")
    val isoMessage = _isoMessage.asStateFlow()

    private val _parsedFields = MutableStateFlow<List<String>>(emptyList())
    val parsedFields = _parsedFields.asStateFlow()

    private val _showParsedMessage = MutableStateFlow(false)
    val showParsedMessage = _showParsedMessage.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun onIsoMessageChange(newMessage: String) {
        _isoMessage.value = newMessage
        _showParsedMessage.value = false
        _parsedFields.value = emptyList()
    }

    fun parseMessage() {
        if (_isoMessage.value.length > 64) {
            viewModelScope.launch {
                try {
                    val packager = BASE24Packager()
                    val message = ISOMsg()
                    message.packager = packager
                    val data = _isoMessage.value.toByteArray()
                    message.unpack(data)

                    val fields = mutableListOf<String>()
                    fields.add("MTI: ${message.mti}")
                    fields.add("Bitmap: ${_isoMessage.value.substring(4..19)}")
                    for (item in 1..message.maxField) {
                        if (message.hasField(item)) {
                            fields.add("Field$item: ${message.getString(item)}")
                        }
                    }
                    _parsedFields.value = fields
                    _showParsedMessage.value = true
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = e.message ?: "An error occurred during parsing."
                    _showParsedMessage.value = false
                    _parsedFields.value = emptyList()
                }
            }
        }
    }
}
