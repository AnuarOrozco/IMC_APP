package com.anuaro.imcapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anuaro.imcapp.ui.theme.IMCTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMCTheme {
                IMCCalculator()
            }
        }
    }
}

@Composable
fun IMCCalculator() {
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var imcResult by remember { mutableStateOf<Float?>(null) }
    var imcStatus by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Calculadora IMC",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 34.sp,
            color = androidx.compose.ui.graphics.Color(0xFF44D988),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Img custom
        val image: Painter = painterResource(id = R.drawable.imc) // Asegúrate de tener una imagen en tu res/drawable
        Image(
            painter = image,
            contentDescription = "IMC Image",
            modifier = Modifier
                .width(200.dp)
                .height(218.dp)
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Age txt
        Text("Edad:", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        BasicTextField(
            value = age,
            onValueChange = { age = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(Modifier.padding(8.dp)) {
                    if (age.isEmpty()) {
                        Text("Ingresa tu edad en años")
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Weight txt
        Text("Peso (kg):", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        BasicTextField(
            value = weight,
            onValueChange = { weight = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(Modifier.padding(8.dp)) {
                    if (weight.isEmpty()) {
                        Text("Ingresa tu peso en kg")
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Height txt
        Text("Estatura (mts):", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        BasicTextField(
            value = height,
            onValueChange = { height = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(Modifier.padding(8.dp)) {
                    if (height.isEmpty()) {
                        Text("Ingresa tu altura (m)")
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Calculate btn xD
        Button(
            onClick = {
                if (weight.isNotEmpty() && height.isNotEmpty() && age.isNotEmpty()) {
                    val weightValue = weight.toFloatOrNull()
                    val heightValue = height.toFloatOrNull()
                    val ageValue = age.toIntOrNull()

                    if (weightValue != null && heightValue != null && heightValue > 0f && ageValue != null) {
                        imcResult = calculateIMC(weightValue, heightValue)
                        imcStatus = evaluateIMCForAge(imcResult!!, ageValue)
                    } else {
                        imcResult = null
                        imcStatus = ""
                    }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF44D988), // Color de fondo del botón
                contentColor = androidx.compose.ui.graphics.Color.Black // Color del texto
            )
        ) {
            Text("Calcular", fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result
        imcResult?.let {
            Text(text = "Tu IMC es: ${"%.2f".format(it)}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = imcStatus,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                color = if (imcStatus.contains("adecuado")) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
            )
        } ?: run {
            Text(text = "Por favor, ingresa tus datos correctamente.", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer
        Text(
            text = "Desarrollado por:",
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text = "Anuar Avalos Orozco",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

fun calculateIMC(weight: Float, height: Float): Float {
    return weight / (height * height)
}

fun evaluateIMCForAge(imc: Float, age: Int): String {
    return when {
        age <= 18 -> {
            when {
                imc < 18.5 -> "Tu IMC es bajo para tu edad. Consulta a un especialista."
                imc in 18.5..24.9 -> "Tu IMC es adecuado para tu edad."
                imc > 24.9 -> "Tu IMC es alto para tu edad. Consulta a un especialista."
                else -> "IMC no válido."
            }
        }
        else -> {
            when {
                imc < 18.5 -> "Tu IMC es bajo. Es recomendable ganar peso."
                imc in 18.5..24.9 -> "Tu IMC es adecuado."
                imc in 25.0..29.9 -> "Tu IMC está en sobrepeso. Considera un plan de alimentación."
                imc >= 30 -> "Tu IMC es alto. Es recomendable consultar a un médico."
                else -> "IMC no válido."
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIMCCalculator() {
    IMCTheme {
        IMCCalculator()
    }
}
