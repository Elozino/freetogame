package com.dev.freetoplay.presentation.screen.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.freetoplay.domain.model.GameDetail
import com.dev.freetoplay.domain.repository.GameRepository
import com.dev.freetoplay.util.GAME_ID_KEY
import com.dev.freetoplay.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val repository: GameRepository,
    private val saveStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _gameDetailsState: MutableStateFlow<Resource<GameDetail?>> =
        MutableStateFlow(value = Resource.Loading())

    val gameDetailState: StateFlow<Resource<GameDetail?>>
        get() = _gameDetailsState

    private val _gameTitleState: MutableState<String> =
        mutableStateOf(value = "")

    val gameTitle: State<String>
        get() = _gameTitleState

    init {
        saveStateHandle.get<String>(key = GAME_ID_KEY)?.let { id ->
            getGameDetail(id = id.toInt())
        }
    }

    private fun getGameDetail(id: Int) {
        viewModelScope.launch {
            _gameDetailsState.value = repository.getGame(id = id)
        }
    }

    fun setGameTitle(title: String){
        _gameTitleState.value = title
    }
}