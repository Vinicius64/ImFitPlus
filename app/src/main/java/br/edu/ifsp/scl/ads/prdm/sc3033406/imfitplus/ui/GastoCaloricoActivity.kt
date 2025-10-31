package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityGastoCaloricoBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import com.bumptech.glide.Glide
import java.util.Locale

class GastoCaloricoActivity : AppCompatActivity() {
    private val agcb: ActivityGastoCaloricoBinding by lazy { ActivityGastoCaloricoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(agcb.root)

        val user = intent.getParcelableExtra<User>("user") ?: run {
            finish()
            return
        }

        val tmb = calcularTMB(user)
        user.gastoCalorico = tmb
        val tmbFormatado = String.format(Locale.getDefault(), "%.2f", tmb)

        agcb.gastoCaloricoResultadoTv.text = tmbFormatado

        agcb.voltarMainBt.setOnClickListener {
            val resultIntent = Intent().apply { putExtra("user", user) }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        agcb.pesoIdealBt.setOnClickListener {
            val intent = Intent(this, PesoActivity::class.java).apply { putExtra("user", user) }
            startActivity(intent)
        }

        Glide.with(this)
            .asGif()
            .load(R.drawable.fogo)
            .into(agcb.gastoCaloricoImg)
    }

    private fun calcularTMB(user: User): Double {
        val peso = user.peso.toDouble()
        val altura = user.altura.toDouble()
        val age = user.idade
        val sexo = user.sexo.trim().lowercase(Locale.getDefault())

        return if (sexo.startsWith("m")) {
            66.0 + (13.7 * peso) + (5.0 * altura) - (6.8 * age)
        } else {
            655.0 + (9.6 * peso) + (1.8 * altura) - (4.7 * age)
        }
    }
}
