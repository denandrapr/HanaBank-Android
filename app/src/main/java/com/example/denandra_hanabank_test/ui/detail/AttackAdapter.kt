package com.example.denandra_hanabank_test.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.denandra_hanabank_test.data.remote.model.pokemon.Attack
import com.example.denandra_hanabank_test.databinding.ItemAttackBinding
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
                item.cost?.forEach { c ->
                    addView(Chip(root.context).apply {
                        text = c
                        isClickable = false
                        isCheckable = false
                        setEnsureMinTouchTargetSize(false)
                    })
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