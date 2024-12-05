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
import uz.excellentshoes.businesscalculation.databinding.ScreenShoeModelsBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ShoeModelAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddShoeModelBottomSheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeModelViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.ShoeModelViewModelImpl

class ShoeModelsScreen : Fragment(R.layout.screen_shoe_models) {
    private var _binding: ScreenShoeModelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShoeModelAdapter
    private val viewModel: ShoeModelViewModel by viewModels<ShoeModelViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenShoeModelsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllShoeModels()
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.shoeModelsListStateFlow.collect{ dataList->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = ShoeModelAdapter()
        binding.recyclerShoeModels.apply {
            adapter = this@ShoeModelsScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnCategoryItemLongClickedListener {
            viewModel.delete(it.objectName)
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = AddShoeModelBottomSheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addShoeModel(it)
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}