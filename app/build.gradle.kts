

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.kizunachat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kizunachat"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //Para los calendarios:
    implementation("com.applandeo:material-calendar-view:1.9.2")

    //PARA LAS NOTIFICACIONES
    implementation("androidx.core:core:1.7.0")
    implementation("androidx.core:core-ktx:1.7.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    //añado la real time DB
    implementation("com.google.firebase:firebase-database")
    //Añadp storage
    implementation("com.google.firebase:firebase-storage")


    //Imports de xml
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.github.joielechong:countrycodepicker:2.4.2")
    implementation("io.michaelrocks:libphonenumber-android:8.13.28")

    //HTTP
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    //GSON
    implementation("com.google.code.gson:gson:2.10.1")

    //Import GLIDE
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}