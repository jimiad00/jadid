package com.example.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.data.model.Character
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.NeonPurple
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    character1: Character,
    character2: Character,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Strength", "Speed", "Durability", "Intellect", "Experience")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f)) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = (size.minDimension / 2f) * 0.72f
            val totalAxes = labels.size

            // 1. Draw 4 concentric pentagon grid levels
            for (level in 1..4) {
                val r = (radius / 4f) * level
                val gridPath = Path()
                for (i in 0 until totalAxes) {
                    val angle = (Math.PI * 2 / totalAxes) * i - Math.PI / 2
                    val x = centerX + (r * cos(angle)).toFloat()
                    val y = centerY + (r * sin(angle)).toFloat()
                    if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                }
                gridPath.close()
                drawPath(
                    path = gridPath,
                    color = NeonPurple.copy(alpha = 0.18f),
                    style = Stroke(width = 1.5f)
                )
            }

            // 2. Draw axis lines and vertex labels
            val textPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#CBD5E1")
                textSize = 28f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
                isFakeBoldText = true
            }

            for (i in 0 until totalAxes) {
                val angle = (Math.PI * 2 / totalAxes) * i - Math.PI / 2
                val lineX = centerX + (radius * cos(angle)).toFloat()
                val lineY = centerY + (radius * sin(angle)).toFloat()

                // Radial axis line
                drawLine(
                    color = DeepViolet.copy(alpha = 0.35f),
                    start = Offset(centerX, centerY),
                    end = Offset(lineX, lineY),
                    strokeWidth = 1.5f
                )

                // Label
                val labelDistance = radius + 36f
                val labelX = centerX + (labelDistance * cos(angle)).toFloat()
                val labelY = centerY + (labelDistance * sin(angle)).toFloat() + 10f

                drawContext.canvas.nativeCanvas.drawText(
                    labels[i],
                    labelX,
                    labelY,
                    textPaint
                )
            }

            // 3. Draw Character 1 Polygon (Deep Violet)
            val values1 = listOf(
                character1.strength,
                character1.speed,
                character1.durability,
                character1.intelligence,
                character1.experience
            )
            drawStatPolygon(
                values = values1,
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                strokeColor = DeepViolet,
                fillColor = DeepViolet.copy(alpha = 0.28f)
            )

            // 4. Draw Character 2 Polygon (Neon Purple)
            val values2 = listOf(
                character2.strength,
                character2.speed,
                character2.durability,
                character2.intelligence,
                character2.experience
            )
            drawStatPolygon(
                values = values2,
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                strokeColor = NeonPurple,
                fillColor = NeonPurple.copy(alpha = 0.32f)
            )
        }
    }
}

private fun DrawScope.drawStatPolygon(
    values: List<Int>,
    centerX: Float,
    centerY: Float,
    radius: Float,
    strokeColor: Color,
    fillColor: Color
) {
    val totalAxes = values.size
    val path = Path()

    for (i in 0 until totalAxes) {
        val angle = (Math.PI * 2 / totalAxes) * i - Math.PI / 2
        val normalized = (values[i] / 100f).coerceIn(0.1f, 1.0f) * radius
        val x = centerX + (normalized * cos(angle)).toFloat()
        val y = centerY + (normalized * sin(angle)).toFloat()

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    // Fill
    drawPath(path = path, color = fillColor, style = Fill)
    // Stroke
    drawPath(path = path, color = strokeColor, style = Stroke(width = 3.5f))

    // Vertex points
    for (i in 0 until totalAxes) {
        val angle = (Math.PI * 2 / totalAxes) * i - Math.PI / 2
        val normalized = (values[i] / 100f).coerceIn(0.1f, 1.0f) * radius
        val x = centerX + (normalized * cos(angle)).toFloat()
        val y = centerY + (normalized * sin(angle)).toFloat()
        drawCircle(color = strokeColor, radius = 5f, center = Offset(x, y))
        drawCircle(color = Color.White, radius = 2.5f, center = Offset(x, y))
    }
}
