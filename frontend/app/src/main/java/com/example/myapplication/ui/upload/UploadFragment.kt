package com.example.myapplication.ui.upload

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContentResolverCompat.query
import androidx.fragment.app.Fragment
import com.example.myapplication.auth.SignInActivity
import com.example.myapplication.databinding.FragmentUploadBinding
import com.example.myapplication.ui.webshop.webshoplistitems.CaffList
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

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
        var selectedFileUri: Uri? = null
        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
             selectedFileUri = data?.data
            val file = File(selectedFileUri.toString())
            val fileDescriptor = activity?.contentResolver?.openFileDescriptor(selectedFileUri!!, "r", null)



            Log.e("URI", selectedFileUri.toString())
            uploadCaff(selectedFileUri).start()
            // Upload the file to your backend using the Uri
        }


    }
   /* fun ContentResolver.getFileName(uri: Uri){
        var name = ""
        val cursor = query(uri, null,null,null, null)
        cursor?.use {
            it.moveToFirst()
            name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }*/



    private fun uploadCaff(file: Uri?): Thread {
        return Thread {
            val boundary = "Boundary-${System.currentTimeMillis()}"

            val url = URL("http://192.168.1.93/request")
            val connection = url.openConnection() as HttpURLConnection
            connection.addRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=$boundary"
            )
            connection.setRequestProperty("x-access-tokens", SignInActivity.token)
            connection.doInput = true
            connection.doOutput = true

            val outputStreamToRequestBody = connection.outputStream
            val httpRequestBodyWriter = BufferedWriter(OutputStreamWriter(outputStreamToRequestBody))

            httpRequestBodyWriter.write("\n--$boundary\n")
            httpRequestBodyWriter.write("Content-Disposition: form-data;"
                    + "name=\"file\";"
                    + "filename=\"" + "2.caff"  + "\""
                    + "\nContent-Type: application/octet-stream\n\n")
            httpRequestBodyWriter.flush()

            // Write the file
            val inputStreamToFile = FileInputStream(file?.getPath() ?: null)
            var bytesRead: Int
            val dataBuffer = ByteArray(1024)
            while (inputStreamToFile.read(dataBuffer).also { bytesRead = it } != -1) {
                outputStreamToRequestBody.write(dataBuffer, 0, bytesRead)
            }
            outputStreamToRequestBody.flush()

            // End of the multipart request
            httpRequestBodyWriter.write("\n--$boundary--\n")
            httpRequestBodyWriter.flush()

            // Close the streams
            outputStreamToRequestBody.close()
            httpRequestBodyWriter.close()

            if (connection.responseCode == 200) {

                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                //val response = Gson().fromJson(inputStreamReader, CaffList::class.java)
                inputStreamReader.close()
                inputSystem.close()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

















