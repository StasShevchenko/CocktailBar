package com.example.coctailbar.cocktails_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.coctailbar.R
import com.example.coctailbar.databinding.CocktailsListFragmentBinding

class CocktailsListFragment : Fragment(R.layout.cocktails_list_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CocktailsListFragmentBinding.bind(view)
    }
}