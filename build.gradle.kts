// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        // Asegúrate de tener este repositorio MavenCentral
        mavenCentral()
        google()
        // Agrega el repositorio de JitPack.io
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.android.application") version "8.3.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
