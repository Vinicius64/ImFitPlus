package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityImcBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import java.util.Locale

class ImcActivity : AppCompatActivity() {

    private lateinit var carl: ActivityResultLauncher<Intent>
    private val aib: ActivityImcBinding by lazy { ActivityImcBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aib.root)

        val user = intent.getParcelableExtra<User>("user")
        user?.let { mostrarResultadoImc(it) }


        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updated = result.data?.getParcelableExtra<User>("user")
                updated?.let {
                    mostrarResultadoImc(it)
                }
            }
        }

        aib.calcularGastoBt.setOnClickListener {
            val intent = Intent(this, GastoCaloricoActivity::class.java).apply { putExtra("user", user) }
            carl.launch(intent)
        }

        aib.voltarBt.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("user", user))
            finish()
        }
    }

    private fun mostrarResultadoImc(user: User) {
        val alturaM = user.altura / 100.0
        val imc = if (alturaM > 0) user.peso / (alturaM * alturaM) else 0.0
        val imcFormatado = String.format(Locale.getDefault(), "%.2f", imc)
        val categoria = when {
            imc < 18.5 -> "Abaixo do peso"
            imc < 25.0 -> "Normal"
            imc < 30.0 -> "Sobrepeso"
            else -> "Obesidade"
        }

        aib.nomeTv.text = listOfNotNull(user.nome.takeIf { it.isNotBlank() }, user.sobrenome.takeIf { it.isNotBlank() }).joinToString(" ")
        aib.imcResultadoTv.text = imcFormatado
        aib.imcClassificacaoTv.text = categoria
    }
}