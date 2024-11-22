import com.example.kotlin_a1.di.GeolocalizacaoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGeolocalizacaoRepository(
        repositoryImpl: GeolocalizacaoRepositoryImpl
    ): GeolocalizacaoRepository
}
