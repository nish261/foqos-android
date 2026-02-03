package com.foqos.android.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.foqos.android.data.repository.FoqosRepository

class AppBlockingService : AccessibilityService() {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var repository: FoqosRepository
    private var blockedPackages: Set<String> = emptySet()
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility service connected")
        
        repository = FoqosRepository(applicationContext)
        
        // Observe active profile and update blocked packages
        scope.launch {
            repository.getActiveProfileFlow().collect { profile ->
                blockedPackages = profile?.blockedApps?.toSet() ?: emptySet()
                Log.d(TAG, "Updated blocked packages: ${blockedPackages.size}")
            }
        }
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val packageName = event.packageName?.toString() ?: return
                
                // Don't block Foqos itself
                if (packageName == application.packageName) return
                
                // Check if this app should be blocked
                if (blockedPackages.contains(packageName)) {
                    Log.d(TAG, "Blocking app: $packageName")
                    
                    // Go back to home screen (blocks the app launch)
                    performGlobalAction(GLOBAL_ACTION_HOME)
                    
                    // Increment blocked launch counter
                    scope.launch {
                        repository.getActiveSession()?.let { session ->
                            repository.incrementBlockedLaunch(session.id)
                        }
                    }
                    
                    // TODO: Show a blocking overlay with profile info
                }
            }
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Accessibility service destroyed")
    }
    
    companion object {
        private const val TAG = "AppBlockingService"
    }
}
