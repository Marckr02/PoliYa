package com.example.campuspocket.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Paleta primitiva de CampusPocket v8 (rediseño editorial).
 *
 * - Tinta verde profunda: héroes, dock, texto y selección.
 * - Lima: selección y resaltados; nunca es el único indicador de estado.
 * - Ámbar: relleno de la acción principal.
 * - Papel cálido: fondo y superficies con bordes finos en lugar de sombras.
 *
 * Los colores de datos (materias/cuentas) viven en ColorPalette.kt y no cambian
 * con el tema ni con los datos ya guardados.
 */
internal object CampusPalette {

    object Light {
        val Background = Color(0xFFF2F0E9)
        val Surface = Color(0xFFFCFBF7)
        val SurfaceSecondary = Color(0xFFE9E7DF)
        val OnBackground = Color(0xFF152A29)
        val OnSurface = Color(0xFF152A29)
        val TextSecondary = Color(0xFF56635F)
        val Primary = Color(0xFF103C3B)
        val OnPrimary = Color(0xFFFFFFFF)
        val PrimaryContainer = Color(0xFFE8EFC7)
        val OnPrimaryContainer = Color(0xFF152A29)
        val Sage = Color(0xFFDCEBDD)
        val OnSage = Color(0xFF14532D)
        val Warm = Color(0xFFF3DED0)
        val OnWarm = Color(0xFF7A2E0E)
        // "Line" de la especificación: solo divisores y bordes decorativos.
        val Border = Color(0xFFAEB6B2)
        val OutlineVariant = Color(0xFFAEB6B2)
        val Success = Color(0xFF17663F)
        val Warning = Color(0xFF8A4B12)
        val Error = Color(0xFFA93A3D)
        val OnError = Color(0xFFFFFFFF)
        val ErrorContainer = Color(0xFFF6DCDD)
        val OnErrorContainer = Color(0xFF5F1215)
        val WarningContainer = Color(0xFFF3DED0)
        val SuccessContainer = Color(0xFFDCEBDD)
        val HeroContainer = Color(0xFF103C3B)
        val OnHeroContainer = Color(0xFFFFFFFF)
        val SurfaceDim = Color(0xFFE9E7DF)
        val SurfaceBright = Color(0xFFFCFBF7)
        val SurfaceContainerLowest = Color(0xFFFCFBF7)
        val SurfaceContainerLow = Color(0xFFF7F5EF)
        val SurfaceContainer = Color(0xFFFCFBF7)
        val SurfaceContainerHigh = Color(0xFFEFEDE5)
        val SurfaceContainerHighest = Color(0xFFE9E7DF)
        val InverseSurface = Color(0xFF103C3B)
        val InverseOnSurface = Color(0xFFFFFFFF)
        val InversePrimary = Color(0xFFDFF36D)

        // Tokens extendidos.
        val Ink = Color(0xFF103C3B)
        val OnInk = Color(0xFFFFFFFF)
        val OnInkMuted = Color(0xFFC3D6D1)
        val InkMuted = Color(0xFF56635F)
        val InkSoft = Color(0xFF1C5551)
        val InkLine = Color(0xFF315756)
        val Lime = Color(0xFFDFF36D)
        // v10 A1: contenido sobre Lima o Ámbar. Comparte valor con OnAccent
        // (#103C3B en ambos temas); onLime es el nombre canónico del par.
        val OnLime = Color(0xFF103C3B)
        val Accent = Color(0xFFF1C75B)
        val OnAccent = Color(0xFF103C3B)
        val AccentMuted = Color(0xFFF7E4AD)
        val OnAccentMuted = Color(0xFF3A2600)
        val Selected = Color(0xFF103C3B)
        val OnSelected = Color(0xFFFFFFFF)
        val Danger = Color(0xFFA93A3D)
        val OnDanger = Color(0xFFFFFFFF)
        val DangerSoft = Color(0xFFF6DCDD)
        val OnDangerSoft = Color(0xFF5F1215)
        val Track = Color(0xFFE9E7DF)
        val OnTrack = Color(0xFF152A29)
        val Lavender = Color(0xFFE9E1F4)
        val OnLavender = Color(0xFF152A29)
        val Peach = Color(0xFFF3DED0)
        val OnPeach = Color(0xFF152A29)
        val Mint = Color(0xFFDCEBDD)
        val OnMint = Color(0xFF152A29)
        val Sky = Color(0xFFDCE9EB)
        val OnSky = Color(0xFF152A29)
        val LimeSoft = Color(0xFFE8EFC7)
        val OnLimeSoft = Color(0xFF152A29)
        val Violet = Color(0xFFE9E2FF)
        val OnViolet = Color(0xFF3B2A7A)
        val Dock = Color(0xFFFCFBF7)
        val OnDock = Color(0xFF103C3B)
        val DockInactive = Color(0xFF56635F)
        val Cream = Color(0xFFFFFAF0)
        val OnCream = Color(0xFF103C3B)
        val ControlLine = Color(0xFF7D8984)
        val GaugeFill = Color(0xFFDCEBDD)
        // v10 A2: relleno dedicado del arco en alerta; no reutilizar DangerSoft
        // (contenedor pálido de alertas, casi invisible sobre Ink en oscuro).
        val GaugeAlert = Color(0xFFFF969B)
        val Scrim = Color(0x9E042229)
        // v9 1.1: color de contenido sobre Ink y de indicador activo
        val InkContent = Color(0xFF103C3B)
        val InkIndicator = Color(0xFF103C3B)
        // v9 3.x: papel de notas (NoteEditorScreen)
        val NotePaperBg = Color(0xFFFFFAF0)
        val NotePaperBorder = Color(0xFFE1D6BC)
        val NotePaperLine = Color(0xFF103C3B)
        val NotePaperText = Color(0xFF243B3F)

