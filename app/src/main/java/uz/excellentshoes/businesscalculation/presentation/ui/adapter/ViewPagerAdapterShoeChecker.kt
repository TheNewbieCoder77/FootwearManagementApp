package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.screen.DeclinedShoesScreen
import uz.excellentshoes.businesscalculation.presentation.ui.screen.ShoeCheckerScreen

class ViewPagerAdapterShoeChecker(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ShoeCheckerScreen()
            1 -> DeclinedShoesScreen()
            else -> ShoeCheckerScreen()
        }
    }

}