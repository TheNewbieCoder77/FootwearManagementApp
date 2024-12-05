package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputCategoryBinding
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputFinishedMoneyBinding

class PayMoneyBottomsheetDialog(context: Context): BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputFinishedMoneyBinding? = null
    private val binding get() = _binding!!
    private var onPayButtonClicked:((paidMoney: String)-> Unit)? = null

    fun setonPayButtonClickedListener(l: (paidMoney: String)-> Unit){
        onPayButtonClicked = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputFinishedMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAdd.setOnClickListener {
            onPayButtonClicked?.invoke(binding.inputPayMoney.text.toString())
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}