        // v10 C2: bordes y acentos adicionales
        val AccentBorder = Color(0xFFD9A92D)
        val NoteLavenderBorder = Color(0xFF8D79AD)
        val NoteLimeSoftBorder = Color(0xFF9AAB3E)
        val NotePeachBorder = Color(0xFFBF7D55)
        val NoteSkyBorder = Color(0xFF6692A0)
    }

    object Dark {
        val Background = Color(0xFF071817)
        val Surface = Color(0xFF102827)
        val SurfaceSecondary = Color(0xFF193330)
        val OnBackground = Color(0xFFF4F7EE)
        val OnSurface = Color(0xFFF4F7EE)
        val TextSecondary = Color(0xFFADBFBA)
        val Primary = Color(0xFFDFF36D)
        val OnPrimary = Color(0xFF103C3B)
        val PrimaryContainer = Color(0xFF394525)
        val OnPrimaryContainer = Color(0xFFF4F7EE)
        val Sage = Color(0xFF244438)
        val OnSage = Color(0xFF8AD5A7)
        val Warm = Color(0xFF4A352D)
        val OnWarm = Color(0xFFFFD9C2)
        val Border = Color(0xFF3A5350)
        val OutlineVariant = Color(0xFF3A5350)
        val Success = Color(0xFF7EE2A8)
        val Warning = Color(0xFFFFC46B)
        val Error = Color(0xFFFF969B)
        val OnError = Color(0xFF490C12)
        val ErrorContainer = Color(0xFF5C1A1F)
        val OnErrorContainer = Color(0xFFFFDAD9)
        val WarningContainer = Color(0xFF4A352D)
        val SuccessContainer = Color(0xFF244438)
        val HeroContainer = Color(0xFF153F3C)
        val OnHeroContainer = Color(0xFFF4F7EE)
        val SurfaceDim = Color(0xFF071817)
        val SurfaceBright = Color(0xFF1A3A37)
        val SurfaceContainerLowest = Color(0xFF071817)
        val SurfaceContainerLow = Color(0xFF0B201F)
        val SurfaceContainer = Color(0xFF102827)
        val SurfaceContainerHigh = Color(0xFF14302E)
        val SurfaceContainerHighest = Color(0xFF193330)
        val InverseSurface = Color(0xFFF4F7EE)
        val InverseOnSurface = Color(0xFF071817)
        val InversePrimary = Color(0xFF103C3B)

