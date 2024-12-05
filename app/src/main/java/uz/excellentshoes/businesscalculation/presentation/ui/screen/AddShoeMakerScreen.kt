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
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.databinding.ScreenAddShoeMakerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.AddShoeMakerAdapter.AddShoeMakerAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.AddSkinTailAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeMakerViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.ShoeMakerViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show

class AddShoeMakerScreen : Fragment(R.layout.screen_add_shoe_maker) {
    private var _binding: ScreenAddShoeMakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShoeMakerViewModel by viewModels<ShoeMakerViewModelImpl>()
    private val adapter by lazy{
        AddShoeMakerAdapter{ selectedCount->
            updateUIBasedOnSelection(selectedCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenAddShoeMakerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        clickEvents()
        setObservers()
        getDataList()
        viewModel.getPreparedTailDataList()
    }

    private fun getDataList(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.preparedTailDataFlow.collect { dataList->
                    if(dataList.isEmpty()){
                        binding.txtPlaceHolder.show()
                        binding.containerShoeMaker.hide()
                    }else{
                        binding.txtPlaceHolder.hide()
                        binding.containerShoeMaker.show()
                    }
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun clickEvents(){
        binding.btnChoose.isEnabled = false
        binding.btnChoose.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            val processShoeMakerDataList = selectedItems.map { itemAdded->
                ProcessShoeMakerData(
                    modelName = itemAdded.modelName,
                    color = itemAdded.color,
                    countPair = itemAdded.countPair,
                    skinType = itemAdded.skinType,
                    comment = itemAdded.comment
                )
            }
            selectedItems.forEach { item->
                viewModel.deletePreparedTailData(item.objectName)
            }
            viewModel.addProcessShoeMakerDataList(processShoeMakerDataList)
            adapter.clearSelections()
        }
    }

    private fun setAdapter(){
        binding.recyclerTail.apply {
            adapter = this@AddShoeMakerScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setObservers(){
        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            binding.progressBar.show()
            binding.containerShoeMaker.hide()
        }

        viewModel.addProcessShoeMakerDataResult.observe(viewLifecycleOwner){ isSuccess->
            if(isSuccess){
                Toast.makeText(requireContext(), "SuccessFully added!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

    }

    private fun updateUIBasedOnSelection(selectedCount: Int) {
        binding.btnChoose.isEnabled = selectedCount > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}