package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.databinding.ItemCommonBinding

class KontragentAdapter : ListAdapter<KontragentData, KontragentAdapter.KontragentViewHolder>(KontragentDiffUtil) {
    private var onCategoryItemLongClickedListener: ((KontragentData)-> Unit)? = null


    fun setOnCategoryItemLongClickedListener(listener: (KontragentData)-> Unit){
        onCategoryItemLongClickedListener = listener
    }

    object KontragentDiffUtil : DiffUtil.ItemCallback<KontragentData>() {
        override fun areItemsTheSame(oldItem: KontragentData, newItem: KontragentData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: KontragentData, newItem: KontragentData): Boolean {
            return oldItem.objectName == newItem.objectName && oldItem.kontragentName == newItem.kontragentName
        }

    }

    inner class KontragentViewHolder(private val binding: ItemCommonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: KontragentData){
            binding.txtName.text = data.kontragentName
            itemView.setOnLongClickListener {
                onCategoryItemLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontragentViewHolder {
        return KontragentViewHolder(
            ItemCommonBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_common,parent,false)))
    }

    override fun onBindViewHolder(holder: KontragentViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}