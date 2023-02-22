package com.example.annotationsguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.annotationsguide.ui.theme.AnnotationsGuideTheme
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {

    // our api client
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor()) // this is the one we created
                    .addInterceptor(
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    )
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // we call the methods and the getPosts one needs authentication
            // and gets checked because of the interceptor
            api.getPost()
            api.getUser()
        }
    }
}

// our data class that makes usage of our annotation of fields
data class User(
    val name: String,
    @AllowedRegex("\\d{4}-\\d{2}-\\d{2}") val birthDate: String
) {

    init {
        // we get the fields from the class
        val fields = this::class.java.declaredFields
        // we go through every field
        fields.forEach { field ->
            field.annotations.forEach { annotation ->
                if (field.isAnnotationPresent(AllowedRegex::class.java)) {
                    val regex = field.getAnnotation(AllowedRegex::class.java)?.regex
                    if (regex?.toRegex()?.matches(birthDate) == false) {
                        throw IllegalArgumentException(
                            "Birth date is not " +
                                    "a valid date: $birthDate"
                        )
                    }
                }
            }
        }
    }
}

// this annotation only works for Fields
@Target(AnnotationTarget.FIELD)
annotation class AllowedRegex(val regex: String)

// we use the validator from our class
fun validatorsFunction(args: Array<String>) {
    val item = Item(amount = 1.0f, name = "Bob")
    val item2 = Item(amount = -1.0f, name = "Bob")
    val item3 = Item(amount = 5.0f, name = "Mario")
    val validator = Validator()

    val isValid = validator.isValid(item)
}