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
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.databinding.ScreenFinishedSkinTailorBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.FinishedSkinTailGroupAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.PayMoneyBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.FinishedSkinTailViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.FinishedSkinTailViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.onFinishedSkinTailClicked
import uz.excellentshoes.businesscalculation.utils.show

class FinishedSkinTailScreen : Fragment(R.layout.screen_finished_skin_tailor), onFinishedSkinTailClicked {
    private var _binding: ScreenFinishedSkinTailorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FinishedSkinTailViewModel by viewModels<FinishedSkinTailViewModelImpl>()
    private lateinit var groupAdapter: FinishedSkinTailGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenFinishedSkinTailorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        viewModel.getDataList()
    }

    private fun setAdapter(){
        groupAdapter = FinishedSkinTailGroupAdapter(this@FinishedSkinTailScreen)
        binding.recyclerViewFinishedWorks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFinishedWorks.adapter = groupAdapter
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.finishedSkinTailDataStateFlow.collect{ dataList->
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

    override fun onClick(data: FinishedSkinTailData) {
        showDialog(data)
    }

    private fun showDialog(data: FinishedSkinTailData){
        val dialog = PayMoneyBottomsheetDialog(requireContext())
        dialog.setonPayButtonClickedListener {
            val temp = data.copy(paidMoney = it)
            viewModel.editData(temp)
            dialog.dismiss()
        }
        dialog.show()
    }
}