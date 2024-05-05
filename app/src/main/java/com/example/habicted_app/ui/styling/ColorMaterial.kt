package com.example.habicted_app.ui.styling

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils


data class ColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color,
) {
    companion object {
        fun colorToPalette(color: Color): ColorPalette {
            return ColorPalette(
                primary = color,
                onPrimary = calculateOnPrimary(color),
                container = calculateContainer(color),
                onContainer = calculateOnContainer(color),
            )
        }

        private fun calculateContainer(color: Color): Color {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(color.toArgb(), hsl)
            hsl[2] = 0.9f
            return Color(ColorUtils.HSLToColor(hsl))
        }

        private fun calculateOnContainer(color: Color): Color {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(color.toArgb(), hsl)
            hsl[2] = 0.1f
            return Color(ColorUtils.HSLToColor(hsl))
        }

        private fun calculateOnPrimary(color: Color): Color {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(color.toArgb(), hsl)
            hsl[2] = 0.1f
            return Color(ColorUtils.HSLToColor(hsl))
        }
    }

}