        // Tokens extendidos.
        val Ink = Color(0xFF153F3C)
        val OnInk = Color(0xFFF4F7EE)
        val OnInkMuted = Color(0xFFC3D6D1)
        val InkMuted = Color(0xFFADBFBA)
        val InkSoft = Color(0xFF20534F)
        val InkLine = Color(0xFF365A57)
        val Lime = Color(0xFFDFF36D)
        val OnLime = Color(0xFF103C3B)
        val Accent = Color(0xFFF1C75B)
        val OnAccent = Color(0xFF103C3B)
        val AccentMuted = Color(0xFF4A3B17)
        val OnAccentMuted = Color(0xFFF7E4AD)
        val Selected = Color(0xFFDFF36D)
        val OnSelected = Color(0xFF103C3B)
        val Danger = Color(0xFFFF969B)
        val OnDanger = Color(0xFF490C12)
        val DangerSoft = Color(0xFF5C2A2D)
        val OnDangerSoft = Color(0xFFFFDAD9)
        val Track = Color(0xFF193330)
        val OnTrack = Color(0xFFF4F7EE)
        val Lavender = Color(0xFF3B334B)
        val OnLavender = Color(0xFFF4F7EE)
        val Peach = Color(0xFF4A352D)
        val OnPeach = Color(0xFFF4F7EE)
        val Mint = Color(0xFF244438)
        val OnMint = Color(0xFFF4F7EE)
        val Sky = Color(0xFF243F45)
        val OnSky = Color(0xFFF4F7EE)
        val LimeSoft = Color(0xFF394525)
        val OnLimeSoft = Color(0xFFF4F7EE)
        val Violet = Color(0xFF2B2347)
        val OnViolet = Color(0xFFDCD2FF)
        val Dock = Color(0xFF102827)
        val OnDock = Color(0xFFF4F7EE)
        val DockInactive = Color(0xFFADBFBA)
        val Cream = Color(0xFF24231D)
        val OnCream = Color(0xFFF0F2E8)
        val ControlLine = Color(0xFF718782)
        val GaugeFill = Color(0xFF83DBA6)
        // v10 A2: #FF969B en ambos temas para que el arco de alerta se vea sobre Ink.
        val GaugeAlert = Color(0xFFFF969B)
        val Scrim = Color(0x9E042229)
        // v9 1.1: color de contenido sobre Ink y de indicador activo
        val InkContent = Color(0xFFF4F7EE)
        val InkIndicator = Color(0xFFDFF36D)
        // v9 3.x: papel de notas (NoteEditorScreen)
        val NotePaperBg = Color(0xFF24231D)
        val NotePaperBorder = Color(0xFF4A4739)
        val NotePaperLine = Color(0xFFDFF36D)
        val NotePaperText = Color(0xFFF1EAD5)

        // v10 C2: bordes y acentos adicionales
        val AccentBorder = Color(0xFFD9A92D)
        val NoteLavenderBorder = Color(0xFFA592C4)
        val NoteLimeSoftBorder = Color(0xFFADBF55)
        val NotePeachBorder = Color(0xFFD4936A)
        val NoteSkyBorder = Color(0xFF7CA8B6)
    }
}

/**
 * Tokens semánticos que Material 3 no cubre de serie: éxito, advertencia,
 * acento ámbar, selección, barra flotante y tintes pastel.
 */
@Immutable
data class CampusExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val warningContainer: Color,
    val successContainer: Color,
    val sage: Color,
    val onSage: Color,
    val warm: Color,
    val onWarm: Color,
    val heroContainer: Color,
    val onHeroContainer: Color,
    val ink: Color,
    val onInk: Color,
    val onInkMuted: Color,
    val inkMuted: Color,
    val inkSoft: Color,
    val inkLine: Color,
    val lime: Color,
    val onLime: Color,
    val accent: Color,
    val onAccent: Color,
    val accentMuted: Color,
    val onAccentMuted: Color,
    val selected: Color,
    val onSelected: Color,
    val danger: Color,
    val onDanger: Color,
    val dangerSoft: Color,
    val onDangerSoft: Color,
    val track: Color,
    val onTrack: Color,
    val lavender: Color,
    val onLavender: Color,
    val peach: Color,
    val onPeach: Color,
    val mint: Color,
    val onMint: Color,
    val sky: Color,
    val onSky: Color,
    val limeSoft: Color,
    val onLimeSoft: Color,
    val violet: Color,
    val onViolet: Color,
    val dock: Color,
    val onDock: Color,
    val dockInactive: Color,
    val cream: Color,
    val onCream: Color,
    val controlLine: Color,
    val gaugeFill: Color,
    val gaugeAlert: Color,
    val scrim: Color,
    // v9 1.1
    val inkContent: Color,
    val inkIndicator: Color,
    // v9 3.x: papel de notas
    val notePaperBg: Color,
    val notePaperBorder: Color,
    val notePaperLine: Color,
    val notePaperText: Color,
    // v10 C2 & C4
    val isDark: Boolean,
    val accentBorder: Color,
    val noteLavenderBorder: Color,
    val noteLimeSoftBorder: Color,
    val notePeachBorder: Color,
    val noteSkyBorder: Color
)

