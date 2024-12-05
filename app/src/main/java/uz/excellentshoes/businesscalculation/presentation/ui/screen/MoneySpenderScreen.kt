package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.graphics.Canvas
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData
import uz.excellentshoes.businesscalculation.databinding.ScreenMoneySpenderBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.MoneySpenderAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.AddMoneyBottomsheetDialog
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.ConfirmDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.CommonMoneySpenderViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.CommonMoneySpenderViewModelImpl
import uz.excellentshoes.businesscalculation.utils.Directions
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale
import java.text.DecimalFormat

class MoneySpenderScreen : Fragment(R.layout.screen_money_spender) {
    private var _binding: ScreenMoneySpenderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonMoneySpenderViewModelImpl by viewModels<CommonMoneySpenderViewModelImpl>()
    private lateinit var adapter: MoneySpenderAdapter
    private var moneySpendDataTemplate: MoneySpendData? = null
    private var actionSide = -1
    private var lastSwipedPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenMoneySpenderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        clickEvents()
        setObservers()

        viewModel.getAllMoneyDataList()
    }

    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.moneySpendDataListStateFlow.collect{ dataList->
                    if(dataList.isEmpty()){
                        binding.recyclerConsumption.hide()
                        binding.txtPlaceHolder.show()
                    }else{
                        binding.recyclerConsumption.show()
                        binding.txtPlaceHolder.hide()
                    }
                    adapter.submitList(dataList)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.paidAmountStateFlow.collect{ amount->
                        binding.txtPaidMoney.text = formatMoneyAmount(amount)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.unPaidAmountStateFlow.collect{ amount ->
                        binding.txtUnPaidMoney.text = formatMoneyAmount(amount)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.receiveAmountStateFlow.collect{ amount->
                        binding.txtEarnedMoney.text = formatMoneyAmount(amount)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.unReceiveAmountStateFlow.collect{ amount->
                        binding.txtNotEarnedMoney.text = formatMoneyAmount(amount)
                    }
                }
            }
        }
    }

    private fun clickEvents(){
        binding.btnAdd.setOnClickListener {
            showAddDialog(null)
        }
    }

    private fun setAdapter(){
        adapter = MoneySpenderAdapter()
        binding.recyclerConsumption.apply {
            adapter = this@MoneySpenderScreen.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val callback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lastSwipedPosition = viewHolder.bindingAdapterPosition
                moneySpendDataTemplate = adapter.currentList[lastSwipedPosition]
                when(direction){
                    ItemTouchHelper.RIGHT ->{
                        actionSide = Directions.RIGHT_SIDE_SWIPE.value
                        showConfirmDialog(moneySpendDataTemplate!!)
                    }
                    ItemTouchHelper.LEFT ->{
                        actionSide = Directions.LEFT_SIDE_SWIPE.value
                        showAddDialog(moneySpendDataTemplate)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(),R.color.edit_color))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(),R.color.color_delete))
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftActionIcon(R.drawable.ic_edit)
                    .setSwipeRightActionIconTint(ContextCompat.getColor(requireContext(),R.color.white))
                    .setSwipeLeftActionIconTint(ContextCompat.getColor(requireContext(),R.color.white))
                    .addSwipeLeftLabel("Tahrirlash")
                    .addSwipeRightLabel("O'chirish")
                    .setSwipeRightLabelColor(ContextCompat.getColor(requireContext(),R.color.white))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(),R.color.white))
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerConsumption)
    }

    private fun showConfirmDialog(moneySpendData: MoneySpendData){
        val dialog = ConfirmDialog()
        dialog.setOnDialogCanceledListener {
            adapter.notifyItemChanged(lastSwipedPosition)
        }
        dialog.setOnCancelButtonClickedListener {
            adapter.notifyItemChanged(lastSwipedPosition)
            dialog.dismiss()
        }
        dialog.setOnConfirmButtonClickedListener {
            viewModel.deleteMoneySpendData(moneySpendData.objectName)
            dialog.dismiss()
        }
        dialog.show(childFragmentManager,null)
    }

    private fun showAddDialog(moneySpendData: MoneySpendData?){
        val dialog = AddMoneyBottomsheetDialog(requireContext(),moneySpendData,viewModel)
        dialog.setOnDialogCanceledListener {
            adapter.notifyItemChanged(lastSwipedPosition)
        }

        dialog.setOnBtnEnterButtonClickedListener { item->
            if(moneySpendData != null){
                viewModel.editMoneySpendData(item)
            }else{
                item.addedTime = getCurrentTime()
                item.checked = false
                viewModel.addMoneySpendData(item)
            }
            dialog.dismiss()
            adapter.notifyItemChanged(lastSwipedPosition)
        }
        dialog.show()
    }


    private fun getCurrentTime(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatMoneyAmount(amount: Double): String {
        val formatter = DecimalFormat("#,###.##")
        return formatter.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}