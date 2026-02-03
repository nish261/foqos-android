package com.foqos.android.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.foqos.android.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPickerScreen(
    selectedApps: List<String>,
    onAppsSelected: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var apps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPackages by remember { mutableStateOf(selectedApps.toSet()) }
    
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            apps = withContext(Dispatchers.IO) {
                val pm = context.packageManager
                pm.getInstalledApplications(PackageManager.GET_META_DATA)
                    .filter { app ->
                        // Only show launchable apps
                        pm.getLaunchIntentForPackage(app.packageName) != null &&
                        app.packageName != context.packageName // Don't show Foqos itself
                    }
                    .map { app ->
                        AppInfo(
                            packageName = app.packageName,
                            appName = app.loadLabel(pm).toString(),
                            icon = try { app.loadIcon(pm) } catch (e: Exception) { null },
                            isSelected = selectedApps.contains(app.packageName)
                        )
                    }
                    .sortedBy { it.appName.lowercase() }
            }
            isLoading = false
        }
    }
    
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isBlank()) {
            apps
        } else {
            apps.filter {
                it.appName.contains(searchQuery, ignoreCase = true) ||
                it.packageName.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Apps") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            onAppsSelected(selectedPackages.toList())
                            onDismiss()
                        }
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search apps...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, "Clear")
                        }
                    }
                },
                singleLine = true
            )
            
            // Selected count
            if (selectedPackages.isNotEmpty()) {
                Text(
                    text = "${selectedPackages.size} ${if (selectedPackages.size == 1) "app" else "apps"} selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // App List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredApps) { app ->
                        AppPickerItem(
                            app = app,
                            isSelected = selectedPackages.contains(app.packageName),
                            onToggle = {
                                selectedPackages = if (selectedPackages.contains(app.packageName)) {
                                    selectedPackages - app.packageName
                                } else {
                                    selectedPackages + app.packageName
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppPickerItem(
    app: AppInfo,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(app.appName) },
        supportingContent = { Text(app.packageName, style = MaterialTheme.typography.bodySmall) },
        leadingContent = {
            // Would show app icon here if we had it loaded properly
            Icon(Icons.Default.Phone, contentDescription = null)
        },
        trailingContent = {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )
        },
        modifier = modifier.clickable(onClick = onToggle)
    )
}
