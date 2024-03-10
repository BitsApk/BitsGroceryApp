package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.LOGGER
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.model.BannerData
import com.bitspanindia.groceryapp.data.model.VideoData
import com.bitspanindia.groceryapp.databinding.ItemBannerImageBinding
import com.bitspanindia.groceryapp.databinding.ItemVideoBannerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class VideoBannerAdapter(
    private val videoList: List<VideoData>,
    private val exoplayer: ExoPlayer?,
    private val context: Context
): RecyclerView.Adapter<VideoBannerAdapter.ViewHolder>() {

    private var currentVisiblePosition = -1

    inner class ViewHolder(val binding: ItemVideoBannerBinding): RecyclerView.ViewHolder(binding.root) {

        init {
        }

        fun bind(video: VideoData) {

            if (absoluteAdapterPosition == 0) {
                exoplayer?.seekTo(0)
                exoplayer?.setMediaItem(MediaItem.fromUri(video.video ?: ""))
                binding.playerView.player = exoplayer
            }

//            val mediaItem = MediaItem.Builder().setUri(Uri.parse(videoUrl)).build()
//            player.setMediaItem(mediaItem)

        }
    }

//    override fun onViewAttachedToWindow(holder: ViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        val position = holder.absoluteAdapterPosition
//        if (currentVisiblePosition != position) {
//            exoPlayer?.seekTo(0)
//            holder.binding.playerView.player = exoPlayer
//            exoPlayer?.playWhenReady = true
//            Log.d("Rishabh", "pos: $position, current pos: $currentVisiblePosition")
//            currentVisiblePosition = position
//        }
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = videoList[position]
        holder.bind(item)

    }


    override fun getItemCount(): Int = videoList.size
}