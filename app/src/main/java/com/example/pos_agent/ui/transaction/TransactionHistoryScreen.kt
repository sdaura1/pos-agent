package com.example.pos_agent.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pos_agent.R
import com.example.pos_agent.core.ui.theme.POSAGENTTheme

@Composable
fun TransactionHistoryScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.transaction_history_title),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionHistoryScreenPreview() {
    POSAGENTTheme {
        TransactionHistoryScreen(modifier = Modifier.fillMaxSize())
    }
}
