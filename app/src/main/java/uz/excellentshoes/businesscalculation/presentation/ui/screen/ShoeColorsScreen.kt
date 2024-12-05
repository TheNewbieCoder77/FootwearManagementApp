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
import uz.excellentshoes.businesscalculation.databinding.ScreenShoeColorsBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ShoeColorAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddShoeColorBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeColorViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.ShoeColorViewModelImpl

class ShoeColorsScreen : Fragment(R.layout.screen_shoe_colors) {
    private var _binding: ScreenShoeColorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShoeColorAdapter
    private val viewModel: ShoeColorViewModel by viewModels<ShoeColorViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenShoeColorsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllShoeColors()
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.shoeColorListStateFlow.collect{ dataList->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setAdapter(){
        adapter = ShoeColorAdapter()
        binding.recyclerShoeColors.apply {
            adapter = this@ShoeColorsScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnCategoryItemLongClickedListener {
            viewModel.deleteShoeColor(it.objectName)
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = AddShoeColorBottomsheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addShoeColor(it)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}