package com.shk.hiltfeed.features.home


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.runtime.LaunchedEffect
import com.shk.hiltfeed.features.blog_list.view.BlogListFragment
import com.shk.hiltfeed.features.post.PostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                HomeScreen(homeViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val tabs = listOf("Posts", "Blogs")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // User count
        UserCount(viewModel)

        // Tabs
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        // Content
        when (selectedTabIndex) {
            0 -> PostFragment()
            1 -> BlogListFragment()
        }
    }
}

@Composable
private fun UserCount(viewModel: HomeViewModel) {
    val userCount by viewModel.userCount.collectAsStateWithLifecycle()
    val lastCount by viewModel.lastCount.collectAsStateWithLifecycle()

    LaunchedEffect(userCount) {
        if (userCount > lastCount) {
            viewModel.updateLastCount(userCount)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$userCount Users", style = MaterialTheme.typography.titleMedium)
    }
}