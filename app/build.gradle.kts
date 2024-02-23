plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-android")

    //ADD the GOOGLE SERVICES GRADLE PLUGIN
    id("com.google.gms.google-services")
}

android {
    namespace = "pt.ipp.estg.cmu"
    compileSdk = 34

    defaultConfig {
        applicationId = "pt.ipp.estg.cmu"
        minSdk = 29
        targetSdk = 33
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
        tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
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
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))

    // Compose
    val composeVersion = "1.5.4"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$composeVersion")


    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")


    // Core
    implementation("androidx.core:core-ktx:1.12.0")

    // Lifecycle
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-firestore-ktx:24.9.1")

    implementation ("com.google.android.gms:play-services-location:21.0.1")

    //Room
    val roomVersion = "2.6.0"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$roomVersion")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$roomVersion")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$roomVersion")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.4.1")


    // Play Services
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.maps.android:maps-compose:4.3.0")


    // Retrofit
    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Compose Material3
    implementation("androidx.compose.material3:material3:1.2.0-alpha11")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    //Material Components
    implementation("com.google.android.material:material:1.10.0")


    // AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.5.4")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
