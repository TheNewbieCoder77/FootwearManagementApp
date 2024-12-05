package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.databinding.ScreenAddSkinCutBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.SelectColorAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.SelectModelAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinDataViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.SkinDataViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class AddSkinCutScreen : Fragment(R.layout.screen_add_skin_cut) {
    private val viewModel: SkinDataViewModel by viewModels<SkinDataViewModelImpl>()
    private var _binding: ScreenAddSkinCutBinding? = null
    private val binding get() = _binding!!
    private lateinit var colorsAdapter: SelectColorAdapter
    private lateinit var modelAdapter: SelectModelAdapter
    private lateinit var skinTypeAdapter: SelectSkinTypeAdapter
    private var modelName = ""
    private var pairCount = 0
    private var colorName = ""
    private var comment = ""
    private var skinType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenAddSkinCutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        clickEvents()
        setListeners()
        setAdapters()
        getListItems()
        viewModel.getColors()
        viewModel.getModels()
        viewModel.getSkinTypes()
    }

    private fun getListItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.colorsStateFlow.collectLatest { list ->
                    colorsAdapter.submitList(list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modelsStateFlow.collectLatest { list ->
                    modelAdapter.submitList(list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skinTypesStateFlow.collectLatest { list ->
                    skinTypeAdapter.submitList(list)
                }
            }
        }
    }


    private fun setAdapters() {
        colorsAdapter = SelectColorAdapter {
            colorName = it.colorName
        }
        binding.recyclerColors.apply {
            adapter = this@AddSkinCutScreen.colorsAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        }



        modelAdapter = SelectModelAdapter {
            modelName = it.shoeModelName
        }
        binding.recyclerModels.apply {
            adapter = this@AddSkinCutScreen.modelAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        }



        skinTypeAdapter = SelectSkinTypeAdapter {
            skinType = it.skinTypeName
        }
        binding.recyclerSkinTypes.apply {
            adapter = this@AddSkinCutScreen.skinTypeAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        }

    }

    private fun clickEvents() {
        binding.btnAddSkin.setOnClickListener {
            if(modelName.isEmpty()){
                Toast.makeText(requireContext(), "Model tanlang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(colorName.isEmpty()){
                Toast.makeText(requireContext(), "Rangni tanlang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(skinType.isEmpty()){
                Toast.makeText(requireContext(), "Teri turini tanlang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pairCount == 0){
                Toast.makeText(requireContext(), "Necha juft ishligini kiriting", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val currentTime = getCurrentTime()
                val skinData = SkinData(
                    modelName = modelName,
                    color = colorName,
                    countPair = pairCount,
                    skinType = skinType,
                    addedTime = currentTime,
                    comment = comment
                )
                val fData = FinishedSkinCutData(
                    modelName = modelName,
                    color = colorName,
                    countPair = pairCount,
                    skinType = skinType,
                    addedTime = currentTime,
                    comment = comment
                )
                viewModel.addSkinData(skinData)
                viewModel.addFinishedSkinCut(fData)
                viewModel.addSkinDataResult.collectLatest { success ->
                    success?.let {
                        if (it) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully added",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setListeners() {

        binding.inputCountPair.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    pairCount = s.toString().toInt()
                } else {
                    pairCount = 0
                }
            }

        })


        binding.inputComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                comment = s.toString()
            }

        })
    }

    private fun setObservers() {
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            binding.containerViews.hide()
            binding.progressBar.show()
        }

    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}