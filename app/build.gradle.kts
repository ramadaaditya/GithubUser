plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
apply("../shared_dependencies.gradle")
android {
    namespace = "com.dicoding.githubuser"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.dicoding.githubuser"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        debug {
            isDebuggable = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    dynamicFeatures += setOf(":favorite")
}

dependencies {
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
    implementation(project(":core"))
    implementation("androidx.core:core-splashscreen:1.0.1")
    //noinspection RiskyLibrary
    implementation ("com.google.android.play:core:1.10.3")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
}