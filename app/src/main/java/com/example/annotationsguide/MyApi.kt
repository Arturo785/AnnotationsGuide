package com.example.annotationsguide

import retrofit2.http.GET


// our api to simulate retrofit
interface MyApi {

    @GET("/users/1")
    suspend fun getUser()

    @GET("/posts/1")
    @Authenticated // this method makes usage of the annotation created
    suspend fun getPost()
}


// our own annotation class, we set the target of it to only work with functions
@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated