package com.wahidabd.dicodingstories.view.page.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.databinding.ItemPostBinding
import com.wahidabd.dicodingstories.utils.convertDate
import com.wahidabd.dicodingstories.utils.setImage

class PostAdapter(private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var differCallback = object : DiffUtil.ItemCallback<PostModel>(){
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
            oldItem == newItem

    }

    private val listDiffer = AsyncListDiffer(this, differCallback)

    var setData: List<PostModel>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)


    private var listener: ((PostModel) -> Unit)? = null
    fun setOnItemClick(listener: (PostModel) -> Unit){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(setData[position], listener)
        holder.container.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fall_down)
    }

    override fun getItemCount(): Int = setData.size

    inner class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root){
        val container = binding.viewRoot
        fun bind(data: PostModel, listener: ((PostModel) -> Unit)?){
            with(binding){
                imgPicture.setImage(data.photoUrl)
                tvName.text = data.name
                tvCreated.convertDate(data.createdAt, context)

                viewRoot.setOnClickListener {
                    listener?.let { listener(data) }
                }
            }
        }
    }

}