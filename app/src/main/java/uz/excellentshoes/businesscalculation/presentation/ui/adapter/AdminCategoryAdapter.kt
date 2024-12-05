package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.databinding.ItemAdminCategoriesBinding

class AdminCategoryAdapter : ListAdapter<CategoryItemData, AdminCategoryAdapter.AdminCategoryViewHolder>(AdminCategoryDiffUtil) {
    private var onCategoryItemClickedListener: ((CategoryItemData) -> Unit)? = null
    private var onCategoryItemLongClickedListener: ((CategoryItemData)-> Unit)? = null

    fun setOnCategoryItemClickedListener(listener: (CategoryItemData) -> Unit){
        onCategoryItemClickedListener = listener
    }

    fun setOnCategoryItemLongClickedListener(listener: (CategoryItemData)-> Unit){
        onCategoryItemLongClickedListener = listener
    }

    object AdminCategoryDiffUtil : DiffUtil.ItemCallback<CategoryItemData>() {
        override fun areItemsTheSame(oldItem: CategoryItemData, newItem: CategoryItemData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryItemData, newItem: CategoryItemData): Boolean {
            return oldItem.categoryId == newItem.categoryId && oldItem.categoryName == newItem.categoryName
        }

    }

    inner class AdminCategoryViewHolder(private val binding: ItemAdminCategoriesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: CategoryItemData){
            binding.categoryName.text = data.categoryName
            itemView.setOnClickListener {
                onCategoryItemClickedListener?.invoke(data)
            }
            itemView.setOnLongClickListener {
                onCategoryItemLongClickedListener?.invoke(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCategoryViewHolder {
        return AdminCategoryViewHolder(
            ItemAdminCategoriesBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_admin_categories,parent,false)))
    }

    override fun onBindViewHolder(holder: AdminCategoryViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}