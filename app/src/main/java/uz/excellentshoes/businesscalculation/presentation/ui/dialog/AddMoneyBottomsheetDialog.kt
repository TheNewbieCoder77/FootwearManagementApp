package uz.excellentshoes.businesscalculation.presentation.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData
import uz.excellentshoes.businesscalculation.databinding.BottomsheetDialogInputMoneyspendBinding
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.CommonMoneySpenderViewModelImpl
import uz.excellentshoes.businesscalculation.utils.toEditable
import java.util.Locale

class AddMoneyBottomsheetDialog(private val context: Context, private val data: MoneySpendData?,
    private val viewModel: CommonMoneySpenderViewModelImpl
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {
    private var _binding: BottomsheetDialogInputMoneyspendBinding? = null
    private val binding get() = _binding!!
    private val appPreferences = AppPreferences.getInstance()
    private var onBtnEnterButtonClickedListener: ((MoneySpendData) -> Unit)? = null
    private var onDialogCanceledListener: (() -> Unit)? = null
    private var spendType = ""
    private var currency = -1
    private lateinit var adapterSpendType: ArrayAdapter<String>
    private val kontragentAdapter = ArrayAdapter(context, R.layout.item_kontragent_dropdown, mutableListOf<String>())
    private var allKontragentData = listOf<KontragentData>()

    fun setOnBtnEnterButtonClickedListener(l: (MoneySpendData) -> Unit) {
        onBtnEnterButtonClickedListener = l
    }

    fun setOnDialogCanceledListener(l: ()-> Unit){
        onDialogCanceledListener = l
    }

    init {
        setOnCancelListener {
            onDialogCanceledListener?.invoke()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = BottomsheetDialogInputMoneyspendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
        }

        setSpinners()
        clickEvents()
        setValues()
        setupAutocomplete()
        observeKontragents()
        viewModel.getAllKontragent()
    }

    private fun setValues(){
        data?.let {
            val spinnerPosition = adapterSpendType.getPosition(data.spendType)
            binding.spinnerReason.setSelection(spinnerPosition)
            binding.inputMoney.text = String.format(Locale.getDefault(), "%.2f", data.moneyAmount).toEditable()
            binding.inputContragent.text = data.kontragentName.toEditable()
            binding.inputComment.text = data.comment.toEditable()
        }
    }


    private fun setSpinners() {
        val spinnerSpendTypeItems = listOf("To'langan", "To'lanishi kerak", "Olingan", "Olinishi kerak")
        adapterSpendType =
            object : ArrayAdapter<String>(context, R.layout.spinner_item, spinnerSpendTypeItems) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view as TextView

                    // Set color based on position
                    when (position) {
                        0 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.on_progress_color
                            )
                        )

                        1 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.decline_border_color
                            )
                        )

                        2 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.on_progress_color
                            )
                        )

                        3 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.decline_border_color
                            )
                        )
                    }

                    return view
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view as TextView

                    // Set color for dropdown items
                    when (position) {
                        0 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.on_progress_color
                            )
                        )

                        1 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.decline_border_color
                            )
                        )

                        2 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.on_progress_color
                            )
                        )

                        3 -> textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.decline_border_color
                            )
                        )
                    }

                    return view
                }
            }

        binding.spinnerReason.adapter = adapterSpendType
        binding.spinnerReason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    spendType = binding.spinnerReason.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

        val spinnerCurrencyItems = listOf("UZS","USD")
        val adapterSpinnerCurrency = ArrayAdapter(context,R.layout.spinner_item,spinnerCurrencyItems)
        adapterSpinnerCurrency.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerCurrency.adapter = adapterSpinnerCurrency
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currency = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    private fun clickEvents() {
        binding.btnAddMoney.setOnClickListener {
            if (checkMoneyAmount(binding.inputMoney.text.toString())) {
                binding.layoutMoney.isErrorEnabled = true
                return@setOnClickListener
            }else binding.layoutMoney.isErrorEnabled = false
            if (checkKontragentName(binding.inputContragent.text.toString())) return@setOnClickListener
            if (checkComment(binding.inputComment.text.toString())) {
                binding.layoutComment.isErrorEnabled = true
                return@setOnClickListener
            }else binding.layoutComment.isErrorEnabled = false

            val moneyAmount = when(currency){
                0 -> binding.inputMoney.text.toString().replace(",",".").toDouble()
                else -> {
                    binding.inputMoney.text.toString().replace(",",".").toDouble() * appPreferences.currencyDollar
                }
            }
            val data = if(data == null){
                MoneySpendData(
                    spendType = spendType,
                    kontragentName = binding.inputContragent.text.toString(),
                    moneyAmount = moneyAmount,
                    comment = binding.inputComment.text.toString()
                )
            }else{
                MoneySpendData(
                    objectName = data.objectName,
                    employeeName = data.employeeName,
                    addedTime = data.addedTime,
                    spendType = spendType,
                    kontragentName = binding.inputContragent.text.toString(),
                    moneyAmount = moneyAmount,
                    comment = binding.inputComment.text.toString()
                )
            }



            onBtnEnterButtonClickedListener?.invoke(data)
        }
    }

    private fun setupAutocomplete(){
        binding.inputContragent.apply {
            threshold = 1
            setAdapter(kontragentAdapter)

            addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filterKontragents(s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            setOnItemClickListener{ parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as String
                val selectedKontragent = allKontragentData.find { it.kontragentName == selectedItem }
                selectedKontragent?.let {
                    setText(it.kontragentName)
                }
            }
        }
    }

    private fun filterKontragents(query: String){
        if(query.isEmpty()){
            updateAdapterWithAllItems()
        }else{
            val filtered = allKontragentData.filter { kontragent->
                kontragent.kontragentName.contains(query,ignoreCase = true)
            }
            val suggestions = filtered.map { it.kontragentName }
            kontragentAdapter.clear()
            kontragentAdapter.addAll(suggestions)
            kontragentAdapter.notifyDataSetChanged()
        }

    }

    private fun observeKontragents(){
        lifecycleScope.launch {
            viewModel.kontragentList.collect{ kontragent->
                allKontragentData = kontragent
                updateAdapterWithAllItems()
            }
        }
    }

    private fun updateAdapterWithAllItems() {
        val allNames = allKontragentData.map { it.kontragentName }
        kontragentAdapter.clear()
        kontragentAdapter.addAll(allNames)
        kontragentAdapter.notifyDataSetChanged()
    }

    private fun checkMoneyAmount(money: String): Boolean {
        return money.isEmpty()
    }

    private fun checkKontragentName(name: String): Boolean {
        return name.isEmpty()
    }

    private fun checkComment(comment: String): Boolean {
        return comment.isEmpty()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

}