package com.example.pos_agent.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pos_agent.core.ui.theme.POSAGENTTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "POS Agent - card processing scaffold ready",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    POSAGENTTheme {
        HomeScreen(Modifier.fillMaxSize())
    }
}
