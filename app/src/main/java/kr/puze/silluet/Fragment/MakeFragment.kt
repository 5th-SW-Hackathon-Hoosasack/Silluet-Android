package kr.puze.silluet.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.puze.silluet.R

class MakeFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_make, container, false)
        return view
    }

    companion object {
        fun newInstance(): MakeFragment = MakeFragment()
    }
}