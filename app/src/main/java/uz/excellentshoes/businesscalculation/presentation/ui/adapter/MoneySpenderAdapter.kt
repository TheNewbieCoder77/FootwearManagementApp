package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData
import uz.excellentshoes.businesscalculation.databinding.ItemMoneySpendBinding
import java.util.Locale

class MoneySpenderAdapter : ListAdapter<MoneySpendData, MoneySpenderAdapter.MoneySpenderViewHolder>(MoneySpenderDiffUtil) {

    object MoneySpenderDiffUtil : DiffUtil.ItemCallback<MoneySpendData>() {
        override fun areItemsTheSame(oldItem: MoneySpendData, newItem: MoneySpendData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MoneySpendData, newItem: MoneySpendData): Boolean {
            return oldItem.spendType == newItem.spendType && oldItem.moneyAmount == newItem.moneyAmount && oldItem.kontragentName == newItem.kontragentName
                    && oldItem.objectName == newItem.objectName
        }

    }

    inner class MoneySpenderViewHolder(private val binding: ItemMoneySpendBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: MoneySpendData){
            binding.inputKontragentText.text = data.kontragentName
            binding.inputSpendReasonText.text = data.comment
            binding.inputStatusText.text = data.spendType
            val color = when(data.spendType){
                "To'langan" -> ContextCompat.getColor(itemView.context,R.color.on_progress_color)
                "To'lanishi kerak" -> ContextCompat.getColor(itemView.context,R.color.decline_border_color)
                "Olindi" -> ContextCompat.getColor(itemView.context,R.color.on_progress_color)
                else -> ContextCompat.getColor(itemView.context,R.color.decline_border_color)
            }
            binding.inputStatusText.setTextColor(color)
            val formattedAmount = getDecimalFormat().format(data.moneyAmount)
            binding.inputMoneyAmountText.text = formattedAmount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneySpenderViewHolder {
        val binding = ItemMoneySpendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoneySpenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoneySpenderViewHolder, position: Int) {
        val data =getItem(position)
        holder.bind(data)
    }

    private fun getDecimalFormat(): DecimalFormat{
        return DecimalFormat("#,###.0", DecimalFormatSymbols(Locale.getDefault()))
    }
}