internal val LightExtendedColors = CampusExtendedColors(
    success = CampusPalette.Light.Success,
    onSuccess = CampusPalette.Light.OnPrimary,
    warning = CampusPalette.Light.Warning,
    onWarning = CampusPalette.Light.OnPrimary,
    errorContainer = CampusPalette.Light.ErrorContainer,
    onErrorContainer = CampusPalette.Light.OnErrorContainer,
    warningContainer = CampusPalette.Light.WarningContainer,
    successContainer = CampusPalette.Light.SuccessContainer,
    sage = CampusPalette.Light.Sage,
    onSage = CampusPalette.Light.OnSage,
    warm = CampusPalette.Light.Warm,
    onWarm = CampusPalette.Light.OnWarm,
    heroContainer = CampusPalette.Light.HeroContainer,
    onHeroContainer = CampusPalette.Light.OnHeroContainer,
    ink = CampusPalette.Light.Ink,
    onInk = CampusPalette.Light.OnInk,
    onInkMuted = CampusPalette.Light.OnInkMuted,
    inkMuted = CampusPalette.Light.InkMuted,
    inkSoft = CampusPalette.Light.InkSoft,
    inkLine = CampusPalette.Light.InkLine,
    lime = CampusPalette.Light.Lime,
    onLime = CampusPalette.Light.OnLime,
    accent = CampusPalette.Light.Accent,
    onAccent = CampusPalette.Light.OnAccent,
    accentMuted = CampusPalette.Light.AccentMuted,
    onAccentMuted = CampusPalette.Light.OnAccentMuted,
    selected = CampusPalette.Light.Selected,
    onSelected = CampusPalette.Light.OnSelected,
    danger = CampusPalette.Light.Danger,
    onDanger = CampusPalette.Light.OnDanger,
    dangerSoft = CampusPalette.Light.DangerSoft,
    onDangerSoft = CampusPalette.Light.OnDangerSoft,
    track = CampusPalette.Light.Track,
    onTrack = CampusPalette.Light.OnTrack,
    lavender = CampusPalette.Light.Lavender,
    onLavender = CampusPalette.Light.OnLavender,
    peach = CampusPalette.Light.Peach,
    onPeach = CampusPalette.Light.OnPeach,
    mint = CampusPalette.Light.Mint,
    onMint = CampusPalette.Light.OnMint,
    sky = CampusPalette.Light.Sky,
    onSky = CampusPalette.Light.OnSky,
    limeSoft = CampusPalette.Light.LimeSoft,
    onLimeSoft = CampusPalette.Light.OnLimeSoft,
    violet = CampusPalette.Light.Violet,
    onViolet = CampusPalette.Light.OnViolet,
    dock = CampusPalette.Light.Dock,
    onDock = CampusPalette.Light.OnDock,
    dockInactive = CampusPalette.Light.DockInactive,
    cream = CampusPalette.Light.Cream,
    onCream = CampusPalette.Light.OnCream,
    controlLine = CampusPalette.Light.ControlLine,
    gaugeFill = CampusPalette.Light.GaugeFill,
    gaugeAlert = CampusPalette.Light.GaugeAlert,
    scrim = CampusPalette.Light.Scrim,
    // v9 1.1
    inkContent = CampusPalette.Light.InkContent,
    inkIndicator = CampusPalette.Light.InkIndicator,
    // v9 3.x: papel de notas
    notePaperBg = CampusPalette.Light.NotePaperBg,
    notePaperBorder = CampusPalette.Light.NotePaperBorder,
    notePaperLine = CampusPalette.Light.NotePaperLine,
    notePaperText = CampusPalette.Light.NotePaperText,
    // v10 C2 & C4
    isDark = false,
    accentBorder = CampusPalette.Light.AccentBorder,
    noteLavenderBorder = CampusPalette.Light.NoteLavenderBorder,
    noteLimeSoftBorder = CampusPalette.Light.NoteLimeSoftBorder,
    notePeachBorder = CampusPalette.Light.NotePeachBorder,
    noteSkyBorder = CampusPalette.Light.NoteSkyBorder
)

