package com.example.myapplication.ui.upload

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentUploadBinding

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    var pickedImage: Uri? = null
    private lateinit var imageView: ImageView

    companion object {
        const val IMAGE_REQUEST_CODE = 100
        const val SELECT_FILE_REQUEST_CODE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        imageView = binding.imageViewUploadImage

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintUploadImage.setOnClickListener {
            binding.constraintUploadImage.visibility = View.INVISIBLE;
            pickFileFromDevice()
        }
    }

    private fun pickFileFromDevice(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Allow the user to select any file type
        startActivityForResult(intent, SELECT_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            val selectedFileUri: Uri? = data?.data

            // Upload the file to your backend using the Uri
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

















