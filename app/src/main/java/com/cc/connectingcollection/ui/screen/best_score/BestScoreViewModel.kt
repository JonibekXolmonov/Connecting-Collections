package com.cc.connectingcollection.ui.screen.best_score

import androidx.lifecycle.ViewModel
import com.cc.connectingcollection.utils.Contants
import com.cc.connectingcollection.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class BestScoreViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {

     val topScores = flow {
        val topThree = getScores()
        emit(topThree)
    }

    private fun getScores(): List<Int> = listOf(
        sharedPref.getInt(Contants.FIRST),
        sharedPref.getInt(Contants.SECOND),
        sharedPref.getInt(Contants.THIRD),
    )
}