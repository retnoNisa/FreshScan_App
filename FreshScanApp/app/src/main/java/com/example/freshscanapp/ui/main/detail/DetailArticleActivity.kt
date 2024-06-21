package com.example.freshscanapp.ui.main.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.freshscanapp.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val viewModel: DetailArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val description = intent.getStringExtra("description")
        val source = intent.getStringExtra("source")

        binding.articleTitle.text = title
        Glide.with(this)
            .load(image)
            .into(binding.imgDesc)
        binding.description.text = description
        binding.source.text = source

        binding.source.setOnClickListener {
            source?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        title?.let {
            viewModel.loadArticleDetail(it)
        }
    }
}
