package com.arya.e_kinerja.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import com.arya.e_kinerja.notification.NotificationWorker
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
    private lateinit var notificationWorker: NotificationWorker

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
            setupNavDrawer(session.level.toString())
            setupNavHeader(session.nip.toString(), session.namaJabatan.toString())
        }

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment -> {
                    binding.appBarMain.toolbar.gone()
                    drawerLayout.close()
                }
                else -> {
                    binding.appBarMain.toolbar.visible()
                }
            }
        }

        notificationWorker = NotificationWorker()
        notificationWorker.setRepeatingAlarm(
            this,
            NotificationWorker.TYPE_REPEATING,
            NotificationWorker.MESSAGE_REPEATING
            )
    }

    private fun setupNavDrawer(level: String) {
        val menuResId = if (level == "3") {
            R.menu.activity_main_drawer_lv3
        } else {
            R.menu.activity_main_drawer_lv4
        }

        navView.menu.clear()
        navView.inflateMenu(menuResId)
        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener { logout() }

        val topLevelDestinationIds = if (level == "3") {
            setOf(
                R.id.dashboardFragment,
                R.id.tugasAktivitasFragment,
                R.id.penilaianAktivitasFragment,
                R.id.laporanAktivitasFragment
            )
        } else {
            setOf(
                R.id.dashboardFragment,
                R.id.tugasAktivitasFragment,
                R.id.laporanAktivitasFragment
            )
        }

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds,
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupNavHeader(nip: String, namaJabatan: String) {
        navHeaderMainBinding.tvNip.text = nip
        navHeaderMainBinding.tvNamaJabatan.text = namaJabatan
    }

    private fun logout(): Boolean {
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