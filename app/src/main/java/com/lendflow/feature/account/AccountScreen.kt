package com.lendflow.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.Avatar
import com.lendflow.core.designsystem.component.InkHero
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.ListRow
import com.lendflow.core.designsystem.component.SecondaryButton
import com.lendflow.core.designsystem.component.SimulationNote
import com.lendflow.core.designsystem.component.StatusBarScrim
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing

@Composable
fun AccountScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val customer = SampleData.customer
    // Toggles are local UI state for now; Phase 6 persists them (DataStore) and wires biometrics.
    var biometrics by rememberSaveable { mutableStateOf(true) }
    var notifications by rememberSaveable { mutableStateOf(true) }
    var confirmSignOut by rememberSaveable { mutableStateOf(false) }

    Box(modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                InkHero {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(Spacing.md))
                        Avatar("${customer.firstName.first()}${customer.lastName.first()}", size = 80.dp)
                        Spacer(Modifier.height(Spacing.md))
                        Text(
                            "${customer.firstName} ${customer.lastName}",
                            color = BrandColors.OnInk,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(customer.email, color = BrandColors.OnInkMuted, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(Spacing.xs))
                        Text("Member since ${customer.memberSince}", color = BrandColors.Mint, style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.height(Spacing.md))
                    }
                }
            }

            item {
                SettingsGroup("Profile") {
                    ListRow("Personal details", icon = R.drawable.ic_person, subtitle = "Name, address, phone", onClick = {})
                    GroupDivider()
                    ListRow("Statements and documents", icon = R.drawable.ic_document, subtitle = "Agreements and annual statements", onClick = {})
                }
            }
            item {
                SettingsGroup("Security") {
                    ListRow(
                        "Biometric sign-in",
                        icon = R.drawable.ic_fingerprint,
                        subtitle = "Use your fingerprint or face",
                        trailing = { Switch(checked = biometrics, onCheckedChange = { biometrics = it }) },
                    )
                    GroupDivider()
                    ListRow("Change passcode", icon = R.drawable.ic_lock, onClick = {})
                }
            }
            item {
                SettingsGroup("Preferences") {
                    ListRow(
                        "Push notifications",
                        icon = R.drawable.ic_notifications,
                        subtitle = "Repayment reminders and updates",
                        trailing = { Switch(checked = notifications, onCheckedChange = { notifications = it }) },
                    )
                }
            }
            item {
                SettingsGroup("Support") {
                    ListRow("Help centre", icon = R.drawable.ic_help, onClick = {})
                    GroupDivider()
                    ListRow("About LendFlow", icon = R.drawable.ic_info, subtitle = "Version 0.1.0", onClick = {})
                }
            }
            item {
                SimulationNote(
                    "LendFlow is a simulation. No real money, credit checks or personal data are used.",
                    modifier = Modifier.padding(horizontal = Spacing.md),
                )
            }
            item {
                SecondaryButton("Sign out", onClick = { confirmSignOut = true }, modifier = Modifier.padding(horizontal = Spacing.md))
            }
        }
        StatusBarScrim()
    }

    if (confirmSignOut) {
        AlertDialog(
            onDismissRequest = { confirmSignOut = false },
            title = { Text("Sign out?") },
            text = { Text("You'll need your passcode to sign back in.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmSignOut = false
                    onSignOut()
                }) { Text("Sign out") }
            },
            dismissButton = { TextButton(onClick = { confirmSignOut = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column(Modifier.padding(horizontal = Spacing.md)) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(start = Spacing.sm, bottom = Spacing.sm)
                .semantics { heading() },
        )
        LendFlowCard(contentPadding = 0.dp) { content() }
    }
}

@Composable
private fun GroupDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(start = 76.dp))
}

@Preview(showSystemUi = true)
@Composable
private fun AccountScreenPreview() {
    LendFlowTheme {
        AccountScreen(onSignOut = {})
    }
}
