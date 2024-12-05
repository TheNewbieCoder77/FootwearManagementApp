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

class AddCategoryBottomsheetDialog(private val context: Context) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputCategoryBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener:((CategoryItemData)-> Unit)? = null

    fun setOnAddButtonClickedListener(l: (CategoryItemData)-> Unit){
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddCategory.setOnClickListener {
            onAddButtonClickedListener?.invoke(CategoryItemData(
                categoryId = binding.inputCategoryId.text.toString().toInt(),
                categoryName = binding.inputCategoryName.text.toString()
            ))
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}