package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabase(context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {
    companion object{
        private const val NAME = "fituserdb"
        private const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, sobrenome TEXT, altura INTEGER, peso INTEGER, idade INTEGER, dataNasc TEXT, sexo TEXT, nivelAtividade TEXT, imc REAL, categoriaImc TEXT, pesoIdeal REAL, gastoCalorico REAL);")
        db.execSQL("CREATE TABLE user_history (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, sobrenome TEXT, altura INTEGER, peso INTEGER, idade INTEGER, dataNasc TEXT, sexo TEXT, nivelAtividade TEXT, imc REAL, categoriaImc TEXT, pesoIdeal REAL, gastoCalorico REAL);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user_event;")
        onCreate(db)
    }
}