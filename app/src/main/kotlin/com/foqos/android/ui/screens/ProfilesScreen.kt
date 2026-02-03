package com.foqos.android.ui.screens

import android.app.Application
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foqos.android.data.model.BlockingProfile
import com.foqos.android.data.model.BlockingStrategy
import com.foqos.android.ui.components.ActiveSessionCard
import com.foqos.android.ui.viewmodel.ProfileViewModel
import com.foqos.android.ui.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    sessionViewModel: SessionViewModel = viewModel()
) {
    val profiles by profileViewModel.profiles.collectAsState()
    val activeProfile by profileViewModel.activeProfile.collectAsState()
    val activeSession by sessionViewModel.activeSession.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<BlockingProfile?>(null) }
    
    Scaffold(
        floatingActionButton = {
            if (activeSession == null) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true }
                ) {
                    Icon(Icons.Default.Add, "Create Profile")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Active Session Card
            activeSession?.let { session ->
                ActiveSessionCard(
                    session = session,
                    onStop = { sessionViewModel.endSession(session.id) },
                    onPause = { sessionViewModel.pauseSession(session.id) },
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Profile List
            if (profiles.isEmpty()) {
                EmptyProfilesState(
                    onCreateClick = { showCreateDialog = true },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(profiles) { profile ->
                        ProfileCard(
                            profile = profile,
                            isActive = profile.id == activeProfile?.id,
                            onStart = {
                                sessionViewModel.startSession(profile)
                            },
                            onClick = { selectedProfile = profile },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
    
    // Create/Edit Profile Dialog
    if (showCreateDialog) {
        CreateProfileDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { profile ->
                profileViewModel.createProfile(profile)
                showCreateDialog = false
            }
        )
    }
    
    // Profile Details Bottom Sheet
    selectedProfile?.let { profile ->
        ProfileDetailsSheet(
            profile = profile,
            onDismiss = { selectedProfile = null },
            onEdit = { /* TODO: Navigate to edit screen */ },
            onDelete = {
                profileViewModel.deleteProfile(profile)
                selectedProfile = null
            }
        )
    }
}

@Composable
fun EmptyProfilesState(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Face,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Profiles Yet",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Create your first blocking profile to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = onCreateClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Profile")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    profile: BlockingProfile,
    isActive: Boolean,
    onStart: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${profile.blockedApps.size} apps • ${profile.strategy.displayName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                if (!isActive) {
                    FilledTonalButton(
                        onClick = onStart
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start")
                    }
                } else {
                    AssistChip(
                        onClick = {},
                        label = { Text("Active") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CreateProfileDialog(
    onDismiss: () -> Unit,
    onSave: (BlockingProfile) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var strategy by remember { mutableStateOf(BlockingStrategy.MANUAL) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name") },
                    placeholder = { Text("Work, Study, Sleep...") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Blocking Strategy", style = MaterialTheme.typography.labelLarge)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                BlockingStrategy.values().forEach { strat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { strategy = strat }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = strategy == strat,
                            onClick = { strategy = strat }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(strat.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            BlockingProfile(
                                name = name,
                                blockedApps = emptyList(),
                                blockedWebsites = emptyList(),
                                strategy = strategy
                            )
                        )
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsSheet(
    profile: BlockingProfile,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ListItem(
                headlineContent = { Text("Edit Profile") },
                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.clickable { onEdit() }
            )
            
            ListItem(
                headlineContent = { Text("Delete Profile") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                modifier = Modifier.clickable { onDelete() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

val BlockingStrategy.displayName: String
    get() = when (this) {
        BlockingStrategy.MANUAL -> "Manual"
        BlockingStrategy.NFC -> "NFC Tag"
        BlockingStrategy.QR -> "QR Code"
        BlockingStrategy.NFC_MANUAL -> "NFC + Manual"
        BlockingStrategy.QR_MANUAL -> "QR + Manual"
        BlockingStrategy.NFC_TIMER -> "NFC + Timer"
        BlockingStrategy.QR_TIMER -> "QR + Timer"
    }
