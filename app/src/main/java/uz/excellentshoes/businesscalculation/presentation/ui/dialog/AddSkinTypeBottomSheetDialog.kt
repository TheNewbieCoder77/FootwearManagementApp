package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputSkinTypeBinding

class AddSkinTypeBottomSheetDialog(private val context: Context) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputSkinTypeBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener: ((SkinTypeData) -> Unit)? = null

    fun setOnAddButtonClickedListener(l: (SkinTypeData) -> Unit) {
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputSkinTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddSkinType.setOnClickListener {
            onAddButtonClickedListener?.invoke(
                SkinTypeData(
                    skinTypeName = binding.inputSkinName.text.toString()
                )
            )
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}