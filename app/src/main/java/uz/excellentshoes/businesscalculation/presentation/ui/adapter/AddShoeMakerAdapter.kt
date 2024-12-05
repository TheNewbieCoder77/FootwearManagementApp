package uz.excellentshoes.businesscalculation.presentation.ui.adapter.AddShoeMakerAdapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.databinding.ItemShoeMakerChooseBinding

class AddShoeMakerAdapter(
    private val onSelectionChanged: (Int) -> Unit
) : ListAdapter<PreparedTailData, AddShoeMakerAdapter.AddShoeMakerViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableSetOf<PreparedTailData>()

    inner class AddShoeMakerViewHolder(
        private val binding: ItemShoeMakerChooseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: PreparedTailData) {
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
            ItemShoeMakerChooseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddShoeMakerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedItems(): List<PreparedTailData> {
        return selectedItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreparedTailData>() {
            override fun areItemsTheSame(oldItem: PreparedTailData, newItem: PreparedTailData): Boolean {
                return oldItem.objectName == newItem.objectName
            }

            override fun areContentsTheSame(oldItem: PreparedTailData, newItem: PreparedTailData): Boolean {
                return oldItem == newItem
            }
        }
    }
}