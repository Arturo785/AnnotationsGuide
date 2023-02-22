package com.example.annotationsguide

class Validator() {

    /**
     * Return true if every item's property annotated with @Positive is positive and if
     * every item's property annotated with @AllowedNames has a value specified in that annotation.
     */
    fun isValid(item: Item): Boolean {
        val fields = item::class.java.declaredFields

        for (field in fields) {
            field.isAccessible = true

            // all the annotations applied to the object
            for (annotation in field.annotations) {
                val value = field.get(item)

                // the current field is this one?
                if (field.isAnnotationPresent(Positive::class.java)) {
                    val amount = value as Float
                    // because we search for positive values
                    if (amount < 0) {
                        return false
                        // we can also throw exceptions
                        // throw IllegalArgumentException("This is not a positive number")
                    }
                }
                // the current field is this one?
                if (field.isAnnotationPresent(AllowedNames::class.java)) {
                    val allowedNames = field.getAnnotation(AllowedNames::class.java)?.names // our property value
                    val name = value as String
                    allowedNames?.let {
                        if (!it.contains(name)) {
                            return false
                            // we can also throw exceptions
                            // throw IllegalArgumentException("This is not an allowed name")
                        }
                    }
                }

            }
        }
        return true
    }
}
