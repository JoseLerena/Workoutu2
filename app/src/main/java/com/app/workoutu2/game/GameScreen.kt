package com.app.workoutu2.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

// --- Paleta de Colores "Hacker" ---
// Define los colores principales de la interfaz para mantener un estilo coherente.
internal val hackerBlack = Color(0xFF000000) // Fondo principal
internal val hackerGreen = Color(0xFF39FF14) // Color para elementos interactivos y positivos
internal val hackerRed = Color.Red // Color para acciones de borrado o errores

/**
 * Composable principal que gestiona la navegación entre las diferentes pantallas del juego.
 * Observa el estado del juego (gameState) y decide qué pantalla mostrar.
 */
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameState by gameViewModel.gameState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = hackerBlack
    ) {
        if (!gameState.isGameStarted) {
            StartMenuScreen(onStartClick = { gameViewModel.onEvent(GameEvent.StartGame) })
        } else if (gameState.isGameOver) {
            SummaryScreen(gameState = gameState, onRestart = { gameViewModel.onEvent(GameEvent.Restart) })
        } else {
            GamePlayScreen(gameState = gameState, onEvent = gameViewModel::onEvent)
        }
    }
}

/**
 * Muestra la pantalla de inicio con el botón para comenzar el juego.
 */
@Composable
fun StartMenuScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ver:1.0",// VERSIÓN ###############################
            modifier = Modifier.align(Alignment.End),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                color = hackerRed
            ))

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Workoutu",// TITULO
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = hackerGreen
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        KeypadButton(
            text = "INICIAR",
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
            )
        Spacer(modifier = Modifier.weight(1.5f))
    }
}

/**
 * Pantalla principal del juego donde el usuario introduce las respuestas a las operaciones.
 */
@Composable
fun GamePlayScreen(gameState: GameState, onEvent: (GameEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding() // Empuja el contenido hacia arriba para no solaparse con la barra de navegación
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Contenedor para mostrar la operación matemática en la parte superior.
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gameState.operation,
                style = TextStyle(
                    fontSize = 100.sp, // tamaño de letra. 
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Red // Color DE LA OPERACION
                ),
                textAlign = TextAlign.Center
            )
        }

        // Contenedor que muestra la respuesta del usuario y el icono de acierto/error.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .height(100.dp)
                .border(2.dp, Color.White, RoundedCornerShape(4.dp)) // Borde blanco estático
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Anima la entrada de la respuesta del usuario.
                AnimatedContent(
                    targetState = gameState.userAnswer,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn())
                            .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                    }, label = ""
                ) { targetText ->
                    Text(
                        text = targetText,
                        style = TextStyle(
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White // Color blanco
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Anima la aparición del icono de feedback (correcto/incorrecto).
                AnimatedContent(targetState = gameState.lastAnswerState, label = "") { state ->
                    when (state) {
                        AnswerState.CORRECT -> Text("✅", style = MaterialTheme.typography.headlineLarge)
                        AnswerState.INCORRECT -> Text("❌", style = MaterialTheme.typography.headlineLarge)
                        AnswerState.NEUTRAL -> {}
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Muestra el teclado numérico.
        NumericKeypad(onEvent)
    }
}

/**
 * Botón personalizable para el teclado numérico y los menús.
 */
@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() } // Detecta interacciones como la pulsación.
    val isClearButton = text == "◁"

    val baseColor = if(isClearButton) hackerRed else hackerGreen
    val fontWeight = if(isClearButton) FontWeight.Bold else FontWeight.Normal

    Button(
        onClick = onClick,
        modifier = modifier
            .border(2.dp, baseColor, RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = hackerBlack,
            contentColor = baseColor
        ),
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = fontWeight
        )
    }
}

/**
 * Dibuja el teclado numérico en una cuadrícula.
 */
@Composable
fun NumericKeypad(onEvent: (GameEvent) -> Unit) {
    val buttons = listOf(
        "7", "8", "9",
        "4", "5", "6",
        "1", "2", "3",
        "0", "◁" // "◁" representa la acción de borrar
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = buttons,
            // El botón "0" ocupa dos columnas de ancho.
            span = { buttonText ->
                GridItemSpan(if (buttonText == "0") 2 else 1)
            }
        ) { buttonText ->
            KeypadButton(
                text = buttonText,
                onClick = {
                    when (buttonText) {
                        "◁" -> onEvent(GameEvent.Clear)
                        else -> onEvent(GameEvent.NumberInput(buttonText.toInt()))
                    }
                },
                // El aspect ratio se ajusta para que los botones mantengan una proporción adecuada.
                modifier = Modifier.aspectRatio(if (buttonText == "0") 3.1f else 1.5f)
            )
        }
    }
}

/**
 * Muestra la pantalla de resumen al final del juego con las estadísticas.
 */
@Composable
fun SummaryScreen(gameState: GameState, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("JUEGO TERMINADO", style = TextStyle(fontSize = 32.sp, color = hackerGreen, fontFamily = FontFamily.Monospace))
        Spacer(modifier = Modifier.height(24.dp))

        // Muestra estadísticas: aciertos, errores y tiempo total.
        Text("Aciertos: ${gameState.correctCount}", style = TextStyle(fontSize = 20.sp, color = Color.White, fontFamily = FontFamily.Monospace))
        Text("Errores: ${gameState.incorrectCount}", style = TextStyle(fontSize = 20.sp, color = hackerRed, fontFamily = FontFamily.Monospace))

        val totalMillis = gameState.totalTime
        val minutes = (totalMillis / 1000) / 60
        val seconds = (totalMillis / 1000) % 60
        val tenths = (totalMillis % 1000) / 100
        val timeString = String.format(Locale.US, "%02d:%02d.%d", minutes, seconds, tenths)
        Text("Tu Tiempo: $timeString", style = TextStyle(fontSize = 24.sp, color = hackerGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(32.dp))

        // Ranking de mejores tiempos
        Text("TOP 5 TIEMPOS", style = TextStyle(fontSize = 20.sp, color = Color.Yellow, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            itemsIndexed(gameState.topTimes) { index, time ->
                val m = (time / 1000) / 60
                val s = (time / 1000) % 60
                val t = (time % 1000) / 100
                val formatted = String.format(Locale.US, "#%d - %02d:%02d.%d", index + 1, m, s, t)
                Text(
                    text = formatted,
                    style = TextStyle(fontSize = 18.sp, color = Color.White, fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para reiniciar el juego.
        KeypadButton(text = "REINICIAR", onClick = onRestart, modifier = Modifier.fillMaxWidth(0.8f).height(60.dp))
    }
}
