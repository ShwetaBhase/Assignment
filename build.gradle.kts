plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.example.demoproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.demoproject"
        minSdk = 26
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
}

dependencies {
    implementation ("com.android.tools.build:gradle:8.3.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-animation:1.0.0-rc01")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.databinding:databinding-runtime:8.3.0")
    implementation("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.retrofit2:adapter-rxjava:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    //room
    implementation ("androidx.arch.persistence.room:runtime:1.1.1")
    annotationProcessor("androidx.arch.persistence.room:compiler:1.1.1")
    testImplementation ("androidx.arch.persistence.room:testing:1.1.1")
//    debugImplementation ("com.amitshekhar.android:debug-db:1.0.4")

    //view model & live data normal
    implementation ("androidx.arch.lifecycle:extensions:1.1.1")
    implementation ("androidx.arch.lifecycle:viewmodel:1.1.1")
    annotationProcessor("androidx.arch.lifecycle:compiler:1.1.1")
//if using java 8,ignore above line and add the Ffollowing line
    implementation ("androidx.arch.lifecycle:common-java8:1.1.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")

    // rxjava and rxandroid
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")


    implementation  ("com.github.bumptech.glide:glide:4.16.0")

//    annotationProcessor  ("com.github.bumptech.glide:compiler:4.11.0’

}