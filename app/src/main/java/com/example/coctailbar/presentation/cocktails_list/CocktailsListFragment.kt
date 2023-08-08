package com.example.coctailbar.presentation.cocktails_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctailbar.MyApplication
import com.example.coctailbar.R
import com.example.coctailbar.databinding.CocktailsListFragmentBinding
import com.example.coctailbar.presentation.MainActivity
import com.example.coctailbar.presentation.add_edit_cocktail.AddEditCocktailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CocktailsListFragment : Fragment(R.layout.cocktails_list_fragment) {
    private val viewModel: CocktailsListViewModel by viewModels { CocktailsListViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = CocktailsListFragmentBinding.bind(view)
        val cocktailsAdapter = CocktailsAdapter{cocktailId ->
            val action = CocktailsListFragmentDirections.actionCocktailsListFragmentToCocktailDetailsFragment(cocktailId = cocktailId)
            findNavController().navigate(action)
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
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.cocktailsList.collectLatest {cocktails ->
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