internal val DarkExtendedColors = CampusExtendedColors(
    success = CampusPalette.Dark.Success,
    onSuccess = CampusPalette.Dark.OnPrimary,
    warning = CampusPalette.Dark.Warning,
    onWarning = CampusPalette.Dark.OnPrimary,
    errorContainer = CampusPalette.Dark.ErrorContainer,
    onErrorContainer = CampusPalette.Dark.OnErrorContainer,
    warningContainer = CampusPalette.Dark.WarningContainer,
    successContainer = CampusPalette.Dark.SuccessContainer,
    sage = CampusPalette.Dark.Sage,
    onSage = CampusPalette.Dark.OnSage,
    warm = CampusPalette.Dark.Warm,
    onWarm = CampusPalette.Dark.OnWarm,
    heroContainer = CampusPalette.Dark.HeroContainer,
    onHeroContainer = CampusPalette.Dark.OnHeroContainer,
    ink = CampusPalette.Dark.Ink,
    onInk = CampusPalette.Dark.OnInk,
    onInkMuted = CampusPalette.Dark.OnInkMuted,
    inkMuted = CampusPalette.Dark.InkMuted,
    inkSoft = CampusPalette.Dark.InkSoft,
    inkLine = CampusPalette.Dark.InkLine,
    lime = CampusPalette.Dark.Lime,
    onLime = CampusPalette.Dark.OnLime,
    accent = CampusPalette.Dark.Accent,
    onAccent = CampusPalette.Dark.OnAccent,
    accentMuted = CampusPalette.Dark.AccentMuted,
    onAccentMuted = CampusPalette.Dark.OnAccentMuted,
    selected = CampusPalette.Dark.Selected,
    onSelected = CampusPalette.Dark.OnSelected,
    danger = CampusPalette.Dark.Danger,
    onDanger = CampusPalette.Dark.OnDanger,
    dangerSoft = CampusPalette.Dark.DangerSoft,
    onDangerSoft = CampusPalette.Dark.OnDangerSoft,
    track = CampusPalette.Dark.Track,
    onTrack = CampusPalette.Dark.OnTrack,
    lavender = CampusPalette.Dark.Lavender,
    onLavender = CampusPalette.Dark.OnLavender,
    peach = CampusPalette.Dark.Peach,
    onPeach = CampusPalette.Dark.OnPeach,
    mint = CampusPalette.Dark.Mint,
    onMint = CampusPalette.Dark.OnMint,
    sky = CampusPalette.Dark.Sky,
    onSky = CampusPalette.Dark.OnSky,
    limeSoft = CampusPalette.Dark.LimeSoft,
    onLimeSoft = CampusPalette.Dark.OnLimeSoft,
    violet = CampusPalette.Dark.Violet,
    onViolet = CampusPalette.Dark.OnViolet,
    dock = CampusPalette.Dark.Dock,
    onDock = CampusPalette.Dark.OnDock,
    dockInactive = CampusPalette.Dark.DockInactive,
    cream = CampusPalette.Dark.Cream,
    onCream = CampusPalette.Dark.OnCream,
    controlLine = CampusPalette.Dark.ControlLine,
    gaugeFill = CampusPalette.Dark.GaugeFill,
    gaugeAlert = CampusPalette.Dark.GaugeAlert,
    scrim = CampusPalette.Dark.Scrim,
    // v9 1.1
    inkContent = CampusPalette.Dark.InkContent,
    inkIndicator = CampusPalette.Dark.InkIndicator,
    // v9 3.x: papel de notas
    notePaperBg = CampusPalette.Dark.NotePaperBg,
    notePaperBorder = CampusPalette.Dark.NotePaperBorder,
    notePaperLine = CampusPalette.Dark.NotePaperLine,
    notePaperText = CampusPalette.Dark.NotePaperText,
    // v10 C2 & C4
    isDark = true,
    accentBorder = CampusPalette.Dark.AccentBorder,
    noteLavenderBorder = CampusPalette.Dark.NoteLavenderBorder,
    noteLimeSoftBorder = CampusPalette.Dark.NoteLimeSoftBorder,
    notePeachBorder = CampusPalette.Dark.NotePeachBorder,
    noteSkyBorder = CampusPalette.Dark.NoteSkyBorder
)

internal val LocalCampusExtendedColors = compositionLocalOf { LightExtendedColors }

/** Acceso ergonómico a los tokens semánticos extendidos del tema actual. */
val MaterialTheme.extended: CampusExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCampusExtendedColors.current
