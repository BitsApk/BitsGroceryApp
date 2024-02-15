package com.bitspanindia.groceryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Grocery: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}