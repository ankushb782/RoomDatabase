package simplifiedcoding.net.kotlinretrofittutorial.api

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var YOUR_TOKEN = "BAh7BzoMdXNlcl9pZGkDI0cDOgp0b2tlbkkiGWcyeFppWXZ5cWs5MWNoRnRDRDhUBjoGRVQ=--534703ee364f6efedf1ad934e5c5cd7033ec3632"

    private val AUTH = "courier.${YOUR_TOKEN}"
    private val SubscriptionKEY = ""

    private const val BASE_URL = "http://93.115.20.44:8080/"

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

    val instance: Api by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        retrofit.create(Api::class.java)
    }

}