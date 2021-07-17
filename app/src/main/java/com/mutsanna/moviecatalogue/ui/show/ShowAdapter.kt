package com.mutsanna.moviecatalogue.ui.show

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.R
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.databinding.ItemDataBinding
import com.mutsanna.moviecatalogue.ui.detail.DetailActivity

class ShowAdapter: PagedListAdapter<TvShowEntity, ShowAdapter.ShowViewHolder>(DIFF_CALLBACK) {
    inner class ShowViewHolder(private val binding: ItemDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShowEntity) {
            with(binding) {
                tvTitle.text = tvShow.title
                tvDate.text = itemView.context.getString(R.string.release_date_rv, tvShow.releaseDate)
                Glide.with(itemView.context)
                    .load(BuildConfig.IMAGE_BASE_URL+tvShow.poster)
                    .into(civPoster)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_TYPE, EXTRA_SHOW)
                intent.putExtra(DetailActivity.EXTRA_SHOW_ID, tvShow.showId)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val itemBinding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(getItem(position) as TvShowEntity)
    }

    companion object{
        const val EXTRA_SHOW = "show"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.showId == newItem.showId
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}