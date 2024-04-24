package uk.ac.tees.mad.d3901263.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3901263.R.raw
import uk.ac.tees.mad.d3901263.R.string
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.ui.theme.primaryPink


object Splash : Navigation {
    override val route = "splash"
    override val titleRes: Int = string.app_name
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {

    val animation = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        animation.animateTo(1f, animationSpec = tween(1500))
        delay(2000L)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(240.dp))
            LoaderAnimation(
                modifier = Modifier
                    .size(300.dp)
                    .scale(animation.value),
                anim = raw.beauty
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(3f))
            Text(
                text = "Beauty Appointment",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = primaryPink,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(animation.value)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Beauty at your Fingertips: Schedule, Relax, Glow!",
                fontSize = 18.sp,
                color = primaryPink,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(animation.value)
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int, iterations: Int = 1) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = iterations
    )
}