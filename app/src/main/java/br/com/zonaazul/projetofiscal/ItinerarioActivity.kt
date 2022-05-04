package br.com.zonaazul.projetofiscal

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

class ItinerarioActivity : AppCompatActivity() {

    var bd: FirebaseFirestore? = null

    private lateinit var btnBack: MaterialButton
    private lateinit var btnOpenMap: MaterialButton
    private lateinit var btnIniciarIt: MaterialButton
    private lateinit var etRota: EditText
    private lateinit var tvPeriodo: TextView
    private lateinit var tvRotaDisp: TextView

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val notificationID = 101
    private val description = "Test notification"

    @SuppressLint("RemoteViewLayout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerario)

        bd = FirebaseFirestore.getInstance()

        tvPeriodo = findViewById(R.id.tvPeriodo)
        tvRotaDisp = findViewById(R.id.tvRotaDisp)

        etRota = findViewById(R.id.etRota)
        btnIniciarIt = findViewById(R.id.btnIniciarIt)

        btnBack = findViewById(R.id.btnBack)
        btnOpenMap = findViewById(R.id.btnOpenMap)

        createNotification()
        rotasDisp()


        btnBack.setOnClickListener {
            backPage()
        }
        btnOpenMap.setOnClickListener {
            openMap()
        }

        btnIniciarIt.setOnClickListener {
            iniciarItinerario(etRota.text.toString())

        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun sendNotification() {

        val intent = Intent(this, ItinerarioActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        builder = Notification.Builder(this, channelId)
            .setContentTitle("Itinerário Iniciado")
            .setContentText("Sua Rota: ${tvRotaDisp.text}\nPeríodo da Rota: ${tvPeriodo.text}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
            .setContentIntent(pendingIntent)
        notificationManager.notify(notificationID, builder.build())
    }

    private fun sendNotification2() {

        val intent = Intent(this, ItinerarioActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        builder = Notification.Builder(this, channelId)
            .setContentTitle("Itinerário Finalizado")
            .setContentText("Você completou a 1 hora da Rota. Inicie outra.\n")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
            .setContentIntent(pendingIntent)
        notificationManager.notify(notificationID, builder.build())
    }

    private fun backPage() {
        val back = Intent(this, HomePageActivity::class.java)
        startActivity(back)
    }

    private fun openMap() {
        val map = Intent(this, MapsActivity::class.java)
        startActivity(map)
    }

    fun rotasDisp(){
        bd!!.collection("MapsItinerário")
            .whereEqualTo("Status", 0)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val rota = document.getString("Nome")

                    tvRotaDisp.setText("$rota")
                }
            }
    }

    fun fimRota(rotaDigitada: String){

        bd!!.collection("MapsItinerário")
            .whereEqualTo("Nome", rotaDigitada)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){

                    val atualiza2 = hashMapOf(
                        "Status" to 0
                    )

                    val delay = 20000L // delay de 20 seg.

                    val interval = 20000L // intervalo de 20 seg.

                    val timer = Timer()

                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            sendNotification2()

                            bd!!.collection("MapsItinerário").document(document.id)
                                .set(atualiza2, SetOptions.merge())
                        }
                    }, delay, interval)
                }
            }
    }


    fun iniciarItinerario(rotaDigitada: String) {

        if (etRota.text.isNullOrEmpty()) {
            Snackbar.make(
                btnBack,
                "Campo está vazio. Informe a rota para prosseguir.",
                Snackbar.LENGTH_LONG
            ).show()
        }else {
            if (etRota.text.toString().equals(tvRotaDisp.text.toString())){
                bd!!.collection("MapsItinerário")
                    .whereEqualTo("Nome", rotaDigitada)
                    .get()
                    .addOnSuccessListener { result ->

                        for (document in result) {

                            val status = document.getLong("Status")!!.toInt()
                            if (status == 1) {
                                Snackbar.make(
                                    btnBack,
                                    "Está rota ja está em processo.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } else {
                                if (document.getString("Nome").equals(rotaDigitada)) {
                                    Toast.makeText(
                                        this,
                                        "Itinerário Iniciado",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val atualiza = hashMapOf(
                                        "Status" to 1
                                    )

                                    bd!!.collection("MapsItinerário").document(document.id)
                                        .set(atualiza, SetOptions.merge())


                                    val periodo = document.getString("Periodo")

                                    tvPeriodo.setText("$periodo")

                                    val intent = Intent(this, StatusVeiculoActivity::class.java)
                                    startActivity(intent)

                                    sendNotification()
                                    fimRota(etRota.text.toString())

                                } else {
                                    Snackbar.make(
                                        btnBack,
                                        "Erro ao iniciar itinerário.",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }.addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error", exception)
                        Snackbar.make(
                            btnBack,
                            "Erro com o banco de dados.",
                            Snackbar.LENGTH_LONG
                        ).show()

                    }
            }else{
                Snackbar.make(
                    btnBack,
                    "Rota já em processo ou digite corretamente..",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
