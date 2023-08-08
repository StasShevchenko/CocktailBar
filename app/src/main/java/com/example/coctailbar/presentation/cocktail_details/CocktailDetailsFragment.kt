package com.example.coctailbar.presentation.cocktail_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coctailbar.MyApplication
import com.example.coctailbar.R
import com.example.coctailbar.databinding.CocktailDetailsFragmentBinding
import com.example.coctailbar.presentation.MainActivity
import com.example.coctailbar.presentation.add_edit_cocktail.AddEditCocktailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CocktailDetailsFragment : Fragment(R.layout.cocktail_details_fragment) {
    private val viewModel: CocktailDetailViewModel by viewModels { CocktailDetailViewModel.Factory }
    val args: CocktailDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CocktailDetailsFragmentBinding.bind(view)
        binding.editButton.setOnClickListener {
            val action = CocktailDetailsFragmentDirections.actionCocktailDetailsFragmentToAddEditCocktailFragment(cocktailId = args.cocktailId)
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentCocktail.collectLatest { cocktail ->
                        cocktail?.let {
                            binding.apply {
                                cocktailTitleTextView.text = cocktail.cocktailName
                                if (cocktail.cocktailDescription != null) {
                                    descriptionTextView.visibility = View.VISIBLE
                                    descriptionTextView.text = cocktail.cocktailDescription
                                }
                                if (cocktail.cocktailRecipe != null) {
                                    recipeTextView.visibility = View.VISIBLE
                                    recipeTextView.text = "recipe: ${cocktail.cocktailRecipe}"
                                }
                                var ingredientsText = "ingredients:\n\n"
                                if (cocktail.cocktailIngredients.isNotEmpty()) {
                                    cocktail.cocktailIngredients.forEach {ingredient ->
                                        ingredientsText += "${ingredient}\n-\n"
                                    }
                                    ingredientsTextView.visibility = View.VISIBLE
                                    ingredientsTextView.text = ingredientsText
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}