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
import uz.excellentshoes.businesscalculation.databinding.ScreenSkinTypesBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.SelectSkinTypeAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddSkinTypeBottomSheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinTypeViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.SkinTypeViewModelImpl

class SkinTypesScreen : Fragment(R.layout.screen_skin_types) {
    private var _binding: ScreenSkinTypesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SelectSkinTypeAdapter
    private val viewModel: SkinTypeViewModel by viewModels<SkinTypeViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenSkinTypesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllSkinTypes()
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.skinTypeListStateFlow.collect{ dataList->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = SelectSkinTypeAdapter()
        binding.recyclerSkinTypes.apply {
            adapter = this@SkinTypesScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnCategoryItemLongClickedListener {
            viewModel.deleteSkinType(it.objectName)
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = AddSkinTypeBottomSheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addSkinType(it)
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}