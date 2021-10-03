package com.example.portfolio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.portfolio.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var launcher: ActivityResultLauncher<Intent>
    private var launcher2: ActivityResultLauncher<Intent>? = null
    lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityMainBinding
    lateinit var newName: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Привязываю разметку через биндинг
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //для активации гугл входа
        auth = Firebase.auth

        //меняю имя, если оно менялось в настройках
        launcher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                    newName = it.data?.getStringExtra("key3").toString()
                    binding.tvName.text = newName
                    binding.tvHello.visibility = View.VISIBLE
                    binding.tvName.visibility = View.VISIBLE
                    binding.bAct1.text = "Войти"
            }
        }


        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {

                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    firebaseAuthWithGoogle(account.idToken!!)
                }

            }catch (e: ApiException){

            }
        }

        //добавляю имя если зарегистрирован и пишу войти
       val name = auth.currentUser?.displayName

        if(auth.currentUser != null) {
            binding.tvName.text = name
            binding.tvHello.visibility = View.VISIBLE
            binding.tvName.visibility = View.VISIBLE
            binding.bAct1.text = "Войти"
        }

        //Открываю второе активити по нажатию на кнопку регистрации
        binding.bAct1.setOnClickListener {
            signInWithGoogle()
        }




    }
    //для гугл регистрации
    private fun getClient(): GoogleSignInClient {

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    //запуск клиента гугл
    private fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                checkAuthState()
            } else Log.d("MyLog","error")
        }
    }

    //проверять зареган ли я был и открывать новое активити и задать имя
    // с почты или из настроек измененное
    private fun checkAuthState(){
        if(auth.currentUser != null){
            val name1 = auth.currentUser?.displayName
            if (binding.tvName.text.isNullOrEmpty()) {
                binding.tvName.text = name1
                launcher2?.launch(
                    Intent(this, Activity2::class.java)
                        .putExtra("key", "$name1")
                )
            } else if(binding.tvName.text == name1) {
                binding.tvName.text = name1
                launcher2?.launch(
                    Intent(this, Activity2::class.java)
                        .putExtra("key", "$name1"))
            } else{
                binding.tvName.text = newName
                launcher2?.launch(
                    Intent(this, Activity2::class.java)
                        .putExtra("key", "$newName"))
            }
        }

    }
}