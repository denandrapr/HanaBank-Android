package com.example.denandra_hanabank_test.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.denandra_hanabank_test.R
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.databinding.ItemPokemonCardBinding

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val allCards = mutableListOf<PokemonCard>() // master
    private val cards = mutableListOf<PokemonCard>()     // visible
    private var showFooter = false
    private var currentQuery: String? = null

    var onFilterResult: ((isEmpty: Boolean, hasQuery: Boolean) -> Unit)? = null

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    fun submitList(newList: List<PokemonCard>) {
        allCards.clear()
        allCards.addAll(newList)
        applyFilter()
    }

    fun setQuery(query: String?) {
        currentQuery = query?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
        applyFilter()
    }

    private fun applyFilter() {
        val q = currentQuery
        cards.clear()
        if (q == null) {
            cards.addAll(allCards)
        } else {
            cards.addAll(
                allCards.filter { c ->
                    (c.name?.contains(q, ignoreCase = true) == true) ||
                            (c.evolvesFrom?.contains(q, ignoreCase = true) == true) ||
                            (c.types?.any { it.contains(q, ignoreCase = true) } == true)
                }
            )
        }
        onFilterResult?.invoke(cards.isEmpty(), q != null)
        notifyDataSetChanged()
    }

    fun showLoadingFooter() {
        if (!showFooter) {
            showFooter = true
            notifyItemInserted(cards.size) // footer ada di index 'cards.size'
        }
    }

    fun hideLoadingFooter() {
        if (showFooter) {
            val pos = cards.size
            showFooter = false
            notifyItemRemoved(pos)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < cards.size) TYPE_ITEM else TYPE_FOOTER
    }

    override fun getItemCount(): Int = cards.size + if (showFooter) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val binding = ItemPokemonCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ItemVH(binding)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_footer_loading, parent, false)
            FooterVH(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemVH) {
            val card = cards[position]
            holder.binding.tvCardName.text = card.name
            Glide.with(holder.itemView).load(card.images?.small).into(holder.binding.imgCard)
        }
    }

    inner class ItemVH(val binding: ItemPokemonCardBinding) : RecyclerView.ViewHolder(binding.root)
    inner class FooterVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}
