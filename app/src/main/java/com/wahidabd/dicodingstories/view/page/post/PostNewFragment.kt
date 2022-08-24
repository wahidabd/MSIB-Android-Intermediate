package com.wahidabd.dicodingstories.view.page.post

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
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.databinding.FragmentPostNewBinding
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

    @Inject lateinit var loading: LottieLoading

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionsGranted()){
                view?.mySnackBar("Not getting permission!")
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.imBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnCamera.setOnClickListener {takePhoto()}
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { sendRequest() }
    }

    private fun sendRequest(){
        val desc = binding.edtDescription.texTrim()
        val request = myFile?.let { PostRequest(desc, it) }

        if (desc.isEmpty()) view?.mySnackBar("${getString(R.string.description)} ${getString(R.string.text_not_null)}")
        else if (myFile == null) view?.mySnackBar("${getString(R.string.image)} ${getString(R.string.text_not_null)}")
        else{
            if (request != null) observableViewModel(request)
        }
    }

    private fun observableViewModel(request: PostRequest) {
        viewModel.postStory(request).observe(viewLifecycleOwner){res ->
            when(res.status){
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

    private fun takePhoto(){
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

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            this.myFile = myFile

            val result = BitmapFactory.decodeFile(this.myFile?.path)
            binding.imgPicture.setImageBitmap(result)
            binding.imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            this.myFile = myFile
            binding.imgPicture.setImageURI(selectedImg)
            binding.imgPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

}