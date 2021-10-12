package com.commandiron.artbooktesting.fragmentfactory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.commandiron.artbooktesting.adapter.ArtsRecylerViewAdapter
import com.commandiron.artbooktesting.adapter.ImageRecyclerViewAdapter
import com.commandiron.artbooktesting.view.ArtDetailsFragment
import com.commandiron.artbooktesting.view.ArtsFragment
import com.commandiron.artbooktesting.view.ImageApiFragment
import javax.inject.Inject

class ArtsFragmentFactory @Inject constructor(
    private val artRecyclerViewAdapter: ArtsRecylerViewAdapter,
    private val imageRecyclerViewAdapter: ImageRecyclerViewAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            ArtsFragment::class.java.name -> ArtsFragment(artRecyclerViewAdapter)
            ImageApiFragment::class.java.name -> ImageApiFragment(imageRecyclerViewAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}