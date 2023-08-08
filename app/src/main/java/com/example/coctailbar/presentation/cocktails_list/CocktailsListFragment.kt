package com.example.coctailbar.presentation.cocktails_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctailbar.MyApplication
import com.example.coctailbar.R
import com.example.coctailbar.databinding.CocktailsListFragmentBinding
import com.example.coctailbar.presentation.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CocktailsListFragment : Fragment(R.layout.cocktails_list_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cocktailsDao = ((activity as MainActivity).application as MyApplication).db.cocktailsDao

        val binding = CocktailsListFragmentBinding.bind(view)
        val cocktailsAdapter = CocktailsAdapter{
        }

        binding.apply {
            cocktailsRecyclerView.apply {
                adapter = cocktailsAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            fab.setOnClickListener{
                val action = CocktailsListFragmentDirections.actionCocktailsListFragmentToAddEditCocktailFragment()
                findNavController().navigate(action)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    cocktailsDao.getAllCocktails().collectLatest {cocktails ->
                        if (cocktails.isEmpty()) {
                            binding.apply {
                                cocktailsRecyclerView.visibility = View.GONE
                                cocktailGirlImage.visibility = View.VISIBLE
                                emptyCocktailsMessageTextView.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                cocktailsRecyclerView.visibility = View.VISIBLE
                                cocktailGirlImage.visibility = View.GONE
                                emptyCocktailsMessageTextView.visibility = View.GONE
                            }
                            cocktailsAdapter.submitList(cocktails)
                        }
                    }
                }
            }
        }
    }

}