package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputCategoryBinding
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputShoeColorBinding

class AddShoeColorBottomsheetDialog(private val context: Context) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputShoeColorBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener: ((ShoeColorData) -> Unit)? = null

    fun setOnAddButtonClickedListener(l: (ShoeColorData) -> Unit) {
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputShoeColorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddColor.setOnClickListener {
            onAddButtonClickedListener?.invoke(
                ShoeColorData(
                    colorName = binding.inputColorName.text.toString()
                )
            )
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}