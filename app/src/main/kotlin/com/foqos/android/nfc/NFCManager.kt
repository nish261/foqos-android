package com.foqos.android.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.util.Log
import java.nio.charset.Charset

class NFCManager(
    private val activity: Activity,
    private val nfcAdapter: NfcAdapter
) {
    
    private var onTagRead: ((String) -> Unit)? = null
    private var onTagWrite: ((Boolean) -> Unit)? = null
    
    fun enableForegroundDispatch() {
        val intent = Intent(activity, activity.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        
        val intentFilters = arrayOf(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        )
        
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, null)
    }
    
    fun disableForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(activity)
    }
    
    fun handleNFCIntent(intent: Intent) {
        val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
        
        tag?.let { readTag(it) }
    }
    
    private fun readTag(tag: Tag) {
        val ndef = Ndef.get(tag) ?: return
        
        try {
            ndef.connect()
            val ndefMessage = ndef.ndefMessage
            
            if (ndefMessage != null) {
                val records = ndefMessage.records
                for (record in records) {
                    if (record.tnf == NdefRecord.TNF_WELL_KNOWN &&
                        record.type.contentEquals(NdefRecord.RTD_TEXT)) {
                        val payload = record.payload
                        val text = String(payload, 3, payload.size - 3, Charset.forName("UTF-8"))
                        onTagRead?.invoke(text)
                        break
                    }
                }
            } else {
                // Empty tag - use tag ID as identifier
                val tagId = bytesToHex(tag.id)
                onTagRead?.invoke(tagId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading NFC tag", e)
        } finally {
            try {
                ndef.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing NFC tag", e)
            }
        }
    }
    
    fun writeTag(tag: Tag, data: String): Boolean {
        val ndef = Ndef.get(tag) ?: return false
        
        return try {
            ndef.connect()
            
            val record = createTextRecord(data)
            val message = NdefMessage(arrayOf(record))
            
            if (ndef.maxSize < message.toByteArray().size) {
                Log.e(TAG, "Tag size too small")
                ndef.close()
                return false
            }
            
            ndef.writeNdefMessage(message)
            ndef.close()
            
            onTagWrite?.invoke(true)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error writing NFC tag", e)
            onTagWrite?.invoke(false)
            false
        }
    }
    
    private fun createTextRecord(text: String): NdefRecord {
        val langBytes = "en".toByteArray(Charset.forName("US-ASCII"))
        val textBytes = text.toByteArray(Charset.forName("UTF-8"))
        val payload = ByteArray(1 + langBytes.size + textBytes.size)
        
        payload[0] = langBytes.size.toByte()
        System.arraycopy(langBytes, 0, payload, 1, langBytes.size)
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.size, textBytes.size)
        
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
    }
    
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(bytes.size * 2)
        
        for (byte in bytes) {
            val octet = byte.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(hexChars[firstIndex])
            result.append(hexChars[secondIndex])
        }
        
        return result.toString()
    }
    
    fun setOnTagReadListener(listener: (String) -> Unit) {
        onTagRead = listener
    }
    
    fun setOnTagWriteListener(listener: (Boolean) -> Unit) {
        onTagWrite = listener
    }
    
    companion object {
        private const val TAG = "NFCManager"
        
        fun isNFCAvailable(activity: Activity): Boolean {
            return NfcAdapter.getDefaultAdapter(activity) != null
        }
        
        fun isNFCEnabled(activity: Activity): Boolean {
            val adapter = NfcAdapter.getDefaultAdapter(activity)
            return adapter?.isEnabled == true
        }
    }
}
