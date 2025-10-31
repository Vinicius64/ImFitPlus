package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var nome: String = "",
    var sobrenome: String = "",
    var altura: Int = 170,
    var peso: Int = 70,
    var idade: Int = 0,
    var dataNasc: String = "",
    var sexo: String = "",
    var nivelAtividade: String = "",
    var imc: Double = 0.0,
    var categoriaImc: String = "",
    var pesoIdeal: Double = 0.0,
    var gastoCalorico: Double = 0.0
) : Parcelable