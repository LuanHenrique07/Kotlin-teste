import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GeolocalizacaoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GeolocalizacaoRepository {

    override suspend fun salvarGeolocalizacao(geoIP: GeolocalizacaoIP) {
        try {
            firestore.collection("geolocalizacao_ips")
                .add(geoIP)
                .await() // Aguarda a operação de forma assíncrona
        } catch (e: Exception) {
            throw e // Relança a exceção para o tratamento no ViewModel
        }
    }

    override suspend fun listarGeolocalizacoes(): List<GeolocalizacaoIP> {
        return try {
            val snapshot = firestore.collection("geolocalizacao_ips")
                .get()
                .await() // Aguarda a conclusão da chamada
            snapshot.documents.mapNotNull { it.toObject<GeolocalizacaoIP>() }
        } catch (e: Exception) {
            throw e // Relança a exceção para o tratamento no ViewModel
        }
    }
}
