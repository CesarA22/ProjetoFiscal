package br.com.zonaazul.projetofiscal

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private lateinit var btnOpenStats: MaterialButton
    private lateinit var btnItinerario: MaterialButton

    private lateinit var auth: FirebaseAuth;

    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()

        btnOpenStats = findViewById(R.id.btnOpenStats)

        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE) 
                as ConnectivityManager

        btnOpenStats.setOnClickListener{
            if (connectivity != null){
                info = connectivity!!.activeNetworkInfo

                if (info != null){
                    if (info!!.state == NetworkInfo.State.CONNECTED){
                        Toast.makeText(this, "Conectado á Internet", Toast.LENGTH_SHORT).show()
                        openStats()
                    }
                }else{
                    Toast.makeText(this, "Falha na conexão com a Internet", Toast.LENGTH_SHORT).show()
                }
            }
            
        }

        btnItinerario = findViewById(R.id.btnItinerario)

        btnItinerario.setOnClickListener{
            if (connectivity != null){
                info = connectivity!!.activeNetworkInfo

                if (info != null){
                    if (info!!.state == NetworkInfo.State.CONNECTED){
                        Toast.makeText(this, "Conectado á Internet", Toast.LENGTH_SHORT).show()
                        openItinerario()
                    }
                }else{
                    Toast.makeText(this, "Falha na conexão com a Internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?){
        if (user == null){
            auth.signInAnonymously().addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Logado Anonimamente", Toast.LENGTH_SHORT).show()
                    val user= auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)
                }
            })
        }
    }

    private fun openStats(){
        val pass = Intent(this, StatusVeiculoActivity::class.java)
        startActivity(pass)
    }

    private fun openItinerario(){
        val pass = Intent(this, ItinerarioActivity::class.java)
        startActivity(pass)
    }

}