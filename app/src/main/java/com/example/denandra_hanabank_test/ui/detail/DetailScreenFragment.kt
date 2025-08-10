package com.example.denandra_hanabank_test.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.denandra_hanabank_test.R
import com.example.denandra_hanabank_test.databinding.FragmentDetailScreenBinding
import com.example.denandra_hanabank_test.ui.detail.DetailViewModel.Companion.ARG_CARD_ID
import com.example.denandra_hanabank_test.utils.CommonUtils.dp
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailScreenFragment : Fragment() {
    private var _binding: FragmentDetailScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()
    private val attackAdapter = AttackAdapter()

    fun newInstance(cardId: String): DetailScreenFragment {
        return DetailScreenFragment().apply {
            arguments = Bundle().apply { putString(ARG_CARD_ID, cardId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupAttacksList()
        observeState()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupAttacksList() {
        binding.rvAttacks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = attackAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { s ->
                    binding.progressBar.isVisible = s.loading
                    binding.content.isGone = s.loading
                    binding.errorContainer.isVisible = s.error != null
                    binding.tvError.text = s.error ?: ""

                    s.card?.let { card ->
                        binding.content.isVisible = true
                        // Header image
                        Glide.with(this@DetailScreenFragment)
                            .load(card.images?.large ?: card.images?.small)
                            .placeholder(R.drawable.bg_card_border)
                            .into(binding.imgCardLarge)

                        binding.tvName.text = card.name.ifEmpty { "-" }

                        binding.chipGroupTypes.bindChips(card.types)

                        binding.tvHp.text = card.hp ?: "-"
                        binding.tvRarity.text = card.rarity ?: "-"

                        val evo = card.evolvesFrom?.trim().orEmpty()
                        binding.tvEvolvesFrom.apply {
                            isVisible = evo.isNotEmpty()
                            text = if (evo.isNotEmpty())
                                getString(R.string.evolves_from, evo) else ""
                        }

                        val setName = card.set?.name.orEmpty()
                        val number = card.number.orEmpty()
                        val printedTotal = card.set?.printedTotal?.toString().orEmpty()
                        binding.tvSetInfo.text = getString(R.string.set_info_format, setName, number, printedTotal)
                        binding.tvArtist.text = getString(R.string.artist_format, card.artist.orEmpty())

                        val weakChips = (card.weaknesses ?: emptyList()).mapNotNull { it.type }
                        binding.chipGroupWeakResist.bindChips(weakChips)

                        val attacks = card.attacks ?: emptyList()
                        if (attacks.isNotEmpty()) {
                            binding.tvAttacksHeader.isVisible = true
                            binding.rvAttacks.isVisible = true
                            attackAdapter.submitList(attacks)
                        } else {
                            binding.tvAttacksHeader.isVisible = false
                            binding.rvAttacks.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun ChipGroup.bindChips(types: List<String>?) {
        removeAllViews()
        types?.forEach { type ->
            addView(createEvolveChip(type))
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
            chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#2A2A2A"))

            setTextColor(Color.parseColor("#EDA606"))
            typeface = ResourcesCompat.getFont(context, R.font.lexend)

            chipStrokeWidth = 0f
            chipStrokeColor = null
            val radius = 16f.dp(ctx)
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
        }
    }
}