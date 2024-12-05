package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonBinding

class SelectSkinTypeAdapter : ListAdapter<SkinTypeData, SelectSkinTypeAdapter.SkinTypeViewHolder>(SkinTypeDiffUtil) {
    private var onCategoryItemLongClickedListener: ((SkinTypeData)-> Unit)? = null


    fun setOnCategoryItemLongClickedListener(listener: (SkinTypeData)-> Unit){
        onCategoryItemLongClickedListener = listener
    }

    object SkinTypeDiffUtil : DiffUtil.ItemCallback<SkinTypeData>() {
        override fun areItemsTheSame(oldItem: SkinTypeData, newItem: SkinTypeData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SkinTypeData, newItem: SkinTypeData): Boolean {
            return oldItem.objectName == newItem.objectName && oldItem.skinTypeName == newItem.skinTypeName
        }

    }

    inner class SkinTypeViewHolder(private val binding: ItemCommonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: SkinTypeData){
            binding.txtName.text = data.skinTypeName
            itemView.setOnLongClickListener {
                onCategoryItemLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinTypeViewHolder {
        return SkinTypeViewHolder(
            ItemCommonBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_common,parent,false)))
    }

    override fun onBindViewHolder(holder: SkinTypeViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}