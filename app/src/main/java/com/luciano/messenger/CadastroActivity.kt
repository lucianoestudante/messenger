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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.luciano.messenger.databinding.ActivityCadastroBinding
import com.luciano.messenger.model.Usuario

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }
    private val firebaseAutenticacao by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var nome  : String
    private lateinit var email : String
    private lateinit var senha : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener {
           finish()
       }
        inicializarEventosClick()


    }
    private fun inicializarEventosClick() {
        binding.btnCadastrar.setOnClickListener {
//Aqui vai verificar se todos os capos foram preenchidos
            if (validarCampos()){
//Aqui chama a função que vai cadastrar usuario

                cadastrarUsuario( nome, email, senha)

            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {

        firebaseAutenticacao.createUserWithEmailAndPassword( email,senha)
            .addOnCompleteListener { resultado ->
                if ( resultado.isSuccessful ){
//Salvar dados no FireStore  ||||   result captura o que retornou ao salvar usuario
//user(usuario)uad(Id do usuario)
                    val idUsuario = resultado.result.user?.uid
                    if (idUsuario != null){
                        val usuario = Usuario(
                            idUsuario, nome, email
                        )
                        salvarUsuarioFireStore(usuario)
                    }

                }
        }.addOnFailureListener { erro ->
            try {
                throw erro
            }catch (emailInvalido: FirebaseAuthInvalidCredentialsException){
                emailInvalido.printStackTrace()
                Toast.makeText(this, "E-mail inválido, digite corretamente", Toast.LENGTH_SHORT).show()
            }catch (usuarioExistente: FirebaseAuthUserCollisionException){
                usuarioExistente.printStackTrace()
                Toast.makeText(this, "E-mail ja existente", Toast.LENGTH_SHORT).show()
            }catch (senhaFraca:FirebaseAuthWeakPasswordException){
                senhaFraca.printStackTrace()
                Toast.makeText(this, "Senha fraca muito fraca", Toast.LENGTH_SHORT).show()
            }
            
            }

    }

    private fun salvarUsuarioFireStore(usuario: Usuario) {

        firestore
            .collection("usuarios")
            .document(usuario.id)
            .set( usuario )
            .addOnSuccessListener {
                Toast.makeText(this, "Sucesso ao fazer seu cadastro", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this,MainActivity::class.java)
                )

            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao fazer seu cadastro", Toast.LENGTH_SHORT).show()
            }


    }

    private fun validarCampos(): Boolean {
//Aqui recebe os dados digitados pelo usuario
         nome  = binding.edTextNome.text.toString().trim()
         email = binding.edTextEmail.text.toString().trim()
         senha = binding.edTextSenha.text.toString().trim()

        var validar = true

//Aqui limpa os erros
        binding.textInputLayoutNome.error = null
        binding.textInputLayoutEmail.error = null
        binding.textInputLayoutSenha.error = null


        if (nome.isEmpty()){
            binding.textInputLayoutNome.error = "Por favor, preencha o nome"
            validar = false
        }
        if (email.isEmpty()){
            binding.textInputLayoutEmail.error = "Por favor, preencha o e-mail"
            validar = false
        }
        if (senha.isEmpty()){
            binding.textInputLayoutSenha.error = "Por favor, preencha a senha"
            validar = false
        }
        return validar
    }




}