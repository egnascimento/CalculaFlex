package br.com.eduardo.calculaflex

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import br.com.eduardo.calculaflex.model.CarData
import br.com.eduardo.calculaflex.utils.CalculaFlexTracker
import br.com.eduardo.calculaflex.utils.DatabaseUtil
import br.com.eduardo.calculaflex.utils.RemoteConfig
import br.com.eduardo.calculaflex.watchers.DecimalTextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_form.*


class FormActivity : BaseActivity() {

    private lateinit var userId: String         //TODO: Perguntar o q eh lateinit
    private lateinit var mAuth: FirebaseAuth
    private val firebaseReferenceNode = "CarData"

    private val defaultClearValueText = "0.0"

    private fun loadBanner() {
        val loginBanner = RemoteConfig.getFirebaseRemoteConfig()
            .getString("banner_image")

        Picasso.get().load(loginBanner).into(ivBanner)
    }


    private fun sendDataToAnalytics() {
        val bundle = Bundle()

        bundle.putString("EVENT_NAME", "CALCULATION")
        bundle.putDouble("GAS_PRICE", etGasPrice.text.toString().toDouble())
        bundle.putDouble("ETHANOL_PRICE", etEthanolPrice.text.toString().toDouble())
        bundle.putDouble("GAS_AVERAGE", etGasAverage.text.toString().toDouble())
        bundle.putDouble("ETHANOL_AVERAGE", etEthanolAverage.text.toString().toDouble())

        CalculaFlexTracker.trackEvent(this, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        mAuth = FirebaseAuth.getInstance()
        userId = FirebaseAuth.getInstance().currentUser!!.uid //TODO: O que eh !!

        listenerFirebaseRealtime()

        etGasPrice.addTextChangedListener(DecimalTextWatcher(etGasPrice))
        etEthanolPrice.addTextChangedListener(DecimalTextWatcher(etEthanolPrice))
        etGasAverage.addTextChangedListener(DecimalTextWatcher(etGasAverage,1))
        etEthanolAverage.addTextChangedListener(DecimalTextWatcher(etEthanolAverage,1))

        val textView: TextView = findViewById(R.id.header_gas_station) as TextView
        textView.text = RemoteConfig.getFirebaseRemoteConfig()
            .getString("header_text")

        loadBanner()

        btCalculate.setOnClickListener {
            saveCarData()

            val proximatela = Intent(this@FormActivity, ResultActivity::class.java)
            proximatela.putExtra("GAS_PRICE", etGasPrice.text.toString().toDouble())
            proximatela.putExtra("ETHANOL_PRICE", etEthanolPrice.text.toString().toDouble())
            proximatela.putExtra("GAS_AVERAGE", etGasAverage.text.toString().toDouble())
            proximatela.putExtra("ETHANOL_AVERAGE", etEthanolAverage.text.toString().toDouble())

            sendDataToAnalytics()

            startActivity(proximatela)

        }
    }

    private fun saveCarData() {
        val carData = CarData(
            etGasPrice.text.toString().toDouble(),
            etEthanolPrice.text.toString().toDouble(),
            etGasAverage.text.toString().toDouble(),
            etEthanolAverage.text.toString().toDouble()
        )

        FirebaseDatabase.getInstance().getReference(firebaseReferenceNode)
            .child(userId)
            .setValue(carData)
    }

    private fun listenerFirebaseRealtime() {
        DatabaseUtil.getDatabase()
            .getReference(firebaseReferenceNode)
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val carData = dataSnapshot.getValue(CarData::class.java)
                    etGasPrice.setText(carData?.gasPrice.toString())
                    etEthanolPrice.setText(carData?.ethanolPrice.toString())
                    etGasAverage.setText(carData?.gasAverage.toString())
                    etEthanolAverage.setText(carData?.ethanolAverage.toString())
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.form_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            R.id.action_clear -> {
                clearData()
                return true
            }
            R.id.action_logout -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        mAuth.signOut()

        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }

    private fun clearData() {
        etGasPrice.setText(defaultClearValueText)
        etEthanolPrice.setText(defaultClearValueText)
        etGasAverage.setText(defaultClearValueText)
        etEthanolAverage.setText(defaultClearValueText)

    }


}
