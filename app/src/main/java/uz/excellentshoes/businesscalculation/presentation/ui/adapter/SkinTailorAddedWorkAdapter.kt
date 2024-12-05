package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.databinding.ItemCuttedSkinBinding

class SkinTailorAddedWorkAdapter : ListAdapter<ProcessTailData, SkinTailorAddedWorkAdapter.SkinTailorAddedWorkViewHolder>(SkinTailorAddedWorkDiffUtil) {

    object SkinTailorAddedWorkDiffUtil : DiffUtil.ItemCallback<ProcessTailData>() {
        override fun areItemsTheSame(oldItem: ProcessTailData, newItem: ProcessTailData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProcessTailData, newItem: ProcessTailData): Boolean {
            return oldItem.skinType == newItem.skinType && oldItem.modelName == newItem.modelName && oldItem.countPair == newItem.countPair && oldItem.objectName == newItem.objectName
        }

    }

    inner class SkinTailorAddedWorkViewHolder(private val binding: ItemCuttedSkinBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ProcessTailData){
            binding.txtModelName.text = data.modelName
            binding.txtSkinName.text = "${data.color} ${data.skinType}"
            binding.txtCount.text = "${data.countPair}"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinTailorAddedWorkViewHolder {
        return SkinTailorAddedWorkViewHolder(
            ItemCuttedSkinBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_cutted_skin,parent,false)))
    }

    override fun onBindViewHolder(holder: SkinTailorAddedWorkViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }

}