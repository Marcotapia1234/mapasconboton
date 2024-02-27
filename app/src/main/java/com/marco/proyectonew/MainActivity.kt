package com.marco.proyectonew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Capturamos el botón
        val botonCambio=findViewById<Button>(R.id.botonCambio)
        val botonGasolineras=findViewById<Button>(R.id.botonGasolinera)

        // Cargamos un fragment en el contenedor de fragments
        val fragmen1=Fragmen1()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmen1)
            .commit()

        //Tenemos un botón para alternar en el contededor entre un fragment u otro
        botonCambio.setOnClickListener {
            val fragment1 = if (isFragmen1FragmentVisible()) Fragmen2() else Fragmen1()
            replaceFragment(fragment1)
        }

        botonGasolineras.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,Fragmen4())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isFragmen1FragmentVisible(): Boolean {
        return supportFragmentManager.findFragmentById(R.id.fragment_container) is Fragmen1
    }



    }


