package by.slavintodron.babyhelper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.ActivityMainBinding
import by.slavintodron.babyhelper.service.FeedingService
import by.slavintodron.babyhelper.ui.fragment.FeedingTimerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        when (intent.getStringExtra(FeedingService.COMMAND_ID)) {
            FeedingService.COMMAND_STOP -> {
                val startIntent = Intent(this, FeedingService::class.java)
                startIntent.action = FeedingService.COMMAND_STOP
                startIntent.putExtra(FeedingService.COMMAND_ID, FeedingService.COMMAND_STOP)
                startIntent.putExtra(FeedingService.STARTED_TIMER_TIME_MS, 55550545048)
                this.startService(startIntent)
                val x = intent.getLongExtra(FeedingService.LEFT_TIMER, 0)
                val xz = intent.getLongExtra(FeedingService.RIGHT_TIMER, 0)
                val fragment = FeedingTimerFragment.newInstance(
                    x, xz
                )

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit()
            // navHostFragment.navController.navigate(R.id.action_mealsFragment_to_mealsAddFragment, bundle)
            }
        }
    }
}