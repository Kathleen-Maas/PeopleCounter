package com.example.peoplecounter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

interface Repository{
    val totalState: MutableStateFlow<Int>
    val currentState: MutableStateFlow<Int>
    fun addPerson()
    fun subtractPerson()
    fun reset()
    fun undo()
    fun redo()
}

class RepositoryImp(
    private val prefs: SharedPreferences
) : Repository {

    private var total: Int = prefs.getInt("total", 0)
    private var current: Int = prefs.getInt("current", 0)
    private val undoStack : Stack<Wrapper> = Stack()
    private val redoStack : Stack<Wrapper> = Stack()

    override val totalState = MutableStateFlow(total)
    override val currentState = MutableStateFlow(current)

    override fun addPerson() {
        undoStack.push(Wrapper(current, total))
        total++
        current++
        redoStack.clear()
        save()
    }

    override fun subtractPerson() {
        undoStack.push(Wrapper(current, total))
        redoStack.clear()
        current--
        save()
    }

    override fun reset() {
        undoStack.push(Wrapper(current, total))
        redoStack.clear()
        total = 0
        current = 0
        save()
    }

    override fun redo() {
        if(!redoStack.empty()){
            undoStack.push(Wrapper(current, total))
            val stackPop : Wrapper = redoStack.pop()
            total = stackPop.total
            current = stackPop.current
            save()
        }
    }

    override fun undo() {
        if(!undoStack.empty()){
            redoStack.push(Wrapper(current, total))
            val stackPop : Wrapper = undoStack.pop()
            total = stackPop.total
            current = stackPop.current
            save()
        }
    }

    private fun save(){
        with (prefs.edit()) {
            putInt("total", total)
            putInt("current", current)
            apply()
        }
        totalState.value = total
        currentState.value = current
    }

}

data class Wrapper(
    val current : Int,
    val total : Int
)