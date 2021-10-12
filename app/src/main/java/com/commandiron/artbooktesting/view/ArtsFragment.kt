package com.commandiron.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.adapter.ArtsRecylerViewAdapter
import com.commandiron.artbooktesting.databinding.FragmentArtsBinding
import com.commandiron.artbooktesting.model.Art
import com.commandiron.artbooktesting.util.Status
import com.commandiron.artbooktesting.viewmodel.ArtsViewModel
import javax.inject.Inject

class ArtsFragment @Inject constructor(
    val artRecyclerViewAdapter: ArtsRecylerViewAdapter
): Fragment(R.layout.fragment_arts) {

    lateinit var viewModel: ArtsViewModel
    private var fragmentBinding: FragmentArtsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()

        setBinding(view)

        setSwipeCallback()

        setRecyclerView()

        setFabListener()

        subscribeToObservers()
    }

    private fun setViewModel(){
        viewModel = ViewModelProvider(requireActivity()).get(ArtsViewModel::class.java)
    }

    private fun setBinding(view: View){
        val binding = FragmentArtsBinding.bind(view)
        fragmentBinding = binding
    }

    private fun setSwipeCallback(){
        val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val layoutPosition = viewHolder.layoutPosition

                val selectedArt = artRecyclerViewAdapter.arts[layoutPosition]

                viewModel.deleteSingleArt(selectedArt)
            }
        }

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(fragmentBinding?.recyclerViewArt)
    }

    private fun setRecyclerView(){
        fragmentBinding?.recyclerViewArt?.adapter = artRecyclerViewAdapter
        fragmentBinding?.recyclerViewArt?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setFabListener(){
        fragmentBinding?.fab?.setOnClickListener {
            val action = ArtsFragmentDirections.actionArtsFragmentToArtDetailsFragment("","","","")
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun subscribeToObservers(){
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            println("ViewModelden gelen data: " + it.size)
            artRecyclerViewAdapter.arts = it
        })
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}