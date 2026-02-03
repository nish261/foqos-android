package com.foqos.android

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.foqos.android.nfc.NFCManager
import com.foqos.android.ui.FoqosApp
import com.foqos.android.ui.theme.FoqosTheme

class MainActivity : ComponentActivity() {
    
    private var nfcManager: NFCManager? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize NFC if available
        NfcAdapter.getDefaultAdapter(this)?.let { adapter ->
            nfcManager = NFCManager(this, adapter)
        }
        
        setContent {
            FoqosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoqosApp()
                }
            }
        }
        
        // Handle NFC intent if launched via NFC tag
        handleIntent(intent)
    }
    
    override fun onResume() {
        super.onResume()
        nfcManager?.enableForegroundDispatch()
    }
    
    override fun onPause() {
        super.onPause()
        nfcManager?.disableForegroundDispatch()
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED,
            NfcAdapter.ACTION_TAG_DISCOVERED -> {
                nfcManager?.handleNFCIntent(intent)
            }
            Intent.ACTION_VIEW -> {
                // Handle deep link from QR code
                intent.data?.let { uri ->
                    if (uri.host == "foqos.app" && uri.pathSegments.firstOrNull() == "profile") {
                        val profileId = uri.pathSegments.getOrNull(1)
                        // Handle profile toggle
                        // This will be implemented in the ViewModel
                    }
                }
            }
        }
    }
}
