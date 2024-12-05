package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonSelectBinding

class SelectColorAdapter(
    private val onItemClicked: (ShoeColorData) -> Unit
) : ListAdapter<ShoeColorData, SelectColorAdapter.SelectColorViewHolder>(SelectColorDiffUtil) {
    private var selectedItemPosition: Int? = null


    object SelectColorDiffUtil : DiffUtil.ItemCallback<ShoeColorData>() {
        override fun areItemsTheSame(oldItem: ShoeColorData, newItem: ShoeColorData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoeColorData, newItem: ShoeColorData): Boolean {
            return oldItem.objectName == newItem.objectName
        }

    }

    inner class SelectColorViewHolder(private val binding: ItemCommonSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShoeColorData, isSelected: Boolean) {
            binding.txtName.text = data.colorName
            binding.root.background = if(isSelected){
                ContextCompat.getDrawable(binding.root.context,R.drawable.bg_item_selected)
            }else{
                ContextCompat.getDrawable(binding.root.context,R.drawable.bg_item_unselected)
            }

            binding.txtName.setTextColor(
                if(isSelected){
                    ContextCompat.getColor(binding.root.context, R.color.white)
                }else{
                    ContextCompat.getColor(binding.root.context, R.color.main_color)
                }
            )

            binding.root.setOnClickListener{
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = bindingAdapterPosition
                previousSelectedItemPosition?.let { notifyItemChanged(it) }
                notifyItemChanged(bindingAdapterPosition)
                onItemClicked(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectColorViewHolder {
        return SelectColorViewHolder(ItemCommonSelectBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_common_select,parent,false)))
    }

    override fun onBindViewHolder(holder: SelectColorViewHolder, position: Int) {
        val data = getItem(position)
        val isSelected = position == selectedItemPosition
        holder.bind(data,isSelected)
    }
}