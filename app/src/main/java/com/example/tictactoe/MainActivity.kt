package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    var showWelcomeScreen by remember { mutableStateOf(true) }
    var playerXWins by remember { mutableStateOf(0) }
    var playerOWins by remember { mutableStateOf(0) }
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    if (showWelcomeScreen) {
        WelcomeScreen(onStartGame = { showWelcomeScreen = false })
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Player X: $playerXWins   |   Player O: $playerOWins",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF37474F)
            )

            when {
                winner != null -> {
                    if (!isGameOver) {
                        if (winner == "X") playerXWins++ else playerOWins++
                        isGameOver = true
                    }
                    Text(
                        text = "Player $winner wins!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF009688)
                    )
                    PlayAgainButtons(
                        onPlayAgain = {
                            resetGame(
                                boardSetter = { board = it },
                                currentPlayerSetter = { currentPlayer = it },
                                winnerSetter = { winner = it },
                                isDrawSetter = { isDraw = it },
                                isGameOverSetter = { isGameOver = it }
                            )
                        },
                        onNewGame = {
                            resetGame(
                                boardSetter = { board = it },
                                currentPlayerSetter = { currentPlayer = it },
                                winnerSetter = { winner = it },
                                isDrawSetter = { isDraw = it },
                                isGameOverSetter = { isGameOver = it }
                            )
                            playerXWins = 0
                            playerOWins = 0
                        }
                    )
                }
                isDraw -> {
                    Text(
                        text = "It's a draw!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF8E24AA)
                    )
                    PlayAgainButtons(
                        onPlayAgain = {
                            resetGame(
                                boardSetter = { board = it },
                                currentPlayerSetter = { currentPlayer = it },
                                winnerSetter = { winner = it },
                                isDrawSetter = { isDraw = it },
                                isGameOverSetter = { isGameOver = it }
                            )
                        },
                        onNewGame = {
                            resetGame(
                                boardSetter = { board = it },
                                currentPlayerSetter = { currentPlayer = it },
                                winnerSetter = { winner = it },
                                isDrawSetter = { isDraw = it },
                                isGameOverSetter = { isGameOver = it }
                            )
                            playerXWins = 0
                            playerOWins = 0
                        }
                    )
                }
                else -> {
                    TicTacToeBoard(
                        board = board,
                        onCellClick = { row, col ->
                            if (board[row][col].isEmpty() && winner == null && !isDraw) {
                                board[row][col] = currentPlayer
                                board = board.map { it.clone() }.toTypedArray()
                                if (checkWinner(board) != null) {
                                    winner = currentPlayer
                                } else if (board.flatten().all { it.isNotEmpty() }) {
                                    isDraw = true
                                } else {
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Tic Tac Toe!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF3F51B5) // Deep Blue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onStartGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text(text = "Start Game", color = Color.White)
        }
    }
}

@Composable
fun TicTacToeBoard(board: Array<Array<String>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    Button(
                        onClick = { onCellClick(row, col) },
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green
                    ) {
                        Text(
                            text = board[row][col],
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayAgainButtons(onPlayAgain: () -> Unit, onNewGame: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onPlayAgain,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
        ) {
            Text(text = "Play Again", color = Color.White)
        }
        Button(
            onClick = onNewGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text(text = "New Game", color = Color.White)
        }
    }
}

fun checkWinner(board: Array<Array<String>>): String? {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty()) {
            return board[i][0]
        }
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty()) {
            return board[0][i]
        }
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty()) {
        return board[0][0]
    }
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty()) {
        return board[0][2]
    }
    return null
}

fun resetGame(
    boardSetter: (Array<Array<String>>) -> Unit,
    currentPlayerSetter: (String) -> Unit,
    winnerSetter: (String?) -> Unit,
    isDrawSetter: (Boolean) -> Unit,
    isGameOverSetter: (Boolean) -> Unit
) {
    boardSetter(Array(3) { Array(3) { "" } })
    currentPlayerSetter("X")
    winnerSetter(null)
    isDrawSetter(false)
    isGameOverSetter(false)
}
