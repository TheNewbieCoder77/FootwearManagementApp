package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.databinding.DialogYesOrNoBinding

class ConfirmDialog : DialogFragment() {
    private var _binding: DialogYesOrNoBinding? = null
    private val binding get() = _binding!!
    private var onCancelButtonClickedListener: (()-> Unit)? = null
    private var onConfirmButtonClickedListener: (()-> Unit)? = null
    private var onDialogCanceledListener: (()-> Unit)? = null

    fun setOnDialogCanceledListener(listener: ()-> Unit){
        onDialogCanceledListener = listener
    }

    fun setOnCancelButtonClickedListener(listener: () -> Unit){
        onCancelButtonClickedListener = listener
    }

    fun setOnConfirmButtonClickedListener(listener: () -> Unit){
        onConfirmButtonClickedListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) // This ensures wrap content behavior
            setBackgroundDrawableResource(android.R.color.transparent) // Optional, to remove any default background
            setWindowAnimations(R.style.DialogAnimation) // Keep your animations if needed
        }
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE,R.style.DialogAnimation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogYesOrNoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.elevation = 8F
        val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(16,8,16,8)
        binding.root.layoutParams = params

        this.isCancelable = true

        binding.btnCancel.setOnClickListener {
            onCancelButtonClickedListener?.invoke()
        }

        binding.btnDo.setOnClickListener {
            onConfirmButtonClickedListener?.invoke()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogCanceledListener?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}