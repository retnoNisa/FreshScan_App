package com.example.freshscanapp.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.freshscanapp.databinding.FragmentScanBinding
import com.example.freshscanapp.ui.main.CameraActivity
import com.example.freshscanapp.ui.main.detail.DetailVeggiesActivity

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCarrot.setOnClickListener { navigateToDetail("carrot") }
        binding.buttonPotato.setOnClickListener { navigateToDetail("potato") }
        binding.buttonTomato.setOnClickListener { navigateToDetail("tomato") }
        binding.buttonPepper.setOnClickListener { navigateToDetail("pepper") }
        binding.buttonCucumber.setOnClickListener { navigateToDetail("cucumber") }
        binding.buttonOkra.setOnClickListener { navigateToDetail("okra") }
        binding.buttonCabbage.setOnClickListener { navigateToDetail("cabbage") }

        binding.card1.setOnClickListener { navigateToCameraActivity() }
        binding.card2.setOnClickListener { navigateToCameraActivity() }
        binding.card3.setOnClickListener { navigateToCameraActivity() }
        binding.card4.setOnClickListener { navigateToCameraActivity() }
        binding.card5.setOnClickListener { navigateToCameraActivity() }
        binding.card6.setOnClickListener { navigateToCameraActivity() }
        binding.card7.setOnClickListener { navigateToCameraActivity() }
    }

    private fun navigateToCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDetail(vegetableType: String) {
        val intent = Intent(requireContext(), DetailVeggiesActivity::class.java)
        intent.putExtra("VEGETABLE_TYPE", vegetableType)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

