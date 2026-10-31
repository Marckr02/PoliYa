package com.example.campuspocket.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.LocalMotion
import com.example.campuspocket.core.designsystem.MotionTokens
import com.example.campuspocket.core.designsystem.extended

/**
 * Interruptor v8 de 45 × 26 dp con área táctil de 48 dp. La pista apagada usa
 * `Track` con borde `ControlLine` para que el control se distinga del fondo; la
 * encendida usa `Ink`. El pulgar y su posición completan el estado, que nunca
 * depende solo del color.
 */
@Composable
fun CampusSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val ext = MaterialTheme.extended
    val motion = LocalMotion.current
    val track by animateColorAsState(
        targetValue = if (checked) ext.ink else ext.track,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "switchTrack"
    )
    val thumb by animateColorAsState(
        targetValue = if (checked) ext.onInk else MaterialTheme.colorScheme.surface,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "switchThumb"
    )
    val offset by animateDpAsState(
        targetValue = if (checked) 19.dp else 0.dp,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "switchOffset"
    )
    Box(
        modifier = modifier
            .size(width = 48.dp, height = 48.dp)
            .semantics { this.contentDescription = contentDescription }
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(45.dp)
                .size(width = 45.dp, height = 26.dp)
                .clip(CircleShape)
                .background(track)
                .border(1.dp, if (checked) ext.inkIndicator else ext.controlLine, CircleShape)
                .padding(3.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offset)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(thumb)
                    .then(
                        if (checked) Modifier else Modifier.border(
                            1.dp,
                            ext.controlLine,
                            CircleShape
                        )
                    )
            )
        }
    }
}
