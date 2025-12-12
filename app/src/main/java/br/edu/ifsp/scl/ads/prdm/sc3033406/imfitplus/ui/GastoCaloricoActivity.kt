package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityGastoCaloricoBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.repository.UserRepository
import com.bumptech.glide.Glide
import java.util.Locale

class GastoCaloricoActivity : AppCompatActivity() {
    private val agcb: ActivityGastoCaloricoBinding by lazy { ActivityGastoCaloricoBinding.inflate(layoutInflater) }

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(agcb.root)

        val user = intent.getParcelableExtra<User>("user") ?: run {
            finish()
            return
        }

        val tmb = calcularTMB(user)
        val fmax = calcularFmax(user)

        user.gastoCalorico = tmb
        val tmbFormatado = String.format(Locale.getDefault(), "%.2f", tmb)

        val fmaxFormatado = (fmax).toString()

        repo = UserRepository(this)

        repo.update(user)

        agcb.gastoCaloricoResultadoTv.text = tmbFormatado
        agcb.frequenciaResultadoTv.text = fmaxFormatado

        calcularTaxas(fmax)

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
        val peso = user.peso ?: return 0.0
        val altura = user.altura ?: return 0.0
        val idade = user.idade ?: return 0.0

        val sexo = user.sexo.trim().lowercase(Locale.getDefault())

        return if (sexo.startsWith("m")) {
            66.0 + (13.7 * peso) + (5.0 * altura) - (6.8 * idade)
        } else {
            655.0 + (9.6 * peso) + (1.8 * altura) - (4.7 * idade)
        }
    }

    private fun calcularFmax(user: User): Int {
        val idade = user.idade ?: return 0
        return 220 - idade
    }

    private fun calcularTaxas(fmax: Int){
        val cinco = fmax*0.5
        val seis = fmax*0.6
        val sete = fmax*0.7
        val oito = fmax*0.8
        val nove = fmax*0.9
        agcb.zonaLeve.text = "Zona leve(50-60%): "+ cinco + " - " + seis + " bpm"
        agcb.zonaQueima.text = "Zona Queima de Gordura(60-70%): "+ seis + " - " + sete + " bpm"
        agcb.zonaAerobica.text = "Zona Aeróbica(70-80%): "+ sete + " - " + oito + " bpm"
        agcb.zonaAnaerobica.text = "Zona Anaeróbica(80-90%): "+ oito + " - " + nove + " bpm"
    }
}
