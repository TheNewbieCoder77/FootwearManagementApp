package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.databinding.ScreenShoeSellerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ShoeSellAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeSellerViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.ShoeSellerViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class ShoeSellerScreen : Fragment(R.layout.screen_shoe_seller) {
    private var _binding: ScreenShoeSellerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShoeSellerViewModel by viewModels<ShoeSellerViewModelImpl>()
    private val adapter by lazy {
        ShoeSellAdapter{ selectedCount->
            updateUIBasedOnSelection(selectedCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenShoeSellerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setAdapter()
        clickEvents()
        setObservers()
        setSearchView()
        viewModel.getPreparedToSailData()
    }

    private fun setSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchData(newText.orEmpty())
                return true
            }

        })
    }

    private fun clickEvents(){
        binding.btnSell.isEnabled = false
        binding.btnSell.setOnClickListener {
            val addedTime = getCurrentTime()
            val selectedItems = adapter.getSelectedItems()
            val soldShoeDataList = selectedItems.map { item->
                SoldShoeData(
                    modelName = item.modelName,
                    color = item.color,
                    countPair = item.countPair,
                    skinType = item.skinType,
                    addedTime = addedTime
                )
            }
            selectedItems.forEach {
                viewModel.deleteShoeSell(it.objectName)
            }
            viewModel.addSoldShoeList(soldShoeDataList)
            adapter.clearSelections()
        }

    }

    private fun setAdapter(){
        binding.recyclerShoes.apply {
            adapter = this@ShoeSellerScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setObservers(){
        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.show()
                binding.recyclerShoes.hide()
            }else{
                binding.progressBar.hide()
                binding.recyclerShoes.show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.preparedToSailStateFlow.collect{ dataList->
                    if(dataList.isEmpty()){
                        binding.containerShoeSell.hide()
                        binding.txtPlaceHolder.show()
                    }else{
                        binding.containerShoeSell.show()
                        binding.txtPlaceHolder.hide()
                    }
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun updateUIBasedOnSelection(selectedCount: Int){
        binding.btnSell.isEnabled = selectedCount > 0
    }

    private fun setToolbar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_icon -> {
                    showPopUpMenu(binding.toolbar)
                    true
                }

                else -> false
            }
        }
    }

    private fun showPopUpMenu(view: View) {
        val popUpMenu = PopupMenu(requireContext(), view, Gravity.END)
        popUpMenu.inflate(R.menu.popup_menu)

        popUpMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logOut -> {
                    Toast.makeText(requireContext(), "Salom", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        popUpMenu.show()
    }

    private fun getCurrentTime(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}