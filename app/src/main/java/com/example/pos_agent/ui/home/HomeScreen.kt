package com.example.pos_agent.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pos_agent.R
import com.example.pos_agent.core.ui.theme.POSAGENTTheme
import com.example.pos_agent.domain.auth.model.User

@Composable
fun HomeScreen(
    user: User,
    onTransactionHistoryClick: () -> Unit,
    onAmountClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.home_welcome, user.displayName),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.home_signed_in_as, user.username),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(),
        ) {
            Button(
                onClick = onTransactionHistoryClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.transaction_history_title))
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onAmountClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.amount_title))
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.profile_title))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    POSAGENTTheme {
        HomeScreen(
            user = User(
                id = "user-1",
                username = "operator",
                displayName = "Terminal Operator",
                role = "OPERATOR",
            ),
            onTransactionHistoryClick = {},
            onAmountClick = {},
            onProfileClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
