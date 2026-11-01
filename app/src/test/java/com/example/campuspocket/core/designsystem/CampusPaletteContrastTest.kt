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

    /**
     * v10 A1: cada estado activo debe leerse en claro y oscuro. Texto ≥ 4,5:1.
     * Los pares reproducen los colores reales de los componentes:
     * pestaña, chip, dock, botón primario, botón flotante, tecla Guardar y casilla.
     */
    @Test
    fun `active state text pairs meet AA in both themes`() {
        val light = CampusPalette.Light
        val dark = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("tab activa/Background claro", light.InkContent, light.Background),
                Pair("tab activa/Surface claro", light.InkContent, light.Surface),
                Pair("dock activo/Dock claro", light.InkContent, light.Dock),
                Pair("chip seleccionado/OnLime claro", light.OnLime, light.Lime),
                Pair("botón primario/Accent claro", light.OnLime, light.Accent),
                Pair("botón flotante/Lime claro", light.OnLime, light.Lime),
                Pair("tecla Guardar/Lime claro", light.OnLime, light.Lime),
                Pair("casilla marcada/Selected claro", light.OnSelected, light.Selected),
                Pair("tab activa/Background oscuro", dark.InkContent, dark.Background),
                Pair("tab activa/Surface oscuro", dark.InkContent, dark.Surface),
                Pair("dock activo/Dock oscuro", dark.InkContent, dark.Dock),
                Pair("chip seleccionado/OnLime oscuro", dark.OnLime, dark.Lime),
                Pair("botón primario/Accent oscuro", dark.OnLime, dark.Accent),
                Pair("botón flotante/Lime oscuro", dark.OnLime, dark.Lime),
                Pair("tecla Guardar/Lime oscuro", dark.OnLime, dark.Lime),
                Pair("casilla marcada/Selected oscuro", dark.OnSelected, dark.Selected)
            ),
            4.5f
        )
    }

    /**
     * v10 A1/A3: bordes, subrayados y pistas activas ≥ 3:1 contra su superficie
     * real. El pulgar del interruptor se elige con [ContrastUtil.onColorFor].
     */
    @Test
    fun `active state indicators meet AA in both themes`() {
        val light = CampusPalette.Light
        val dark = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("subrayado pestaña/Background claro", light.InkIndicator, light.Background),
                Pair("borde chip/Background claro", light.InkIndicator, light.Background),
                Pair("borde dock/Dock claro", light.InkIndicator, light.Dock),
                Pair("subrayado dock/Dock claro", light.InkIndicator, light.Dock),
                Pair("borde y sombra FAB/Background claro", light.InkIndicator, light.Background),
                Pair("borde Guardar/Surface claro", light.InkIndicator, light.Surface),
                Pair("pista interruptor on/Background claro", light.InkIndicator, light.Background),
                Pair(
                    "pulgar interruptor on/Pista claro",
                    ContrastUtil.onColorFor(light.InkIndicator),
                    light.InkIndicator
                ),
                Pair("subrayado pestaña/Background oscuro", dark.InkIndicator, dark.Background),
                Pair("borde chip/Background oscuro", dark.InkIndicator, dark.Background),
                Pair("borde dock/Dock oscuro", dark.InkIndicator, dark.Dock),
                Pair("subrayado dock/Dock oscuro", dark.InkIndicator, dark.Dock),
                Pair("borde y sombra FAB/Background oscuro", dark.InkIndicator, dark.Background),
                Pair("borde Guardar/Surface oscuro", dark.InkIndicator, dark.Surface),
                Pair("pista interruptor on/Background oscuro", dark.InkIndicator, dark.Background),
                Pair(
                    "pulgar interruptor on/Pista oscuro",
                    ContrastUtil.onColorFor(dark.InkIndicator),
                    dark.InkIndicator
                )
            ),
            3f
        )
    }

    /** v10 A2: el arco de alerta y el relleno normal deben verse sobre el héroe `Ink`. */
    @Test
    fun `gauge alert and fill meet AA against hero ink in both themes`() {
        val light = CampusPalette.Light
        val dark = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("GaugeAlert/Ink claro", light.GaugeAlert, light.Ink),
                Pair("GaugeFill/Ink claro", light.GaugeFill, light.Ink),
                Pair("GaugeAlert/Ink oscuro", dark.GaugeAlert, dark.Ink),
                Pair("GaugeFill/Ink oscuro", dark.GaugeFill, dark.Ink)
            ),
            3f
        )
    }

    /** v10 C2: bordes de notas pastel y bordes de botones primarios. */
    @Test
    fun `new C2 border tokens meet non text AA in both themes`() {
        val light = CampusPalette.Light
        val dark = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("AccentBorder/Surface claro", light.AccentBorder, light.Surface),
                Pair("NoteLavenderBorder/Surface claro", light.NoteLavenderBorder, light.Surface),
                Pair("NoteLimeSoftBorder/Surface claro", light.NoteLimeSoftBorder, light.Surface),
                Pair("NotePeachBorder/Surface claro", light.NotePeachBorder, light.Surface),
                Pair("NoteSkyBorder/Surface claro", light.NoteSkyBorder, light.Surface),
                Pair("AccentBorder/Surface oscuro", dark.AccentBorder, dark.Surface),
                Pair("NoteLavenderBorder/Surface oscuro", dark.NoteLavenderBorder, dark.Surface),
                Pair("NoteLimeSoftBorder/Surface oscuro", dark.NoteLimeSoftBorder, dark.Surface),
                Pair("NotePeachBorder/Surface oscuro", dark.NotePeachBorder, dark.Surface),
                Pair("NoteSkyBorder/Surface oscuro", dark.NoteSkyBorder, dark.Surface)
            ),
            3f
        )
    }

    /** v10 A1: `OnLime` es el nombre canónico del contenido sobre Lima y Ámbar. */
    @Test
    fun `onLime is readable on lime and accent in both themes`() {
        val light = CampusPalette.Light
        val dark = CampusPalette.Dark
        assertPairs(
            listOf(
                Pair("OnLime/Lime claro", light.OnLime, light.Lime),
                Pair("OnLime/Accent claro", light.OnLime, light.Accent),
                Pair("OnLime/Lime oscuro", dark.OnLime, dark.Lime),
                Pair("OnLime/Accent oscuro", dark.OnLime, dark.Accent)
            ),
            4.5f
        )
    }
}
