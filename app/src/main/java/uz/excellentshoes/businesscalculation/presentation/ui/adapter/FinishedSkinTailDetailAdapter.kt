package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonFinishedDetailsBinding
import uz.excellentshoes.businesscalculation.utils.onFinishedSkinTailClicked

class FinishedSkinTailDetailAdapter(
    private val onItemClick: onFinishedSkinTailClicked
) : ListAdapter<FinishedSkinTailData, FinishedSkinTailDetailAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: ItemCommonFinishedDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: FinishedSkinTailData) {
            binding.apply {
                root.setOnLongClickListener {
                    onItemClick.onClick(data)
                    true
                }

                tvModelAndColor.text = "${data.modelName} ${data.color}"
                tvCountPair.text = "${data.countPair} ta"
                tvSkinType.text = data.skinType
                tvAddedTime.text = data.addedTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommonFinishedDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FinishedSkinTailData>() {
        override fun areItemsTheSame(
            oldItem: FinishedSkinTailData,
            newItem: FinishedSkinTailData
        ): Boolean = oldItem.objectName == newItem.objectName

        override fun areContentsTheSame(
            oldItem: FinishedSkinTailData,
            newItem: FinishedSkinTailData
        ): Boolean = oldItem == newItem
    }
}