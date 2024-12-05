package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.databinding.ItemUserPasswordBinding

class UserPasswordAdapter : ListAdapter<UserPasswordData, UserPasswordAdapter.UserPasswordViewHolder>(ShoeColorDiffUtil) {
    private var onItemLongClickedListener: ((UserPasswordData)-> Unit)? = null
    private var onItemClickedListener: ((UserPasswordData)-> Unit)? = null

    fun setOnItemLongClickedListener(listener: (UserPasswordData)-> Unit){
        onItemLongClickedListener = listener
    }

    fun setOnItemClickedListener(listener: (UserPasswordData)-> Unit){
        onItemClickedListener = listener
    }

    object ShoeColorDiffUtil : DiffUtil.ItemCallback<UserPasswordData>() {
        override fun areItemsTheSame(oldItem: UserPasswordData, newItem: UserPasswordData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserPasswordData, newItem: UserPasswordData): Boolean {
            return oldItem.objectName == newItem.objectName
        }

    }

    inner class UserPasswordViewHolder(private val binding: ItemUserPasswordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: UserPasswordData){
            binding.txtName.text = data.userName
            binding.txtPassword.text = data.userPassword
            itemView.setOnLongClickListener {
                onItemLongClickedListener?.invoke(data)
                true
            }
            itemView.setOnClickListener {
                onItemClickedListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPasswordViewHolder {
        return UserPasswordViewHolder(
            ItemUserPasswordBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user_password,parent,false)))
    }

    override fun onBindViewHolder(holder: UserPasswordViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }
}