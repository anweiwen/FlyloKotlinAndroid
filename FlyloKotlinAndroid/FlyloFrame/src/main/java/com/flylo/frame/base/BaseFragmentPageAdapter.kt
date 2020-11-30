package com.flylo.frame.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.base
 * @ClassName:      BaseFragmentPageAdapter
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:29 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:29 PM
 * @UpdateRemark:
 * @Version:
 */
class BaseFragmentPageAdapter(
    fm: FragmentManager?,
    fgms: List<Fragment>
) :
    FragmentPagerAdapter(fm!!,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private val fgms: List<Fragment>

    override fun getCount(): Int {
        return fgms.size
    }

    override fun getItem(position: Int): Fragment {
        return fgms[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    init {
        this.fgms = fgms
    }
}