package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonBinding

class ShoeColorAdapter : ListAdapter<ShoeColorData, ShoeColorAdapter.ShoeColorViewHolder>(ShoeColorDiffUtil) {
    private var onCategoryItemLongClickedListener: ((ShoeColorData)-> Unit)? = null


    fun setOnCategoryItemLongClickedListener(listener: (ShoeColorData)-> Unit){
        onCategoryItemLongClickedListener = listener
    }

    object ShoeColorDiffUtil : DiffUtil.ItemCallback<ShoeColorData>() {
        override fun areItemsTheSame(oldItem: ShoeColorData, newItem: ShoeColorData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoeColorData, newItem: ShoeColorData): Boolean {
            return oldItem.objectName == newItem.objectName && oldItem.colorName == newItem.colorName
        }

    }

    inner class ShoeColorViewHolder(private val binding: ItemCommonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ShoeColorData){
            binding.txtName.text = data.colorName
            itemView.setOnLongClickListener {
                onCategoryItemLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeColorViewHolder {
        return ShoeColorViewHolder(
            ItemCommonBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_common,parent,false)))
    }

    override fun onBindViewHolder(holder: ShoeColorViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}