package com.carttonme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.carttonme.R
import com.carttonme.model.Smurf

@Composable
fun LoadingScreen(isLoading: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.loading_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun MainScreen(
    smurfs: List<Smurf>,
    isGrid: Boolean,
    onToggleLayout: () -> Unit,
    onSmurfSelected: (Smurf) -> Unit,
    onSmurfMe: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.main_title),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = onToggleLayout) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_layout_toggle),
                    contentDescription = stringResource(id = R.string.toggle_layout)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSmurfMe,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.smurf_me_next))
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (isGrid) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(smurfs.size) { index ->
                    SmurfCard(
                        smurf = smurfs[index],
                        onClick = { onSmurfSelected(smurfs[index]) }
                    )
                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(smurfs) { smurf ->
                    SmurfRow(smurf = smurf, onClick = { onSmurfSelected(smurf) })
                }
            }
        }
    }
}

@Composable
fun SmurfScreen(smurf: Smurf, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = smurf.imageUrl,
            contentDescription = smurf.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = smurf.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.smurf_personality, smurf.text),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedButton(onClick = onBack) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    }
}

@Composable
fun SmurfMeScreen(
    smurfs: List<Smurf>,
    viewModel: SmurfMeViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedImage by viewModel.selectedImage.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val showAd by viewModel.showAd.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.smurf_me_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.smurf_me_mission),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.selectImage("fake://upload") }) {
                        Text(text = stringResource(id = R.string.upload_image))
                    }
                    Button(onClick = { viewModel.selectImage("fake://camera") }) {
                        Text(text = stringResource(id = R.string.take_photo))
                    }
                }
                selectedImage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = stringResource(id = R.string.processing_status, smurfs.random().name))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (isProcessing) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.processing_image))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (showAd) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.ad_title),
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = viewModel::dismissAd) {
                            Text(text = stringResource(id = R.string.close))
                        }
                    }
                    Text(text = stringResource(id = R.string.ad_body))
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}

@Composable
private fun SmurfCard(smurf: Smurf, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = smurf.imageUrl,
                contentDescription = smurf.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = smurf.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun SmurfRow(smurf: Smurf, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = smurf.imageUrl,
                contentDescription = smurf.name,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = smurf.name, style = MaterialTheme.typography.titleMedium)
                Text(text = smurf.text, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
