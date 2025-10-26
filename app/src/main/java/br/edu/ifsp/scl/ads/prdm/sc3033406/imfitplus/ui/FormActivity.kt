package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.ui

    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.graphics.toColor
    import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.databinding.ActivityFormsBinding
    import com.wefika.horizontalpicker.HorizontalPicker

    class FormActivity : AppCompatActivity() {

        private val afv: ActivityFormsBinding by lazy {
            ActivityFormsBinding.inflate(layoutInflater)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(afv.root)

            configurarPicker(afv.alturaHp, 120, 230, 170)
            configurarPicker(afv.pesoHp, 30, 250, 70)
        }

        private fun configurarPicker(picker: HorizontalPicker, min: Int, max: Int, valorInicial: Int) {
            val valores = (min..max).map { it.toString() }.toTypedArray()
            picker.setValues(valores)
            picker.setSelectedItem(valorInicial - min)
        }
    }