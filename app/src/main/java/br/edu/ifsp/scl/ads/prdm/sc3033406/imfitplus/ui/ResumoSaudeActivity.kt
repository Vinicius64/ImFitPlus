package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityResumoSaudeBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.repository.UserRepository
import java.util.Locale

class ResumoSaudeActivity : AppCompatActivity() {

    private val arsb: ActivityResumoSaudeBinding by lazy { ActivityResumoSaudeBinding.inflate(layoutInflater) }

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(arsb.root)

        repo = UserRepository(this)

        val user = intent.getParcelableExtra<User>("user") ?: run {
            finish()
            return
        }

        setData(user)
        carregarHistorico()

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

    private fun carregarHistorico() {
        val users = repo.getLastTenHistory()
        val container = arsb.historyContainer
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)


        for (u in users) {
            val itemBinding = br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ItemHistoryBinding
                .inflate(inflater, container, false)
            val item = itemBinding.root

            itemBinding.nomeHistTv.text = listOfNotNull(u.nome.takeIf { it.isNotBlank() }, u.sobrenome.takeIf { it.isNotBlank() }).joinToString(" ")
            itemBinding.imcTv.text = "IMC: " + (u.imc?.let { String.format(Locale.getDefault(), "%.2f", it) } ?: "-")
            itemBinding.pesoHistTv.text = "Peso: " + (u.peso?.let { "$it kg" } ?: "-")
            itemBinding.categoriaTv.text = "Categoria: " + (u.categoriaImc.ifBlank { "-" })
            itemBinding.gastoTv.text = "Gasto: " + (u.gastoCalorico?.let { String.format(Locale.getDefault(), "%.2f kcal", it) } ?: "-")

            container.addView(item)
        }
    }
}