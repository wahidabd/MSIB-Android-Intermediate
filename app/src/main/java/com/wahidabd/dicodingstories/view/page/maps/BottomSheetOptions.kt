package com.wahidabd.dicodingstories.view.page.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.databinding.BottomMapsStyleBinding
import com.wahidabd.dicodingstories.utils.MapStyle
import com.wahidabd.dicodingstories.utils.MapType.*
import com.wahidabd.dicodingstories.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetOptions : BottomSheetDialogFragment() {

    private var _binding: BottomMapsStyleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ThemeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomMapsStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgMapsNormal.setOnClickListener { viewModel.setMapType(NORMAL)}
        binding.imgMapsSattelite.setOnClickListener { viewModel.setMapType(SATTELITE)}
        binding.imgMapsTerrain.setOnClickListener { viewModel.setMapType(TERRAIN)}

        binding.imgMapsStyleNormal.setOnClickListener { viewModel.setMapStyle(MapStyle.NORMAL) }
        binding.imgMapsStyleNight.setOnClickListener { viewModel.setMapStyle(MapStyle.NIGHT) }
        binding.imgMapsStyleSilver.setOnClickListener { viewModel.setMapStyle(MapStyle.SILVER) }

        setMapsTheme()
    }


    private fun setMapsTheme(){
        viewModel.getMapType().observe(viewLifecycleOwner){ type ->
            with(binding){
                when(type){
                    NORMAL -> {
                        tvMapsNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        tvMapsSattelite.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsTerrain.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))

                        imgMapsNormal.setBackgroundResource(R.drawable.bg_stroke_maps)
                        imgMapsSattelite.background = null
                        imgMapsTerrain.background = null
                    }
                    SATTELITE -> {
                        tvMapsNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsSattelite.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        tvMapsTerrain.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))

                        imgMapsNormal.background = null
                        imgMapsSattelite.setBackgroundResource(R.drawable.bg_stroke_maps)
                        imgMapsTerrain.background = null
                    }
                    TERRAIN -> {
                        tvMapsNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsSattelite.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsTerrain.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))

                        imgMapsNormal.background = null
                        imgMapsSattelite.background = null
                        imgMapsTerrain.setBackgroundResource(R.drawable.bg_stroke_maps)
                    }
                }
            }
        }

        viewModel.getMapStyle().observe(viewLifecycleOwner){style ->
            with(binding){
                when(style){
                    MapStyle.NORMAL -> {
                        tvMapsStyleNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        tvMapsStyleNight.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsStyleSilver.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))

                        imgMapsStyleNormal.setBackgroundResource(R.drawable.bg_stroke_maps)
                        imgMapsStyleNight.background = null
                        imgMapsStyleSilver.background = null
                    }
                    MapStyle.NIGHT -> {
                        tvMapsStyleNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsStyleNight.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        tvMapsStyleSilver.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))

                        imgMapsStyleNormal.background = null
                        imgMapsStyleNight.setBackgroundResource(R.drawable.bg_stroke_maps)
                        imgMapsStyleSilver.background = null
                    }
                    MapStyle.SILVER -> {
                        tvMapsStyleNormal.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsStyleNight.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_wood))
                        tvMapsStyleSilver.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))

                        imgMapsStyleNormal.background = null
                        imgMapsStyleNight.background = null
                        imgMapsStyleSilver.setBackgroundResource(R.drawable.bg_stroke_maps)
                    }
                }
            }
        }
    }
}