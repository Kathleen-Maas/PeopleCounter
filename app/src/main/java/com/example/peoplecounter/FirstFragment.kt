package com.example.peoplecounter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.peoplecounter.databinding.FragmentFirstBinding
import viewBinding
import java.util.concurrent.ThreadLocalRandom.current

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val myViewModel: MyViewModel by viewModels<MyViewModel> { MyViewModelFactory(RepositoryImp(requireContext().getSharedPreferences("default", Context.MODE_PRIVATE ))) }
    private val ui: FragmentFirstBinding by viewBinding(FragmentFirstBinding::bind)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel.viewState.observe(viewLifecycleOwner){ state ->
            ui.currentPeople.text = state.currentPeople.toString()
            ui.totalPeople.text = state.totalPeople.toString()
            ui.currentPeople.setTextColor(Color.parseColor(state.colorCode))
            ui.subtractButton.isVisible = state.subtractVisible
        }

        ui.resetButton.setOnClickListener(){
            myViewModel.onResetTapped()
        }

        ui.addButton.setOnClickListener(){
            myViewModel.onAddTapped()
        }

        ui.subtractButton.setOnClickListener(){
            myViewModel.onSubtractTapped()
        }

        ui.undoButton.setOnClickListener(){
            myViewModel.onUndoTapped()
        }

        ui.redoButton.setOnClickListener(){
            myViewModel.onRedoTapped()
        }
    }

}