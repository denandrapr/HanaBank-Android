package com.example.denandra_hanabank_test.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.denandra_hanabank_test.R
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import com.example.denandra_hanabank_test.databinding.ItemPokemonCardBinding
import com.google.android.material.chip.Chip

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val allCards = mutableListOf<PokemonCard>()
    private val cards = mutableListOf<PokemonCard>()
    private var showFooter = false
    private var currentQuery: String? = null

    var onFilterResult: ((isEmpty: Boolean, hasQuery: Boolean) -> Unit)? = null
    var onItemClick: ((PokemonCard) -> Unit)? = null

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
            notifyItemInserted(cards.size)
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
            holder.bind(card)
            holder.itemView.setOnClickListener { onItemClick?.invoke(card) }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is ItemVH) {
            Glide.with(holder.itemView).clear(holder.binding.imgCard)
            holder.binding.chipGroupTypes.removeAllViews()
        }
        super.onViewRecycled(holder)
    }

    inner class ItemVH(val binding: ItemPokemonCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: PokemonCard) = with(binding) {
            Glide.with(itemView)
                .load(card.images?.small)
                .placeholder(R.drawable.img_error_api)
                .error(R.drawable.img_error_api)
                .into(imgCard)

            tvCardName.text = card.name.ifEmpty {
                "-"
            }

            chipGroupTypes.apply {
                removeAllViews()
                card.types?.forEach { type ->
                    val chip = Chip(context).apply {
                        text = type
                        isClickable = false
                        isCheckable = false
                        isFocusable = false
                        setEnsureMinTouchTargetSize(false)
                        chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#000000"))

                        setTextColor(Color.parseColor("#EDA606"))
                        typeface = ResourcesCompat.getFont(context, R.font.lexend)

                        chipStrokeWidth = 0f
                        chipStrokeColor = null
                        val radius = 16f.dp(context)
                        shapeAppearanceModel = shapeAppearanceModel
                            .toBuilder()
                            .setAllCornerSizes(radius)
                            .build()
                    }
                    addView(chip)
                }
            }

            chipGroupEvolves.apply {
                removeAllViews()
                val evo = card.evolvesFrom?.trim().orEmpty().ifEmpty { "-" }
                addView(createEvolveChip(evo))
                visibility = View.VISIBLE
            }
        }

        private fun createEvolveChip(textValue: String): Chip {
            val ctx = binding.root.context
            return Chip(ctx).apply {
                text = ctx.getString(R.string.evolves_from, textValue)
                isClickable = false
                isCheckable = false
                isFocusable = false
                setEnsureMinTouchTargetSize(false)

                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#000000"))
                setTextColor(Color.WHITE)

                chipStrokeWidth = 0f
                chipStrokeColor = null
                val radius = 16f.dp(ctx)
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(radius)
                    .build()
            }
        }
    }

    inner class FooterVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun Float.dp(context: android.content.Context): Float {
        return this * context.resources.displayMetrics.density
    }
}