package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeCheckData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.databinding.ScreenShoeCheckerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ShoeCheckerAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.ConfirmDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.CommonShoeCheckerViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.CommonShoeCheckerViewModelImpl
import uz.excellentshoes.businesscalculation.utils.CommonShoeCheckerViewModelFactory
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class ShoeCheckerScreen : Fragment(R.layout.screen_shoe_checker) {
    private var _binding: ScreenShoeCheckerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShoeCheckerAdapter
    private lateinit var viewModel: CommonShoeCheckerViewModel
    private var dailyShoeCount = 0
    private var dailyFinishedShoeCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenShoeCheckerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,CommonShoeCheckerViewModelFactory(getCurrentDate()))[CommonShoeCheckerViewModelImpl::class.java]
        setAdapter()
        getDataList()
        setObservers()
        viewModel.progressBarShoeCheckerLiveData.observe(viewLifecycleOwner,progressBarShoeCheckerObserver)
        viewModel.getAllPreparedShoeMakerData()
    }

    @SuppressLint("SetTextI18n")
    private fun getDataList(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.preparedShoeMakerDataListStateFlow.collect { dataList->
                    if(dataList.isEmpty()){
                        binding.txtPlaceHolder.show()
                        binding.recyclerShoeChecker.hide()
                    }else{
                        binding.txtPlaceHolder.hide()
                        binding.recyclerShoeChecker.show()
                    }
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = ShoeCheckerAdapter()
        binding.recyclerShoeChecker.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShoeChecker.adapter = adapter
        adapter.setOnPassButtonClickedListener {
            showDialog(it)
        }
        adapter.setOnDeclineButtonClickedListener {
            viewModel.deletePreparedShoeMakerData(it.objectName)
            val decData = DeclinedShoeData(
                modelName = it.modelName,
                color = it.color,
                skinType = it.skinType,
                countPair = it.countPair,
                comment = it.comment
            )
            viewModel.addDeclinedShoeData(decData)
        }
    }

    private val progressBarShoeCheckerObserver = Observer<Boolean>{
        if(it){
            binding.progressBar.show()
            binding.onProgressLayout.hide()
            binding.recyclerShoeChecker.hide()
        }else{
            binding.progressBar.hide()
            binding.onProgressLayout.show()
            binding.recyclerShoeChecker.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.shoeCountStateFlow.collect { shoeCount->
                        shoeCount?.let {
                            dailyShoeCount = shoeCount
                            binding.progressLayout.max = shoeCount
                            binding.txtPairsCount.text = "$dailyFinishedShoeCount/$dailyShoeCount"
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.finishedShoeCountStateFlow.collect{ finishedShoeCount->
                        finishedShoeCount?.let {
                            dailyFinishedShoeCount = finishedShoeCount
                            binding.progressLayout.progress = finishedShoeCount
                            binding.txtPairsCount.text = "$dailyFinishedShoeCount/$dailyShoeCount"
                        }
                    }
                }
            }
        }
    }

    private fun showDialog(preparedShoeMakerData: PreparedShoeMakerData){
        val dialog = ConfirmDialog()
        dialog.setOnDialogCanceledListener {
            dialog.dismiss()
        }
        dialog.setOnCancelButtonClickedListener {
            dialog.dismiss()
        }
        dialog.setOnConfirmButtonClickedListener {
            viewModel.showShoeCheckerProgressBar(true)
            val currentTime = getCurrentTime()
            val data = PreparedToSailData(
                modelName = preparedShoeMakerData.modelName,
                color = preparedShoeMakerData.color,
                skinType = preparedShoeMakerData.skinType,
                countPair = preparedShoeMakerData.countPair,
                comment = preparedShoeMakerData.comment
            )
            val fData = FinishedShoeCheckData(
                modelName = preparedShoeMakerData.modelName,
                color = preparedShoeMakerData.color,
                skinType = preparedShoeMakerData.skinType,
                countPair = preparedShoeMakerData.countPair,
                comment = preparedShoeMakerData.comment,
                addedTime = currentTime
            )
            dialog.dismiss()
            viewModel.addShoeSellerData(data)
            viewModel.addFinishedShoeCheck(fData)
            viewModel.deletePreparedShoeMakerData(preparedShoeMakerData.objectName)
            viewModel.showShoeCheckerProgressBar(false)
        }
        dialog.show(childFragmentManager,null)
    }

    private fun getCurrentTime(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}