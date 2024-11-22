import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeolocalizacaoViewModel @Inject constructor(
    private val repository: GeolocalizacaoRepository
) : ViewModel() {

    val listaDeIps = mutableStateListOf<GeolocalizacaoIP>()

    fun carregarGeolocalizacoes() {
        viewModelScope.launch {
            try {
                val lista = repository.listarGeolocalizacoes()
                listaDeIps.clear()
                listaDeIps.addAll(lista)
            } catch (e: Exception) {
                println("Erro ao carregar geolocalizações: ${e.message}")
            }
        }
    }

    fun adicionarGeolocalizacao(geoIP: GeolocalizacaoIP) {
        viewModelScope.launch {
            try {
                repository.salvarGeolocalizacao(geoIP)
                carregarGeolocalizacoes()
            } catch (e: Exception) {
                println("Erro ao adicionar geolocalização: ${e.message}")
            }
        }
    }
}
