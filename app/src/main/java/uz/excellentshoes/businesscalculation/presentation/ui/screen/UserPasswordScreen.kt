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
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.databinding.ScreenUserPasswordBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ShoeModelAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.UserPasswordAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddUserPasswordBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.UserPasswordViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.UserPasswordViewModelImpl

class UserPasswordScreen : Fragment(R.layout.screen_user_password) {
    private var _binding: ScreenUserPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserPasswordAdapter
    private val viewModel: UserPasswordViewModel by viewModels<UserPasswordViewModelImpl>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenUserPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        clickEvents()
        viewModel.getAllUserPassword()
    }

    private fun setAdapter(){
        adapter = UserPasswordAdapter()
        binding.recyclerUserPassword.apply {
            adapter = this@UserPasswordScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setOnItemLongClickedListener{
            viewModel.deleteUserPassword(it.objectName)
        }
        adapter.setOnItemClickedListener {
            showAddDialog(it)
        }
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userPasswordStateFlow.collect{ dataList->
                    adapter.submitList(dataList)
                }
            }
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showAddDialog(null)
        }
    }

    private fun showAddDialog(data: UserPasswordData?){
        val dialog = AddUserPasswordBottomsheetDialog(requireContext(),data)
        dialog.setOnAddButtonClickedListener {
            viewModel.addUserPassword(it)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}