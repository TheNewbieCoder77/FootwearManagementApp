package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.databinding.ScreenLoginBinding
import uz.excellentshoes.businesscalculation.presentation.viewmodel.LoginViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.LoginViewModelImpl

class LoginScreen : Fragment(R.layout.screen_login) {
    private var _binding: ScreenLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()
    private val appPreferences = AppPreferences.getInstance()
    private var jobPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openNextScreenLiveData.observe(this, openNextScreenObserver)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinner()
        clickEvents()
        setObservers()
        viewModel.getAllUserPassword()
    }

    private fun setObservers() {
        viewModel.errorFullNameLiveData.observe(viewLifecycleOwner) {
            if (it.first) {
                binding.layoutFullName.isErrorEnabled = true
                binding.layoutFullName.error = it.second
            } else {
                binding.layoutFullName.isErrorEnabled = false
                binding.layoutFullName.error = null
            }

        }

        viewModel.errorPasswordLiveData.observe(viewLifecycleOwner) {
            if (!it.first) {
                binding.layoutPasswordEnter.isErrorEnabled = true
                binding.layoutPasswordEnter.error = it.second
            } else {
                binding.layoutPasswordEnter.isErrorEnabled = false
                binding.layoutPasswordEnter.error = null
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userPasswordStateFlow.collect{userPasswordDataList->
                val spinnerItems = userPasswordDataList.map { it.userName }
                val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerItems)
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.spinnerPosition.adapter = adapter
            }
        }
    }

    private fun clickEvents() {
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.checkFullName(binding.inputFullName.text.toString())) return@launch
                val temp =
                    viewModel.checkPassword(binding.inputPassword.text.toString(), jobPosition)
                if (!temp) return@launch
                val exists = viewModel.checkPhoneNumber(
                    binding.inputPhoneNumber.text.toString(),
                    jobPosition
                )
                if (!exists) {
                    binding.layoutPhoneNumber.isErrorEnabled = true
                    binding.layoutPhoneNumber.error = "Ro'yxatdan o'tmagan raqam"
                } else {
                    binding.layoutPhoneNumber.isErrorEnabled = false
                    binding.layoutPhoneNumber.error = null
                    appPreferences.phoneNumber = binding.inputPhoneNumber.text.toString()
                    appPreferences.fullName = binding.inputFullName.text.toString()
                    viewModel.login()
                }
            }
        }

    }

    private fun setSpinner() {
        binding.spinnerPosition.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jobPosition = viewModel.userPasswordStateFlow.value[position].jobPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private val openNextScreenObserver = Observer<Unit> {
        when (jobPosition) {
            1 -> findNavController().navigate(R.id.action_loginScreen_to_skinCutterScreen)
            2 -> findNavController().navigate(R.id.action_loginScreen_to_skinTailorScreen)
            3 -> findNavController().navigate(R.id.action_loginScreen_to_shoeMakerScreen)
            4 -> findNavController().navigate(R.id.action_loginScreen_to_tabLayoutShoeCheckerScreen)
            5 -> findNavController().navigate(R.id.action_loginScreen_to_shoeSellerScreen)
            6 -> findNavController().navigate(R.id.action_loginScreen_to_moneySpenderScreen)
            7 -> findNavController().navigate(R.id.action_loginScreen_to_warehouseManScreen)
            8 -> findNavController().navigate(R.id.action_loginScreen_to_adminScreen)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}