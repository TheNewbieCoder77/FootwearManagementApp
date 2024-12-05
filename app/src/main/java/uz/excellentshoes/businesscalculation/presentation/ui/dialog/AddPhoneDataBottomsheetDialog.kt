package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputCurrencyDollarBinding
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputPhonenumberBinding

class AddPhoneDataBottomsheetDialog(private val context: Context) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputPhonenumberBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener: ((PhoneData) -> Unit)? = null

    fun setOnAddButtonClickedListener(l: (PhoneData)-> Unit){
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputPhonenumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddPhone.setOnClickListener {
            onAddButtonClickedListener?.invoke(
                PhoneData(
                    jobPosition = binding.inputJobPositionId.text.toString().toInt(),
                    phoneNumber = binding.inputPhoneNumber.text.toString()
                ))
        }
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }


}