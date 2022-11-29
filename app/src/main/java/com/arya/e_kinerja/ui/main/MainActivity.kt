package com.arya.e_kinerja.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arya.e_kinerja.R
import com.arya.e_kinerja.databinding.ActivityMainBinding
import com.arya.e_kinerja.databinding.NavHeaderMainBinding
import com.arya.e_kinerja.receiver.AlarmReceiver
import com.arya.e_kinerja.utils.gone
import com.arya.e_kinerja.utils.visible
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController
    private lateinit var navHeaderView: View
    private lateinit var navHeaderMainBinding: NavHeaderMainBinding

    private lateinit var alarmReceiver: AlarmReceiver

    var onFabClick: (() -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        navHeaderView = navView.getHeaderView(0)
        navHeaderMainBinding = NavHeaderMainBinding.bind(navHeaderView)

        viewModel.getSession().observe(this) { session ->
            setUpNavDrawer(session.level.toString())
            setUpNavHeader(session.nip.toString(), session.namaJabatan.toString())
        }

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.appBarMain.toolbar.gone()
                    binding.appBarMain.fab.gone()
                }
                R.id.tugasAktivitasFragment, R.id.laporanAktivitasFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.appBarMain.toolbar.visible()
                    binding.appBarMain.fab.visible()
                    setUpFab(destination.id)
                }
                R.id.inputAktivitasFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.appBarMain.toolbar.visible()
                    binding.appBarMain.fab.gone()
                }
                else -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.appBarMain.toolbar.visible()
                    binding.appBarMain.fab.gone()
                }
            }
        }

        binding.appBarMain.fab.setOnClickListener {
            onFabClick?.invoke()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpNavDrawer(level: String) {
        val menuResId = when (level) {
            "1", "2", "3" -> {
                R.menu.activity_main_drawer_lv3
            }
            else -> {
                R.menu.activity_main_drawer_lv4
            }
        }

        navView.menu.clear()
        navView.inflateMenu(menuResId)
        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener { logout() }

        val topLevelDestinationIds = when (level) {
            "1", "2", "3" -> {
                setOf(
                    R.id.dashboardFragment,
                    R.id.tugasAktivitasFragment,
                    R.id.penilaianAktivitasFragment,
                    R.id.laporanAktivitasFragment,
                    R.id.pengaturanFragment
                )
            }
            else -> {
                setOf(
                    R.id.dashboardFragment,
                    R.id.tugasAktivitasFragment,
                    R.id.laporanAktivitasFragment,
                    R.id.pengaturanFragment
                )
            }
        }

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds,
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setUpNavHeader(nip: String, namaJabatan: String) {
        navHeaderMainBinding.tvNip.text = nip
        navHeaderMainBinding.tvNamaJabatan.text = namaJabatan
    }

    private fun setUpFab(id: Int) {
        when (id) {
            R.id.tugasAktivitasFragment -> {
                binding.appBarMain.fab.apply {
                    text = resources.getString(R.string.input_aktivitas)
                    icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_fab_input)
                }
            }
            R.id.laporanAktivitasFragment -> {
                binding.appBarMain.fab.apply {
                    text = resources.getString(R.string.print_aktivitas)
                    icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_fab_print)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun logout(): Boolean {
        alarmReceiver = AlarmReceiver()
        alarmReceiver.cancelReminderAlarm(this)

        viewModel.postNotifikasi(false)
        viewModel.deleteSession()

        navController.navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
        )

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}