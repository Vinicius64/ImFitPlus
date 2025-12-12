package br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.repository

import android.content.ContentValues
import android.content.Context
import br.edu.ifsp.scl.ads.prdm.sc3033406.imfitplus.model.User

class UserRepository(context: Context) {
    private val dbHelper = UserDatabase(context)

    fun insert(user: User): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("nome", user.nome)
            put("sobrenome", user.sobrenome)

            if (user.altura != null) put("altura", user.altura) else putNull("altura")
            if (user.peso != null) put("peso", user.peso) else putNull("peso")
            if (user.idade != null) put("idade", user.idade) else putNull("idade")

            put("dataNasc", user.dataNasc)
            put("sexo", user.sexo)
            put("nivelAtividade", user.nivelAtividade)

            if (user.imc != null) put("imc", user.imc) else putNull("imc")
            put("categoriaImc", user.categoriaImc)
            if (user.pesoIdeal != null) put("pesoIdeal", user.pesoIdeal) else putNull("pesoIdeal")
            if (user.gastoCalorico != null) put("gastoCalorico", user.gastoCalorico) else putNull("gastoCalorico")
        }
        val id = db.insert("user", null, cv)
        db.close()
        return id
    }

    fun update(user: User): Int {
        if (user.id == null) return 0
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("nome", user.nome)
            put("sobrenome", user.sobrenome)

            if (user.altura != null) put("altura", user.altura) else putNull("altura")
            if (user.peso != null) put("peso", user.peso) else putNull("peso")
            if (user.idade != null) put("idade", user.idade) else putNull("idade")

            put("dataNasc", user.dataNasc)
            put("sexo", user.sexo)
            put("nivelAtividade", user.nivelAtividade)

            if (user.imc != null) put("imc", user.imc) else putNull("imc")
            put("categoriaImc", user.categoriaImc)
            if (user.pesoIdeal != null) put("pesoIdeal", user.pesoIdeal) else putNull("pesoIdeal")
            if (user.gastoCalorico != null) put("gastoCalorico", user.gastoCalorico) else putNull("gastoCalorico")
        }
        val rows = db.update("user", cv, "id = ?", arrayOf(user.id.toString()))
        db.close()
        return rows
    }

    fun getById(id: Long): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("user", null, "id = ?", arrayOf(id.toString()), null, null, null)
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                sobrenome = cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                altura = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("altura")),
                peso = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("peso")),
                idade = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("idade")),
                dataNasc = cursor.getString(cursor.getColumnIndexOrThrow("dataNasc")),
                sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo")),
                nivelAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nivelAtividade")),
                imc = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("imc")),
                categoriaImc = cursor.getString(cursor.getColumnIndexOrThrow("categoriaImc")),
                pesoIdeal = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("pesoIdeal")),
                gastoCalorico = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("gastoCalorico"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getLast(): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("user", null, null, null, null, null, "id DESC", "1")
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                sobrenome = cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                altura = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("altura")),
                peso = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("peso")),
                idade = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("idade")),
                dataNasc = cursor.getString(cursor.getColumnIndexOrThrow("dataNasc")),
                sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo")),
                nivelAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nivelAtividade")),
                imc = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("imc")),
                categoriaImc = cursor.getString(cursor.getColumnIndexOrThrow("categoriaImc")),
                pesoIdeal = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("pesoIdeal")),
                gastoCalorico = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("gastoCalorico"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getLastHistory(): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("user_history", null, null, null, null, null, "id DESC", "1")
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                sobrenome = cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                altura = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("altura")),
                peso = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("peso")),
                idade = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("idade")),
                dataNasc = cursor.getString(cursor.getColumnIndexOrThrow("dataNasc")),
                sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo")),
                nivelAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nivelAtividade")),
                imc = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("imc")),
                categoriaImc = cursor.getString(cursor.getColumnIndexOrThrow("categoriaImc")),
                pesoIdeal = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("pesoIdeal")),
                gastoCalorico = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("gastoCalorico"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun addHistory(user: User): Long {

        val last = getLastHistory()

        if (last != null) {
            if (last.nome == user.nome &&
                last.sobrenome == user.sobrenome &&
                last.altura == user.altura &&
                last.peso == user.peso &&
                last.idade == user.idade &&
                last.dataNasc == user.dataNasc &&
                last.sexo == user.sexo &&
                last.nivelAtividade == user.nivelAtividade) {
                return -1
            }
        }

        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("nome", user.nome)
            put("sobrenome", user.sobrenome)

            if (user.altura != null) put("altura", user.altura) else putNull("altura")
            if (user.peso != null) put("peso", user.peso) else putNull("peso")
            if (user.idade != null) put("idade", user.idade) else putNull("idade")

            put("dataNasc", user.dataNasc)
            put("sexo", user.sexo)
            put("nivelAtividade", user.nivelAtividade)

            if (user.imc != null) put("imc", user.imc) else putNull("imc")
            put("categoriaImc", user.categoriaImc)
            if (user.pesoIdeal != null) put("pesoIdeal", user.pesoIdeal) else putNull("pesoIdeal")
            if (user.gastoCalorico != null) put("gastoCalorico", user.gastoCalorico) else putNull("gastoCalorico")
        }
        val id = db.insert("user_history", null, cv)
        db.close()
        return id
    }

    fun getLastTenHistory(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("user_history", null, null, null, null, null, "id DESC", "10")
        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val u = User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    sobrenome = cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                    altura = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("altura")),
                    peso = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("peso")),
                    idade = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("idade")),
                    dataNasc = cursor.getString(cursor.getColumnIndexOrThrow("dataNasc")),
                    sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo")),
                    nivelAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nivelAtividade")),
                    imc = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("imc")),
                    categoriaImc = cursor.getString(cursor.getColumnIndexOrThrow("categoriaImc")),
                    pesoIdeal = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("pesoIdeal")),
                    gastoCalorico = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("gastoCalorico"))
                )
                users.add(u)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }


    private fun android.database.Cursor.getIntOrNull(columnIndex: Int): Int? =
        if (isNull(columnIndex)) null else getInt(columnIndex)

    private fun android.database.Cursor.getDoubleOrNull(columnIndex: Int): Double? =
        if (isNull(columnIndex)) null else getDouble(columnIndex)
}