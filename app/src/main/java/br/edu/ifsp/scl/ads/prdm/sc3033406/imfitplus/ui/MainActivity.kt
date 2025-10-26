package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(amb.root)

        amb.initBt.setOnClickListener {
            val formIntent = Intent(this, FormActivity::class.java)
            startActivity(formIntent)
        }
    }


}