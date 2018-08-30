package kr.puze.silluet.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import kr.puze.silluet.Fragment.CameraFragment
import kr.puze.silluet.Fragment.MakeFragment
import kr.puze.silluet.Fragment.MarketFragment

class TabLayoutAdapter(fm: FragmentManager, private var tapCount: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                tapCount = position
                return MarketFragment()
            }
            1 -> {
                tapCount = position
                return CameraFragment()
            }
            2 -> {
                tapCount = position
                return MakeFragment()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tapCount
    }
}