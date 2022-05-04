package br.com.zonaazul.projetofiscal

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class StatusVeiculoActivity : AppCompatActivity() {

    var bd: FirebaseFirestore? = null

    private lateinit var btnBack: MaterialButton
    private lateinit var btnOpenRegister: MaterialButton
    private lateinit var etPlaca: EditText
    private lateinit var btnConsultar: MaterialButton
    private lateinit var tvInfo1: TextView
    private lateinit var tvInfo2: TextView
    private lateinit var tvIrreg: TextView
    private lateinit var tvPlacaDigitada: TextView
    private lateinit var tvHorarioEstadia: TextView
    private lateinit var tvHorarioEntrada: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_veiculo)

        bd = FirebaseFirestore.getInstance()

        btnBack = findViewById(R.id.btnBack)
        btnOpenRegister = findViewById(R.id.btnOpenRegister)
        etPlaca = findViewById(R.id.etPlaca)
        btnConsultar = findViewById(R.id.btnConsultar)
        tvInfo1 = findViewById(R.id.tvInfo1)
        tvInfo2 = findViewById(R.id.tvInfo2)
        tvIrreg = findViewById(R.id.tvIrreg)
        tvPlacaDigitada = findViewById(R.id.tvPlacaDigitada)
        tvHorarioEstadia = findViewById(R.id.tvHorarioEstadia)
        tvHorarioEntrada = findViewById(R.id.tvHorarioEntrada)

        btnBack.setOnClickListener{
            backPage()
        }

        btnOpenRegister.setOnClickListener{
            if(tvPlacaDigitada.text.isNullOrEmpty()) {
                Snackbar.make(btnBack, "Não é possível prosseguir. Nenhuma placa consultada.", Snackbar.LENGTH_LONG)
                    .show()
            }else if (tvInfo1.isVisible){
                Snackbar.make(btnBack, "Não é possível prosseguir. Essa placa está regular.", Snackbar.LENGTH_LONG)
                    .show()
            }else{
                openRegister()
            }
        }

        btnConsultar.setOnClickListener{

            fazerConsulta(etPlaca.text.toString())
        }
    }

    fun fazerConsulta(placaBuscada: String){

        if(etPlaca.text.isNullOrEmpty()){
            Snackbar.make(btnBack, "Campo está vazio. Informe a placa.", Snackbar.LENGTH_LONG).show()

        }else if (etPlaca.text.length< 7 || etPlaca.text.length>7) {
            Snackbar.make(btnBack, "Número de caracteres não correspondem.", Snackbar.LENGTH_LONG)
                .show()

        }else{
            bd!!.collection("Veículo")
                .whereEqualTo("Placa", placaBuscada)
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {

                        if (document.getString("Placa").equals(placaBuscada)){
                            Snackbar.make(btnBack, "Placa Encontrada", Snackbar.LENGTH_LONG).show()

                            val placa = document.getString("Placa")
                            val hrEst = document.getString("Estadia(h)")
                            //val hrEnt = document.getDate("Dia/Hora").toString()
                            val pagamento = document.getLong("Pagamento")!!.toInt()

                            tvPlacaDigitada.setText("$placa")
                            tvHorarioEstadia.setText("$hrEst")

                            //getTimeDate(hrEnt)
                            //tvHorarioEntrada.setText(hrEnt)

                            if (pagamento == 1) {
                                tvInfo2.visibility = View.INVISIBLE
                                tvInfo1.visibility = View.VISIBLE

                                tvIrreg.visibility = View.INVISIBLE

                                btnBack.visibility = View.VISIBLE

                                tvPlacaDigitada.setTextColor(getResources().getColor(R.color.green))
                                tvHorarioEstadia.setTextColor(getResources().getColor(R.color.green))
                                tvHorarioEntrada.setTextColor(getResources().getColor(R.color.green))
                            } else {
                                tvInfo1.visibility = View.INVISIBLE
                                tvInfo2.visibility = View.VISIBLE

                                tvIrreg.visibility = View.VISIBLE

                                btnBack.visibility = View.INVISIBLE

                                tvPlacaDigitada.setTextColor(getResources().getColor(R.color.red))
                                tvHorarioEstadia.setTextColor(getResources().getColor(R.color.red))
                                tvHorarioEntrada.setTextColor(getResources().getColor(R.color.red))
                            }


                            etPlaca.text.clear()

                        }else{
                            Snackbar.make(btnBack, "Placa não encontrada", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error", exception)
                }
        }
    }

    /*
    fun getTimeDate(timestamp: String?): String? {
        return try {
            val netDate = Date(timestamp!!)
            val sfd = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.getDefault())
            sfd.format(netDate)
        } catch (e: Exception) {
            "date"
        }
    }

     */



    private fun backPage(){
        val back = Intent(this, HomePageActivity::class.java)
        startActivity(back)
    }

    private fun openRegister(){
        val openReg = Intent(this, RegistroIrregActivity::class.java)
        openReg.putExtra("Placa", tvPlacaDigitada.text.toString())
        startActivity(openReg)
    }

}


