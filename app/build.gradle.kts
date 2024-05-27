plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.note"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.note"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("androidx.work:work-runtime-ktx:2.9.0")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("com.google.dagger:hilt-android:2.49")
    implementation ("com.google.accompanist:accompanist-insets:v0.35.0-alpha")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-android-compiler:2.44")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.profileinstaller:profileinstaller:1.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.room:room-rxjava2:2.6.1")
    implementation("androidx.room:room-rxjava3:2.6.1")
    implementation("androidx.room:room-guava:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1") // For Java projects
    implementation ("androidx.room:room-ktx:2.6.1") // Kotlin
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation ("com.google.accompanist:accompanist-insets:0.23.1")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation ("com.google.code.gson:gson:2.10")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.room:room-common:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("com.google.android.engage:engage-core:1.5.1")
    testImplementation("junit:junit:4.13.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-util:1.6.7")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
