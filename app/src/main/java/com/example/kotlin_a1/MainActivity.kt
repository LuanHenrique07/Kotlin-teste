package com.example.kotlin_a1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.kotlin_a1.Domain.GeolocalizacaoAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TelaGeolocalizacaoIP()
            }
        }
    }

    @Composable
    fun TelaGeolocalizacaoIP() {
        var enderecoIP by remember { mutableStateOf(TextFieldValue("")) }
        var textoResultado by remember { mutableStateOf(AnnotatedString("Informações do IP serão exibidas aqui")) }
        val keyboardController = LocalSoftwareKeyboardController.current // Controlador do teclado

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Consulta de Geolocalização por IP",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Digite um endereço IP para consultar informações geográficas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = enderecoIP.text,
                    onValueChange = { novoValor -> enderecoIP = formatarEntradaIP(TextFieldValue(novoValor)) },
                    label = { Text("Endereço IP") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (enderecoIP.text.isNotBlank()) {
                            keyboardController?.hide() // Fecha o teclado
                            buscarGeolocalizacaoIP(enderecoIP.text) { resultado ->
                                textoResultado = resultado
                            }
                        }
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (enderecoIP.text.isNotBlank()) {
                            keyboardController?.hide() // Fecha o teclado
                            buscarGeolocalizacaoIP(enderecoIP.text) { resultado ->
                                textoResultado = resultado
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Buscar Geolocalização", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = textoResultado,
                        style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }


    private fun buscarGeolocalizacaoIP(ip: String, onResult: (AnnotatedString) -> Unit) {
        val servicoGeolocalizacao = GeolocalizacaoAPI()

        lifecycleScope.launch {
            try {
                val resultado = servicoGeolocalizacao.obterInformacoesIP(ip)
                if (resultado != null) {
                    val informacoes = AnnotatedString.Builder().apply {
                        appendNegrito("IP: "); append("${resultado.ip}\n")
                        appendNegrito("Nome do Host: "); append("${resultado.hostname ?: "Desconhecido"}\n")
                        appendNegrito("Código do Continente: "); append("${resultado.continent_code ?: "Desconhecido"}\n")
                        appendNegrito("Nome do Continente: "); append("${resultado.continent_name ?: "Desconhecido"}\n")
                        appendNegrito("Código do País: "); append("${resultado.country_code2 ?: "Desconhecido"}\n")
                        appendNegrito("Estado: "); append("${resultado.state_prov ?: "Desconhecido"}\n")
                        appendNegrito("Cidade: "); append("${resultado.city ?: "Desconhecida"}\n")
                        appendNegrito("Latitude: "); append("${resultado.latitude ?: "Desconhecida"}\n")
                        appendNegrito("Longitude: "); append("${resultado.longitude ?: "Desconhecida"}\n")
                        appendNegrito("Fuso Horário: "); append("${resultado.fusoHorario ?: "Desconhecido"}\n")
                        appendNegrito("Provedor (ISP): "); append("${resultado.isp ?: "Desconhecido"}\n")
                        appendNegrito("Organização: "); append("${resultado.organization ?: "Desconhecida"}\n")
                      }.toAnnotatedString()
                    onResult(informacoes)
                } else {
                    onResult(AnnotatedString("Erro ao buscar informações de geolocalização."))
                }
            } catch (e: Exception) {
                onResult(AnnotatedString("Erro: ${e.message}"))
            }
        }
    }

    private fun AnnotatedString.Builder.appendNegrito(texto: String) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(texto)
        pop()
    }

    private fun formatarEntradaIP(entrada: TextFieldValue): TextFieldValue {
        val digitos = entrada.text.filter { it.isDigit() || it == '.' }
        val segmentos = digitos.split(".").take(4) // Máximo de 4 segmentos para IP
        val textoFormatado = segmentos.joinToString(".").take(15)
        val novaPosicaoCursor = textoFormatado.length.coerceAtMost(15)

        return TextFieldValue(
            text = textoFormatado,
            selection = androidx.compose.ui.text.TextRange(novaPosicaoCursor)
        )
    }
}
