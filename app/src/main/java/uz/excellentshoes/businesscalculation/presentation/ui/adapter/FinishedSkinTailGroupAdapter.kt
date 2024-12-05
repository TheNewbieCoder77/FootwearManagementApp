package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonFinishedGroupBinding
import uz.excellentshoes.businesscalculation.utils.onFinishedSkinTailClicked

class FinishedSkinTailGroupAdapter(
    private val onItemClick: onFinishedSkinTailClicked
) : ListAdapter<Map.Entry<String, List<FinishedSkinTailData>>, FinishedSkinTailGroupAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: ItemCommonFinishedGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Map.Entry<String, List<FinishedSkinTailData>>) {
            binding.apply {
                txtName.text = group.key
                rvGroups.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = FinishedSkinTailDetailAdapter(onItemClick).apply {
                        submitList(group.value)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommonFinishedGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Map.Entry<String, List<FinishedSkinTailData>>>() {
        override fun areItemsTheSame(
            oldItem: Map.Entry<String, List<FinishedSkinTailData>>,
            newItem: Map.Entry<String, List<FinishedSkinTailData>>
        ): Boolean {
            return oldItem.key == newItem.key
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Map.Entry<String, List<FinishedSkinTailData>>,
            newItem: Map.Entry<String, List<FinishedSkinTailData>>
        ): Boolean {
            return oldItem.key == newItem.key && oldItem.value == newItem.value
        }
    }
}