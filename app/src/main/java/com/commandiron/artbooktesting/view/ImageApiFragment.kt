package com.commandiron.artbooktesting.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.adapter.ImageRecyclerViewAdapter
import com.commandiron.artbooktesting.databinding.FragmentArtDetailsBinding
import com.commandiron.artbooktesting.databinding.FragmentImageApiBinding
import com.commandiron.artbooktesting.util.Status
import com.commandiron.artbooktesting.viewmodel.ArtsViewModel
import com.commandiron.artbooktesting.viewmodel.ImageApiViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    val imageRecyclerViewAdapter: ImageRecyclerViewAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ImageApiViewModel
    private var fragmentBinding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()

        setBinding(view)

        setEditTextListener()

        setRecyclerView()

        subscribeToObservers()

        setBackPressedCallback()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(ImageApiViewModel::class.java)
    }

    private fun setBinding(view: View) {
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding
    }

    private fun setEditTextListener(){
        var job: Job? = null
        fragmentBinding?.searchArtEditText?.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchImage(it.toString())
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken,0)
                    }
                }
            }
        }
    }

    private fun setRecyclerView(){
        fragmentBinding?.searchArtRecyclerView?.adapter = imageRecyclerViewAdapter
        fragmentBinding?.searchArtRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun subscribeToObservers(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS->{
                    val urls = it.data?.hits?.map {imageResult ->
                        imageResult.previewURL
                    }

                    imageRecyclerViewAdapter.images = urls ?: listOf()
                    fragmentBinding?.searchArtProgressBar?.visibility = View.GONE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                    fragmentBinding?.searchArtProgressBar?.visibility = View.GONE
                }
                Status.LOADING->{
                    fragmentBinding?.searchArtProgressBar?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setBackPressedCallback(){
        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}