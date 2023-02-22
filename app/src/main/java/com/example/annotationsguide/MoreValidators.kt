package com.example.annotationsguide

// Custom annotations
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Positive

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllowedNames(val names: Array<String>)


// the class to test the annotations
class Item(@Positive val amount: Float, @AllowedNames(["Alice", "Bob"]) val name: String)
