package com.example.peoplecounter

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class MyViewModel(val repository: Repository) : ViewModel() {

    private val colorCodeState = repository.currentState.map { currentNumber ->
        if(currentNumber > 15)
        {"#FF1100"}
        else
        {"#FF6200EE"}
    }

    private val subtractVisibleState : Flow<Boolean> = repository.currentState.map{ currentNumber ->
        currentNumber != 0
    }

    val viewState = combine (
        repository.currentState,
        repository.totalState,
        colorCodeState,
        subtractVisibleState) {a, b, c, d ->
        MyViewState(a, b, c, d)}.asLiveData()

    fun onResetTapped() {
        repository.reset()
    }

    fun onAddTapped(){
        repository.addPerson()
    }

    fun onSubtractTapped(){
        repository.subtractPerson()
    }

    fun onUndoTapped(){
        repository.undo()
    }

    fun onRedoTapped(){
        repository.redo()
    }

}
