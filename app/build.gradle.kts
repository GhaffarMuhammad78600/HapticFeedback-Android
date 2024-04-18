plugins {
    id("com.android.application")
}

android {
    namespace = "com.app.hepticfeedback"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.hepticfeedback"
        minSdk = 23
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


    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //text
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

    //exoplayer
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    //lottie
    implementation ("com.airbnb.android:lottie:6.4.0")

    //gif reader
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.23")



}