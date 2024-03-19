package com.bitspan.bitsjobkaro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.databinding.ActivityJobmainBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import com.bitspan.bitsjobkaro.ui.librariesImplementation.meowBottomNav.MeowBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class JobMainActivity : AppCompatActivity() {


    private val recViewModel: RecruiterViewModel by viewModels()
    private lateinit var binding: ActivityJobmainBinding
    private lateinit var navController: NavController


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a  = DataBindingUtil.setContentView(this, R.layout.activity_jobmain)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


        val navBuilder = NavOptions.Builder().setEnterAnim(R.anim.nav_enter_anim).setExitAnim(R.anim.nav_exit_anim).setPopEnterAnim(R.anim.nav_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_pop_exit_anim).build()


        binding.mainBottomNav.setOnClickMenuListener  { item ->
            if (userType) popEmpBackStack(item.id == R.id.recruiterHomeFragment)
            else popRecBackStack(item.id == R.id.recruiterHomeFragment)
            when (item.id) {
                R.id.recruiterHomeFragment -> {
                    if (userType) navController.navigate(R.id.homeFragmentSeeker, null, navBuilder)
                    else navController.navigate(R.id.recruiterHomeFragment,null, navBuilder)
                }
                R.id.recruiterInteractedFragment -> {
                    if (userType) navController.navigate(R.id.appliedJobFragment,null, navBuilder)
                    else navController.navigate(R.id.recruiterInteractedFragment,null, navBuilder)
                }
                R.id.chatFragment -> {
                    navController.navigate(R.id.chatCompanyFragment,null, navBuilder)
                }
                R.id.recruiterProfileFragment -> {
                    if (userType) navController.navigate(R.id.profileFragment,null, navBuilder)
                    else navController.navigate(R.id.recruiterProfileFragment,null, navBuilder)
                }
            }
            true
        }

//        val currentTime = System.currentTimeMillis()
//        val data = Data.Builder().putInt(NotifyWork.NOTIFICATION_ID, 0).build()
//        Log.d("Devashish","Note3"+currentTime)
//        scheduleNotification(data)


        lifecycleScope.launch {
            recViewModel.getStates()
            recViewModel.getDistrict()
        }

    }

    fun setUpBottomNavMenu() {
        binding.mainBottomNav.add(MeowBottomNavigation.Model(R.id.recruiterHomeFragment, R.drawable.icon_home, "Home"))
        binding.mainBottomNav.add(MeowBottomNavigation.Model(R.id.recruiterInteractedFragment, R.drawable.icon_job_applied, "Saved"))
        binding.mainBottomNav.add(MeowBottomNavigation.Model(R.id.chatFragment, R.drawable.icon_chat, "Chat"))
        binding.mainBottomNav.add(MeowBottomNavigation.Model(R.id.recruiterProfileFragment, R.drawable.icon_profile, "Profile"))

    }

    fun showHomeNav(id: Int) {
        binding.mainBottomNav.show(id, false)
    }

    fun askNotifPermission() {
        askNotificationPermission()
    }

    fun bottomNavBarVisibility(visibility: Int) {
        binding.mainBottomNav.visibility = visibility
    }


    fun popEmpBackStack(include: Boolean) {
        navController.popBackStack(R.id.homeFragmentSeeker, include)
    }

    fun popRecBackStack(include: Boolean) {
        navController.popBackStack(R.id.recruiterHomeFragment, include)
    }


//    private fun scheduleNotification(data: Data) {
//        Log.d("Devashish","Note1")
//        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
//            .setInitialDelay(5000,TimeUnit.MILLISECONDS).setInputData(data).build()
//
//        val instanceWorkManager = WorkManager.getInstance(this)
//        instanceWorkManager.beginUniqueWork(
//            NotifyWork.NOTIFICATION_WORK,
//            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
//        Log.d("Devashish","Note2")
//    }


}