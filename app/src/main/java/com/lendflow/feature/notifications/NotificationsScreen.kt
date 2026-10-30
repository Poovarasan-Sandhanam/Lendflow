package com.lendflow.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.IconBadge
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.Notice
import com.lendflow.core.ui.asDayMonth

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { InkTopBar("Notifications", onBack = onBack) },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.padding(innerPadding),
        ) {
            items(SampleData.notices, key = { it.id }) { NoticeCard(it) }
        }
    }
}

@Composable
private fun NoticeCard(notice: Notice) {
    val container = if (notice.unread) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLowest
    LendFlowCard(containerColor = container) {
        Row(verticalAlignment = Alignment.Top) {
            IconBadge(R.drawable.ic_notifications, containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, contentColor = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(Spacing.md))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(notice.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                    Text(notice.date.asDayMonth(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.size(Spacing.xs))
                Text(notice.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (notice.unread) {
                Spacer(Modifier.width(Spacing.sm))
                Box(
                    Modifier
                        .padding(top = 6.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .semantics { contentDescription = "Unread" },
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NotificationsScreenPreview() {
    LendFlowTheme {
        NotificationsScreen(onBack = {})
    }
}
