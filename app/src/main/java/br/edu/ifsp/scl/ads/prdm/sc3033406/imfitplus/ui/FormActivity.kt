package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

import java.time.LocalDate
import java.time.Period
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityFormsBinding
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.repository.UserRepository
import com.wefika.horizontalpicker.HorizontalPicker
import kotlin.compareTo

class FormActivity : AppCompatActivity() {

    private lateinit var carl: ActivityResultLauncher<Intent>

    private val afv: ActivityFormsBinding by lazy {
        ActivityFormsBinding.inflate(layoutInflater)
    }

    private lateinit var repo: UserRepository

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(afv.root)

        repo = UserRepository(this)

        configurarPicker(afv.alturaHp, 120, 230, 170)
        configurarPicker(afv.pesoHp, 30, 250, 70)

        repo.getLast()?.let { last ->
            editingId = last.id
            preencherCampos(last)
        }

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updated = result.data?.getParcelableExtra<User>("user")
                updated?.let {
                    preencherCampos(it)
                    if (it.id != null) editingId = it.id
                }
            }
        }

        afv.calcularBt.setOnClickListener { enviarParaImc() }
    }

    override fun onPause() {
        super.onPause()
        val nome = afv.nomeEt.text.toString()
        if (nome.isNullOrBlank()) return

        val dia = afv.dataNascDp.dayOfMonth
        val mes = afv.dataNascDp.month
        val ano = afv.dataNascDp.year
        val dataIso = String.format("%04d-%02d-%02d", ano, mes + 1, dia)

        val nivel = try {
            afv.nivelAtividadeSp.selectedItem?.toString() ?: ""
        } catch (_: Exception) {
            ""
        }

        val draft = User(
            id = editingId,
            nome = nome,
            sobrenome = afv.sobrenomeEt.text.toString(),
            altura = 120 + afv.alturaHp.getSelectedItem(),
            peso = 30 + afv.pesoHp.getSelectedItem(),
            idade = calcularIdade(ano, mes, dia),
            dataNasc = dataIso,
            sexo = when (afv.sexoRg.checkedRadioButtonId) {
                afv.masculinoRb.id -> "Masculino"
                afv.femininoRb.id  -> "Feminino"
                else -> ""
            },
            nivelAtividade = nivel
        )
        if (editingId == null) {
            val id = repo.insert(draft)
            if (id > 0) editingId = id
        } else {
            repo.update(draft)
        }
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
        val dataIso = String.format("%04d-%02d-%02d", ano, mes + 1, dia)

        val nivel = try {
            afv.nivelAtividadeSp.selectedItem?.toString() ?: ""
        } catch (_: Exception) {
            ""
        }

        val user = User(
            id = editingId,
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
            idade = calcularIdade(ano,mes,dia),
            dataNasc = dataIso,
            nivelAtividade = nivel
        )
        if (editingId == null) {
            val id = repo.insert(user)
            if (id > 0) {
                editingId = id
                Toast.makeText(this, "Usuário salvo com id $id", Toast.LENGTH_SHORT).show()
                val userWithId = user.copy(id = id)
                val intent = Intent(this, ImcActivity::class.java).apply { putExtra("user", userWithId) }
                carl.launch(intent)
            } else {
                Toast.makeText(this, "Erro ao salvar usuário", Toast.LENGTH_SHORT).show()
            }
        } else {
            val rows = repo.update(user)
            if (rows > 0) {
                Toast.makeText(this, "Usuário atualizado", Toast.LENGTH_SHORT).show()
                val userWithId = user.copy(id = editingId)
                val intent = Intent(this, ImcActivity::class.java).apply { putExtra("user", userWithId) }
                carl.launch(intent)
            } else {
                Toast.makeText(this, "Erro ao atualizar usuário", Toast.LENGTH_SHORT).show()
            }
        }
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
        user.altura?.let { altura ->
            afv.alturaHp.setSelectedItem(altura - 120)
        }
        user.peso?.let { peso ->
            afv.pesoHp.setSelectedItem(peso - 30)
        }

        user.dataNasc?.takeIf { it.length >= 10 }?.let { iso ->
            try {
                val parts = iso.substring(0,10).split("-")
                val y = parts[0].toInt()
                val m = parts[1].toInt() - 1
                val d = parts[2].toInt()
                afv.dataNascDp.updateDate(y, m, d)
            } catch (_: Exception) {}
        }

        when (user.sexo) {
            "Masculino" -> afv.sexoRg.check(afv.masculinoRb.id)
            "Feminino"  -> afv.sexoRg.check(afv.femininoRb.id)
            else -> afv.sexoRg.clearCheck()
        }

        try {
            val target = user.nivelAtividade ?: ""
            val adapter = afv.nivelAtividadeSp.adapter
            var found = false
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i)?.toString() == target) {
                    afv.nivelAtividadeSp.setSelection(i)
                    found = true
                    break
                }
            }
            if (!found && target.isNotBlank()) {
                (adapter as? ArrayAdapter<String>)?.let { arr ->
                    arr.add(target)
                    afv.nivelAtividadeSp.setSelection(arr.count - 1)
                }
            }
        } catch (_: Exception) {
        }
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
