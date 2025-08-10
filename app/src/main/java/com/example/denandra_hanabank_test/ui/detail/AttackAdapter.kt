package com.example.denandra_hanabank_test.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.denandra_hanabank_test.R
import com.example.denandra_hanabank_test.data.remote.model.pokemon.Attack
import com.example.denandra_hanabank_test.databinding.ItemAttackBinding
import com.example.denandra_hanabank_test.utils.CommonUtils.dp
import com.google.android.material.chip.Chip


class AttackAdapter : ListAdapter<Attack, AttackAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Attack>() {
        override fun areItemsTheSame(oldItem: Attack, newItem: Attack): Boolean =
            oldItem.name == newItem.name && oldItem.text == newItem.text

        override fun areContentsTheSame(oldItem: Attack, newItem: Attack): Boolean =
            oldItem == newItem
    }

    inner class VH(val binding: ItemAttackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Attack) = with(binding) {
            tvAttackName.text = item.name ?: "-"
            tvAttackDamage.text = item.damage.orEmpty()
            tvAttackText.text = item.text.orEmpty()

            chipGroupEnergyCost.apply {
                removeAllViews()
                item.cost?.forEach { type ->
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(ItemAttackBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}