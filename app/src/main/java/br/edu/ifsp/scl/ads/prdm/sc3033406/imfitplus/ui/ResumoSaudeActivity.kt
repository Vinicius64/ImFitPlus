package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityResumoSaudeBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import java.util.Locale

class ResumoSaudeActivity : AppCompatActivity() {

    private val arsb: ActivityResumoSaudeBinding by lazy { ActivityResumoSaudeBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(arsb.root)

        val user = intent.getParcelableExtra<User>("user") ?: run {
            finish()
            return
        }

        setData(user)

        arsb.voltarBt.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("user", user))
            finish()
        }
    }

    private fun setData(user: User){
        arsb.nomeTv.text = listOfNotNull(user.nome.takeIf { it.isNotBlank() }, user.sobrenome.takeIf { it.isNotBlank() }).joinToString(" ")
        arsb.pesoTv.text = String.format(Locale.getDefault(), "%.2f", user.pesoIdeal)
        arsb.categoriaTv.text = user.categoriaImc
        arsb.imcTv.text = String.format(Locale.getDefault(), "%.2f" + " imc", user.imc)
        arsb.gastoCaloricoTv.text = String.format(Locale.getDefault(), "%.2f" + "kcal", user.gastoCalorico)
        arsb.recomendacaoAguaTv.text = String.format(Locale.getDefault(),"%.2f" + " L", (user.peso!!*0.0350))
    }
}