package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputWarehouseProductBinding
import uz.excellentshoes.businesscalculation.databinding.ScreenWarehouseProductBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.WarehouseProductAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddWarehouseProductBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.WarehouseProductViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.WarehouseProductViewModelImpl

class WarehouseProductScreen: Fragment(R.layout.screen_warehouse_product) {
    private var _binding: ScreenWarehouseProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WarehouseProductViewModelImpl by viewModels<WarehouseProductViewModelImpl>()
    private lateinit var adapter: WarehouseProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenWarehouseProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllWarehouseProducts()
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showBottomSheetDialog(null)
        }
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.warehouseProductStateFlow.collect{
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = WarehouseProductAdapter()
        binding.recyclerUnits.apply {
            adapter = this@WarehouseProductScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnItemClickListener {
            showBottomSheetDialog(it)
        }
        adapter.setOnItemLongClickListener {
            viewModel.deleteWarehouseProduct(it.objectName)
        }



    }

    private fun showBottomSheetDialog(data: WarehouseProductData?){
        val dialog = AddWarehouseProductBottomsheetDialog(requireContext(),data,viewModel)
        dialog.setOnBtnEnterClickedListener {
            if(data != null){
                viewModel.editWarehouseProduct(it)
            }else{
                viewModel.addWarehouseProduct(it)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}