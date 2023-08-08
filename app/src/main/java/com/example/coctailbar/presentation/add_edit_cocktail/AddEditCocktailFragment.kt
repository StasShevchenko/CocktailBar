package com.example.coctailbar.presentation.add_edit_cocktail

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.coctailbar.R
import com.example.coctailbar.databinding.AddEditCocktailFragmentBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddEditCocktailFragment : Fragment(R.layout.add_edit_cocktail_fragment) {

    private val viewModel: AddEditCocktailViewModel by viewModels { AddEditCocktailViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AddEditCocktailFragmentBinding.bind(view)

        binding.apply {
            cocktailNameEditText.addTextChangedListener {
                viewModel.setCocktailName(it.toString())
            }
            saveButton.setOnClickListener {
                if(viewModel.saveCocktail())
                findNavController().popBackStack()
            }
            descriptionEditText.addTextChangedListener {
                viewModel.setDescription(it.toString())
            }
            recipeEditText.addTextChangedListener {
                viewModel.setRecipe(it.toString())
            }
            addIngredientButton.setOnClickListener {
                showAddIngredientDialog()
            }
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cocktailNameErrorState.collectLatest {
                        binding.cocktailNameInputLayout.error =
                            if (it) "Enter cocktail name" else ""
                    }
                }
                launch {
                    viewModel.cocktailIngredients.collectLatest { ingredients ->
                        binding.ingredientsChipGroup.removeViews(
                            0,
                            binding.ingredientsChipGroup.childCount - 1
                        )
                        ingredients.forEachIndexed { index, it ->
                            val ingredientChip = Chip(context)
                            ingredientChip.text = it
                            ingredientChip.isCloseIconVisible = true
                            ingredientChip.setCloseIconTintResource(R.color.sky_blue)
                            ingredientChip.setOnClickListener {
                                viewModel.removeIngredient(index)
                            }
                            binding.ingredientsChipGroup.addView(ingredientChip, 0)
                        }
                    }
                }
            }
        }
    }

    private fun showAddIngredientDialog() {
        val alertDialog = MaterialAlertDialogBuilder(
            requireContext(),
        ).setView(R.layout.add_ingredient_dialog).setBackground(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.dialog_background
            )
        ).create()
        alertDialog.show()
        val cancelButton = alertDialog.findViewById<MaterialButton>(R.id.cancel_button)
        cancelButton!!.setOnClickListener {
            alertDialog.dismiss()
        }
        val ingredientNameInput = alertDialog.findViewById<TextInputLayout>(R.id.ingredient_name_input)
        val ingredientEditText =
            alertDialog.findViewById<TextInputEditText>(R.id.ingredient_name_edit_text)
        ingredientEditText!!.addTextChangedListener {
            ingredientNameInput!!.error = ""
        }
        val addIngredientButton = alertDialog.findViewById<MaterialButton>(R.id.save_button)
        addIngredientButton!!.setOnClickListener {
            if (ingredientEditText.text.toString().isBlank()) {
                ingredientNameInput!!.error = "Add title"
            } else{
                viewModel.addIngredient(ingredientEditText.text.toString())
                alertDialog.dismiss()
            }
        }
    }
}
