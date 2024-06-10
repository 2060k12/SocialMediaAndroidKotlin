package com.phoenix.socialmedia.camera

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.phoenix.socialmedia.R
import com.phoenix.socialmedia.databinding.CameraFragmentBinding
import java.text.SimpleDateFormat
import java.util.Locale


class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels()

    private lateinit var binding: CameraFragmentBinding

    // Using CameraController
    private lateinit var cameraController: LifecycleCameraController


    // camera State
    private var cameraFacingFront : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = CameraFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!CameraViewModel.hasPermission(context = requireContext()) ){
            activityResultLauncher.launch(CameraViewModel.REQUIRED_PERMISSION)
        }

        else
        {
            startCamera()

        }

        binding.captureImageButton.setOnClickListener(){
            takePhoto()
        }

        binding.rotateCameraButton.setOnClickListener(){
            cameraFacingFront = !cameraFacingFront
            startCamera()

        }

        binding.goToGalleryButton.setOnClickListener(){
            openGallery()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            val selectedImageUri : String = data?.data.toString()
            val bundle = bundleOf("imageUri" to selectedImageUri)
            findNavController().navigate(R.id.action_cameraFragment_to_loadClickedImageFragment, bundle)

        }

    }



    private fun takePhoto() {
        val name = SimpleDateFormat(CameraViewModel.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SocialMedia-Image")
            }
        }

        // Create output options which contains file + metadata
        val outputOption = ImageCapture.OutputFileOptions.Builder(
                requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
        
        
        cameraController.takePicture(
            outputOption, 
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    var savedImageUri = outputFileResults.savedUri
                    val navController : NavController = findNavController()
                    val bundle = bundleOf("imageUri" to savedImageUri.toString())
                    navController.navigate(R.id.action_cameraFragment_to_loadClickedImageFragment, bundle)
                    Toast.makeText(context, outputFileResults.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    private fun startCamera() {
      val previewView: PreviewView = binding.previewView
        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(this)
        if(cameraFacingFront){
            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        }
        else
        {
            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        }
        previewView.controller = cameraController
    }


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions->

            var permissionGranted = true
            permissions.entries.forEach{
                if(it.key in CameraViewModel.REQUIRED_PERMISSION && !it.value){
                    permissionGranted = false
                }
                
                if(!permissionGranted){
                    Toast.makeText(context, "Permission request denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

}

