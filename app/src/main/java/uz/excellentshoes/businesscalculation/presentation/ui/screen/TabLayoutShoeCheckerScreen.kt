package uz.excellentshoes.businesscalculation.presentation.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import uz.excellentshoes.businesscalculation.R
import uz.excellentshoes.businesscalculation.databinding.ScreenTablayoutShoeCheckerBinding
import uz.excellentshoes.businesscalculation.presentation.ui.adapter.ViewPagerAdapterShoeChecker

class TabLayoutShoeCheckerScreen : Fragment(R.layout.screen_tablayout_shoe_checker) {
    private var _binding: ScreenTablayoutShoeCheckerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ViewPagerAdapterShoeChecker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenTablayoutShoeCheckerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ViewPagerAdapterShoeChecker(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab, position ->
            when(position){
                0 -> tab.text = "Tayyor ishlar"
                1 -> tab.text = "Muammoli ishlar"
            }
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}