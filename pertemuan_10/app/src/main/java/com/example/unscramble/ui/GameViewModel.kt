package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val usedWords: MutableSet<String> = mutableSetOf()
    private val allWords: List<String> = listOf(
        "kotlin", "android", "compose", "function", "variable",
        "object", "class", "inheritance", "activity", "fragment"
    )

    private lateinit var currentWord: String

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    var userGuess = ""
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWordAndShuffle(),
            hint = ""
        )
        userGuess = ""
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
    }

    fun checkUserGuess() {
        val correctGuess = userGuess.equals(currentWord, ignoreCase = true)

        if (correctGuess) {
            val updatedScore = _uiState.value.score + SCORE_INCREASE
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    fun showHint() {
        val hintText = currentWord.take(2) // ambil 2 huruf pertama
        _uiState.update { currentState ->
            currentState.copy(hint = hintText)
        }
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore,
                    hint = ""
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore,
                    hint = ""
                )
            }
        }
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        while (usedWords.contains(currentWord)) {
            currentWord = allWords.random()
        }
        usedWords.add(currentWord)

        return shuffleWord(currentWord)
    }

    private fun shuffleWord(word: String): String {
        val letters = word.toCharArray()
        do {
            letters.shuffle()
        } while (String(letters) == word)
        return String(letters)
    }

    companion object {
        private const val MAX_NO_OF_WORDS = 10
        private const val SCORE_INCREASE = 20
    }
}
