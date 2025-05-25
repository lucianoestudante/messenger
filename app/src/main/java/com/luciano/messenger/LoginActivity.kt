package com.luciano.messenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.luciano.messenger.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val firebaseAutenticacao by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var email : String
    private lateinit var senha : String

    override fun onStart() {
        super.onStart()
        verifivarUsuarioLogado()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //firebaseAutenticacao.signOut()



        binding.textCadastro.setOnClickListener {
            val intent = Intent(this,CadastroActivity::class.java)
            startActivity(intent)
        }
        binding.bynLogar.setOnClickListener {
            if (validarCampos()){
                logarUsuario()
            }
        }
    }

    fun verifivarUsuarioLogado() {
        val usuarioAtual = firebaseAutenticacao.currentUser
        if ( usuarioAtual != null){
            startActivity(
                Intent(this,MainActivity::class.java)
            )
        }

    }

    private fun logarUsuario() {
        firebaseAutenticacao.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            //Aqui quando sucesso ao autenticar
            Toast.makeText(this, "Logado com sucesso", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this,MainActivity::class.java)
            )
        }.addOnFailureListener {erro ->
//Aqui quando erro ao autenticar
            try {
                throw erro
            }catch (emailInvalido: FirebaseAuthInvalidUserException) {
                emailInvalido.printStackTrace()
                Toast.makeText(
                    this,
                    "E-mail inválido, digite corretamente ou faça seu cadastro.",
                    Toast.LENGTH_SHORT
                ).show()
            }catch (userInvalido: FirebaseAuthInvalidCredentialsException) {
                userInvalido.printStackTrace()
                Toast.makeText(
                    this,
                    "E-mail ou senha incorretos!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    private fun validarCampos(): Boolean {
        email = binding.edTextLogEmail.text.toString().trim()
        senha = binding.edTextLogSenha.text.toString().trim()

//Aqui vai ser o retorno da função
        var validar = true

//Aqui limpa os erros dos campos
        binding.textInputLayoutLogEmail.error = null
        binding.textInputLayoutLogSenha.error = null

//Aqui testa se os campos estão vazios
        if (email.isEmpty()){
            binding.textInputLayoutLogEmail.error = "Por favor, preencha o e-mail"
            validar = false
        }
        if (senha.isEmpty()){
            binding.textInputLayoutLogSenha.error = "Por favor, preencha a senha"
            validar = false
        }
        return validar

    }
}