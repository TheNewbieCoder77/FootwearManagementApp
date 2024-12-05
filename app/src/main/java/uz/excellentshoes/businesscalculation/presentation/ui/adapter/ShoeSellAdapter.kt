package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.databinding.ItemShoeSellBinding

class ShoeSellAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : ListAdapter<PreparedToSailData, ShoeSellAdapter.ShoeSellViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableSetOf<PreparedToSailData>()

    inner class ShoeSellViewHolder(
        private val binding: ItemShoeSellBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: PreparedToSailData) {
            binding.apply {
                txtModelName.text = data.modelName
                txtCount.text = "${data.countPair} ta"
                txtSkinName.text = "${data.color} ${data.skinType}"

                // Set checkbox state without triggering listener
                btnCheckBox.setOnCheckedChangeListener(null)
                btnCheckBox.isChecked = selectedItems.contains(data)

                // Set new listener
                btnCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedItems.add(data)
                    } else {
                        selectedItems.remove(data)
                    }
                    onSelectionChanged(selectedItems.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeSellViewHolder {
        return ShoeSellViewHolder(
            ItemShoeSellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoeSellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedItems(): List<PreparedToSailData> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreparedToSailData>() {
            override fun areItemsTheSame(oldItem: PreparedToSailData, newItem: PreparedToSailData): Boolean {
                return oldItem.objectName == newItem.objectName
            }

            override fun areContentsTheSame(oldItem: PreparedToSailData, newItem: PreparedToSailData): Boolean {
                return oldItem == newItem
            }
        }
    }
}