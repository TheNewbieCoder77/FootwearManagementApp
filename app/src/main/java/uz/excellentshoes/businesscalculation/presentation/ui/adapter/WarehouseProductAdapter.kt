package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData
import uz.excellentshoes.businesscalculation.databinding.ItemProductAddBinding

class WarehouseProductAdapter : ListAdapter<WarehouseProductData, WarehouseProductAdapter.WarehouseProductViewHolder>(DIFF_CALLBACK) {
    private var onItemClickListener: ((WarehouseProductData) -> Unit)? = null
    private var onItemLongClickListener: ((WarehouseProductData) -> Unit)? = null


    fun setOnItemClickListener(listener: (WarehouseProductData) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (WarehouseProductData) -> Unit) {
        onItemLongClickListener = listener
    }

    inner class WarehouseProductViewHolder(private val binding: ItemProductAddBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: WarehouseProductData) {
            binding.txtProductName.text = data.productName + " " + data.productColor
            binding.txtCount.text = "${data.productCount}"
            binding.txtUnit.text = data.productUnit
            if(data.anotherInfo.isNotEmpty()) binding.txtAnother.text = data.anotherInfo
            itemView.setOnClickListener {
                onItemClickListener?.invoke(data)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseProductViewHolder {
        return WarehouseProductViewHolder(
            ItemProductAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WarehouseProductViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<WarehouseProductData>(){
        override fun areItemsTheSame(oldItem: WarehouseProductData, newItem: WarehouseProductData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WarehouseProductData, newItem: WarehouseProductData): Boolean {
            return oldItem.productName == newItem.productName && oldItem.productCount == newItem.productCount
        }
    }

}