package com.example.annotationsguide

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

// And interceptor is something that runs on every request the api client makes
// and modifies the request as we want

// our own custom interceptor, that gets triggered on every http request by the client
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation = chain.request().tag(Invocation::class.java)
            ?: return chain.proceed(chain.request())

        // it's a boolean that checks if we have the annotation in the method called
        // because it's an interceptor every httpCall gets this validation
        val shouldAttachAuthHeader = invocation
            .method()
            .annotations // we check for annotations if available in the case of retrofit because we don't own the class
            .any { it.annotationClass == Authenticated::class } // if we have one

        // if we need authentication we modify the request and add our fake token in this case
        return if (shouldAttachAuthHeader) {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "my token")
                    .build()
            )
        } else chain.proceed(chain.request()) // if not necessary we proceed with the original request
    }
}