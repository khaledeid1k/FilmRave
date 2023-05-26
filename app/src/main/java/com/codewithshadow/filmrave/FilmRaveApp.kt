package com.codewithshadow.filmrave

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


// Define the FilmRaveApp class that extends the Application class
@HiltAndroidApp
class FilmRaveApp : Application() {

    // Initialize a companion object that holds a reference to the application instance
    companion object {
        // Create a private nullable variable to hold the application instance
        private var application: FilmRaveApp? = null

        // Define a getter method to access the application context
        val context: Context?
            // Use a Kotlin getter to retrieve the application context if it exists
            get() = application
    }

    // Create an init block to initialize the application variable with the current instance
    init {
        application = this
    }
}