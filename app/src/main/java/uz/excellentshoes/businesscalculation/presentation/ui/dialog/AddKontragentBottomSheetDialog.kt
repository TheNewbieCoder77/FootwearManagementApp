package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputKontragentBinding

class AddKontragentBottomSheetDialog(private val context: Context) : BottomSheetDialog(context){
    private var _binding: BottomsheetDialogInputKontragentBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener: ((KontragentData) -> Unit)? = null

    fun setOnAddButtonClickedListener(l: (KontragentData) -> Unit) {
        onAddButtonClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputKontragentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        binding.btnAddKontragent.setOnClickListener {
            onAddButtonClickedListener?.invoke(
                KontragentData(
                    kontragentName = binding.inputKontragentName.text.toString()
                )
            )
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}