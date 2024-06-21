package com.example.freshscanapp.ui.main.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.freshscanapp.R
import com.example.freshscanapp.data.api.ApiConfig
import com.example.freshscanapp.data.api.Vegetable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailVeggiesActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imgDesc: ImageView
    private lateinit var tvStep1: TextView
    private lateinit var tvStep2: TextView
    private lateinit var tvStep3: TextView
    private lateinit var tvStep4: TextView
    private lateinit var tvStep5: TextView
    private lateinit var tvStep6: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_veggies)

        tvName = findViewById(R.id.tv_name)
        tvDescription = findViewById(R.id.tv_description)
        imgDesc = findViewById(R.id.img_desc)
        tvStep1 = findViewById(R.id.tv_step1)
        tvStep2 = findViewById(R.id.tv_step2)
        tvStep3 = findViewById(R.id.tv_step3)
        tvStep4 = findViewById(R.id.tv_step4)
        tvStep5 = findViewById(R.id.tv_step5)
        tvStep6 = findViewById(R.id.tv_step6)
        progressBar = findViewById(R.id.progressBar)

        val vegetableType = intent.getStringExtra("VEGETABLE_TYPE")
        vegetableType?.let { fetchVegetableDetails(it) }
    }

    private fun fetchVegetableDetails(vegetableType: String) {
        // Menampilkan progress bar saat memuat data
        progressBar.visibility = ProgressBar.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val response = when (vegetableType) {
                "carrot" -> ApiConfig.getApiService1().getCarrotDetail()
                "potato" -> ApiConfig.getApiService1().getPotatoDetail()
                "tomato" -> ApiConfig.getApiService1().getTomatoDetail()
                "pepper" -> ApiConfig.getApiService1().getPepperDetail()
                "cucumber" -> ApiConfig.getApiService1().getCucumberDetail()
                "okra" -> ApiConfig.getApiService1().getOkraDetail()
                "cabbage" -> ApiConfig.getApiService1().getCabbageDetail()
                else -> null
            }
            response?.let {
                if (it.isSuccessful) {
                    val vegetable = it.body()?.vegetable
                    vegetable?.let { veg ->
                        withContext(Dispatchers.Main) {
                            updateUI(veg)
                            progressBar.visibility = ProgressBar.GONE
                        }
                    }
                } else {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    private fun updateUI(vegetable: Vegetable) {
        tvName.text = vegetable.name
        tvDescription.text = vegetable.detail
        Glide.with(this).load(vegetable.image).into(imgDesc)
        tvStep1.text = vegetable.step1
        tvStep2.text = vegetable.step2
        tvStep3.text = vegetable.step3
        tvStep4.text = vegetable.step4
        tvStep5.text = vegetable.step5
        tvStep6.text = vegetable.step6
    }
}
