package br.com.eduardo.calculaflex.utils

import android.app.Activity
import br.com.eduardo.calculaflex.*

class ScreenMap {

    companion object {
        val SCREEN_NOT_TRACKING = "SCREEN_NOT_TRACKING"

        private fun getScreenNameBy(className:String): String {
            val screensNames = getScreenNames()
            return if (screensNames[className] == null) SCREEN_NOT_TRACKING else screensNames[className]!!
        }

        fun getScreenNameBy(activity: Activity): String {
            return getScreenNameBy(activity::class.java.simpleName)
        }

        fun getClassName(screenName: String): String {
            val screenNames = getScreenNames()

            for(o in screenNames.keys) {
                if(screenNames[o] == screenName) {
                    return o
                }
            }
            return ""
        }

        private fun getScreenNames():Map<String,String> {
            return mapOf(
                Pair(FormActivity::class.java.simpleName, "Cadastro_Formulario"),
                Pair(LoginActivity::class.java.simpleName, "Login_Usuario"),
                Pair(ResultActivity::class.java.simpleName, "Calculo_Resultado"),
                Pair(SignUpActivity::class.java.simpleName, "Criacao_Usuario"),
                Pair(SplashActivity::class.java.simpleName, "Splash")
            )
        }
    }
}