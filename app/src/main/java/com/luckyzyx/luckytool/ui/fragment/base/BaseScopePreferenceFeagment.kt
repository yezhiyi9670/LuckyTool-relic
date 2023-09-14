package com.luckyzyx.luckytool.ui.fragment.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.ThemeUtils
import com.luckyzyx.luckytool.utils.restartScopes
import com.luckyzyx.luckytool.utils.setupMenuProvider

@Suppress("unused")
abstract class BaseScopePreferenceFeagment : ModulePreferenceFragment(), MenuProvider {

    /**
     * 相关作用域
     */
    open val scopes = arrayOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setupMenuProvider(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 是否启用重启菜单
     * @return Boolean
     */
    open fun isEnableRestartMenu(): Boolean = false

    /**
     * 是否启用跳转菜单
     * @return Boolean
     */
    open fun isEnableOpenMenu(): Boolean = false

    /**
     * 自定义跳转菜单点击事件
     * @return Unit
     */
    open fun callOpenMenu() {}

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            isVisible = isEnableRestartMenu()
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            isVisible = isEnableOpenMenu()
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == 1) requireActivity().restartScopes(scopes)
        if (menuItem.itemId == 2) callOpenMenu()
        return true
    }
}