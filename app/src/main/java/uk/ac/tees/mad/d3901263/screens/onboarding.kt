package uk.ac.tees.mad.d3901263.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.screens.authentication.Login
import uk.ac.tees.mad.d3901263.screens.splash.LoaderAnimation
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onboardings = listOf(
        OnboardingState(
            anim = R.raw.salon,
            title = "Discover nearby Beauty Havens",
            description = "Unlock the beauty secrets hidden in your neighbourhood!"
        ),
        OnboardingState(
            anim = R.raw.appointment,
            title = "Effortless Appointment Bookings",
            description = "Pick your dream salon, choose your preferred date, and secure your sport in few steps."
        ),
        OnboardingState(
            anim = R.raw.chat,
            title = "Connect and Converse with Salons",
            description = "Realtime chats for effortless beauty consultations."
        )
    )

    val pagerState = rememberPagerState {
        onboardings.size
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text(
                text = "Skip",
                color = primaryPink,
                modifier = Modifier.clickable {
                    onOnboardingFinish(context)
                    navController.navigate(Login.route)
                }
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.wrapContentSize()
        ) { currentPage ->
            Column(
                Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoaderAnimation(
                    modifier = Modifier.size(400.dp),
                    anim = onboardings[currentPage].anim,
                    iterations = LottieConstants.IterateForever
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = onboardings[currentPage].title,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                )
                Text(
                    text = onboardings[currentPage].description,
                    modifier = Modifier.padding(top = 25.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                this@Row.AnimatedVisibility(visible = pagerState.currentPage != 0) {
                    IconButton(
                        onClick = {
                            val prevPage = pagerState.currentPage - 1
                            if (prevPage >= 0) {
                                scope.launch {
                                    pagerState.animateScrollToPage(prevPage)
                                }
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, primaryPink), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryPink
                        )
                    }
                }
            }

            PageIndicator(
                pageCount = onboardings.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .padding(60.dp)
            )
            IconButton(
                onClick = {
                    val nextPage = pagerState.currentPage + 1
                    if (nextPage == onboardings.size) {
                        scope.launch(Dispatchers.Main) {
                            onOnboardingFinish(context)
                            navController.popBackStack()
                            navController.navigate(Login.route)
                        }
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .border(BorderStroke(2.dp, primaryPink), CircleShape)
                    .background(primaryPink)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Back",
                    tint = smokeWhite
                )
            }
        }
    }

}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorDot(
    isSelected: Boolean
) {
    val width =
        animateDpAsState(targetValue = if (isSelected) 17.dp else 13.dp, label = "Indicator width")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(width.value)
            .clip(CircleShape)
            .background(if (isSelected) primaryPink else primaryPink.copy(alpha = 0.5f))
    )

}

data class OnboardingState(
    val anim: Int,
    val title: String,
    val description: String
)

private fun onOnboardingFinish(context: Context) {
    val sharedPreference = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putBoolean("isFinished", true).apply()
}

object Onboarding : Navigation {
    override val route = "onboarding"
    override val titleRes: Int = R.string.onboarding
}