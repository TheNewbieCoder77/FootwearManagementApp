package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.databinding.ItemSoldShoeBinding

class SoldShoesAdapter : ListAdapter<SoldShoeData, SoldShoesAdapter.SoldShoesViewHolder>(SoldShoesDiffUtil) {
    private var onSoldShoeItemLongClickedListener: ((SoldShoeData)-> Unit)? = null
    private var onSoldShoeItemClickedListener: ((SoldShoeData)-> Unit)? = null

    fun setOnSoldShoeItemLongClickedListener(listener: (SoldShoeData)-> Unit){
        onSoldShoeItemLongClickedListener = listener
    }

    fun setOnSoldShoeItemClickedListener(listener: (SoldShoeData)-> Unit){
        onSoldShoeItemClickedListener = listener
    }

    object SoldShoesDiffUtil : DiffUtil.ItemCallback<SoldShoeData>() {
        override fun areItemsTheSame(oldItem: SoldShoeData, newItem: SoldShoeData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SoldShoeData, newItem: SoldShoeData): Boolean {
            return oldItem.objectName == newItem.objectName && oldItem.skinType == newItem.skinType
        }

    }

    inner class SoldShoesViewHolder(private val binding: ItemSoldShoeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: SoldShoeData){
            binding.txtModelName.text = data.modelName
            binding.txtSkinName.text = data.skinType
            "${data.countPair} ta".also { binding.txtCount.text = it }

            itemView.setOnLongClickListener{
                onSoldShoeItemLongClickedListener?.invoke(data)
                true
            }

            itemView.setOnClickListener {
                onSoldShoeItemClickedListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldShoesViewHolder {
        return SoldShoesViewHolder(
            ItemSoldShoeBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_sold_shoe,parent,false)))
    }

    override fun onBindViewHolder(holder: SoldShoesViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}