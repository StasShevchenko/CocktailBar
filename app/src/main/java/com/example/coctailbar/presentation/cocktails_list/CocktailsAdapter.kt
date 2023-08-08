package com.example.coctailbar.presentation.cocktails_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail
import com.example.coctailbar.databinding.CocktailItemBinding

class CocktailsAdapter(private val clickListener: (Long) -> Unit) : ListAdapter<Cocktail, CocktailsAdapter.CocktailsViewHolder>(
    CockTailsComparator()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CocktailsAdapter.CocktailsViewHolder {
        val binding =
            CocktailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CocktailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CocktailsAdapter.CocktailsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CocktailsViewHolder(private val binding: CocktailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init{
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val currentItem = getItem(position)
                        clickListener(currentItem.cocktailId)
                    }
                }
            }

        fun bind(cocktail: Cocktail) {
            binding.apply {
                cocktailTitleTextView.text = cocktail.cocktailName
            }
        }
    }

    class CockTailsComparator : DiffUtil.ItemCallback<Cocktail>(){
        override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem.cocktailId == newItem.cocktailId
        }

        override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem == newItem
        }
    }
}