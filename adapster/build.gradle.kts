plugins {
    androidLibrary()
}

android {
    namespace = "com.arthurivanets.adapster"
    compileSdk = appConfig.compileSdkVersion

    defaultConfig {
        minSdk = appConfig.minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = appConfig.javaCompatibilityVersion
        targetCompatibility = appConfig.javaCompatibilityVersion
    }
}

dependencies {
    implementation(deps.appCompat)
    implementation(deps.recyclerView)

    testImplementation(deps.jUnit)
    androidTestImplementation(deps.testRunner)
}
