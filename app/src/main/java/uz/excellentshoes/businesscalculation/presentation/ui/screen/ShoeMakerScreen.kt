package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.databinding.ScreenShoeMakerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ProcessShoeMakerAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.dialog.ConfirmDialog
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeMakerViewModel
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.ShoeMakerViewModelImpl
import uz.excellentshoes.businesscalculation.utils.Directions.LEFT_SIDE_SWIPE
import uz.excellentshoes.businesscalculation.utils.Directions.RIGHT_SIDE_SWIPE
import uz.excellentshoes.businesscalculation.utils.hide
import uz.excellentshoes.businesscalculation.utils.show
import java.text.SimpleDateFormat
import java.util.Locale

class ShoeMakerScreen : Fragment(R.layout.screen_shoe_maker) {
    private var _binding: ScreenShoeMakerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProcessShoeMakerAdapter
    private val viewModel: ShoeMakerViewModel by viewModels<ShoeMakerViewModelImpl>()
    private val appPreferences = AppPreferences.getInstance()
    private var actionSwipe = -1
    private var lastSwipedPosition = -1
    private lateinit var processShoeMakerDataTemplate: ProcessShoeMakerData


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenShoeMakerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        clickEvents()
        setToolbar()
        getDataList()
        viewModel.progressBarSecondLiveData.observe(viewLifecycleOwner,progressBarSecondObserver)
        viewModel.fetchProcessShoeMakerDataByPhoneNumber(appPreferences.phoneNumber)
    }

    private fun setAdapter(){
        adapter = ProcessShoeMakerAdapter()
        binding.recyclerShoeMaker.adapter = adapter
        binding.recyclerShoeMaker.layoutManager = LinearLayoutManager(requireContext())
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lastSwipedPosition = viewHolder.bindingAdapterPosition
                processShoeMakerDataTemplate = adapter.currentList[lastSwipedPosition]
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        actionSwipe = RIGHT_SIDE_SWIPE.value
                        showDialogConfirm()
                    }
                    ItemTouchHelper.LEFT ->{
                        actionSwipe = LEFT_SIDE_SWIPE.value
                        showDialogConfirm()
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(),R.color.color_finish))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(),R.color.color_delete))
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftActionIcon(R.drawable.ic_done)
                    .setSwipeRightActionIconTint(ContextCompat.getColor(requireContext(),R.color.white))
                    .setSwipeLeftActionIconTint(ContextCompat.getColor(requireContext(),R.color.white))
                    .addSwipeLeftLabel("Tugatish")
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
        itemTouchHelper.attachToRecyclerView(binding.recyclerShoeMaker)

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

    private fun clickEvents(){
        binding.btnGetWork.setOnClickListener {
            findNavController().navigate(R.id.action_shoeMakerScreen_to_addShoeMakerScreen)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDataList(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.processShoeMakerDataList.collect{ data->
                    binding.txtDailyWork.text = "${data.size}ta komplekt jarayonda"
                    if(data.isEmpty()){
                        binding.txtPlaceHolder.show()
                        binding.recyclerShoeMaker.hide()
                    }else{
                        binding.txtPlaceHolder.hide()
                        binding.recyclerShoeMaker.show()
                    }
                    adapter.submitList(data)
                }
            }
        }
    }

    private val progressBarSecondObserver = Observer<Boolean>{
        if(it){
            binding.progressBar.show()
            binding.recyclerShoeMaker.hide()
        }else{
            binding.progressBar.hide()
            binding.recyclerShoeMaker.show()
        }
    }

    private fun showDialogConfirm(){
        val dialog = ConfirmDialog()
        dialog.setOnCancelButtonClickedListener {
            adapter.notifyItemChanged(lastSwipedPosition)
            dialog.dismiss()
        }
        dialog.setOnDialogCanceledListener {
            adapter.notifyItemChanged(lastSwipedPosition)
        }
        dialog.setOnConfirmButtonClickedListener {
            when(actionSwipe){
                RIGHT_SIDE_SWIPE.value ->{
                    viewModel.showProgressBar(true)
                    val data = PreparedTailData(
                        employeeName = processShoeMakerDataTemplate.employeeName,
                        modelName = processShoeMakerDataTemplate.modelName,
                        skinType = processShoeMakerDataTemplate.skinType,
                        countPair = processShoeMakerDataTemplate.countPair,
                        color = processShoeMakerDataTemplate.color,
                        comment = processShoeMakerDataTemplate.comment
                    )
                    viewModel.addPreparedTailData(data)
                }
                LEFT_SIDE_SWIPE.value -> {
                    viewModel.showProgressBar(false)
                    val currentTime = getCurrentTime()
                    val data = PreparedShoeMakerData(
                        modelName = processShoeMakerDataTemplate.modelName,
                        color = processShoeMakerDataTemplate.color,
                        skinType = processShoeMakerDataTemplate.skinType,
                        countPair = processShoeMakerDataTemplate.countPair,
                        addedTime = currentTime,
                        comment = processShoeMakerDataTemplate.comment
                    )
                    val fData = FinishedShoeMakeData(
                        modelName = processShoeMakerDataTemplate.modelName,
                        color = processShoeMakerDataTemplate.color,
                        skinType = processShoeMakerDataTemplate.skinType,
                        countPair = processShoeMakerDataTemplate.countPair,
                        addedTime = currentTime,
                        comment = processShoeMakerDataTemplate.comment
                    )
                    viewModel.addFinishedShoeMake(fData)
                    viewModel.addPreparedShoeMakerData(data)
                }
            }
            viewModel.deleteProcessShoeMakerData(processShoeMakerDataTemplate.objectName)
            viewModel.showProgressBar(false)
            if(adapter.currentList.size == 1) adapter.notifyItemRemoved(lastSwipedPosition)
            Toast.makeText(requireContext(), "THis is size: ${adapter.currentList.size}", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show(childFragmentManager,null)
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