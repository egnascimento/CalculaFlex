package br.com.eduardo.calculaflex.utils

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object CalculaFlexTracker {         //Qual a diferenca entre class e object: Object nao precisa ser instanciado como uma classe


    fun trackScreen(activity: Activity, screenName:String) {
        if(screenName != ScreenMap.SCREEN_NOT_TRACKING) {
            val mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity)
            mFirebaseAnalytics.setCurrentScreen(activity, screenName, null)
        }
    }

    fun trackEvent(activity: Activity, bundle: Bundle) {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}