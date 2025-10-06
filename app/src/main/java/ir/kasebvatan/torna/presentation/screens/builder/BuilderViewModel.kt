package ir.kasebvatan.torna.presentation.screens.builder

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ir.kasebvatan.torna.data.Cache
import ir.kasebvatan.torna.data.FieldModel
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.BASE24Packager
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

class BuilderViewModel : ViewModel() {

    var isoMessage by mutableStateOf<String?>(null)
        private set

    var fields by mutableStateOf<List<FieldModel>>(emptyList())
        private set

    fun buildIsoMessage(pan: String, amount: String, context: Context) {
        val stan = getStan()
        val message = ISOMsg("0200")
        val packager = BASE24Packager()
        message.packager = packager
        message.set(2, pan)
        message.set(4, amount.padStart(12, '0')) // Amount
        message.set(7, getDateTime())
        message.set(11, stan)
        isoMessage = String(message.pack())
        Log.i("isoMessage ==>", isoMessage.toString())

        val currentFields = mutableListOf<FieldModel>().apply {
            add(
                FieldModel(
                    2, message.getString(2), "Primary Account Number"
                )
            )
            add(FieldModel(4, message.getString(4), "Amount: $amount"))
            add(
                FieldModel(
                    7, message.getString(7), "Date & Time: MMDDHHMMSS"
                )
            )
            add(FieldModel(11, stan, "Stan"))
        }
        fields = currentFields

    }

    fun clear() {
        isoMessage = null
        fields = emptyList()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }

    fun getStan(): String {
        if (Cache["stan", 0] == 999999) Cache["stan"] = 1
        else Cache["stan"] = Cache["stan", 0] + 1
        val stanCache = Cache["stan", 0].toString()
        return if (stanCache.length == 6) stanCache
        else "0".repeat(6 - stanCache.length) + stanCache
    }

    fun getDateTime(): String {
        val date = PersianDate()
        val formater = PersianDateFormat("mjHis")
        return formater.format(date)
    }

}
