package com.luckyzyx.luckytool.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Process
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.ActivityMainBinding
import com.luckyzyx.luckytool.utils.data.*
import com.luckyzyx.luckytool.utils.tools.*
import kotlin.system.exitProcess

@Obfuscate
@Suppress("PrivatePropertyName")
open class MainActivity : AppCompatActivity() {
    //检测Prefs状态
    private var isStart = YukiHookAPI.Status.isModuleActive
    private val KEY_PREFIX = MainActivity::class.java.name + '.'
    private val EXTRA_SAVED_INSTANCE_STATE = KEY_PREFIX + "SAVED_INSTANCE_STATE"

    private lateinit var binding: ActivityMainBinding

    private fun newIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }

    private fun newIntent(savedInstanceState: Bundle, context: Context): Intent {
        return newIntent(context).putExtra(EXTRA_SAVED_INSTANCE_STATE, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationFragment()
        initDynamicShortcuts()

        checkActive(isStart)
        if (!isStart) return

        checkPermissions()

    }

    private fun checkPermissions() {
        //所有文件访问权限
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent.setData(Uri.parse("package:$packageName")))
            toast(getString(R.string.all_files_access_permission))
        }
    }

    private fun initDynamicShortcuts() {
        val status = packageManager.getComponentEnabledSetting(
            ComponentName(
                packageName,
                "${packageName}.Hide"
            )
        )
        if (status == 2) return
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val lsposed = ShortcutInfo.Builder(this, "lsposed").apply {
            setShortLabel("LSPosed")
            setIcon(Icon.createWithResource(packageName, R.mipmap.android_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut", "lsposed")
            intent.setClassName(packageName, AliveActivity::class.java.name)
            setIntent(intent)
        }.build()
        val oplusGames = ShortcutInfo.Builder(this, "oplusGames").apply {
            setShortLabel(getAppLabel("com.oplus.games").toString())
            setIcon(Icon.createWithResource(packageName, R.mipmap.oplusgames_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut", "oplusGames")
            intent.setClassName("com.oplus.games", "business.compact.activity.GameBoxCoverActivity")
            setIntent(intent)
        }.build()
        val chargingTest = ShortcutInfo.Builder(this, "chargingTest").apply {
            setShortLabel(getString(R.string.charging_test))
            setIcon(
                Icon.createWithResource(
                    packageName,
                    R.drawable.ic_baseline_charging_station_24
                )
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut", "chargingTest")
            intent.setClassName(packageName, AliveActivity::class.java.name)
            setIntent(intent)
        }.build()
        val processManager = ShortcutInfo.Builder(this, "processManager").apply {
            setShortLabel(getString(R.string.process_manager))
            setIcon(Icon.createWithResource(packageName, R.mipmap.android_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut", "processManager")
            intent.setClassName(packageName, AliveActivity::class.java.name)
            setIntent(intent)
        }.build()
        shortcutManager.dynamicShortcuts = listOf(lsposed, oplusGames, chargingTest, processManager)
    }

    private fun initNavigationFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_other,
            R.id.nav_function,
            R.id.nav_home,
            R.id.nav_log,
            R.id.nav_setting,
        ).build()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val bottomNavigationView = binding.navView
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun checkTheme() {
        if (ThemeUtils(this).isDynamicColor()) {
            DynamicColors.applyToActivityIfAvailable(this)
        }
        val themeMode = getString(ModulePrefs, "dark_theme", "MODE_NIGHT_FOLLOW_SYSTEM")
        ThemeUtils(this).initTheme(themeMode)
    }

    private fun checkActive(status: Boolean) {
        if (status) return
        MaterialAlertDialogBuilder(this).apply {
            setCancelable(false)
            setMessage(getString(R.string.unsupported_xposed))
            setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                exitProcess(0)
            }
            //setNegativeButton(R.string.ignore, null)
        }.show()
    }

    fun restart() {
        if (SDK >= A12 || !Process.isApplicationUid(Process.myUid())) {
            recreate()
        } else {
            try {
                val savedInstanceState = Bundle()
                onSaveInstanceState(savedInstanceState)
                finish()
                startActivity(newIntent(savedInstanceState, this))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } catch (e: Throwable) {
                recreate()
            }
        }
    }

    fun restartScope(context: Context) {
        val xposedScope = resources.getStringArray(R.array.xposed_scope)
        val commands = ArrayList<String>()
        for (scope in xposedScope) {
            if (scope == "android") continue
            if (scope == "com.android.systemui") {
                commands.add("kill -9 `pgrep systemui`")
                continue
            }
            commands.add("killall $scope")
            commands.add("am force-stop $scope")
            context.getAppVersion(scope)
        }
        safeOfNull {
            if (getBoolean(ModulePrefs, "statusbar_clock_enable", false) && getBoolean(
                    ModulePrefs, "statusbar_clock_show_second", false
                )
            ) {
                commands.add("settings put secure clock_seconds 1")
            } else {
                commands.add("settings put secure clock_seconds 0")
            }
        }
        MaterialAlertDialogBuilder(context)
            .setMessage(getString(R.string.restart_scope_message))
            .setPositiveButton(getString(android.R.string.ok)) { _: DialogInterface?, _: Int ->
                modulePrefs.clearCache()
                ShellUtils.execCommand(commands, true)
            }
            .setNeutralButton(getString(android.R.string.cancel), null)
            .show()
    }
}