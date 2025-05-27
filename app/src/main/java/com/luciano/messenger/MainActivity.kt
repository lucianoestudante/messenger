package com.luciano.messenger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.luciano.messenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val firebaseAutenticacao by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        inicializarToolbar()


    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeMainToolbar.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Messenger"

        }
        addMenuProvider(
            object  : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate( R.menu.menu_principal, menu )
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId){
                        R.id.item_perfil -> {
                            startActivity(
                                Intent(applicationContext,PerfilActivity::class.java)
                            )

                        }
                        R.id.item_sair -> {
                            deslogarUsuario()

                        }
                    }
                    return true
                }


            }

        )
    }

    private fun deslogarUsuario() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sai?")
            .setNegativeButton("Cancelar") {dialog, posicao ->}
            .setPositiveButton("Sim"){ dialog, posicao ->
                firebaseAutenticacao.signOut()
                startActivity(
                    Intent (applicationContext, LoginActivity::class.java )
                )

            }
            .create()
            .show()
    }
}