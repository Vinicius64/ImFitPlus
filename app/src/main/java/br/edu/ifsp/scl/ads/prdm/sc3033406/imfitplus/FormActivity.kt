package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityFormsBinding

class FormActivity : AppCompatActivity() {
    private val afv: ActivityFormsBinding by lazy {
        ActivityFormsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(afv.root)
    }
}