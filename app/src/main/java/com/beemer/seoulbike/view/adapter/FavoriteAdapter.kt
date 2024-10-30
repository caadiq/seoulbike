package com.beemer.seoulbike.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.beemer.seoulbike.databinding.RowStationEmptyFavoriteBinding
import com.beemer.seoulbike.databinding.RowStationFavoriteBinding
import com.beemer.seoulbike.model.dto.StationListDto
import com.beemer.seoulbike.view.diff.StationListDiffUtil
import com.beemer.seoulbike.view.utils.UnitConversion.formatDistance

class FavoriteAdapter(private val listener: OnFavoriteClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnFavoriteClickListener {
        fun setOnFavoriteClick(item: StationListDto, lottie: LottieAnimationView)
    }

    private var itemList = mutableListOf<StationListDto>()
    private var onItemClickListener: ((StationListDto, Int) -> Unit)? = null

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_EMPTY = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = if (itemList.isEmpty()) 1 else itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_EMPTY) {
            val binding = RowStationEmptyFavoriteBinding.inflate(inflater, parent, false)
            EmptyViewHolder(binding)
        } else {
            val binding = RowStationFavoriteBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder && itemList.isNotEmpty()) {
            holder.bind(itemList[position])
        }
    }

    inner class ViewHolder(private val binding: RowStationFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION && itemList.isNotEmpty()) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: StationListDto) {
            binding.txtNo.text = item.stationNo.replace("^0+".toRegex(), "")
            binding.txtName.text = item.stationNm
            binding.txtDistance.text = item.distance?.let { formatDistance(it) }
            binding.txtQrBike.text = item.stationStatus.qrBikeCnt.toString()
            binding.txtElecBike.text = item.stationStatus.elecBikeCnt.toString()

            if (item.isFavorite) {
                if (binding.lottie.progress == 0.0f)
                    binding.lottie.playAnimation()
            } else {
                binding.lottie.progress = 0.0f
                binding.lottie.cancelAnimation()
            }

            binding.lottie.setOnClickListener {
                listener.setOnFavoriteClick(item, binding.lottie)
            }
        }
    }

    inner class EmptyViewHolder(binding: RowStationEmptyFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (StationListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<StationListDto>) {
        val diffCallBack = StationListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}
