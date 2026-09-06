package com.app.workoutu2.game

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class GameState(
    val operation: String = "",
    val correctAnswer: Int = 0,
    val userAnswer: String = "",
    val round: Int = 1,
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val totalTime: Long = 0,
    val isGameStarted: Boolean = false,
    val isGameOver: Boolean = false,
    val lastAnswerState: AnswerState = AnswerState.NEUTRAL,
    val topTimes: List<Long> = emptyList()
)

enum class AnswerState { CORRECT, INCORRECT, NEUTRAL }

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("workoutu_prefs", Context.MODE_PRIVATE)
    private val _gameState = MutableStateFlow(GameState(topTimes = loadTopTimes()))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var startTime: Long = 0
    private var timerJob: Job? = null

    private fun loadTopTimes(): List<Long> {
        val savedString = sharedPreferences.getString("top_times", "") ?: ""
        if (savedString.isEmpty()) return emptyList()
        return savedString.split(",").mapNotNull { it.toLongOrNull() }.sorted()
    }

    private fun saveTopTimes(times: List<Long>) {
        val savedString = times.joinToString(",")
        sharedPreferences.edit().putString("top_times", savedString).apply()
    }

    private fun updateTopTimes(newTime: Long) {
        val currentTop = loadTopTimes().toMutableList()
        currentTop.add(newTime)
        val updatedTop = currentTop.sorted().take(5)
        saveTopTimes(updatedTop)
        _gameState.update { it.copy(topTimes = updatedTop) }
    }

    private fun startGame() {
        _gameState.update { it.copy(isGameStarted = true, isGameOver = false, totalTime = 0, round = 1, correctCount = 0, incorrectCount = 0) }
        startTime = System.currentTimeMillis()
        generateQuestion()
        startTimer()
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.NumberInput -> handleInput(event.number)
            is GameEvent.Clear -> handleClear()
            is GameEvent.Restart -> restartGame()
            is GameEvent.StartGame -> startGame()
        }
    }

    private fun handleInput(number: Int) {
        if (_gameState.value.isGameOver) return
        if (_gameState.value.lastAnswerState == AnswerState.INCORRECT) return

        val newAnswer = _gameState.value.userAnswer + number.toString()
        _gameState.update { it.copy(userAnswer = newAnswer, lastAnswerState = AnswerState.NEUTRAL) }

        val correctAnswerString = _gameState.value.correctAnswer.toString()
        if (newAnswer.length == correctAnswerString.length) {
            checkAnswer(newAnswer)
        } else if (newAnswer.length > correctAnswerString.length) {
            markAsIncorrect()
        }
    }

    private fun checkAnswer(answer: String) {
        if (answer.toIntOrNull() == _gameState.value.correctAnswer) {
            _gameState.update {
                it.copy(
                    correctCount = it.correctCount + 1,
                    lastAnswerState = AnswerState.CORRECT
                )
            }
            proceedToNextRound()
        } else {
            markAsIncorrect()
        }
    }

    private fun markAsIncorrect() {
        _gameState.update {
            it.copy(
                incorrectCount = it.incorrectCount + 1,
                lastAnswerState = AnswerState.INCORRECT
            )
        }
        viewModelScope.launch {
            delay(300)
            _gameState.update { it.copy(userAnswer = "", lastAnswerState = AnswerState.NEUTRAL) }
        }
    }

    private fun handleClear() {
        if (_gameState.value.isGameOver) return
        _gameState.update { it.copy(userAnswer = "", lastAnswerState = AnswerState.NEUTRAL) }
    }

    private fun proceedToNextRound() {
        if (_gameState.value.round >= 20) {
            endGame()
        } else {
            _gameState.update { it.copy(round = it.round + 1) }
            generateQuestion()
        }
    }

    private fun generateQuestion() {
        val operator = listOf("+", "-", "*", "/").random()

        val question: String
        val answer: Int

        when (operator) {
            "+" -> {
                val num1 = Random.nextInt(10, 101)
                val num2 = Random.nextInt(10, 101)
                question = "$num1+$num2"
                answer = num1 + num2
            }
            "-" -> {
                val num1 = Random.nextInt(10, 101)
                val num2 = Random.nextInt(10, 101)
                val n1 = maxOf(num1, num2)
                val n2 = minOf(num1, num2)
                question = "$n1-$n2"
                answer = n1 - n2
            }
            "*" -> {
                val num1 = Random.nextInt(0, 12)
                val num2 = Random.nextInt(1, 12)
                question = "${num1}x${num2}"
                answer = num1 * num2
            }
            "/" -> {
                val divisor = Random.nextInt(1, 13)
                val quotient = Random.nextInt(1, 13)
                val dividend = divisor * quotient
                question = "$dividend÷$divisor"
                answer = quotient
            }
            else -> {
                question = ""
                answer = 0
            }
        }

        _gameState.update {
            it.copy(
                operation = question,
                correctAnswer = answer,
                userAnswer = "",
                lastAnswerState = AnswerState.NEUTRAL
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (!_gameState.value.isGameOver) {
                val elapsed = System.currentTimeMillis() - startTime
                _gameState.update { it.copy(totalTime = elapsed) }
                delay(100)
            }
        }
    }

    private fun endGame() {
        timerJob?.cancel()
        val finalTime = System.currentTimeMillis() - startTime
        _gameState.update { it.copy(isGameOver = true, totalTime = finalTime) }
        updateTopTimes(finalTime)
    }

    private fun restartGame() {
        _gameState.update { 
            it.copy(
                isGameStarted = false,
                isGameOver = false,
                userAnswer = "",
                lastAnswerState = AnswerState.NEUTRAL
            )
        }
    }
}

sealed class GameEvent {
    data class NumberInput(val number: Int) : GameEvent()
    object Clear : GameEvent()
    object Restart : GameEvent()
    object StartGame : GameEvent()
}
