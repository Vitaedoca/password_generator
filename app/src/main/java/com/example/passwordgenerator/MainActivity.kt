package com.example.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvPassword = findViewById<TextView>(R.id.tvPassword)
        val btnGeneratePassword = findViewById<Button>(R.id.btnGeneratePassword)
        val btnCopy = findViewById<Button>(R.id.btnCopy)
        val seekBarLength = findViewById<SeekBar>(R.id.seekBarLength)
        val tvLengthIndicator = findViewById<TextView>(R.id.tvLengthIndicator)

        // Switches
        val switchLowercase = findViewById<Switch>(R.id.switchLowercase)
        val switchUppercase = findViewById<Switch>(R.id.switchUppercase)
        val switchNumbers = findViewById<Switch>(R.id.switchNumbers)
        val switchSpecialChars = findViewById<Switch>(R.id.switchSpecialChars)

        // Comprimento inicial da senha
        var passwordLength = seekBarLength.progress

        // Atualiza o indicador de comprimento quando o SeekBar é ajustado
        seekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                passwordLength = progress
                tvLengthIndicator.text = "$passwordLength"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Gera uma nova senha com as configurações escolhidas ao clicar no botão
        btnGeneratePassword.setOnClickListener {
            val newPassword = generateRandomPassword(
                passwordLength,
                switchLowercase.isChecked,
                switchUppercase.isChecked,
                switchNumbers.isChecked,
                switchSpecialChars.isChecked
            )

            if (newPassword.isNotEmpty()) {
                tvPassword.text = newPassword
            } else {
                Toast.makeText(this, "Selecione pelo menos uma opção de caractere", Toast.LENGTH_SHORT).show()
            }
        }

        // Copia a senha gerada para a área de transferência ao clicar no botão
        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Senha", tvPassword.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Senha copiada!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateRandomPassword(
        length: Int,
        useLowercase: Boolean,
        useUppercase: Boolean,
        useNumbers: Boolean,
        useSpecialChars: Boolean
    ): String {
        // Define os conjuntos de caracteres com base nas opções selecionadas
        var chars = ""
        if (useLowercase) chars += "abcdefghijklmnopqrstuvwxyz"
        if (useUppercase) chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        if (useNumbers) chars += "0123456789"
        if (useSpecialChars) chars += "!@#\$%&*"

        // Verifica se ao menos um conjunto de caracteres foi selecionado
        if (chars.isEmpty()) return ""

        // Gera a senha aleatória com o comprimento especificado
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }
}
