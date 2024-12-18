package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputWarehouseProductBinding
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.WarehouseProductViewModelImpl
import uz.excellentshoes.businesscalculation.utils.toEditable
import java.util.Locale

class AddWarehouseProductBottomsheetDialog(private val context: Context, private val data: WarehouseProductData?,
     private val viewModel: WarehouseProductViewModelImpl) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {
    private var _binding: BottomsheetDialogInputWarehouseProductBinding? = null
    private val binding get() = _binding!!
    private var onBtnEnterClickedListener: ((WarehouseProductData) -> Unit)? = null
    private var selectedUnit = ""

    fun setOnBtnEnterClickedListener(l: (WarehouseProductData) -> Unit) {
        onBtnEnterClickedListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = BottomsheetDialogInputWarehouseProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        setSpinner()
        clickEvents()
        setValues()
        viewModel.getAllUnits()
    }

    @SuppressLint("SetTextI18n")
    private fun setValues(){
        data?.let {
            binding.inputProductName.text = it.productName.toEditable()
            binding.inputProductColor.text = it.productColor.toEditable()
            binding.inputProductCount.text = it.productCount.toString().toEditable()
            binding.inputProductPrice.text = String.format(Locale.getDefault(),"%.2f",it.productPrice).toEditable()
            binding.inputAnother.text = it.anotherInfo.toEditable()
        }
    }

    private fun setSpinner(){
        lifecycleScope.launch {
            viewModel.unitsList.collect { units ->
                val spinnerItems = units.map { it.unitName }
                val adapter = ArrayAdapter(context,R.layout.spinner_item,spinnerItems)
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.spinnerUnit.adapter = adapter

                binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedUnit = spinnerItems[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }
            }
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener{
            val moneyAmount = binding.inputProductPrice.text.toString().replace(",",".").toDouble()

            val tempData = if(data == null){
                WarehouseProductData(
                    productName =  binding.inputProductName.text.toString(),
                    productColor = binding.inputProductColor.text.toString(),
                    productCount = binding.inputProductCount.text.toString().toInt(),
                    productPrice = moneyAmount,
                    productUnit = selectedUnit,
                    anotherInfo = binding.inputAnother.text.toString()
                    )
            }else{
                WarehouseProductData(
                    objectName = data.objectName,
                    productName =  binding.inputProductName.text.toString(),
                    productColor = binding.inputProductColor.text.toString(),
                    productCount = binding.inputProductCount.text.toString().toInt(),
                    productPrice = moneyAmount,
                    productUnit = selectedUnit,
                    anotherInfo = binding.inputAnother.text.toString()
                )
            }
            onBtnEnterClickedListener?.invoke(tempData)
        }



    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}