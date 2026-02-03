package com.foqos.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foqos.android.ui.viewmodel.StatsViewModel
import com.foqos.android.ui.components.formatDuration

@Composable
fun StatsScreen(
    statsViewModel: StatsViewModel = viewModel()
) {
    val stats by statsViewModel.stats.collectAsState()
    val recentSessions by statsViewModel.recentSessions.collectAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Your Focus Stats",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // Stats Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Sessions",
                    value = stats.totalSessions.toString(),
                    icon = Icons.Default.List,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Focus Time",
                    value = formatDuration(stats.totalFocusTimeMs),
                    icon = Icons.Default.DateRange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Current Streak",
                    value = "${stats.currentStreak} ${if (stats.currentStreak == 1) "day" else "days"}",
                    icon = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Longest Streak",
                    value = "${stats.longestStreak} ${if (stats.longestStreak == 1) "day" else "days"}",
                    icon = Icons.Default.FavoriteBorder,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            StatCard(
                title = "Blocked App Launches",
                value = stats.totalBlockedLaunches.toString(),
                icon = Icons.Default.Lock,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Recent Sessions
        if (recentSessions.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Sessions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(recentSessions.take(10)) { session ->
                SessionHistoryCard(session = session)
            }
        } else {
            item {
                EmptyStatsState()
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SessionHistoryCard(
    session: com.foqos.android.data.model.BlockingSession
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.profileName,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                val duration = session.endTime?.let { end ->
                    end - session.startTime - session.totalPauseTimeMs
                } ?: 0
                
                Text(
                    text = formatDuration(duration),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                if (session.blockedLaunches > 0) {
                    Text(
                        text = "${session.blockedLaunches} blocked",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Icon(
                imageVector = if (session.completedSuccessfully) 
                    Icons.Default.CheckCircle 
                else 
                    Icons.Default.Close,
                contentDescription = null,
                tint = if (session.completedSuccessfully)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun EmptyStatsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No sessions yet",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Start your first focus session to see stats here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
