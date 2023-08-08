package com.example.coctailbar.presentation.cocktails_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coctailbar.MyApplication
import com.example.coctailbar.R
import com.example.coctailbar.databinding.CocktailsListFragmentBinding
import com.example.coctailbar.presentation.MainActivity
import kotlinx.coroutines.launch

class CocktailsListFragment : Fragment(R.layout.cocktails_list_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CocktailsListFragmentBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            ((activity as MainActivity).application as MyApplication).db.cocktailsDao.getAllCocktails()
        }
        binding.fab.setOnClickListener{
            val action = CocktailsListFragmentDirections.actionCocktailsListFragmentToAddEditCocktailFragment()
            findNavController().navigate(action)
        }
    }


}