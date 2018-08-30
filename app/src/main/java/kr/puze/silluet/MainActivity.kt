package kr.puze.silluet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kr.puze.silluet.Adapter.TabLayoutAdapter
import kr.puze.silluet.Adapter.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val tabLayout = TabLayoutAdapter(supportFragmentManager, main_tab_layout.tabCount)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        main_tab_layout.addTab(main_tab_layout.newTab().setText("market").setIcon(R.drawable.ic_market_select).setTag("market"), 0)
        main_tab_layout.addTab(main_tab_layout.newTab().setText("camera").setIcon(R.drawable.ic_camera_select).setTag("camera"), 0)
        main_tab_layout.addTab(main_tab_layout.newTab().setText("make").setIcon(R.drawable.ic_make_select).setTag("make"), 0)
        main_tab_layout.tabGravity = TabLayout.GRAVITY_FILL

        main_view_pager.adapter = tabLayout
        main_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))

        main_view_pager.adapter = viewPagerAdapter
        main_view_pager.addOnPageChangeListener(viewPageChangeListener)

        main_tab_layout.setupWithViewPager(main_view_pager)
        initTabLayout()
        main_view_pager.currentItem = 1
    }
    private val viewPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            offTabLayout()
            for (index in 0..3) {
                when (index) {
                    0 -> if(index==position)main_tab_layout.getTabAt(position)!!.setIcon(R.drawable.ic_market_select)
                    1 -> if(index==position)main_tab_layout.getTabAt(position)!!.setIcon(R.drawable.ic_camera_select)
                    2 -> if(index==position)main_tab_layout.getTabAt(position)!!.setIcon(R.drawable.ic_make_select)
                }
            }
        }
    }

    private fun initTabLayout() : Unit {
        main_tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_market_unselect)
        main_tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_camera_select)
        main_tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_make_unselect)
    }

    fun offTabLayout() : Unit {
        main_tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_market_unselect)
        main_tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_camera_unselect)
        main_tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_make_unselect)
    }
}
