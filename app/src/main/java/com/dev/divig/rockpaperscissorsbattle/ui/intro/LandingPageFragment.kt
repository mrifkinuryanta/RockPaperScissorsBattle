package com.dev.divig.rockpaperscissorsbattle.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.preference.GamePreference
import com.dev.divig.rockpaperscissorsbattle.databinding.FragmentLandingPageBinding
import com.dev.divig.rockpaperscissorsbattle.enum.LandingPage
import com.dev.divig.rockpaperscissorsbattle.ui.menu.GameMenuActivity

class LandingPageFragment(private val landingPage: LandingPage) : Fragment() {
    private lateinit var gamePreference: GamePreference

    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLandingPageBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        when (landingPage) {
            LandingPage.FIRST -> {
                binding?.ivLandingPageBackground?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_landing_page_1
                    )
                )
                binding?.ivLandingPageIcon?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_landing_page_1_32dp
                    )
                )
                binding?.tvLandingPageTitle?.text =
                    getString(R.string.text_placeholder_player_vs_player)
                binding?.tvLandingPageDesc?.text = getString(R.string.text_desc_landing_page_1)
            }
            LandingPage.SECOND -> {
                binding?.ivLandingPageBackground?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_landing_page_2
                    )
                )
                binding?.ivLandingPageIcon?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_landing_page_2_32dp
                    )
                )
                binding?.tvLandingPageTitle?.text =
                    getString(R.string.text_placeholder_player_vs_computer)
                binding?.tvLandingPageDesc?.text = getString(R.string.text_desc_landing_page_2)
            }
            LandingPage.THIRD -> {
                gamePreference = GamePreference(requireContext())
                binding?.ivLandingPageBackground?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_landing_page_3
                    )
                )
                binding?.ivLandingPageIcon?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_landing_page_3_32dp
                    )
                )
                binding?.tvLandingPageTitle?.text = getString(R.string.text_title_landing_page_3)
                binding?.tvLandingPageDesc?.isVisible = false
                binding?.tilNamePlayerOne?.isVisible = true
                binding?.etNamePlayerOne?.setText(gamePreference.namePlayerOne)
            }
        }
    }

    private fun isValidFormName(): Boolean {
        val etNamePlayerOne = binding?.etNamePlayerOne?.text.toString().trim()

        return if (etNamePlayerOne.isEmpty()) {
            binding?.tilNamePlayerOne?.isErrorEnabled = true
            binding?.tilNamePlayerOne?.error = getString(R.string.text_error_field_request)
            false
        } else {
            binding?.tilNamePlayerOne?.isErrorEnabled = false
            gamePreference.namePlayerOne = etNamePlayerOne
            true
        }
    }

    fun navigateToGameMenu() {
        if (isValidFormName()) {
            startActivity(Intent(requireContext(), GameMenuActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}