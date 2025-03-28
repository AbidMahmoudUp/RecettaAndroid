import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "Trnity.ITP.Recetta"
    compileSdk = 34

    defaultConfig {
        applicationId = "Trnity.ITP.Recetta"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    val activity_version = "1.9.3"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2023.09.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")
    implementation("androidx.compose.material3:material3-window-size-class-android:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.52")
    runtimeOnly("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation("androidx.compose.animation:animation:1.7.5")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.36.0")

    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-camera2:1.4.0")
    implementation("androidx.camera:camera-view:1.4.0")
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("androidx.compose.foundation:foundation:1.7.5")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.room:room-runtime:2.5.0")
    implementation ("androidx.room:room-ktx:2.5.1")
    kapt ("androidx.room:room-compiler:2.5.0")



}
kapt{
    correctErrorTypes = true
}