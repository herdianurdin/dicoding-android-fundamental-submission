package com.saeware.github.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.saeware.github.R
import com.saeware.github.databinding.ActivitySettingsBinding
import com.saeware.github.utils.UIHelper.Companion.toggleDarkMode
import com.saeware.github.utils.ViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getFactory(this)
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsViewModel.getDarkModeSetting().observe(this) {
            toggleDarkMode(it)
            binding.switchDarkMode.isChecked = it
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, checked: Boolean ->
            settingsViewModel.saveDarkModeSetting(checked)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}