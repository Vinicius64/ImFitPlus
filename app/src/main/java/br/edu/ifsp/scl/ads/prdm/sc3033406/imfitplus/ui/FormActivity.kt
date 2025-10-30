package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import java.time.LocalDate
import java.time.Period
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityFormsBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import com.wefika.horizontalpicker.HorizontalPicker
import kotlin.compareTo

class FormActivity : AppCompatActivity() {

    private lateinit var carl: ActivityResultLauncher<Intent>

    private val afv: ActivityFormsBinding by lazy {
        ActivityFormsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(afv.root)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updated = result.data?.getParcelableExtra<User>("user")
                updated?.let {
                    preencherCampos(it)
                }
            }
        }

        afv.calcularBt.setOnClickListener { enviarParaImc() }

        configurarPicker(afv.alturaHp, 120, 230, 170)
        configurarPicker(afv.pesoHp, 30, 250, 70)
    }

    private fun configurarPicker(picker: HorizontalPicker, min: Int, max: Int, valorInicial: Int) {
        val valores = (min..max).map { it.toString() }.toTypedArray()
        picker.setValues(valores)
        picker.setSelectedItem(valorInicial - min)
    }

    private fun enviarParaImc() {
        if (!validateInputs()) return

        val dia = afv.dataNascDp.dayOfMonth
        val mes = afv.dataNascDp.month
        val ano = afv.dataNascDp.year

        val user = User(
            nome = afv.nomeEt.text.toString(),
            sobrenome = afv.sobrenomeEt.text.toString(),
            altura = 120 + afv.alturaHp.getSelectedItem(),
            peso = 30 + afv.pesoHp.getSelectedItem(),
            sexo = afv.sexoRg.checkedRadioButtonId.let {
                when (it) {
                    afv.masculinoRb.id -> "Masculino"
                    afv.femininoRb.id  -> "Feminino"
                    else -> ""
                }
            },
            idade = calcularIdade(ano,mes,dia)
        )
        val intent = Intent(this, ImcActivity::class.java).apply { putExtra("user", user) }
        carl.launch(intent)
    }

    fun calcularIdade(ano: Int, mesZeroBased: Int, dia: Int): Int {
        return try {
            val nascimento = LocalDate.of(ano, mesZeroBased + 1, dia)
            val hoje = LocalDate.now()
            if (nascimento.isAfter(hoje)) return 0
            Period.between(nascimento, hoje).years
        } catch (_: Exception) {
            0
        }
    }

    private fun preencherCampos(user: User) {
        afv.nomeEt.setText(user.nome)
        afv.sobrenomeEt.setText(user.sobrenome)
        afv.alturaHp.setSelectedItem(user.altura - 120)
        afv.pesoHp.setSelectedItem(user.peso - 30)
    }

    private fun validateInputs(): Boolean {
        var valid = true

        if (afv.nomeEt.text.isNullOrBlank()) {
            afv.nomeEt.error = "Digite o nome"
            afv.nomeEt.requestFocus()
            valid = false
        } else {
            afv.nomeEt.error = null
        }

        if (afv.sobrenomeEt.text.isNullOrBlank()) {
            if (valid) afv.sobrenomeEt.requestFocus()
            afv.sobrenomeEt.error = "Digite o sobrenome"
            valid = false
        } else {
            afv.sobrenomeEt.error = null
        }

        if (afv.sexoRg.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Selecione o sexo", Toast.LENGTH_SHORT).show()
            valid = false
        }

        val dia = afv.dataNascDp.dayOfMonth
        val mes = afv.dataNascDp.month
        val ano = afv.dataNascDp.year
        val idade = calcularIdade(ano, mes, dia)
        if (idade <= 0) {
            Toast.makeText(this, "Data de nascimento inválida", Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }
}
