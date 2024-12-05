package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.databinding.ItemSkinTailChooseBinding

class AddSkinTailAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : ListAdapter<SkinData, AddSkinTailAdapter.AddShoeMakerViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableSetOf<SkinData>()

    inner class AddShoeMakerViewHolder(
        private val binding: ItemSkinTailChooseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: SkinData) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddShoeMakerViewHolder {
        return AddShoeMakerViewHolder(
            ItemSkinTailChooseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddShoeMakerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedItems(): List<SkinData> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SkinData>() {
            override fun areItemsTheSame(oldItem: SkinData, newItem: SkinData): Boolean {
                return oldItem.objectName == newItem.objectName
            }

            override fun areContentsTheSame(oldItem: SkinData, newItem: SkinData): Boolean {
                return oldItem == newItem
            }
        }
    }
}