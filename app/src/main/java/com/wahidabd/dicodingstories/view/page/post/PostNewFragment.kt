package com.wahidabd.dicodingstories.view.page.post

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.databinding.FragmentPostNewBinding
import com.wahidabd.dicodingstories.utils.Constants.LOCATION_PERMISSION
import com.wahidabd.dicodingstories.utils.Constants.REQUEST_CODE_PERMISSIONS
import com.wahidabd.dicodingstories.utils.Constants.REQUIRED_PERMISSIONS
import com.wahidabd.dicodingstories.utils.createCustomTempFile
import com.wahidabd.dicodingstories.utils.lottie.LottieLoading
import com.wahidabd.dicodingstories.utils.mySnackBar
import com.wahidabd.dicodingstories.utils.texTrim
import com.wahidabd.dicodingstories.utils.uriToFile
import com.wahidabd.dicodingstories.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PostNewFragment : Fragment() {

    private var _binding: FragmentPostNewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by viewModels()
    private var myFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Float? = null
    private var lon: Float? = null

    @Inject
    lateinit var loading: LottieLoading

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                view?.mySnackBar("Not getting permission!")
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.shareLocation.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                launcherLocationPermission.launch(LOCATION_PERMISSION)
            }else{
                lat = null
                lon = null
            }
        }

        binding.imBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnCamera.setOnClickListener { takePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { sendRequest() }
    }

    private fun sendRequest() {
        val desc = binding.edtDescription.texTrim()
        val request = PostRequest(desc, lat = lat, lon = lon)

        if (desc.isEmpty()) view?.mySnackBar("${getString(R.string.description)} ${getString(R.string.text_not_null)}")
        else if (myFile == null) view?.mySnackBar("${getString(R.string.image)} ${getString(R.string.text_not_null)}")
        else {
            myFile?.let { observableViewModel(request, it) }
        }
    }

    private fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun getMyLastLocation() {
        if (checkPermission(LOCATION_PERMISSION[0]) && checkPermission(LOCATION_PERMISSION[1])) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                }
            }
        }else{
            launcherLocationPermission.launch(LOCATION_PERMISSION)
        }
    }

    private fun observableViewModel(request: PostRequest, file: File) {
        viewModel.postStory(request, file).observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.LOADING -> loading.start(requireContext())
                Status.ERROR -> {
                    loading.stop()
                    view?.mySnackBar(res.message.toString())
                }
                Status.SUCCESS -> {
                    loading.stop()
                    view?.mySnackBar(res.data?.message.toString())
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        createCustomTempFile(requireContext().applicationContext).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(), "com.wahidabd.dicodingstories", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            this.myFile = myFile

            val result = BitmapFactory.decodeFile(this.myFile?.path)
            binding.imgPicture.setImageBitmap(result)
            binding.imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            this.myFile = myFile
            binding.imgPicture.setImageURI(selectedImg)
            binding.imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private var launcherLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            when {
                permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLastLocation()
                permission[Manifest.permission.ACCESS_COARSE_LOCATION]
                    ?: false -> getMyLastLocation()
                else -> {
                    binding.shareLocation.isChecked = false
                    binding.shareLocation.isEnabled = false
                }
            }
        }

}