package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.databinding.ItemDeclinedShoeBinding

class DeclinedShoesAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : ListAdapter<DeclinedShoeData, DeclinedShoesAdapter.DeclinedShoesViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableSetOf<DeclinedShoeData>()

    inner class DeclinedShoesViewHolder(
        private val binding: ItemDeclinedShoeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: DeclinedShoeData) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeclinedShoesViewHolder {
        return DeclinedShoesViewHolder(
            ItemDeclinedShoeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeclinedShoesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedItems(): List<DeclinedShoeData> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DeclinedShoeData>() {
            override fun areItemsTheSame(oldItem: DeclinedShoeData, newItem: DeclinedShoeData): Boolean {
                return oldItem.objectName == newItem.objectName
            }

            override fun areContentsTheSame(oldItem: DeclinedShoeData, newItem: DeclinedShoeData): Boolean {
                return oldItem == newItem
            }
        }
    }
}