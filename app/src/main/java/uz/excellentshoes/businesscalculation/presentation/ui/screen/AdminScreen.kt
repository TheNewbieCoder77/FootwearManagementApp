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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.databinding.ScreenAdminBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.AdminCategoryAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddCategoryBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddDollarCurrencyBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddPhoneDataBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.AdminCategoryViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.AdminCategoryViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show

class AdminScreen : Fragment(R.layout.screen_admin) {
    private var _binding: ScreenAdminBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminCategoryViewModel by viewModels<AdminCategoryViewModelImpl>()
    private lateinit var adapter: AdminCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllCategories()
    }

    private fun setObservers() {
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.recyclerAdmin.hide()
                binding.progressBar.show()
            } else {
                binding.recyclerAdmin.show()
                binding.progressBar.hide()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryDataListStateFlow.collect { dataList ->
                    adapter.submitList(dataList)
                }
            }
        }
    }


    private fun clickEvents() {
        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = AddCategoryBottomsheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addCategory(it)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDollarCurrencyDialog() {
        val dialog = AddDollarCurrencyBottomsheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addDollarCurrency(it)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showPhoneDataDialog(){
        val dialog = AddPhoneDataBottomsheetDialog(requireContext())
        dialog.setOnAddButtonClickedListener {
            viewModel.addPhoneData(it)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setAdapter() {
        adapter = AdminCategoryAdapter()
        binding.recyclerAdmin.apply {
            adapter = this@AdminScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        adapter.setOnCategoryItemClickedListener { category ->
            when (category.categoryId) {
                1 -> findNavController().navigate(R.id.action_adminScreen_to_shoeColorsScreen)
                2 -> findNavController().navigate(R.id.action_adminScreen_to_skinTypesScreen)
                3 -> findNavController().navigate(R.id.action_adminScreen_to_shoeModelsScreen)
                4 -> findNavController().navigate(R.id.action_adminScreen_to_contragentsScreen)
                5 -> findNavController().navigate(R.id.action_adminScreen_to_warehouseProductScreen)
                6 -> findNavController().navigate(R.id.action_adminScreen_to_userPasswordScreen)
                7 -> showDollarCurrencyDialog()
                8 -> showPhoneDataDialog()
                9 -> findNavController().navigate(R.id.action_adminScreen_to_tablayoutFinishedWorksScreen)
                10 -> findNavController().navigate(R.id.action_adminScreen_to_soldShoesScreen)
            }
        }

        adapter.setOnCategoryItemLongClickedListener {
            viewModel.deleteCategory(it.objectName)
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}