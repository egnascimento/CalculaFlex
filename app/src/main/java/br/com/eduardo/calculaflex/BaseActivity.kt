package br.com.eduardo.calculaflex

import android.support.v7.app.AppCompatActivity
import br.com.eduardo.calculaflex.utils.CalculaFlexTracker
import br.com.eduardo.calculaflex.utils.ScreenMap

open class BaseActivity: AppCompatActivity() { //Qual a diferenca entre open class e class? Open class pode ser herdada. por exemplo, AppCompatActivity deve ser open class

    open fun getScreenName(): String {
        return ScreenMap.Companion.getScreenNameBy(this)
    }

    override fun onStart() {
        super.onStart()
        CalculaFlexTracker.trackScreen(this, getScreenName())
    }
}