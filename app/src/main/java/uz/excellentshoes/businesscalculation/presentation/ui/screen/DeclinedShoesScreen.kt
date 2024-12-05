package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.databinding.ScreenDeclinedShoesBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.DeclinedShoesAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.CommonShoeCheckerViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.CommonShoeCheckerViewModelImpl
import uz.excellentshoes.businesscalculation.utils.CommonShoeCheckerViewModelFactory
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class DeclinedShoesScreen : Fragment(R.layout.screen_declined_shoes) {
    private var _binding: ScreenDeclinedShoesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CommonShoeCheckerViewModel
    private val adapter by lazy {
        DeclinedShoesAdapter{ selectedCount->
            updateUIBasedOnSelection(selectedCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenDeclinedShoesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,CommonShoeCheckerViewModelFactory(getCurrentDate()))[CommonShoeCheckerViewModelImpl::class.java]
        setAdapter()
        clickEvents()
        setObservers()
        getDataList()
        viewModel.getAllDeclinedShoeData()
    }

    private fun getDataList(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.declinedShoeDataListStateFlow.collect{ dataList->
                    if(dataList.isEmpty()){
                        binding.txtPlaceHolder.show()
                        binding.recyclerDeclinedShoe.hide()
                    }else{
                        binding.txtPlaceHolder.hide()
                        binding.recyclerDeclinedShoe.show()
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
            val preparedShoesDataList = selectedItems.map { data->
                PreparedShoeMakerData(
                    modelName = data.modelName,
                    color = data.color,
                    countPair = data.countPair,
                    skinType = data.skinType,
                    comment = data.comment
                )
            }
            selectedItems.forEach { i->
                viewModel.deleteDeclinedShoeData(i.objectName)
            }
            viewModel.addPreparedShoeDataList(preparedShoesDataList)
            adapter.clearSelections()
        }

    }


    private fun setAdapter(){
        binding.recyclerDeclinedShoe.apply {
            adapter = this@DeclinedShoesScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setObservers(){
        viewModel.progressBarDeclinedShoeLiveData.observe(viewLifecycleOwner){
            if(it){
                binding.recyclerDeclinedShoe.hide()
                binding.progressBar.show()
            }else{
                binding.recyclerDeclinedShoe.show()
                binding.progressBar.hide()
            }
        }
    }


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }

    private fun updateUIBasedOnSelection(selectedCount: Int){
        binding.btnChoose.isEnabled = selectedCount > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}