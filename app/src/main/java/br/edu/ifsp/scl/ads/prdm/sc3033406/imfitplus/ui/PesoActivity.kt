package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.R
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityPesoBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import java.util.Locale

class PesoActivity : AppCompatActivity() {
    private val apb: ActivityPesoBinding by lazy{
        ActivityPesoBinding.inflate(layoutInflater)
    }
    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(apb.root)

        val user = intent.getParcelableExtra<User>("user")
        user?.let { mostrarResultadoPesoIdeal(it) }

        apb.voltarBt.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("user", user))
            finish()
        }

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updated = result.data?.getParcelableExtra<User>("user")
                updated?.let {
                    mostrarResultadoPesoIdeal(it)
                }
            }
        }

        apb.resumoBt.setOnClickListener {
            val intent = Intent(this, ResumoSaudeActivity::class.java).apply { putExtra("user", user) }
            carl.launch(intent)
        }
    }

    private fun mostrarResultadoPesoIdeal(user: User){
        val altura = user.altura.toDouble() / 100.0
        val pesoIdeal = 22.0 * (altura * altura)
        user.pesoIdeal = pesoIdeal
        val pesoFormatado = String.format(Locale.getDefault(), "%.2f", pesoIdeal)
        apb.nomeTv.text = listOfNotNull(user.nome.takeIf { it.isNotBlank() }, user.sobrenome.takeIf { it.isNotBlank() }).joinToString(" ")
        apb.pesoIdealTv.setText(getString(R.string.peso_ideal, pesoFormatado))
    }
}