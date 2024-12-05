package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputUserPasswordBinding

class AddUserPasswordBottomsheetDialog(context: Context, private val data: UserPasswordData?) : BottomSheetDialog(context) {
    private var _binding: BottomsheetDialogInputUserPasswordBinding? = null
    private val binding get() = _binding!!
    private var onAddButtonClickedListener:((UserPasswordData)-> Unit)? = null

    fun setOnAddButtonClickedListener(l: (UserPasswordData)-> Unit){
        onAddButtonClickedListener = l
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        setValues()
        clickEvents()
    }

    @SuppressLint("SetTextI18n")
    private fun setValues(){
        data?.let {
            binding.inputUserName.setText(it.userName)
            binding.inputUserPassword.setText(it.userPassword)
            binding.inputJobPosition.setText(it.jobPosition.toString())
        }
    }

    private fun clickEvents(){
        binding.btnAddUserData.setOnClickListener {
            val temp = UserPasswordData(
                objectName = data?.objectName ?: "",
                userName = binding.inputUserName.text.toString(),
                userPassword = binding.inputUserPassword.text.toString(),
                jobPosition = binding.inputJobPosition.text.toString().toInt()
            )
            onAddButtonClickedListener?.invoke(temp)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}