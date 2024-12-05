package uz.excellentshoes.businesscalculation.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.excellentshoes.businesscalculation.presentation.ui.screen.FinishedShoeMakerScreen
import uz.excellentshoes.businesscalculation.presentation.ui.screen.FinishedSkinCutterScreen
import uz.excellentshoes.businesscalculation.presentation.ui.screen.FinishedSkinTailScreen

class ViewPagerAdapterFinishedWorks(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FinishedSkinCutterScreen()
            1-> FinishedSkinTailScreen()
            2-> FinishedShoeMakerScreen()
            else-> FinishedSkinCutterScreen()
        }
    }

}