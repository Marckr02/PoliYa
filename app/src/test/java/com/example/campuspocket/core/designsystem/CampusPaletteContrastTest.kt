package com.example.campuspocket.core.designsystem

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifica los pares de texto y controles definidos en la especificación v8:
 * texto normal ≥ 4,5:1 y elementos de interfaz ≥ 3:1.
 */
class CampusPaletteContrastTest {

    private data class Pair(val label: String, val foreground: Color, val background: Color)

    private fun assertPairs(pairs: List<Pair>, minimum: Float) {
        pairs.forEach { pair ->
            val ratio = ContrastUtil.contrastRatio(pair.foreground, pair.background)
            assertTrue(
                "${pair.label}: $ratio < $minimum",
                ratio >= minimum
            )
        }
    }

    @Test
    fun `light text pairs meet AA`() {
        val p = CampusPalette.Light
        assertPairs(
            listOf(
                Pair("OnBackground/Background", p.OnBackground, p.Background),
                Pair("OnSurface/Surface", p.OnSurface, p.Surface),
                Pair("InkMuted/Background", p.InkMuted, p.Background),
                Pair("InkMuted/Surface", p.InkMuted, p.Surface),
                Pair("OnInk/Ink", p.OnInk, p.Ink),
                Pair("OnInkMuted/Ink", p.OnInkMuted, p.Ink),
                Pair("OnLime/Lime", p.OnLime, p.Lime),
                Pair("OnAccent/Accent", p.OnAccent, p.Accent),
                Pair("OnAccentMuted/AccentMuted", p.OnAccentMuted, p.AccentMuted),
                Pair("OnTrack/Track", p.OnTrack, p.Track),
                Pair("OnLavender/Lavender", p.OnLavender, p.Lavender),
                Pair("OnPeach/Peach", p.OnPeach, p.Peach),
                Pair("OnMint/Mint", p.OnMint, p.Mint),
                Pair("OnSky/Sky", p.OnSky, p.Sky),
                Pair("OnLimeSoft/LimeSoft", p.OnLimeSoft, p.LimeSoft),
                Pair("OnDanger/Danger", p.OnDanger, p.Danger),
                Pair("OnDangerSoft/DangerSoft", p.OnDangerSoft, p.DangerSoft),
                Pair("OnError/Error", p.OnError, p.Error),
                Pair("OnErrorContainer/ErrorContainer", p.OnErrorContainer, p.ErrorContainer),
                Pair("OnCream/Cream", p.OnCream, p.Cream),
                Pair("OnDock/Dock", p.OnDock, p.Dock),
                // v9 1.1: new tokens
                Pair("InkContent/Background", p.InkContent, p.Background),
                Pair("InkContent/Surface", p.InkContent, p.Surface),
                Pair("InkIndicator/Background", p.InkIndicator, p.Background),
                Pair("InkIndicator/Surface", p.InkIndicator, p.Surface)
            ),
            4.5f
        )
    }

    @Test
    fun `dark text pairs meet AA`() {
        val p = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("OnBackground/Background", p.OnBackground, p.Background),
                Pair("OnSurface/Surface", p.OnSurface, p.Surface),
                Pair("InkMuted/Background", p.InkMuted, p.Background),
                Pair("InkMuted/Surface", p.InkMuted, p.Surface),
                Pair("OnInk/Ink", p.OnInk, p.Ink),
                Pair("OnInkMuted/Ink", p.OnInkMuted, p.Ink),
                Pair("OnLime/Lime", p.OnLime, p.Lime),
                Pair("OnAccent/Accent", p.OnAccent, p.Accent),
                Pair("OnAccentMuted/AccentMuted", p.OnAccentMuted, p.AccentMuted),
                Pair("OnTrack/Track", p.OnTrack, p.Track),
                Pair("OnLavender/Lavender", p.OnLavender, p.Lavender),
                Pair("OnPeach/Peach", p.OnPeach, p.Peach),
                Pair("OnMint/Mint", p.OnMint, p.Mint),
                Pair("OnSky/Sky", p.OnSky, p.Sky),
                Pair("OnLimeSoft/LimeSoft", p.OnLimeSoft, p.LimeSoft),
                Pair("OnDanger/Danger", p.OnDanger, p.Danger),
                Pair("OnDangerSoft/DangerSoft", p.OnDangerSoft, p.DangerSoft),
                Pair("OnError/Error", p.OnError, p.Error),
                Pair("OnErrorContainer/ErrorContainer", p.OnErrorContainer, p.ErrorContainer),
                Pair("OnCream/Cream", p.OnCream, p.Cream),
                Pair("OnDock/Dock", p.OnDock, p.Dock),
                // v9 1.1: new tokens
                Pair("InkContent/Background", p.InkContent, p.Background),
                Pair("InkContent/Surface", p.InkContent, p.Surface),
                Pair("InkIndicator/Background", p.InkIndicator, p.Background),
                Pair("InkIndicator/Surface", p.InkIndicator, p.Surface)
            ),
            4.5f
        )
    }

    @Test
    fun `control borders meet AA for non text`() {
        assertPairs(
            listOf(
                Pair(
                    "ControlLine/Background",
                    CampusPalette.Light.ControlLine,
                    CampusPalette.Light.Background
                ),
                Pair(
                    "ControlLine/Surface",
                    CampusPalette.Light.ControlLine,
                    CampusPalette.Light.Surface
                ),
                Pair(
                    "ControlLine/Background dark",
                    CampusPalette.Dark.ControlLine,
                    CampusPalette.Dark.Background
                ),
                Pair(
                    "ControlLine/Surface dark",
                    CampusPalette.Dark.ControlLine,
                    CampusPalette.Dark.Surface
                ),
                // v9 1.1: indicator borders
                Pair(
                    "InkIndicator/Background",
                    CampusPalette.Light.InkIndicator,
                    CampusPalette.Light.Background
                ),
                Pair(
                    "InkIndicator/Surface",
                    CampusPalette.Light.InkIndicator,
                    CampusPalette.Light.Surface
                ),
                Pair(
                    "InkIndicator/Background dark",
                    CampusPalette.Dark.InkIndicator,
                    CampusPalette.Dark.Background
                ),
                Pair(
                    "InkIndicator/Surface dark",
                    CampusPalette.Dark.InkIndicator,
                    CampusPalette.Dark.Surface
                )
            ),
            3f
        )
    }
}
