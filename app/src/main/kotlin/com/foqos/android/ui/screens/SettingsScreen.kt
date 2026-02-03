package com.foqos.android.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.foqos.android.BuildConfig
import com.foqos.android.util.PermissionChecker

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val permissionChecker = remember { PermissionChecker(context) }
    
    var accessibilityEnabled by remember { mutableStateOf(permissionChecker.isAccessibilityEnabled()) }
    var usageStatsEnabled by remember { mutableStateOf(permissionChecker.isUsageStatsEnabled()) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Permissions Section
        item {
            Text(
                text = "Permissions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            PermissionItem(
                title = "Accessibility Service",
                description = "Required to detect and block app launches",
                isEnabled = accessibilityEnabled,
                isRequired = true,
                onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                }
            )
        }
        
        item {
            PermissionItem(
                title = "Usage Stats Access",
                description = "Required to monitor app usage",
                isEnabled = usageStatsEnabled,
                isRequired = true,
                onClick = {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    context.startActivity(intent)
                }
            )
        }
        
        // About Section
        item {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }
        
        item {
            SettingsItem(
                title = "Version",
                subtitle = "v${BuildConfig.VERSION_NAME}",
                icon = Icons.Default.Info,
                onClick = {}
            )
        }
        
        item {
            SettingsItem(
                title = "Open Source",
                subtitle = "View on GitHub",
                icon = Icons.Default.Send,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nish261/foqos-android"))
                    context.startActivity(intent)
                }
            )
        }
        
        item {
            SettingsItem(
                title = "Privacy Policy",
                subtitle = "Your data stays on your device",
                icon = Icons.Default.Lock,
                onClick = {}
            )
        }
        
        item {
            SettingsItem(
                title = "iOS Version",
                subtitle = "Check out the original Foqos",
                icon = Icons.Default.Phone,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.foqos.app/"))
                    context.startActivity(intent)
                }
            )
        }
        
        // Data Section
        item {
            Text(
                text = "Data",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }
        
        item {
            SettingsItem(
                title = "Export Data",
                subtitle = "Export profiles and sessions",
                icon = Icons.Default.Share,
                onClick = { /* TODO: Implement export */ }
            )
        }
        
        item {
            SettingsItem(
                title = "Clear All Data",
                subtitle = "Delete all profiles and sessions",
                icon = Icons.Default.Delete,
                onClick = { /* TODO: Implement clear data */ },
                isDestructive = true
            )
        }
    }
}

@Composable
fun PermissionItem(
    title: String,
    description: String,
    isEnabled: Boolean,
    isRequired: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else if (isRequired)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (isRequired) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "REQUIRED",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isEnabled) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Icon(
                imageVector = if (isEnabled) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isEnabled) 
                    MaterialTheme.colorScheme.primary 
                else if (isRequired)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = if (isDestructive) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                color = if (isDestructive)
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}
