package will.denne.artwork.koin

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import will.denne.artwork.data.api.ArticApi
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.data.repository.ArtworkRepositoryImpl
import will.denne.artwork.viewmodel.ArtworkDetailViewModel
import will.denne.artwork.viewmodel.ArtworkViewModel

object KoinModule {
    val appModule = module {
        single<ArtworkRepository> { ArtworkRepositoryImpl(get()) }
        viewModel { ArtworkViewModel(get()) }
        viewModel { (artworkId: Int) ->
            ArtworkDetailViewModel(
                artworkId = artworkId,
                artworkRepository = get()
            )
        }
        single { createRetrofit() }
        single { provideArticApi(get()) }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.artic.edu/")
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()

    private fun provideArticApi(retrofit: Retrofit): ArticApi {
        return retrofit.create(ArticApi::class.java)
    }
}
