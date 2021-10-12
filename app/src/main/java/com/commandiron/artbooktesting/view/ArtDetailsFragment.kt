package com.commandiron.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.commandiron.artbooktesting.R
import com.commandiron.artbooktesting.databinding.FragmentArtDetailsBinding
import com.commandiron.artbooktesting.databinding.FragmentArtsBinding
import com.commandiron.artbooktesting.util.Status
import com.commandiron.artbooktesting.viewmodel.ArtDetailsViewModel
import com.commandiron.artbooktesting.viewmodel.ArtsViewModel
import javax.inject.Inject

class ArtDetailsFragment@Inject constructor(
    val glide: RequestManager
): Fragment(R.layout.fragment_art_details) {

    lateinit var viewModel: ArtDetailsViewModel
    private var fragmentBinding: FragmentArtDetailsBinding? = null

    private var imageUrl: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()

        setBinding(view)

        setBackPressed()

        getBundle()

        subscribeToObservers()

        setImageViewButton()

        setSaveButton()
    }

    private fun setViewModel(){
        viewModel = ViewModelProvider(requireActivity()).get(ArtDetailsViewModel::class.java)
    }

    private fun setBinding(view: View) {
        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding
    }

    private fun setBackPressed() {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setSelectedImage("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    private fun getBundle(){
        arguments?.let {
            val bundle = ArtDetailsFragmentArgs.fromBundle(it)
            if(bundle.artName.isNotEmpty()){
                fragmentBinding?.artNameEditText?.setText(bundle.artName)
                fragmentBinding?.artArtistNameEditText?.setText(bundle.artArtistName)
                fragmentBinding?.artYearEditText?.setText(bundle.artYear)
            }
            imageUrl = ArtDetailsFragmentArgs.fromBundle(it).artUrl
            viewModel.setSelectedImage(imageUrl)
        }
    }

    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer {url->
            fragmentBinding?.let {
                glide.load(url).into(it.artImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToArtsFragment())
                    viewModel.resetInsertArtMsg()
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                }
                Status.LOADING ->{
                }
            }
        })
    }

    private fun setImageViewButton(){
        fragmentBinding?.artImageView?.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }
    }

    private fun setSaveButton(){
        fragmentBinding?.artSave?.setOnClickListener {
            viewModel.makeArt(
                fragmentBinding?.artNameEditText?.text.toString(),
                fragmentBinding?.artArtistNameEditText?.text.toString(),
                fragmentBinding?.artYearEditText?.text.toString())
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}