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
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.databinding.ScreenFinishedShoeMakerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.FinishedShoeMakeGroupAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.PayMoneyBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.FinishedShoeMakeViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.FinishedShoeMakeViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.onFinishedShoeMakeClicked
import uz.excellentshoes.businesscalculation.utils.show

class FinishedShoeMakerScreen : Fragment(R.layout.screen_finished_shoe_maker), onFinishedShoeMakeClicked {
    private var _binding: ScreenFinishedShoeMakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FinishedShoeMakeViewModel by viewModels<FinishedShoeMakeViewModelImpl>()
    private lateinit var groupAdapter: FinishedShoeMakeGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenFinishedShoeMakerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        viewModel.getDataList()
    }

    private fun setAdapter(){
        groupAdapter = FinishedShoeMakeGroupAdapter(this@FinishedShoeMakerScreen)
        binding.recyclerViewFinishedWorks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFinishedWorks.adapter = groupAdapter
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.finishedShoeMakeDataStateFlow.collect{ dataList->
                    if(dataList.isEmpty()){
                        binding.recyclerViewFinishedWorks.hide()
                        binding.txtPlaceHolder.show()
                    }else{
                        binding.recyclerViewFinishedWorks.show()
                        binding.txtPlaceHolder.hide()
                    }
                    val groupData = dataList.groupBy { it.employeeName }.entries.toList()
                    groupAdapter.submitList(groupData)
                }
            }
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.show()
                binding.recyclerViewFinishedWorks.hide()
            }
            else {
                binding.progressBar.hide()
                binding.recyclerViewFinishedWorks.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(data: FinishedShoeMakeData) {
        showDialog(data)
    }

    private fun showDialog(data: FinishedShoeMakeData){
        val dialog = PayMoneyBottomsheetDialog(requireContext())
        dialog.setonPayButtonClickedListener {
            val temp = data.copy(paidMoney = it)
            viewModel.editData(temp)
            dialog.dismiss()
        }
        dialog.show()
    }
}