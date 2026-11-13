package com.example.pos_agent.ui.navigation.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object EnterExitTransitions {
    val SlideInFromRight: EnterTransition = slideInHorizontally { it }
    val SlideOutToLeft: ExitTransition = slideOutHorizontally { -it }
    val SlideInFromLeft: EnterTransition = slideInHorizontally { -it }
    val SlideOutToRight: ExitTransition = slideOutHorizontally { it }
}
