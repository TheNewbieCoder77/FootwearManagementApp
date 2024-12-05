package uz.excellentshoes.businesscalculation.presentation.ui.screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.databinding.ScreenAddSkinTailBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.AddSkinTailAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinTailorViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.SkinTailorViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show

class AddSkinTailScreen : Fragment(R.layout.screen_add_skin_tail) {
    private var _binding: ScreenAddSkinTailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SkinTailorViewModel by viewModels<SkinTailorViewModelImpl>()
    private val adapter by lazy {
        AddSkinTailAdapter{ selectedCount->
            updateUIBasedOnSelection(selectedCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenAddSkinTailBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        clickEvents()
        setObservers()
        getDataList()
        viewModel.getSkinDataList()
    }

    private fun getDataList(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.skinDataFlow.collect { skinDataList->
                    if(skinDataList.isEmpty()){
                        binding.txtPlaceHolder.show()
                        binding.recyclerSkins.hide()
                    }else{
                        binding.txtPlaceHolder.hide()
                        binding.recyclerSkins.show()
                    }
                    adapter.submitList(skinDataList)
                }
            }
        }
    }

    private fun clickEvents(){
        binding.btnChoose.isEnabled = false
        binding.btnChoose.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            val processTailDataList = selectedItems.map { item->
                ProcessTailData(
                    modelName = item.modelName,
                    color = item.color,
                    countPair = item.countPair,
                    skinType = item.skinType,
                    comment = item.comment
                )
            }
            selectedItems.forEach {
                viewModel.deleteSkinData(it.objectName)
            }
            viewModel.addProcessDataList(processTailDataList)
            adapter.clearSelections()
        }
    }

    private fun setAdapter(){
        binding.recyclerSkins.apply {
            adapter = this@AddSkinTailScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setObservers(){
        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            binding.progressBar.show()
            binding.recyclerSkins.hide()
        }

        viewModel.addProcessResult.observe(viewLifecycleOwner){ isSuccess->
            if(isSuccess){
                Toast.makeText(requireContext(), "SuccessFully added!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun updateUIBasedOnSelection(selectedCount: Int){
        binding.btnChoose.isEnabled = selectedCount > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}