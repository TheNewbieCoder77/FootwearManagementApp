package uz.excellentshoes.businesscalculation.presentation.ui.adapter



import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.databinding.ItemCuttedSkinBinding

class SkinCutterAdapter : ListAdapter<SkinData,SkinCutterAdapter.SkinCutterViewHolder>(SkinCutterDiffUtil) {
    private var onCutSkinLongClickedListener: ((SkinData) -> Unit)? = null

    fun setOnCutSkinLongClickedListener(listener: (SkinData) -> Unit){
        onCutSkinLongClickedListener = listener
    }

    object SkinCutterDiffUtil : DiffUtil.ItemCallback<SkinData>() {
        override fun areItemsTheSame(oldItem: SkinData, newItem: SkinData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SkinData, newItem: SkinData): Boolean {
            return oldItem.skinType == newItem.skinType && oldItem.modelName == newItem.modelName && oldItem.countPair == newItem.countPair
        }

    }

    inner class SkinCutterViewHolder(private val binding: ItemCuttedSkinBinding) :RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: SkinData){
            binding.txtModelName.text = data.modelName
            binding.txtSkinName.text = "${data.color} ${data.skinType}"
            binding.txtCount.text = "${data.countPair}"

            itemView.setOnLongClickListener {
                onCutSkinLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinCutterViewHolder {
        return SkinCutterViewHolder(ItemCuttedSkinBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_cutted_skin,parent,false)))
    }

    override fun onBindViewHolder(holder: SkinCutterViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}