package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppStudioPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = AppStudioBlue,
    contentColor: Color = Color.White,
    testTag: String = "primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun AppStudioOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0xFF1E293B),
    textColor: Color = Color(0xFF0F172A),
    testTag: String = "outlined_button"
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AppStudioInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "input_field"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(InputFieldBg)
            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.padding(end = 12.dp)) {
                    leadingIcon()
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xFF94A3B8),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                androidx.compose.foundation.text.BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(testTag),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions
                )
            }

            if (trailingIcon != null) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    trailingIcon()
                }
            }
        }
    }
}

@Composable
fun OnboardingStepIndicator(
    currentStep: Int,
    totalSteps: Int = 4,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalSteps) {
            val isActive = i <= currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isActive) AppStudioBlue else Color(0xFFE2E8F0))
            )
        }
    }
}

// ----------------- ILLUSTRATIONS -----------------

/**
 * Illustration 1: Three Mobile phone mockups side-by-side (Workout, Dark Analytics, Wallet)
 * Recreating Screenshot 1
 */
@Composable
fun IllustrationThreePhones(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(230.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Left phone (Workout / health)
        Box(
            modifier = Modifier
                .offset(x = (-66).dp, y = 14.dp)
                .width(84.dp)
                .height(154.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E7FF))
                .border(2.dp, Color(0xFF4338CA), RoundedCornerShape(16.dp))
                .padding(6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Circular gauge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color(0xFFFF5722), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF5722),
                        modifier = Modifier.size(16.dp)
                    )
                }
                // Little workout cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier.size(18.dp).background(Color(0xFFFFCCBC), RoundedCornerShape(4.dp)))
                    Box(modifier = Modifier.size(18.dp).background(Color(0xFFC5CAE9), RoundedCornerShape(4.dp)))
                }
                // Mini bar chart
                Row(
                    modifier = Modifier.fillMaxWidth().height(20.dp).background(Color.White, RoundedCornerShape(4.dp)).padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(modifier = Modifier.width(4.dp).height(8.dp).background(Color(0xFF3F51B5)))
                    Box(modifier = Modifier.width(4.dp).height(14.dp).background(Color(0xFF3F51B5)))
                    Box(modifier = Modifier.width(4.dp).height(11.dp).background(Color(0xFF3F51B5)))
                    Box(modifier = Modifier.width(4.dp).height(17.dp).background(Color(0xFF3F51B5)))
                }
            }
        }

        // Center phone (Dark Analytics Dashboard) - Prominent in center
        Box(
            modifier = Modifier
                .width(112.dp)
                .height(190.dp)
                .shadow(16.dp, RoundedCornerShape(22.dp))
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF1E293B))
                .border(3.dp, Color(0xFF0F172A), RoundedCornerShape(22.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top circular dial
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .background(Color(0xFF0F172A))
                        .border(3.dp, Color(0xFFF97316), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("23", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("45.2%", color = Color(0xFF38BDF8), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("234", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                // Line graph
                Canvas(modifier = Modifier.fillMaxWidth().height(24.dp)) {
                    val p = Path().apply {
                        moveTo(0f, size.height * 0.7f)
                        cubicTo(size.width * 0.3f, size.height * 0.2f, size.width * 0.7f, size.height * 0.9f, size.width, size.height * 0.3f)
                    }
                    drawPath(p, Color(0xFFF97316), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                }

                // Vertical bars
                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(modifier = Modifier.width(6.dp).height(14.dp).background(Color(0xFF38BDF8), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.width(6.dp).height(24.dp).background(Color(0xFF38BDF8), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.width(6.dp).height(18.dp).background(Color(0xFF38BDF8), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.width(6.dp).height(28.dp).background(Color(0xFF38BDF8), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.width(6.dp).height(20.dp).background(Color(0xFF38BDF8), RoundedCornerShape(2.dp)))
                }

                // Bottom bar dots
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF38BDF8)))
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                }
            }
        }

        // Right phone (Wallet / Finance Card)
        Box(
            modifier = Modifier
                .offset(x = 66.dp, y = 14.dp)
                .width(84.dp)
                .height(154.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1B4B))
                .border(2.dp, Color(0xFF312E81), RoundedCornerShape(16.dp))
                .padding(6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF4338CA), Color(0xFF6366F1))),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("•••• 4092", color = Color.White, fontSize = 7.sp)
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFF59E0B), CircleShape))
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(6.dp).background(Color(0xFF475569), RoundedCornerShape(3.dp)))
                    Box(modifier = Modifier.fillMaxWidth(0.5f).height(6.dp).background(Color(0xFF475569), RoundedCornerShape(3.dp)))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color(0xFF4F46E5), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+ Add Money", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Illustration 2: Customize your app fully (Admin panel & person)
 * Recreating Screenshot 2
 */
