package com.example.forzafootball.ui.worldcup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

/**
 * Displays a team badge from [imageUrl].
 *
 * While loading, or if the image is missing/fails (e.g. the campaign bucket has no badge),
 * it gracefully falls back to a circular avatar with the team's initial.
 */
@Composable
fun TeamBadge(
    teamName: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val fallback: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = teamName.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }

    if (imageUrl == null) {
        fallback()
        return
    }

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = teamName,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size),
        loading = { fallback() },
        error = { fallback() },
        success = { SubcomposeAsyncImageContent() },
    )
}

