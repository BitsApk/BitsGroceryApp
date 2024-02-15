plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.bitspanindia.groceryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bitspanindia.groceryapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation
    val navigationVersion = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // For circular image
    implementation("de.hdodenhof:circleimageview:3.1.0")


    // Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0") {
        exclude("com.android.support")
    }
    kapt ("com.github.bumptech.glide:compiler:4.16.0")

    //loading shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // For phone num otp
    implementation ("com.github.aabhasr1:OtpView:v1.1.2-ktx")

//     Hilt dependency
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt ("com.google.dagger:hilt-compiler:2.50")

    //DatStore
    implementation ("androidx.datastore:datastore-preferences-core:1.0.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore:1.0.0")


    val lottieVersion = "5.2.0"
    implementation ("com.airbnb.android:lottie:$lottieVersion")

    //indicator
    implementation ("com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2")

    //photozoom
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")

    //google map sdk
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    //paging library
    val pagingVersion = "3.2.1"

    implementation ("androidx.paging:paging-runtime-ktx:$pagingVersion")
}

kapt {
    correctErrorTypes = true
}