@Composable
fun IllustrationAdminPanel(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(230.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(260.dp, 200.dp)) {
            // Document background
            drawRoundRect(
                color = Color(0xFFF8FAFC),
                topLeft = Offset(20f, 10f),
                size = Size(180f, 220f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawRoundRect(
                color = Color(0xFFCBD5E1),
                topLeft = Offset(20f, 10f),
                size = Size(180f, 220f),
                cornerRadius = CornerRadius(20f, 20f),
                style = Stroke(width = 3.dp.toPx())
            )

            // Purple block on document
            drawRoundRect(
                color = Color(0xFF6B21A8),
                topLeft = Offset(36f, 32f),
                size = Size(28f, 28f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            // Document lines
            drawRoundRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(72f, 36f),
                size = Size(110f, 8f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(72f, 50f),
                size = Size(80f, 8f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Second row with purple square and text lines
            drawRoundRect(
                color = Color(0xFF6B21A8),
                topLeft = Offset(36f, 76f),
                size = Size(28f, 28f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = Color(0xFFCBD5E1),
                topLeft = Offset(72f, 80f),
                size = Size(110f, 6f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = Color(0xFFCBD5E1),
                topLeft = Offset(72f, 92f),
                size = Size(110f, 6f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = Color(0xFFCBD5E1),
                topLeft = Offset(72f, 104f),
                size = Size(60f, 6f),
                cornerRadius = CornerRadius(3f, 3f)
            )

            // Small decorative dots
            drawCircle(Color(0xFF831843), radius = 6f, center = Offset(10f, 18f))
            drawCircle(Color(0xFF831843), radius = 8f, center = Offset(-8f, 96f))

            // Figure beside the document (Developer with blue/purple shirt)
            // Head
            drawCircle(Color(0xFF0F172A), radius = 18f, center = Offset(210f, 66f))
            // Body (purple shirt)
            drawRoundRect(
                color = Color(0xFF4F46E5),
                topLeft = Offset(195f, 86f),
                size = Size(32f, 60f),
                cornerRadius = CornerRadius(8f, 8f)
            )
            // Left arm touching the panel
            val armPath = Path().apply {
                moveTo(200f, 95f)
                lineTo(180f, 105f)
            }
            drawPath(armPath, Color(0xFF4F46E5), style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round))
            // Legs (dark pants)
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(198f, 146f),
                size = Size(11f, 64f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(213f, 146f),
                size = Size(11f, 64f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

/**
 * Illustration 3: Start with templates (Designer & Easel / UI)
 * Recreating Screenshot 5
 */
@Composable
fun IllustrationTemplates(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(230.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(260.dp, 200.dp)) {
            // Ground line
            val ground = Path().apply {
                moveTo(20f, 180f)
                cubicTo(80f, 175f, 160f, 185f, 240f, 180f)
            }
            drawPath(ground, Color(0xFF94A3B8), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

            // Phone screen template on right
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(160f, 20f),
                size = Size(50f, 86f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawRoundRect(
                color = Color(0xFF334155),
                topLeft = Offset(160f, 20f),
                size = Size(50f, 86f),
                cornerRadius = CornerRadius(12f, 12f),
                style = Stroke(width = 2.dp.toPx())
            )
            // Template blocks inside phone
            drawRoundRect(Color(0xFF1E293B), topLeft = Offset(166f, 32f), size = Size(38f, 8f), cornerRadius = CornerRadius(2f, 2f))
            drawRoundRect(Color(0xFFEA580C), topLeft = Offset(166f, 46f), size = Size(38f, 8f), cornerRadius = CornerRadius(2f, 2f))
            drawRoundRect(Color(0xFFF97316), topLeft = Offset(166f, 60f), size = Size(38f, 8f), cornerRadius = CornerRadius(2f, 2f))

            // Large pencil / stylus
            val pencil = Path().apply {
                moveTo(85f, 60f)
                lineTo(105f, 160f)
            }
            drawPath(pencil, Color(0xFFEA580C), style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round))

            // Person sitting / designing
            // Head
            drawCircle(Color(0xFF0F172A), radius = 16f, center = Offset(130f, 40f))
            // Body (Orange shirt)
            drawRoundRect(
                color = Color(0xFFEA580C),
                topLeft = Offset(115f, 58f),
                size = Size(30f, 46f),
                cornerRadius = CornerRadius(8f, 8f)
            )
            // Pants (white/light)
            drawRoundRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(110f, 104f),
                size = Size(40f, 34f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            // Legs extending down
            drawRoundRect(Color(0xFFE2E8F0), topLeft = Offset(132f, 136f), size = Size(10f, 36f), cornerRadius = CornerRadius(3f, 3f))

            // Small gear & creative ball
            drawCircle(Color(0xFF0F172A), radius = 10f, center = Offset(50f, 96f))
            drawCircle(Color.White, radius = 5f, center = Offset(50f, 96f))
            drawCircle(Color(0xFFEA580C), radius = 8f, center = Offset(148f, 168f))
        }
    }
}

/**
 * Illustration 4: Google Play Rocket & Globe
 * Recreating Screenshot 3
 */
@Composable
fun IllustrationPublishPlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(230.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp, 180.dp)) {
            // Globe (blue with green continents)
            drawCircle(
                color = Color(0xFF38BDF8),
                radius = 54f,
                center = Offset(100f, 90f)
            )
            // Continents
            val continent1 = Path().apply {
                moveTo(70f, 75f)
                cubicTo(85f, 60f, 115f, 70f, 110f, 95f)
                cubicTo(105f, 120f, 75f, 110f, 70f, 75f)
            }
            drawPath(continent1, Color(0xFF4ADE80))

            val continent2 = Path().apply {
                moveTo(110f, 85f)
                lineTo(135f, 95f)
                lineTo(125f, 115f)
                close()
            }
            drawPath(continent2, Color(0xFF4ADE80))
        }
    }
}

/**
 * Illustration Notifications Screen: Glowing Lightning Android Mascot
 * Recreating Screenshot 4
 */
@Composable
fun IllustrationGlowingMascot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(110.dp)
            .shadow(24.dp, CircleShape, spotColor = Color(0xFF00E5FF))
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF1E3A8A),
                        Color(0xFF0F172A)
                    )
                )
            )
            .border(2.dp, Color(0xFF00E5FF).copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(70.dp)) {
            // Android Dome
            val dome = Path().apply {
                moveTo(size.width * 0.15f, size.height * 0.55f)
                cubicTo(
                    size.width * 0.15f, size.height * 0.15f,
                    size.width * 0.85f, size.height * 0.15f,
                    size.width * 0.85f, size.height * 0.55f
                )
                close()
            }
            drawPath(
                dome,
                Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF3B82F6)))
            )

            // Antennae
            drawLine(
                color = Color(0xFF00E5FF),
                start = Offset(size.width * 0.32f, size.height * 0.25f),
                end = Offset(size.width * 0.20f, size.height * 0.10f),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF00E5FF),
                start = Offset(size.width * 0.68f, size.height * 0.25f),
                end = Offset(size.width * 0.80f, size.height * 0.10f),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Robot Eyes
            drawCircle(Color.White, radius = 3.dp.toPx(), center = Offset(size.width * 0.36f, size.height * 0.42f))
            drawCircle(Color.White, radius = 3.dp.toPx(), center = Offset(size.width * 0.64f, size.height * 0.42f))

            // Glowing Electric Bolt superimposed
            val bolt = Path().apply {
                moveTo(size.width * 0.55f, size.height * 0.32f)
                lineTo(size.width * 0.38f, size.height * 0.62f)
                lineTo(size.width * 0.52f, size.height * 0.62f)
                lineTo(size.width * 0.45f, size.height * 0.92f)
                lineTo(size.width * 0.70f, size.height * 0.54f)
                lineTo(size.width * 0.55f, size.height * 0.54f)
                close()
            }
            drawPath(
                bolt,
                Brush.linearGradient(
                    listOf(Color(0xFFFFD600), Color(0xFF00E5FF), Color(0xFFA855F7))
                )
            )
        }
    }
}

/**
 * Illustration Auth Screen: Person accessing app studio admin panel
 * Recreating Screenshot 6 & 7
 */
@Composable
fun IllustrationAuthHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(110.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(140.dp, 90.dp)) {
            // Desk / Base
            drawRoundRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(10f, 68f),
                size = Size(120f, 6f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            // Giant Profile / Dashboard Card
            drawRoundRect(
                color = Color(0xFF38BDF8),
                topLeft = Offset(24f, 15f),
                size = Size(60f, 54f),
                cornerRadius = CornerRadius(8f, 8f)
            )
            // Profile icon in card
            drawCircle(Color.White, radius = 8f, center = Offset(54f, 32f))
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(40f, 44f),
                size = Size(28f, 14f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Person standing next to dashboard
            drawCircle(Color(0xFF0F172A), radius = 10f, center = Offset(96f, 26f))
            drawRoundRect(
                color = Color(0xFF2563EB),
                topLeft = Offset(88f, 38f),
                size = Size(16f, 30f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}
