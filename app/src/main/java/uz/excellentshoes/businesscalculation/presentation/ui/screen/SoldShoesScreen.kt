package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.databinding.ScreenSoldShoesBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.SoldShoesAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.SoldShoeInfoBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SoldShoesViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.SoldShoesViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show

class SoldShoesScreen : Fragment(R.layout.screen_sold_shoes) {
    private var _binding: ScreenSoldShoesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SoldShoesViewModel by viewModels<SoldShoesViewModelImpl>()
    private lateinit var adapter: SoldShoesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenSoldShoesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setToolbar()
        setObservers()
        viewModel.getAllSoldShoes()
    }

    private fun setAdapter(){
        adapter = SoldShoesAdapter()
        binding.recyclerSoldShoes.apply {
            adapter = this@SoldShoesScreen.adapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        adapter.setOnSoldShoeItemLongClickedListener {
            viewModel.deleteSoldShoe(it.objectName)
        }

        adapter.setOnSoldShoeItemClickedListener {
            showDialog(it)
        }
    }

    private fun showDialog(data: SoldShoeData){
        val dialog = SoldShoeInfoBottomsheetDialog(requireContext(),data)
        dialog.show()
    }

    private fun setObservers(){
        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.show()
                binding.recyclerSoldShoes.hide()
            }else{
                binding.progressBar.hide()
                binding.recyclerSoldShoes.show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.soldShoeStateFlow.collect{ dataList ->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun setToolbar(){
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_icon ->{
                    showPopUpMenu(binding.toolbar)
                    true
                }

                else -> false
            }
        }
    }

    private fun showPopUpMenu(view: View){
        val popUpMenu = PopupMenu(requireContext(),view, Gravity.END)
        popUpMenu.inflate(R.menu.popup_menu)

        popUpMenu.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.logOut ->{
                    Toast.makeText(requireContext(), "Salom", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popUpMenu.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}