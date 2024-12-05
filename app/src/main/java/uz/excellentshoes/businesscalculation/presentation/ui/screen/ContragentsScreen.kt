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
import uz.excellentshoes.businesscalculation.databinding.ScreenContragentsBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.KontragentAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddKontragentBottomSheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.KontragentViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.KontragentViewModelImpl

class ContragentsScreen: Fragment(R.layout.screen_contragents) {
    private var _binding: ScreenContragentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: KontragentAdapter
    private val viewModel: KontragentViewModel by viewModels<KontragentViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenContragentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllKontragent()
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.kontragentListStateFlow.collect{ dataList->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = KontragentAdapter()
        binding.recyclerKontragent.apply {
            adapter = this@ContragentsScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnCategoryItemLongClickedListener {
            viewModel.deleteKontragent(it.objectName)
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = AddKontragentBottomSheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addKontragent(it)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}