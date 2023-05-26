package com.cc.connectingcollection.ui.screen.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc.connectingcollection.utils.Contants.SOUND
import com.cc.connectingcollection.utils.Contants.VIBRATION
import com.cc.connectingcollection.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {

    private var _vibrationEnabled = mutableStateOf(false)
    val vibrationEnabled get() = _vibrationEnabled

    private var _soundEnabled = mutableStateOf(false)
    val soundEnabled get() = _soundEnabled

    fun controlVibration(isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                sharedPref.saveBoolean(VIBRATION, isEnabled)
                _vibrationEnabled.value = isEnabled
            } catch (e: Exception) {
                //not saved
            }
        }
    }

    private fun getVibration() {
        _vibrationEnabled.value = sharedPref.getBoolean(VIBRATION)
    }

    fun controlSound(isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                sharedPref.saveBoolean(SOUND, isEnabled)
                _soundEnabled.value = isEnabled
            } catch (e: Exception) {
                //not saved
            }
        }
    }

    private fun getSound() {
        _soundEnabled.value = sharedPref.getBoolean(SOUND)
    }

    init {
        viewModelScope.launch {
            getSound()
            getVibration()
        }
    }
}