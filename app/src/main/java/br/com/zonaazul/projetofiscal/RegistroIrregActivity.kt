package br.com.zonaazul.projetofiscal

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import br.com.zonaazul.projetofiscal.databinding.ActivityRegistroIrregBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class RegistroIrregActivity : AppCompatActivity() {

    var bd: FirebaseFirestore? = null

    private lateinit var binding: ActivityRegistroIrregBinding

    private lateinit var btnOpenCamera: MaterialButton
    private lateinit var btnEnviarIrreg: MaterialButton
    private lateinit var etJustificativa: EditText
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var tvPlacaIrreg: TextView

    var storage: FirebaseStorage? = null

    var REQUEST_TAKE_PHOTO = 1

    lateinit var photoPath: String
    lateinit var photoPath2: String
    lateinit var photoPath3: String
    lateinit var photoPath4: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroIrregBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage
        bd = FirebaseFirestore.getInstance()

        btnOpenCamera = findViewById(R.id.btnOpenCamera)
        btnEnviarIrreg = findViewById(R.id.btnEnviarIrreg)
        etJustificativa = findViewById(R.id.etJustificativa)
        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)
        imageView4 = findViewById(R.id.imageView4)
        tvPlacaIrreg = findViewById(R.id.tvPlacaIrreg)

        val dados = intent.extras
        var placaIrreg = dados?.getString("Placa")

        tvPlacaIrreg.setText(placaIrreg)

        btnOpenCamera.setOnClickListener{
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }

        btnEnviarIrreg.setOnClickListener{
            verificarCampos()

            uploadImg1()
            uploadImg2()
            uploadImg3()
            uploadImg4()
        }
    }

    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                takePicture()
                takePicture2()
                takePicture3()
                takePicture4()
            }else{
                Snackbar.make(binding.root,"Você não concedeu a permissão para usar a câmera", Snackbar.LENGTH_INDEFINITE).show()
            }
        }

   private fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager)!= null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            }catch (e: IOException){}
             if (photoFile != null){

                val photoUri = FileProvider.getUriForFile(
                    this,
                    "br.com.zonaazul.projetofiscal",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun takePicture2(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager)!= null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile2()
            }catch (e: IOException){}
            if (photoFile != null){

                val photoUri = FileProvider.getUriForFile(
                    this,
                    "br.com.zonaazul.projetofiscal",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun takePicture3(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager)!= null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile3()
            }catch (e: IOException){}
            if (photoFile != null){

                val photoUri = FileProvider.getUriForFile(
                    this,
                    "br.com.zonaazul.projetofiscal",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun takePicture4(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager)!= null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile4()
            }catch (e: IOException){}
            if (photoFile != null){

                val photoUri = FileProvider.getUriForFile(
                    this,
                    "br.com.zonaazul.projetofiscal",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            imageView1.setImageURI(Uri.parse(photoPath))
            imageView2.setImageURI(Uri.parse(photoPath2))
            imageView3.setImageURI(Uri.parse(photoPath3))
            imageView4.setImageURI(Uri.parse(photoPath4))
        }
    }

    private fun createImageFile(): File? {
        val fileName = "Photo"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath = image.absolutePath
        return image
    }

    private fun createImageFile2(): File? {
        val fileName = "Photo"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath2 = image.absolutePath
        return image
    }

    private fun createImageFile3(): File? {
        val fileName = "Photo"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath3 = image.absolutePath
        return image
    }

    private fun createImageFile4(): File? {
        val fileName = "Photo"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath4 = image.absolutePath
        return image
    }

    private fun uploadImg1() {
        val bitmap = (imageView1.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        val data = baos.toByteArray()

        val reference = storage!!.reference.child("ImagensVeículos/uploadImg1.jpg")

        var uploadTask = reference.putBytes(data)
    }
    private fun uploadImg2() {
        val bitmap2 = (imageView2.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()

        bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        val data = baos.toByteArray()

        val reference = storage!!.reference.child("ImagensVeículos/uploadImg2.jpg")

        var uploadTask = reference.putBytes(data)
    }

    private fun uploadImg3() {
        val bitmap3 = (imageView3.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()

        bitmap3.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        val data = baos.toByteArray()

        val reference = storage!!.reference.child("ImagensVeículos/uploadImg3.jpg")

        var uploadTask = reference.putBytes(data)
    }

    private fun uploadImg4() {
        val bitmap4 = (imageView4.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()

        bitmap4.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        val data = baos.toByteArray()

        val reference = storage!!.reference.child("ImagensVeículos/uploadImg4.jpg")

        var uploadTask = reference.putBytes(data)
    }


    private fun verificarCampos(){
        if (etJustificativa.text.isNullOrEmpty()){
            Snackbar.make(btnEnviarIrreg, "Preencha todos os campos para poder prosseguir!", Snackbar.LENGTH_LONG).show()
        }else{

            val placa = tvPlacaIrreg.text.toString()

            bd!!.collection("Veículo")
                .whereEqualTo("Placa", placa)
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {

                        val justification = etJustificativa.text.toString()

                        if (document.getString("Placa").equals(placa)){

                            val atualiza = hashMapOf(
                                "Justificativa" to justification
                            )

                            bd!!.collection("Veículo").document(document.id).set(atualiza, SetOptions.merge())

                            Toast.makeText(this, "Irregularidade Enviada!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, StatusVeiculoActivity::class.java)
                            startActivity(intent)


                        }else{
                            Snackbar.make(btnEnviarIrreg, "Erro ao registrar irregularidade", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error", exception)
                    Snackbar.make(btnEnviarIrreg, "Erro com o banco de dados.", Snackbar.LENGTH_LONG).show()

                }
        }
    }

}