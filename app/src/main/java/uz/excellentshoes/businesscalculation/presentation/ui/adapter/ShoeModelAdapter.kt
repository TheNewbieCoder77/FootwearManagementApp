package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonBinding

class ShoeModelAdapter : ListAdapter<ShoeModelData, ShoeModelAdapter.ShoeModelViewHolder>(ShoeModelDiffUtil) {
    private var onCategoryItemLongClickedListener: ((ShoeModelData)-> Unit)? = null


    fun setOnCategoryItemLongClickedListener(listener: (ShoeModelData)-> Unit){
        onCategoryItemLongClickedListener = listener
    }

    object ShoeModelDiffUtil : DiffUtil.ItemCallback<ShoeModelData>() {
        override fun areItemsTheSame(oldItem: ShoeModelData, newItem: ShoeModelData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoeModelData, newItem: ShoeModelData): Boolean {
            return oldItem.objectName == newItem.objectName && oldItem.shoeModelName == newItem.shoeModelName
        }

    }

    inner class ShoeModelViewHolder(private val binding: ItemCommonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ShoeModelData){
            binding.txtName.text = data.shoeModelName
            itemView.setOnLongClickListener {
                onCategoryItemLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeModelViewHolder {
        return ShoeModelViewHolder(
            ItemCommonBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_common,parent,false)))
    }

    override fun onBindViewHolder(holder: ShoeModelViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}