package com.wahidabd.dicodingstories.view.page.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.databinding.FragmentDetailBinding
import com.wahidabd.dicodingstories.utils.bitmapFromVector
import com.wahidabd.dicodingstories.utils.setImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imBack.setOnClickListener {
            findNavController().navigateUp()
        }

        with(binding){
            imgPicture.setImage(args.post.photoUrl)
            tvName.text = args.post.name
            tvDesc.text = args.post.description
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps_in_detail) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true

        val data = args.post
        if (data.lat != null && data.lon != null){
            binding.cardMaps.visibility = View.VISIBLE

            val latLng = LatLng(data.lat, data.lon)
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(bitmapFromVector(requireContext(), R.drawable.ic_marker))
            )

            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 10F)
            )
        }else{
            binding.cardMaps.visibility = View.GONE
        }
    }
}