import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(viewModel: GeolocalizacaoViewModel = hiltViewModel()) {
    val listaDeIps by remember { mutableStateOf(viewModel.listaDeIps) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Lista de IPs
        LazyColumn {
            items(listaDeIps) { ip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "IP: ${ip.ip}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Cidade: ${ip.city ?: "Desconhecida"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Botão para adicionar um IP
        Button(
            onClick = {
                viewModel.adicionarGeolocalizacao(
                    GeolocalizacaoIP(ip = "192.168.0.1", city = "São Paulo")
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Adicionar IP")
        }
    }
}
