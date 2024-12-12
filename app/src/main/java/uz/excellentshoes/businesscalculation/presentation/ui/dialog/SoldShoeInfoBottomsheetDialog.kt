package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogSoldShoeInfoBinding

class SoldShoeInfoBottomsheetDialog(context: Context, private val data: SoldShoeData) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogSoldShoeInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogSoldShoeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnClose.setOnClickListener{
            dismiss()
        }
        binding.txtModelName.text = data.modelName
        binding.txtColorName.text = data.color
        "${data.countPair} ta".also { binding.txtPairCount.text = it }
        binding.txtSkinType.text = data.skinType
        val temp = data.addedTime.split(" ")
        binding.txtAddedTime.text = temp[0]
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}