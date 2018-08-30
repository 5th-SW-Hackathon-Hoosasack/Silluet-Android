package kr.puze.silluet.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kr.puze.silluet.Fragment.CameraFragment
import kr.puze.silluet.Fragment.MakeFragment
import kr.puze.silluet.Fragment.MarketFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MarketFragment.newInstance()
            1 -> CameraFragment.newInstance()
            2 -> MakeFragment.newInstance()
            else -> CameraFragment.newInstance()
        }
    }
    override fun getCount(): Int = 3
}