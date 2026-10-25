package com.lendflow.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.designsystem.component.InkGradient
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing

private data class Slide(@param:DrawableRes val icon: Int, val title: String, val body: String)

private val slides = listOf(
    Slide(R.drawable.ic_bolt, "Borrow with confidence", "See your personalised rate in minutes. Checking won't affect your credit score."),
    Slide(R.drawable.ic_trending_up, "Stay on top of repayments", "Track every loan, see what's left and overpay whenever you like, with no fees."),
    Slide(R.drawable.ic_shield, "Secure by design", "Passcode and biometric sign-in. A simulation, so no real money or data is involved."),
)

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLogIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { slides.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(InkGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(Spacing.lg),
    ) {
        Wordmark()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            SlideContent(slides[page])
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = Spacing.lg)
                .semantics { contentDescription = "Page ${pagerState.currentPage + 1} of ${slides.size}" },
        ) {
            repeat(slides.size) { index ->
                val selected = pagerState.currentPage == index
                val width by animateDpAsState(if (selected) 24.dp else 8.dp, label = "dot")
                val color by animateColorAsState(if (selected) BrandColors.Mint else BrandColors.OnInkMuted, label = "dotColor")
                Box(
                    Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color),
                )
            }
        }

        PrimaryButton(text = "Get started", onClick = onGetStarted, onInk = true)
        TextButton(
            onClick = onLogIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.sm),
        ) {
            Text("I already have an account", color = BrandColors.OnInk, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
internal fun Wordmark(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            append("lendflow")
            withStyle(SpanStyle(color = BrandColors.Mint)) { append(".") }
        },
        color = BrandColors.OnInk,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.semantics { contentDescription = "LendFlow" },
    )
}

@Composable
private fun SlideContent(slide: Slide) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.sm),
    ) {
        // Concentric rings give the slide an illustration without any bitmap assets.
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(BrandColors.InkGlow.copy(alpha = 0.35f)),
            )
            Box(
                Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(BrandColors.InkGlow.copy(alpha = 0.7f)),
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(BrandColors.Mint),
            ) {
                Icon(painterResource(slide.icon), contentDescription = null, tint = BrandColors.OnMint, modifier = Modifier.size(48.dp))
            }
        }
        Spacer(Modifier.height(Spacing.xxl))
        Text(
            slide.title,
            color = BrandColors.OnInk,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.md))
        Text(
            slide.body,
            color = BrandColors.OnInkMuted,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
    LendFlowTheme {
        WelcomeScreen(onGetStarted = {}, onLogIn = {})
    }
}
