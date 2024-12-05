package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.DollarCurrencyData
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputCurrencyDollarBinding

class AddDollarCurrencyBottomsheetDialog(private val context: Context) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputCurrencyDollarBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener: ((DollarCurrencyData) -> Unit)? = null

    fun setOnAddButtonClickedListener(l: (DollarCurrencyData) -> Unit) {
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputCurrencyDollarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddCurrency.setOnClickListener {
            onAddButtonClickedListener?.invoke(
                DollarCurrencyData(
                    dollarCurrency = binding.inputCurrencyDollar.text.toString().toInt()
                )
            )
        }

    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}