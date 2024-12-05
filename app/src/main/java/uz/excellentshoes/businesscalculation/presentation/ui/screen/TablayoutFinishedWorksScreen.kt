package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.databinding.ScreenTablayoutFinishedWorksBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ViewPagerAdapterFinishedWorks

class TablayoutFinishedWorksScreen : Fragment(R.layout.screen_tablayout_finished_works) {
    private var _binding: ScreenTablayoutFinishedWorksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ViewPagerAdapterFinishedWorks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenTablayoutFinishedWorksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ViewPagerAdapterFinishedWorks(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab, position->
            when(position){
                0 -> tab.text = "Bichuvchi"
                1 -> tab.text = "Tikuvchi"
                2 -> tab.text = "Kosib"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}