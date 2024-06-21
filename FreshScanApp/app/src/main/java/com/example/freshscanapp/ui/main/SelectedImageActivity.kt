package com.example.freshscanapp.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.freshscanapp.R
import com.example.freshscanapp.data.api.ApiConfig
import com.example.freshscanapp.databinding.ActivitySelectedImageBinding
import com.example.freshscanapp.reduceFileImage
import com.example.freshscanapp.ui.main.recomendations.RecCabbageActivity
import com.example.freshscanapp.ui.main.recomendations.RecCarrotActivity
import com.example.freshscanapp.ui.main.recomendations.RecCucumberActivity
import com.example.freshscanapp.ui.main.recomendations.RecOkraActivity
import com.example.freshscanapp.ui.main.recomendations.RecPepperActivity
import com.example.freshscanapp.ui.main.recomendations.RecPotatoActivity
import com.example.freshscanapp.ui.main.recomendations.RecTomatoActivity
import com.example.freshscanapp.ui.main.recomendations.RottenActivity
import com.example.freshscanapp.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class SelectedImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectedImageBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.backgroundImage.setImageURI(imageUri)
            currentImageUri = imageUri
        }

        binding.scanButton.setOnClickListener { uploadImage() }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.backgroundImage.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("SelectedImageActivity", "Image file path: ${imageFile.path}")
            showLoading(true)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService2()
                    Log.d("SelectedImageActivity", "Starting API call")
                    val successResponse = apiService.scanImage(multipartBody)
                    Log.d("SelectedImageActivity", "API call successful: $successResponse")
                    with(successResponse.data) {
                        val resultText = String.format("It's %s\n%s", freshnessInfo, prediction)
                        Log.d("SelectedImageActivity", "Result text: $resultText")
                        binding.resultTextView.text = resultText

                        if (freshnessInfo.equals("rotten", ignoreCase = true)) {
                            binding.resultTextView.setTextColor(ContextCompat.getColor(this@SelectedImageActivity, R.color.red))
                        } else {
                            binding.resultTextView.setTextColor(ContextCompat.getColor(this@SelectedImageActivity, R.color.light_green))
                        }

                        // Check if freshnessInfo is "rotten"
                        if (freshnessInfo.equals("rotten", ignoreCase = true)) {
                            binding.rottenButton.visibility = View.VISIBLE
                            binding.rottenButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RottenActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            binding.rottenButton.visibility = View.GONE
                        }

                        // Check if prediction is "fresh_carrot"
                        if (prediction.equals("fresh_carrot", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecCarrotActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_tomato", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecTomatoActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_potato", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecPotatoActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_pepper", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecPepperActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_cucumber", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecCucumberActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_okra", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecOkraActivity::class.java)
                                startActivity(intent)
                            }
                        } else if (prediction.equals("fresh_cabbage", ignoreCase = true)) {
                            binding.freshButton.visibility = View.VISIBLE
                            binding.freshButton.setOnClickListener {
                                val intent = Intent(this@SelectedImageActivity, RecCabbageActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            binding.freshButton.visibility = View.GONE
                        }
                    }
                } catch (e: HttpException) {
                    Log.e("SelectedImageActivity", "HttpException: ${e.message}")
                    showToast(getString(R.string.upload_error))
                } catch (e: Exception) {
                    Log.e("SelectedImageActivity", "Exception: ${e.message}")
                    showToast(getString(R.string.upload_error))
                } finally {
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
