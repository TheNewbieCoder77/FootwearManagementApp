package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.databinding.ItemCuttedSkinBinding

class ProcessShoeMakerAdapter : ListAdapter<ProcessShoeMakerData, ProcessShoeMakerAdapter.ProcessShoeMakerViewHolder>(ProcessShoeMakerDiffUtil) {

    object ProcessShoeMakerDiffUtil : DiffUtil.ItemCallback<ProcessShoeMakerData>() {
        override fun areItemsTheSame(oldItem: ProcessShoeMakerData, newItem: ProcessShoeMakerData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProcessShoeMakerData, newItem: ProcessShoeMakerData): Boolean {
            return oldItem.skinType == newItem.skinType && oldItem.modelName == newItem.modelName && oldItem.countPair == newItem.countPair && oldItem.objectName == newItem.objectName
        }

    }

    inner class ProcessShoeMakerViewHolder(private val binding: ItemCuttedSkinBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ProcessShoeMakerData){
            binding.txtModelName.text = data.modelName
            binding.txtSkinName.text = "${data.color} ${data.skinType}"
            binding.txtCount.text = "${data.countPair}"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessShoeMakerViewHolder {
        return ProcessShoeMakerViewHolder(
            ItemCuttedSkinBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_cutted_skin,parent,false)))
    }

    override fun onBindViewHolder(holder: ProcessShoeMakerViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }

}