package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.databinding.ItemShoeCheckerBinding

class ShoeCheckerAdapter : ListAdapter<PreparedShoeMakerData,ShoeCheckerAdapter.ShoeCheckerViewHolder>(ShoeCheckerDiffUtil) {
    private var onPassButtonClickedListener: ((PreparedShoeMakerData)-> Unit)? = null
    private var onDeclineButtonClickedListener: ((PreparedShoeMakerData)-> Unit)? = null

    fun setOnPassButtonClickedListener(l: (PreparedShoeMakerData)-> Unit){
        onPassButtonClickedListener = l
    }

    fun setOnDeclineButtonClickedListener(l: (PreparedShoeMakerData)-> Unit){
        onDeclineButtonClickedListener = l
    }

    object ShoeCheckerDiffUtil : DiffUtil.ItemCallback<PreparedShoeMakerData>() {
        override fun areItemsTheSame(oldItem: PreparedShoeMakerData, newItem: PreparedShoeMakerData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PreparedShoeMakerData, newItem: PreparedShoeMakerData): Boolean {
            return oldItem.skinType == newItem.skinType && oldItem.modelName == newItem.modelName && oldItem.countPair == newItem.countPair
        }

    }

    inner class ShoeCheckerViewHolder(private val binding: ItemShoeCheckerBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: PreparedShoeMakerData){
            binding.txtModelName.text = data.modelName
            binding.txtSkinName.text = "${data.color} ${data.skinType}"
            binding.txtCount.text = "${data.countPair}"

            binding.btnFinish.setOnClickListener {
                onPassButtonClickedListener?.invoke(data)
            }

            binding.btnDecline.setOnClickListener {
                onDeclineButtonClickedListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeCheckerViewHolder {
        return ShoeCheckerViewHolder(
            ItemShoeCheckerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_shoe_checker,parent,false))
        )
    }

    override fun onBindViewHolder(holder: ShoeCheckerViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}