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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.databinding.ScreenSkinCutterBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.SkinCutterAdapter
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinDataViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.SkinDataViewModelImpl
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show

class SkinCutterScreen : Fragment(R.layout.screen_skin_cutter) {
    private var _binding: ScreenSkinCutterBinding? = null
    private val binding get() = _binding!!
    private lateinit var skinCutterAdapter: SkinCutterAdapter
    private val viewModel: SkinDataViewModel by viewModels<SkinDataViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openNextScreenLiveData.observe(this, openNextScreenObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenSkinCutterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        clickEvents()
        setToolbar()
        viewModel.getSkinDataList()
        getDataList()
        observeDeleteResult()
    }

    private fun getDataList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skinDataFlow.collect { skinDataList ->
                    if (skinDataList.isEmpty()) {
                        binding.txtPlaceHolder.show()
                        binding.recyclerSkinCut.hide()
                    } else {
                        binding.txtPlaceHolder.hide()
                        binding.recyclerSkinCut.show()
                    }
                    skinCutterAdapter.submitList(skinDataList)
                }
            }
        }
    }

    private fun observeDeleteResult() {
        lifecycleScope.launch {
            viewModel.deleteSkinDataResult.collect { isDeleted ->
                if (isDeleted) Toast.makeText(
                    requireContext(),
                    "Successfully deleted!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setAdapter() {
        skinCutterAdapter = SkinCutterAdapter()
        binding.recyclerSkinCut.adapter = skinCutterAdapter
        binding.recyclerSkinCut.layoutManager = LinearLayoutManager(requireContext())
        skinCutterAdapter.setOnCutSkinLongClickedListener {
            viewModel.deleteSkinData(it.objectName)
        }
    }

    private fun clickEvents() {
        binding.btnAdd.setOnClickListener {
            viewModel.openAddSkinCutScreen()
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

    private val openNextScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_skinCutterScreen_to_addSkinCutScreen)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}