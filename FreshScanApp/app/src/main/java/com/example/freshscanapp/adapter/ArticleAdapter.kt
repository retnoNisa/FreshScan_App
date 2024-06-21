package com.example.freshscanapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshscanapp.R
import com.example.freshscanapp.data.api.ArticlesItem
import com.example.freshscanapp.databinding.ItemRowArticleBinding
import com.example.freshscanapp.ui.main.detail.DetailArticleActivity

class ArticleAdapter : ListAdapter<ArticlesItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        Glide.with(holder.imgArticle.context)
            .load(article.image)
            .into(holder.imgArticle)

        holder.binding.buttonReadMore.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("image", article.image)
                putExtra("description", article.detail)
                putExtra("source", article.source)
            }
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ItemRowArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgArticle: ImageView = itemView.findViewById(R.id.img_article)
        fun bind(article: ArticlesItem) {
            binding.titleArticle.text = article.title
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
