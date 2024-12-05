package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonFinishedDetailsBinding
import uz.excellentshoes.businesscalculation.utils.onFinishedSkinCutClicked

class FinishedSkinCutDetailAdapter(
    private val onItemClick: onFinishedSkinCutClicked
) : ListAdapter<FinishedSkinCutData, FinishedSkinCutDetailAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: ItemCommonFinishedDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: FinishedSkinCutData) {
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

    companion object DiffCallback : DiffUtil.ItemCallback<FinishedSkinCutData>() {
        override fun areItemsTheSame(
            oldItem: FinishedSkinCutData,
            newItem: FinishedSkinCutData
        ): Boolean = oldItem.objectName == newItem.objectName

        override fun areContentsTheSame(
            oldItem: FinishedSkinCutData,
            newItem: FinishedSkinCutData
        ): Boolean = oldItem == newItem
    }
}
