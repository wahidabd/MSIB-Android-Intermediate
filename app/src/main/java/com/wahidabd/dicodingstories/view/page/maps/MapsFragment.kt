package com.wahidabd.dicodingstories.view.page.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.databinding.FragmentMapsBinding
import com.wahidabd.dicodingstories.utils.MapStyle
import com.wahidabd.dicodingstories.utils.MapStyle.*
import com.wahidabd.dicodingstories.utils.MapType
import com.wahidabd.dicodingstories.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: ThemeViewModel by viewModels()

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivType.setOnClickListener {
            val bottomSheet = BottomSheetOptions()
            bottomSheet.show(parentFragmentManager, null)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true

        getMyLocation()
        getMapsTheme()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if (isGranted) getMyLocation()
    }

    private fun getMyLocation(){
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getMapsTheme(){
        settingViewModel.getMapType().observe(viewLifecycleOwner){ type ->
            when(type){
                MapType.NORMAL -> map.mapType =  GoogleMap.MAP_TYPE_NORMAL
                MapType.SATTELITE -> map.mapType =  GoogleMap.MAP_TYPE_SATELLITE
                MapType.TERRAIN -> map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }

        settingViewModel.getMapStyle().observe(viewLifecycleOwner){style ->
            when(style){
                NORMAL -> map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style_normal))
                NIGHT -> map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style_night))
                SILVER -> map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style_silver))
            }
        }
    }

}